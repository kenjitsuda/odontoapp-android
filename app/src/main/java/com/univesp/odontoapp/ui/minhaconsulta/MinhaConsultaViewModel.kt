package com.univesp.odontoapp.ui.minhaconsulta

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MinhaConsultaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Implementar verificar consultas marcadas"
    }
    val text: LiveData<String> = _text
}