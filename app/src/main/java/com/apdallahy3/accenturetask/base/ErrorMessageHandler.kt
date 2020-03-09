package com.apdallahy3.accenturetask.base

interface ErrorMessageHandler {

    fun getMessage() : String?

    fun onRetry() : Unit
}