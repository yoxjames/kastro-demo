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
import dev.jamesyox.kastro.demo.mountClass
import dev.jamesyox.statik.classSelector
import dev.jamesyox.statik.css
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.pct
import kotlinx.css.width
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.js.button
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

context(_: CoroutineScope)
fun TagConsumer<HTMLElement>.TabView(
    stylesheet: KastroDemoStylesheet,
    selectedTab: StateFlow<TableType>,
    onSelectTab: (TableType) -> Unit
) {
    div {
        css {
            width = 100.pct
            display = Display.block
        }
        TableType.entries.forEach { tab ->
            val cssClass = selectedTab.map { selectedTab ->
                if (selectedTab == tab) stylesheet.activeTab else stylesheet.inactiveTab
            }
            button {
                classSelector(stylesheet.inactiveTab)
                +tab.name
                onClickFunction = { onSelectTab(tab) }
            }.mountClass(cssClass)
        }
    }
}

enum class TableType {
    States, Events, Current, About
}
