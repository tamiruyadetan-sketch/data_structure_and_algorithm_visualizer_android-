package com.example.dsavisualizer.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dsavisualizer.model.AlgoInfo
import com.example.dsavisualizer.model.Step
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VisualizerViewModel : ViewModel() {
    var steps by mutableStateOf<List<Step>>(emptyList())
        private set
    var index by mutableIntStateOf(0)
        private set
    var playing by mutableStateOf(false)
        private set
    var speed by mutableFloatStateOf(0.5f) // 0 = slow, 1 = fast

    private var job: Job? = null

    val current: Step? get() = steps.getOrNull(index)

    fun load(algo: AlgoInfo, input: List<Int>, target: Int?) {
        pause()
        steps = if (input.isEmpty()) emptyList() else algo.run(input, target)
        index = 0
    }

    fun play() {
        if (steps.isEmpty()) return
        if (index >= steps.lastIndex) index = 0
        job?.cancel()
        playing = true
        job = viewModelScope.launch {
            while (index < steps.lastIndex) {
                delay(((1.1f - speed) * 1200).toLong())
                index++
            }
            playing = false
        }
    }

    fun pause() {
        job?.cancel()
        playing = false
    }

    fun next() { pause(); if (index < steps.lastIndex) index++ }
    fun previous() { pause(); if (index > 0) index-- }
    fun restart() { pause(); index = 0 }
}
