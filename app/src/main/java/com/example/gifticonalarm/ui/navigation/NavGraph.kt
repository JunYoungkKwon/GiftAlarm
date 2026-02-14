package com.example.gifticonalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gifticonalarm.ui.feature.add.AddGifticonScreen
import com.example.gifticonalarm.ui.feature.detail.DetailScreen
import com.example.gifticonalarm.ui.feature.home.HomeScreen
import com.example.gifticonalarm.ui.feature.home.dashboard.DashboardScreen
import com.example.gifticonalarm.ui.feature.settings.SettingsScreen

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Coupons : Screen("coupons")
    data object Add : Screen("add")
    data object Settings : Screen("settings")
    data object Detail : Screen("detail/{gifticonId}") {
        fun createRoute(gifticonId: Long) = "detail/$gifticonId"
    }
}

/**
 * App navigation graph.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }

        composable(Screen.Coupons.route) {
            HomeScreen(
                onNavigateToAdd = {
                    navController.navigate(Screen.Add.route)
                },
                onNavigateToDetail = { gifticonId ->
                    navController.navigate(Screen.Detail.createRoute(gifticonId))
                }
            )
        }

        composable(Screen.Add.route) {
            AddGifticonScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Coupons.route) {
                        popUpTo(Screen.Coupons.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("gifticonId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val gifticonId = backStackEntry.arguments?.getLong("gifticonId") ?: return@composable
            DetailScreen(
                gifticonId = gifticonId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
