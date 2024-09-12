package com.example.sosapp.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Fragment.launchFragment(destination: Int) {
    try {
        findNavController().navigate(destination)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
}
fun Fragment.showDialog(
    message: String,
    titleType: String = "Error",  // Default title
    onPositiveAction: (() -> Unit)? = null // Optional action for OK button
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(titleType)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            onPositiveAction?.invoke()  // Execute the action if provided
        }
        .show()
}






