package com.example.educonnect.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.educonnect.components.EduButton
import com.example.educonnect.ui.auth.AuthViewModel
import com.example.educonnect.ui.theme.PurpleMain

@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val userProfile by authViewModel.userProfile.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            if (email.isEmpty()) email = profile.email
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val updateStatus by authViewModel.updateStatus.collectAsStateWithLifecycle()

    LaunchedEffect(updateStatus) {
        if (updateStatus == "success") {
            authViewModel.resetUpdateStatus()
            onNavigateBack()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clickable { galleryLauncher.launch("image/*") }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, PurpleMain, CircleShape)
                                .background(Color(0xFFE9ECEF)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedImageUri != null) {
                                AsyncImage(
                                    model = selectedImageUri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else if (!userProfile?.profilePictureUrl.isNullOrEmpty()) {
                                AsyncImage(
                                    model = userProfile?.profilePictureUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(70.dp))
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(PurpleMain),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Ganti Foto", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    isError = email.isEmpty()
                )

                if (email.isEmpty()) {
                    Text(
                        text = "Email tidak boleh kosong",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                if (updateStatus != null && updateStatus != "success") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = updateStatus ?: "", color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }
            }

            EduButton(
                text = "Simpan Perubahan",
                onClick = {
                    if (email.isNotEmpty()) {
                        authViewModel.updateProfile(email = email, imageUri = selectedImageUri, context = context)
                    }
                }
            )
        }
    }
}