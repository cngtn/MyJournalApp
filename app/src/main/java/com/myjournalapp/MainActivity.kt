package com.myjournalapp

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.myjournalapp.navigation.MyJournalBottomNavItem
import com.myjournalapp.navigation.MyJournalNavHost
import com.myjournalapp.ui.theme.MyJournalAppTheme
import com.myjournalapp.util.BiometricAuthenticator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.myjournalapp.data.preferences.UserPreferencesRepository
import com.myjournalapp.navigation.MyJournalDestinations
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.draw.blur
import kotlinx.coroutines.delay
import androidx.lifecycle.lifecycleScope
import com.myjournalapp.notifications.MeditationNotificationManager
import com.myjournalapp.services.JournalChallengeService
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize SnackbarNotificationManager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        MeditationNotificationManager.initialize(applicationContext, notificationManager)

        setContent {
            MyJournalAppTheme {

                // -- System UI --
                val colorScheme = MaterialTheme.colorScheme
                val view = LocalView.current
                val darkTheme = isSystemInDarkTheme()

                SideEffect {
                    val window = (view.context as AppCompatActivity).window
                    window.statusBarColor = android.graphics.Color.TRANSPARENT
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                }


                // -- Auth state --
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                val showMainContent = remember { mutableStateOf(false) } // New state

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_START) {
                            lifecycleOwner.lifecycleScope.launch { // Launch a coroutine
                                delay(200) // Add a small delay to ensure UI renders blur
                                BiometricAuthenticator.authenticate(
                                    context = context,
                                    title = "Xác thực để tiếp tục",
                                    subtitle = "Sử dụng vân tay hoặc khuôn mặt của bạn",
                                    description = "",
                                    onSuccess = { showMainContent.value = true },
                                    onFailure = { _, errString ->
                                        Toast.makeText(context, "Xác thực thất bại: $errString", Toast.LENGTH_SHORT).show()
                                        finish()
                                    },
                                    onError = { _, errString ->
                                        Toast.makeText(context, "Lỗi xác thực: $errString", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                )
                            }
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                val onboardingCompleted by userPreferencesRepository.onboardingCompleted.collectAsStateWithLifecycle(initialValue = false)
                val initialStartDestination = remember(onboardingCompleted) {
                    if (onboardingCompleted) MyJournalDestinations.DASHBOARD_ROUTE else MyJournalDestinations.ONBOARDING_ROUTE
                }

                if (showMainContent.value) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val noBottomNavRoutes = listOf(MyJournalDestinations.ONBOARDING_ROUTE)

                    Scaffold(
                        bottomBar = {
                            if (currentDestination?.route !in noBottomNavRoutes) {
                                NavigationBar {
                                    MyJournalBottomNavItem.items.forEach { screen ->
                                        NavigationBarItem(
                                            icon = { Icon(screen.icon, contentDescription = null) },
                                            label = { Text(screen.title) },
                                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                            onClick = {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        contentWindowInsets = WindowInsets(0,0,0,0) // disable auto insets
                    ) { innerPadding ->
                        MyJournalNavHost(
                            navController = navController,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding), // Apply innerPadding here
                            startDestination = initialStartDestination
                        )
                    }


                } else {
                    // Loading / Auth Screen
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(radius = 16.dp), // Apply blur here
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Đang chờ xác thực sinh trắc học...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
    }
}
