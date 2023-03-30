package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var getAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    private var getAndroidVersionFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        getAndroidVersionsCall = mockApi.getRecentAndroidVersions()

        getAndroidVersionsCall?.enqueue(object : Callback<List<AndroidVersion>> {
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if (response.isSuccessful) {
                    val mostRecentVersion = response.body()?.lastOrNull()

                    getAndroidVersionFeaturesCall = mostRecentVersion?.let {
                        mockApi.getAndroidVersionFeatures(it.apiLevel)
                    }

                    with(getAndroidVersionFeaturesCall) {
                        this?.enqueue(object : Callback<VersionFeatures> {
                            override fun onResponse(
                                call: Call<VersionFeatures>,
                                response: Response<VersionFeatures>
                            ) {
                                if (response.isSuccessful) {
                                    val versionFeatures = response.body()
                                    uiState.value = UiState.Success(versionFeatures!!)
                                } else {
                                    uiState.value =
                                        UiState.Error("Error fetching Android Version Features")
                                }
                            }

                            override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                                uiState.value =
                                    UiState.Error("Error fetching Android Version Features")
                            }
                        })
                    }

                } else {
                    uiState.value = UiState.Error("Error fetching Android Versions")
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCleared() {
        super.onCleared()

        getAndroidVersionsCall?.cancel()
        getAndroidVersionFeaturesCall?.cancel()
    }
}