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

package dev.jamesyox.kastro.demo.svgk.js

import dev.jamesyox.svg4kt.attr.AttributeConsumer
import dev.jamesyox.svg4kt.createSvg
import dev.jamesyox.svg4kt.tags.Svg
import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement
import org.w3c.dom.svg.SVGElement

fun TagConsumer<HTMLElement>.svg(
    block: context(dev.jamesyox.svg4kt.TagConsumer<SVGElement>, Svg, AttributeConsumer) () -> Unit
): SVGElement {
    val svg = createSvg { block() }
    val hackDiv = div { }
    val currentNode = hackDiv.parentNode
    currentNode?.removeChild(hackDiv)
    currentNode?.appendChild(svg)
    return svg
}
