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

import dev.jamesyox.kastro.demo.GlobalState
import dev.jamesyox.kastro.demo.KastroDemoEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import web.geolocation.GeolocationPosition
import web.geolocation.PositionOptions
import web.navigator.navigator

class GeolocationManager(
    private val coroutineScope: CoroutineScope,
    private val globalState: GlobalState
) {
    fun run() {
        coroutineScope.launch {
            globalState.geolocationRequests.collect {
                navigator.geolocation.getCurrentPositionWithCallbacks(
                    options = object : PositionOptions {
                        override var enableHighAccuracy: Boolean? = true
                        override var maximumAge: Int? = null
                        override var timeout: Int? = null
                    },
                    successCallback = { geolocationPosition: GeolocationPosition ->
                        this@launch.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.GeolocationEvent.GeolocationSuccess(
                                    Location(
                                        latitude = geolocationPosition.coords.latitude,
                                        longitude = geolocationPosition.coords.longitude
                                    )
                                )
                            )
                        }
                    },
                    errorCallback = { error ->
                        this@launch.launch {
                            globalState.kastroDemoEvents.emit(
                                KastroDemoEvent.GeolocationEvent.GeolocationFailure
                            )
                        }
                    }
                )
            }
        }
    }
}
