package com.example.tipcalculator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.example.tipcalculator.databinding.ActivityMainBinding
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener {
            calculateTip()
        }

        binding.costInputEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
    }

    private fun calculateTip() {
        val costOfService: Double? = binding.costInputEditText.text.toString().toDoubleOrNull()
        val tipAmount: TextView = binding.tipAmount
        if(costOfService == null) {
            tipAmount.text = ""
            tipAmount.visibility = View.INVISIBLE
            return
        } else if(costOfService == 0.0) {
            displayTip(0.0, tipAmount)
            return
        }
        val selectedQuality: Double = when(binding.radioGroup.checkedRadioButtonId) {
            R.id.amazing_rating -> 0.20
            R.id.good_rating -> 0.18
            else -> 0.15
        }
        val tip: Double = when(binding.roundSwitch.isChecked) {
            false -> costOfService*selectedQuality
            true -> ceil(costOfService*selectedQuality)
        }
        displayTip(tip, tipAmount)
    }

    private fun displayTip(tip: Double, tipAmount: TextView) {
        tipAmount.text = getString(R.string.tip_amount, NumberFormat.getCurrencyInstance().format(tip))
        tipAmount.visibility = View.VISIBLE
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}