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

import dev.jamesyox.kastro.demo.leaflet.LeafletManager
import dev.jamesyox.kastro.demo.misc.GeolocationManager
import dev.jamesyox.kastro.demo.misc.UrlParamsParser
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.html.dom.append

private val clock = Clock.System
private val timeZone = TimeZone.currentSystemDefault()
private val coroutineScope = CoroutineScope(Dispatchers.Default)

fun main() {
    val urlParamsParser = UrlParamsParser(clock = clock, timeZone = timeZone)
    val globalState = GlobalState(
        clock = clock,
        startingParams = urlParamsParser.startingParams,
        appScope = coroutineScope
    )

    with(document) {
        getElementById("kastro-demo-container")?.append {
            AppView(
                stylesheet = KastroDemoStylesheetAgreement,
                appScope = coroutineScope,
                globalState = globalState,
                timeZone = timeZone
            )
        }

        LeafletManager(
            coroutineScope = coroutineScope,
            globalState = globalState
        ).run()

        GeolocationManager(
            coroutineScope = coroutineScope,
            globalState = globalState
        ).run()
    }
}
