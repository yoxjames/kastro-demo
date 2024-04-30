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
import dev.jamesyox.kastro.demo.detail.gridcell.StartsCell
import dev.jamesyox.kastro.demo.detail.gridcell.TwilightCell
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.sol.SolarPhaseState
import dev.jamesyox.kastro.demo.sol.prettyString
import dev.jamesyox.kastro.sol.Twilight
import kotlinx.datetime.TimeZone

fun SolarStateDetailView(
    description: String,
    solarPhaseState: SolarPhaseState,
    timeZone: TimeZone,
    onEvent: (SolarStateDetailViewEvent) -> Unit
) = htmlContent {
    DetailView(
        title = solarPhaseState.phase.prettyString,
        description = description,
        onBack = { onEvent(SolarStateDetailViewEvent.BackClicked) }
    ) {
        val startEvent = solarPhaseState.startEvent?.let { it::class.simpleName }
        if (startEvent != null) {
            StartsCell(
                timeZone = timeZone,
                time = solarPhaseState.start,
                startEventName = startEvent
            )
        }
        val endEvent = solarPhaseState.endEvent?.let { it::class.simpleName }
        if (endEvent != null) {
            EndsCell(
                timeZone = timeZone,
                time = solarPhaseState.end,
                endEventName = endEvent
            )
        }
        if (startEvent != null && endEvent != null) {
            DurationCell(solarPhaseState.end - solarPhaseState.start)
        }
        if (solarPhaseState.phase is Twilight) {
            TwilightCell(solarPhaseState.phase)
        }
        ApiCell(solarPhaseState.phase)
    }
}

sealed interface SolarStateDetailViewEvent {
    data object BackClicked : SolarStateDetailViewEvent
}
