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

package dev.jamesyox.kastro.demo

import dev.jamesyox.kastro.demo.detail.LightStateDetailView
import dev.jamesyox.kastro.demo.detail.LunarHorizonEventDetailView
import dev.jamesyox.kastro.demo.detail.LunarStateDetailView
import dev.jamesyox.kastro.demo.detail.SolarEventDetailView
import dev.jamesyox.kastro.demo.detail.SolarStateDetailView
import dev.jamesyox.kastro.demo.misc.html
import dev.jamesyox.kastro.demo.sol.SolarLightDescriptions
import dev.jamesyox.kastro.demo.sol.SolarPhaseDescriptions
import dev.jamesyox.kastro.demo.sol.description
import dev.jamesyox.kastro.demo.sol.shortDescription
import dev.jamesyox.kastro.demo.svgk.Selected
import dev.jamesyox.kastro.demo.view.ListView
import dev.jamesyox.kastro.demo.view.TopPanel
import dev.jamesyox.statik.css
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.css.Align
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.LinearDimension.Companion.auto
import kotlinx.css.Margin
import kotlinx.css.alignItems
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.margin
import kotlinx.css.maxWidth
import kotlinx.css.pct
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.datetime.TimeZone
import kotlinx.html.TagConsumer
import kotlinx.html.js.div
import org.w3c.dom.HTMLElement

context(
    appScope: CoroutineScope,
    _: TimeZone
)
fun TagConsumer<HTMLElement>.AppView(
    stylesheet: KastroDemoStylesheet,
    globalState: GlobalState,
) {
    div {
        css {
            width = 100.pct
            margin = Margin(0.px, auto)
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
        }
        TopPanel(
            stylesheet = stylesheet,
            globalState = globalState,
        ) {
            appScope.launch { globalState.kastroDemoEvents.emit(KastroDemoEvent.TopPanel(it)) }
        }

        val lowerContainer = div {
            css {
                maxWidth = 1200.px
                width = 100.pct
            }
        }
        globalState.paramsState.map {
            it.html(
                globalState = globalState,
                stylesheet = stylesheet
            ) {
                appScope.launch { globalState.kastroDemoEvents.emit(KastroDemoEvent.List(it)) }
            }
        }
            .combine(globalState.selectedItem) { params, selected -> Pair(params, selected) }
            .map {
                when (val selected = it.second) {
                    is Selected.SelectedEvent.Lunar -> LunarHorizonEventDetailView(event = selected.lunarEvent) {
                        appScope.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.DetailView.LunarEvent(it)
                            )
                        }
                    }

                    is Selected.SelectedEvent.Solar -> SolarEventDetailView(
                        description = selected.solarEvent.shortDescription,
                        event = selected.solarEvent,
                    ) {
                        appScope.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.DetailView.SolarEvent(it)
                            )
                        }
                    }

                    is Selected.SelectedState.Lunar -> LunarStateDetailView(
                        lunarHorizonState = selected.lunarPhaseState,
                    ) {
                        appScope.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.DetailView.Lunar(it)
                            )
                        }
                    }

                    is Selected.SelectedState.Light -> LightStateDetailView(
                        description = selected.solarLightState.lightState.description(SolarLightDescriptions),
                        solarLightState = selected.solarLightState,
                    ) {
                        appScope.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.DetailView.Light(it)
                            )
                        }
                    }

                    is Selected.SelectedState.Solar -> SolarStateDetailView(
                        description = selected.solarPhaseState.phase.description(SolarPhaseDescriptions),
                        solarPhaseState = selected.solarPhaseState,
                    ) {
                        appScope.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.DetailView.Solar(it)
                            )
                        }
                    }

                    null -> ListView(
                        stylesheet = stylesheet,
                        tableType = globalState.tabState,
                        kastroDemoTabs = it.first
                    ) {
                        appScope.launch { globalState.kastroDemoEvents.emit(KastroDemoEvent.List(it)) }
                    }
                }
            }.insertingBefore(lowerContainer, null)
    }
}
