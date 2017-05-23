package com.alexkorrnd.base.extensions

import android.widget.EditText
import android.widget.TextView

var EditText.textWithSelection: CharSequence?
    get() = text
    set(value) {
        setText(value, TextView.BufferType.EDITABLE)
        value?.let { setSelection(it.length) }
    }
