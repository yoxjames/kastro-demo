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

import dev.jamesyox.svg4kt.TagConsumer
import dev.jamesyox.svg4kt.attr.attrs.ViewBox
import dev.jamesyox.svg4kt.attr.attrs.d
import dev.jamesyox.svg4kt.attr.attrs.fill
import dev.jamesyox.svg4kt.attr.attrs.stroke
import dev.jamesyox.svg4kt.attr.attrs.strokeWidth
import dev.jamesyox.svg4kt.attr.attrs.viewBox
import dev.jamesyox.svg4kt.attr.types.obj.SvgColor
import dev.jamesyox.svg4kt.attr.types.obj.SvgPaint
import dev.jamesyox.svg4kt.attr.types.obj.none
import dev.jamesyox.svg4kt.tags.categories.container.ElementContainer
import dev.jamesyox.svg4kt.tags.path
import dev.jamesyox.svg4kt.tags.svg
import dev.jamesyox.svg4kt.util.A
import dev.jamesyox.svg4kt.util.L
import dev.jamesyox.svg4kt.util.M
import org.w3c.dom.svg.SVGElement

context(_: TagConsumer<SVGElement>, _: ElementContainer.Svg)
fun RiseGlyph(color: SvgColor): SVGElement {
    return HorizonGlyph(color = color) {
        path {
            strokeWidth = 0.8.none
            stroke(SvgColor.Red)
            fill = SvgPaint.None
            d {
                M(0, 3)
                L(0, 1)
                M(-1, 2.0)
                L(0, 1)
                L(1, 2.0)
            }
        }
    }
}

context(_: TagConsumer<SVGElement>, _: ElementContainer.Svg)
fun SetGlyph(color: SvgColor): SVGElement {
    return HorizonGlyph(color = color) {
        path {
            strokeWidth = 0.8.none
            stroke(SvgColor.Red)
            fill = SvgPaint.None
            d {
                M(0, 0.5)
                L(0, 2.5)
                M(-1, 1.5)
                L(0, 2.5)
                L(1, 1.5)
            }
        }
    }
}

context(_: TagConsumer<SVGElement>, _: ElementContainer.Svg)
internal fun HorizonGlyph(
    color: SvgColor,
    arrowContent: context(TagConsumer<SVGElement>, ElementContainer.Path) () -> Unit,
): SVGElement {
    return svg {
        viewBox = ViewBox(minX = -3, minY = -3, width = 6, height = 6)
        path {
            fill(color)
            d {
                M(-3, 0)
                A(
                    rx = 3,
                    ry = 3,
                    angle = 0,
                    largeArcFlag = false,
                    sweepFlag = true,
                    x = 3,
                    y = 0
                )
            }
        }
        arrowContent()
    }
}
