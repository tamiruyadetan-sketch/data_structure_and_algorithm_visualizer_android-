package com.example.dsavisualizer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dsavisualizer.engine.Algorithms

// ================= HOME TAB =================

@Composable
fun ModernHomeScreen(nav: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(4.dp))
        Text(
            "Data Structures &\nAlgorithms",
            fontSize = 28.sp, fontWeight = FontWeight.Black, color = DsaTheme.textMain, lineHeight = 32.sp
        )
        Text("Learn · Practice · Build your skills", fontSize = 14.sp, color = DsaTheme.textSub)

        HeroCard(
            "Data Structures",
            "7 topics · Stack, Queue, Trees & more",
            Modifier.clickable { nav.navigate("m/structures") }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HeroCard(
                "Algorithms",
                "15 topics",
                Modifier.weight(1f).clickable { nav.navigate("m/algorithms") },
                height = 120.dp
            )
        }

        HeroCard(
            "Quick Access",
            "Sorting · Searching · Practice",
            Modifier.clickable { nav.navigate("practice") },
            height = 110.dp
        )

        SectionHeader("Quick Access")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ModernRowCard(
                "Favorites", "Saved items", Icons.Default.Star, DsaTheme.accentYellow,
                onClick = { nav.navigate("m/structures") }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ModernRowCard(
                "Practice", "Solve problems", Icons.Default.Quiz, DsaTheme.accentPurple,
                onClick = { nav.navigate("practice") }, modifier = Modifier.weight(1f)
            )
            ModernRowCard(
                "Visualize", "Sorting steps", Icons.Default.PlayCircle, DsaTheme.accentGreen,
                onClick = { nav.navigate("sorting") }, modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

// ================= CATEGORIES TAB =================

@Composable
fun ModernCategoriesScreen(nav: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(4.dp))
        Text("Data Structures", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DsaTheme.textMain)
        ModernRowCard("Arrays", "Find elements by index", Icons.Default.GridOn, DsaTheme.accentBlue) { nav.navigate("m/array") }
        ModernRowCard("Linked List", "Nodes connected in order", Icons.Default.Link, DsaTheme.accentTeal) { nav.navigate("m/linkedlist") }
        ModernRowCard("Stack", "LIFO - Last In, First Out", Icons.Default.Layers, DsaTheme.accentPurple) { nav.navigate("m/stack") }
        ModernRowCard("Queue", "FIFO - First In, First Out", Icons.Default.Queue, DsaTheme.accentOrange) { nav.navigate("m/queue") }
        ModernRowCard("Trees", "Hierarchical data structure", Icons.Default.Park, DsaTheme.accentGreen) { nav.navigate("m/tree") }
        ModernRowCard("Graph", "Nodes and edges", Icons.Default.Hub, DsaTheme.accentPink) { nav.navigate("m/graph") }
        ModernRowCard("Hash Table", "Key-value pairs", Icons.Default.TableChart, DsaTheme.accentYellow) { nav.navigate("m/hashtable") }
        ModernRowCard("Heap", "Complete binary tree", Icons.Default.Landscape, DsaTheme.accentPurple) { nav.navigate("m/heap") }
        ModernRowCard("Trie", "Prefix tree for strings", Icons.Default.AccountTree, DsaTheme.accentTeal) { nav.navigate("m/trie") }

        Spacer(Modifier.height(4.dp))
        Text("Algorithms", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DsaTheme.textMain)
        ModernRowCard("Sorting Algorithms", "Arrange elements in order", Icons.Default.Calculate, DsaTheme.accentOrange) { nav.navigate("m/sorting") }
        ModernRowCard("Searching Algorithms", "Find elements by index", Icons.Default.Search, DsaTheme.accentBlue) { nav.navigate("m/searching") }
        ModernRowCard("Graph Algorithms", "Traversal, paths, networks", Icons.Default.DeviceHub, DsaTheme.accentGreen) { nav.navigate("m/graphAlgos") }
        Spacer(Modifier.height(16.dp))
    }
}

// ================= SEARCH TAB =================

@Composable
fun ModernSearchScreen(nav: NavController) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) {
        if (query.isBlank()) emptyList()
        else WikiData.all.filter { it.title.contains(query.trim(), ignoreCase = true) }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search algorithms, structures…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = DsaTheme.primary) },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DsaTheme.primary,
                unfocusedBorderColor = DsaTheme.textSub.copy(alpha = 0.3f),
                focusedContainerColor = DsaTheme.card,
                unfocusedContainerColor = DsaTheme.card
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if (query.isBlank()) {
            Text("Try: \"quick sort\", \"heap\", \"binary\"…", color = DsaTheme.textSub, fontSize = 13.sp)
        } else if (results.isEmpty()) {
            Text("No results for \"$query\"", color = DsaTheme.textSub, fontSize = 14.sp)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(results) { e ->
                    ModernRowCard(e.title, e.definition.take(48) + "…", e.icon, e.iconColor) {
                        nav.navigate("m/${e.id}")
                    }
                }
            }
        }
    }
}

// ================= PROFILE TAB =================

@Composable
fun ModernProfileScreen(nav: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier.size(86.dp).background(DsaTheme.gradient, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("L", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black)
        }
        Text("Learner", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DsaTheme.textMain)
        Text("DSA Student", fontSize = 13.sp, color = DsaTheme.textSub)

        Spacer(Modifier.height(4.dp))
        SectionHeader("My Progress")
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProgressRow("Data Structures", "${WikiData.structures.size} topics", 0.8f, DsaTheme.primary)
                ProgressRow("Sorting", "${WikiData.sorting.size} algorithms", 0.6f, DsaTheme.accentOrange)
                ProgressRow("Searching", "${WikiData.searching.size} algorithms", 0.7f, DsaTheme.accentGreen)
                ProgressRow("Graphs", "${WikiData.graphAlgos.size} algorithms", 0.4f, DsaTheme.accentPink)
            }
        }

        SectionHeader("Tools")
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                ProfileRow(Icons.Default.PlayCircle, DsaTheme.accentGreen, "Sorting Playground", "Watch sorts animate") { nav.navigate("sorting") }
                HorizontalDivider(color = DsaTheme.bg)
                ProfileRow(Icons.Default.Search, DsaTheme.accentBlue, "Searching Playground", "Watch searches animate") { nav.navigate("searching") }
                HorizontalDivider(color = DsaTheme.bg)
                ProfileRow(Icons.Default.Quiz, DsaTheme.accentPurple, "Practice Quiz", "${Algorithms.all.size} algorithms covered") { nav.navigate("practice") }
                HorizontalDivider(color = DsaTheme.bg)
            }
        }

        SectionHeader("About")
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("DSA Visualizer", fontWeight = FontWeight.Bold, color = DsaTheme.textMain)
                Text("Version 1.0 · Learn • Visualize • Practice", fontSize = 13.sp, color = DsaTheme.textSub)
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}


@Composable
private fun ProgressRow(label: String, detail: String, fraction: Float, color: Color) {
    Column {
        Row(Modifier.fillMaxWidth()) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DsaTheme.textMain)
            Spacer(Modifier.weight(1f))
            Text(detail, fontSize = 12.sp, color = DsaTheme.textSub)
        }
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { fraction },
            color = color,
            trackColor = color.copy(alpha = 0.12f),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProfileRow(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color, title: String, sub: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DsaTheme.textMain)
            Text(sub, fontSize = 12.sp, color = DsaTheme.textSub)
        }
        Icon(Icons.Default.ChevronRight, null, tint = DsaTheme.textSub.copy(alpha = 0.6f))
    }
}

// ================= CATEGORY LIST SCREENS =================

@Composable
fun ModernCategoryListScreen(nav: NavController, categoryId: String) {
    val entries: List<WikiEntry> = when (categoryId) {
        "structures" -> WikiData.structures
        "algorithms" -> WikiData.searching + WikiData.sorting + WikiData.graphAlgos
        "sorting" -> WikiData.sorting
        "searching" -> WikiData.searching
        "graphAlgos" -> WikiData.graphAlgos
        else -> emptyList()
    }
    val title = when (categoryId) {
        "structures" -> "Data Structures"
        "algorithms" -> "Algorithms"
        "sorting" -> "Sorting Algorithms"
        "searching" -> "Searching Algorithms"
        "graphAlgos" -> "Graph Algorithms"
        else -> "Topics"
    }

    Scaffold(
        containerColor = DsaTheme.bg,
        topBar = { ModernTopBar(title, nav) }
    ) { pad ->
        Column(
            Modifier.padding(pad).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            entries.forEach { e ->
                ModernRowCard(e.title, e.definition.take(52) + "…", e.icon, e.iconColor) {
                    nav.navigate("m/${e.id}")
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ================= DETAIL SCREEN =================

@Composable
fun ModernDetailScreen(nav: NavController, id: String) {
    val entry = WikiData.byId(id) ?: run {
        ModernTopBar("Not found", nav)
        return
    }

    Scaffold(
        containerColor = DsaTheme.bg,
        topBar = { ModernTopBar(entry.title, nav) }
    ) { pad ->
        Column(
            Modifier.padding(pad).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            // Big colored header card with icon
            Box(
                Modifier.fillMaxWidth().height(92.dp).background(DsaTheme.gradient, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(Modifier.padding(horizontal = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(52.dp).background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(entry.icon, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(entry.title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
                        Text(entry.timeComplexity, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    }
                }
            }

            WhatIsCard(entry.title, entry.definition, entry.types)

            if (entry.steps.isNotEmpty()) StepsCard(entry.steps)

            ComplexityCard(entry.timeComplexity, entry.spaceComplexity)
            if (entry.best.isNotEmpty() && entry.worst.isNotEmpty()) {
                BestWorstCard(entry.best, entry.worst)
            }

            ModernCodeCard(entry.code)

            entry.vizId?.let { route ->
                val target = if (route.startsWith("struct/") || route.startsWith("viz/")) route else "viz/$route"
                Button(
                    onClick = { nav.navigate(target) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DsaTheme.primary)
                ) {
                    Icon(Icons.Default.PlayCircle, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (route.startsWith("struct/")) "View Example" else "View Code / Visualize", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
