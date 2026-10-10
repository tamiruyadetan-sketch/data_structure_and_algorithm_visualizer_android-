package com.example.dsavisualizer.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dsavisualizer.engine.Algorithms
import com.example.dsavisualizer.model.Mark

fun markColor(m: Mark): Color = when (m) {
    Mark.NONE -> Color(0xFF90A4AE)
    Mark.COMPARE -> Color(0xFFFFB300)
    Mark.SWAP -> Color(0xFFE53935)
    Mark.SORTED -> Color(0xFF43A047)
    Mark.FOUND -> Color(0xFF00C853)
    Mark.RANGE -> Color(0xFF64B5F6)
    Mark.PIVOT -> Color(0xFF8E24AA)
}

private fun parseInput(s: String): List<Int> =
    s.split(",", " ", ";").mapNotNull { it.trim().toIntOrNull() }.take(15)

@Composable
fun VisualizerScreen(nav: NavController, id: String) {
    val algo = remember(id) { Algorithms.byId(id) }
    val vm: VisualizerViewModel = viewModel()
    var input by rememberSaveable { mutableStateOf("5, 3, 8, 1, 9, 2") }
    var target by rememberSaveable { mutableStateOf("8") }

    fun visualize() = vm.load(algo, parseInput(input), target.toIntOrNull())
    LaunchedEffect(id) { visualize() }

    AppScaffold(algo.name, nav) {
        Text(algo.description)

        OutlinedTextField(
            value = input, onValueChange = { input = it },
            label = { Text("Numbers (comma separated, max 15)") },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        if (algo.needsTarget) {
            OutlinedTextField(
                value = target, onValueChange = { target = it },
                label = { Text("Target") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(onClick = { visualize() }, modifier = Modifier.fillMaxWidth()) { Text("VISUALIZE") }

        val step = vm.current
        if (step == null) {
            Text("Enter at least one valid number.", color = MaterialTheme.colorScheme.error)
        } else {
            Bars(step.values, step.marks)
            Text(step.message, style = MaterialTheme.typography.titleMedium)
            Text("Step ${vm.index + 1} / ${vm.steps.size}")
            LinearProgressIndicator(
                progress = { (vm.index + 1f) / vm.steps.size },
                modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = vm::restart, contentPadding = PaddingValues(12.dp)) { Text("⟲") }
                Button(onClick = vm::previous, contentPadding = PaddingValues(12.dp)) { Text("⏮") }
                Button(
                    onClick = { if (vm.playing) vm.pause() else vm.play() },
                    contentPadding = PaddingValues(12.dp)
                ) { Text(if (vm.playing) "⏸" else "▶") }
                Button(onClick = vm::next, contentPadding = PaddingValues(12.dp)) { Text("⏭") }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Slow")
                Slider(value = vm.speed, onValueChange = { vm.speed = it }, modifier = Modifier.weight(1f))
                Text("Fast")
            }
            Legend()
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Complexity", style = MaterialTheme.typography.titleMedium)
                Text("Best: ${algo.best}")
                Text("Average: ${algo.average}")
                Text("Worst: ${algo.worst}")
                Text("Space: ${algo.space}")
            }
        }
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Kotlin code", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text(
                    algo.code, fontFamily = FontFamily.Monospace, fontSize = 12.sp,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
fun Bars(values: List<Int>, marks: Map<Int, Mark>) {
    val max = (values.maxOrNull() ?: 1).coerceAtLeast(1)
    Row(
        Modifier.fillMaxWidth().height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEachIndexed { i, v ->
            val color by animateColorAsState(markColor(marks[i] ?: Mark.NONE), label = "bar")
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$v", fontSize = 11.sp)
                Box(
                    Modifier.fillMaxWidth()
                        .height((160f * v / max).coerceAtLeast(8f).dp)
                        .background(color, RoundedCornerShape(4.dp))
                )
                Text("$i", fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun Legend() {
    val items = listOf("Compare" to Mark.COMPARE, "Swap" to Mark.SWAP, "Sorted" to Mark.SORTED,
        "Found" to Mark.FOUND, "Range" to Mark.RANGE, "Pivot/Min" to Mark.PIVOT)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { (label, m) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(12.dp).background(markColor(m), RoundedCornerShape(2.dp)))
                        Spacer(Modifier.width(4.dp))
                        Text(label, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
