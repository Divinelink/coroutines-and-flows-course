package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            var result: BigInteger
            val computationalDuration = measureTimeMillis {
                result = calculateFactorial(factorialOf)
            }

            var resultString: String
            val stringConversionDuration = measureTimeMillis {
                withContext(Dispatchers.Default) {
                    resultString = result.toString()
                }
            }

            uiState.value = UiState.Success(
                result = resultString,
                computationDuration = computationalDuration,
                stringConversionDuration = stringConversionDuration,
            )
        }

    }

    private suspend fun calculateFactorial(factorialOf: Int) =
        withContext(Dispatchers.Default) {
            var factorial = BigInteger.ONE
            for (i in 1..factorialOf) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            Timber.d("Calculating Factorial Completed!")
            factorial
        }
}