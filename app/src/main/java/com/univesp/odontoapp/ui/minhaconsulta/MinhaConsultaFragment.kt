package com.univesp.odontoapp.ui.minhaconsulta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.univesp.odontoapp.databinding.FragmentMinhaconsultaBinding

class MinhaConsultaFragment : Fragment() {

    private var _binding: FragmentMinhaconsultaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val minhaConsultaViewModel =
            ViewModelProvider(this).get(MinhaConsultaViewModel::class.java)

        _binding = FragmentMinhaconsultaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        minhaConsultaViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}