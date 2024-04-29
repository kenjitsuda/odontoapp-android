package com.univesp.odontoapp.ui.agendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AgendarConsultaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Selecione uma data"
    }
    val text: LiveData<String> = _text


}