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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.BrandBlueLight
import com.example.nammashaale.ui.theme.ErrorRed
import com.example.nammashaale.ui.theme.SuccessGreen
import com.example.nammashaale.ui.theme.SurfaceVariant
import com.example.nammashaale.viewmodel.AssetViewModel
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    vm: AssetViewModel = hiltViewModel()
) {
    val assets by vm.assetList.collectAsState(initial = emptyList())
    val needsRepairOrBroken = assets.count { it.condition != "GREEN" }
    val totalAssets = assets.size
    
    // Health score reduces by 25% for each broken/needs-repair item
    val healthScore = max(0, 100 - (needsRepairOrBroken * 25))

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
                        Text("Namma-Shaale Inventory", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue) }
                    IconButton(onClick = {}) { Icon(Icons.Default.FilterList, contentDescription = null, tint = BrandBlue) }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceVariant)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { onNavigate("report") },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandBlueLight, contentColor = BrandBlue),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Share Report", fontSize = 12.sp)
                    }
                }
            }

            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SummaryCard(
                        title = "TOTAL ASSETS",
                        value = totalAssets.toString(),
                        subtitle = "100% active",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(16.dp))
                    SummaryCard(
                        title = "NEEDS REPAIR / BROKEN",
                        value = needsRepairOrBroken.toString(),
                        subtitle = if (needsRepairOrBroken == 0) "All Clear" else "Attention Required",
                        isSuccess = needsRepairOrBroken == 0,
                        isError = needsRepairOrBroken > 0,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
                InventoryHealthCard(healthScore)
            }

            item {
                Row(
                    modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Assets", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { onNavigate("all_assets") }) {
                        Text("View all", color = BrandBlue)
                    }
                }
            }

            items(assets.take(5)) { asset ->
                AssetItemRow(
                    name = asset.name,
                    location = "${asset.location} • ${asset.category}",
                    status = when(asset.condition) {
                        "GREEN" -> "Working"
                        "YELLOW" -> "Needs Repair"
                        else -> "Broken"
                    },
                    isWorking = asset.condition == "GREEN",
                    onClick = { onNavigate("asset_details/${asset.id}") }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String, 
    value: String, 
    subtitle: String, 
    modifier: Modifier = Modifier, 
    isSuccess: Boolean = false,
    isError: Boolean = false
) {
    val highlightColor = if (isError) ErrorRed else if (isSuccess) SuccessGreen else BrandBlue
    val backgroundColor = if (isError) Color(0xFFFFEBEE) else Color.White
    
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 10.sp, color = if (isError) ErrorRed else Color.Gray, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = highlightColor)
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSuccess) {
                    Box(Modifier.size(16.dp).background(SuccessGreen.copy(0.2f), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Check, null, modifier = Modifier.size(10.dp), tint = SuccessGreen)
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(subtitle, fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                } else {
                    Box(Modifier.fillMaxWidth().height(4.dp).background(Color.LightGray, CircleShape)) {
                        Box(Modifier.fillMaxWidth(0.7f).fillMaxHeight().background(highlightColor, CircleShape))
                    }
                }
            }
            if (!isSuccess) {
                Text(subtitle, fontSize = 10.sp, color = if (isError) ErrorRed else Color.Gray, modifier = Modifier.padding(top = 4.dp), fontWeight = if (isError) FontWeight.Bold else FontWeight.Normal)
            }
        }
    }
}

@Composable
fun InventoryHealthCard(score: Int) {
    val backgroundColor = if (score < 100) ErrorRed else BrandBlue
    
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(Modifier.padding(24.dp)) {
            Text("Inventory Health Score", color = Color.White.copy(0.8f), fontSize = 14.sp)
            Text("$score%", color = Color.White, fontSize = 52.sp, fontWeight = FontWeight.Black)
            
            Spacer(Modifier.height(16.dp))
            
            if (score < 100) {
                // High-visibility alert banner for action items
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Action Required: Check Broken Items", 
                            color = ErrorRed, 
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "All assets in excellent condition", 
                        color = Color.White, 
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssetItemRow(name: String, location: String, status: String, isWorking: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).background(SurfaceVariant, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Widgets, null, modifier = Modifier.size(20.dp), tint = Color.Gray)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(location, fontSize = 11.sp, color = Color.Gray)
            }
            val statusColor = if (isWorking) SuccessGreen else ErrorRed
            Box(
                Modifier.background(statusColor.copy(0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
