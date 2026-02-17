package com.example.gifticonalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gifticonalarm.ui.navigation.NavGraph
import com.example.gifticonalarm.ui.navigation.Screen
import com.example.gifticonalarm.ui.onboarding.AppStartViewModel
import com.example.gifticonalarm.ui.theme.GifticonAlarmTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * App entry activity.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GifticonAlarmTheme {
                GifticonAlarmApp()
            }
        }
    }
}

/**
 * Root app composable with bottom navigation.
 */
@Composable
fun GifticonAlarmApp() {
    val appStartViewModel: AppStartViewModel = hiltViewModel()
    val isOnboardingCompleted by appStartViewModel.isOnboardingCompleted.observeAsState()

    if (isOnboardingCompleted == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (isOnboardingCompleted == true) {
        Screen.Home.route
    } else {
        Screen.Onboarding.route
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomItems = listOf(
        BottomNavItem(Screen.Home.route, "홈", Icons.Filled.Home),
        BottomNavItem(Screen.Coupons.route, "쿠폰함", Icons.Filled.Inventory2),
        BottomNavItem(Screen.CouponRegistration.route, "추가", Icons.Filled.AddCircle),
        BottomNavItem(Screen.Settings.route, "설정", Icons.Filled.Settings)
    )

    Scaffold(
        bottomBar = {
            currentDestination?.let { destination ->
                val shouldShowBottomBar = destination.hierarchy
                    .any { navDestination ->
                        bottomItems.any { it.route == navDestination.route }
                    }

                if (shouldShowBottomBar) {
                    NavigationBar {
                        bottomItems.forEach { item ->
                            val selected = destination.hierarchy
                                .any { navDestination -> navDestination.route == item.route }

                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
