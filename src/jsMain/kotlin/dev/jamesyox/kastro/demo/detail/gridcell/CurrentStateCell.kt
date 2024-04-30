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

package dev.jamesyox.kastro.demo.detail.gridcell

import dev.jamesyox.statik.css
import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.Padding
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.px
import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.CurrentStateCell(): HTMLElement {
    return div {
        css {
            display = Display.flex
            padding = Padding(12.px)
            backgroundColor = Color("#313131")
            borderRadius = 8.px
            maxWidth = 800.px
            alignItems = Align.center
            flexDirection = FlexDirection.column
        }
    }
}
