package voidthinking.star.box

import voidthinking.star.box.plugin.PluginDiscoverer
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import javax.swing.UIManager

fun main() = SwingUtilities.invokeLater {
    runCatching {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    }
    JFrame().apply {
        layout = GridBagLayout()

        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE

        add(JLabel("插件列表"), GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            gridwidth = 2
            insets.top = 8
            insets.bottom = 8
        })

        for ((index, plugin) in PluginDiscoverer.plugins.withIndex()) {
            add(JLabel(plugin.name, SwingConstants.LEFT), GridBagConstraints().apply {
                gridx = 0
                gridy = index + 1
                weightx = 1.0
                insets.left = 8
                insets.bottom = 8
                anchor = GridBagConstraints.WEST
            })
            add(JButton("启动").apply {
                addActionListener {
                    Thread(plugin::start).start()
                    SwingUtilities.invokeLater {
                        dispose()
                    }
                }
            }, GridBagConstraints().apply {
                gridx = 1
                gridy = index + 1
                insets.left = 8
                insets.right = 8
                insets.bottom = 8
            })
        }

        title = "星点盒"
        isResizable = false
        minimumSize = Dimension(250, 20)
        pack()

        isVisible = true
    }
}