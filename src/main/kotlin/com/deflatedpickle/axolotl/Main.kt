package com.deflatedpickle.axolotl

import ModernDocking.DockingRegion
import ModernDocking.app.Docking
import ModernDocking.app.RootDockingPanel
import ModernDocking.settings.Settings
import com.deflatedpickle.axolotl.gui.ToolBar
import com.deflatedpickle.axolotl.gui.Window
import com.deflatedpickle.axolotl.gui.dock.*
import com.formdev.flatlaf.FlatDarculaLaf
import com.formdev.flatlaf.util.SystemInfo
import com.jidesoft.plaf.LookAndFeelFactory
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.MenuBar
import java.awt.SystemColor.desktop
import javax.swing.Box
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.SwingUtilities


fun main(args: Array<String>) {
    System.setProperty("log4j.skipJansi", "false")

    if (args.isEmpty()) {
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true)
            JDialog.setDefaultLookAndFeelDecorated(true)
        }

        if (SystemInfo.isMacOS) {
            // todo: mac support
            System.setProperty("apple.laf.useScreenMenuBar", "true")
            System.setProperty("apple.awt.application.name", "Axolotl")
            System.setProperty("apple.awt.application.appearance", "system")

            with(Desktop.getDesktop()) {
                if (isSupported(Desktop.Action.APP_ABOUT)) {
                    setAboutHandler {
                    }
                }


                if (isSupported(Desktop.Action.APP_PREFERENCES)) {
                    setPreferencesHandler { e ->

                    }
                }

                if (isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
                    setQuitHandler { e, response ->
                        response.performQuit()
                    }
                }
            }

            if (SystemInfo.isMacFullWindowContentSupported) {
                Window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
                Window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
                Window.rootPane.putClientProperty("apple.awt.windowTitleVisible", false)
                ToolBar.add(Box.createHorizontalStrut(70), 0)
            }
        }

        SwingUtilities.invokeLater {
            FlatDarculaLaf.setup()
            LookAndFeelFactory.installJideExtension()

            Settings.setAlwaysDisplayTabMode(true)

            Docking.initialize(Window)
            Window.add(RootDockingPanel(Window), BorderLayout.CENTER)

            Docking.dock(ContentDockable(), Window, DockingRegion.CENTER)

            Docking.dock(PlacesDockable, Window, DockingRegion.WEST)

            Window.isVisible = true
        }
    } else {
        // todo: cli
    }
}