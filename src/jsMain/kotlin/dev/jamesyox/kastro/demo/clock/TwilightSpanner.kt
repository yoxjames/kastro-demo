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

import dev.jamesyox.kastro.demo.calculateOpacity
import dev.jamesyox.kastro.demo.computeAngle
import dev.jamesyox.kastro.demo.sol.SolarPhaseState
import dev.jamesyox.kastro.demo.sol.stroke
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.attr.strokeOpacity
import dev.jamesyox.svgk.attr.types.obj.pct
import dev.jamesyox.svgk.attrs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.w3c.dom.svg.SVGElement
import kotlin.time.Instant

context(coroutineScope: CoroutineScope)
fun TagConsumer<SVGElement>.TwilightSpanner(
    radius: Double,
    solarPhaseStates: List<SolarPhaseState>,
    timeRange: ClosedRange<Instant>,
    selected: Flow<Selected.SelectedState?>,
    onClick: (SolarPhaseState) -> Unit
) {
    val elements = solarPhaseStates.associateBy(keySelector = { it }) {
        circlePart(
            radius = radius,
            strokeWidth = 20.0,
            stroke = it.phase.stroke,
            start = timeRange.computeAngle(it.start),
            theta = timeRange.computeAngle(from = it.start, time = it.end),
            onClick = { onClick(it) }
        )
    }

    coroutineScope.launch {
        selected.collect { selectedState ->
            elements.forEach { mapEntry ->
                mapEntry.value.attrs {
                    strokeOpacity = mapEntry.key.calculateOpacity(selectedState).pct
                }
            }
        }
    }
}
