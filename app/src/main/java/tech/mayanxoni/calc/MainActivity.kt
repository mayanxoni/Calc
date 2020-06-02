/*
MIT License

Copyright (c) 2020 Mayank Soni

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package tech.mayanxoni.calc

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private val twTop: TextView by bind(R.id.tw_top)
    private val twBottom: AppCompatTextView by bind(R.id.tw_bottom)

    private val btn0: Button by bind(R.id.btn_0)
    private val btn1: Button by bind(R.id.btn_1)
    private val btn2: Button by bind(R.id.btn_2)
    private val btn3: Button by bind(R.id.btn_3)
    private val btn4: Button by bind(R.id.btn_4)
    private val btn5: Button by bind(R.id.btn_5)
    private val btn6: Button by bind(R.id.btn_6)
    private val btn7: Button by bind(R.id.btn_7)
    private val btn8: Button by bind(R.id.btn_8)
    private val btn9: Button by bind(R.id.btn_9)

    private val btnFraction: Button by bind(R.id.btn_fraction)
    private val btnSq: Button by bind(R.id.btn_sq)
    private val btnSqRoot: Button by bind(R.id.btn_sq_root)
    private val btnPercent: Button by bind(R.id.btn_percent)
    private val btnDiv: Button by bind(R.id.btn_div)
    private val btnMul: Button by bind(R.id.btn_mul)
    private val btnAdd: Button by bind(R.id.btn_add)
    private val btnSub: Button by bind(R.id.btn_sub)
    private val btnNegate: Button by bind(R.id.btn_negate)
    private val btnDecimal: Button by bind(R.id.btn_decimal)
    private val btnClear: Button by bind(R.id.btn_clear)
    private val btnCalc: Button by bind(R.id.btn_calc)
    private val btnDelete: Button by bind(R.id.btn_delete)

    private val FRACTION = "1/"
    private val SQUARE = "sqr"
    private val ROOT = "√"
    private val PERCENTAGE = ""

    private val DIVISION = " ÷ "
    private val MULTIPLICATION = " × "
    private val ADDITION = " + "
    private val SUBTRACTION = " − "

    private val NEGATE = "negate"
    private val DECIMAL = "."
    private val EQUAL = " = "

    private val ZERO: String = "0"
    private val ONE: String = "1"
    private val TWO: String = "2"
    private val THREE: String = "3"
    private val FOUR: String = "4"
    private val FIVE: String = "5"
    private val SIX: String = "6"
    private val SEVEN: String = "7"
    private val EIGHT: String = "8"
    private val NINE: String = "9"

    private val INIT = ""

    private var isFutureOperationButtonClicked: Boolean = false
    private var isInstantOperationButtonClicked: Boolean = false
    private var isEqualButtonClicked: Boolean = false

    private var currentNumber: Double = 0.0
    private var currentResult: Double = 0.0

    private var historyText = ""
    private var historyInstantOperationText = ""
    private var historyActionList: ArrayList<String> = ArrayList()

    private var currentOperation = INIT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFraction.setOnClickListener { onInstantOperationButtonClick(FRACTION) }
        btnSq.setOnClickListener { onInstantOperationButtonClick(SQUARE) }
        btnSqRoot.setOnClickListener { onInstantOperationButtonClick(ROOT) }
        btnPercent.setOnClickListener { onInstantOperationButtonClick(PERCENTAGE) }

        btn0.setOnClickListener { onNumberButtonClick(ZERO) }
        btn1.setOnClickListener { onNumberButtonClick(ONE) }
        btn2.setOnClickListener { onNumberButtonClick(TWO) }
        btn3.setOnClickListener { onNumberButtonClick(THREE) }
        btn4.setOnClickListener { onNumberButtonClick(FOUR) }
        btn5.setOnClickListener { onNumberButtonClick(FIVE) }
        btn6.setOnClickListener { onNumberButtonClick(SIX) }
        btn7.setOnClickListener { onNumberButtonClick(SEVEN) }
        btn8.setOnClickListener { onNumberButtonClick(EIGHT) }
        btn9.setOnClickListener { onNumberButtonClick(NINE) }

        btnDiv.setOnClickListener { onFutureOperationButtonClick(DIVISION) }
        btnMul.setOnClickListener { onFutureOperationButtonClick(MULTIPLICATION) }
        btnAdd.setOnClickListener { onFutureOperationButtonClick(ADDITION) }
        btnSub.setOnClickListener { onFutureOperationButtonClick(SUBTRACTION) }

        btnNegate.setOnClickListener {
            val currentValue: String = twBottom.text.toString()
            currentNumber = formatStringToDouble(currentValue)
            if (currentNumber == 0.0)
                return@setOnClickListener
            currentNumber *= -1
            twBottom.text = formatDoubleToString(currentNumber)
            if (isInstantOperationButtonClicked) {
                historyInstantOperationText = "($historyInstantOperationText)"
                historyInstantOperationText = StringBuilder()
                    .append(NEGATE)
                    .append(historyInstantOperationText)
                    .toString()
                twTop.text = StringBuilder()
                    .append(historyText)
                    .append(currentOperation)
                    .append(historyInstantOperationText)
                    .toString()
            }
            if (isEqualButtonClicked)
                currentOperation = INIT
            isFutureOperationButtonClicked = false
            isEqualButtonClicked = false
        }

        btnDecimal.setOnClickListener {
            var currentValue: String = twBottom.text.toString()
            if (isFutureOperationButtonClicked || isInstantOperationButtonClicked || isEqualButtonClicked) {
                currentValue = StringBuilder()
                    .append(ZERO)
                    .append(DECIMAL)
                    .toString()
                if (isInstantOperationButtonClicked) {
                    historyInstantOperationText = ""
                    twTop.text = StringBuilder()
                        .append(historyText)
                        .append(currentOperation)
                        .toString()
                }
                if (isEqualButtonClicked)
                    currentOperation = INIT
                currentNumber = 0.0
            } else if (currentValue.contains(DECIMAL)) {
                return@setOnClickListener
            } else {
                currentValue = StringBuilder()
                    .append(currentValue)
                    .append(DECIMAL)
                    .toString()
            }
            twBottom.text = currentValue
            isFutureOperationButtonClicked = false
            isInstantOperationButtonClicked = false
            isEqualButtonClicked = false
        }

        btnClear.setOnClickListener {
            currentNumber = 0.0
            currentResult = 0.0
            currentOperation = INIT
            historyText = ""
            historyInstantOperationText = ""
            twBottom.text = formatDoubleToString(currentNumber)
            twTop.text = historyText
            isFutureOperationButtonClicked = false
            isEqualButtonClicked = false
            isInstantOperationButtonClicked = false
        }

        btnCalc.setOnClickListener {
            if (isFutureOperationButtonClicked)
                currentNumber = currentResult
            val historyAllText = calculateResult()
            historyActionList.add(historyAllText)
            historyText = StringBuilder()
                .append(formatDoubleToString(currentResult))
                .toString()
            twTop.text = ""
            isFutureOperationButtonClicked = false
            isEqualButtonClicked = true
        }

        btnDelete.setOnClickListener {
            if (isFutureOperationButtonClicked || isInstantOperationButtonClicked || isEqualButtonClicked)
                return@setOnClickListener
            var currentValue: String = twBottom.text.toString()
            val charsLimit = if (currentValue.first().isDigit()) 1 else 2
            if (currentValue.length > charsLimit)
                currentValue = currentValue.substring(0, currentValue.length - 1)
            else
                currentValue = ZERO
            twBottom.text = currentValue
            currentNumber = formatStringToDouble(currentValue)
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun onNumberButtonClick(number: String, isHistory: Boolean = false) {
        var currentValue: String = twBottom.text.toString()
        currentValue =
            if (currentValue == ZERO || isFutureOperationButtonClicked || isInstantOperationButtonClicked || isEqualButtonClicked || isHistory) {
                number
            } else {
                StringBuilder()
                    .append(currentValue)
                    .append(number)
                    .toString()
            }
        try {
            currentNumber = formatStringToDouble(currentValue)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Value must be a number!")
        }
        twBottom.text = currentValue
        if (isEqualButtonClicked) {
            currentOperation = INIT
            historyText = ""
        }
        if (isInstantOperationButtonClicked) {
            historyInstantOperationText = ""
            twTop.text = StringBuilder()
                .append(historyText)
                .append(currentOperation)
                .toString()
            isInstantOperationButtonClicked = false
        }
        isFutureOperationButtonClicked = false
        isEqualButtonClicked = false
    }

    private fun onFutureOperationButtonClick(operation: String) {
        if (!isFutureOperationButtonClicked && !isEqualButtonClicked)
            calculateResult()
        currentOperation = operation
        if (isInstantOperationButtonClicked) {
            isInstantOperationButtonClicked = false
            historyText = twTop.text.toString()
        }
        twTop.text = StringBuilder()
            .append(historyText)
            .append(operation)
            .toString()
        isFutureOperationButtonClicked = true
        isEqualButtonClicked = false
    }

    private fun onInstantOperationButtonClick(operation: String) {
        var currentValue: String = twBottom.text.toString()
        var thisOperationNumber: Double = formatStringToDouble(currentValue)
        currentValue = "(${formatDoubleToString(thisOperationNumber)})"
        when (operation) {
            PERCENTAGE -> {
                thisOperationNumber = (currentResult * thisOperationNumber) / 100
                currentValue = formatDoubleToString(thisOperationNumber)
            }
            ROOT -> thisOperationNumber = thisOperationNumber.sqrt
            SQUARE -> thisOperationNumber *= thisOperationNumber
            FRACTION -> thisOperationNumber = 1 / thisOperationNumber
        }
        if (isInstantOperationButtonClicked) {
            historyInstantOperationText = "($historyInstantOperationText)"
            historyInstantOperationText = StringBuilder()
                .append(operation)
                .append(historyInstantOperationText)
                .toString()
            twTop.text = if (isEqualButtonClicked) {
                historyInstantOperationText
            } else {
                StringBuilder()
                    .append(historyText)
                    .append(currentOperation)
                    .append(historyInstantOperationText)
                    .toString()
            }
        } else if (isEqualButtonClicked) {
            historyInstantOperationText = StringBuilder()
                .append(operation)
                .append(currentValue)
                .toString()
            twTop.text = historyInstantOperationText
        } else {
            historyInstantOperationText = StringBuilder()
                .append(operation)
                .append(currentValue)
                .toString()
            twTop.text = StringBuilder()
                .append(historyText)
                .append(currentOperation)
                .append(historyInstantOperationText)
                .toString()
        }
        twBottom.text = formatDoubleToString(thisOperationNumber)
        if (isEqualButtonClicked)
            currentResult = thisOperationNumber
        else
            currentNumber = thisOperationNumber
        isInstantOperationButtonClicked = true
        isFutureOperationButtonClicked = false
    }

    private fun calculateResult(): String {
        when (currentOperation) {
            INIT -> {
                currentResult = currentNumber
                historyText = StringBuilder()
                    .append(twTop.text.toString())
                    .toString()
            }
            ADDITION -> currentResult += currentNumber
            SUBTRACTION -> currentResult -= currentNumber
            MULTIPLICATION -> currentResult *= currentNumber
            DIVISION -> currentResult /= currentNumber
        }
        twBottom.text = formatDoubleToString(currentResult)
        if (isInstantOperationButtonClicked) {
            isInstantOperationButtonClicked = false
            historyText = twTop.text.toString()
            if (isEqualButtonClicked) historyText = StringBuilder()
                .append(historyText)
                .append(currentOperation)
                .append(formatDoubleToString(currentNumber))
                .toString()
        } else {
            historyText = StringBuilder()
                .append(historyText)
                .append(currentOperation)
                .append(formatDoubleToString(currentNumber))
                .toString()
        }
        return StringBuilder()
            .append(historyText)
            .append(EQUAL)
            .append(formatDoubleToString(currentResult))
            .toString()
    }

    private fun useNumberFormat(): DecimalFormat {
        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = '.'
        val format = DecimalFormat("#.##############")
        format.decimalFormatSymbols = symbols
        return format
    }

    private fun formatDoubleToString(number: Double): String {
        return useNumberFormat()
            .format(number)
    }

    private fun formatStringToDouble(number: String): Double {
        return useNumberFormat()
            .parse(number)
            .toDouble()
    }

    private val Double.sqrt: Double get() = sqrt(this)

    private fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
    }
}
