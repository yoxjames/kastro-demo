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

import dev.jamesyox.kastro.demo.GlobalState
import dev.jamesyox.kastro.demo.detail.gridcell.LunarStateCell
import dev.jamesyox.kastro.demo.detail.gridcell.SolarStateCell
import dev.jamesyox.kastro.demo.htmlContent
import dev.jamesyox.statik.css
import kotlinx.coroutines.CoroutineScope
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.GridTemplateColumns
import kotlinx.css.Padding
import kotlinx.css.backgroundColor
import kotlinx.css.columnGap
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexWrap
import kotlinx.css.gridTemplateColumns
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.css.rowGap
import kotlinx.html.js.div

fun CurrentView(
    coroutineScope: CoroutineScope,
    globalState: GlobalState,
) = htmlContent {
    div {
        css {
            padding = Padding(16.px)
            backgroundColor = Color("#171718")
            display = Display.flex
            flexDirection = FlexDirection.row
            flexWrap = FlexWrap.wrap
            gridTemplateColumns = GridTemplateColumns.repeat("2, minmax(0, 1fr)")
            columnGap = 12.px
            rowGap = 12.px
        }
        SolarStateCell(coroutineScope, globalState)
        LunarStateCell(coroutineScope, globalState)
    }
}