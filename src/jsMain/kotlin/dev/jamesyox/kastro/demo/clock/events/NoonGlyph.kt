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

package dev.jamesyox.kastro.demo.clock.events

import dev.jamesyox.kastro.demo.sol.stroke
import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.attrs.ViewBox
import dev.jamesyox.svg4kt.attr.attrs.d
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.viewBox
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.path
import dev.jamesyox.svg4kt.tags.svg
import dev.jamesyox.svg4kt.util.L
import dev.jamesyox.svg4kt.util.M
import dev.jamesyox.svg4kt.util.Z
import org.w3c.dom.svg.SVGElement

context(_: TagConsumer<SVGElement>, _: ElementContainer.Svg)
fun NoonGlyph(): SVGElement {
    return svg {
        viewBox = ViewBox(minX = -3, minY = -3, width = 6, height = 6)
        path {
            fill(SolarPhase.Day.stroke)
            d {
                M(-3, 3)
                L(0, -3)
                L(3, 3)
                Z
            }
        }
    }
}
