package com.kigya.notedgeapp.presentation.ui.navigation

import androidx.annotation.StringRes

typealias NotifierCallback = (Unit) -> Unit

interface Notifier {

    fun showSnackbar(@StringRes message: Int, notifierCallback: NotifierCallback? = null)

}