# DSA Visualizer — Complexity Reference (C++)

All data structures and algorithms are implemented in C++ here, then mirrored in the
Android UI. This document gives the **why** behind every Big-O claim.

---

## 1. Arrays (contiguous memory)

| Operation | Time |
|---|---|
| Access by index `arr[i]` | **O(1)** |
| Search (unordered) | **O(n)** |
| Insert at index | **O(n)** |
| Delete at index | **O(n)** |
| Append / push_back | **O(1)** amortized |

**Why?**
- Elements live in **one contiguous block**. The address of `arr[i]` is
  `base + i * sizeof(element)`, so any index is a single arithmetic step:
  `O(1)`.
- Insertion/deletion in the *middle* forces every later element to shift:
  `i` elements → `O(i)` ≤ `O(n)`.
- Appending is `O(1)` **amortized**: a resize copies the whole block,
  but it happens only after the size doubles, so the cost is spread
  (`1 + 2 + 4 + ... + n = O(n)` work over `n` appends).

---

## 2. Linked Lists (nodes + pointers)

| Operation | Time |
|---|---|
| Access / search | **O(n)** |
| Insert / delete at HEAD | **O(1)** |
| Insert / delete at TAIL (with tail pointer) | **O(1)** |
| Insert / delete at a KNOWN index/node | **O(1)** |
| Insert / delete at an unknown index | **O(n)** |

**Why?**
- There is **no contiguous block**: to reach the `i`-th node you must
  follow `next` pointers one by one → `O(i)` ≤ `O(n)`.
- Head insertion/deletion is a single pointer re-write: `O(1)`.
- A **doubly linked** list adds a `prev` pointer so deletion given a node
  reference is also `O(1)` (here we use singly linked, so deletion needs a
  walk).

---

## 3. Stack (LIFO)

| Operation | Time |
|---|---|
| Push / pop / peek | **O(1)** |

**Why?**
- The Stack keeps a `top` index into an array. All three operations only
  touch that one position → `O(1)`.
- Implemented here on a fixed-size array; with a dynamic array push is
  `O(1)` amortized (the resize cost is spread).

---

## 4. Queue (FIFO)

| Operation | Time |
|---|---|
| Enqueue / dequeue / peek | **O(1)** |

**Why?**
- A naive `Vector`-based queue does `removeAt(0)` which shifts every
  element left → `O(n)`.
- The **circular buffer** reuses freed front space: both `enqueue` and
  `dequeue` only update one index (`front`/`rear`) and a size counter →
  `O(1)` each.

---

## 5. Binary Search Tree (BST)

| Operation | Average | Worst |
|---|---|---|
| Search / Insert / Delete | **O(log n)** | **O(n)** |

**Why?**
- BST cost depends on **height `h`**, not directly on node count.
- Balanced tree: each step discards half the remaining nodes →
  `h = O(log n)`.
- All keys inserted in order → a **degenerate (linked-list) tree**:
  `h = O(n)`, so every operation degrades to `O(n)`.
- This is exactly why balanced BSTs (AVL / Red-Black) rotate to keep
  `h = O(log n)`.

---

## 6. Sorting algorithms

### Bubble Sort
| Case | Time |
|---|---|
| Best | **O(n)** |
| Average / Worst | **O(n²)** |
| Space | **O(1)** |

**Why?** Two nested loops over the data: `n·(n-1)/2` comparisons →
`O(n²)`. The early-`break` exits **as soon as a full pass finds no swap**,
so an already-sorted input finishes after one pass → **O(n)**.

### Selection Sort
| Case | Time |
|---|---|
| Best / Average / Worst | **O(n²)** |
| Space | **O(1)** |

**Why?** The outer loop runs `n-1` times and the inner loop scans the
unsorted suffix every time → `n·(n-1)/2` comparisons → `O(n²)` always,
even on sorted input (it can't detect order, so no early exit).

### Insertion Sort
| Case | Time |
|---|---|
| Best | **O(n)** |
| Average / Worst | **O(n²)** |
| Space | **O(1)** |

**Why?** Each element is inserted into the already-sorted prefix. On an
already-sorted array the inner `while` never runs → `O(n)`. On a reverse
sorted array every element is shifted all the way left → **`O(n²)`**.

---

## 7. Searching algorithms

### Linear Search
| Case | Time |
|---|---|
| Best | **O(1)** |
| Average / Worst | **O(n)** |

**Why?** One pass that returns as soon as the target matches. Best case
= first element; worst case = element not present or at the end.

### Binary Search
| Case | Time |
|---|---|
| Best | **O(1)** |
| Average / Worst | **O(log n)** |
| Space | **O(1)** iterative |

**Why?** The array is cut in half each step. `n → n/2 → n/4 → ... → 1`
means `2ᵏ = n` → `k = log₂ n` steps. **Requires a sorted array.**

---

## 8. Space complexity summary

| Structure / Algorithm | Auxiliary Space |
|---|---|
| Array | `O(1)` (input) + storage |
| Linked List | `O(n)` for `n` nodes (each node allocates separately) |
| Stack (array) | `O(n)` |
| Queue (circular) | `O(n)` |
| BST | `O(n)` nodes |
| Bubble / Selection / Insertion sort | **O(1)** |
| Linear search | **O(1)** |
| Binary search | **O(1)** (iterative) |

**Note:** recursion uses hidden *call-stack* memory. Binary search
implemented recursively costs `O(log n)` call frames; the iterative
version above stays `O(1)` extra space.

---

## Complexity cheat-sheet

| | Access | Search | Insert | Delete |
|---|---|---|---|---|
| **Array** | O(1) | O(n) | O(n) | O(n) |
| **Linked List** | O(n) | O(n) | O(1) head | O(1) head |
| **Stack (array)** | O(n) | O(n) | O(1) | O(1) |
| **Queue (circ.** | O(n) | O(n) | O(1) | O(1) |
| **BST (avg)** | O(log n) | O(log n) | O(log n) | O(log n) |
| **BST (worst)** | O(n) | O(n) | O(n) | O(n) |

| Algorithm | Best | Average | Worst | Space |
|---|---|---|---|---|
| Bubble Sort | O(n) | O(n²) | O(n²) | O(1) |
| Selection Sort | O(n²) | O(n²) | O(n²) | O(1) |
| Insertion Sort | O(n) | O(n²) | O(n²) | O(1) |
| Linear Search | O(1) | O(n) | O(n) | O(1) |
| Binary Search | O(1) | O(log n) | O(log n) | O(1) |
