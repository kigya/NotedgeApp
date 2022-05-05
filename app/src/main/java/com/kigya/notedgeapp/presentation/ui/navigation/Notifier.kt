package com.kigya.notedgeapp.presentation.ui.navigation

typealias NotifierCallback = (Unit) -> Unit

interface Notifier {

    fun showSnackbar(message: String, notifierCallback: NotifierCallback? = null)

}