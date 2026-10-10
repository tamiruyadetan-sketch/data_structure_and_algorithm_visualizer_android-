package com.example.dsavisualizer.engine

import com.example.dsavisualizer.model.AlgoInfo
import com.example.dsavisualizer.model.Mark
import com.example.dsavisualizer.model.Step

object Algorithms {

    private fun marks(base: Set<Int>, vararg extra: Pair<Int, Mark>): Map<Int, Mark> =
        base.associateWith { Mark.SORTED } + extra

    // ---------------- SORTING ----------------

    fun bubbleSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val sorted = mutableSetOf<Int>()
        for (i in 0 until a.size - 1) {
            var swapped = false
            for (j in 0 until a.size - 1 - i) {
                steps += Step(a.toList(), marks(sorted, j to Mark.COMPARE, j + 1 to Mark.COMPARE),
                    "Compare ${a[j]} and ${a[j + 1]}")
                if (a[j] > a[j + 1]) {
                    val tmp = a[j]; a[j] = a[j + 1]; a[j + 1] = tmp
                    swapped = true
                    steps += Step(a.toList(), marks(sorted, j to Mark.SWAP, j + 1 to Mark.SWAP),
                        "${a[j + 1]} > ${a[j]}, so swap them")
                }
            }
            sorted += a.size - 1 - i
            if (!swapped) break
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun selectionSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val sorted = mutableSetOf<Int>()
        for (i in 0 until a.size - 1) {
            var min = i
            for (j in i + 1 until a.size) {
                steps += Step(a.toList(), marks(sorted, min to Mark.PIVOT, j to Mark.COMPARE),
                    "Compare ${a[j]} with current minimum ${a[min]}")
                if (a[j] < a[min]) {
                    min = j
                    steps += Step(a.toList(), marks(sorted, min to Mark.PIVOT),
                        "New minimum: ${a[min]}")
                }
            }
            if (min != i) {
                val tmp = a[i]; a[i] = a[min]; a[min] = tmp
                steps += Step(a.toList(), marks(sorted, i to Mark.SWAP, min to Mark.SWAP),
                    "Swap minimum into position $i")
            }
            sorted += i
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun insertionSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        for (i in 1 until a.size) {
            var j = i
            while (j > 0) {
                steps += Step(a.toList(), mapOf(j - 1 to Mark.COMPARE, j to Mark.COMPARE),
                    "Compare ${a[j - 1]} and ${a[j]}")
                if (a[j - 1] > a[j]) {
                    val tmp = a[j]; a[j] = a[j - 1]; a[j - 1] = tmp
                    steps += Step(a.toList(), mapOf(j - 1 to Mark.SWAP, j to Mark.SWAP),
                        "Shift ${a[j]} to the right")
                    j--
                } else break
            }
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun mergeSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val aux = a.toTypedArray()

        fun merge(lo: Int, mid: Int, hi: Int) {
            for (i in lo..hi) aux[i] = a[i]
            var i = lo
            var j = mid + 1
            for (k in lo..hi) {
                a[k] = when {
                    i > mid -> aux[j++]
                    j > hi -> aux[i++]
                    aux[i] <= aux[j] -> aux[i++]
                    else -> aux[j++]
                }
                steps += Step(
                    a.toList(),
                    (lo..hi).associateWith { Mark.RANGE } + (k to Mark.SWAP),
                    "Merging [${lo}..${mid}] and [${mid + 1}..${hi}]: place ${a[k]} at index $k"
                )
            }
        }

        fun sort(lo: Int, hi: Int) {
            if (lo >= hi) return
            val mid = (lo + hi) / 2
            sort(lo, mid)
            sort(mid + 1, hi)
            merge(lo, mid, hi)
        }

        sort(0, a.size - 1)
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun quickSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val sorted = mutableSetOf<Int>()

        fun swap(i: Int, j: Int) {
            val tmp = a[i]; a[i] = a[j]; a[j] = tmp
        }

        fun partition(lo: Int, hi: Int): Int {
            val pivot = a[hi]
            steps += Step(a.toList(), marks(sorted, hi to Mark.PIVOT),
                "Pivot = $pivot (last element of [${lo}..${hi}])")
            var i = lo
            for (j in lo until hi) {
                steps += Step(a.toList(), marks(sorted, hi to Mark.PIVOT, j to Mark.COMPARE),
                    "Compare ${a[j]} with pivot $pivot")
                if (a[j] < pivot) {
                    if (i != j) {
                        swap(i, j)
                        steps += Step(a.toList(), marks(sorted, hi to Mark.PIVOT, i to Mark.SWAP, j to Mark.SWAP),
                            "${a[i]} < $pivot, swap into left part")
                    }
                    i++
                }
            }
            if (i != hi) {
                swap(i, hi)
                steps += Step(a.toList(), marks(sorted, i to Mark.SWAP, hi to Mark.SWAP),
                    "Place pivot $pivot at its final position $i")
            }
            return i
        }

        fun sort(lo: Int, hi: Int) {
            if (lo >= hi) return
            val p = partition(lo, hi)
            sorted += p
            sort(lo, p - 1)
            sort(p + 1, hi)
        }

        sort(0, a.size - 1)
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun heapSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))

        fun swap(i: Int, j: Int) {
            val tmp = a[i]; a[i] = a[j]; a[j] = tmp
        }

        fun siftDown(start: Int, end: Int) {
            var root = start
            while (root * 2 + 1 <= end) {
                val child = root * 2 + 1
                var target = root
                if (a[target] < a[child]) target = child
                if (child + 1 <= end && a[target] < a[child + 1]) target = child + 1
                if (target == root) return
                swap(root, target)
                steps += Step(a.toList(), mapOf(root to Mark.SWAP, target to Mark.SWAP),
                    "Sift down: swap ${a[root]} and ${a[target]} to restore max-heap")
                root = target
            }
        }

        // Build max heap
        for (start in (a.size - 2) / 2 downTo 0) siftDown(start, a.size - 1)
        steps += Step(a.toList(), a.indices.associateWith { Mark.RANGE }, "Max-heap built (largest value at root)")

        // Repeatedly extract max
        for (end in a.size - 1 downTo 1) {
            swap(0, end)
            steps += Step(a.toList(), (end until a.size).associateWith { Mark.SORTED },
                "Move max ${a[end]} to position $end")
            siftDown(0, end - 1)
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun shellSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        var gap = a.size / 2
        while (gap > 0) {
            for (i in gap until a.size) {
                var j = i
                while (j >= gap) {
                    steps += Step(a.toList(), mapOf(j - gap to Mark.COMPARE, j to Mark.COMPARE),
                        "Gap $gap: compare ${a[j - gap]} and ${a[j]}")
                    if (a[j - gap] > a[j]) {
                        val tmp = a[j]; a[j] = a[j - gap]; a[j - gap] = tmp
                        steps += Step(a.toList(), mapOf(j - gap to Mark.SWAP, j to Mark.SWAP),
                            "Gap $gap: swap them")
                        j -= gap
                    } else break
                }
            }
            gap /= 2
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun countingSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val min = a.min()
        val max = a.max()
        val counts = IntArray(max - min + 1)
        a.forEachIndexed { idx, v ->
            counts[v - min]++
            steps += Step(a.toList(), mapOf(idx to Mark.COMPARE), "Count value $v")
        }
        steps += Step(a.toList(), message = "Counts built for range [$min..$max]")
        var idx = 0
        for (c in counts.indices) {
            repeat(counts[c]) {
                a[idx] = c + min
                steps += Step(a.toList(), mapOf(idx to Mark.SORTED),
                    "Place ${c + min} at index $idx")
                idx++
            }
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun radixSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val min = a.min()
        if (min < 0) { // shift so all values are non-negative
            for (i in a.indices) a[i] -= min
            steps += Step(a.toList(), message = "Shifted values by ${-min} to make them non-negative")
        }
        val max = a.max()
        var exp = 1
        var pass = 1
        while (max / exp > 0) {
            val output = IntArray(a.size)
            val counts = IntArray(10)
            for (v in a) counts[(v / exp) % 10]++
            for (d in 1 until 10) counts[d] += counts[d - 1]
            for (i in a.indices.reversed()) {
                val d = (a[i] / exp) % 10
                output[--counts[d]] = a[i]
            }
            for (i in a.indices) a[i] = output[i]
            val place = when (pass) { 1 -> "1s"; 2 -> "10s"; 3 -> "100s"; 4 -> "1000s"; else -> "${exp}th" }
            steps += Step(a.toList(), a.indices.associateWith { Mark.RANGE },
                "After pass $pass (sorting by $place digit): ${a.joinToString(", ")}")
            exp *= 10
            pass++
        }
        if (min < 0) for (i in a.indices) a[i] += min
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    fun bucketSort(input: List<Int>, @Suppress("UNUSED_PARAMETER") t: Int?): List<Step> {
        val a = input.toMutableList()
        val steps = mutableListOf(Step(a.toList(), message = "Initial array"))
        val min = a.min()
        val max = a.max()
        val range = max - min
        if (range == 0) {
            steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "All values equal — already sorted")
            return steps
        }
        val bucketCount = (a.size / 2).coerceAtLeast(1)
        val buckets = Array(bucketCount) { mutableListOf<Int>() }
        a.forEachIndexed { idx, v ->
            val bi = ((v - min) * (bucketCount - 1) / range)
            buckets[bi].add(v)
            steps += Step(a.toList(), mapOf(idx to Mark.RANGE), "$v goes into bucket $bi")
        }
        var idx = 0
        for ((bi, b) in buckets.withIndex()) {
            b.sort()
            for (v in b) {
                a[idx] = v
                steps += Step(a.toList(), mapOf(idx to Mark.SORTED),
                    "Empty sorted bucket $bi: place $v at index $idx")
                idx++
            }
        }
        steps += Step(a.toList(), a.indices.associateWith { Mark.SORTED }, "Array is sorted")
        return steps
    }

    // ---------------- SEARCHING ----------------

    fun linearSearch(input: List<Int>, target: Int?): List<Step> {
        val a = input
        val t = target ?: return listOf(Step(a, message = "Enter a target value"))
        val steps = mutableListOf(Step(a, message = "Searching for $t"))
        for (i in a.indices) {
            steps += Step(a, mapOf(i to Mark.COMPARE), "Is ${a[i]} == $t ?")
            if (a[i] == t) {
                steps += Step(a, mapOf(i to Mark.FOUND), "Found $t at index $i")
                return steps
            }
        }
        steps += Step(a, message = "$t is not in the array")
        return steps
    }

    fun binarySearch(input: List<Int>, target: Int?): List<Step> {
        val t = target ?: return listOf(Step(input, message = "Enter a target value"))
        val a = input.sorted()
        val steps = mutableListOf(Step(a, message = "Binary search needs a sorted array (sorted for you). Target = $t"))
        var lo = 0
        var hi = a.size - 1
        while (lo <= hi) {
            val mid = (lo + hi) / 2
            val range = (lo..hi).associateWith { Mark.RANGE }
            steps += Step(a, range + (mid to Mark.COMPARE),
                "Search range [$lo..$hi], middle index $mid = ${a[mid]}")
            when {
                a[mid] == t -> {
                    steps += Step(a, mapOf(mid to Mark.FOUND), "Found $t at index $mid")
                    return steps
                }
                a[mid] < t -> {
                    steps += Step(a, range + (mid to Mark.COMPARE), "${a[mid]} < $t, search the right half")
                    lo = mid + 1
                }
                else -> {
                    steps += Step(a, range + (mid to Mark.COMPARE), "${a[mid]} > $t, search the left half")
                    hi = mid - 1
                }
            }
        }
        steps += Step(a, message = "$t is not in the array")
        return steps
    }

    fun jumpSearch(input: List<Int>, target: Int?): List<Step> {
        val t = target ?: return listOf(Step(input, message = "Enter a target value"))
        val a = input.sorted()
        val steps = mutableListOf(Step(a, message = "Jump search needs a sorted array (sorted for you). Target = $t"))
        val n = a.size
        val block = maxOf(1, kotlin.math.sqrt(n.toDouble()).toInt())
        var prev = 0
        var curr = block
        while (prev < n && a[minOf(curr, n) - 1] < t) {
            steps += Step(a, (prev until minOf(curr, n)).associateWith { Mark.RANGE },
                "Jump ahead by $block: skip block [${prev}..${minOf(curr, n) - 1}]")
            prev = curr
            curr += block
        }
        if (prev < n) {
            steps += Step(a, (prev until minOf(curr, n)).associateWith { Mark.RANGE },
                "Block [${prev}..${minOf(curr, n) - 1}] may contain $t — do a linear scan")
            for (i in prev until minOf(curr, n)) {
                steps += Step(a, mapOf(i to Mark.COMPARE), "Is ${a[i]} == $t ?")
                if (a[i] == t) {
                    steps += Step(a, mapOf(i to Mark.FOUND), "Found $t at index $i")
                    return steps
                }
            }
        }
        steps += Step(a, message = "$t is not in the array")
        return steps
    }

    fun exponentialSearch(input: List<Int>, target: Int?): List<Step> {
        val t = target ?: return listOf(Step(input, message = "Enter a target value"))
        val a = input.sorted()
        val steps = mutableListOf(Step(a, message = "Exponential search needs a sorted array (sorted for you). Target = $t"))
        val n = a.size
        if (n == 0) { steps += Step(a, message = "$t is not in the array"); return steps }
        if (a[0] == t) {
            steps += Step(a, mapOf(0 to Mark.FOUND), "Found $t at index 0")
            return steps
        }
        var bound = 1
        while (bound < n && a[bound] < t) {
            steps += Step(a, (0..bound).associateWith { Mark.RANGE },
                "$t > ${a[bound]}, double the range to [0..$bound]")
            bound *= 2
        }
        var lo = bound / 2
        var hi = minOf(bound, n - 1)
        steps += Step(a, (lo..hi).associateWith { Mark.RANGE },
            "Binary search inside range [${lo}..${hi}]")
        while (lo <= hi) {
            val mid = (lo + hi) / 2
            steps += Step(a, (lo..hi).associateWith { Mark.RANGE } + (mid to Mark.COMPARE),
                "Middle index $mid = ${a[mid]}")
            when {
                a[mid] == t -> {
                    steps += Step(a, mapOf(mid to Mark.FOUND), "Found $t at index $mid")
                    return steps
                }
                a[mid] < t -> lo = mid + 1
                else -> hi = mid - 1
            }
        }
        steps += Step(a, message = "$t is not in the array")
        return steps
    }

    fun ternarySearch(input: List<Int>, target: Int?): List<Step> {
        val t = target ?: return listOf(Step(input, message = "Enter a target value"))
        val a = input.sorted()
        val steps = mutableListOf(Step(a, message = "Ternary search needs a sorted array (sorted for you). Target = $t"))
        var lo = 0
        var hi = a.size - 1
        while (lo <= hi) {
            val third = (hi - lo) / 3
            val m1 = lo + third
            val m2 = hi - third
            val range = (lo..hi).associateWith { Mark.RANGE }
            steps += Step(a, range + (m1 to Mark.COMPARE) + (m2 to Mark.COMPARE),
                "Split [${lo}..${hi}] at midpoints $m1 (${a[m1]}) and $m2 (${a[m2]})")
            when {
                a[m1] == t -> {
                    steps += Step(a, mapOf(m1 to Mark.FOUND), "Found $t at index $m1")
                    return steps
                }
                a[m2] == t -> {
                    steps += Step(a, mapOf(m2 to Mark.FOUND), "Found $t at index $m2")
                    return steps
                }
                t < a[m1] -> {
                    steps += Step(a, range + (m1 to Mark.COMPARE), "$t < ${a[m1]}, search the left third")
                    hi = m1 - 1
                }
                t > a[m2] -> {
                    steps += Step(a, range + (m2 to Mark.COMPARE), "$t > ${a[m2]}, search the right third")
                    lo = m2 + 1
                }
                else -> {
                    steps += Step(a, range, "$t is between the midpoints, search the middle third")
                    lo = m1 + 1
                    hi = m2 - 1
                }
            }
        }
        steps += Step(a, message = "$t is not in the array")
        return steps
    }

    // ---------------- CATALOG ----------------

    val all: List<AlgoInfo> = listOf(
        AlgoInfo(
            "linear", "Linear Search", "Searching",
            "Checks every element one by one from the start until the target is found or the array ends.",
            "O(1)", "O(n)", "O(n)", "O(1)",
            """fun linearSearch(a: IntArray, target: Int): Int {
    for (i in a.indices) {
        if (a[i] == target) return i
    }
    return -1
}""", true, ::linearSearch
        ),
        AlgoInfo(
            "binary", "Binary Search", "Searching",
            "Works on a sorted array. Compare the target with the middle element and discard half of the range each time.",
            "O(1)", "O(log n)", "O(log n)", "O(1)",
            """fun binarySearch(a: IntArray, target: Int): Int {
    var lo = 0
    var hi = a.size - 1
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        when {
            a[mid] == target -> return mid
            a[mid] < target -> lo = mid + 1
            else -> hi = mid - 1
        }
    }
    return -1
}""", true, ::binarySearch
        ),
        AlgoInfo(
            "bubble", "Bubble Sort", "Sorting",
            "Repeatedly compares adjacent elements and swaps them if they are in the wrong order. Large values bubble to the end.",
            "O(n)", "O(n²)", "O(n²)", "O(1)",
            """fun bubbleSort(a: IntArray) {
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
}""", false, ::bubbleSort
        ),
        AlgoInfo(
            "selection", "Selection Sort", "Sorting",
            "Finds the smallest element in the unsorted part and swaps it into the next position of the sorted part.",
            "O(n²)", "O(n²)", "O(n²)", "O(1)",
            """fun selectionSort(a: IntArray) {
    for (i in 0 until a.size - 1) {
        var min = i
        for (j in i + 1 until a.size) {
            if (a[j] < a[min]) min = j
        }
        val t = a[i]; a[i] = a[min]; a[min] = t
    }
}""", false, ::selectionSort
        ),
        AlgoInfo(
            "insertion", "Insertion Sort", "Sorting",
            "Builds the sorted array one element at a time by inserting each new element into its correct place on the left.",
            "O(n)", "O(n²)", "O(n²)", "O(1)",
            """fun insertionSort(a: IntArray) {
    for (i in 1 until a.size) {
        var j = i
        while (j > 0 && a[j - 1] > a[j]) {
            val t = a[j]; a[j] = a[j - 1]; a[j - 1] = t
            j--
        }
    }
}""", false, ::insertionSort
        ),
        AlgoInfo(
            "merge", "Merge Sort", "Sorting",
            "Divide and conquer: split the array in half, sort each half recursively, then merge the two sorted halves.",
            "O(n log n)", "O(n log n)", "O(n log n)", "O(n)",
            """fun mergeSort(a: IntArray, lo: Int, hi: Int) {
    if (lo >= hi) return
    val mid = (lo + hi) / 2
    mergeSort(a, lo, mid)
    mergeSort(a, mid + 1, hi)
    merge(a, lo, mid, hi) // merge two sorted halves
}""", false, ::mergeSort
        ),
        AlgoInfo(
            "quick", "Quick Sort", "Sorting",
            "Picks a pivot, partitions the array so smaller values go left and larger go right, then recursively sorts each part.",
            "O(n log n)", "O(n log n)", "O(n²)", "O(log n)",
            """fun quickSort(a: IntArray, lo: Int, hi: Int) {
    if (lo >= hi) return
    val p = partition(a, lo, hi) // Lomuto, pivot = a[hi]
    quickSort(a, lo, p - 1)
    quickSort(a, p + 1, hi)
}""", false, ::quickSort
        ),
        AlgoInfo(
            "heap", "Heap Sort", "Sorting",
            "Builds a max-heap from the array, then repeatedly swaps the root (largest) with the last unsorted element and sifts down.",
            "O(n log n)", "O(n log n)", "O(n log n)", "O(1)",
            """fun heapSort(a: IntArray) {
    for (i in a.size / 2 - 1 downTo 0) siftDown(a, i, a.size - 1)
    for (end in a.size - 1 downTo 1) {
        val t = a[0]; a[0] = a[end]; a[end] = t
        siftDown(a, 0, end - 1)
    }
}""", false, ::heapSort
        ),
        AlgoInfo(
            "shell", "Shell Sort", "Sorting",
            "Generalized insertion sort: compares elements far apart first (large gap), then progressively reduces the gap to 1.",
            "O(n log n)", "O(n^1.3)", "O(n²)", "O(1)",
            """fun shellSort(a: IntArray) {
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
}""", false, ::shellSort
        ),
        AlgoInfo(
            "counting", "Counting Sort", "Sorting",
            "Counts the occurrences of each value, then rebuilds the array in order. Works well when the value range is small.",
            "O(n + k)", "O(n + k)", "O(n + k)", "O(k)",
            """fun countingSort(a: IntArray) {
    val counts = IntArray(a.max() + 1)
    for (v in a) counts[v]++
    var idx = 0
    for (v in counts.indices)
        repeat(counts[v]) { a[idx++] = v }
}""", false, ::countingSort
        ),
        AlgoInfo(
            "radix", "Radix Sort", "Sorting",
            "Sorts digit by digit (1s, 10s, 100s, …) using a stable sort per digit. Never compares two elements directly.",
            "O(d·(n + k))", "O(d·(n + k))", "O(d·(n + k))", "O(n + k)",
            """fun radixSort(a: IntArray) {
    var exp = 1
    val max = a.max()
    while (max / exp > 0) {
        countingSortByDigit(a, exp) // stable sort by digit
        exp *= 10
    }
}""", false, ::radixSort
        ),
        AlgoInfo(
            "bucket", "Bucket Sort", "Sorting",
            "Distributes elements into buckets by their value range, sorts each bucket, then concatenates them in order.",
            "O(n + k)", "O(n + k)", "O(n²)", "O(n)",
            """fun bucketSort(a: IntArray) {
    val buckets = Array(a.size) { mutableListOf<Int>() }
    for (v in a) buckets[v / a.size].add(v)
    var idx = 0
    for (b in buckets) {
        b.sort()
        for (v in b) a[idx++] = v
    }
}""", false, ::bucketSort
        ),
        AlgoInfo(
            "jump", "Jump Search", "Searching",
            "Jumps ahead in fixed-size blocks (√n) on a sorted array until it passes the target, then linearly scans that block.",
            "O(1)", "O(√n)", "O(√n)", "O(1)",
            """fun jumpSearch(a: IntArray, target: Int): Int {
    val block = sqrt(a.size.toDouble()).toInt()
    var prev = 0
    var curr = block
    while (curr < a.size && a[curr - 1] < target) {
        prev = curr; curr += block
    }
    for (i in prev until minOf(curr, a.size))
        if (a[i] == target) return i
    return -1
}""", true, ::jumpSearch
        ),
        AlgoInfo(
            "exponential", "Exponential Search", "Searching",
            "On a sorted array, doubles the search range (1, 2, 4, 8, …) until it brackets the target, then binary-searches that range.",
            "O(1)", "O(log n)", "O(log n)", "O(1)",
            """fun exponentialSearch(a: IntArray, target: Int): Int {
    if (a.isEmpty()) return -1
    var bound = 1
    while (bound < a.size && a[bound] < target) bound *= 2
    var lo = bound / 2
    var hi = minOf(bound, a.size - 1)
    // binary search in [lo..hi]
    while (lo <= hi) {
        val mid = (lo + hi) / 2
        when {
            a[mid] == target -> return mid
            a[mid] < target -> lo = mid + 1
            else -> hi = mid - 1
        }
    }
    return -1
}""", true, ::exponentialSearch
        ),
        AlgoInfo(
            "ternary", "Ternary Search", "Searching",
            "Like binary search but splits the sorted range into three parts using two midpoints, halving-and-a-third each step.",
            "O(1)", "O(log₃ n)", "O(log₃ n)", "O(1)",
            """fun ternarySearch(a: IntArray, target: Int): Int {
    var lo = 0
    var hi = a.size - 1
    while (lo <= hi) {
        val m1 = lo + (hi - lo) / 3
        val m2 = hi - (hi - lo) / 3
        when {
            a[m1] == target -> return m1
            a[m2] == target -> return m2
            target < a[m1] -> hi = m1 - 1
            target > a[m2] -> lo = m2 + 1
            else -> { lo = m1 + 1; hi = m2 - 1 }
        }
    }
    return -1
}""", true, ::ternarySearch
        )
    )

    fun byId(id: String): AlgoInfo = all.first { it.id == id }
}
