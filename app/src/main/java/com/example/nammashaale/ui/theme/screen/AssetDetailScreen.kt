package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nammashaale.data.dao.entity.AssetHistory
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.SuccessGreen
import com.example.nammashaale.ui.theme.SurfaceVariant
import com.example.nammashaale.viewmodel.AssetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetDetailScreen(
    assetId: Long?,
    serialNumber: String? = null,
    onNavigateBack: () -> Unit,
    vm: AssetViewModel = hiltViewModel()
) {
    val assets by vm.assetList.collectAsState(initial = emptyList())
    val asset = assets.find { 
        if (assetId != null) it.id == assetId else it.serialNumber == serialNumber 
    }
    
    val historyState = if (asset != null) {
        vm.getAssetHistory(asset.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList<AssetHistory>()) }
    }
    val history = historyState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asset Management", fontWeight = FontWeight.Bold, color = BrandBlue) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandBlue)
                    }
                }
            )
        }
    ) { padding ->
        if (asset == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Asset not found")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceVariant)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().height(180.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Image, null, modifier = Modifier.size(80.dp), tint = Color.LightGray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        DetailRow("Asset Name", asset.name)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceVariant)
                        DetailRow("Serial Number", asset.serialNumber)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceVariant)
                        DetailRow("Category", asset.category)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceVariant)
                        DetailRow("Initial Condition", asset.assetType)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SurfaceVariant)
                        DetailRow("Status", when(asset.condition) {
                            "GREEN" -> "Working"
                            "YELLOW" -> "Needs Repair"
                            else -> "Broken"
                        })
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Update Status", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusButton("Working", SuccessGreen, Modifier.weight(1f)) { 
                        vm.updateStatus(asset, "GREEN", adminName = "Radhika") 
                    }
                    StatusButton("Needs Repair", Color(0xFFFFA000), Modifier.weight(1.2f)) { 
                        vm.updateStatus(asset, "YELLOW", adminName = "Radhika") 
                    }
                    StatusButton("Broken", Color.Red, Modifier.weight(1f)) { 
                        vm.updateStatus(asset, "RED", adminName = "Radhika") 
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("Asset History", modifier = Modifier.align(Alignment.Start), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (history.isEmpty()) {
                            Text("No history available", color = Color.Gray, fontSize = 12.sp)
                        } else {
                            history.forEach { item ->
                                val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                                Column {
                                    Text(item.message, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                    Text(date, fontSize = 11.sp, color = Color.Gray)
                                }
                                if (item != history.last()) HorizontalDivider(color = SurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusButton(text: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}
