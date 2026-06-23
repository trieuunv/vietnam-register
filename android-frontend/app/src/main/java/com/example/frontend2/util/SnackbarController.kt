package com.example.frontend2.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Controller to manage Snackbar displays throughout the app
 */
class SnackbarController(
    private val scope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState
) {
    /**
     * Show an error message in the Snackbar
     */
    fun showError(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    /**
     * Show a success message in the Snackbar
     */
    fun showSuccess(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    /**
     * Show an informational message in the Snackbar
     */
    fun showInfo(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }
}
