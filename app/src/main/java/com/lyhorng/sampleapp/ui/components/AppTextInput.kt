package com.lyhorng.sampleapp.ui.components

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lyhorng.sampleapp.R

class AppTextInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val tvLabel: TextView
    val tilInput: TextInputLayout
    val etInput: TextInputEditText

    private var required: Boolean = false

    init {
        orientation = VERTICAL

        LayoutInflater.from(context).inflate(
            R.layout.view_text_input,
            this,
            true
        )

        tvLabel = findViewById(R.id.tvLabel)
        tilInput = findViewById(R.id.tilInput)
        etInput = findViewById(R.id.etInput)

        context.obtainStyledAttributes(attrs, R.styleable.AppTextInput).apply {

            val label = getString(R.styleable.AppTextInput_inputLabel).orEmpty()
            val hint = getString(R.styleable.AppTextInput_inputHint).orEmpty()
            val icon = getDrawable(R.styleable.AppTextInput_inputStartIcon)
            val mode = getInt(R.styleable.AppTextInput_inputMode, 0)
            required = getBoolean(R.styleable.AppTextInput_inputRequired, false)
            val enabled = getBoolean(R.styleable.AppTextInput_inputEnabled, true)

            tvLabel.text = if (required && label.isNotEmpty()) {
                "$label *"
            } else {
                label
            }

            etInput.hint = hint
            tilInput.startIconDrawable = icon

            applyInputMode(mode)
            setInputEnabled(enabled)

            recycle()
        }
    }

    private fun applyInputMode(mode: Int) {
        when (mode) {
            1 -> {
                etInput.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                tilInput.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }

            2 -> {
                etInput.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                tilInput.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }

            3 -> {
                etInput.inputType = InputType.TYPE_CLASS_NUMBER
                tilInput.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }

            4 -> {
                etInput.inputType = InputType.TYPE_CLASS_PHONE
                tilInput.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }

            else -> {
                etInput.inputType = InputType.TYPE_CLASS_TEXT
                tilInput.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }
        }
    }

    fun getText(): String {
        return etInput.text.toString().trim()
    }

    fun setText(value: String?) {
        etInput.setText(value.orEmpty())
    }

    fun setError(message: String?) {
        tilInput.error = message
    }

    fun clearError() {
        tilInput.error = null
    }

    fun clearText() {
        etInput.text?.clear()
    }

    fun focus() {
        etInput.requestFocus()
    }

    fun isEmpty(): Boolean {
        return getText().isEmpty()
    }

    fun validateRequired(message: String = "This field is required"): Boolean {
        return if (required && isEmpty()) {
            setError(message)
            focus()
            false
        } else {
            clearError()
            true
        }
    }

    fun setInputEnabled(enabled: Boolean) {
        etInput.isEnabled = enabled
        tilInput.isEnabled = enabled
    }
}