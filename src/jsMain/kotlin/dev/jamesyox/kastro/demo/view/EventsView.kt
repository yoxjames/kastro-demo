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
import dev.jamesyox.kastro.demo.table.EventRowEvent
import dev.jamesyox.kastro.demo.table.EventRows
import dev.jamesyox.kastro.luna.LunarEvent
import dev.jamesyox.kastro.sol.SolarEvent
import dev.jamesyox.statik.css
import kotlinx.css.BorderCollapse
import kotlinx.css.borderCollapse
import kotlinx.css.height
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.datetime.TimeZone
import kotlinx.html.table
import kotlinx.html.th
import kotlinx.html.tr

fun EventsView(
    stylesheet: KastroDemoStylesheet,
    solarEvents: List<SolarEvent>,
    lunarEvents: List<LunarEvent>,
    timeZone: TimeZone,
    onEvent: (EventsViewEvent) -> Unit
) = htmlContent {
    table {
        css {
            width = 100.pct
            borderCollapse = BorderCollapse.collapse
        }
        tr {
            css {
                height = 50.px
            }
            th { +"Event" }
            th { +"Time" }
            th { +"Description" }
        }
        EventRows(
            stylesheet = stylesheet,
            timeZone = timeZone,
            lunarEvents = lunarEvents,
            solarEvents = solarEvents,
        ) {
            onEvent(EventsViewEvent.EventRow(it))
        }
    }.apply {
        onmouseleave = { onEvent(EventsViewEvent.OnMouseLeave) }
    }
}

sealed interface EventsViewEvent {
    value class EventRow(
        val event: EventRowEvent
    ) : EventsViewEvent

    data object OnMouseLeave : EventsViewEvent
}
