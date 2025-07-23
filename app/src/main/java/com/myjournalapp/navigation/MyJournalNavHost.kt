package com.myjournalapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.myjournalapp.ui.screens.onboarding.OnboardingScreen
import com.myjournalapp.ui.screens.timeline.TimelineScreen
import com.myjournalapp.ui.screens.editor.EntryEditorScreen
import com.myjournalapp.ui.screens.entrydetail.EntryDetailScreen
import com.myjournalapp.ui.screens.insights.InsightsScreen
import com.myjournalapp.ui.screens.challenges.ChallengesScreen
import com.myjournalapp.ui.screens.dashboard.DashboardScreen
import com.myjournalapp.ui.screens.reflection.ReflectionScreen
import com.myjournalapp.ui.screens.profile.ProfileScreen
import com.myjournalapp.ui.screens.notes.NotesScreen

object MyJournalDestinations {
    const val ONBOARDING_ROUTE = "onboarding"
    const val DASHBOARD_ROUTE = "dashboard"
    const val TIMELINE_ROUTE = "timeline"
    const val EDITOR_ROUTE = "editor"
    const val ENTRY_DETAIL_ROUTE = "entry_detail/{entryId}"
    const val INSIGHTS_ROUTE = "insights"
    const val CHALLENGES_ROUTE = "challenges"
    const val REFLECTION_ROUTE = "reflection"
    const val PROFILE_ROUTE = "profile"
    const val NOTES_ROUTE = "notes"
}

@Composable
fun MyJournalNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = MyJournalDestinations.ONBOARDING_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MyJournalDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(onCompleted = { navController.navigate(MyJournalDestinations.DASHBOARD_ROUTE) { popUpTo(MyJournalDestinations.ONBOARDING_ROUTE) { inclusive = true } } }, )
        }
        composable(MyJournalDestinations.DASHBOARD_ROUTE) {
            DashboardScreen()
        }
        composable(MyJournalDestinations.TIMELINE_ROUTE) {
            TimelineScreen(onEntryClick = { entry ->
                navController.navigate(MyJournalDestinations.ENTRY_DETAIL_ROUTE.replace(
                    "{entryId}",
                    entry.id
                ))
            })
        }
        composable(MyJournalDestinations.EDITOR_ROUTE) {
            EntryEditorScreen()
        }
        composable(MyJournalDestinations.ENTRY_DETAIL_ROUTE) {
            EntryDetailScreen()
        }
        composable(MyJournalDestinations.INSIGHTS_ROUTE) {
            InsightsScreen()
        }
        composable(MyJournalDestinations.CHALLENGES_ROUTE) {
            ChallengesScreen()
        }
        composable(MyJournalDestinations.REFLECTION_ROUTE) {
            ReflectionScreen()
        }
        composable(MyJournalDestinations.PROFILE_ROUTE) {
            ProfileScreen()
        }
        composable(MyJournalDestinations.NOTES_ROUTE) {
            NotesScreen()
        }
    }
}