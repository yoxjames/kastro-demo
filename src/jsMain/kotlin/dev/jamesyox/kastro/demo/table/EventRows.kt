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

package dev.jamesyox.kastro.demo.table

import dev.jamesyox.kastro.demo.KastroDemoStylesheet
import dev.jamesyox.kastro.demo.clock.Moon
import dev.jamesyox.kastro.demo.clock.events.EventGlyph
import dev.jamesyox.kastro.demo.clock.events.NadirGlyph
import dev.jamesyox.kastro.demo.clock.events.NoonGlyph
import dev.jamesyox.kastro.demo.clock.events.RiseGlyph
import dev.jamesyox.kastro.demo.clock.events.SetGlyph
import dev.jamesyox.kastro.demo.luna.prettyString
import dev.jamesyox.kastro.demo.luna.shortDescription
import dev.jamesyox.kastro.demo.prettyString
import dev.jamesyox.kastro.demo.sol.prettyString
import dev.jamesyox.kastro.demo.sol.shortDescription
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.statik.classSelector
import dev.jamesyox.statik.css
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignContent
import kotlinx.css.alignItems
import kotlinx.css.alignSelf
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOverFunction
import kotlinx.html.js.td
import kotlinx.html.js.tr
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.EventRows(
    stylesheet: KastroDemoStylesheet,
    timeZone: TimeZone,
    lunarEvents: List<LunarEvent>,
    solarEvents: List<SolarEvent>,
    onEvent: (EventRowEvent) -> Unit
) {
    (solarEvents.map { EventRowType.Solar(it) } + lunarEvents.map { EventRowType.Lunar(it) })
        .sortedBy { it.time }
        .forEach { row ->
            tr {
                classSelector(stylesheet.eventTableRow)
                EventCell(row)
                TableCell(row.time.toLocalDateTime(timeZone).time.prettyString())
                TableCell(row.shortDescription)
                onClickFunction = {
                    when (row) {
                        is EventRowType.Lunar -> onEvent(EventRowEvent.Lunar.Select(row.event))
                        is EventRowType.Solar -> onEvent(EventRowEvent.Solar.Select(row.event))
                    }
                }
                onMouseOverFunction = {
                    when (row) {
                        is EventRowType.Lunar -> onEvent(EventRowEvent.Lunar.Hover(row.event))
                        is EventRowType.Solar -> onEvent(EventRowEvent.Solar.Hover(row.event))
                    }
                }
            }
        }
}

sealed interface EventRowEvent {
    sealed interface Lunar : EventRowEvent {
        val lunarEvent: LunarEvent
        value class Hover(
            override val lunarEvent: LunarEvent
        ) : Lunar

        value class Select(
            override val lunarEvent: LunarEvent
        ) : Lunar
    }
    sealed interface Solar : EventRowEvent {
        val solarEvent: SolarEvent
        value class Hover(
            override val solarEvent: SolarEvent
        ) : Solar

        value class Select(
            override val solarEvent: SolarEvent
        ) : Solar
    }
}

private sealed interface EventRowType {
    value class Solar(
        val event: SolarEvent
    ) : EventRowType

    value class Lunar(
        val event: LunarEvent
    ) : EventRowType
}

private val EventRowType.time get() = when (this) {
    is EventRowType.Lunar -> event.time
    is EventRowType.Solar -> event.time
}

private val EventRowType.prettyString get() = when (this) {
    is EventRowType.Lunar -> event.prettyString
    is EventRowType.Solar -> event.prettyString
}

private val EventRowType.shortDescription get() = when (this) {
    is EventRowType.Lunar -> event.shortDescription
    is EventRowType.Solar -> event.shortDescription
}

private fun TagConsumer<HTMLElement>.EventCell(content: EventRowType) {
    td {
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                alignItems = Align.center
                alignContent = Align.center
                alignSelf = Align.center
            }
            when (content) {
                is EventRowType.Lunar -> when (content.event) {
                    is LunarEvent.HorizonEvent.Moonrise -> EventGlyph { RiseGlyph("white") }
                    is LunarEvent.HorizonEvent.Moonset -> EventGlyph { SetGlyph("white") }
                    is LunarEvent.PhaseEvent.FirstQuarter -> EventGlyph {
                        Moon(
                            LunarEvent.PhaseEvent.FirstQuarter.phase
                        )
                    }
                    is LunarEvent.PhaseEvent.FullMoon -> EventGlyph { Moon(LunarEvent.PhaseEvent.FullMoon.phase) }
                    is LunarEvent.PhaseEvent.LastQuarter -> EventGlyph { Moon(LunarEvent.PhaseEvent.LastQuarter.phase) }
                    is LunarEvent.PhaseEvent.NewMoon -> EventGlyph { Moon(LunarEvent.PhaseEvent.NewMoon.phase) }
                }

                is EventRowType.Solar -> when (content.event) {
                    is SolarEvent.Sunrise -> EventGlyph { RiseGlyph("yellow") }
                    is SolarEvent.Sunset -> EventGlyph { SetGlyph("yellow") }
                    is SolarEvent.Noon -> EventGlyph { NoonGlyph() }
                    is SolarEvent.Nadir -> EventGlyph { NadirGlyph() }
                    else -> Unit
                }
            }
            +content.prettyString
        }
    }
}
