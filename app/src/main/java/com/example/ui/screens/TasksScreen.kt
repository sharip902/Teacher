package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HomeworkAssignment
import com.example.ui.theme.*
import com.example.util.VoiceHelper

@Composable
fun TasksScreen(
    voiceHelper: VoiceHelper,
    onOpenSolver: () -> Unit
) {
    var assignments by remember {
        mutableStateOf(
            listOf(
                HomeworkAssignment("a1", "Math Worksheet #4: 2-Digit Addition", "In Progress", "Due Tomorrow", "Step 2 of 3 completed"),
                HomeworkAssignment("a2", "Science: Plant Photosynthesis & Sunlight", "Completed", "Due Friday", "All 4 steps complete", isCompleted = true),
                HomeworkAssignment("a3", "English: Spelling & Phonics 10 Words", "Pending", "Due Monday", "Ready to start"),
                HomeworkAssignment("a4", "Kannada: Alphabet & Word Matching", "Pending", "Due Tuesday", "Ready to start")
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "My Homework Tasks",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = OnSurface
                        )
                        Text(
                            text = "Grade 3 • 2 tasks due this week",
                            fontSize = 12.sp,
                            color = OnSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = { onOpenSolver() },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryContainer,
                            contentColor = OnPrimaryContainer
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Scan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        items(assignments) { item ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (item.isCompleted) TertiaryFixed else if (item.status == "In Progress") SecondaryFixed else PrimaryFixed
                        ) {
                            Text(
                                text = item.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (item.isCompleted) OnTertiaryFixedVariant else if (item.status == "In Progress") OnSecondaryFixed else OnPrimaryFixed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text(
                            text = item.dueDate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant
                        )
                    }

                    Text(
                        text = item.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )

                    Text(
                        text = item.progressSteps,
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.isCompleted) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Tertiary, modifier = Modifier.size(18.dp))
                                Text("Done! ⭐ +25", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Tertiary)
                            }
                        } else {
                            Button(
                                onClick = {
                                    voiceHelper.speak("Opening ${item.title}")
                                    onOpenSolver()
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (item.status == "In Progress") Secondary else Primary,
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = if (item.status == "In Progress") "Resume Solver" else "Start Homework",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
