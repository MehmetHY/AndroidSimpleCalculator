package com.example.androidcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var calculator: Calculator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calculator = Calculator(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        for (i in 0 until (calculator?.getItemCountToSave()!!)) {
            outState.putString(calculator?.getItemsKeysToSave()?.get(i), calculator?.getItemsContentsToSave()?.get(i))
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        calculator?.restoreState(savedInstanceState)
    }
}