package com.example.gifticonalarm.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gifticonalarm.ui.feature.add.AddGifticonScreen
import com.example.gifticonalarm.ui.feature.detail.DetailScreen
import com.example.gifticonalarm.ui.feature.home.HomeScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Add : Screen("add")
    data object Detail : Screen("detail/{gifticonId}") {
        fun createRoute(gifticonId: Long) = "detail/$gifticonId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
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
                    navController.popBackStack()
                }
            )
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