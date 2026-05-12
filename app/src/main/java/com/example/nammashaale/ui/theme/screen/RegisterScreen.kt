package com.example.nammashaale.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nammashaale.ui.theme.BrandBlue
import com.example.nammashaale.viewmodel.AuthViewModel
import com.example.nammashaale.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onNavigateBack: () -> Unit, 
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var schoolName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading = authViewModel.isLoading
    val errorMessage = authViewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack, enabled = !isLoading) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrandBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Create Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Text(
                text = "Join the scholastic asset management network",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            RegistrationInputField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Enter your name",
                leadingIcon = Icons.Default.Person,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(20.dp))

            RegistrationInputField(
                label = "School Name",
                value = schoolName,
                onValueChange = { schoolName = it },
                placeholder = "Enter school name",
                leadingIcon = Icons.Outlined.Language,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(20.dp))

            RegistrationInputField(
                label = "Email Address",
                value = email,
                onValueChange = { email = it },
                placeholder = "admin@school.edu",
                leadingIcon = Icons.Default.Email,
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            RegistrationInputField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••",
                leadingIcon = Icons.Default.Lock,
                enabled = !isLoading,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    authViewModel.register(email, password) {
                        userViewModel.updateProfile(fullName, schoolName, email)
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading && fullName.isNotBlank() && schoolName.isNotBlank() && email.isNotBlank() && password.length >= 6
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account? ", color = Color.Gray)
                TextButton(onClick = onNavigateBack, enabled = !isLoading) {
                    Text("Sign In", color = BrandBlue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RegistrationInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean = true,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                focusedBorderColor = BrandBlue
            )
        )
    }
}
