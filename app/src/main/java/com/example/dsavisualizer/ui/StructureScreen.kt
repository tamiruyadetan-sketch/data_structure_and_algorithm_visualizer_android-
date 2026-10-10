package com.example.dsavisualizer.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

@Composable
fun StructureScreen(nav: NavController, type: String) {
    when (type) {
        "array" -> ArrayContent(nav)
        "linkedlist" -> LinkedListContent(nav)
        "doublylinkedlist" -> DoublyLinkedListContent(nav)
        "circularlinkedlist" -> CircularLinkedListContent(nav)
        "stack" -> StackContent(nav)
        "queue" -> QueueContent(nav)
        "tree" -> TreeContent(nav)
        "heap" -> HeapContent(nav)
        "hashtable" -> HashTableContent(nav)
        "graph" -> GraphContent(nav)
        "trie" -> TrieContent(nav)
        else -> AppScaffold("Error", nav) { Text("Structure not found") }
    }
}

@Composable
fun CodeCard(code: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2B2B2B))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Kotlin Implementation", color = Color(0xFFA9B7C6), style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                code,
                color = Color(0xFFA9B7C6),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                modifier = Modifier.horizontalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
fun ArrayContent(nav: NavController) {
    val items = remember { mutableStateListOf(10, 20, 30, 40, 50) }
    var text by remember { mutableStateOf("") }
    var indexText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Array: Contiguous Memory") }

    AppScaffold("Array", nav) {
        InfoCard(
            "Array", 
            "Access: O(1), Search: O(n), Insert/Delete: O(n)",
            "A collection of items stored at contiguous memory locations. Elements are accessed by index starting from 0. Useful when you know the index of the element you want to retrieve."
        )
        
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = text, onValueChange = { text = it },
                label = { Text("Val") }, modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = indexText, onValueChange = { indexText = it },
                label = { Text("Idx") }, modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val v = text.toIntOrNull()
                val i = indexText.toIntOrNull()
                if (v != null) {
                    if (i != null && i in 0..items.size) {
                        items.add(i, v)
                        message = "Inserted $v at index $i"
                    } else {
                        items.add(v)
                        message = "Appended $v"
                    }
                    text = ""
                }
            }) { Text("Insert") }
            Button(onClick = {
                val i = indexText.toIntOrNull()
                if (i != null && i in 0 until items.size) {
                    val v = items.removeAt(i)
                    message = "Removed $v from index $i"
                } else if (items.isNotEmpty()) {
                    val v = items.removeAt(items.size - 1)
                    message = "Removed last element $v"
                }
            }) { Text("Delete") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        
        LazyRow(
            Modifier.fillMaxWidth().padding(vertical = 24.dp).border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(items) { index, item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(index.toString(), fontSize = 10.sp, color = Color.Gray)
                    Cell(item, width = 50, color = Color(0xFFBBDEFB))
                }
            }
        }

        CodeCard("""
// Accessing element
val element = array[index] // O(1)

// Inserting at index
fun insert(index: Int, value: Int) {
    // Requires shifting all elements to the right
    for (i in size downTo index + 1) {
        array[i] = array[i-1]
    }
    array[index] = value
}
        """.trimIndent())
    }
}

@Composable
fun LinkedListContent(nav: NavController) {
    val items = remember { mutableStateListOf(10, 20, 30) }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Linked List: Nodes and Pointers") }

    AppScaffold("Linked List", nav) {
        InfoCard(
            "Linked List (Singly)", 
            "Access: O(n), Search: O(n), Insert/Delete: O(1) at ends",
            "A sequence of nodes where each node contains data and a 'next' pointer. Unlike arrays, nodes are not stored contiguously in memory, allowing for efficient dynamic sizing."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it },
            label = { Text("Value") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                text.toIntOrNull()?.let { items.add(0, it); message = "Added $it to Head"; text = "" }
            }) { Text("Add Head") }
            Button(onClick = {
                text.toIntOrNull()?.let { items.add(it); message = "Added $it to Tail"; text = "" }
            }) { Text("Add Tail") }
            Button(onClick = {
                if (items.isNotEmpty()) {
                    val v = items.removeAt(0)
                    message = "Removed Head: $v"
                }
            }) { Text("Rem Head") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (items.isEmpty()) Text("NULL", color = Color.Gray, modifier = Modifier.padding(16.dp))
                items.forEachIndexed { index, item ->
                    NodeView(item, index == 0, index == items.size - 1)
                    if (index < items.size - 1) {
                        Pointer()
                    } else {
                        Pointer("NULL")
                    }
                }
            }
        }

        CodeCard("""
class Node(val data: Int, var next: Node? = null)

fun addAtHead(value: Int) {
    val newNode = Node(value)
    newNode.next = head
    head = newNode
}

fun removeHead() {
    head = head?.next
}
        """.trimIndent())
    }
}

@Composable
fun NodeView(value: Int, isHead: Boolean, isTail: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isHead) Text("HEAD", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF43A047))
        else Spacer(Modifier.height(12.dp))
        
        Box(
            Modifier.size(65.dp, 55.dp)
                .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$value", fontWeight = FontWeight.Bold)
                HorizontalDivider(Modifier.width(45.dp), thickness = 1.dp, color = Color(0xFF4CAF50))
                Text("next", fontSize = 9.sp, color = Color.Gray)
            }
        }
        if (isTail) Text("TAIL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
    }
}

@Composable
fun Pointer(label: String = "") {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 4.dp)) {
        Text("──▶", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), fontSize = 18.sp)
        if (label.isNotEmpty()) Text(label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun StackContent(nav: NavController) {
    val items = remember { mutableStateListOf(10, 20, 30) }
    var text by remember { mutableStateOf("40") }
    var message by remember { mutableStateOf("Ready") }

    AppScaffold("Stack", nav) {
        InfoCard(
            "Stack (LIFO)", 
            "Push: O(1), Pop: O(1), Peek: O(1)",
            "A Linear data structure that follows the Last-In-First-Out principle. The last element added to the stack is the first one to be removed."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it }, singleLine = true,
            label = { Text("Value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val v = text.toIntOrNull()
                if (v != null) { items.add(v); message = "PUSH($v)"; text = "" }
            }) { Text("Push") }
            Button(onClick = {
                if (items.isNotEmpty()) {
                    val v = items.removeAt(items.lastIndex)
                    message = "POP → $v"
                } else message = "Stack Underflow!"
            }) { Text("Pop") }
            OutlinedButton(onClick = {
                message = if (items.isEmpty()) "Empty" else "PEEK → ${items.last()}"
            }) { Text("Peek") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = Color(0xFFD32F2F))
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, 
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            if (items.isNotEmpty()) {
                Text("TOP", fontWeight = FontWeight.Bold, color = Color.Red)
                Text("↓", fontWeight = FontWeight.Bold)
            }
            items.reversed().forEachIndexed { index, item ->
                Cell(item, color = if (index == 0) Color(0xFFFFCDD2) else Color(0xFFF5F5F5))
            }
            Box(Modifier.width(130.dp).height(4.dp).background(Color.DarkGray))
        }

        CodeCard("""
class Stack<T> {
    private val elements = mutableListOf<T>()
    
    fun push(item: T) = elements.add(item)
    
    fun pop(): T? = if (isEmpty()) null 
                   else elements.removeAt(elements.size - 1)
    
    fun peek(): T? = elements.lastOrNull()
}
        """.trimIndent())
    }
}

@Composable
fun QueueContent(nav: NavController) {
    val items = remember { mutableStateListOf(10, 20, 30) }
    var text by remember { mutableStateOf("40") }
    var message by remember { mutableStateOf("Ready") }

    AppScaffold("Queue", nav) {
        InfoCard(
            "Queue (FIFO)", 
            "Enqueue: O(1), Dequeue: O(1), Peek: O(1)",
            "A linear structure which follows a particular order in which the operations are performed. The order is First In First Out (FIFO)."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it }, singleLine = true,
            label = { Text("Value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val v = text.toIntOrNull()
                if (v != null) { items.add(v); message = "ENQUEUE($v)"; text = "" }
            }) { Text("Enqueue") }
            Button(onClick = {
                if (items.isNotEmpty()) {
                    val v = items.removeAt(0)
                    message = "DEQUEUE → $v"
                } else message = "Queue Empty!"
            }) { Text("Dequeue") }
            OutlinedButton(onClick = {
                message = if (items.isEmpty()) "Empty" else "PEEK → ${items.first()}"
            }) { Text("Peek") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = Color(0xFF1976D2))

        Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (items.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 8.dp)) {
                        Text("FRONT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Blue)
                        Text("↴", fontWeight = FontWeight.Bold)
                    }
                }
                items.forEachIndexed { index, it ->
                    Cell(it, width = 60, color = if (index == 0) Color(0xFFBBDEFB) else if (index == items.size - 1) Color(0xFFC8E6C9) else Color(0xFFF5F5F5))
                    if (index < items.size - 1) Spacer(Modifier.width(4.dp))
                }
                if (items.isNotEmpty()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(start = 8.dp)) {
                        Text("REAR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF388E3C))
                        Text("↲", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        CodeCard("""
class Queue<T> {
    private val elements = mutableListOf<T>()
    
    fun enqueue(item: T) = elements.add(item)
    
    fun dequeue(): T? = if (isEmpty()) null 
                       else elements.removeAt(0)
}
        """.trimIndent())
    }
}

@Composable
fun TreeContent(nav: NavController) {
    var root by remember { mutableStateOf<BSTNode?>(null) }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Binary Search Tree") }

    AppScaffold("Tree (BST)", nav) {
        InfoCard(
            "Binary Search Tree", 
            "Search/Insert/Delete: O(log n) average, O(n) worst",
            "A node-based binary tree data structure which has the following properties: The left subtree of a node contains only nodes with keys lesser than the node’s key. The right subtree contains only nodes with keys greater than the node’s key."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it },
            label = { Text("Value") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                text.toIntOrNull()?.let {
                    root = insertBST(root, it)
                    message = "Inserted $it"
                    text = ""
                }
            }) { Text("Insert") }
            Button(onClick = {
                root = null
                message = "Tree cleared"
            }) { Text("Clear") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        // ---- Traversal tabs (Preorder / Inorder / Postorder / Level-order) ----
        var traversalTab by remember { mutableStateOf(0) }
        val tabs = listOf("Preorder", "Inorder", "Postorder", "Level Order")
        TabRow(
            selectedTabIndex = traversalTab,
            containerColor = DsaTheme.card,
            contentColor = DsaTheme.primary
        ) {
            tabs.forEachIndexed { i, t -> Tab(selected = traversalTab == i, onClick = { traversalTab = i }, text = { Text(t, fontSize = 12.sp) }) }
        }
        if (root != null) {
            val seq = when (traversalTab) {
                0 -> preorder(root!!)
                1 -> inorder(root!!)
                2 -> postorder(root!!)
                else -> levelOrder(root!!)
            }
            Text(
                "${tabs[traversalTab]}:  " + seq.joinToString(" → "),
                fontWeight = FontWeight.Bold,
                color = DsaTheme.primary,
                fontSize = 14.sp
            )
        }

        Box(
            Modifier.fillMaxWidth().height(350.dp).border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)).padding(16.dp), 
            contentAlignment = Alignment.TopCenter
        ) {
            if (root == null) Text("Empty Tree", color = Color.Gray, modifier = Modifier.align(Alignment.Center))
            else root?.let { TreeView(it) }
        }

        CodeCard("""
class Node(val value: Int, var left: Node? = null, var right: Node? = null)

fun insert(root: Node?, value: Int): Node {
    if (root == null) return Node(value)
    if (value < root.value) 
        root.left = insert(root.left, value)
    else if (value > root.value) 
        root.right = insert(root.right, value)
    return root
}
        """.trimIndent())
    }
}

class BSTNode(val value: Int, var left: BSTNode? = null, var right: BSTNode? = null)

fun insertBST(root: BSTNode?, value: Int): BSTNode {
    if (root == null) return BSTNode(value)
    if (value < root.value) root.left = insertBST(root.left, value)
    else if (value > root.value) root.right = insertBST(root.right, value)
    return root
}

fun preorder(n: BSTNode): List<Int> = listOf(n.value) + (n.left?.let { preorder(it) } ?: emptyList()) + (n.right?.let { preorder(it) } ?: emptyList())
fun inorder(n: BSTNode): List<Int> = (n.left?.let { inorder(it) } ?: emptyList()) + listOf(n.value) + (n.right?.let { inorder(it) } ?: emptyList())
fun postorder(n: BSTNode): List<Int> = (n.left?.let { postorder(it) } ?: emptyList()) + (n.right?.let { postorder(it) } ?: emptyList()) + listOf(n.value)
fun levelOrder(n: BSTNode): List<Int> {
    val out = mutableListOf<Int>()
    val q = ArrayDeque<BSTNode>(); q.add(n)
    while (q.isNotEmpty()) {
        val cur = q.removeFirst(); out.add(cur.value)
        cur.left?.let(q::add); cur.right?.let(q::add)
    }
    return out
}

@Composable
fun TreeView(node: BSTNode) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(45.dp).background(Color(0xFFFFF3E0), CircleShape).border(2.dp, Color(0xFFFF9800), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("${node.value}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Row(horizontalArrangement = Arrangement.Center) {
            if (node.left != null || node.right != null) {
                Column(Modifier.padding(horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (node.left != null) {
                        Text("↙", color = Color.Gray, fontSize = 16.sp)
                        TreeView(node.left!!)
                    } else {
                        Spacer(Modifier.size(45.dp))
                    }
                }
                Column(Modifier.padding(horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (node.right != null) {
                        Text("↘", color = Color.Gray, fontSize = 16.sp)
                        TreeView(node.right!!)
                    } else {
                        Spacer(Modifier.size(45.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun Cell(v: Int, width: Int = 120, color: Color = Color(0xFFE3F2FD)) {
    Box(
        Modifier.width(width.dp).height(44.dp)
            .background(color, RoundedCornerShape(6.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) { Text("$v", fontWeight = FontWeight.Medium) }
}

// ================= DOUBLY LINKED LIST =================

@Composable
fun DoublyLinkedListContent(nav: NavController) {
    val items = remember { mutableStateListOf(10, 20, 30) }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Doubly Linked List: next + prev pointers") }

    AppScaffold("Doubly Linked List", nav) {
        InfoCard(
            "Doubly Linked List",
            "Access: O(n), Search: O(n), Insert/Delete at ends: O(1)",
            "Each node stores data plus two pointers: next and prev. Traversal works in both directions and a known node can be deleted in O(1)."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it },
            label = { Text("Value") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                text.toIntOrNull()?.let { items.add(0, it); message = "Added $it at head"; text = "" }
            }) { Text("Add Head") }
            Button(onClick = {
                text.toIntOrNull()?.let { items.add(it); message = "Added $it at tail"; text = "" }
            }) { Text("Add Tail") }
            Button(onClick = {
                if (items.isNotEmpty()) { val v = items.removeAt(items.lastIndex); message = "Removed Tail: $v" }
            }) { Text("Rem Tail") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("NULL ", color = Color.Gray)
                items.forEachIndexed { index, item ->
                    DNodeView(item)
                    if (index < items.size - 1) Pointer("\u21C4") else Text(" \u21C4 NULL", color = Color.Gray)
                }
            }
        }

        CodeCard("""
class DNode(val data: Int,
            var next: DNode? = null,
            var prev: DNode? = null)

fun addAtTail(value: Int) {
    val node = DNode(value)
    node.prev = tail
    tail?.next = node
    tail = node
}
        """.trimIndent())
    }
}

@Composable
fun DNodeView(value: Int) {
    Box(
        Modifier.size(80.dp, 55.dp)
            .background(Color(0xFFEDE7F6), RoundedCornerShape(8.dp))
            .border(2.dp, Color(0xFF7E57C2), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("p", fontSize = 10.sp, color = Color(0xFF7E57C2))
            Text("$value", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
            Text("n", fontSize = 10.sp, color = Color(0xFF7E57C2))
        }
    }
}

// ================= CIRCULAR LINKED LIST =================

@Composable
fun CircularLinkedListContent(nav: NavController) {
    val items = remember { mutableStateListOf(10, 20, 30) }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Circular Linked List: tail points back to head") }

    AppScaffold("Circular Linked List", nav) {
        InfoCard(
            "Circular Linked List",
            "Access: O(n), Search: O(n), Insert at ends: O(1) with tail pointer",
            "Like a singly linked list, but the last node points back to the head instead of NULL, forming a circle. Useful for round-robin scheduling."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it },
            label = { Text("Value") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                text.toIntOrNull()?.let { items.add(0, it); message = "Added $it at head"; text = "" }
            }) { Text("Add Head") }
            Button(onClick = {
                text.toIntOrNull()?.let { items.add(it); message = "Added $it at tail"; text = "" }
            }) { Text("Add Tail") }
            Button(onClick = {
                if (items.isNotEmpty()) { val v = items.removeAt(0); message = "Removed Head: $v" }
            }) { Text("Rem Head") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
            Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    items.forEachIndexed { index, item ->
                        NodeView(item, index == 0, index == items.size - 1)
                        if (index < items.size - 1) Pointer()
                    }
                }
            }
            if (items.isNotEmpty()) {
                Text("\u21B0 tail.next points back to HEAD \u21B0", fontSize = 12.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
            }
        }

        CodeCard("""
class CNode(val data: Int, var next: CNode? = null)

// tail.next always points to head
fun addAtTail(value: Int) {
    val node = CNode(value)
    if (tail == null) { tail = node; node.next = node }
    else {
        node.next = tail.next
        tail.next = node
        tail = node
    }
}
        """.trimIndent())
    }
}

// ================= HEAP (MIN-HEAP) =================

class MinHeapOps {
    companion object {
        fun siftUp(a: MutableList<Int>, i: Int) {
            var c = i
            while (c > 0) {
                val p = (c - 1) / 2
                if (a[c] < a[p]) { val t = a[c]; a[c] = a[p]; a[p] = t; c = p } else break
            }
        }

        fun siftDown(a: MutableList<Int>, i: Int) {
            var p = i
            while (true) {
                val l = p * 2 + 1
                val r = l + 1
                var s = p
                if (l < a.size && a[l] < a[s]) s = l
                if (r < a.size && a[r] < a[s]) s = r
                if (s == p) return
                val t = a[p]; a[p] = a[s]; a[s] = t
                p = s
            }
        }
    }
}

@Composable
fun HeapContent(nav: NavController) {
    val heap = remember { mutableStateListOf(5, 15, 10, 30, 25) }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Min-Heap stored in an array") }

    AppScaffold("Heap (Min-Heap)", nav) {
        InfoCard(
            "Binary Heap",
            "Insert: O(log n), Extract-min: O(log n), Peek: O(1)",
            "A complete binary tree stored in an array: for index i, children sit at 2i+1 and 2i+2, the parent at (i-1)/2. In a min-heap every parent is smaller than its children."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it },
            label = { Text("Value") }, modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                text.toIntOrNull()?.let { v ->
                    heap.add(v); MinHeapOps.siftUp(heap, heap.lastIndex)
                    message = "Inserted $v, sifted up"; text = ""
                }
            }) { Text("Insert") }
            Button(onClick = {
                if (heap.isEmpty()) message = "Heap empty!"
                else {
                    val min = heap.first()
                    heap[0] = heap.last()
                    heap.removeAt(heap.lastIndex)
                    if (heap.isNotEmpty()) MinHeapOps.siftDown(heap, 0)
                    message = "Extracted min: $min"
                }
            }) { Text("Extract Min") }
            OutlinedButton(onClick = {
                message = if (heap.isEmpty()) "Empty" else "PEEK (min) \u2192 ${heap.first()}"
            }) { Text("Peek") }
            Button(onClick = { heap.clear(); message = "Heap cleared" }) { Text("Clear") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            HeapTreeView(heap.toList())
            Text("Array representation:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                itemsIndexed(heap) { index, item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(index.toString(), fontSize = 10.sp, color = Color.Gray)
                        Cell(item, width = 44, color = Color(0xFFE0F7FA))
                    }
                }
            }
        }

        CodeCard("""
// Min-heap in an array: children of i are 2i+1, 2i+2
fun insert(a: MutableList<Int>, v: Int) {
    a.add(v)
    var c = a.size - 1
    while (c > 0) {
        val p = (c - 1) / 2
        if (a[c] < a[p]) { swap(a, c, p); c = p } else break
    }
}

fun extractMin(a: MutableList<Int>): Int {
    val min = a[0]
    a[0] = a.last(); a.removeAt(a.size - 1)
    siftDown(a, 0)
    return min
}
        """.trimIndent())
    }
}

@Composable
fun HeapTreeView(heap: List<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        var levelStart = 0
        var level = 0
        while (levelStart < heap.size) {
            val levelEnd = minOf(levelStart + (1 shl level), heap.size)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                for (i in levelStart until levelEnd) {
                    Box(
                        Modifier.size(40.dp).background(Color(0xFFE0F7FA), CircleShape)
                            .border(2.dp, Color(0xFF00ACC1), CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("${heap[i]}", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
                }
            }
            levelStart = levelEnd
            level++
        }
    }
}

// ================= HASH TABLE =================

@Composable
fun HashTableContent(nav: NavController) {
    val bucketCount = 5
    val entries = remember { mutableStateListOf<Pair<Int, String>>(1 to "one", 3 to "three", 6 to "six") }
    var keyText by remember { mutableStateOf("") }
    var valueText by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Hash Table: key % bucketCount = index") }

    AppScaffold("Hash Table", nav) {
        InfoCard(
            "Hash Table (Separate Chaining)",
            "Insert: O(1) avg, Search: O(1) avg, Delete: O(1) avg",
            "Stores key-value pairs. A hash function maps each key to a bucket index (here: key mod 5). Collisions are handled by chaining multiple entries in the same bucket."
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = keyText, onValueChange = { keyText = it },
                label = { Text("Key") }, modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = valueText, onValueChange = { valueText = it },
                label = { Text("Value") }, modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val k = keyText.toIntOrNull()
                val v = valueText
                if (k != null && v.isNotEmpty()) {
                    entries.removeAll { it.first == k }
                    entries.add(k to v)
                    message = "Put($k, \"$v\") \u2192 bucket ${k % bucketCount}"
                    keyText = ""; valueText = ""
                }
            }) { Text("Put") }
            OutlinedButton(onClick = {
                val k = keyText.toIntOrNull()
                if (k != null) {
                    val found = entries.find { it.first == k }
                    message = if (found != null) "Get($k) = \"${found.second}\" (bucket ${k % bucketCount})"
                              else "Get($k): key not found"
                }
            }) { Text("Get") }
            OutlinedButton(onClick = {
                val k = keyText.toIntOrNull()
                if (k != null) {
                    val removed = entries.removeAll { it.first == k }
                    message = if (removed) "Removed key $k" else "Key $k not found"
                }
            }) { Text("Remove") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        Column(Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            for (b in 0 until bucketCount) {
                val bucketItems = entries.filter { it.first % bucketCount == b }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("[$b]", fontWeight = FontWeight.Bold, color = Color(0xFF1976D2), modifier = Modifier.width(36.dp))
                    Text("\u2500\u25B6", color = Color.Gray, fontSize = 14.sp)
                    if (bucketItems.isEmpty()) Text("empty", color = Color.LightGray, fontSize = 13.sp)
                    bucketItems.forEach { (k, v) ->
                        Box(
                            Modifier.padding(horizontal = 4.dp)
                                .background(Color(0xFFFFF9C4), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFFFBC02D), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) { Text("$k: \"$v\"", fontSize = 13.sp) }
                        Text("\u2500\u25B6", color = Color.Gray, fontSize = 14.sp)
                    }
                    Text("NULL", color = Color.LightGray, fontSize = 12.sp)
                }
            }
        }

        CodeCard("""
class HashTable {
    private val buckets = Array(5) { mutableListOf<Pair<Int, String>>() }

    private fun hash(key: Int) = key % 5

    fun put(key: Int, value: String) {
        val b = buckets[hash(key)]
        b.removeAll { it.first == key }
        b.add(key to value)
    }

    fun get(key: Int): String? =
        buckets[hash(key)].find { it.first == key }?.second
}
        """.trimIndent())
    }
}

// ================= GRAPH =================

@Composable
fun GraphContent(nav: NavController) {
    var nodeCount by remember { mutableIntStateOf(5) }
    val edges = remember { mutableStateListOf(0 to 1, 1 to 2, 2 to 3, 3 to 4, 4 to 0, 1 to 3) }
    var uText by remember { mutableStateOf("") }
    var vText by remember { mutableStateOf("") }
    var highlight by remember { mutableStateOf<List<Int>>(emptyList()) }
    var order by remember { mutableStateOf<List<Int>>(emptyList()) }
    var message by remember { mutableStateOf("Undirected Graph") }

    LaunchedEffect(order) {
        if (order.isNotEmpty()) {
            highlight = emptyList()
            for (v in order) {
                highlight = highlight + v
                delay(500)
            }
        }
    }

    fun neighbors(u: Int): List<Int> =
        edges.filter { (a, b) -> a == u || b == u }.map { (a, b) -> if (a == u) b else a }.distinct().sorted()

    AppScaffold("Graph", nav) {
        InfoCard(
            "Undirected Graph (Adjacency List)",
            "Add edge: O(1), BFS: O(V + E), DFS: O(V + E)",
            "A set of vertices connected by edges. BFS explores level by level using a queue; DFS explores as deep as possible using a stack. Watch the traversal light up node by node."
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = uText, onValueChange = { uText = it }, label = { Text("From") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = vText, onValueChange = { vText = it }, label = { Text("To") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(onClick = {
                val u = uText.toIntOrNull(); val v = vText.toIntOrNull()
                if (u != null && v != null && u in 0 until nodeCount && v in 0 until nodeCount && u != v) {
                    val e1 = u to v; val e2 = v to u
                    if (e1 !in edges && e2 !in edges) {
                        edges.add(e1); message = "Added edge $u \u2014 $v"
                    } else message = "Edge already exists"
                    uText = ""; vText = ""
                }
            }) { Text("Add Edge") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (nodeCount < 8) { nodeCount++; message = "Added vertex ${nodeCount - 1}" }
            }) { Text("Add Vertex") }
            OutlinedButton(onClick = {
                if (nodeCount > 1) {
                    nodeCount--
                    edges.removeAll { (a, b) -> a == nodeCount || b == nodeCount }
                    message = "Removed vertex $nodeCount"
                }
            }) { Text("Remove Vertex") }
            Button(onClick = {
                val visited = mutableSetOf<Int>()
                val result = mutableListOf<Int>()
                val queue = ArrayDeque<Int>()
                queue.add(0); visited.add(0)
                while (queue.isNotEmpty()) {
                    val u = queue.removeFirst()
                    result.add(u)
                    for (v in neighbors(u)) if (v !in visited) { visited.add(v); queue.add(v) }
                }
                order = result
                message = "BFS from 0: ${result.joinToString(" \u2192 ")}"
            }) { Text("Run BFS") }
            Button(onClick = {
                val visited = mutableSetOf<Int>()
                val result = mutableListOf<Int>()
                val stack = ArrayDeque<Int>()
                stack.add(0)
                while (stack.isNotEmpty()) {
                    val u = stack.removeLast()
                    if (u in visited) continue
                    visited.add(u); result.add(u)
                    for (v in neighbors(u).reversed()) if (v !in visited) stack.add(v)
                }
                order = result
                message = "DFS from 0: ${result.joinToString(" \u2192 ")}"
            }) { Text("Run DFS") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)

        val textMeasurer = rememberTextMeasurer()
        Canvas(Modifier.fillMaxWidth().height(300.dp).padding(vertical = 8.dp)) {
            val n = nodeCount
            val radius = size.minDimension / 2 - 50f
            val cx = size.width / 2f
            val cy = size.height / 2f
            val positions = (0 until n).map { i ->
                val angle = 2.0 * Math.PI * i / n - Math.PI / 2
                Offset(cx + (radius * cos(angle)).toFloat(), cy + (radius * sin(angle)).toFloat())
            }
            edges.forEach { (u, v) ->
                if (u < n && v < n) drawLine(Color(0xFF90A4AE), positions[u], positions[v], strokeWidth = 4f)
            }
            positions.forEachIndexed { i, p ->
                val visited = i in highlight
                drawCircle(if (visited) Color(0xFF43A047) else Color(0xFFBBDEFB), 26f, p)
                drawCircle(if (visited) Color(0xFF2E7D32) else Color(0xFF1976D2), 26f, p, style = Stroke(width = 3f))
                val layout = textMeasurer.measure("$i", TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
                drawText(layout, color = Color.Black,
                    topLeft = Offset(p.x - layout.size.width / 2f, p.y - layout.size.height / 2f))
            }
        }

        CodeCard("""
// Adjacency list
class Graph(val v: Int) {
    val adj = Array(v) { mutableListOf<Int>() }
    fun addEdge(u: Int, w: Int) { adj[u].add(w); adj[w].add(u) }

    fun bfs(start: Int): List<Int> {
        val visited = BooleanArray(v)
        val order = mutableListOf<Int>()
        val queue = ArrayDeque<Int>()
        queue.add(start); visited[start] = true
        while (queue.isNotEmpty()) {
            val u = queue.removeFirst()
            order.add(u)
            for (w in adj[u]) if (!visited[w]) {
                visited[w] = true; queue.add(w)
            }
        }
        return order
    }
}
        """.trimIndent())
    }
}

// ================= TRIE =================

class TrieNode(val ch: Char = '\u0000') {
    val children = mutableMapOf<Char, TrieNode>()
    var isWord = false
}

fun insertTrie(root: TrieNode, word: String) {
    var cur = root
    for (c in word.lowercase()) cur = cur.children.getOrPut(c) { TrieNode(c) }
    cur.isWord = true
}

@Composable
fun TrieContent(nav: NavController) {
    val root = remember { TrieNode() }
    val words = remember { mutableStateListOf("cat", "car", "cart", "dog") }
    var text by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("Trie (Prefix Tree)") }

    LaunchedEffect(Unit) { words.forEach { insertTrie(root, it) } }

    AppScaffold("Trie", nav) {
        InfoCard(
            "Trie (Prefix Tree)",
            "Insert: O(L), Search: O(L), Prefix search: O(P)",
            "A tree for storing strings where each edge represents one character. Words sharing a prefix share the same path, making prefix lookups very fast. L = word length."
        )

        OutlinedTextField(
            value = text, onValueChange = { text = it },
            label = { Text("Word (letters only)") }, modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val w = text.trim().lowercase()
                if (w.isNotEmpty()) {
                    insertTrie(root, w)
                    words.add(w)
                    message = "Inserted \"$w\""
                    text = ""
                }
            }) { Text("Insert") }
            Button(onClick = {
                root.children.clear()
                words.clear()
                message = "Trie cleared"
            }) { Text("Clear") }
        }
        Text(message, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        if (words.isNotEmpty()) {
            Text("Words: ${words.joinToString(", ")}", fontSize = 13.sp, color = Color.Gray)
        }

        Box(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(40.dp).background(Color(0xFFE8EAF6), CircleShape)
                        .border(2.dp, Color(0xFF3F51B5), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("\u2022", fontWeight = FontWeight.Bold) }
                if (root.children.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                        root.children.values.forEach { child ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("\u2198", color = Color.Gray, fontSize = 14.sp)
                                TrieView(child)
                            }
                        }
                    }
                }
            }
        }

        CodeCard("""
class TrieNode {
    val children = HashMap<Char, TrieNode>()
    var isWord = false
}

class Trie {
    val root = TrieNode()

    fun insert(word: String) {
        var cur = root
        for (c in word)
            cur = cur.children.getOrPut(c) { TrieNode() }
        cur.isWord = true
    }

    fun contains(word: String): Boolean {
        var cur = root
        for (c in word) cur = cur.children[c] ?: return false
        return cur.isWord
    }
}
        """.trimIndent())
    }
}

@Composable
fun TrieView(node: TrieNode) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(36.dp).background(Color(0xFFE8EAF6), CircleShape)
                .border(2.dp, if (node.isWord) Color(0xFF43A047) else Color(0xFF3F51B5), CircleShape),
            contentAlignment = Alignment.Center
        ) { Text("${node.ch}", fontSize = 14.sp, fontWeight = FontWeight.Bold) }
        if (node.children.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                node.children.values.forEach { child ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("\u2193", color = Color.Gray, fontSize = 12.sp)
                        TrieView(child)
                    }
                }
            }
        }
    }
}
