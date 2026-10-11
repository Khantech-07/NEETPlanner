package com.example.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "neet_reminders_channel"
        const val CHANNEL_NAME = "NEET Study Reminders"

        const val ACTION_TRIGGER = "com.example.neetplanner.ACTION_TRIGGER_REMINDER"
        const val ACTION_MARK_DONE = "com.example.neetplanner.ACTION_MARK_DONE"
        const val ACTION_SNOOZE = "com.example.neetplanner.ACTION_SNOOZE"

        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_TASK_TITLE = "extra_task_title"
        const val EXTRA_TASK_SUBJECT = "extra_task_subject"
        const val EXTRA_TARGET_Q = "extra_target_q"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra(EXTRA_TASK_ID, -1L)
        val taskTitle = intent.getStringExtra(EXTRA_TASK_TITLE) ?: "Study Session"
        val subject = intent.getStringExtra(EXTRA_TASK_SUBJECT) ?: "NEET Prep"
        val targetQ = intent.getIntExtra(EXTRA_TARGET_Q, 0)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        when (intent.action) {
            ACTION_TRIGGER -> {
                createNotificationChannel(notificationManager)

                // Open App Intent
                val openAppIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                val openAppPendingIntent = PendingIntent.getActivity(
                    context,
                    taskId.toInt(),
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Mark as Done Intent
                val doneIntent = Intent(context, ReminderReceiver::class.java).apply {
                    action = ACTION_MARK_DONE
                    putExtra(EXTRA_TASK_ID, taskId)
                }
                val donePendingIntent = PendingIntent.getBroadcast(
                    context,
                    (taskId * 10 + 1).toInt(),
                    doneIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                // Snooze Intent (15 min)
                val snoozeIntent = Intent(context, ReminderReceiver::class.java).apply {
                    action = ACTION_SNOOZE
                    putExtra(EXTRA_TASK_ID, taskId)
                    putExtra(EXTRA_TASK_TITLE, taskTitle)
                    putExtra(EXTRA_TASK_SUBJECT, subject)
                    putExtra(EXTRA_TARGET_Q, targetQ)
                }
                val snoozePendingIntent = PendingIntent.getBroadcast(
                    context,
                    (taskId * 10 + 2).toInt(),
                    snoozeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val subText = if (targetQ > 0) "Target: $targetQ Questions" else "Official Mock Paper"
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("NEET Prep: $subject")
                    .setContentText("$taskTitle • $subText")
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Time to focus on $taskTitle ($subject)!\nTarget: $subText. Stay dedicated to your dream score!"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(openAppPendingIntent)
                    .addAction(android.R.drawable.checkbox_on_background, "Mark Done", donePendingIntent)
                    .addAction(android.R.drawable.ic_popup_reminder, "Snooze 15m", snoozePendingIntent)
                    .build()

                notificationManager.notify(taskId.toInt(), notification)
            }

            ACTION_MARK_DONE -> {
                notificationManager.cancel(taskId.toInt())
                if (taskId != -1L) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = AppDatabase.getDatabase(context).studyTaskDao()
                        dao.updateCompletion(taskId, true)
                    }
                }
            }

            ACTION_SNOOZE -> {
                notificationManager.cancel(taskId.toInt())
                val snoozeTime = System.currentTimeMillis() + 15 * 60 * 1000L
                ReminderManager.scheduleReminder(
                    context = context,
                    taskId = taskId,
                    taskTitle = taskTitle,
                    subject = subject,
                    targetQ = targetQ,
                    triggerAtMillis = snoozeTime
                )
            }
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily study sessions and target reminders for NEET planner"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
