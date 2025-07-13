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

package dev.jamesyox.kastro.demo.detail

import dev.jamesyox.kastro.demo.detail.gridcell.ApiCell
import dev.jamesyox.kastro.demo.detail.gridcell.DurationCell
import dev.jamesyox.kastro.demo.detail.gridcell.EndsCell
import dev.jamesyox.kastro.demo.detail.gridcell.LightStateCell
import dev.jamesyox.kastro.demo.detail.gridcell.StartsCell
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.sol.SolarLightState
import dev.jamesyox.kastro.demo.sol.prettyString
import kotlinx.datetime.TimeZone

sealed interface LightStateDetailViewEvent {
    data object BackClicked : LightStateDetailViewEvent
}

context(_: TimeZone)
fun LightStateDetailView(
    description: String,
    solarLightState: SolarLightState,
    onEvent: (LightStateDetailViewEvent) -> Unit
) = htmlContent {
    DetailView(
        title = solarLightState.lightState.prettyString,
        description = description,
        onBack = { onEvent(LightStateDetailViewEvent.BackClicked) }
    ) {
        val startEvent = solarLightState.startEvent?.let { it::class.simpleName }
        if (startEvent != null) {
            StartsCell(time = solarLightState.start, startEventName = startEvent)
        }
        val endEvent = solarLightState.endEvent?.let { it::class.simpleName }
        if (endEvent != null) {
            EndsCell(time = solarLightState.end, endEventName = endEvent)
        }
        if (startEvent != null && endEvent != null) {
            DurationCell(solarLightState.end - solarLightState.start)
        }
        LightStateCell(solarLightState.lightState)
        ApiCell(solarLightState.lightState)
    }
}
