package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.playground.utils.retry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi(),
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1000L

        // run api.getAndroidVersionFeatures(27) and api.getAndroidVersionFeatures(28) in parallel


        val features27Deferred = viewModelScope.async {
            retryWithTimeout(
                numberOfRetries,
                timeout,
            ) {
                api.getAndroidVersionFeatures(27)
            }
        }

        val features28Deferred = viewModelScope.async {
            retryWithTimeout(
                numberOfRetries = numberOfRetries,
                timeout = timeout,
            ) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {
            try {
                val versionFeatures = listOf(
                    features27Deferred,
                    features28Deferred,
                ).awaitAll()

                uiState.value = UiState.Success(versionFeatures)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Error getting Android version features")
            }
        }
    }

    private suspend fun <T> retryWithTimeout(
        numberOfRetries: Int,
        timeout: Long,
        block: suspend () -> T,
    ) = retry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }
}