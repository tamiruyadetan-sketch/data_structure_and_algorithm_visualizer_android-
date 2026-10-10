package com.example.dsavisualizer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

// ================= THEME COLORS (matching UI.png) =================

object DsaTheme {
    val primary = Color(0xFF3D2EE6)        // indigo
    val primaryDark = Color(0xFF2A1FB8)
    val gradient = Brush.horizontalGradient(listOf(Color(0xFF4A3AF0), Color(0xFF6C4DF6)))
    val bg = Color(0xFFF6F6FB)             // page background
    val card = Color.White
    val textMain = Color(0xFF1A1B2E)
    val textSub = Color(0xFF7A7B9A)
    val accentPurple = Color(0xFF8B5CF6)
    val accentTeal = Color(0xFF14B8A6)
    val accentOrange = Color(0xFFF97316)
    val accentPink = Color(0xFFEC4899)
    val accentBlue = Color(0xFF3B82F6)
    val accentGreen = Color(0xFF22C55E)
    val accentRed = Color(0xFFEF4444)
    val accentYellow = Color(0xFFEAB308)
}

// ================= TOP BAR =================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(title: String, nav: NavController? = null, actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        title = {
            Text(title, fontWeight = FontWeight.Bold, color = DsaTheme.textMain)
        },
        navigationIcon = {
            if (nav != null) {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DsaTheme.textMain)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = DsaTheme.bg)
    )
}

// ================= BOTTOM NAVIGATION =================

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Outlined.Home, "home"),
    BottomNavItem("Categories", Icons.Outlined.Category, "categories"),
    BottomNavItem("Search", Icons.Outlined.Search, "searchTab"),
    BottomNavItem("Profile", Icons.Outlined.Person, "profile")
)

@Composable
fun BottomNavBar(nav: NavController, currentRoute: String?) {
    NavigationBar(containerColor = DsaTheme.card, tonalElevation = 8.dp) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        nav.navigate(item.route) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, item.label) },
                label = { Text(item.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DsaTheme.primary,
                    selectedTextColor = DsaTheme.primary,
                    indicatorColor = DsaTheme.primary.copy(alpha = 0.12f),
                    unselectedIconColor = DsaTheme.textSub,
                    unselectedTextColor = DsaTheme.textSub
                )
            )
        }
    }
}

// ================= HOME HERO CARD =================

@Composable
fun HeroCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 150.dp
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(DsaTheme.gradient, RoundedCornerShape(20.dp))
    ) {
        // decorative circles
        Box(
            Modifier.align(Alignment.TopEnd).padding(top = 12.dp, end = 16.dp)
                .size(90.dp).background(Color.White.copy(alpha = 0.12f), CircleShape)
        )
        Box(
            Modifier.align(Alignment.CenterEnd).padding(end = 56.dp)
                .size(46.dp).background(Color.White.copy(alpha = 0.10f), CircleShape)
        )
        Text("</>", color = Color.White.copy(alpha = 0.25f), fontSize = 44.sp,
            fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.CenterEnd).padding(end = 28.dp))
        Column(Modifier.align(Alignment.CenterStart).padding(20.dp)) {
            Text(title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, lineHeight = 28.sp)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
        }
    }
}

// ================= ROW CARD (icon + title + subtitle + arrow) =================

@Composable
fun ModernRowCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(42.dp).background(iconBg.copy(alpha = 0.15f), RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconBg, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DsaTheme.textMain)
                Text(subtitle, fontSize = 12.sp, color = DsaTheme.textSub, lineHeight = 15.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = DsaTheme.textSub.copy(alpha = 0.6f))
        }
    }
}

// ================= "WHAT IS X?" INFO CARD =================

@Composable
fun WhatIsCard(title: String, body: String, types: List<String> = emptyList()) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("What is $title?", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DsaTheme.textMain)
            Spacer(Modifier.height(8.dp))
            Text(body, fontSize = 13.sp, lineHeight = 19.sp, color = DsaTheme.textSub)
            if (types.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                Text("Types", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DsaTheme.textMain)
                Spacer(Modifier.height(4.dp))
                types.forEach {
                    Text("•  $it", fontSize = 13.sp, color = DsaTheme.textSub, modifier = Modifier.padding(vertical = 1.dp))
                }
            }
        }
    }
}

// ================= COMPLEXITY CARDS =================

@Composable
fun ComplexityCard(time: String, space: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0EDFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Schedule, null, tint = DsaTheme.primary)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Time Complexity", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DsaTheme.textMain)
                Text(time, fontSize = 12.sp, color = DsaTheme.textSub)
            }
            Spacer(Modifier.width(20.dp))
            Icon(Icons.Default.Memory, null, tint = DsaTheme.accentPurple)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Space Complexity", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DsaTheme.textMain)
                Text(space, fontSize = 12.sp, color = DsaTheme.textSub)
            }
        }
    }
}

@Composable
fun BestWorstCard(best: String, worst: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F7F1)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Bolt, null, tint = Color(0xFF0E9F6E))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Best Complexity", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DsaTheme.textMain)
                Text(best, fontSize = 12.sp, color = DsaTheme.textSub)
            }
            Spacer(Modifier.width(20.dp))
            Icon(Icons.Default.TrendingDown, null, tint = Color(0xFF0E9F6E))
            Spacer(Modifier.width(10.dp))
            Column {
                Text("Worst Complexity", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DsaTheme.textMain)
                Text(worst, fontSize = 12.sp, color = DsaTheme.textSub)
            }
        }
    }
}

// ================= STEPS CARD =================

@Composable
fun StepsCard(steps: List<String>) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Steps", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DsaTheme.textMain)
            Spacer(Modifier.height(8.dp))
            steps.forEachIndexed { i, s ->
                Row(Modifier.padding(vertical = 3.dp)) {
                    Box(
                        Modifier.size(20.dp).background(DsaTheme.primary.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("${i + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DsaTheme.primary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(s, fontSize = 13.sp, color = DsaTheme.textSub, lineHeight = 17.sp)
                }
            }
        }
    }
}

// ================= CODE CARD =================

@Composable
fun ModernCodeCard(code: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B33)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("// Kotlin", color = Color(0xFF9C8CFF), fontSize = 11.sp, fontFamily = FontFamily.Monospace)
            Spacer(Modifier.height(6.dp))
            Text(
                code,
                color = Color(0xFFD6D2F5),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        }
    }
}

// ================= SECTION HEADER =================

@Composable
fun SectionHeader(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = DsaTheme.textMain)
}
