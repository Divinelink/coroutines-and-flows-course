package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi(),
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading
        val job = viewModelScope.launch { // the launch builder has as a default dispatcher the Main.immediate dispatcher
            try {
                Timber.d("I'm the first statement in the coroutine")
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }
        }

        Timber.d("I'm the first statement after launching the coroutine")

        viewModelScope.launch(Dispatchers.Main.immediate) {
            try {
                Timber.d("Main Dispatcher => I'm the first statement in the coroutine")
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }
        }

        Timber.d("Main Dispatcher => I'm the first statement after launching the coroutine")

        job.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                Timber.d("Coroutine was cancelled")
            }
        }
    }
}