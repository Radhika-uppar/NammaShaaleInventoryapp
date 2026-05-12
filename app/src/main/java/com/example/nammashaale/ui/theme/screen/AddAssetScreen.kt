package com.example.nammashaale.ui.theme.screen

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.SurfaceVariant
import com.example.nammashaale.viewmodel.AssetViewModel
import com.example.nammashaale.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssetScreen(
    userViewModel: UserViewModel,
    onBackClick: () -> Unit,
    vm: AssetViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var assetType by remember { mutableStateOf("New") }
    
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(userViewModel.categories.firstOrNull() ?: "") }
    
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) capturedImage = bitmap
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Asset", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandBlue) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = BrandBlue)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable { cameraLauncher.launch() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                border = BorderStroke(1.dp, Color.LightGray.copy(0.5f))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    if (capturedImage != null) {
                        Image(bitmap = capturedImage!!.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PhotoCamera, null, tint = BrandBlue, modifier = Modifier.size(48.dp))
                            Text("Take Photo", fontWeight = FontWeight.Bold, color = BrandBlue)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            InputField(label = "Asset Name", value = name, placeholder = "e.g. Football", onValueChange = { name = it })
            Spacer(Modifier.height(16.dp))
            
            Text("Category", modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedCategory, onValueChange = {}, readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    userViewModel.categories.forEach { category ->
                        DropdownMenuItem(text = { Text(category) }, onClick = { selectedCategory = category; expanded = false })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            InputField(label = "Location", value = location, placeholder = "e.g. Block B", onValueChange = { location = it })
            Spacer(Modifier.height(16.dp))
            InputField(label = "Serial Number", value = serial, placeholder = "SN-XXXX", onValueChange = { serial = it })
            
            Spacer(Modifier.height(16.dp))
            
            // New and Used buttons below Serial Number
            Text("Initial Condition", modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { assetType = "New" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (assetType == "New") BrandBlue else SurfaceVariant,
                        contentColor = if (assetType == "New") Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("New")
                }
                Button(
                    onClick = { assetType = "Used" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (assetType == "Used") BrandBlue else SurfaceVariant,
                        contentColor = if (assetType == "Used") Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Used")
                }
            }

            Spacer(Modifier.height(16.dp))
            InputField(label = "Additional Notes", value = notes, placeholder = "Enter any extra details...", onValueChange = { notes = it })

            Spacer(Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BrandBlue.copy(0.05f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Inventory Policy", fontWeight = FontWeight.Bold, color = BrandBlue)
                    Text(
                        "All assets must be verified quarterly. Damaged items must be reported immediately. Removal of assets requires administrative approval.",
                        fontSize = 11.sp, color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = {
                        if (name.isNotBlank() && serial.isNotBlank()) {
                            vm.addAsset(name, serial, location, selectedCategory, notes, assetType)
                            onBackClick()
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
                ) {
                    Text("Save Asset", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, placeholder: String, onValueChange: (String) -> Unit) {
    Column {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
