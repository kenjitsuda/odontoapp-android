package com.univesp.odontoapp.ui.agendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.univesp.odontoapp.R
import com.univesp.odontoapp.databinding.FragmentAgendarconsultaBinding

class AgendarConsultaFragment : Fragment() {

    private var _binding: FragmentAgendarconsultaBinding? = null

    private val binding get() = _binding!!

    private var dataSelecionada = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val agendarConsultaViewModel =
            ViewModelProvider(this).get(AgendarConsultaViewModel::class.java)

        _binding = FragmentAgendarconsultaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        agendarConsultaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.calendario.setOnDateChangeListener { view, year, month, dayOfMonth ->
            dataSelecionada = "$dayOfMonth/$month/$year"
        }

        binding.continuar.setOnClickListener {
            // Adicionar validação data, não permitir datas passadas
            // não permitir datas vazias ou nulas

            val nav = findMyNavController()
            nav.navigate(R.id.action_nav_agendarconsulta_to_nav_profissional)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.data -> {
                        // clearCompletedTasks()
                        true
                    }
                    R.id.profissional -> {
                        // loadTasks(true)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findMyNavController(): NavController {
        return Navigation.findNavController(binding.root)
    }
}