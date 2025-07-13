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
import dev.jamesyox.kastro.demo.KastroDemoStylesheet
import dev.jamesyox.kastro.demo.clock.CelestialClockEvent
import dev.jamesyox.kastro.demo.clock.CelsetialClockComponent
import dev.jamesyox.kastro.demo.input.InputPanel
import dev.jamesyox.kastro.demo.input.InputPanelEvent
import dev.jamesyox.kastro.demo.mountClass
import dev.jamesyox.kastro.demo.settingAttribute
import dev.jamesyox.statik.classSelector
import dev.jamesyox.statik.css
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.Padding
import kotlinx.css.Position
import kotlinx.css.borderRadius
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.left
import kotlinx.css.padding
import kotlinx.css.paddingTop
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.px
import kotlinx.css.top
import kotlinx.css.width
import kotlinx.html.TagConsumer
import kotlinx.html.id
import kotlinx.html.js.button
import kotlinx.html.js.div
import kotlinx.html.js.img
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

enum class TopPanelState {
    Clock, Settings
}

sealed interface TopPanelEvent {
    data object SettingsClicked : TopPanelEvent

    value class InputPanel(
        val event: InputPanelEvent
    ) : TopPanelEvent

    value class ClockEvent(
        val event: CelestialClockEvent
    ) : TopPanelEvent
}

context(_: CoroutineScope)
fun TagConsumer<HTMLElement>.TopPanel(
    stylesheet: KastroDemoStylesheet,
    globalState: GlobalState,
    onEvent: (TopPanelEvent) -> Unit
): HTMLElement {
    val topPanelState = globalState.topPanelState
    return div {
        css {
            position = Position.relative
            display = Display.flex
            paddingTop = 4.px
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.center
            width = 100.pct
        }

        val inputPanel = InputPanel(
            parameters = globalState.paramsState.value,
            mapUpdates = globalState.nonManualUpdates,
            paramsState = globalState.paramsState
        ) {
            onEvent(TopPanelEvent.InputPanel(it))
        }
        val celestialClock = div {
            classSelector(stylesheet.inputPanelActive)
            CelsetialClockComponent(
                state = globalState.celestialClockState,
                selected = globalState.focusedItem,
            ) {
                onEvent(TopPanelEvent.ClockEvent(it))
            }
        }

        button {
            id = "settings-button"
            classSelector(stylesheet.smallScreenOnly)
            css {
                position = Position.absolute
                top = 16.px
                left = 16.px
                padding = Padding(8.px)
                borderRadius = 8.px
            }
            img {
                css {
                    paddingTop = 4.px
                    width = 24.px
                    height = 24.px
                }
                src = "settings.svg"
            }.settingAttribute(
                qualifiedName = "src",
                values = topPanelState.map { state ->
                    when (state) {
                        TopPanelState.Clock -> "settings.svg"
                        TopPanelState.Settings -> "back.svg"
                    }
                }
            )
            onClickFunction = { onEvent(TopPanelEvent.SettingsClicked) }
        }

        inputPanel.mountClass(
            topPanelState.map {
                when (it) {
                    TopPanelState.Settings -> stylesheet.inputPanelActive
                    TopPanelState.Clock -> stylesheet.inputPanelInactive
                }
            }
        )

        celestialClock.mountClass(
            topPanelState.map {
                when (it) {
                    TopPanelState.Clock -> stylesheet.inputPanelActive
                    TopPanelState.Settings -> stylesheet.inputPanelInactive
                }
            }
        )
    }
}
