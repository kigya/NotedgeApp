package com.kigya.notedgeapp.utils.extensions

import androidx.fragment.app.Fragment
import com.kigya.notedgeapp.presentation.ui.navigation.Navigator
import com.kigya.notedgeapp.presentation.ui.navigation.Notifier

fun Fragment.navigator() = requireActivity() as Navigator

fun Fragment.notifier() = requireActivity() as Notifier