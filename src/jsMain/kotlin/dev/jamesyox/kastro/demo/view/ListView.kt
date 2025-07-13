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
import dev.jamesyox.kastro.demo.misc.KastroDemoTabs
import dev.jamesyox.kastro.demo.mountTo
import dev.jamesyox.statik.css
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.Padding
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexWrap
import kotlinx.css.minWidth
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.html.js.div

context(_: CoroutineScope)
fun ListView(
    stylesheet: KastroDemoStylesheet,
    tableType: StateFlow<TableType>,
    kastroDemoTabs: KastroDemoTabs,
    onEvent: (ListViewEvent) -> Unit
) = htmlContent {
    // Table Container
    div {
        css {
            minWidth = 100.pct
        }
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                flexWrap = FlexWrap.wrap
            }
            TabView(
                stylesheet = stylesheet,
                selectedTab = tableType,
            ) {
                onEvent(ListViewEvent.SelectTab(it))
            }
        }
        val tabContent = div {
            css {
                display = Display.block
                padding = Padding(left = 12.px, right = 12.px, bottom = 12.px)
                backgroundColor = Color("#171718")
            }
        }
        tableType.map {
            when (it) {
                TableType.States -> kastroDemoTabs.statesTab
                TableType.Events -> kastroDemoTabs.eventsTab
                TableType.Current -> kastroDemoTabs.currentTab
                TableType.About -> kastroDemoTabs.aboutTab
            }
        }.mountTo(tabContent)
    }
}

sealed interface ListViewEvent {
    value class StatesView(
        val event: StateViewEvent
    ) : ListViewEvent

    value class EventsView(
        val event: EventsViewEvent
    ) : ListViewEvent

    value class SelectTab(
        val tab: TableType
    ) : ListViewEvent
}
