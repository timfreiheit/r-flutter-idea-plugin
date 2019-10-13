package de.timfreiheit.r_flutter.actions.ui

import de.timfreiheit.r_flutter.data.StringResource
import java.awt.*
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

fun showCreateNewStringKeyDialog(preselection: StringResource? = null): StringResource? {

    val textSize = Dimension(170, 30);
    val keyField: JTextField = JTextField().apply {
        preferredSize = textSize
        text = preselection?.key ?: ""
    }
    val valueField: JTextField = JTextField().apply {
        preferredSize = textSize
        text = preselection?.value ?: ""
    }
    val rootPanel = JPanel().apply {
        layout = BorderLayout()

        val panel = JPanel().apply {
            layout = GridBagLayout()
            add(JLabel("Key: "), GridBagConstraints().apply {
                gridx = 0
                gridy = 0
                anchor = GridBagConstraints.WEST
            })
            add(keyField, GridBagConstraints().apply {
                gridx = 1
                gridy = 0
            })
            add(JLabel("Value: "), GridBagConstraints().apply {
                gridx = 0
                gridy = 1
                anchor = GridBagConstraints.WEST
            })
            add(valueField, GridBagConstraints().apply {
                gridx = 1
                gridy = 1
            })
        }
        add(panel, BorderLayout.CENTER)
    }
    val action = JOptionPane.showOptionDialog(null, rootPanel, "Create new string resource",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
            null, null, null);
    if (action == JOptionPane.OK_OPTION) {
        val key = keyField.text.replace(Regex("[^._A-Za-z0-9]"), "");
        if (key.isEmpty()) {
            return null
        }
        val value = valueField.text
        return StringResource(key, value)
    }
    return null
}
