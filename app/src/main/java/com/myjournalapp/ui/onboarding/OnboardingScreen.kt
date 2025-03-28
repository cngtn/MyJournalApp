package com.myjournalapp.ui.onboarding // <<--- THAY BẰNG PACKAGE NAME CỦA BẠN

// --- Import các thư viện cần thiết ---
import com.myjournalapp.R
import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // Import các hàm layout (Column, Box, Padding, Size, ...)
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.* // Import các thành phần Material 3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myjournalapp.ui.theme.MyJournalAppTheme // <<--- Import Theme của bạn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

// --- Data class: Định nghĩa cấu trúc dữ liệu cho mỗi trang onboarding ---
data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val iconVector: ImageVector
)

// --- Composable chính: Màn hình Onboarding ---
@OptIn(ExperimentalFoundationApi::class) // Cần thiết cho Pager API
@Composable
fun OnboardingScreen(
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = rememberOnboardingPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            OnboardingBottomBar(
                pagerState = pagerState,
                pageCount = pages.size,
                scope = scope,
                onCompleted = onCompleted
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Padding của Scaffold
        ) { pageIndex ->
            // <<--- TÍNH TOÁN LẠI pageOffset ---
            val pageOffset = (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction

            OnboardingPageLayout(
                page = pages[pageIndex],
                pageOffset = pageOffset // <<--- TRUYỀN pageOffset VÀO LẠI
            )
        }
    } // Kết thúc Scaffold
}

// --- Composable cho phần Bottom Bar (Indicator và Nút) ---
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnboardingBottomBar(
    pagerState: PagerState,
    pageCount: Int,
    scope: CoroutineScope,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
//            .navigationBarsPadding() // Tránh thanh điều hướng
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PageIndicator(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    if (pagerState.currentPage < pageCount - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onCompleted()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (pagerState.currentPage < pageCount - 1) {
                    stringResource(R.string.onboarding_next)
                } else {
                    stringResource(R.string.onboarding_get_started)
                }
            )
        }
    }
}

// --- Composable cho Layout của một trang Onboarding (Ảnh tràn viền, Text có hiệu ứng) ---
@Composable
fun OnboardingPageLayout(
    page: OnboardingPage,
    pageOffset: Float, // <<--- THÊM LẠI THAM SỐ pageOffset
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // --- Phần Trên (50%): Hình ảnh Edge-to-Edge ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Có thể xóa Text debug inset nếu bạn không cần nữa
            // val statusBarHeightDp = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
            // Text("Top Inset: $statusBarHeightDp", ...)
        }

        // --- Phần Dưới (50%): Nội dung Text (Có hiệu ứng + Tránh Navigation Bar) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                // <<--- BỎ COMMENT DÒNG NÀY ---
//                .navigationBarsPadding() // Quan trọng: Tránh thanh điều hướng hệ thống
                .padding(horizontal = 16.dp, vertical = 12.dp) // Padding cho lề text
                // <<--- ÁP DỤNG LẠI graphicsLayer ĐỂ TẠO HIỆU ỨNG ---
                .graphicsLayer {
                    // Hiệu ứng mờ dần dựa trên offset
                    alpha = 1f - abs(pageOffset).coerceIn(0f, 1f)

                    // Tùy chọn thêm hiệu ứng khác nếu muốn:
                     translationX = pageOffset * 150f // Parallax
                     val scale = 1f - (abs(pageOffset).coerceIn(0f, 0.2f) * 0.5f) // Scale
                     scaleX = scale
                     scaleY = scale
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Tiêu đề
            Text(
                text = stringResource(id = page.titleRes),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    // <<--- CHỈ THÊM Modifier.offset(y = ...) ĐỂ ĐẨY LÊN ---
                    .offset(y = (-50).dp) // Dịch chuyển hiển thị lên trên 24dp. Điều chỉnh giá trị này nếu cần.
                    // Padding bottom để tạo khoảng cách với Description vẫn giữ nguyên
                    .padding(bottom = 16.dp)
            )
            Row(
                // Căn giữa các item trong Row theo chiều dọc
                verticalAlignment = Alignment.CenterVertically,
                // Bạn có thể thêm padding trên cho Row nếu cần thêm khoảng cách với Title
                // modifier = Modifier.padding(top = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp) // <<--- Kích thước của nền (lớn hơn icon)
                        .clip(CircleShape) // <<--- Hình dạng nền (Hình tròn)
                        // .clip(MaterialTheme.shapes.small) // Hoặc dùng bo góc từ Theme
                        .background(MaterialTheme.colorScheme.secondaryContainer), // <<--- Màu nền
                    contentAlignment = Alignment.Center // Căn giữa Icon bên trong Box
                ) {
                    // Icon bên trong Box
                    Icon(
                        imageVector = page.iconVector, // Lấy icon từ dữ liệu trang
                        contentDescription = null,
                        // Không cần size ở đây nữa vì Box đã định kích thước bao ngoài
                        // Chỉ đặt màu cho icon
                        tint = MaterialTheme.colorScheme.onSecondaryContainer // <<--- Màu icon tương phản với nền
                    )
                } // --- Kết thúc Box nền ---

                // Khoảng cách giữa Icon và Text
                Spacer(modifier = Modifier.width(8.dp))

                // Mô tả
                Text(
                    text = stringResource(id = page.descriptionRes),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start, // Vẫn giữ căn trái nếu bạn muốn
                    // Hoặc TextAlign.Center nếu muốn căn giữa trong Row
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                    // Không cần graphicsLayer riêng ở đây vì cả khối đã có hiệu ứng fade
                )
            } // Kết thúc Row (Icon + Mô tả)
        }
    } // Kết thúc Column ngoài cùng
}

// --- Composable cho Chỉ báo trang (Dots Indicator) ---
@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = index == currentPage
            val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            val size = if (isSelected) 12.dp else 8.dp
            Box(
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

// --- Hàm tiện ích để lấy danh sách các trang ---
// Sử dụng remember để tối ưu, tránh tạo lại list không cần thiết
@Composable
private fun rememberOnboardingPages(): List<OnboardingPage> {
    return remember {
        listOf(
            OnboardingPage(
                imageRes = R.drawable.onboarding_image_1, // <<--- THAY THẾ RESOURCE
                titleRes = R.string.onboarding_title_1,
                descriptionRes = R.string.onboarding_desc_1,
                iconVector = Icons.Outlined.LocationOn,
            ),
            OnboardingPage(
                imageRes = R.drawable.onboarding_image_2, // <<--- THAY THẾ RESOURCE
                titleRes = R.string.onboarding_title_2,
                descriptionRes = R.string.onboarding_desc_2,
                iconVector = Icons.Outlined.CheckCircle,
            ),
            OnboardingPage(
                imageRes = R.drawable.onboarding_image_3, // <<--- THAY THẾ RESOURCE
                titleRes = R.string.onboarding_title_3,
                descriptionRes = R.string.onboarding_desc_3,
                iconVector = Icons.Outlined.Create,
            )
            // Thêm các trang khác nếu cần
        )
    }
}

// --- Hàm Preview cho Android Studio ---
// Giúp xem trước giao diện mà không cần chạy toàn bộ ứng dụng
@Preview(showBackground = true, name = "Onboarding Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Onboarding Dark")
@Composable
fun OnboardingScreenPreview() {
    // Luôn bao bọc Preview bằng Theme của ứng dụng
    MyJournalAppTheme {
        OnboardingScreen(onCompleted = {}) // Truyền lambda trống cho preview
    }
}