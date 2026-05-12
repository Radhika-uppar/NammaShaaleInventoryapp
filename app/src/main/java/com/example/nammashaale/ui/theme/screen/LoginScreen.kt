package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }

    val isLoading = authViewModel.isLoading
    val errorMessage = authViewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(BrandBlue, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Widgets, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome Back",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Text(
            text = "Sign in to manage your school's inventory assets effectively.",
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start).padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Text(
            text = "Email Address",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("admin@namma-shaale.edu", color = Color.LightGray) },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                focusedBorderColor = BrandBlue
            ),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Password",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp),
            fontSize = 14.sp
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("••••••••", color = Color.LightGray) },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                focusedBorderColor = BrandBlue
            ),
            enabled = !isLoading
        )

        TextButton(
            onClick = { showForgotDialog = true },
            modifier = Modifier.align(Alignment.End),
            enabled = !isLoading
        ) {
            Text("Forgot Password?", color = BrandBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                authViewModel.login(email, password, onLoginSuccess)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
            TextButton(onClick = onCreateAccountClick, enabled = !isLoading) {
                Text("Create Account", color = BrandBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }

    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            title = { Text("Reset Password") },
            text = {
                Column {
                    Text("Enter your email address to receive a password reset link.", fontSize = 14.sp)
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = forgotEmail,
                        onValueChange = { forgotEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.resetPassword(forgotEmail) {
                            showForgotDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    enabled = !isLoading && forgotEmail.isNotBlank()
                ) {
                    Text("Send Link")
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
