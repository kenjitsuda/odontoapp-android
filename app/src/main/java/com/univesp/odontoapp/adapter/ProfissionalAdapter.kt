package com.univesp.odontoapp.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.univesp.odontoapp.R

class ProfissionalAdapter(private val context: Activity, private val title: Array<String>, private val description: Array<String>, private val profissionalIdList:Array<Int>)
    : ArrayAdapter<String>(context, R.layout.profissional_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.profissional_list, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView
        val subtitleText = rowView.findViewById(R.id.description) as TextView
        val id = rowView.findViewById(R.id.profissionalid) as TextView

        titleText.text = title[position]
        subtitleText.text = description[position]
        id.text = profissionalIdList[position].toString()

        return rowView
    }
}