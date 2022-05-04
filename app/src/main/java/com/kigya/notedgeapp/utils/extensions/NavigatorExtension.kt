package com.kigya.notedgeapp.utils.extensions

import androidx.fragment.app.Fragment
import com.kigya.notedgeapp.presentation.ui.navigation.Navigator

fun Fragment.navigator() = requireActivity() as Navigator
