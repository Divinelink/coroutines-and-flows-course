package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.*
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi(),
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                api.getAndroidVersionFeatures(apiLevel = 27)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Error while fetching Android version features")
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        uiState.value = UiState.Loading

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Error while fetching Android version features")
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            api.getAndroidVersionFeatures(apiLevel = 27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
//            val recentAndroidVersions = api.getRecentAndroidVersions()

//            supervisorScope {
//                val oreoFeatures = async {
//                    api.getAndroidVersionFeatures(27)
//                }
//                val pieFeatures = async {
//                    api.getAndroidVersionFeatures(28)
//                }
//                val android10Features = async {
//                    api.getAndroidVersionFeatures(29)
//                }
//                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
//                    .mapNotNull {
//                        try {
//                            it.await()
//                        } catch (e: Exception) {
//                            Timber.e(e)
//                            null
//                        }
//                    }
//                uiState.value = UiState.Success(versionFeatures)
//            }

            val deferredVersionFeatures = listOf(27, 28, 29)
                .map { androidVersion ->
                    async {
                        try {
                            api.getAndroidVersionFeatures(androidVersion)
                        } catch (e: Exception) {
                            if (e is CancellationException) {
                                throw e
                            }
                            Timber.e(e)
                            null
                        }
                    }
                }
            val versionFeatures = deferredVersionFeatures.mapNotNull { it.await() }
            uiState.value = UiState.Success(versionFeatures)
        }


    }
}