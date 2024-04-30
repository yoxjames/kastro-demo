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

package dev.jamesyox.kastro.demo.detail

import dev.jamesyox.statik.css
import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.FlexWrap
import kotlinx.css.Padding
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.columnGap
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexWrap
import kotlinx.css.height
import kotlinx.css.marginLeft
import kotlinx.css.maxWidth
import kotlinx.css.padding
import kotlinx.css.paddingTop
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.rowGap
import kotlinx.css.width
import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.img
import kotlinx.html.js.onClickFunction
import kotlinx.html.p
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.DetailView(
    title: String,
    description: String,
    onBack: () -> Unit,
    content: TagConsumer<HTMLElement>.() -> Unit
) {
    div {
        css {
            padding = Padding(16.px)
            maxWidth = 100.pct
            backgroundColor = Color("#171718")
        }
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                alignItems = Align.center
            }
            button {
                img {
                    css {
                        paddingTop = 4.px
                        width = 24.px
                        height = 24.px
                    }
                    src = "back.svg"
                }
                onClickFunction = { onBack() }
            }
            h2 {
                css {
                    marginLeft = 12.px
                }
                +title
            }
        }

        p {
            css {
                paddingTop = 12.px
            }
            +description
        }
        div {
            css {
                paddingTop = 10.px
                display = Display.flex
                flexDirection = FlexDirection.row
                flexWrap = FlexWrap.wrap
                columnGap = 12.px
                rowGap = 12.px
            }
            content()
        }
    }
}
