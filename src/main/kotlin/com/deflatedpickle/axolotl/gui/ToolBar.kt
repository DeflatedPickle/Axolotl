package com.deflatedpickle.axolotl.gui

import com.deflatedpickle.monoconskt.Monocons
import javax.swing.JButton
import javax.swing.JToolBar

object ToolBar : JToolBar("Axolotl") {
    init {
        add(JButton(Monocons.ARROW_BACK))
        add(JButton(Monocons.ARROW_FORWARD))
    }
}