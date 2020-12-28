package com.illiarb.revoluttest.libs.ui.toolbar

interface HasToolbar {

    fun getToolbarOptions(): ToolbarOptions

    class ToolbarOptions(
        val isVisible: Boolean = false,
        val title: String? = null,
        val hasCloseButton: Boolean = false,
        val onNavigationButtonClick: () -> Unit = {}
    )
}