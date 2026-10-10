package com.example.dsavisualizer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

private data class Question(val text: String, val options: List<String>, val answer: Int)

private val questions = listOf(
    Question("Which data structure uses LIFO?", listOf("Queue", "Stack", "Graph", "Tree"), 1),
    Question("Which data structure uses FIFO?", listOf("Stack", "Tree", "Queue", "Heap"), 2),
    Question("Average time complexity of Binary Search?", listOf("O(n)", "O(log n)", "O(n²)", "O(1)"), 1),
    Question("Best case of Bubble Sort (with early exit)?", listOf("O(n)", "O(n log n)", "O(n²)", "O(1)"), 0),
    Question("Binary Search requires the array to be...", listOf("Empty", "Reversed", "Sorted", "Unique only"), 2),
    Question("Stack: Push(10), Push(20), Push(30), Pop(). What is on top?", listOf("10", "20", "30", "Empty"), 1),
    Question("Space complexity of Insertion Sort?", listOf("O(n)", "O(log n)", "O(n²)", "O(1)"), 3),
    Question("Which sorting algorithm merges two sorted halves?", listOf("Bubble Sort", "Merge Sort", "Insertion Sort", "Selection Sort"), 1),
    Question("Worst-case time complexity of Quick Sort?", listOf("O(n log n)", "O(n)", "O(n²)", "O(log n)"), 2),
    Question("Which sorting algorithm builds a max-heap first?", listOf("Heap Sort", "Merge Sort", "Radix Sort", "Counting Sort"), 0),
    Question("In a binary heap stored in an array, the children of index i are at...", listOf("i+1 and i+2", "2i and 2i+1", "2i+1 and 2i+2", "i/2 and i/2+1"), 2),
    Question("In a min-heap, the smallest element is always...", listOf("At the last index", "At the root", "In a leaf", "Anywhere"), 1),
    Question("Which structure stores strings so shared prefixes share one path?", listOf("BST", "Trie", "Hash Table", "Heap"), 1),
    Question("Which structure maps keys to buckets using a hash function?", listOf("Trie", "Heap", "Hash Table", "Linked List"), 2),
    Question("Average time complexity of a hash table lookup?", listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"), 0),
    Question("Graph traversal that explores level by level using a queue?", listOf("DFS", "BFS", "Dijkstra", "Binary Search"), 1),
    Question("Graph traversal that goes as deep as possible using a stack?", listOf("BFS", "DFS", "Heap Sort", "Jump Search"), 1),
    Question("A node in a doubly linked list stores...", listOf("Only next", "Only prev", "next and prev", "No pointers"), 2),
    Question("In a circular linked list, the last node points to...", listOf("NULL", "Itself", "The head", "A sentinel only"), 2),
    Question("Average time complexity of Jump Search on a sorted array?", listOf("O(n)", "O(√n)", "O(log n)", "O(n log n)"), 1)
)

@Composable
fun PracticeScreen(nav: NavController) {
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var selected by remember { mutableStateOf<Int?>(null) }

    AppScaffold("Practice", nav) {
        if (index >= questions.size) {
            Text("Quiz finished!", style = MaterialTheme.typography.headlineSmall)
            Text("Score: $score / ${questions.size}", style = MaterialTheme.typography.titleLarge)
            Button(onClick = { index = 0; score = 0; selected = null }) { Text("Try again") }
        } else {
            val q = questions[index]
            Text("Question ${index + 1} of ${questions.size}")
            Text(q.text, style = MaterialTheme.typography.titleMedium)
            q.options.forEachIndexed { i, opt ->
                val chosen = selected
                val label = when {
                    chosen == null -> opt
                    i == q.answer -> "✅ $opt"
                    i == chosen -> "❌ $opt"
                    else -> opt
                }
                OutlinedButton(
                    onClick = { if (selected == null) { selected = i; if (i == q.answer) score++ } },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(label) }
            }
            if (selected != null) {
                Button(onClick = { index++; selected = null }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (index == questions.lastIndex) "Finish" else "Next")
                }
            }
        }
    }
}
