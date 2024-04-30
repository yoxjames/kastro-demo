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

package dev.jamesyox.kastro.demo.table

import dev.jamesyox.statik.css
import kotlinx.css.Border
import kotlinx.css.Color
import kotlinx.css.border
import kotlinx.css.color
import kotlinx.html.TagConsumer
import kotlinx.html.js.td
import org.w3c.dom.HTMLElement

internal fun TagConsumer<HTMLElement>.TableCell(content: String) {
    td {
        css {
            color = Color.inherit
            border = Border.none
        }
        +content
    }
}
