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

package dev.jamesyox.kastro.demo.clock

import dev.jamesyox.kastro.common.HorizonState
import dev.jamesyox.kastro.demo.calculateOpacity
import dev.jamesyox.kastro.demo.computeAngle
import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.strokeOpacity
import dev.jamesyox.svgk.attrs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.w3c.dom.svg.SVGElement

private const val MOON_UP_COLOR = "#ffffff"
private const val MOON_DOWN_COLOR = "#2f4f4f"

fun TagConsumer<SVGElement>.LunarHorizonSpanner(
    radius: Double,
    timeRange: ClosedRange<Instant>,
    lunarHorizonState: List<LunarHorizonState>,
    coroutineScope: CoroutineScope,
    selected: Flow<Selected.SelectedState?>,
    onClick: (LunarHorizonState) -> Unit
) {
    val elements = lunarHorizonState.associateBy(keySelector = { it }) {
        circlePart(
            radius = radius,
            stroke = it.horizonState.stroke,
            strokeWidth = 20.0,
            start = timeRange.computeAngle(it.start),
            theta = timeRange.computeAngle(from = it.start, time = it.end),
            onClick = { onClick(it) }
        )
    }

    coroutineScope.launch {
        selected.collect { selectedState ->
            elements.forEach { mapEntry ->
                mapEntry.value.attrs {
                    strokeOpacity = mapEntry.key.calculateOpacity(selectedState)
                }
            }
        }
    }
}

internal val HorizonState.stroke get() = when (this) {
    HorizonState.Up -> MOON_UP_COLOR
    HorizonState.Down -> MOON_DOWN_COLOR
}
