/*
 * Copyright 2024 James Yox
 *
 * This file is part of kastro-demo.
 *
 * Kastro-demo is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Kastro-demo is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with kastro-demo. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package dev.jamesyox.kastro.demo

import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.SolarPhaseState
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.statik.css
import dev.jamesyox.statik.css.Selector
import dev.jamesyox.svgk.JsDomTagConsumer
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlinx.css.StyledElement
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.html.TagConsumer
import kotlinx.html.consumers.onFinalize
import kotlinx.html.dom.createTree
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.MutationObserver
import org.w3c.dom.MutationObserverInit
import org.w3c.dom.Node
import org.w3c.dom.asList
import org.w3c.dom.svg.SVGElement
import kotlin.math.roundToInt
import kotlin.time.Instant

fun SolarPhaseState.calculateOpacity(selected: Selected.SelectedState?): Double {
    return selected?.let { if (it is Selected.SelectedState.Solar && it.solarPhaseState == this) 100.0 else 30.0 }
        ?: 100.0
}

fun SolarLightState.calculateOpacity(selected: Selected.SelectedState?): Double {
    return selected?.let { if (it is Selected.SelectedState.Light && it.solarLightState == this) 100.0 else 30.0 }
        ?: 100.0
}

fun LunarHorizonState.calculateOpacity(selected: Selected.SelectedState?): Double {
    return selected?.let { if (it is Selected.SelectedState.Lunar && it.lunarPhaseState == this) 100.0 else 30.0 }
        ?: 100.0
}

fun <T, K> List<T>.distinctUntilChangedBy(keySelector: (T) -> K): List<T> {
    var previous: K? = null
    return buildList {
        this@distinctUntilChangedBy.forEach {
            val key = keySelector(it)
            if (key != previous) {
                add(it)
            }
            previous = key
        }
    }
}

const val wedgeSize = 20

fun ClosedRange<Instant>.computeAngle(time: Instant?): Double {
    val sanitizedTime = time ?: start
    val totalTime = endInclusive - start
    return ((sanitizedTime - start) / totalTime * (360.0 - wedgeSize) + (wedgeSize / 2.0)) + 90.0
}

fun ClosedRange<Instant>.computeAngle(time: Instant?, from: Instant?): Double {
    return computeAngle(time) - computeAngle(from ?: endInclusive)
}

fun prettyHour(hour: Int): String {
    val displayHour = when {
        hour == 0 -> 12
        hour >= 13 -> hour - 12
        else -> hour
    }

    return "$displayHour"
}

fun Element.replace(content: List<Element>) {
    innerHTML = ""
    content.forEach { appendChild(it) }
}

fun htmlContent(block: TagConsumer<HTMLElement>.() -> Unit): List<HTMLElement> {
    return ArrayList<HTMLElement>().let { result ->
        document.createTree().onFinalize { htmlElement, partial ->
            if (!partial) {
                result.add(htmlElement)
            }
        }.block()

        result
    }
}

fun svgContent(block: dev.jamesyox.svgk.TagConsumer<SVGElement>.() -> Unit): List<SVGElement> {
    return JsDomTagConsumer(document).apply(block).output()
}

context(coroutineScope: CoroutineScope)
fun Flow<List<Element>>.insertingBefore(node: Element, child: Node?) {
    coroutineScope.launch {
        scan(Pair(emptyList<Element>(), emptyList<Element>())) { acc, elementList -> Pair(acc.second, elementList) }
            .collect { elementPairs ->
                elementPairs.first.forEach { node.removeChild(it) }
                elementPairs.second.forEach { node.insertBefore(it, child) }
            }
    }
}

context(coroutineScope: CoroutineScope)
fun Element.replacingInnerHTML(flow: Flow<List<HTMLElement>>) {
    coroutineScope.launch {
        flow.collect {
            innerHTML = ""
            it.forEach { insertBefore(it, null) }
        }
    }
}

context(coroutineScope: CoroutineScope)
fun HTMLElement.replacingInnerText(flow: Flow<String>) {
    coroutineScope.launch { flow.collect { innerText = it } }
}

context(coroutineScope: CoroutineScope)
fun Flow<List<Element>>.mountTo(element: Element) {
    coroutineScope.launch {
        collect {
            element.replace(it)
        }
    }
}

context(coroutineScope: CoroutineScope)
fun Element.settingAttribute(
    qualifiedName: String,
    values: Flow<String>
) {
    coroutineScope.launch {
        values.collect {
            setAttribute(qualifiedName, it)
        }
    }
}

fun Element.domCoroutineScope(): CoroutineScope {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val observer = MutationObserver { mutationRecords, _ ->
        if (mutationRecords.any { it.removedNodes.asList().contains(this) }) {
            coroutineScope.cancel()
        }
    }
    observer.observe(document, MutationObserverInit(childList = true, subtree = true))
    return coroutineScope
}

context(coroutineScope: CoroutineScope)
fun HTMLElement.mountCss(flow: Flow<StyledElement?>) {
    coroutineScope.launch { flow.collect { if (it != null) css(it) } }
}

context(coroutineScope: CoroutineScope)
fun HTMLElement.values(flow: Flow<String>) {
    coroutineScope.launch {
        flow.collect { this@values.asDynamic().value = it }
    }
}

context(coroutineScope: CoroutineScope)
fun HTMLElement.mountClasses(flow: Flow<List<Selector.Class>>) {
    coroutineScope.launch {
        flow.collect {
            className = ""
            className = it.joinToString(" ") { it.name }
        }
    }
}

context(_: CoroutineScope)
fun HTMLElement.mountClass(flow: Flow<Selector.Class>) {
    mountClasses(flow.map { listOf(it) })
}

internal fun LocalTime.prettyString(): String {
    val timeFormat = LocalTime.Format {
        amPmHour(padding = Padding.NONE)
        char(':')
        minute()
        char(':')
        second()
        char(' ')
        amPmMarker(am = "am", pm = "pm")
    }
    return format(timeFormat)
}

fun Double.roundTwoSigFig(): String {
    return ((this * 100).roundToInt() / 100.0).toString()
}
