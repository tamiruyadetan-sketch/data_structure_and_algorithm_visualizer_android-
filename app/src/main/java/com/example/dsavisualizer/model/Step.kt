package com.example.dsavisualizer.model

/** Role of an element in the current animation step. */
enum class Mark { NONE, COMPARE, SWAP, SORTED, FOUND, RANGE, PIVOT }

/**
 * One frame of an algorithm run.
 * The algorithm engine produces a list of these; the UI only renders them.
 */
data class Step(
    val values: List<Int>,
    val marks: Map<Int, Mark> = emptyMap(),
    val message: String
)

data class AlgoInfo(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val best: String,
    val average: String,
    val worst: String,
    val space: String,
    val code: String,
    val needsTarget: Boolean,
    val run: (List<Int>, Int?) -> List<Step>
)
