package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.SuccessGreen
import com.example.nammashaale.viewmodel.AssetViewModel

@Composable
fun SummaryReportDialog(
    onDismiss: () -> Unit,
    vm: AssetViewModel = hiltViewModel()
) {
    val assets by vm.assetList.collectAsState(initial = emptyList())
    val workingCount = assets.count { it.condition == "GREEN" }
    val repairCount = assets.count { it.condition == "YELLOW" }
    val brokenCount = assets.count { it.condition == "RED" }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = BrandBlue, fontWeight = FontWeight.Bold)
            }
        },
        title = { 
            Text(
                "Namma-Shaale Inventory Report", 
                fontSize = 18.sp, 
                fontWeight = FontWeight.Bold,
                color = BrandBlue
            ) 
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Inventory Summary", fontWeight = FontWeight.Bold)
                
                ReportRow("Total Assets", assets.size.toString(), Color.Black)
                ReportRow("Working Items", workingCount.toString(), SuccessGreen)
                ReportRow("Needs Repair", repairCount.toString(), Color(0xFFFFA000))
                ReportRow("Broken Items", brokenCount.toString(), Color.Red)
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                
                val score = if (assets.isNotEmpty()) ((workingCount.toFloat() / assets.size) * 100).toInt() else 100
                Text(
                    "Overall Health Score: $score%",
                    fontWeight = FontWeight.Bold,
                    color = if (score > 60) SuccessGreen else Color.Red
                )
            }
        }
    )
}

@Composable
fun ReportRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold, color = color)
    }
}
