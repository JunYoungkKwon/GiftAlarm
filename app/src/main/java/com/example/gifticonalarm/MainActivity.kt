package com.example.gifticonalarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.content.ContextCompat
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
    private val appStartViewModel: AppStartViewModel by viewModels()
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            appStartViewModel.isOnboardingCompleted.value == null
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        setContent {
            GifticonAlarmTheme {
                GifticonAlarmApp()
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val isGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (isGranted) return
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
        ) {}
        return
    }
    val startDestination = if (isOnboardingCompleted == true) {
        Screen.HomeTab.route
    } else {
        Screen.Onboarding.route
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomItems = listOf(
        BottomNavItem(Screen.HomeTab.route, "홈", Icons.Filled.Home),
        BottomNavItem(Screen.CouponsTab.route, "쿠폰함", Icons.Filled.Inventory2),
        BottomNavItem(Screen.AddTab.route, "추가", Icons.Filled.AddCircle),
        BottomNavItem(Screen.SettingsTab.route, "설정", Icons.Filled.Settings)
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
                                    if (selected) return@NavigationBarItem
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id)
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = { Text(item.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF191970),
                                    selectedTextColor = Color(0xFF191970)
                                )
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
