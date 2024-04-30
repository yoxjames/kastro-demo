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

package dev.jamesyox.kastro.demo.leaflet

import dev.jamesyox.kastro.demo.GlobalState
import dev.jamesyox.kastro.demo.KastroDemoEvent
import dev.jamesyox.kastro.demo.misc.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import web.dom.observers.ResizeObserver
import kotlin.js.json
import kotlin.math.roundToInt

@Suppress("TooGenericExceptionCaught")
class LeafletManager(
    private val coroutineScope: CoroutineScope,
    private val globalState: GlobalState
) {
    private val leaflet = js("""require("leaflet")""")
    private val map = leaflet.map("map")

    private fun moveListener() {
        coroutineScope.launch {
            val latitude = map.getCenter().lat.unsafeCast<Double>()
                .let { it * 100000.0 }
                .roundToInt()
                .let { it / 100000.0 }
            val longitude = map.getCenter().lng.unsafeCast<Double>()
                .let { it * 100000.0 }
                .roundToInt()
                .let { it / 100000.0 }

            globalState.kastroDemoEvents.emit(KastroDemoEvent.MapMovement(Location(latitude, longitude)))
        }
    }
    fun run() {
        try {
            leaflet.tileLayer(
                "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
                json(
                    "maxZoom" to 19,
                    "attribution" to "&copy <a href=\"http://www.openstreetmap.org/copyright\">OpenStreetMap</a>"
                )
            ).addTo(map)

            map.on("moveend") { moveListener() }
            val mapElement = web.dom.document.getElementById("map")!!
            val resizeObserver = ResizeObserver { _, _ ->
                map.invalidateSize()
                Unit
            }
            resizeObserver.observe(mapElement)
            coroutineScope.launch {
                globalState.nonMapUpdates.collect {
                    map.off("moveend")
                    map.on("moveend") {
                        map.off("moveend")
                        map.on("moveend") { moveListener() }
                    }
                    val pos = leaflet.latLng(arrayOf(it.latitude, it.longitude))
                    map.setView(pos, 8)
                }
            }
        } catch (e: kotlin.coroutines.cancellation.CancellationException) {
            throw e
        } catch (e: Exception) {
            println("Error $e")
        }
    }
}
