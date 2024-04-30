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

package dev.jamesyox.kastro.demo.view

import dev.jamesyox.kastro.demo.KastroDemoStylesheet
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.SolarPhaseState
import dev.jamesyox.kastro.demo.table.LunarHorizonPhaseRows
import dev.jamesyox.kastro.demo.table.LunarPhaseRowEvent
import dev.jamesyox.kastro.demo.table.SolarPhaseRowEvent
import dev.jamesyox.kastro.demo.table.SolarStateRows
import dev.jamesyox.statik.classSelector
import dev.jamesyox.statik.css
import kotlinx.css.height
import kotlinx.css.px
import kotlinx.datetime.TimeZone
import kotlinx.html.js.onMouseLeaveFunction
import kotlinx.html.js.table
import kotlinx.html.th
import kotlinx.html.tr

fun StatesView(
    stylesheet: KastroDemoStylesheet,
    solarTableState: List<SolarPhaseState>,
    lunarTableState: List<LunarHorizonState>,
    lightTableState: List<SolarLightState>,
    timeZone: TimeZone,
    onEvent: (StateViewEvent) -> Unit
) = htmlContent {
    // TableSeparator("Sun")
    table {
        classSelector(stylesheet.stateTable)
        tr {
            css {
                height = 50.px
            }
            th { +"State" }
            th { +"Start" }
            th { +"End" }
            th { +"Duration" }
        }
        SolarStateRows(
            stylesheet = stylesheet,
            timeZone = timeZone,
            solarPhases = solarTableState,
            lightStates = lightTableState,
        ) {
            onEvent(StateViewEvent.Solar(it))
        }

        // TableSeparator("Moon")
        LunarHorizonPhaseRows(
            stylesheet = stylesheet,
            timeZone = timeZone,
            lunarHorizonStates = lunarTableState,
        ) {
            onEvent(StateViewEvent.Lunar(it))
        }
        onMouseLeaveFunction = { onEvent(StateViewEvent.OnMouseLeave) }
    }
}

sealed interface StateViewEvent {
    value class Solar(
        val event: SolarPhaseRowEvent
    ) : StateViewEvent

    value class Lunar(
        val event: LunarPhaseRowEvent
    ) : StateViewEvent

    data object OnMouseLeave : StateViewEvent
}
