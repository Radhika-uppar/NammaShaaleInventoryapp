package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammashaale.ui.theme.BrandBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanBarcodeScreen(onScanResult: (String) -> Unit, onBack: () -> Unit) {
    var manualCode by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Barcode", fontWeight = FontWeight.Bold, color = BrandBlue) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandBlue)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.Black.copy(0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = BrandBlue
                )
                // In a real app, this would be a CameraPreview
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "Align barcode within the frame",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text("Or enter code manually", fontSize = 14.sp, color = Color.Gray)
            
            OutlinedTextField(
                value = manualCode,
                onValueChange = { manualCode = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                placeholder = { Text("SN-XXXX-XXXX") },
                shape = RoundedCornerShape(12.dp)
            )
            
            Button(
                onClick = { if (manualCode.isNotBlank()) onScanResult(manualCode) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
            ) {
                Text("Confirm Code", fontWeight = FontWeight.Bold)
            }
        }
    }
}
