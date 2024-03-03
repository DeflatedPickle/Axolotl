package com.deflatedpickle.axolotl.gui.dock

import ModernDocking.Dockable
import ModernDocking.app.Docking
import com.deflatedpickle.monoconskt.Monocons
import com.deflatedpickle.speckle.Speckle
import org.jdesktop.swingx.JXList
import org.jdesktop.swingx.JXPanel
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.*

object PlacesDockable : JPanel(), Dockable {
    val model = DefaultListModel<Place>()
    val list = JXList(model).apply {
        cellRenderer = PlacesCellRenderer()

        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    ContentDockable.current?.let { current ->
                        val index = this@apply.locationToIndex(e.point)
                        if (index > -1 && this@apply.getCellBounds(index, index).contains(e.point)){
                            val value = this@apply.model.getElementAt(index)
                            current.cwd = (value as Place).place
                            current.refresh()
                        }
                    }
                }
            }
        })
    }

    class Place(
        val place: File,
        val icon: ImageIcon = Monocons.FOLDER_NEW,
    )

    class PlacesCellRenderer : ListCellRenderer<Place> {
        val icon = JLabel()
        val title = JLabel()

        override fun getListCellRendererComponent(
            list: JList<out Place>?,
            value: Place?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
        ) = JXPanel().apply {
            layout = BorderLayout()

            removeAll()

            if (isSelected) {
                background = list!!.selectionBackground
                foreground = list.selectionForeground
            } else {
                background = list!!.background
                foreground = list.foreground
            }

            font = list.font

            title.text = value?.place?.name ?: ""
            icon.icon = value?.icon

            add(icon, BorderLayout.WEST)
            add(title, BorderLayout.CENTER)
        }
    }

    init {
        Docking.registerDockable(this)

        this.layout = BorderLayout()
        add(list, BorderLayout.CENTER)

        for (i in listOf(
            Place(Speckle.home),
            Place(Speckle.desktop),
            Place(Speckle.documents),
            Place(Speckle.download),
            Place(Speckle.music),
            Place(Speckle.pictures),
            Place(Speckle.video),
            Place(Speckle.data.resolve("Trash"), Monocons.DELETE)
        )) {
            model.addElement(i)
        }
    }

    override fun getPersistentID(): String = "places"
    override fun getTabText(): String = "Places"
}