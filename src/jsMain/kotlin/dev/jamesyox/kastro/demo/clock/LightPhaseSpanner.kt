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
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.radiusOffset
import dev.jamesyox.kastro.demo.sol.stroke
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.attrs.strokeOpacity
import dev.jamesyox.svg4kt.attrs
import dev.jamesyox.svg4kt.meta.unsafe
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.w3c.dom.svg.SVGElement
import kotlin.time.Instant

context(
    coroutineScope: CoroutineScope,
    tagConsumer: TagConsumer<SVGElement>,
    _: ElementContainer.Circle
)
fun LightPhaseSpan(
    radius: Double,
    solarLightState: List<SolarLightState>,
    timeRange: ClosedRange<Instant>,
    selected: Flow<Selected.SelectedState?>,
    onClick: (SolarLightState) -> Unit
) {
    val elements = solarLightState.associateBy(keySelector = { it }) {
        circlePart(
            radius = radius + it.lightState.radiusOffset,
            start = timeRange.computeAngle(it.start),
            theta = timeRange.computeAngle(from = it.start, time = it.end),
            outlineColor = it.lightState.stroke,
            outlineWidth = 3.0
        ) { onClick(it) }
    }

    coroutineScope.launch {
        selected.collect { selectedState ->
            elements.forEach { mapEntry ->
                mapEntry.value.attrs {
                    unsafe {
                        strokeOpacity = (mapEntry.key.calculateOpacity(selectedState) / 100.0).toFloat()
                    }
                }
            }
        }
    }
}
