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

package dev.jamesyox.kastro.demo.misc

import dev.jamesyox.kastro.demo.CelestialParameters
import dev.jamesyox.kastro.demo.GlobalState
import dev.jamesyox.kastro.demo.KastroDemoStylesheet
import dev.jamesyox.kastro.demo.view.AboutView
import dev.jamesyox.kastro.demo.view.CurrentView
import dev.jamesyox.kastro.demo.view.EventsView
import dev.jamesyox.kastro.demo.view.ListViewEvent
import dev.jamesyox.kastro.demo.view.StatesView
import kotlinx.coroutines.CoroutineScope
import org.w3c.dom.HTMLElement

data class KastroDemoTabs(
    val statesTab: List<HTMLElement>,
    val eventsTab: List<HTMLElement>,
    val currentTab: List<HTMLElement>,
    val aboutTab: List<HTMLElement>,
)

fun CelestialParameters.html(
    coroutineScope: CoroutineScope,
    stylesheet: KastroDemoStylesheet,
    globalState: GlobalState,
    onEvent: (ListViewEvent) -> Unit
): KastroDemoTabs {
    return KastroDemoTabs(
        statesTab = StatesView(
            stylesheet = stylesheet,
            solarTableState = solarPhaseStates,
            lunarTableState = lunarHorizonState,
            lightTableState = solarLightState,
            timeZone = timeZone,
        ) {
            onEvent(ListViewEvent.StatesView(it))
        },
        eventsTab = EventsView(
            stylesheet = stylesheet,
            solarEvents = solarEvents,
            lunarEvents = lunarEvents,
            timeZone = timeZone,
        ) {
            onEvent(ListViewEvent.EventsView(it))
        },
        currentTab = CurrentView(
            coroutineScope = coroutineScope,
            globalState = globalState
        ),
        aboutTab = AboutView()
    )
}
