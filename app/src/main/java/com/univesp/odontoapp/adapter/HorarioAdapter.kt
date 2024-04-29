package com.univesp.odontoapp.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.univesp.odontoapp.R

class HorarioAdapter(private val context: Activity, private val title: Array<String>)
    : ArrayAdapter<String>(context, R.layout.horario_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.horario_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView

        titleText.text = title[position]

        return rowView
    }
}