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
import dev.jamesyox.kastro.demo.prettyString
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.SolarPhaseState
import dev.jamesyox.kastro.demo.sol.prettyString
import dev.jamesyox.kastro.demo.sol.stroke
import dev.jamesyox.kastro.sol.LightState
import dev.jamesyox.kastro.sol.SolarPhase
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

fun TagConsumer<HTMLElement>.SolarStateRows(
    stylesheet: KastroDemoStylesheet,
    timeZone: TimeZone,
    solarPhases: List<SolarPhaseState>,
    lightStates: List<SolarLightState>,
    onEvent: (SolarPhaseRowEvent) -> Unit
) {
    (solarPhases.map { SolarRowType.PhaseRow(it) } + lightStates.map { SolarRowType.LightRow(it) })
        .sortedBy { it.time }
        .forEach { row ->
            tr {
                classSelector(stylesheet.stateTableRow)
                css {
                    backgroundColor = Color(row.stroke)
                    color = if (row.isBlackText) Color.black else Color.white
                }
                TableCell(row.prettyString)
                StartCell(timeZone, row)
                // End
                EndCell(timeZone, row)
                // Duration
                val duration = (row.end - row.start).inWholeSeconds.seconds
                TableCell(duration.toString())
                onClickFunction = {
                    when (row) {
                        is SolarRowType.LightRow -> onEvent(SolarPhaseRowEvent.Light.Select(row.solarLightState))
                        is SolarRowType.PhaseRow -> onEvent(SolarPhaseRowEvent.Phase.Select(row.solarPhaseState))
                    }
                }
                onMouseOverFunction = {
                    when (row) {
                        is SolarRowType.LightRow -> onEvent(SolarPhaseRowEvent.Light.Hover(row.solarLightState))
                        is SolarRowType.PhaseRow -> onEvent(SolarPhaseRowEvent.Phase.Hover(row.solarPhaseState))
                    }
                }
            }
        }
}

private fun TagConsumer<HTMLElement>.StartCell(timeZone: TimeZone, row: SolarRowType) {
    when (row) {
        is SolarRowType.LightRow -> {
            when (row.solarLightState.startEvent == null) {
                true -> TableCell("")
                false -> TableCell(row.start.toLocalDateTime(timeZone).time.prettyString())
            }
        }
        is SolarRowType.PhaseRow -> {
            when (row.solarPhaseState.startEvent == null) {
                true -> TableCell("")
                false -> TableCell(row.solarPhaseState.start.toLocalDateTime(timeZone).time.prettyString())
            }
        }
    }
}
sealed interface SolarPhaseRowEvent {
    sealed interface Phase : SolarPhaseRowEvent {
        val phase: SolarPhaseState

        value class Hover(
            override val phase: SolarPhaseState
        ) : Phase

        value class Select(
            override val phase: SolarPhaseState
        ) : Phase
    }

    sealed interface Light : SolarPhaseRowEvent {
        val lightState: SolarLightState

        value class Hover(
            override val lightState: SolarLightState
        ) : Light

        value class Select(
            override val lightState: SolarLightState
        ) : Light
    }
}

private sealed interface SolarRowType {
    value class LightRow(
        val solarLightState: SolarLightState
    ) : SolarRowType

    value class PhaseRow(
        val solarPhaseState: SolarPhaseState
    ) : SolarRowType
}

private val SolarRowType.time get() = when (this) {
    is SolarRowType.LightRow -> solarLightState.start
    is SolarRowType.PhaseRow -> solarPhaseState.start
}

private val SolarRowType.stroke get() = when (this) {
    is SolarRowType.LightRow -> solarLightState.lightState.stroke
    is SolarRowType.PhaseRow -> solarPhaseState.phase.stroke
}

private val blackTextPhases = listOf(SolarPhase.Day, SolarPhase.CivilDusk, SolarPhase.CivilDawn)
private val blackTextLightStates = listOf(LightState.GoldenHourDawn, LightState.GoldenHourDusk)

private val SolarRowType.isBlackText get() = when (this) {
    is SolarRowType.LightRow -> blackTextLightStates.contains(solarLightState.lightState)
    is SolarRowType.PhaseRow -> blackTextPhases.contains(solarPhaseState.phase)
}

private val SolarRowType.prettyString get() = when (this) {
    is SolarRowType.LightRow -> solarLightState.lightState.prettyString
    is SolarRowType.PhaseRow -> solarPhaseState.phase.prettyString
}

private val SolarRowType.start get() = when (this) {
    is SolarRowType.LightRow -> solarLightState.start
    is SolarRowType.PhaseRow -> solarPhaseState.start
}

private val SolarRowType.end get() = when (this) {
    is SolarRowType.LightRow -> solarLightState.end
    is SolarRowType.PhaseRow -> solarPhaseState.end
}

private fun TagConsumer<HTMLElement>.EndCell(timeZone: TimeZone, row: SolarRowType) {
    when (row) {
        is SolarRowType.LightRow -> {
            when (row.solarLightState.endEvent == null) {
                true -> TableCell("")
                false -> TableCell(row.end.toLocalDateTime(timeZone).time.prettyString())
            }
        }
        is SolarRowType.PhaseRow -> {
            when (row.solarPhaseState.endEvent == null) {
                true -> TableCell("")
                false -> TableCell(row.solarPhaseState.end.toLocalDateTime(timeZone).time.prettyString())
            }
        }
    }
}
