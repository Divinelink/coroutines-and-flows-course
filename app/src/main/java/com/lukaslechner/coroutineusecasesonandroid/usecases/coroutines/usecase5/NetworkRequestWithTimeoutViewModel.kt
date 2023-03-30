package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi(),
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeout: Long) {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val recentAndroidVersions = withTimeout(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (timeoutCancellationException: kotlinx.coroutines.TimeoutCancellationException) {
                uiState.value = UiState.Error("Network request timed out")
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }
        }
    }

}