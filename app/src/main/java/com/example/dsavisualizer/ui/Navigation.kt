package com.example.dsavisualizer.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private val tabRoutes = setOf("home", "categories", "searchTab", "profile")

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    val showBottomBar = currentRoute in tabRoutes

    Scaffold(
        containerColor = DsaTheme.bg,
        bottomBar = { if (showBottomBar) BottomNavBar(nav, currentRoute) }
    ) { pad ->
        NavHost(
            nav,
            startDestination = "home",
            modifier = androidx.compose.ui.Modifier.padding(pad)
        ) {
            // ---- Bottom-nav tabs ----
            composable("home") { ModernHomeScreen(nav) }
            composable("categories") { ModernCategoriesScreen(nav) }
            composable("searchTab") { ModernSearchScreen(nav) }
            composable("profile") { ModernProfileScreen(nav) }

            // ---- Wiki detail + category lists ----
            composable("m/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                if (WikiData.byId(id) != null) ModernDetailScreen(nav, id)
                else ModernCategoryListScreen(nav, id)
            }

            // ---- Playground / legacy screens ----
            composable("sorting") { SortingScreen(nav) }
            composable("searching") { SearchingScreen(nav) }
            composable("practice") { PracticeScreen(nav) }
            composable("struct/{type}") { StructureScreen(nav, it.arguments?.getString("type") ?: "stack") }
            composable("viz/{id}") { backStackEntry ->
                VisualizerScreen(nav, backStackEntry.arguments?.getString("id") ?: "")
            }
        }
    }
}
