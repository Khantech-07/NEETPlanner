package com.example

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.data.InitialScheduleData
import com.example.data.ReminderManager
import com.example.data.StudyTaskEntity
import com.example.ui.theme.BiologyMint
import com.example.ui.theme.ChemistryCoral
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.MilestoneBorder
import com.example.ui.theme.MilestonePurple
import com.example.ui.theme.MockTestAmber
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PhysicsCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

// =========================================================================
// ViewModel
// =========================================================================
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).studyTaskDao()

    private val _tasks = MutableStateFlow<List<StudyTaskEntity>>(emptyList())
    val tasks: StateFlow<List<StudyTaskEntity>> = _tasks.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val count = dao.getTaskCount()
            if (count == 0) {
                dao.insertAll(InitialScheduleData.getInitialTasks())
            }
            dao.getAllTasksFlow().collect { list ->
                _tasks.value = list
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterChanged(filter: String) {
        _selectedFilter.value = filter
    }

    fun toggleTaskCompletion(task: StudyTaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val newStatus = !task.isCompleted
            // If checking done and practiced questions is 0, auto-fill target
            val newPracticed = if (newStatus && task.practicedQuestions == 0 && task.targetQuestions > 0) {
                task.targetQuestions
            } else {
                task.practicedQuestions
            }
            dao.updateTask(task.copy(isCompleted = newStatus, practicedQuestions = newPracticed))
        }
    }

    fun updatePracticedQuestions(taskId: Long, count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val clamped = count.coerceAtLeast(0)
            dao.updatePracticedQuestions(taskId, clamped)
        }
    }

    fun incrementQuestions(task: StudyTaskEntity, step: Int = 5) {
        val next = task.practicedQuestions + step
        updatePracticedQuestions(task.id, next)
    }

    fun decrementQuestions(task: StudyTaskEntity, step: Int = 5) {
        val next = (task.practicedQuestions - step).coerceAtLeast(0)
        updatePracticedQuestions(task.id, next)
    }

    fun setReminderTime(taskId: Long, timeMillis: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateReminderTime(taskId, timeMillis)
        }
    }
}

// =========================================================================
// Main Activity
// =========================================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                NeetPlannerApp()
            }
        }
    }
}

// =========================================================================
// Subject Helper
// =========================================================================
fun getSubjectColor(subject: String): Color {
    return when (subject.lowercase()) {
        "physics" -> PhysicsCyan
        "chemistry" -> ChemistryCoral
        "biology", "botany", "zoology" -> BiologyMint
        "mock test" -> MockTestAmber
        else -> PhysicsCyan
    }
}

fun getSubjectShortLabel(subject: String): String {
    return when (subject.lowercase()) {
        "physics" -> "PHYS"
        "chemistry" -> "CHEM"
        "botany" -> "BOTANY"
        "zoology" -> "ZOOLOGY"
        "biology" -> "BIO"
        "mock test" -> "TEST"
        else -> subject.uppercase()
    }
}

// =========================================================================
// Main Screen Composable
// =========================================================================
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NeetPlannerApp(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    val tasks by viewModel.tasks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    // Permission launcher for Android 13+ notifications
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Notification permission needed for study reminders", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Active Reminder Dialog State
    var reminderDialogTask by remember { mutableStateOf<StudyTaskEntity?>(null) }
    // Edit Questions Dialog State
    var editQuestionsTask by remember { mutableStateOf<StudyTaskEntity?>(null) }
    var dashboardExpanded by remember { mutableStateOf(false) }

    // Progress Calculations
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val taskCompletionPercent = if (totalTasks > 0) (completedTasks * 100f / totalTasks) else 0f

    val totalTargetQuestions = tasks.sumOf { it.targetQuestions }
    val totalPracticedQuestions = tasks.sumOf { it.practicedQuestions }
    val questionCompletionPercent = if (totalTargetQuestions > 0) {
        (totalPracticedQuestions * 100f / totalTargetQuestions).coerceAtMost(100f)
    } else 0f

    // Combined overall completion formula: 60% chapter tasks completed + 40% question practice target
    val overallSyllabusPercent = if (totalTasks > 0) {
        ((taskCompletionPercent * 0.6f) + (questionCompletionPercent * 0.4f)).coerceIn(0f, 100f)
    } else 0f

    // Filter tasks
    val filteredTasks = remember(tasks, searchQuery, selectedFilter) {
        tasks.filter { task ->
            val matchesSearch = searchQuery.isBlank() ||
                task.chapter.contains(searchQuery, ignoreCase = true) ||
                task.subject.contains(searchQuery, ignoreCase = true) ||
                task.notes.contains(searchQuery, ignoreCase = true) ||
                task.fullDate.contains(searchQuery, ignoreCase = true) ||
                task.weekTitle.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "All" -> true
                "Physics" -> task.subject.equals("Physics", ignoreCase = true)
                "Chemistry" -> task.subject.equals("Chemistry", ignoreCase = true)
                "Biology" -> task.subject.equals("Biology", ignoreCase = true) ||
                    task.subject.equals("Botany", ignoreCase = true) ||
                    task.subject.equals("Zoology", ignoreCase = true)
                "Tests" -> task.subject.equals("Mock Test", ignoreCase = true)
                "Pending" -> !task.isCompleted
                "Done" -> task.isCompleted
                "Milestone" -> task.isMilestone
                else -> true
            }
            matchesSearch && matchesFilter
        }
    }

    // Group tasks by full date for sticky headers
    val groupedByDate = remember(filteredTasks) {
        filteredTasks.groupBy { task ->
            "${task.fullDate} • ${task.weekTitle}"
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // =========================================================================
            // Header Dashboard (Fixed Top Section)
            // =========================================================================
            HeaderDashboard(
                taskCompletionPercent = taskCompletionPercent,
                questionCompletionPercent = questionCompletionPercent,
                overallPercent = overallSyllabusPercent,
                completedTasks = completedTasks,
                totalTasks = totalTasks,
                totalPracticedQuestions = totalPracticedQuestions,
                totalTargetQuestions = totalTargetQuestions,
                expanded = dashboardExpanded,
                onToggleExpand = { dashboardExpanded = !dashboardExpanded },
                tasks = tasks
            )

            // Search Bar & Filter Chips at top of task list
            SearchBarAndFilters(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                selectedFilter = selectedFilter,
                onFilterSelected = viewModel::onFilterChanged,
                resultCount = filteredTasks.size,
                totalCount = tasks.size,
                onClearAllFilters = {
                    viewModel.onSearchQueryChanged("")
                    viewModel.onFilterChanged("All")
                },
                onJumpToMilestone = {
                    coroutineScope.launch {
                        val milestoneIndex = filteredTasks.indexOfFirst { it.isMilestone }
                        if (milestoneIndex != -1) {
                            listState.animateScrollToItem(milestoneIndex)
                        } else {
                            viewModel.onFilterChanged("Milestone")
                        }
                    }
                }
            )

            // =========================================================================
            // LazyColumn Task List with Sticky Date Headers
            // =========================================================================
            if (filteredTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Science,
                            contentDescription = "No tasks found",
                            tint = TextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No study schedule items match your filter.",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.onSearchQueryChanged("")
                                viewModel.onFilterChanged("All")
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PhysicsCyan)
                        ) {
                            Text("Reset Filters")
                        }
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("task_lazy_column"),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    groupedByDate.forEach { (dateHeader, dateTasks) ->
                        stickyHeader(key = "header_$dateHeader") {
                            DateStickyHeader(
                                headerText = dateHeader,
                                tasks = dateTasks
                            )
                        }

                        items(
                            items = dateTasks,
                            key = { it.id }
                        ) { task ->
                            TaskRowItem(
                                task = task,
                                onToggleCompletion = { viewModel.toggleTaskCompletion(task) },
                                onIncrement = { viewModel.incrementQuestions(task, 5) },
                                onDecrement = { viewModel.decrementQuestions(task, 5) },
                                onOpenEditQuestions = { editQuestionsTask = task },
                                onOpenReminder = { reminderDialogTask = task }
                            )
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // Edit Questions Dialog
    // =========================================================================
    editQuestionsTask?.let { task ->
        var inputCount by remember(task) { mutableStateOf(task.practicedQuestions.toString()) }
        val keyboardController = LocalSoftwareKeyboardController.current

        AlertDialog(
            onDismissRequest = { editQuestionsTask = null },
            title = {
                Text(
                    text = "Update Qs Practiced",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    Text(
                        text = "${task.chapter} (${task.subject})",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Target: ${task.targetQuestions} Questions",
                        color = PhysicsCyan,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = inputCount,
                        onValueChange = { inputCount = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Questions Solved") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PhysicsCyan,
                            unfocusedBorderColor = DarkOutline,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("questions_input_field")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = inputCount.toIntOrNull() ?: 0
                        viewModel.updatePracticedQuestions(task.id, parsed)
                        editQuestionsTask = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PhysicsCyan, contentColor = Color.Black)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editQuestionsTask = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkSurfaceCard
        )
    }

    // =========================================================================
    // Interactive Reminder Modal Dialog
    // =========================================================================
    reminderDialogTask?.let { task ->
        ReminderScheduleDialog(
            task = task,
            onDismiss = { reminderDialogTask = null },
            onSetReminder = { delayMillis, label ->
                val triggerTime = System.currentTimeMillis() + delayMillis
                ReminderManager.scheduleReminder(
                    context = context,
                    taskId = task.id,
                    taskTitle = task.chapter,
                    subject = task.subject,
                    targetQ = task.targetQuestions,
                    triggerAtMillis = triggerTime
                )
                viewModel.setReminderTime(task.id, triggerTime)
                reminderDialogTask = null
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        "Study reminder set for $label: ${task.chapter}"
                    )
                }
            },
            onTestInstantAlert = {
                ReminderManager.triggerInstantNotification(
                    context = context,
                    taskId = task.id,
                    taskTitle = task.chapter,
                    subject = task.subject,
                    targetQ = task.targetQuestions
                )
                reminderDialogTask = null
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Alert triggered! Check your notifications bar.")
                }
            }
        )
    }
}

// =========================================================================
// Header Dashboard (Fixed Top Section with Syllabus Tracking)
// =========================================================================
@Composable
fun HeaderDashboard(
    taskCompletionPercent: Float,
    questionCompletionPercent: Float,
    overallPercent: Float,
    completedTasks: Int,
    totalTasks: Int,
    totalPracticedQuestions: Int,
    totalTargetQuestions: Int,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    tasks: List<StudyTaskEntity>
) {
    val animatedOverall by animateFloatAsState(
        targetValue = overallPercent / 100f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "overall_progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, DarkOutline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // App Branding & Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(PhysicsCyan, BiologyMint))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NEET",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "NEET 2026 Master Plan",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "81-Day Daily Countdown Schedule",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Expand/Collapse Details
                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.testTag("dashboard_toggle_button")
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle dashboard details",
                        tint = PhysicsCyan
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Linear Progress Bar & Overall Percent
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "SYLLABUS PROGRESS",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${String.format("%.1f", overallPercent)}%",
                        color = PhysicsCyan,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$completedTasks / $totalTasks Tasks Done",
                        color = TextPrimary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$totalPracticedQuestions / $totalTargetQuestions Qs Practiced",
                        color = ChemistryCoral,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Linear Progress Bar
            LinearProgressIndicator(
                progress = { animatedOverall },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .testTag("overall_progress_bar"),
                color = PhysicsCyan,
                trackColor = DarkSurfaceVariant
            )

            // Expandable Subject Breakdown
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "Subject-Wise Target Completion",
                        color = TextSecondary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val subjects = listOf(
                        Triple("Physics", PhysicsCyan, tasks.filter { it.subject.equals("Physics", true) }),
                        Triple("Chemistry", ChemistryCoral, tasks.filter { it.subject.equals("Chemistry", true) }),
                        Triple("Biology", BiologyMint, tasks.filter { it.subject in listOf("Biology", "Botany", "Zoology") }),
                        Triple("Mock Papers", MockTestAmber, tasks.filter { it.subject.equals("Mock Test", true) })
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        subjects.forEach { (name, color, subTasks) ->
                            val doneCount = subTasks.count { it.isCompleted }
                            val totalCount = subTasks.size
                            val pct = if (totalCount > 0) (doneCount * 100 / totalCount) else 0

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = name,
                                        color = color,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "$pct%",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "$doneCount/$totalCount",
                                        color = TextMuted,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// =========================================================================
// Search Bar & Filter Chips
// =========================================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarAndFilters(
    query: String,
    onQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    resultCount: Int,
    totalCount: Int,
    onClearAllFilters: () -> Unit,
    onJumpToMilestone: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        // Prominent Elevated Search Field for filtering study tasks
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = BorderStroke(
                1.dp,
                if (query.isNotBlank()) PhysicsCyan.copy(alpha = 0.8f) else DarkOutline
            )
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_text_field"),
                placeholder = {
                    Text(
                        "Search chapter name or subject (Physics, Chem, Bio)...",
                        color = TextMuted,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search chapters and subjects",
                        tint = if (query.isNotBlank()) PhysicsCyan else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onQueryChange("")
                                keyboardController?.hide()
                            },
                            modifier = Modifier.testTag("search_clear_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search query",
                                tint = PhysicsCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                }),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = PhysicsCyan
                )
            )
        }

        // Search feedback and quick clear bar
        if (query.isNotBlank() || selectedFilter != "All") {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (query.isNotBlank()) {
                        "Found $resultCount of $totalCount tasks for \"$query\""
                    } else {
                        "Filter: $selectedFilter ($resultCount tasks)"
                    },
                    color = PhysicsCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Reset all filters",
                    color = ChemistryCoral,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onClearAllFilters() }
                        .padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal Filter Chips for Quick Subject and Status Switching
        val filterOptions = listOf(
            "All",
            "Physics",
            "Chemistry",
            "Biology",
            "Tests",
            "Pending",
            "Done",
            "Milestone"
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterOptions) { filter ->
                val isSelected = selectedFilter == filter
                val chipColor = when (filter) {
                    "Physics" -> PhysicsCyan
                    "Chemistry" -> ChemistryCoral
                    "Biology" -> BiologyMint
                    "Tests" -> MockTestAmber
                    "Milestone" -> MilestonePurple
                    else -> PhysicsCyan
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { onFilterSelected(filter) },
                    label = {
                        Text(
                            text = if (filter == "Milestone") "⭐ 22 Nov Milestone" else filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = chipColor.copy(alpha = 0.2f),
                        selectedLabelColor = chipColor,
                        containerColor = DarkSurface,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = DarkOutline,
                        selectedBorderColor = chipColor
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("filter_chip_$filter")
                )
            }
        }
    }
}

// =========================================================================
// Date Sticky Header
// =========================================================================
@Composable
fun DateStickyHeader(
    headerText: String,
    tasks: List<StudyTaskEntity>
) {
    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val totalQ = tasks.sumOf { it.targetQuestions }
    val solvedQ = tasks.sumOf { it.practicedQuestions }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sticky_date_header"),
        color = DarkBackground.copy(alpha = 0.95f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = headerText,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (totalQ > 0) {
                    Text(
                        text = "$solvedQ/$totalQ Qs",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (completedCount == totalCount) BiologyMint.copy(alpha = 0.2f) else DarkSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "$completedCount/$totalCount Done",
                        color = if (completedCount == totalCount) BiologyMint else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// =========================================================================
// Task Row Item Component
// =========================================================================
@Composable
fun TaskRowItem(
    task: StudyTaskEntity,
    onToggleCompletion: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onOpenEditQuestions: () -> Unit,
    onOpenReminder: () -> Unit
) {
    val subjectColor = getSubjectColor(task.subject)
    val isDone = task.isCompleted

    // Highlight 22 November milestone card with glowing amber/violet gradient border!
    val cardBorder = if (task.isMilestone) {
        BorderStroke(2.dp, Brush.horizontalGradient(listOf(MilestoneBorder, MilestonePurple, PhysicsCyan)))
    } else {
        BorderStroke(1.dp, if (isDone) BiologyMint.copy(alpha = 0.3f) else DarkOutline)
    }

    val cardBg = if (task.isMilestone) {
        Color(0xFF1F1B2E)
    } else if (isDone) {
        DarkSurfaceCard.copy(alpha = 0.7f)
    } else {
        DarkSurfaceCard
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("task_card_${task.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = cardBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Milestone Glowing Banner for 22 Nov 2026
            if (task.isMilestone) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(MilestoneBorder.copy(alpha = 0.25f), MilestonePurple.copy(alpha = 0.25f))
                            )
                        )
                        .border(
                            1.dp,
                            MilestoneBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Milestone Highlight",
                            tint = MilestoneBorder,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TARGET MILESTONE: 22 NOVEMBER 2026",
                            color = MilestoneBorder,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Top Row: Checkbox, Chapter Title, Subject Badge, Reminder Bell
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Done Checkbox
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { onToggleCompletion() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = BiologyMint,
                        uncheckedColor = TextMuted,
                        checkmarkColor = Color.Black
                    ),
                    modifier = Modifier.testTag("checkbox_task_${task.id}")
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Chapter & Subject
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Subject Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(subjectColor.copy(alpha = 0.2f))
                                .border(0.5.dp, subjectColor, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = getSubjectShortLabel(task.subject),
                                color = subjectColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (task.reminderTimeMillis > System.currentTimeMillis()) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Active Reminder",
                                tint = PhysicsCyan,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = task.chapter,
                        color = if (isDone) TextMuted else TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                // Reminder Button
                IconButton(
                    onClick = onOpenReminder,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("reminder_button_${task.id}")
                ) {
                    Icon(
                        imageVector = if (task.reminderTimeMillis > 0) Icons.Default.Alarm else Icons.Outlined.Alarm,
                        contentDescription = "Set Reminder",
                        tint = if (task.reminderTimeMillis > 0) PhysicsCyan else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Notes / Subtopics (from Grok table)
            if (task.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurfaceVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = task.notes,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Row: Question Target & Counter Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Target Q Info
                if (task.targetQuestions > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Target: ",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${task.targetQuestions} Qs",
                            color = subjectColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MockTestAmber.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Official Paper Test",
                            color = MockTestAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Interactive Counter Buttons (+ / - / direct tap)
                if (task.targetQuestions > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Minus Button
                        IconButton(
                            onClick = onDecrement,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .testTag("btn_minus_${task.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrement questions",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Practiced count (clickable to edit directly)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .border(1.dp, if (task.practicedQuestions >= task.targetQuestions) BiologyMint else DarkOutline, RoundedCornerShape(8.dp))
                                .clickable { onOpenEditQuestions() }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${task.practicedQuestions}",
                                    color = if (task.practicedQuestions >= task.targetQuestions) BiologyMint else TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "/${task.targetQuestions}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Plus Button
                        IconButton(
                            onClick = onIncrement,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant)
                                .testTag("btn_plus_${task.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increment questions",
                                tint = PhysicsCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    // For official mock papers: Mark Complete Quick Button
                    TextButton(
                        onClick = onToggleCompletion,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = if (isDone) BiologyMint else MockTestAmber
                        )
                    ) {
                        Icon(
                            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = "Test status",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isDone) "Paper Completed" else "Mark Paper Done",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// Reminder Schedule Dialog
// =========================================================================
@Composable
fun ReminderScheduleDialog(
    task: StudyTaskEntity,
    onDismiss: () -> Unit,
    onSetReminder: (delayMillis: Long, label: String) -> Unit,
    onTestInstantAlert: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = "Alarm icon",
                    tint = PhysicsCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Study Session Reminder",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "${task.chapter} (${task.subject})",
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Choose when you want to be notified:",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Presets
                val presets = listOf(
                    Pair(15 * 60 * 1000L, "In 15 Minutes"),
                    Pair(30 * 60 * 1000L, "In 30 Minutes"),
                    Pair(60 * 60 * 1000L, "In 1 Hour"),
                    Pair(3 * 60 * 60 * 1000L, "In 3 Hours")
                )

                presets.forEach { (delay, label) ->
                    OutlinedButton(
                        onClick = { onSetReminder(delay, label) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = DarkSurface,
                            contentColor = TextPrimary
                        ),
                        border = BorderStroke(1.dp, DarkOutline),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(label)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Instant Test Alert Button
                Button(
                    onClick = onTestInstantAlert,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_test_instant_alert"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PhysicsCyan,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Test Notification",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Trigger Test Alert Now",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = DarkSurfaceCard
    )
}
