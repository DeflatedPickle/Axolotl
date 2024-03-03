package com.deflatedpickle.axolotl.gui.dock

import ModernDocking.Dockable
import ModernDocking.DockingRegion
import ModernDocking.app.Docking
import ModernDocking.event.DockingEvent
import ModernDocking.settings.Settings
import com.deflatedpickle.axolotl.gui.menu.ContentContextMenu
import com.deflatedpickle.axolotl.gui.toolbar.ContentToolbar
import com.deflatedpickle.axolotl.util.History
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.time.DurationFormatUtils
import org.jdesktop.swingx.JXTable
import java.awt.BorderLayout
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import java.util.concurrent.TimeUnit
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel

class ContentDockable : JPanel(), Dockable {
    companion object {
        var current: ContentDockable? = null
    }

    class ContentCellRenderer(
        val column: Int
    ) : DefaultTableCellRenderer() {
        override fun setValue(value: Any) {
            val path = (value as File).toPath()
            val basicAttributes = Files.readAttributes(path.toRealPath(), BasicFileAttributes::class.java)

            text = when (column) {
                0 -> value.name
                1 -> FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(value))
                2 -> DurationFormatUtils.formatDurationHMS(basicAttributes.creationTime().toMillis())
                3 -> DurationFormatUtils.formatDurationHMS(basicAttributes.lastAccessTime().toMillis())
                4 -> DurationFormatUtils.formatDurationHMS(basicAttributes.lastModifiedTime().toMillis())
                else -> ""
            }
        }
    }

    private val model = object : DefaultTableModel(
        arrayOf(
            "Name",
            "Size",
            "Creation Time",
            "Last Accessed",
            "Last Modified",
        ), 0
    ) {
        override fun isCellEditable(row: Int, column: Int) = false
        // override fun isCellEditable(row: Int, column: Int) = this.getColumnName(column) == "Name"
    }
    private val table = JXTable(model).apply {
        getColumn(0).setCellRenderer(ContentCellRenderer(0))
        getColumn(1).setCellRenderer(ContentCellRenderer(1))
        getColumn(2).setCellRenderer(ContentCellRenderer(2))
        getColumn(3).setCellRenderer(ContentCellRenderer(3))
        getColumn(4).setCellRenderer(ContentCellRenderer(4))

        addMouseListener(object : MouseAdapter() {
            private fun popup(e: MouseEvent) {
                val row = this@apply.rowAtPoint(e.point)
                val column = this@apply.columnAtPoint(e.point)

                if (row > -1 && row <= this@apply.rowCount && column > -1 && column <= this@apply.columnCount) {
                    this@apply.setRowSelectionInterval(row, row)
                    val rect = this@apply.getCellRect(row, column, true)
                    ContentContextMenu.show(e.component, rect.x + rect.width - rect.width / 6, rect.y)
                }
            }

            override fun mousePressed(e: MouseEvent) {
                if (e.isPopupTrigger) {
                    popup(e)
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                if (e.isPopupTrigger) {
                    popup(e)
                }
            }

            override fun mouseClicked(e: MouseEvent) {
                if (e.isPopupTrigger) return
                current?.let { current ->
                    val index = this@apply.rowAtPoint(e.point)
                    if (index > -1) {
                        val value = this@apply.model.getValueAt(index, 0) as File
                        val leaf = cwd.resolve(value)

                        when (e.clickCount) {
                            1 -> {
                                if (leaf.isDirectory) {
                                    Docking.undock(PreviewDockable)
                                } else if (leaf.isFile) {
                                    if (Docking.isDocked(PreviewDockable)) {
                                        Docking.bringToFront(PreviewDockable)
                                    } else {
                                        Docking.dock(PreviewDockable, this@ContentDockable, DockingRegion.EAST)
                                    }
                                }
                            }

                            2 -> {
                                if (leaf.isDirectory) {
                                    current.cwd = cwd.resolve(value)
                                    current.refresh()
                                } else if (leaf.isFile) {
                                    Desktop.getDesktop().open(leaf)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    var cwd = File(System.getProperty("user.dir"))

    private val id = UUID.randomUUID()

    init {
        Docking.registerDockable(this)

        this.layout = BorderLayout()
        add(ContentToolbar, BorderLayout.NORTH)
        add(JScrollPane(this.table), BorderLayout.CENTER)

        Docking.addDockingListener {
            when (it.id) {
                DockingEvent.ID.SHOWN -> current = this
                DockingEvent.ID.HIDDEN -> if (current == this) current = null
                else -> {}
            }
        }

        if (current == null) {
            current = this
        }

        refresh()
    }

    fun refresh() {
        model.rowCount = 0

        for (i in cwd.listFiles() ?: arrayOf()) {
            model.addRow(arrayOf(i, i, i, i, i))
        }
    }

    override fun getPersistentID(): String = this.id.toString()
    override fun getTabText(): String = this.cwd.name
    override fun getTabPosition() = Settings.getTabLayoutPolicy()
}