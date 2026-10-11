package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTaskDao {
    @Query("SELECT * FROM study_tasks ORDER BY dayIndex ASC, id ASC")
    fun getAllTasksFlow(): Flow<List<StudyTaskEntity>>

    @Query("SELECT * FROM study_tasks ORDER BY dayIndex ASC, id ASC")
    suspend fun getAllTasks(): List<StudyTaskEntity>

    @Query("SELECT * FROM study_tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Long): StudyTaskEntity?

    @Query("SELECT COUNT(*) FROM study_tasks")
    suspend fun getTaskCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<StudyTaskEntity>)

    @Update
    suspend fun updateTask(task: StudyTaskEntity)

    @Query("UPDATE study_tasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletion(id: Long, isCompleted: Boolean)

    @Query("UPDATE study_tasks SET practicedQuestions = :count WHERE id = :id")
    suspend fun updatePracticedQuestions(id: Long, count: Int)

    @Query("UPDATE study_tasks SET reminderTimeMillis = :timeMillis WHERE id = :id")
    suspend fun updateReminderTime(id: Long, timeMillis: Long)
}
