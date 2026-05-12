package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.BrandBlueLight
import com.example.nammashaale.ui.theme.SurfaceVariant
import com.example.nammashaale.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userViewModel: UserViewModel, 
    onLogout: () -> Unit,
    onNavigateToHelpCenter: () -> Unit = {},
    onNavigateToAddAsset: () -> Unit = {}
) {
    var showCategoryDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    
    var showProfileDialog by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(userViewModel.fullName) }
    var editSchool by remember { mutableStateOf(userViewModel.schoolName) }
    var editEmail by remember { mutableStateOf(userViewModel.email) }

    var showLogoutConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(BrandBlue, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Widgets, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("Namma Shaale Inventory", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToHelpCenter) { Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Help Center") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddAsset,
                containerColor = BrandBlue,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Asset")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ProfileSection(userViewModel.fullName, userViewModel.schoolName) {
                    editName = userViewModel.fullName
                    editSchool = userViewModel.schoolName
                    editEmail = userViewModel.email
                    showProfileDialog = true
                }
            }

            item {
                LastLoginCard()
            }

            item {
                Text(
                    "APP SETTINGS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                SettingsGroupCard {
                    SettingSwitchItem(
                        icon = Icons.Outlined.Notifications,
                        title = "Push Notifications",
                        subtitle = "Alerts for low stock & audits",
                        checked = userViewModel.isNotificationsEnabled,
                        onCheckedChange = { userViewModel.isNotificationsEnabled = it }
                    )
                    HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingSwitchItem(
                        icon = Icons.Outlined.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Reduce eye strain at night",
                        checked = userViewModel.isDarkMode,
                        onCheckedChange = { userViewModel.isDarkMode = it }
                    )
                    HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingClickItem(
                        icon = Icons.Outlined.Language,
                        title = "Language",
                        value = userViewModel.language,
                        onClick = {
                            userViewModel.language = when(userViewModel.language) {
                                "English (India)" -> "Kannada"
                                "Kannada" -> "Tamil"
                                else -> "English (India)"
                            }
                        }
                    )
                }
            }

            item {
                Text(
                    "INVENTORY SETTINGS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                SettingsGroupCard {
                    Column(Modifier.padding(16.dp).fillMaxWidth()) {
                        Text("Audit Frequency", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Slider(
                            value = userViewModel.auditFrequency,
                            onValueChange = { userViewModel.auditFrequency = it },
                            colors = SliderDefaults.colors(thumbColor = BrandBlue, activeTrackColor = BrandBlue)
                        )
                        Text(
                            text = when {
                                userViewModel.auditFrequency < 0.3f -> "Monthly"
                                userViewModel.auditFrequency < 0.7f -> "Quarterly"
                                else -> "Yearly"
                            },
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                    HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingClickItem(
                        icon = Icons.Default.Category,
                        title = "Manage Categories",
                        value = "${userViewModel.categories.size} Active",
                        onClick = { showCategoryDialog = true }
                    )
                    HorizontalDivider(color = SurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                    SettingClickItem(
                        icon = Icons.AutoMirrored.Filled.HelpOutline,
                        title = "Help Center",
                        value = "Guidelines",
                        onClick = onNavigateToHelpCenter
                    )
                }
            }

            item {
                Button(
                    onClick = { showLogoutConfirmation = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer, contentColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Logout Session", fontWeight = FontWeight.Bold)
                }
            }
            
            item { Spacer(Modifier.height(80.dp)) }
        }
    }

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("Add New Category") },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("Category Name") },
                    placeholder = { Text("e.g. Book") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        userViewModel.addCategory(newCategoryName)
                        newCategoryName = ""
                        showCategoryDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = { Text("Edit Profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Full Name") })
                    OutlinedTextField(value = editSchool, onValueChange = { editSchool = it }, label = { Text("School Name") })
                    OutlinedTextField(value = editEmail, onValueChange = { editEmail = it }, label = { Text("Email") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    userViewModel.updateProfile(editName, editSchool, editEmail)
                    showProfileDialog = false
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showLogoutConfirmation) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirmation = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout from your session?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirmation = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Logout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileSection(name: String, school: String, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                Icon(Icons.Default.Person, null, modifier = Modifier.align(Alignment.Center).size(40.dp), tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(school, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = BrandBlueLight,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VerifiedUser, null, tint = BrandBlue, modifier = Modifier.size(12.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Full System Access", fontSize = 10.sp, color = BrandBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun LastLoginCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BrandBlue)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Last Login", color = Color.White.copy(0.8f), fontSize = 12.sp)
            Text("Today, 08:45 AM", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.2f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp)
            ) {
                Icon(Icons.Default.History, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Audit Logs", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SettingsGroupCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Composable
fun SettingSwitchItem(icon: ImageVector, title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(40.dp).background(SurfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = BrandBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(subtitle, fontSize = 11.sp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = BrandBlue)
        )
    }
}

@Composable
fun SettingClickItem(icon: ImageVector, title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(40.dp).background(SurfaceVariant, CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = BrandBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
        TextButton(onClick = onClick) {
            Text("CHANGE", color = BrandBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Text(value, fontSize = 12.sp, color = Color.Gray)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}
