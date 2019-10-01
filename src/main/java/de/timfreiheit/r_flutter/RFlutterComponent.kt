package de.timfreiheit.r_flutter

import com.intellij.openapi.components.BaseComponent

class RFlutterComponent : BaseComponent {
    override fun initComponent() {
        ActionsProvider.register()
    }
}
