# data_structure_and_algorithm_visualizer_android-
This data structure and algorithm definition , example  short implemantation  by kotlin and visiualize to understand easily on android  mobile
# 📊 DSA Visualizer

**An interactive Android app that teaches Data Structures and Algorithms by letting you *watch* them run, step by step.**

Built with **Kotlin** and **Jetpack Compose**. Works fully **offline**.

> Instead of only reading that Bubble Sort "compares adjacent elements", you see exactly which two bars are compared, when they swap, and why.

---

## ✨ Features

- **62 topics**: 23 data structures and 39 algorithms.
- **Step-by-step animation** for every topic with **▶ Play, ⏸ Pause, ⏭ Next, ⏮ Previous, ⟲ Restart** and a **speed slider**.
- **Your own input**: type your own numbers, words, graph edges or matrix values and watch the result.
- **Detailed explanation** for each topic: definition, how it works (steps), time and space complexity, real-world uses.
- **Kotlin code** shown for every algorithm.
- **Live explanation text** on every frame ("Compare 5 and 3, so swap them").
- **Colour legend**: compare, swap, sorted, found, visited, pivot, red/black nodes ...
- **Search** inside the topic lists.
- **Practice quiz** with 16 multiple-choice questions and scoring.
- **No internet, no account, no ads.**

---

## 📚 What is covered

### Data Structures (23)

| Group | Topics |
|---|---|
| **Linear** | Array · Dynamic Array · Singly Linked List · Doubly Linked List · Circular Linked List · Stack · Simple Queue · Circular Queue · Priority Queue · Deque |
| **Trees** | Binary Tree · Binary Search Tree · AVL Tree · Red-Black Tree · Min Heap · Max Heap · B-Tree · B+ Tree · Trie |
| **Graphs** | Directed · Undirected · Weighted · Unweighted |

### Algorithms (39)

| Category | Topics |
|---|---|
| **Searching (6)** | Linear · Binary · Jump · Interpolation · Exponential · Fibonacci |
| **Sorting (10)** | Bubble · Selection · Insertion · Merge · Quick · Heap · Counting · Radix · Bucket · Shell |
| **Graph (10)** | BFS · DFS · Dijkstra · Bellman-Ford · Floyd-Warshall · A\* · Kruskal · Prim · Topological Sort · Tarjan (SCC) |
| **Tree (9)** | Preorder · Inorder · Postorder · Level-order traversal · BST Search · BST Insertion · BST Deletion · AVL Rotation · Heapify |
| **Divide & Conquer (4)** | Merge Sort · Quick Sort · Binary Search · Strassen's Matrix Multiplication |

---

## 🧠 How it works

The most important design decision: the **algorithm engine is completely separated from the UI**.

```
 User input (text)
        │
        ▼
 ┌───────────────┐      List<Frame>      ┌──────────────────┐
 │ Algorithm     │ ────────────────────► │ Visualizer       │
 │ engine        │  (immutable snapshots) │ (Jetpack Compose)│
 │ pure Kotlin   │                        │ draws one frame  │
 └───────────────┘                        └──────────────────┘
```

Every algorithm runs once and returns a list of **frames** (snapshots of the structure plus an explanation message). The UI simply steps through that list. Because frames are stored, **going backwards is trivial** and the same player works for everything.

There are six kinds of frames, so the whole app needs only six renderers:

| Frame | Used for |
|---|---|
| `BarsFrame` | sorting and searching on arrays |
| `TreeFrame` | BST, AVL, Red-Black, heaps, B-Tree, B+ Tree, Trie, traversals |
| `GraphFrame` | all graph algorithms and graph types |
| `GridFrame` | Floyd-Warshall matrix, A\* maze, Strassen |
| `ChainFrame` | arrays, linked lists, stacks, queues, deque |
| `MessageFrame` | errors / hints |

---

## 🗂 Project structure

```
app/src/main/java/com/example/dsavisualizer/
├── MainActivity.kt
├── model/
│   ├── Frame.kt            # Frame types + Mark (colour roles)
│   └── Topic.kt            # Topic, InputKind, RunArgs
├── engine/                 # pure Kotlin, no Android dependencies
│   ├── Sorting.kt          # 10 sorting algorithms
│   ├── Searching.kt        # 6 searching algorithms
│   ├── GraphAlgos.kt       # 10 graph algorithms + graph parser
│   ├── TreeAlgos.kt        # BST, AVL, Red-Black, heap, B/B+ tree, trie, traversals
│   ├── StructDemos.kt      # array, linked lists, stack, queues, graph types, Strassen
│   └── Util.kt             # frame helpers + generic tree layout
├── content/
│   ├── CatalogStructures.kt   # text for the 23 data structures
│   ├── CatalogAlgorithms.kt   # text + code for the 39 algorithms
│   └── Catalog.kt
└── ui/
    ├── Navigation.kt
    ├── Screens.kt          # home + topic list
    ├── TopicScreen.kt      # explanation + player for one topic
    ├── Renderers.kt        # bars / tree / graph / grid / chain drawing
    ├── VisualizerViewModel.kt   # play, pause, next, previous, speed
    └── PracticeScreen.kt   # quiz
```

---

## 🚀 Getting started

### Requirements

- **Android Studio** Koala (2024.1) or newer
- **JDK 17** (bundled with Android Studio)
- Android device or emulator with **API 24+** (Android 7.0)

### Run

```bash
git clone https://github.com/<your-username>/DSAVisualizer.git
cd DSAVisualizer
```

1. Open the folder in **Android Studio** (*File → Open*).
2. Let Gradle sync finish (accept the Gradle wrapper prompt if it appears).
3. Press **Run ▶** on an emulator or a connected phone.

### Tests

```bash
./gradlew test
```

The unit tests check that every sorting algorithm sorts correctly, every search finds (or correctly misses) its target, and every one of the 62 topics produces frames with its default input.

---

## ⌨️ Input formats

| Topic type | Input example |
|---|---|
| Numbers (arrays, sorting, trees, heaps ...) | `25, 10, 40, 5, 30` |
| Search algorithms | numbers + a **target** value |
| Trie | words `cat car dog` + a word to search |
| Graphs | edges `A-B:4, A-C:2, B-D:5` (`:weight` optional, up to 10 vertices) |
| Directed graphs (Bellman-Ford, Topological Sort, Tarjan, Floyd-Warshall) | the same format; edges are treated as one-way from first to second vertex |
| Strassen | 8 numbers: `A11 A12 A21 A22 B11 B12 B21 B22` |

Graph algorithms with a start vertex (BFS, DFS, Dijkstra, Bellman-Ford, Prim) take its name in the **target** field.

---

## ➕ Adding a new algorithm

1. **Write the engine** in `engine/`. It must return a `List<Frame>`:

   ```kotlin
   fun cocktailSort(input: List<Int>): List<Frame> {
       val a = input.toMutableList()
       val f = mutableListOf<Frame>(bf(a, "Initial array"))
       // ... on every comparison / swap:
       f += bf(a, "Compare ${a[i]} and ${a[i + 1]}", mapOf(i to Mark.COMPARE, i + 1 to Mark.COMPARE))
       return f
   }
   ```

2. **Register it** in `content/CatalogAlgorithms.kt`:

   ```kotlin
   alg("cocktail-sort", "Cocktail Sort", SORT,
       "Definition ...", "1. Step\n2. Step", "Where it is used ...",
       ac("O(n)", "O(n^2)", "O(n^2)", "O(1)"),
       "fun cocktailSort(...) { ... }",
       default = SORT_DEFAULT) { Sorting.cocktail(it.nums()) }
   ```

That is all: it appears in the list, gets a search entry, and uses the same animation controls. No new UI code is required.

---

## 🛠 Tech stack

| | |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| State | ViewModel + Compose state |
| Drawing | Compose `Canvas` and `TextMeasurer` |
| Tests | JUnit 4 |
| Min SDK | 24 |

---

## 🗺 Roadmap

- [ ] Progress tracking and favourites (Room / DataStore)
- [ ] Hash Table with collision handling
- [ ] Larger-scale Strassen (recursive n × n matrices)
- [ ] More quiz questions and an "animation challenge" mode ("what happens next?")
- [ ] Dark theme polish and tablet layout
- [ ] Additional languages

---

## 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a branch: `git checkout -b feature/my-feature`
3. Commit your changes and run `./gradlew test`
4. Open a Pull Request

---

## 📄 License

Released under the **MIT License**. Add a `LICENSE` file with your name and year before publishing.

---

## 👤 Author

**Your Name**: [GitHub](https://github.com/your-username)

If this project helped you learn, give it a ⭐

