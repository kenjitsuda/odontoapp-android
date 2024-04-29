package com.univesp.odontoapp.ui.horarios

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.univesp.odontoapp.R
import com.univesp.odontoapp.adapter.HorarioAdapter
import com.univesp.odontoapp.databinding.FragmentHorariosBinding


class HorariosFragment: Fragment() {

    private var _binding: FragmentHorariosBinding? = null

    private val binding get() = _binding!!

    private lateinit var horario: String
    private lateinit var profissional: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val horariosViewModel =
            ViewModelProvider(this)[HorariosViewModel::class.java]

        _binding = FragmentHorariosBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profissional = arguments?.getString("profissional").toString()
        binding.profissional.text = profissional

        val horarios: Array<String> = arrayOf("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30")
        val listAdapter = HorarioAdapter(requireActivity(), horarios)
        binding.listaHorario.adapter = listAdapter

        binding.listaHorario.setOnItemClickListener { parent, view, position, id ->
            binding.textProfissional.text = profissional
            horario = parent.adapter.getItem(position).toString()
            binding.textHorario.text = horario
        }

        binding.continuar.setOnClickListener {
            val alertDialog1 = AlertDialog.Builder(requireActivity())

            alertDialog1
                .setMessage("Consulta agendada")
                .setIcon(R.drawable.baseline_calendar_month_24)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                    val nav = findMyNavController()
                    nav.navigate(R.id.action_nav_horarios_to_nav_home)
                }

            val alert = alertDialog1.create()
            alert.show()
            val textview = alert.findViewById<TextView>(android.R.id.message)
            textview!!.gravity = Gravity.CENTER
            textview.setTextColor(resources.getColor(R.color.teal_700))
            alert.show()
        }

        return root
    }

    private fun findMyNavController(): NavController {
        return Navigation.findNavController(binding.root)
    }
}