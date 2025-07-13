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

import dev.jamesyox.kastro.demo.prettyString
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.html.TagConsumer
import kotlinx.html.br
import kotlinx.html.code
import kotlinx.html.h3
import kotlinx.html.p
import org.w3c.dom.HTMLElement
import kotlin.time.Instant

context(timeZone: TimeZone)
fun TagConsumer<HTMLElement>.StartsCell(
    time: Instant,
    startEventName: String,
) {
    GridCell {
        h3 { +"Starts" }
        p {
            +time.toLocalDateTime(timeZone).time.prettyString()
            br
            +"With: "
            code { +startEventName }
        }
    }
}
