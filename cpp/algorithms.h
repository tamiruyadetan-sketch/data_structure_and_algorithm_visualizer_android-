#pragma once
#include <cstddef>
#include <utility>
#include <vector>
#include <stdexcept>

// ============================================================================
// 1. ARRAYS
// A contiguous block of memory. Access is O(1) because the address of
// element i is computed as base_address + i * element_size.
// ============================================================================
template <typename T>
class Array {
public:
    explicit Array(std::size_t capacity) : cap_(capacity), size_(0) {
        data_ = new T[capacity];
    }
    ~Array() { delete[] data_; }

    Array(const Array&) = delete;
    Array& operator=(const Array&) = delete;

    void pushBack(const T& v) {
        if (size_ == cap_) throw std::overflow_error("Array overflow");
        data_[size_++] = v;
    }
    T& at(std::size_t i) {
        if (i >= size_) throw std::out_of_range("Array index out of range");
        return data_[i];
    }
    const T& at(std::size_t i) const {
        if (i >= size_) throw std::out_of_range("Array index out of range");
        return data_[i];
    }
    std::size_t getSize() const { return size_; }
    bool isEmpty() const { return size_ == 0; }

private:
    T* data_;
    std::size_t cap_;
    std::size_t size_;
};

// ============================================================================
// 2. LINKED LISTS  (singly linked)
// Each node is allocated separately, so nodes are NOT stored contiguously.
// ============================================================================
template <typename T>
struct ListNode {
    T data;
    ListNode* next;
    explicit ListNode(const T& d, ListNode* n = nullptr) : data(d), next(n) {}
};

template <typename T>
class LinkedList {
public:
    LinkedList() : head_(nullptr) {}
    ~LinkedList() {
        while (ListNode<T>* cur = head_) { head_ = head_->next; delete cur; }
    }

    void pushFront(const T& v) {
        head_ = new ListNode<T>(v, head_);   // O(1)
    }
    void pushBack(const T& v);               // O(n) — must walk to the tail
    void removeFront();                      // O(1)
    void removeAt(std::size_t i);            // O(n)
    T& get(std::size_t i);                   // O(n)
    std::size_t getSize() const;

private:
    ListNode<T>* head_;
};

// ============================================================================
// 3. STACK  (LIFO)
// Implemented on top of an array. Top is the last element of the array.
// ============================================================================
template <typename T, std::size_t CAP = 1024>
class Stack {
public:
    void push(const T& v) {
        if (top_ == CAP) throw std::overflow_error("Stack overflow");
        data_[top_++] = v;                   // O(1)
    }
    T pop() {
        if (isEmpty()) throw std::underflow_error("Stack underflow");
        return data_[--top_];                // O(1)
    }
    T peek() const {
        if (isEmpty()) throw std::underflow_error("Stack is empty");
        return data_[top_ - 1];              // O(1)
    }
    bool isEmpty() const { return top_ == 0; }

private:
    T data_[CAP];
    std::size_t top_ = 0;
};

// ============================================================================
// 4. QUEUE  (FIFO)
// Queue needs to remove from the FRONT. With a plain vector this deletion
// costs O(n). A circular buffer keeps both enqueue and dequeue at O(1).
// ============================================================================
template <typename T, std::size_t CAP = 1024>
class Queue {
public:
    void enqueue(const T& v) {
        if (size_ == CAP) throw std::overflow_error("Queue overflow");
        data_[rear_] = v;
        rear_ = (rear_ + 1) % CAP;           // O(1)
        ++size_;
    }
    T dequeue() {
        if (isEmpty()) throw std::underflow_error("Queue is empty");
        T v = data_[front_];
        front_ = (front_ + 1) % CAP;         // O(1)
        --size_;
        return v;
    }
    T peek() const {
        if (isEmpty()) throw std::underflow_error("Queue is empty");
        return data_[front_];                // O(1)
    }
    bool isEmpty() const { return size_ == 0; }

private:
    T data_[CAP];
    std::size_t front_ = 0;
    std::size_t rear_ = 0;
    std::size_t size_ = 0;
};

// ============================================================================
// 5. BINARY SEARCH TREE  (BST)
// Each node keeps an ordered left/right child. Search/Insert/Delete walk
// from the root; cost depends on tree HEIGHT h.
// Balanced BST (AVL, Red-Black): h = O(log n).
// Unbalanced degenerate tree: h = O(n).
// ============================================================================
template <typename T>
struct BSTNode {
    T key;
    BSTNode* left;
    BSTNode* right;
    explicit BSTNode(const T& k, BSTNode* l = nullptr, BSTNode* r = nullptr)
        : key(k), left(l), right(r) {}
};

template <typename T>
class BST {
public:
    ~BST() { destroy(root_); }
    void insert(const T& k) { root_ = insert(root_, k); }     // O(h)
    bool search(const T& k) const { return search(root_, k); } // O(h)
    bool remove(const T& k) { root_ = remove(root_, k); }      // O(h)

private:
    BSTNode<T>* root_;

    BSTNode<T>* insert(BSTNode<T>* n, const T& k) {
        if (!n) return new BSTNode<T>(k);
        if (k < n->key) n->left = insert(n->left, k);
        else if (k > n->key) n->right = insert(n->right, k);
        return n;
    }
    bool search(BSTNode<T>* n, const T& k) const {
        if (!n) return false;
        if (k == n->key) return true;
        return k < n->key ? search(n->left, k) : search(n->right, k);
    }
    BSTNode<T>* remove(BSTNode<T>* n, const T& k) {
        if (!n) return nullptr;
        if (k < n->key) n->left = remove(n->left, k);
        else if (k > n->key) n->right = remove(n->right, k);
        else {
            if (!n->left) { BSTNode<T>* t = n->right; delete n; return t; }
            if (!n->right) { BSTNode<T>* t = n->left; delete n; return t; }
            BSTNode<T>* succ = n->right;
            while (succ->left) succ = succ->left;
            n->key = succ->key;
            n->right = remove(n->right, succ->key);
        }
        return n;
    }
    void destroy(BSTNode<T>* n) {
        if (!n) return;
        destroy(n->left); destroy(n->right); delete n;
    }
};

// ============================================================================
// 6. SORTING  (all operate in-place on a vector)
// ============================================================================
void bubbleSort(std::vector<int>& a);      // O(n^2) time, O(1) space
void selectionSort(std::vector<int>& a);   // O(n^2) time, O(1) space
void insertionSort(std::vector<int>& a);   // O(n) best, O(n^2) worst, O(1) space

// ============================================================================
// 7. SEARCHING
// ============================================================================
int linearSearch(const std::vector<int>& a, int target);     // O(n)
int binarySearch(const std::vector<int>& a, int target);     // O(log n), needs sorted
