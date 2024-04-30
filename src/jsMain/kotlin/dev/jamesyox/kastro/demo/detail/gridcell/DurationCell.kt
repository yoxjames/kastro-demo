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

import kotlinx.html.TagConsumer
import kotlinx.html.br
import kotlinx.html.h3
import kotlinx.html.p
import org.w3c.dom.HTMLElement
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

fun TagConsumer<HTMLElement>.DurationCell(duration: Duration) {
    GridCell {
        h3 { +"Occurs For" }
        p {
            +"$duration"
            br
            val percent = ((duration / 1.days) * 10000).roundToInt() / 100.0
            +"Represents $percent% of the 24 hour day"
        }
    }
}
