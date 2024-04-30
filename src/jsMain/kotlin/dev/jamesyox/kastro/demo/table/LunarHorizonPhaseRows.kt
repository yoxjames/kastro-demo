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

import dev.jamesyox.kastro.common.HorizonState
import dev.jamesyox.kastro.demo.KastroDemoStylesheet
import dev.jamesyox.kastro.demo.clock.stroke
import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.luna.lunarPrettyString
import dev.jamesyox.kastro.demo.prettyString
import dev.jamesyox.statik.classSelector
import dev.jamesyox.statik.css
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import kotlinx.css.color
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.html.TagConsumer
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOverFunction
import kotlinx.html.tr
import org.w3c.dom.HTMLElement
import kotlin.time.Duration.Companion.seconds

fun TagConsumer<HTMLElement>.LunarHorizonPhaseRows(
    stylesheet: KastroDemoStylesheet,
    timeZone: TimeZone,
    lunarHorizonStates: List<LunarHorizonState>,
    onEvent: (LunarPhaseRowEvent) -> Unit
) {
    val blackTextPhases = listOf(HorizonState.Up)
    lunarHorizonStates.forEach { lunarHorizonState ->
        tr {
            classSelector(stylesheet.stateTableRow)
            css {
                backgroundColor = Color(lunarHorizonState.horizonState.stroke)
                color = if (blackTextPhases.contains(lunarHorizonState.horizonState)) Color.black else Color.white
            }
            TableCell(lunarHorizonState.horizonState.lunarPrettyString)
            StartCell(timeZone, lunarHorizonState)
            EndCell(timeZone, lunarHorizonState)
            DurationCell(lunarHorizonState)
            onClickFunction = { onEvent(LunarPhaseRowEvent.Select(lunarHorizonState)) }
            onMouseOverFunction = { onEvent(LunarPhaseRowEvent.Hover(lunarHorizonState)) }
        }
    }
}

sealed interface LunarPhaseRowEvent {
    val lunarHorizonState: LunarHorizonState

    value class Hover(
        override val lunarHorizonState: LunarHorizonState
    ) : LunarPhaseRowEvent

    value class Select(
        override val lunarHorizonState: LunarHorizonState
    ) : LunarPhaseRowEvent
}

private fun TagConsumer<HTMLElement>.StartCell(timeZone: TimeZone, lunarHorizonState: LunarHorizonState) {
    if (lunarHorizonState.startEvent != null) {
        TableCell(lunarHorizonState.start.toLocalDateTime(timeZone).time.prettyString())
    } else {
        TableCell("")
    }
}

private fun TagConsumer<HTMLElement>.EndCell(timeZone: TimeZone, lunarHorizonState: LunarHorizonState) {
    if (lunarHorizonState.endEvent != null) {
        TableCell(lunarHorizonState.end.toLocalDateTime(timeZone).time.prettyString())
    } else {
        TableCell("")
    }
}

private fun TagConsumer<HTMLElement>.DurationCell(lunarHorizonState: LunarHorizonState) {
    val duration = (lunarHorizonState.end - lunarHorizonState.start).inWholeSeconds.seconds
    TableCell(duration.toString())
}
