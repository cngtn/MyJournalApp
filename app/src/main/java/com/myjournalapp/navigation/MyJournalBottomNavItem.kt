package com.myjournalapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MyJournalBottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object Dashboard : MyJournalBottomNavItem("Dashboard", Icons.Default.Home, MyJournalDestinations.DASHBOARD_ROUTE)
    object Timeline : MyJournalBottomNavItem("Timeline", Icons.Default.DateRange, MyJournalDestinations.TIMELINE_ROUTE)
    object Insights : MyJournalBottomNavItem("Insights", Icons.Default.Lightbulb, MyJournalDestinations.INSIGHTS_ROUTE)
    object Challenges : MyJournalBottomNavItem("Challenges", Icons.Default.Star, MyJournalDestinations.CHALLENGES_ROUTE)
    object Profile : MyJournalBottomNavItem("Profile", Icons.Default.Person, MyJournalDestinations.PROFILE_ROUTE)

    companion object {
        val items = listOf(Dashboard, Timeline, Insights, Challenges, Profile)
    }
}

