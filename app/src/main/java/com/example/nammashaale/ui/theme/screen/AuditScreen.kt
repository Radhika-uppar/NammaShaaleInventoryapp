package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nammashaale.data.dao.entity.Asset
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.ErrorRed
import com.example.nammashaale.ui.theme.SuccessGreen
import com.example.nammashaale.ui.theme.SurfaceVariant
import com.example.nammashaale.viewmodel.AssetViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditScreen(
    onScanClick: () -> Unit,
    vm: AssetViewModel = hiltViewModel()
) {
    val assets by vm.assetList.collectAsState(initial = emptyList())
    val recentActivity by vm.recentActivity.collectAsState(initial = emptyList())
    
    // Items that need verification
    val pendingAssets = assets.filter { it.condition != "GREEN" }

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
                        Text("Inventory Audit", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceVariant)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AuditHeaderCard(onScanClick)
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Pending Verification", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    Surface(
                        color = Color.LightGray.copy(0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("${pendingAssets.size} Items Total", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp)
                    }
                }
            }

            if (pendingAssets.isEmpty()) {
                item {
                    Text("No pending verifications", color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))
                }
            } else {
                items(pendingAssets) { asset ->
                    PendingAuditListItem(
                        asset = asset,
                        onVerify = { vm.updateStatus(asset, "GREEN", adminName = "Radhika") },
                        onReportIssue = { vm.updateStatus(asset, "RED", adminName = "Radhika", issueDescription = "Damaged reported") }
                    )
                }
            }

            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Recent Activity", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = {}) {
                        Text("View All", color = BrandBlue, fontSize = 12.sp)
                    }
                }
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (recentActivity.isEmpty()) {
                            Text("No recent activity", fontSize = 12.sp, color = Color.Gray)
                        } else {
                            recentActivity.take(10).forEach { activity ->
                                val icon = if (activity.status == "GREEN") Icons.Default.CheckCircle else Icons.Default.Error
                                val color = if (activity.status == "GREEN") SuccessGreen else ErrorRed
                                
                                val diff = System.currentTimeMillis() - activity.timestamp
                                val timeLabel = when {
                                    diff < 60000 -> "just now"
                                    diff < 3600000 -> "${diff / 60000}m ago"
                                    diff < 86400000 -> "${diff / 3600000}h ago"
                                    else -> SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(activity.timestamp))
                                }
                                
                                AuditActivityRow(icon, color, "${activity.message} - $timeLabel")
                            }
                        }
                    }
                }
            }
            
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun AuditHeaderCard(onScanClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BrandBlue)
    ) {
        Column(Modifier.padding(24.dp)) {
            Text("Verify Assets", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "Scan barcodes or manually confirm items to keep the inventory up to date.",
                color = Color.White.copy(0.8f),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onScanClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = BrandBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.QrCodeScanner, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Start Scanning", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PendingAuditListItem(asset: Asset, onVerify: () -> Unit, onReportIssue: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(48.dp).background(SurfaceVariant, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Inventory, null, tint = BrandBlue)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(asset.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${asset.location} • ${asset.category}", fontSize = 11.sp, color = Color.Gray)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onVerify,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Verified", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = onReportIssue,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.ErrorOutline, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Report Issue", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AuditActivityRow(icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, text: String) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        Icon(icon, null, tint = color, modifier = Modifier.size(16.dp).padding(top = 2.dp))
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 12.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
    }
}
