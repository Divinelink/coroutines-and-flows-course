package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14.mapToEntityList
import kotlinx.coroutines.launch

class RoomAndCoroutinesViewModel(
    private val api: MockApi,
    private val database: AndroidVersionDao,
) : BaseViewModel<UiState>() {

    fun loadData() {
        uiState.value = UiState.Loading.LoadFromDb
        viewModelScope.launch {
            val localVersions = database.getAndroidVersions()
            if (localVersions.isEmpty()) {
                uiState.value = UiState.Error(DataSource.DATABASE, "Local data are empty.")
            } else {
                uiState.value = UiState.Success(DataSource.DATABASE, localVersions.mapToUiModelList())
            }

            uiState.value = UiState.Loading.LoadFromNetwork

            try {
                api.getRecentAndroidVersions().onEach { androidVersion ->
                    database.insert(androidVersion.mapToEntity())
                }.also {
                    uiState.value = UiState.Success(DataSource.NETWORK, it)
                }
            } catch (exception: Exception) {
                uiState.value = UiState.Error(DataSource.NETWORK, "Something went wrong!")
            }
        }

    }

    fun clearDatabase() {
        viewModelScope.launch {
            database.clear()
        }
    }
}

enum class DataSource(val dataSourceName: String) {
    DATABASE("Database"),
    NETWORK("Network")
}