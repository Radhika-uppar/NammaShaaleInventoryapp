package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.SuccessGreen
import com.example.nammashaale.ui.theme.SurfaceVariant
import com.example.nammashaale.viewmodel.AssetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAssetsScreen(
    onNavigateBack: () -> Unit,
    onAssetClick: (Long) -> Unit,
    vm: AssetViewModel = hiltViewModel()
) {
    val assets by vm.assetList.collectAsState(initial = emptyList())

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
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandBlue)
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue) }
                    IconButton(onClick = {}) { Icon(Icons.Default.FilterList, contentDescription = null, tint = BrandBlue) }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceVariant)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("All Assets", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(24.dp), tint = Color.Gray)
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(assets) { asset ->
                    AllAssetScreenListItem(
                        name = asset.name,
                        location = "${asset.location} • ${asset.category}",
                        serial = asset.serialNumber,
                        status = when(asset.condition) {
                            "GREEN" -> "WORKING"
                            "YELLOW" -> "NEEDS REPAIR"
                            else -> "BROKEN"
                        },
                        statusColor = when(asset.condition) {
                            "GREEN" -> SuccessGreen
                            "YELLOW" -> Color(0xFFFFA000)
                            else -> Color.Red
                        },
                        onClick = { onAssetClick(asset.id) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun AllAssetScreenListItem(
    name: String,
    location: String,
    serial: String,
    status: String,
    statusColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(60.dp)
                    .background(SurfaceVariant, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, null, tint = Color.LightGray, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 1)
                    Spacer(Modifier.weight(1f))
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            status,
                            color = statusColor,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(location, fontSize = 12.sp, color = Color.Gray)
                Text(serial, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
        }
    }
}
