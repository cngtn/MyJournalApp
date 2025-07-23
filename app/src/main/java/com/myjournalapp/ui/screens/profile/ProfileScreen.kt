package com.myjournalapp.ui.screens.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.myjournalapp.services.JournalChallengeService

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.getOrDefault(Manifest.permission.POST_NOTIFICATIONS, false)) {
                startChallengeService(context)
            }
            // Handle the case where the user denies the permission
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Profile Screen")
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= 36) {
                    notificationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.POST_NOTIFICATIONS,
                            "android.permission.POST_PROMOTED_NOTIFICATIONS"
                        )
                    )
                } else {
                    startChallengeService(context)
                }
            }) {
                Text("Start Journal Challenge")
            }
        }
    }
}

private fun startChallengeService(context: Context) {
    val intent = Intent(context, JournalChallengeService::class.java)
    context.startService(intent)
}