package com.deflatedpickle.axolotl.gui

import ModernDocking.app.Docking
import ModernDocking.app.RootDockingPanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.WindowConstants

object Window : JFrame("Axolotl") {
    init {
        size = Dimension(600, 600)
        jMenuBar = MenuBar
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setLocationRelativeTo(null)

        add(ToolBar, BorderLayout.NORTH)
        add(StatusBar, BorderLayout.SOUTH)
    }
}