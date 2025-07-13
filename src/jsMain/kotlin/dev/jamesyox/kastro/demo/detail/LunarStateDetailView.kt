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
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.luna.LunarHorizonState
import dev.jamesyox.kastro.demo.luna.lunarDescription
import dev.jamesyox.kastro.demo.luna.lunarPrettyString
import kotlinx.datetime.TimeZone

context(_: TimeZone)
fun LunarStateDetailView(
    lunarHorizonState: LunarHorizonState,
    onEvent: (LunarStateDetailViewEvent) -> Unit
) = htmlContent {
    DetailView(
        title = lunarHorizonState.horizonState.lunarPrettyString,
        description = lunarHorizonState.horizonState.lunarDescription,
        onBack = { onEvent(LunarStateDetailViewEvent.BackClicked) },
    ) {
        val startEvent = lunarHorizonState.startEvent?.let { it::class.simpleName }
        if (startEvent != null) {
            StartsCell(time = lunarHorizonState.start, startEventName = startEvent)
        }
        val endEvent = lunarHorizonState.endEvent?.let { it::class.simpleName }
        if (endEvent != null) {
            EndsCell(time = lunarHorizonState.end, endEventName = endEvent)
        }
        if (startEvent != null && endEvent != null) {
            DurationCell(lunarHorizonState.end - lunarHorizonState.start)
        }
        ApiCell(lunarHorizonState.horizonState)
    }
}

sealed interface LunarStateDetailViewEvent {
    data object BackClicked : LunarStateDetailViewEvent
}
