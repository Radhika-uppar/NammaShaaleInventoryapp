package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.ui.theme.SurfaceVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpCenterScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help Center", fontWeight = FontWeight.Bold, color = BrandBlue) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Welcome to Namma Shaale Inventory",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrandBlue
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HelpSection(
                title = "How to add assets?",
                description = "Tap the '+' button on the bottom right of the main screens. Fill in the name, serial number, and category. You can also take a photo of the item."
            )
            
            HelpSection(
                title = "Performing an Audit",
                description = "Go to the Audit tab (clipboard icon). You will see items pending verification. Tap 'Verified' if the item is in good condition, or 'Report Issue' if it needs attention."
            )
            
            HelpSection(
                title = "Health Score",
                description = "Your inventory health starts at 100%. Each broken or damaged item reduces the score by 25%. Keep your assets maintained to maintain a high score!"
            )
            
            HelpSection(
                title = "Managing Categories",
                description = "In the Settings tab, you can manage categories to group your assets effectively (e.g., Electronics, Furniture)."
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = BrandBlue.copy(0.1f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = BrandBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Need more help? Contact our support team at support@nammashaale.edu",
                        fontSize = 14.sp,
                        color = BrandBlue
                    )
                }
            }
        }
    }
}

@Composable
fun HelpSection(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BrandBlue)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
