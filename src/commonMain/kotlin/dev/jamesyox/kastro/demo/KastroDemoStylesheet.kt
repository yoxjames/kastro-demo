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
import dev.jamesyox.statik.css.ClassSelector
import dev.jamesyox.statik.css.StylesheetReference
import dev.jamesyox.statik.css.classSelector
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
    val eventTableRow: ClassSelector
    val stateTable: ClassSelector
    val stateTableRow: ClassSelector
    val inactiveTab: ClassSelector
    val activeTab: ClassSelector
    val inputPanelActive: ClassSelector
    val inputPanelInactive: ClassSelector
    val smallScreenOnly: ClassSelector
}

object KastroDemoStylesheetAgreement : KastroDemoStylesheet {
    override val eventTableRow = ClassSelector("event-table-row")
    override val stateTableRow = ClassSelector("state-table-row")
    override val inactiveTab = ClassSelector("inactive-tab")
    override val activeTab = ClassSelector("active-tab")
    override val inputPanelActive = ClassSelector("input-panel-active")
    override val inputPanelInactive = ClassSelector("input-panel-inactive")
    override val smallScreenOnly = ClassSelector("small-screens-only")
    override val stateTable = ClassSelector("state-table")
}

val KastroDemoCssAgreement = StylesheetReference(
    style = KastroDemoStylesheetAgreement,
    httpPath = "./style.css"
)

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

    classSelector(agreement.inactiveTab) {
        tab()
        backgroundColor = Color.inherit
    }

    classSelector(agreement.activeTab) {
        tab()
        backgroundColor = Color("#171718")
    }

    classSelector(agreement.inputPanelActive) {
        display = Display.flex
        flexDirection = FlexDirection.column
        alignItems = Align.center
        alignContent = Align.center
        justifyContent = JustifyContent.center
        height = 50.vh
        width = 50.vh
        marginBottom = 12.px
    }

    classSelector(agreement.inputPanelInactive) {
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

    classSelector(agreement.eventTableRow) {
        height = 50.px
        border = Border.none
        backgroundColor = Color.inherit
        color = Color.white

        hover {
            backgroundColor = Color(SolarPhase.CivilDawn.stroke)
            color = Color.black
        }
    }

    classSelector(agreement.stateTable) {
        width = 100.pct
        borderCollapse = BorderCollapse.collapse
    }

    classSelector(agreement.stateTableRow) {
        height = 50.px
        opacity = 0.85

        hover {
            opacity = 1.0
        }
    }
}
