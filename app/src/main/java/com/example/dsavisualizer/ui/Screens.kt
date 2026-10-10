package com.example.dsavisualizer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    title: String,
    nav: NavController? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        containerColor = DsaTheme.bg,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, color = DsaTheme.textMain) },
                navigationIcon = {
                    if (nav != null) {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = DsaTheme.textMain)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DsaTheme.bg)
            )
        }
    ) { pad ->
        Column(
            Modifier.padding(pad).padding(horizontal = 16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
fun MenuCard(title: String, subtitle: String, onClick: () -> Unit) {
    ModernRowCard(title, subtitle, Icons.Default.ChevronRight, DsaTheme.primary, onClick = onClick)
}

@Composable
fun InfoCard(title: String, complexity: String, description: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DsaTheme.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DsaTheme.textMain)
            Text(complexity, style = MaterialTheme.typography.bodySmall, color = DsaTheme.primary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium, color = DsaTheme.textSub, fontSize = 13.sp, lineHeight = 18.sp)
        }
    }
}

