package com.example.dsavisualizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dsavisualizer.engine.Algorithms

@Composable
fun SortingScreen(nav: NavController) {
    var selected by remember { mutableStateOf<String?>(null) }
    var nums by remember { mutableStateOf("5, 3, 8, 1, 9, 2") }

    AppScaffold("Sorting Playground", nav) {
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
                Button(onClick = { selected = "bubble" }, modifier = Modifier.weight(1f)) { Text("Bubble") }
                Button(onClick = { selected = "selection" }, modifier = Modifier.weight(1f)) { Text("Selection") }
                Button(onClick = { selected = "insertion" }, modifier = Modifier.weight(1f)) { Text("Insertion") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { selected = "merge" }, modifier = Modifier.weight(1f)) { Text("Merge") }
                Button(onClick = { selected = "quick" }, modifier = Modifier.weight(1f)) { Text("Quick") }
                Button(onClick = { selected = "heap" }, modifier = Modifier.weight(1f)) { Text("Heap") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { selected = "shell" }, modifier = Modifier.weight(1f)) { Text("Shell") }
                Button(onClick = { selected = "counting" }, modifier = Modifier.weight(1f)) { Text("Counting") }
                Button(onClick = { selected = "radix" }, modifier = Modifier.weight(1f)) { Text("Radix") }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { selected = "bucket" }, modifier = Modifier.weight(1f)) { Text("Bucket") }
            }
            
            if (selected != null) {
                val list = nums.split(",").mapNotNull { it.trim().toIntOrNull() }
                if (list.isNotEmpty()) {
                    val steps = when (selected) {
                        "bubble" -> Algorithms.bubbleSort(list, null)
                        "selection" -> Algorithms.selectionSort(list, null)
                        "insertion" -> Algorithms.insertionSort(list, null)
                        "merge" -> Algorithms.mergeSort(list, null)
                        "quick" -> Algorithms.quickSort(list, null)
                        "heap" -> Algorithms.heapSort(list, null)
                        "shell" -> Algorithms.shellSort(list, null)
                        "counting" -> Algorithms.countingSort(list, null)
                        "radix" -> Algorithms.radixSort(list, null)
                        "bucket" -> Algorithms.bucketSort(list, null)
                        else -> emptyList()
                    }
                    val finalResult = steps.lastOrNull()?.values ?: list
                    Text(
                        "Result: ${finalResult.joinToString(", ")}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(onClick = { nav.navigate("viz/$selected") }) {
                        Text("View Step-by-Step Visualization")
                    }
                }
            }
            
            Spacer(Modifier.height(8.dp))
            InfoCard(
                "Bubble Sort", "Best O(n) · Avg O(n²) · Worst O(n²) · Space O(1)",
                "Repeatedly compares adjacent elements and swaps them if they are in the wrong order."
            )
            InfoCard(
                "Selection Sort", "O(n²) · Space O(1)",
                "Finds the smallest element in the unsorted part and swaps it into place."
            )
            InfoCard(
                "Insertion Sort", "Best O(n) · Avg/Worst O(n²) · Space O(1)",
                "Builds the sorted array one element at a time by inserting each new element."
            )
            InfoCard(
                "Merge Sort", "O(n log n) all cases · Space O(n)",
                "Divide and conquer: split in half, sort each half recursively, then merge the sorted halves."
            )
            InfoCard(
                "Quick Sort", "Best/Avg O(n log n) · Worst O(n²) · Space O(log n)",
                "Picks a pivot, partitions smaller values left and larger right, then sorts each part recursively."
            )
            InfoCard(
                "Heap Sort", "O(n log n) all cases · Space O(1)",
                "Builds a max-heap, then repeatedly moves the largest element to the end and restores the heap."
            )
            InfoCard(
                "Shell Sort", "Best O(n log n) · Avg O(n^1.3) · Worst O(n²)",
                "Insertion sort over shrinking gaps: compares far-apart elements first, finishing with gap 1."
            )
            InfoCard(
                "Counting Sort", "O(n + k) · Space O(k)",
                "Counts occurrences of each value, then rebuilds the array in order. Great for small value ranges."
            )
            InfoCard(
                "Radix Sort", "O(d·(n + k)) · Space O(n + k)",
                "Sorts digit by digit (1s, 10s, 100s, …) with a stable sort per digit. No comparisons needed."
            )
            InfoCard(
                "Bucket Sort", "Avg O(n + k) · Worst O(n²) · Space O(n)",
                "Scatters values into range buckets, sorts each bucket, then concatenates them."
            )
        }
    }
}
