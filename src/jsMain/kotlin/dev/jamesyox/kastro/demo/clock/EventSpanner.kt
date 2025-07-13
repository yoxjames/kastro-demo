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

import dev.jamesyox.kastro.demo.clock.events.ClockEventGlyph
import dev.jamesyox.kastro.demo.clock.events.NadirGlyph
import dev.jamesyox.kastro.demo.clock.events.NoonGlyph
import dev.jamesyox.kastro.demo.clock.events.RiseGlyph
import dev.jamesyox.kastro.demo.clock.events.SetGlyph
import dev.jamesyox.kastro.demo.computeAngle
import dev.jamesyox.kastro.demo.domCoroutineScope
import dev.jamesyox.kastro.demo.insertingBefore
import dev.jamesyox.kastro.demo.svgContent
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.svgk.TagConsumer
import dev.jamesyox.svgk.tags.path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.toStdlibInstant
import org.w3c.dom.svg.SVGElement
import kotlin.time.Instant

private sealed interface KastroEvent {
    value class Solar(
        val event: SolarEvent
    ) : KastroEvent

    value class Lunar(
        val event: LunarEvent
    ) : KastroEvent
}

private val KastroEvent.time get() = when (this) {
    is KastroEvent.Lunar -> event.time
    is KastroEvent.Solar -> event.time
}

private val KastroEvent.shouldDraw: Boolean get() {
    val solarEventsToDraw = listOf(
        SolarEvent.Nadir::class,
        SolarEvent.Noon::class,
        SolarEvent.Sunset::class,
        SolarEvent.Sunrise::class
    )
    return when (this) {
        is KastroEvent.Lunar -> true
        is KastroEvent.Solar -> solarEventsToDraw.contains(event::class)
    }
}

fun TagConsumer<SVGElement>.EventSpanner(
    timeRange: ClosedRange<Instant>,
    outerRadius: Double,
    innerRadius: Double,
    selectedInnerRadius: Double,
    solarEvents: List<SolarEvent>,
    lunarEvents: List<LunarEvent>,
    selectedEvent: Flow<Selected.SelectedEvent?>,
) {
    fun ClockEventGlyph(
        time: Instant,
        content: TagConsumer<SVGElement>.() -> SVGElement
    ) {
        ClockEventGlyph(
            distance = outerRadius + 3,
            time = time,
            timeRange = timeRange,
            content = content
        )
    }
    (solarEvents.map { KastroEvent.Solar(it) } + lunarEvents.map { KastroEvent.Lunar(it) })
        .sortedBy { it.time }
        .filter { it.shouldDraw }
        .forEach { event ->
            EventHash(
                time = event.time.toStdlibInstant(),
                outerRadius = outerRadius,
                innerRadius = innerRadius,
                timeRange = timeRange,
                strokeWidth = 0.3,
                color = "white"
            )
            when (event) {
                is KastroEvent.Lunar -> when (val lunarEvent = event.event) {
                    is LunarEvent.HorizonEvent.Moonrise -> ClockEventGlyph(time = lunarEvent.time.toStdlibInstant()) {
                        RiseGlyph("white")
                    }
                    is LunarEvent.HorizonEvent.Moonset -> ClockEventGlyph(time = lunarEvent.time.toStdlibInstant()) {
                        SetGlyph("white")
                    }
                    is LunarEvent.PhaseEvent.FirstQuarter -> ClockEventGlyph(time = lunarEvent.time.toStdlibInstant()) {
                        Moon(LunarEvent.PhaseEvent.FirstQuarter.phase)
                    }
                    is LunarEvent.PhaseEvent.FullMoon -> ClockEventGlyph(time = lunarEvent.time.toStdlibInstant()) {
                        Moon(LunarEvent.PhaseEvent.FullMoon.phase)
                    }
                    is LunarEvent.PhaseEvent.LastQuarter -> ClockEventGlyph(time = lunarEvent.time.toStdlibInstant()) {
                        Moon(LunarEvent.PhaseEvent.LastQuarter.phase)
                    }
                    is LunarEvent.PhaseEvent.NewMoon -> ClockEventGlyph(time = lunarEvent.time.toStdlibInstant()) {
                        Moon(LunarEvent.PhaseEvent.NewMoon.phase)
                    }
                }
                is KastroEvent.Solar -> when (val solarEvent = event.event) {
                    is SolarEvent.Sunrise -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time.toStdlibInstant(),
                        timeRange = timeRange,
                    ) {
                        RiseGlyph("yellow")
                    }
                    is SolarEvent.Sunset -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time.toStdlibInstant(),
                        timeRange = timeRange,
                    ) {
                        SetGlyph("yellow")
                    }
                    is SolarEvent.Nadir -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time.toStdlibInstant(),
                        timeRange = timeRange,
                    ) {
                        NadirGlyph()
                    }
                    is SolarEvent.Noon -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time.toStdlibInstant(),
                        timeRange = timeRange,
                    ) {
                        NoonGlyph()
                    }
                    else -> Unit
                }
            }
        }

    context(last!!.domCoroutineScope()) {
        // This needs to happen after the DOM stuff is attached. Manual dom manipulation. Gotta love it...
        contextOf<CoroutineScope>().launch {
            selectedEvent.map {
                svgContent {
                    it?.let { selected ->
                        this@svgContent.EventHash(
                            time = selected.time.toStdlibInstant(),
                            outerRadius = outerRadius,
                            innerRadius = selectedInnerRadius,
                            timeRange = timeRange,
                            strokeWidth = 0.5,
                            color = "red"
                        )
                    }
                }
            }.insertingBefore(last!!.parentElement!!, null)
        }
    }
}

private val Selected.SelectedEvent.time get() = when (this) {
    is Selected.SelectedEvent.Lunar -> lunarEvent.time
    is Selected.SelectedEvent.Solar -> solarEvent.time
}

private fun TagConsumer<SVGElement>.EventHash(
    time: Instant,
    outerRadius: Double,
    innerRadius: Double,
    strokeWidth: Double,
    color: String,
    timeRange: ClosedRange<Instant>,
): SVGElement {
    val angularPosition = timeRange.computeAngle(time)
    val innerPosition = SvgCoords.Polar(
        distance = innerRadius,
        theta = angularPosition
    )
    val outerPosition = SvgCoords.Polar(outerRadius, angularPosition)
    return path(
        stroke = color,
        strokeWidth = strokeWidth,
        fill = "none",
    ) {
        M(innerPosition.x, innerPosition.y)
        L(outerPosition.x, outerPosition.y)
    }
}
