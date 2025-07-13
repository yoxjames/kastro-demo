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

import dev.jamesyox.kastro.demo.KastroDemoStylesheetAgreement.smallScreenOnly
import dev.jamesyox.kastro.demo.sol.stroke
import dev.jamesyox.kastro.sol.SolarPhase
import dev.jamesyox.statik.css.Selector
import dev.jamesyox.statik.css.selector
import kotlinx.css.Align
import kotlinx.css.Border
import kotlinx.css.BorderCollapse
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.Float
import kotlinx.css.JustifyContent
import kotlinx.css.Margin
import kotlinx.css.Outline
import kotlinx.css.Padding
import kotlinx.css.Position
import kotlinx.css.TextAlign
import kotlinx.css.a
import kotlinx.css.alignContent
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.border
import kotlinx.css.borderCollapse
import kotlinx.css.borderRadius
import kotlinx.css.button
import kotlinx.css.code
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.float
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.label
import kotlinx.css.left
import kotlinx.css.margin
import kotlinx.css.marginBottom
import kotlinx.css.opacity
import kotlinx.css.outline
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.css.th
import kotlinx.css.top
import kotlinx.css.vh
import kotlinx.css.width

interface KastroDemoStylesheet {
    val eventTableRow: Selector.Class
    val stateTable: Selector.Class
    val stateTableRow: Selector.Class
    val inactiveTab: Selector.Class
    val activeTab: Selector.Class
    val inputPanelActive: Selector.Class
    val inputPanelInactive: Selector.Class
    val smallScreenOnly: Selector.Class
}

object KastroDemoStylesheetAgreement : KastroDemoStylesheet {
    override val eventTableRow = Selector.Class("event-table-row")
    override val stateTableRow = Selector.Class("state-table-row")
    override val inactiveTab = Selector.Class("inactive-tab")
    override val activeTab = Selector.Class("active-tab")
    override val inputPanelActive = Selector.Class("input-panel-active")
    override val inputPanelInactive = Selector.Class("input-panel-inactive")
    override val smallScreenOnly = Selector.Class("small-screens-only")
    override val stateTable = Selector.Class("state-table")
}

fun CssBuilder.KastroDemoStylesheet(
    agreement: KastroDemoStylesheetAgreement,
) {
    fun CssBuilder.tab() {
        color = Color.white
        float = Float.left
        border = Border.none
        outline = Outline.none
        cursor = Cursor.pointer
        padding = Padding(14.px, 16.px)
        fontSize = 17.px
    }

    body {
        backgroundColor = Color.black
    }

    "th, td, tr, h1, h2, h3, p, li" {
        color = Color.white
    }

    th {
        textAlign = TextAlign.left
    }

    "h3, h2, h1, p" {
        margin = Margin(0.px)
    }

    a {
        color = Color.white
    }

    button {
        backgroundColor = Color("#313131")
        border = Border.none
        padding = Padding(8.px)
        color = Color.white
    }

    code {
        color = Color.white
        padding = Padding(4.px)
        backgroundColor = Color.black
    }

    label {
        color = Color.white
    }

    "#map" {
        height = 100.pct
        width = 100.pct
    }

    ".event-table-row:nth-child(odd)" {
        backgroundColor = Color("#312e2e")
    }

    media("(hover: hover)") {
        "button:hover" {
            backgroundColor = Color("#003fc5")
        }
    }

    selector(agreement.inactiveTab) {
        tab()
        backgroundColor = Color.inherit
    }

    selector(agreement.activeTab) {
        tab()
        backgroundColor = Color("#171718")
    }

    selector(agreement.inputPanelActive) {
        display = Display.flex
        flexDirection = FlexDirection.column
        alignItems = Align.center
        alignContent = Align.center
        justifyContent = JustifyContent.center
        height = 50.vh
        width = 50.vh
        marginBottom = 12.px
    }

    selector(agreement.inputPanelInactive) {
        display = Display.flex
        flexDirection = FlexDirection.column
        alignItems = Align.center
        alignContent = Align.center
        justifyContent = JustifyContent.center
        height = 50.vh
        width = 50.vh
        marginBottom = 12.px
    }

    media("screen and (max-width: 800px)") {
        ".input-panel-inactive" {
            display = Display.none
        }

        ".input-panel-active" {
            width = 100.pct
            justifyContent = JustifyContent.center
        }

        "#settings-button" {
            position = Position.absolute
            top = 16.px
            left = 16.px
            padding = Padding(8.px)
            borderRadius = 8.px
        }
    }

    media("screen and (min-width: 800px)") {
        ".${smallScreenOnly.name}" {
            display = Display.none
        }

        ".input-panel" {
            display = Display.block
        }
    }

    selector(agreement.eventTableRow) {
        height = 50.px
        border = Border.none
        backgroundColor = Color.inherit
        color = Color.white

        hover {
            backgroundColor = Color(SolarPhase.CivilDawn.stroke)
            color = Color.black
        }
    }

    selector(agreement.stateTable) {
        width = 100.pct
        borderCollapse = BorderCollapse.collapse
    }

    selector(agreement.stateTableRow) {
        height = 50.px
        opacity = 0.85

        hover {
            opacity = 1.0
        }
    }
}
