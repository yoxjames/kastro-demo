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

import dev.jamesyox.kastro.demo.detail.LightStateDetailViewEvent
import dev.jamesyox.kastro.demo.detail.LunarHorizonEventDetailViewEvent
import dev.jamesyox.kastro.demo.detail.LunarStateDetailViewEvent
import dev.jamesyox.kastro.demo.detail.SolarEventDetailViewEvent
import dev.jamesyox.kastro.demo.detail.SolarStateDetailViewEvent
import dev.jamesyox.kastro.demo.misc.Location
import dev.jamesyox.kastro.demo.view.ListViewEvent
import dev.jamesyox.kastro.demo.view.TopPanelEvent

sealed interface KastroDemoEvent {
    value class List(
        val event: ListViewEvent
    ) : KastroDemoEvent

    value class TopPanel(
        val event: TopPanelEvent
    ) : KastroDemoEvent

    sealed interface GeolocationEvent : KastroDemoEvent {
        value class GeolocationSuccess(
            val location: Location
        ) : GeolocationEvent

        data object GeolocationFailure : GeolocationEvent
    }

    value class MapMovement(
        val location: Location
    ) : KastroDemoEvent

    sealed interface DetailView : KastroDemoEvent {
        value class Solar(
            val event: SolarStateDetailViewEvent
        ) : DetailView

        value class Light(
            val event: LightStateDetailViewEvent
        ) : DetailView

        value class Lunar(
            val event: LunarStateDetailViewEvent
        ) : DetailView

        value class SolarEvent(
            val event: SolarEventDetailViewEvent
        ) : DetailView

        value class LunarEvent(
            val event: LunarHorizonEventDetailViewEvent
        ) : DetailView
    }
}
