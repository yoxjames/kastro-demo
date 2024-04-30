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
import dev.jamesyox.kastro.demo.detail.gridcell.TimeCell
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.kastro.demo.luna.prettyString
import dev.jamesyox.kastro.demo.luna.shortDescription
import dev.jamesyox.kastro.luna.LunarEvent
import kotlinx.datetime.TimeZone

fun LunarHorizonEventDetailView(
    event: LunarEvent,
    timeZone: TimeZone,
    onEvent: (LunarHorizonEventDetailViewEvent) -> Unit
) = htmlContent {
    DetailView(
        title = event.prettyString,
        description = event.shortDescription,
        onBack = { onEvent(LunarHorizonEventDetailViewEvent.BackClicked) }
    ) {
        TimeCell(
            timeZone = timeZone,
            event.time
        )
        ApiCell(event)
    }
}

sealed interface LunarHorizonEventDetailViewEvent {
    data object BackClicked : LunarHorizonEventDetailViewEvent
}
