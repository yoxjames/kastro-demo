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
import dev.jamesyox.kastro.demo.insertingBefore
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.attrs.d
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.stroke
import dev.jamesyox.svg4kt.attr.attrs.strokeWidth
import dev.jamesyox.svg4kt.attr.types.obj.SvgColor
import dev.jamesyox.svg4kt.attr.types.obj.SvgPaint
import dev.jamesyox.svg4kt.attr.types.obj.none
import dev.jamesyox.svg4kt.createSvgFragment
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.path
import dev.jamesyox.svg4kt.util.L
import dev.jamesyox.svg4kt.util.M
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

context(
    coroutineScope: CoroutineScope,
    tagConsumer: TagConsumer<SVGElement>,
    _: ElementContainer.Path,
    _: ElementContainer.Svg,
    _: ElementContainer.G
)
fun EventSpanner(
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
        content: context (TagConsumer<SVGElement>, ElementContainer.Svg) () -> SVGElement
    ) {
        ClockEventGlyph(
            distance = outerRadius + 3,
            time = time,
            timeRange = timeRange,
            content = { content() }
        )
    }
    (solarEvents.map { KastroEvent.Solar(it) } + lunarEvents.map { KastroEvent.Lunar(it) })
        .sortedBy { it.time }
        .filter { it.shouldDraw }
        .forEach { event ->
            EventHash(
                time = event.time,
                outerRadius = outerRadius,
                innerRadius = innerRadius,
                outlineWidth = 0.3,
                color = SvgColor.White,
                timeRange = timeRange
            )
            when (event) {
                is KastroEvent.Lunar -> when (val lunarEvent = event.event) {
                    is LunarEvent.HorizonEvent.Moonrise -> ClockEventGlyph(time = lunarEvent.time) {
                        RiseGlyph(SvgColor.White)
                    }
                    is LunarEvent.HorizonEvent.Moonset -> ClockEventGlyph(time = lunarEvent.time) {
                        SetGlyph(SvgColor.White)
                    }
                    is LunarEvent.PhaseEvent.FirstQuarter -> ClockEventGlyph(time = lunarEvent.time) {
                        Moon(LunarEvent.PhaseEvent.FirstQuarter.phase)
                    }
                    is LunarEvent.PhaseEvent.FullMoon -> ClockEventGlyph(time = lunarEvent.time) {
                        Moon(LunarEvent.PhaseEvent.FullMoon.phase)
                    }
                    is LunarEvent.PhaseEvent.LastQuarter -> ClockEventGlyph(time = lunarEvent.time) {
                        Moon(LunarEvent.PhaseEvent.LastQuarter.phase)
                    }
                    is LunarEvent.PhaseEvent.NewMoon -> ClockEventGlyph(time = lunarEvent.time) {
                        Moon(LunarEvent.PhaseEvent.NewMoon.phase)
                    }
                }
                is KastroEvent.Solar -> when (val solarEvent = event.event) {
                    is SolarEvent.Sunrise -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time,
                        timeRange = timeRange,
                    ) {
                        RiseGlyph(SvgColor.Yellow)
                    }
                    is SolarEvent.Sunset -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time,
                        timeRange = timeRange,
                    ) {
                        SetGlyph(SvgColor.Yellow)
                    }
                    is SolarEvent.Nadir -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time,
                        timeRange = timeRange,
                    ) {
                        NadirGlyph()
                    }
                    is SolarEvent.Noon -> ClockEventGlyph(
                        distance = outerRadius + 3,
                        time = solarEvent.time,
                        timeRange = timeRange,
                    ) {
                        NoonGlyph()
                    }
                    else -> Unit
                }
            }
        }
    // This needs to happen after the DOM stuff is attached. Manual dom manipulation. Gotta love it...
    coroutineScope.launch {
        selectedEvent.map {
            createSvgFragment {
                it?.let { selected ->
                    EventHash(
                        time = selected.time,
                        outerRadius = outerRadius,
                        innerRadius = selectedInnerRadius,
                        outlineWidth = 0.5,
                        color = SvgColor.Red,
                        timeRange = timeRange
                    )
                }
            }
        }.insertingBefore(tagConsumer.output().parentElement!!, null)
    }
}

private val Selected.SelectedEvent.time get() = when (this) {
    is Selected.SelectedEvent.Lunar -> lunarEvent.time
    is Selected.SelectedEvent.Solar -> solarEvent.time
}

context(_: TagConsumer<SVGElement>, _: ElementContainer.Path)
private fun EventHash(
    time: Instant,
    outerRadius: Double,
    innerRadius: Double,
    outlineWidth: Double,
    color: SvgColor,
    timeRange: ClosedRange<Instant>,
): SVGElement {
    val angularPosition = timeRange.computeAngle(time)
    val innerPosition = SvgCoords.Polar(
        distance = innerRadius,
        theta = angularPosition
    )
    val outerPosition = SvgCoords.Polar(outerRadius, angularPosition)
    return path {
        stroke(color)
        strokeWidth = outlineWidth.none
        fill = SvgPaint.None
        d {
            M(innerPosition.x, innerPosition.y)
            L(outerPosition.x, outerPosition.y)
        }
    }
}
