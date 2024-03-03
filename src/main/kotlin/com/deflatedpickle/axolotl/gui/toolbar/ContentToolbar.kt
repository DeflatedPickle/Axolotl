package com.deflatedpickle.axolotl.gui.toolbar

import com.deflatedpickle.monoconskt.Monocons
import javax.swing.JButton
import javax.swing.JToolBar

object ContentToolbar : JToolBar("Content") {
    init {
        add(JButton(Monocons.FILE_NEW))
        add(JButton(Monocons.FOLDER_NEW))
    }
}