package com.univesp.odontoapp.ui.profissional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.univesp.odontoapp.R
import com.univesp.odontoapp.adapter.ProfissionalAdapter
import com.univesp.odontoapp.databinding.FragmentProfissionalBinding

class ProfissionalFragment : Fragment() {

    private var _binding: FragmentProfissionalBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profissionalViewModel =
            ViewModelProvider(this).get(ProfissionalViewModel::class.java)

        _binding = FragmentProfissionalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var profissionais: Array<String> = arrayOf("João", "Maria", "Ariel")
        var descricao: Array<String> = arrayOf("Especialista em implantes", "Especialista em prótese", "Clínico geral")
        val profissionalAdapter = ProfissionalAdapter(requireActivity(), profissionais, descricao)
        binding.listaProfissional.adapter = profissionalAdapter

        binding.listaProfissional.setOnItemClickListener { parent, view, position, id ->
            val profissional = parent.adapter.getItem(position)
            val bundle = Bundle()
            bundle.putString("profissional", profissional.toString())
            val nav = findMyNavController()
            nav.navigate(R.id.action_nav_profissional_to_nav_horarios, bundle)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val menuHost: MenuHost = requireActivity()

//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.main, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                // Handle the menu selection
//                return when (menuItem.itemId) {
//                    R.id.data -> {
//                        // clearCompletedTasks()
//                        true
//                    }
//                    R.id.profissional -> {
//                        // loadTasks(true)
//                        true
//                    }
//                    else -> false
//                }
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findMyNavController(): NavController {
        return Navigation.findNavController(binding.root)
    }
}