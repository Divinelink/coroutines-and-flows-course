package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInSeveralCoroutinesViewModel(
    private val factorialCalculator: FactorialCalculator = FactorialCalculator(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : BaseViewModel<UiState>() {

    fun performCalculation(
        factorialOf: Int,
        numberOfCoroutines: Int,
    ) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            var factorialResult: BigInteger

            val computationDuration = withContext(defaultDispatcher) {
                measureTimeMillis {
                    factorialResult =
                        factorialCalculator.calculateFactorial(
                            factorialOf,
                            numberOfCoroutines
                        )
                }
            }

            var resultString: String
            val stringConversionDuration = measureTimeMillis {
                resultString = convertToString(factorialResult)
            }

            uiState.value = UiState.Success(
                resultString,
                computationDuration,
                stringConversionDuration,
            )
        }
    }


    // TODO: execute on background thread
    private suspend fun convertToString(number: BigInteger) =
        withContext(defaultDispatcher) {
            number.toString()
        }
}