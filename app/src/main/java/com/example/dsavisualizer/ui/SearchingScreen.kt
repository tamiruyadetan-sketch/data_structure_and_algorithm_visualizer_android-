package com.example.dsavisualizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dsavisualizer.engine.Algorithms
import com.example.dsavisualizer.model.Mark

@Composable
fun SearchingScreen(nav: NavController) {
    var nums by remember { mutableStateOf("5, 3, 8, 1, 9") }
    var target by remember { mutableStateOf("8") }
    var mode by remember { mutableStateOf<String?>(null) }

    AppScaffold("Searching Playground", nav) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Array:", modifier = Modifier.padding(end = 8.dp))
                OutlinedTextField(
                    value = nums, onValueChange = { nums = it },
                    modifier = Modifier.weight(1f), singleLine = true,
                    placeholder = { Text("e.g. 5, 3, 8") }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = target, onValueChange = { target = it },
                    label = { Text("Target") }, singleLine = true, modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { mode = "linear" }, modifier = Modifier.weight(1f)) { Text("Linear") }
                Button(onClick = { mode = "binary" }, modifier = Modifier.weight(1f)) { Text("Binary") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { mode = "jump" }, modifier = Modifier.weight(1f)) { Text("Jump") }
                Button(onClick = { mode = "exponential" }, modifier = Modifier.weight(1f)) { Text("Exponential") }
                Button(onClick = { mode = "ternary" }, modifier = Modifier.weight(1f)) { Text("Ternary") }
            }
            
            Spacer(Modifier.height(8.dp))
            
            if (mode != null && nums.isNotBlank() && target.isNotBlank()) {
                val list = nums.split(",").mapNotNull { it.trim().toIntOrNull() }
                val t = target.toIntOrNull()
                
                if (list.isNotEmpty() && t != null) {
                    val steps = when (mode) {
                        "linear" -> Algorithms.linearSearch(list, t)
                        "binary" -> Algorithms.binarySearch(list.sorted(), t)
                        "jump" -> Algorithms.jumpSearch(list, t)
                        "exponential" -> Algorithms.exponentialSearch(list, t)
                        "ternary" -> Algorithms.ternarySearch(list, t)
                        else -> emptyList()
                    }
                    
                    val foundStep = steps.find { step -> 
                        step.marks.values.any { it == Mark.FOUND } 
                    }
                    
                    Text(
                        "Result: ${if (foundStep != null) "Found!" else "Not found"}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (foundStep != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { nav.navigate("viz/$mode") }) {
                        Text("View Step-by-Step Visualization")
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                InfoCard(
                    "Linear Search", "Best O(1) · Avg/Worst O(n)",
                    "Checks every element one by one from the start until the target is found or the array ends."
                )
                InfoCard(
                    "Binary Search", "Best O(1) · Avg/Worst O(log n)",
                    "Works on a sorted array by repeatedly dividing the search interval in half."
                )
                InfoCard(
                    "Jump Search", "Best O(1) · Avg/Worst O(√n)",
                    "Jumps ahead in √n-size blocks on a sorted array, then linearly scans the block that may contain the target."
                )
                InfoCard(
                    "Exponential Search", "Best O(1) · Avg/Worst O(log n)",
                    "Doubles the range (1, 2, 4, 8, …) until it brackets the target, then binary-searches that range."
                )
                InfoCard(
                    "Ternary Search", "Best O(1) · Avg/Worst O(log₃ n)",
                    "Splits the sorted range into three parts with two midpoints and keeps only the third that can contain the target."
                )
            }
        }
    }
}
