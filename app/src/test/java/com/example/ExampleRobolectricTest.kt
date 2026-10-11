package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.InitialScheduleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("NEET Planner", appName)
  }

  @Test
  fun `verify full initial schedule contains 81 days`() {
    val tasks = InitialScheduleData.getInitialTasks()
    assertTrue("Schedule should contain all parsed days", tasks.isNotEmpty())
    val milestoneTask = tasks.find { it.isMilestone }
    assertTrue("Should include 22 November milestone", milestoneTask != null)
    assertEquals("NRTS 08 • Phase-1 Major Milestone", milestoneTask?.chapter)
  }
}
