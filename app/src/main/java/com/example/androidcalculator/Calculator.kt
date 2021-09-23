package com.example.androidcalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import java.math.BigDecimal

class Calculator(private val activity: AppCompatActivity) {
    private var textView: TextView? = null
    private var preTextView: TextView? = null
    private var operationTextView: TextView? = null
    private var button0: Button? = null
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var button4: Button? = null
    private var button5: Button? = null
    private var button6: Button? = null
    private var button7: Button? = null
    private var button8: Button? = null
    private var button9: Button? = null
    private var buttonC: Button? = null
    private var buttonSign: Button? = null
    private var buttonPerc: Button? = null
    private var buttonDel: Button? = null
    private var buttonDiv: Button? = null
    private var buttonMult: Button? = null
    private var buttonSubt: Button? = null
    private var buttonDot: Button? = null
    private var buttonEqu: Button? = null
    private var buttonAdd: Button? = null
    private var horizontalScrollView: HorizontalScrollView? = null

    private var activeOperation: OperationType = OperationType.NONE

    init {
        setupMembers()
        setupCallbacks()
    }

    fun getItemsKeysToSave(): List<String> {
        return listOf("TextViewKey", "PreTextViewKey", "OperatorTextViewKey")
    }
    fun getItemsContentsToSave(): List<String> {
        return listOf(textView?.text.toString(), preTextView?.text.toString(), operationTextView?.text.toString())
    }
    fun getItemCountToSave(): Int {
        return getItemsKeysToSave().size
    }
    fun restoreState(state: Bundle) {
        val textViewText = state.getString(getItemsKeysToSave()[0], "0")
        textView?.text = textViewText
        val preTextViewText = state.getString(getItemsKeysToSave()[1], "")
        preTextView?.text = preTextViewText
        val operatorTextViewText = state.getString(getItemsKeysToSave()[2], "")
        operationTextView?.text = operatorTextViewText
    }

    private fun setupMembers() {
        textView = activity.findViewById(R.id.textView)
        preTextView = activity.findViewById(R.id.preTextView)
        operationTextView = activity.findViewById(R.id.operationTextView)
        button0 = activity.findViewById(R.id.button0)
        button1 = activity.findViewById(R.id.button1)
        button2 = activity.findViewById(R.id.button2)
        button3 = activity.findViewById(R.id.button3)
        button4 = activity.findViewById(R.id.button4)
        button5 = activity.findViewById(R.id.button5)
        button6 = activity.findViewById(R.id.button6)
        button7 = activity.findViewById(R.id.button7)
        button8 = activity.findViewById(R.id.button8)
        button9 = activity.findViewById(R.id.button9)
        buttonC = activity.findViewById(R.id.buttonC)
        buttonSign = activity.findViewById(R.id.buttonSign)
        buttonPerc = activity.findViewById(R.id.buttonPerc)
        buttonDel = activity.findViewById(R.id.buttonDel)
        buttonDiv = activity.findViewById(R.id.buttonDiv)
        buttonMult = activity.findViewById(R.id.buttonMult)
        buttonSubt = activity.findViewById(R.id.buttonSubt)
        buttonDot = activity.findViewById(R.id.buttonDot)
        buttonEqu = activity.findViewById(R.id.buttonEqu)
        buttonAdd = activity.findViewById(R.id.buttonAdd)
        horizontalScrollView = activity.findViewById(R.id.horizontalScrollView)
    }

    private fun setupCallbacks() {
        button0?.setOnClickListener { appendDigit(0) }
        button1?.setOnClickListener { appendDigit(1) }
        button2?.setOnClickListener { appendDigit(2) }
        button3?.setOnClickListener { appendDigit(3) }
        button4?.setOnClickListener { appendDigit(4) }
        button5?.setOnClickListener { appendDigit(5) }
        button6?.setOnClickListener { appendDigit(6) }
        button7?.setOnClickListener { appendDigit(7) }
        button8?.setOnClickListener { appendDigit(8) }
        button9?.setOnClickListener { appendDigit(9) }
        buttonDot?.setOnClickListener { appendDot() }
        buttonC?.setOnClickListener { onClearPressed() }
        buttonSign?.setOnClickListener { changeSign() }
        buttonDel?.setOnClickListener { deleteLastDigit() }
        buttonAdd?.setOnClickListener { onAddSignPressed() }
        buttonSubt?.setOnClickListener { onSubtructPressed() }
        buttonMult?.setOnClickListener { onMultiplyPressed() }
        buttonDiv?.setOnClickListener { onDividePressed() }
        buttonPerc?.setOnClickListener { onPercentagePressed() }
        buttonEqu?.setOnClickListener { onEquationPressed() }
    }

    private fun onClearPressed() {
        textView?.text = "0"
        operationTextView?.text = ""
        preTextView?.text = ""
        activeOperation = OperationType.NONE
    }

    private fun appendDigit(digit: Int) {
        handleError()
        val text = textView?.text.toString()
        if (text == "0" || text == activity.getString(R.string.error_value)) {
            textView?.text = "$digit"
            return
        }
        textView?.append("$digit")
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun changeSign() {
        handleError()
        val text = textView?.text.toString()
        if (text == "0") {
            return
        }
        if (text[0] != '-') {
            val newText = "-$text"
            textView?.text = newText
        } else {
            textView?.text = textView?.text?.removePrefix("-")
        }
    }

    private fun deleteLastDigit() {
        handleError()
        val text = textView?.text.toString()
        val length = text.length
        if (length == 1 || (length == 2 && text[0] == '-')) {
            textView?.text = "0"
            return
        }
        val newText = text.removeRange(length - 1, length)
        textView?.text = newText
    }

    private fun appendDot() {
        handleError()
        val text = textView?.text.toString()
        if (text.contains('.')) {
            return
        }
        val newText = "${text}."
        textView?.text = newText
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun onAddSignPressed() {
        handleError()
        if (activeOperation != OperationType.NONE) {
            handleLastOperation()
        }
        operationTextView?.text = "+"
        preTextView?.text = textView?.text
        textView?.text = "0"
        activeOperation = OperationType.ADD
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun onSubtructPressed() {
        handleError()
        if (activeOperation != OperationType.NONE) {
            handleLastOperation()
        }
        operationTextView?.text = "-"
        preTextView?.text = textView?.text
        textView?.text = "0"
        activeOperation = OperationType.SUBTRUCT
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun onMultiplyPressed() {
        handleError()
        if (activeOperation != OperationType.NONE) {
            handleLastOperation()
        }
        operationTextView?.text = "X"
        preTextView?.text = textView?.text
        textView?.text = "0"
        activeOperation = OperationType.MULTIPLY
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun onDividePressed() {
        handleError()
        if (activeOperation != OperationType.NONE) {
            handleLastOperation()
        }
        operationTextView?.text = "÷"
        preTextView?.text = textView?.text
        textView?.text = "0"
        activeOperation = OperationType.DIVIDE
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun onPercentagePressed() {
        handleError()
        if (activeOperation != OperationType.NONE) {
            handleLastOperation()
        }
        operationTextView?.text = "%"
        preTextView?.text = textView?.text
        textView?.text = "0"
        activeOperation = OperationType.PERCENTAGE
        horizontalScrollView?.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
    }

    private fun onEquationPressed() {
        handleError()
        if (activeOperation == OperationType.NONE) {
            return
        }
        handleLastOperation()
        preTextView?.text = ""
        operationTextView?.text = ""
        activeOperation = OperationType.NONE
    }

    private fun handleLastOperation() {
        when (activeOperation) {
            OperationType.NONE -> return
            OperationType.ADD -> {
                val value = preTextView?.text.toString().toBigDecimal() + textView?.text.toString().toBigDecimal()
                textView?.text = "$value"
            }
            OperationType.SUBTRUCT -> {
                val value = preTextView?.text.toString().toBigDecimal() - textView?.text.toString().toBigDecimal()
                textView?.text = "$value"
            }
            OperationType.MULTIPLY -> {
                val value = preTextView?.text.toString().toBigDecimal() * textView?.text.toString().toBigDecimal()
                textView?.text = "$value"
            }
            OperationType.DIVIDE -> {
                if (textView?.text.toString().toBigDecimal() == BigDecimal.ZERO) {
                    onClearPressed()
                    textView?.text = activity.getString(R.string.error_value)
                }
                else
                {
                    val value: Double = preTextView?.text.toString().toDouble() / textView?.text.toString().toDouble()
                    textView?.text = "$value"
                }
            }
            OperationType.PERCENTAGE -> {
                val value = preTextView?.text.toString().toDouble() * textView?.text.toString().toDouble() / 100.0
                textView?.text = "$value"
            }
        }
    }

    private fun handleError() {
        val text = textView?.text.toString()
        if (text == activity.getString(R.string.error_value) || preTextView?.text.toString() == activity.getString(R.string.error_value)) {
            onClearPressed()
        }
    }

}