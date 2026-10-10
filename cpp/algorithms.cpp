template <typename T>
void LinkedList<T>::pushBack(const T& v) {
    ListNode<T>* n = new ListNode<T>(v);
    if (!head_) { head_ = n; return; }
    ListNode<T>* cur = head_;
    while (cur->next) cur = cur->next;   // O(n) — walk to the tail
    cur->next = n;
}

template <typename T>
void LinkedList<T>::removeFront() {
    if (!head_) throw std::underflow_error("List is empty");
    ListNode<T>* t = head_;
    head_ = head_->next;
    delete t;                            // O(1)
}

template <typename T>
void LinkedList<T>::removeAt(std::size_t i) {
    if (!head_ || i >= getSize()) throw std::out_of_range("Index out of range");
    if (i == 0) { removeFront(); return; }
    ListNode<T>* cur = head_;
    for (std::size_t k = 0; k < i - 1; ++k) cur = cur->next; // O(i)
    ListNode<T>* t = cur->next;          // O(1)
    cur->next = t->next;
    delete t;
}

template <typename T>
T& LinkedList<T>::get(std::size_t i) {
    if (!head_ || i >= getSize()) throw std::out_of_range("Index out of range");
    ListNode<T>* cur = head_;
    for (std::size_t k = 0; k < i; ++k) cur = cur->next; // O(i)
    return cur->data;
}

template <typename T>
std::size_t LinkedList<T>::getSize() const {
    std::size_t n = 0;
    for (ListNode<T>* cur = head_; cur; cur = cur->next, ++n) {}
    return n;
}

// ---------------------------------------------------------------------------
// SORTING
// ---------------------------------------------------------------------------
void bubbleSort(std::vector<int>& a) {
    std::size_t n = a.size();
    for (std::size_t i = 0; i < n - 1; ++i) {
        bool swapped = false;
        for (std::size_t j = 0; j < n - 1 - i; ++j) {
            if (a[j] > a[j + 1]) {
                std::swap(a[j], a[j + 1]);
                swapped = true;
            }
        }
        if (!swapped) break;             // best case O(n): already sorted
    }
}

void selectionSort(std::vector<int>& a) {
    std::size_t n = a.size();
    for (std::size_t i = 0; i < n - 1; ++i) {
        std::size_t min = i;
        for (std::size_t j = i + 1; j < n; ++j) {
            if (a[j] < a[min]) min = j;  // O(n^2) comparisons, always
        }
        std::swap(a[i], a[min]);
    }
}

void insertionSort(std::vector<int>& a) {
    std::size_t n = a.size();
    for (std::size_t i = 1; i < n; ++i) {
        int key = a[i];
        std::size_t j = i;
        while (j > 0 && a[j - 1] > key) {  // best case: no shift → O(n)
            a[j] = a[j - 1];
            --j;
        }
        a[j] = key;
    }
}

// ---------------------------------------------------------------------------
// SEARCHING
// ---------------------------------------------------------------------------
int linearSearch(const std::vector<int>& a, int target) {
    for (std::size_t i = 0; i < a.size(); ++i) {  // O(n) worst
        if (a[i] == target) return static_cast<int>(i);
    }
    return -1;
}

int binarySearch(const std::vector<int>& a, int target) {
    std::size_t lo = 0, hi = a.size() - 1;
    while (lo <= hi) {
        std::size_t mid = lo + (hi - lo) / 2;
        if (a[mid] == target) return static_cast<int>(mid);
        if (a[mid] < target) lo = mid + 1;      // O(log n): halve range
        else hi = mid - 1;
    }
    return -1;
}
