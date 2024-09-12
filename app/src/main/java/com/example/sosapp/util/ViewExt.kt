package com.example.sosapp.util

import android.view.View

/**
 * Convenience function to hide View
 *
 */
fun View.hide(onlyInvisible: Boolean = false) {
    this.visibility = if (onlyInvisible) View.INVISIBLE else View.GONE
}

/**
 * Convenience function to show View
 *
 */
fun View.show() {
    this.visibility = View.VISIBLE
}




