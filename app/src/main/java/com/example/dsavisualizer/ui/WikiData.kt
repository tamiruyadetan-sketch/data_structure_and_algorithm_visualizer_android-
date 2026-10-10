package com.example.dsavisualizer.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

/** A wiki-style entry: definition, steps, complexity, and code for one topic. */
data class WikiEntry(
    val id: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: androidx.compose.ui.graphics.Color,
    val definition: String,
    val types: List<String> = emptyList(),
    val steps: List<String> = emptyList(),
    val timeComplexity: String,
    val spaceComplexity: String,
    val best: String = "",
    val worst: String = "",
    val code: String,
    val vizId: String? = null
)

object WikiData {

    // ================= DATA STRUCTURES =================

    val array = WikiEntry(
        "array", "Array", Icons.Default.GridOn, DsaTheme.accentBlue,
        "An array is a linear data structure that stores elements in contiguous memory locations. Each element can be accessed directly by its index, starting from 0.",
        types = listOf("One-dimensional array", "Multi-dimensional array", "Dynamic array (ArrayList)"),
        steps = listOf(
            "Access by index — O(1)",
            "Search linearly — O(n)",
            "Insert/delete requires shifting — O(n)"
        ),
        timeComplexity = "Access: O(1) · Search: O(n) · Insert: O(n)",
        spaceComplexity = "O(n)",
        code = """val arr = intArrayOf(10, 20, 30, 40)
val x = arr[2]        // O(1) access
arr[0] = 99           // O(1) update
// insert at index 1:
for (i in arr.size - 1 downTo 1) arr[i] = arr[i - 1]
arr[1] = 15           // O(n) shift""",
        vizId = "struct/array"
    )

    val linkedList = WikiEntry(
        "linkedlist", "Linked List", Icons.Default.Link, DsaTheme.accentTeal,
        "A linked list is a linear data structure where elements (nodes) are stored at non-contiguous memory locations and connected using pointers. Each node contains data and a reference to the next node.",
        types = listOf("Singly linked list", "Doubly linked list", "Circular linked list"),
        steps = listOf(
            "Insertion — O(1) at head/tail",
            "Deletion — O(1) with node reference",
            "Search / access — O(n)"
        ),
        timeComplexity = "Insert: O(1) · Search: O(n)",
        spaceComplexity = "O(n)",
        code = """class Node(val data: Int, var next: Node? = null)

fun addAtHead(value: Int) {
    val node = Node(value)
    node.next = head
    head = node
}""",
        vizId = "struct/linkedlist"
    )

    val stack = WikiEntry(
        "stack", "Stack", Icons.Default.Layers, DsaTheme.accentPurple,
        "A stack is a linear data structure that follows the LIFO principle — Last In, First Out. The last element pushed is the first one popped.",
        types = listOf("Array-based stack", "Linked-list-based stack"),
        steps = listOf(
            "Push — add on top · O(1)",
            "Pop — remove from top · O(1)",
            "Peek — read the top · O(1)"
        ),
        timeComplexity = "Push/Pop/Peak: O(1)",
        spaceComplexity = "O(n)",
        code = """class Stack<T> {
    private val items = mutableListOf<T>()
    fun push(v: T) = items.add(v)
    fun pop(): T? = items.removeLastOrNull()
    fun peek(): T? = items.lastOrNull()
}""",
        vizId = "struct/stack"
    )

    val queue = WikiEntry(
        "queue", "Queue", Icons.Default.Queue, DsaTheme.accentOrange,
        "A queue is a linear data structure that follows the FIFO principle — First In, First Out. Elements are added at the rear and removed from the front.",
        types = listOf("Simple queue", "Circular queue", "Priority queue", "Deque"),
        steps = listOf(
            "Enqueue — add at rear · O(1)",
            "Dequeue — remove from front · O(1)",
            "Peek — read the front · O(1)"
        ),
        timeComplexity = "Enqueue/Dequeue: O(1)",
        spaceComplexity = "O(n)",
        code = """class Queue<T> {
    private val items = ArrayDeque<T>()
    fun enqueue(v: T) = items.addLast(v)
    fun dequeue(): T? = items.removeFirstOrNull()
}""",
        vizId = "struct/queue"
    )

    val tree = WikiEntry(
        "tree", "Tree", Icons.Default.Park, DsaTheme.accentGreen,
        "A tree is a hierarchical, non-linear data structure made of nodes connected by edges, with one root node and no cycles. A Binary Search Tree keeps left < parent < right ordering for fast search.",
        types = listOf("Binary tree", "Binary Search Tree (BST)", "AVL tree", "Heap"),
        steps = listOf(
            "Search / insert / delete — O(log n) average",
            "Degenerates to O(n) if unbalanced",
            "Traversals: Inorder, Preorder, Postorder, Level-order"
        ),
        timeComplexity = "Search/Insert: O(log n) avg",
        spaceComplexity = "O(n)",
        code = """class Node(val value: Int) {
    var left: Node? = null
    var right: Node? = null
}

fun insert(root: Node?, v: Int): Node {
    if (root == null) return Node(v)
    if (v < root.value) root.left = insert(root.left, v)
    else root.right = insert(root.right, v)
    return root
}""",
        vizId = "struct/tree"
    )

    val graph = WikiEntry(
        "graph", "Graph", Icons.Default.Hub, DsaTheme.accentPink,
        "A graph is a non-linear data structure consisting of vertices (nodes) connected by edges. It can model networks, maps, and relationships. Traversal uses BFS (queue) or DFS (stack).",
        types = listOf("Directed graph", "Undirected graph", "Weighted graph"),
        steps = listOf(
            "BFS — level by level · O(V + E)",
            "DFS — as deep as possible · O(V + E)",
            "Stored as adjacency list or matrix"
        ),
        timeComplexity = "BFS/DFS: O(V + E)",
        spaceComplexity = "O(V + E)",
        code = """class Graph(val v: Int) {
    val adj = Array(v) { mutableListOf<Int>() }
    fun addEdge(u: Int, w: Int) {
        adj[u].add(w); adj[w].add(u)
    }
}""",
        vizId = "struct/graph"
    )

    val hashTable = WikiEntry(
        "hashtable", "Hash Table", Icons.Default.TableChart, DsaTheme.accentYellow,
        "A hash table (hash map) stores key-value pairs and uses a hash function to compute a bucket index for each key, giving average O(1) lookups. Collisions are resolved by chaining or open addressing.",
        types = listOf("HashMap", "HashSet", "Ordered map"),
        steps = listOf(
            "Hash the key → bucket index",
            "Store (key, value) in that bucket",
            "Collisions: chain in a list per bucket"
        ),
        timeComplexity = "Get/Put: O(1) average",
        spaceComplexity = "O(n)",
        code = """class HashTable {
    private val buckets = Array(5) { mutableListOf<Pair<Int, String>>() }
    private fun hash(k: Int) = k % 5
    fun put(k: Int, v: String) { buckets[hash(k)].add(k to v) }
    fun get(k: Int) = buckets[hash(k)].find { it.first == k }?.second
}""",
        vizId = "struct/hashtable"
    )

    val heap = WikiEntry(
        "heap", "Heap", Icons.Default.Landscape, DsaTheme.accentPurple,
        "A heap is a complete binary tree stored in an array. A min-heap keeps every parent smaller than its children, so the minimum is always at the root. Used in priority queues and Heap Sort.",
        types = listOf("Min-heap", "Max-heap", "Priority queue"),
        steps = listOf(
            "Insert at the end, sift up — O(log n)",
            "Extract root, move last to root, sift down — O(log n)",
            "Children of i live at 2i+1 and 2i+2"
        ),
        timeComplexity = "Insert/Extract: O(log n)",
        spaceComplexity = "O(n)",
        code = """fun siftUp(a: MutableList<Int>, i: Int) {
    var c = i
    while (c > 0) {
        val p = (c - 1) / 2
        if (a[c] < a[p]) { swap(a, c, p); c = p } else break
    }
}""",
        vizId = "struct/heap"
    )

    val trie = WikiEntry(
        "trie", "Trie", Icons.Default.AccountTree, DsaTheme.accentTeal,
        "A trie (prefix tree) is a tree for storing strings. Each edge represents one character, so words that share a prefix share the same path — perfect for autocomplete and dictionaries.",
        types = listOf("Standard trie", "Compressed trie (radix tree)", "Suffix tree"),
        steps = listOf(
            "Insert — walk/create one node per character · O(L)",
            "Search — follow characters from the root · O(L)",
            "Prefix search — collect the subtree · O(P)"
        ),
        timeComplexity = "Insert/Search: O(L)",
        spaceComplexity = "O(ALPHABET × N × L)",
        code = """class TrieNode {
    val children = HashMap<Char, TrieNode>()
    var isWord = false
}

fun insert(root: TrieNode, word: String) {
    var cur = root
    for (c in word) cur = cur.children.getOrPut(c) { TrieNode() }
    cur.isWord = true
}""",
        vizId = "struct/trie"
    )

    // ================= SEARCHING =================

    val linearSearch = WikiEntry(
        "linearSearch", "Linear Search", Icons.Default.Search, DsaTheme.accentBlue,
        "Linear search checks every element one by one from the start of the list until it finds the target or reaches the end. It works on both sorted and unsorted data.",
        steps = listOf(
            "Start at index 0",
            "Compare each element with the target",
            "If equal → return the index",
            "If the end is reached → not found"
        ),
        timeComplexity = "Best O(1) · Avg/Worst O(n)",
        spaceComplexity = "O(1)",
        best = "O(1)",
        worst = "O(n)",
        code = """fun linearSearch(a: IntArray, target: Int): Int {
    for (i in a.indices) {
        if (a[i] == target) return i
    }
    return -1
}""",
        vizId = "linear"
    )

    val binarySearch = WikiEntry(
        "binarySearch", "Binary Search", Icons.Default.VerticalAlignCenter, DsaTheme.accentPurple,
        "Binary search works on a sorted list. It compares the target with the middle element and discards half of the range each step, giving logarithmic search time.",
        steps = listOf(
            "Find the middle element",
            "If target == middle → found",
            "If target is smaller → search the left half",
            "If target is larger → search the right half",
            "Repeat until the range is empty"
        ),
        timeComplexity = "Best O(1) · Avg/Worst O(log n)",
        spaceComplexity = "O(1)",
        best = "O(1)",
        worst = "O(log n)",
        code = """fun binarySearch(a: IntArray, target: Int): Int {
    var lo = 0; var hi = a.size - 1
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        when {
            a[mid] == target -> return mid
            a[mid] < target -> lo = mid + 1
            else -> hi = mid - 1
        }
    }
    return -1
}""",
        vizId = "binary"
    )

    val jumpSearch = WikiEntry(
        "jumpSearch", "Jump Search", Icons.Default.ShoppingCartCheckout, DsaTheme.accentOrange,
        "Jump search jumps ahead in fixed blocks of size √n on a sorted list until it passes the target, then linearly scans inside that single block.",
        steps = listOf(
            "Jump ahead √n at a time",
            "Stop when the block may contain the target",
            "Linear scan inside that block"
        ),
        timeComplexity = "Avg/Worst O(√n)",
        spaceComplexity = "O(1)",
        worst = "O(√n)",
        code = """fun jumpSearch(a: IntArray, target: Int): Int {
    val block = sqrt(a.size.toDouble()).toInt()
    var prev = 0; var curr = block
    while (curr < a.size && a[curr - 1] < target) {
        prev = curr; curr += block
    }
    for (i in prev until minOf(curr, a.size))
        if (a[i] == target) return i
    return -1
}""",
        vizId = "jump"
    )

    val exponentialSearch = WikiEntry(
        "exponentialSearch", "Exponential Search", Icons.Default.Launch, DsaTheme.accentTeal,
        "Exponential search doubles a range (1, 2, 4, 8, …) until it brackets the target on a sorted list, then binary-searches only that range. Great when the target is near the start.",
        steps = listOf(
            "Check element 0",
            "Double the bound: 1, 2, 4, 8 …",
            "Stop when a[bound] ≥ target",
            "Binary search the last range"
        ),
        timeComplexity = "Avg/Worst O(log n)",
        spaceComplexity = "O(1)",
        worst = "O(log n)",
        code = """fun exponentialSearch(a: IntArray, t: Int): Int {
    var bound = 1
    while (bound < a.size && a[bound] < t) bound *= 2
    var lo = bound / 2; var hi = minOf(bound, a.size - 1)
    // binary search [lo..hi]
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        if (a[mid] == t) return mid
        if (a[mid] < t) lo = mid + 1 else hi = mid - 1
    }
    return -1
}""",
        vizId = "exponential"
    )

    val ternarySearch = WikiEntry(
        "ternarySearch", "Ternary Search", Icons.Default.CallSplit, DsaTheme.accentPink,
        "Ternary search splits a sorted range into three parts with two midpoints and keeps only the third that can contain the target. Like binary search, but two comparisons per step.",
        steps = listOf(
            "Split the range with mid1 and mid2",
            "If target == mid1/mid2 → found",
            "Else keep only the matching third",
            "Repeat until found or empty"
        ),
        timeComplexity = "Avg/Worst O(log₃ n)",
        spaceComplexity = "O(1)",
        worst = "O(log₃ n)",
        code = """fun ternarySearch(a: IntArray, t: Int): Int {
    var lo = 0; var hi = a.size - 1
    while (lo <= hi) {
        val m1 = lo + (hi - lo) / 3
        val m2 = hi - (hi - lo) / 3
        if (a[m1] == t) return m1
        if (a[m2] == t) return m2
        when {
            t < a[m1] -> hi = m1 - 1
            t > a[m2] -> lo = m2 + 1
            else -> { lo = m1 + 1; hi = m2 - 1 }
        }
    }
    return -1
}""",
        vizId = "ternary"
    )

    // ================= SORTING =================

    val bubbleSort = WikiEntry(
        "bubbleSort", "Bubble Sort", Icons.Default.Calculate, DsaTheme.accentBlue,
        "Bubble sort repeatedly compares adjacent elements and swaps them if they are in the wrong order. After each pass the largest unsorted value bubbles to its final place.",
        steps = listOf(
            "Compare adjacent pairs from the left",
            "Swap if the left one is bigger",
            "Largest value bubbles to the end",
            "Repeat for the unsorted part",
            "Stop early if no swaps happened"
        ),
        timeComplexity = "Best O(n) · Avg/Worst O(n²)",
        spaceComplexity = "O(1)",
        best = "O(n)",
        worst = "O(n²)",
        code = """fun bubbleSort(a: IntArray) {
    for (i in 0 until a.size - 1) {
        var swapped = false
        for (j in 0 until a.size - 1 - i) {
            if (a[j] > a[j + 1]) {
                val t = a[j]; a[j] = a[j + 1]; a[j + 1] = t
                swapped = true
            }
        }
        if (!swapped) break
    }
}""",
        vizId = "bubble"
    )

    val selectionSort = WikiEntry(
        "selectionSort", "Selection Sort", Icons.Default.Rule, DsaTheme.accentTeal,
        "Selection sort finds the smallest element in the unsorted part and swaps it into the next position. It always makes exactly n−1 swaps.",
        steps = listOf(
            "Find the minimum of the unsorted part",
            "Swap it with the first unsorted slot",
            "Grow the sorted part by one",
            "Repeat until the end"
        ),
        timeComplexity = "O(n²) all cases",
        spaceComplexity = "O(1)",
        worst = "O(n²)",
        code = """fun selectionSort(a: IntArray) {
    for (i in 0 until a.size - 1) {
        var min = i
        for (j in i + 1 until a.size)
            if (a[j] < a[min]) min = j
        val t = a[i]; a[i] = a[min]; a[min] = t
    }
}""",
        vizId = "selection"
    )

    val insertionSort = WikiEntry(
        "insertionSort", "Insertion Sort", Icons.Default.EditNote, DsaTheme.accentPurple,
        "Insertion sort builds the sorted array one element at a time, inserting each new element into its correct position among the already-sorted elements — like sorting playing cards.",
        steps = listOf(
            "Take the next element (the key)",
            "Compare it backwards through the sorted part",
            "Shift bigger elements right",
            "Drop the key into its slot"
        ),
        timeComplexity = "Best O(n) · Avg/Worst O(n²)",
        spaceComplexity = "O(1)",
        best = "O(n)",
        worst = "O(n²)",
        code = """fun insertionSort(a: IntArray) {
    for (i in 1 until a.size) {
        var j = i
        while (j > 0 && a[j - 1] > a[j]) {
            val t = a[j]; a[j] = a[j - 1]; a[j - 1] = t
            j--
        }
    }
}""",
        vizId = "insertion"
    )

    val mergeSort = WikiEntry(
        "mergeSort", "Merge Sort", Icons.Default.MergeType, DsaTheme.accentGreen,
        "Merge sort is a divide-and-conquer algorithm: split the array in half, recursively sort each half, then merge the two sorted halves. It is stable and always O(n log n).",
        steps = listOf(
            "Divide the array into two halves",
            "Recursively sort each half",
            "Merge the two sorted halves",
            "Repeat until one sorted array remains"
        ),
        timeComplexity = "O(n log n) all cases",
        spaceComplexity = "O(n)",
        worst = "O(n log n)",
        code = """fun mergeSort(a: IntArray, lo: Int, hi: Int) {
    if (lo >= hi) return
    val mid = (lo + hi) / 2
    mergeSort(a, lo, mid)
    mergeSort(a, mid + 1, hi)
    merge(a, lo, mid, hi)
}""",
        vizId = "merge"
    )

    val quickSort = WikiEntry(
        "quickSort", "Quick Sort", Icons.Default.Bolt, DsaTheme.accentOrange,
        "Quick sort picks a pivot and partitions the array so smaller values go to its left and larger to its right, then recursively sorts both sides. It is the fastest general-purpose sort in practice.",
        steps = listOf(
            "Choose a pivot (here: last element)",
            "Partition: smaller left, larger right",
            "Place the pivot at its final index",
            "Recursively sort both sides"
        ),
        timeComplexity = "Best/Avg O(n log n) · Worst O(n²)",
        spaceComplexity = "O(log n)",
        best = "O(n log n)",
        worst = "O(n²)",
        code = """fun quickSort(a: IntArray, lo: Int, hi: Int) {
    if (lo >= hi) return
    val p = partition(a, lo, hi)
    quickSort(a, lo, p - 1)
    quickSort(a, p + 1, hi)
}""",
        vizId = "quick"
    )

    val heapSort = WikiEntry(
        "heapSort", "Heap Sort", Icons.Default.Landscape, DsaTheme.accentRed,
        "Heap sort builds a max-heap from the array, then repeatedly swaps the root (largest) with the last unsorted element and restores the heap. Always O(n log n) with O(1) extra space.",
        steps = listOf(
            "Build a max-heap from the array",
            "Swap the root with the last unsorted element",
            "Sift down to restore the heap",
            "Repeat until everything is sorted"
        ),
        timeComplexity = "O(n log n) all cases",
        spaceComplexity = "O(1)",
        worst = "O(n log n)",
        code = """fun heapSort(a: IntArray) {
    for (i in a.size / 2 - 1 downTo 0) siftDown(a, i, a.size - 1)
    for (end in a.size - 1 downTo 1) {
        val t = a[0]; a[0] = a[end]; a[end] = t
        siftDown(a, 0, end - 1)
    }
}""",
        vizId = "viz/heap"
    )

    val countingSort = WikiEntry(
        "countingSort", "Counting Sort", Icons.Default.Pin, DsaTheme.accentYellow,
        "Counting sort counts how many times each value appears, then rebuilds the array in order. It never compares elements — it needs a small range of integer keys.",
        steps = listOf(
            "Count occurrences of every value",
            "Rebuild the array from the counts",
            "No comparisons are made at all"
        ),
        timeComplexity = "O(n + k) all cases",
        spaceComplexity = "O(k)",
        worst = "O(n + k)",
        code = """fun countingSort(a: IntArray) {
    val counts = IntArray(a.max() + 1)
    for (v in a) counts[v]++
    var idx = 0
    for (v in counts.indices)
        repeat(counts[v]) { a[idx++] = v }
}""",
        vizId = "counting"
    )

    val radixSort = WikiEntry(
        "radixSort", "Radix Sort", Icons.Default.Tag, DsaTheme.accentPink,
        "Radix sort sorts digit by digit (1s, 10s, 100s, …) using a stable sort for each digit position. It never compares two elements directly.",
        steps = listOf(
            "Sort by the 1s digit (stable)",
            "Sort by the 10s digit",
            "… continue to the highest digit",
            "After the last pass the array is sorted"
        ),
        timeComplexity = "O(d·(n + k))",
        spaceComplexity = "O(n + k)",
        worst = "O(d·(n + k))",
        code = """fun radixSort(a: IntArray) {
    var exp = 1
    val max = a.max()
    while (max / exp > 0) {
        countingSortByDigit(a, exp)
        exp *= 10
    }
}""",
        vizId = "radix"
    )

    val bucketSort = WikiEntry(
        "bucketSort", "Bucket Sort", Icons.Default.DataArray, DsaTheme.accentBlue,
        "Bucket sort scatters elements into buckets by their value range, sorts each bucket individually, then concatenates them. Excellent for uniformly distributed data.",
        steps = listOf(
            "Create k empty buckets",
            "Scatter values into buckets by range",
            "Sort each bucket",
            "Concatenate buckets in order"
        ),
        timeComplexity = "Avg O(n + k) · Worst O(n²)",
        spaceComplexity = "O(n)",
        worst = "O(n²)",
        code = """fun bucketSort(a: IntArray) {
    val buckets = Array(a.size) { mutableListOf<Int>() }
    for (v in a) buckets[v / a.size].add(v)
    var idx = 0
    for (b in buckets) {
        b.sort()
        for (v in b) a[idx++] = v
    }
}""",
        vizId = "bucket"
    )

    val shellSort = WikiEntry(
        "shellSort", "Shell Sort", Icons.Default.ViewWeek, DsaTheme.accentTeal,
        "Shell sort is a generalized insertion sort: it compares elements far apart (large gap) first and shrinks the gap to 1, letting early moves fix big disorders cheaply.",
        steps = listOf(
            "Start with gap = n/2",
            "Gapped insertion sort per gap",
            "Halve the gap each round",
            "Final pass with gap 1 = insertion sort"
        ),
        timeComplexity = "Best O(n log n) · Worst O(n²)",
        spaceComplexity = "O(1)",
        best = "O(n log n)",
        worst = "O(n²)",
        code = """fun shellSort(a: IntArray) {
    var gap = a.size / 2
    while (gap > 0) {
        for (i in gap until a.size) {
            var j = i
            while (j >= gap && a[j - gap] > a[j]) {
                val t = a[j]; a[j] = a[j - gap]; a[j - gap] = t
                j -= gap
            }
        }
        gap /= 2
    }
}""",
        vizId = "shell"
    )

    // ================= GRAPH ALGORITHMS =================

    val bfs = WikiEntry(
        "bfs", "Breadth-First Search (BFS)", Icons.Default.DeviceHub, DsaTheme.accentBlue,
        "BFS explores a graph level by level starting from a source vertex, using a queue. It finds the shortest path (fewest edges) in unweighted graphs.",
        steps = listOf(
            "Put the start vertex in the queue",
            "Dequeue a vertex, visit it",
            "Enqueue all unvisited neighbors",
            "Repeat until the queue is empty"
        ),
        timeComplexity = "O(V + E)",
        spaceComplexity = "O(V)",
        code = """fun bfs(start: Int): List<Int> {
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
}""",
        vizId = "struct/graph"
    )

    val dfs = WikiEntry(
        "dfs", "Depth-First Search (DFS)", Icons.Default.AccountTree, DsaTheme.accentGreen,
        "DFS explores as deep as possible along each branch before backtracking, using a stack (or recursion). It is the basis for cycle detection, topological sort, and maze solving.",
        steps = listOf(
            "Push the start vertex on a stack",
            "Pop a vertex; if unvisited, visit it",
            "Push all its neighbors",
            "Repeat until the stack is empty"
        ),
        timeComplexity = "O(V + E)",
        spaceComplexity = "O(V)",
        code = """fun dfs(start: Int): List<Int> {
    val visited = mutableSetOf<Int>()
    val order = mutableListOf<Int>()
    val stack = ArrayDeque<Int>()
    stack.addLast(start)
    while (stack.isNotEmpty()) {
        val u = stack.removeLast()
        if (u in visited) continue
        visited.add(u); order.add(u)
        for (w in adj[u].reversed()) stack.addLast(w)
    }
    return order
}""",
        vizId = "struct/graph"
    )

    val dijkstra = WikiEntry(
        "dijkstra", "Dijkstra's Algorithm", Icons.Default.Route, DsaTheme.accentPurple,
        "Dijkstra's algorithm finds the shortest path from a source to all vertices in a weighted graph with non-negative edges, greedily expanding the closest unvisited vertex.",
        steps = listOf(
            "Set all distances ∞ except source = 0",
            "Pick the closest unvisited vertex",
            "Relax all its outgoing edges",
            "Mark it visited; repeat for all"
        ),
        timeComplexity = "O((V + E) log V) with a heap",
        spaceComplexity = "O(V)",
        code = """// distances via a priority queue
fun dijkstra(src: Int): IntArray {
    val dist = IntArray(v) { Int.MAX_VALUE }
    dist[src] = 0
    val pq = java.util.PriorityQueue(intArrayOf(0, src).let { listOf(it) }.size) { a: IntArray, b: IntArray -> a[0] - b[0] }
    // add (dist, vertex) pairs, pop the closest, relax edges
    return dist
}""",
        vizId = null
    )

    // ================= ALL ENTRIES =================

    val structures = listOf(array, linkedList, stack, queue, tree, heap, trie, graph, hashTable)
    val searching = listOf(linearSearch, binarySearch, jumpSearch, exponentialSearch, ternarySearch)
    val sorting = listOf(bubbleSort, selectionSort, insertionSort, mergeSort, quickSort, heapSort, countingSort, radixSort, bucketSort, shellSort)
    val graphAlgos = listOf(bfs, dfs, dijkstra)

    val all: List<WikiEntry> = structures + searching + sorting + graphAlgos

    fun byId(id: String): WikiEntry? = all.find { it.id == id }
}
