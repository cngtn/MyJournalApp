package com.myjournalapp.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED
import androidx.biometric.BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED
import androidx.biometric.BiometricManager.BIOMETRIC_STATUS_UNKNOWN
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricAuthenticator {

    fun authenticate(
        context: Context,
        title: String,
        subtitle: String,
        description: String,
        onSuccess: () -> Unit,
        onFailure: (errorCode: Int, errString: CharSequence) -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        val biometricManager = BiometricManager.from(context)
        val authenticatorTypes = BIOMETRIC_STRONG or
                BIOMETRIC_WEAK or
                DEVICE_CREDENTIAL

        val canAuthenticate = biometricManager.canAuthenticate(authenticatorTypes)

        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setAllowedAuthenticators(authenticatorTypes)
                .build()

            val biometricPrompt = BiometricPrompt(
                context as FragmentActivity,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailure(BIOMETRIC_ERROR_NONE_ENROLLED, "Authentication failed")
                    }
                }
            )
            biometricPrompt.authenticate(promptInfo)
        } else {
            // Handle cases where biometric authentication is not available
            val errorMessage = when (canAuthenticate) {
                BIOMETRIC_ERROR_NO_HARDWARE -> "No biometric features available on this device."
                BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Biometric features are currently unavailable."
                BIOMETRIC_ERROR_NONE_ENROLLED -> "No biometrics enrolled. Please enroll biometrics in your device settings."
                BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "Security update required."
                BIOMETRIC_ERROR_UNSUPPORTED -> "Biometric authentication is not supported."
                BIOMETRIC_STATUS_UNKNOWN -> "Unknown biometric status."
                else -> "Biometric authentication is not available."
            }
            onError(canAuthenticate, errorMessage)
        }
    }
}
