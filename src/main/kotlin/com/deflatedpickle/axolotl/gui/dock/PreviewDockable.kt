package com.deflatedpickle.axolotl.gui.dock

import ModernDocking.Dockable
import ModernDocking.app.Docking
import javax.swing.JPanel

object PreviewDockable : JPanel(), Dockable {
    init {
        Docking.registerDockable(this)
    }

    override fun getPersistentID(): String = "preview"
    override fun getTabText(): String = "Preview"
}