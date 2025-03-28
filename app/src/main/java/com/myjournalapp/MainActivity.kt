package com.myjournalapp // <<--- PACKAGE NAME CỦA BẠN

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge // Import hàm helper mới
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
// import androidx.core.view.WindowCompat // Không cần import này nữa nếu chỉ dùng enableEdgeToEdge()
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.myjournalapp.ui.onboarding.OnboardingScreen
import com.myjournalapp.ui.theme.MyJournalAppTheme // <<--- KIỂM TRA TÊN THEME CỦA BẠN
import com.myjournalapp.ui.timeline.TimelineScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// --- Thiết lập DataStore Preferences ---
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

// --- MainActivity ---
class MainActivity : ComponentActivity() {

    private var isLoadingContent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // --- THỨ TỰ KHỞI TẠO QUAN TRỌNG ---
        // 1. Bật Edge-to-Edge (nên gọi đầu tiên)
        enableEdgeToEdge()

        // 2. Cài đặt Splash Screen (ngay sau enableEdgeToEdge, trước super.onCreate)
        val splashScreen = installSplashScreen()

        // 3. Gọi super.onCreate
        super.onCreate(savedInstanceState)
        // -----------------------------------

        // 4. Giữ Splash Screen hiển thị nếu cần
        splashScreen.setKeepOnScreenCondition { isLoadingContent }

        // 5. Thiết lập giao diện
        setContent {
            MyJournalAppTheme { // <<--- Đảm bảo dùng đúng tên Theme của bạn

                // --- Điều chỉnh System UI ---
                val view = LocalView.current
                val darkTheme: Boolean = isSystemInDarkTheme()
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        // Đặt thanh hệ thống trong suốt
                        window.statusBarColor = Color.Transparent.toArgb()
                        window.navigationBarColor = Color.Transparent.toArgb()

                        // Điều chỉnh màu icon hệ thống
                        val insetsController = WindowCompat.getInsetsController(window, view)
//                        insetsController.isAppearanceLightStatusBars = !darkTheme
//                        insetsController.isAppearanceLightNavigationBars = !darkTheme
                    }
                }
                // --- Kết thúc System UI ---

                val context = LocalContext.current

                // --- Đọc trạng thái onboarding ---
                val onboardingCompletedFlow: Flow<Boolean> = remember {
                    context.dataStore.data.map { preferences ->
                        preferences[ONBOARDING_COMPLETED_KEY] ?: false
                    }
                }
                val onboardingCompleted by onboardingCompletedFlow.collectAsState(initial = null)

                // --- Cập nhật cờ isLoadingContent ---
                LaunchedEffect(onboardingCompleted) {
                    if (onboardingCompleted != null) {
                        isLoadingContent = false
                    }
                }

                // --- Hiển thị nội dung chính ---
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedVisibility(
                        visible = onboardingCompleted != null,
                        enter = fadeIn(/* ... */), // Giữ nguyên hiệu ứng
                        exit = fadeOut(/* ... */)
                    ) {
                        if (onboardingCompleted == false) {
                            OnboardingScreen(
                                onCompleted = {
                                    lifecycleScope.launch {
                                        context.dataStore.edit { settings ->
                                            settings[ONBOARDING_COMPLETED_KEY] = true
                                        }
                                    }
                                }
                            )
                        } else {
                            // ----- **THAY THẾ MainAppScreen() BẰNG TimelineScreen() TẠI ĐÂY** -----
                            TimelineScreen(
                                // 1. Truyền lambda cho hành động xem chi tiết
                                onNavigateToDetail = { entryId ->
                                    // Đây là nơi bạn sẽ dùng NavController để điều hướng
                                    // Ví dụ: navController.navigate("detailRoute/${entryId}")
                                    Log.d("MainActivity", "Navigate to Detail Screen for ID: $entryId")
                                    // TODO: (Sau này) Triển khai điều hướng thực tế đến màn hình Chi tiết
                                },

                                // 2. Truyền lambda cho hành động tạo mới (nhấn FAB, nhấn gợi ý)
                                onNavigateToCreate = {
                                    // Đây là nơi bạn sẽ dùng NavController để điều hướng
                                    // Ví dụ: navController.navigate("createRoute")
                                    Log.d("MainActivity", "Navigate to Create Screen")
                                    // TODO: (Sau này) Triển khai điều hướng thực tế đến màn hình Tạo mới
                                },

                                // 3. (Tùy chọn) Truyền lambda cho các hành động khác nếu cần
                                // onNavigateToSearch = { /* Xử lý nhấn nút tìm kiếm */ },
                                // onNavigateToFilter = { /* Xử lý nhấn nút lọc */ }
                            )
                            // ----- **KẾT THÚC THAY THẾ** -----
                        }
                    } // Kết thúc AnimatedVisibility
                } // Kết thúc Surface
            } // Kết thúc Theme
        } // Kết thúc setContent
    } // Kết thúc onCreate
} // Kết thúc MainActivity

// --- Placeholder Màn hình chính ---
@Composable
fun MainAppScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Đây là Màn hình chính")
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyJournalAppTheme { // <<--- Đảm bảo dùng đúng tên Theme của bạn
        MainAppScreen()
    }
}