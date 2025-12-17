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

import dev.jamesyox.kastro.demo.CelestialParameters
import dev.jamesyox.kastro.demo.computeAngle
import dev.jamesyox.kastro.demo.misc.asPair
import dev.jamesyox.kastro.demo.mountTo
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.kastro.demo.svgk.js.svgMagick
import dev.jamesyox.kastro.luna.calculateLunarState
import dev.jamesyox.svgk.attr.objs.transform.Scale
import dev.jamesyox.svgk.attr.objs.transform.Translate
import dev.jamesyox.svgk.attr.types.obj.ViewBox
import dev.jamesyox.svgk.svgElement
import dev.jamesyox.svgk.tags.circle
import dev.jamesyox.svgk.tags.g
import dev.jamesyox.svgk.tags.path
import dev.jamesyox.svgk.tags.svg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.html.TagConsumer
import org.w3c.dom.HTMLElement
import org.w3c.dom.svg.SVGElement
import kotlin.time.Instant

private const val moonClockRadius = 35.0
private const val sunClockRadius = 75.0
private const val moonScaleFactor = 0.03

data class CelestialClockState(
    val time: Instant,
    val celestialParameters: CelestialParameters
)

context(_: CoroutineScope)
fun TagConsumer<HTMLElement>.CelsetialClockComponent(
    state: Flow<CelestialClockState>,
    selected: StateFlow<Selected?>,
    onEvent: (CelestialClockEvent) -> Unit
) {
    val svgRoot = svgMagick {
        svg(viewBox = ViewBox(-100, -100, 200, 200)) { }
    }

    svgRoot.onmouseleave = {
        onEvent(CelestialClockEvent.OnMouseLeave)
    }

    state.asDom(selected) { onEvent(it) }.mountTo(svgRoot)
}

context(_: CoroutineScope)
private fun dev.jamesyox.svgk.TagConsumer<SVGElement>.CelestialClock(
    state: CelestialClockState,
    selected: StateFlow<Selected?>,
    onEvent: (CelestialClockEvent) -> Unit
) {
        val selectedState = selected.filterIsInstance<Selected.SelectedState?>()
        val selectedEvent = selected.filterIsInstance<Selected.SelectedEvent?>()
        ClockOverlay(
            radius = sunClockRadius + 13.0,
            timeRange = state.celestialParameters.timeRange
        )

        // Draw twilight phases
        TwilightSpanner(
            radius = sunClockRadius,
            solarPhaseStates = state.celestialParameters.solarPhaseStates,
            timeRange = state.celestialParameters.timeRange,
            selected = selectedState,
            onClick = { onEvent(CelestialClockEvent.SolarPhaseEvent.Select(it)) }
        )

        // Draw Light Phases (Golden Hour, Blue Hour)
        LightPhaseSpan(
            radius = sunClockRadius - 10.0 - 3.0,
            solarLightState = state.celestialParameters.solarLightState,
            selected = selectedState,
            timeRange = state.celestialParameters.timeRange,
            onClick = { onEvent(CelestialClockEvent.LightEvent.Select(it)) }
        )

        // Draw Moon Horizon Clock
        LunarHorizonSpanner(
            radius = moonClockRadius,
            lunarHorizonState = state.celestialParameters.lunarHorizonState,
            timeRange = state.celestialParameters.timeRange,
            selected = selectedState,
            onClick = { onEvent(CelestialClockEvent.LunarHorizonEvent.Select(it)) }
        )

    // Current Time
    if (state.celestialParameters.timeRange.contains(state.time)) {
        val timePosition = state.celestialParameters.timeRange.computeAngle(state.time)
        val currentTimePosition = SvgCoords.Polar(sunClockRadius + 16.0, timePosition)
        val moonPosition = SvgCoords.Polar(moonClockRadius + 16.0, timePosition)
        circle(
            cx = 0.0,
            cy = 0.0,
            r = 3.0,
            fill = "red",
        )
        path(
            stroke = "red",
            strokeWidth = 0.8,
        ) {
            M(0, 0)
            L(currentTimePosition.x, currentTimePosition.y)
        }
        // Draw the moon
        g(
            transform = listOf(
                Translate(moonPosition.x, moonPosition.y),
                Scale(moonScaleFactor),
                Translate(-100, -100),
            )
        ) {
            Moon(
                state.time.calculateLunarState(state.celestialParameters.location.asPair())
                    .illumination
                    .phase
            )
        }
    }

    // Other events
    EventSpanner(
        timeRange = state.celestialParameters.timeRange,
        solarEvents = state.celestialParameters.solarEvents,
        lunarEvents = state.celestialParameters.lunarEvents,
        outerRadius = sunClockRadius + 11.5,
        innerRadius = sunClockRadius + 10.0,
        selectedInnerRadius = moonClockRadius - 10,
        selectedEvent = selectedEvent,
    )
}

fun Flow<CelestialClockState>.asDom(
    selected: StateFlow<Selected?>,
    onEvent: (CelestialClockEvent) -> Unit
): Flow<List<SVGElement>> {
    var coroutineScope: CoroutineScope? = null
    return mapNotNull { state ->
        coroutineScope?.cancel()
        coroutineScope = CoroutineScope(Dispatchers.Default)
        context(coroutineScope) { svgElement { CelestialClock(state, selected, onEvent) } }
    }
}
