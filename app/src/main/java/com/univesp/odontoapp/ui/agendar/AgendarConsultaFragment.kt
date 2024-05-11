package com.univesp.odontoapp.ui.agendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.univesp.odontoapp.R
import com.univesp.odontoapp.adapter.ProfissionalAdapter
import com.univesp.odontoapp.databinding.FragmentAgendarconsultaBinding
import com.univesp.odontoapp.ui.profissional.ProfissionalFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture


class AgendarConsultaFragment : Fragment() {

    private var _binding: FragmentAgendarconsultaBinding? = null

    private val binding get() = _binding!!

    private var dataSelecionada = ""
    private lateinit var profissional: String
    private var profissionalId: Int = 0
    private lateinit var agenda: String

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
        profissional = arguments?.getString("profissional").toString()
        profissionalId = arguments?.getInt("profissionalId")!!


        binding.calendario.setOnDateChangeListener { view, year, month, dayOfMonth ->
            dataSelecionada = "$dayOfMonth/$month/$year"
            lifecycleScope.launch {
                val jsonRequest = JSONObject()
                jsonRequest.put("data", dataSelecionada)
                jsonRequest.put("profissional", profissionalId)
                agenda = request(jsonRequest.toString()).toString()
            }
        }

        binding.continuar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("profissional", profissional)
            bundle.putInt("profissionalId", profissionalId)
            bundle.putString("agenda", agenda)
            bundle.putString("data", dataSelecionada)
            val nav = findMyNavController()
            nav.navigate(R.id.action_nav_agendarconsulta_to_nav_horarios, bundle)
        }

        return root
    }

    internal class CallbackFuture : CompletableFuture<Response?>(),
        Callback {

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            super.complete(response)
        }
    }

    private suspend fun request(json: String): JSONArray {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

            // put your json here
            val requestBody = RequestBody.create(JSON, json)
            val request = Request.Builder()
                .url("http://10.0.2.2:8080/v1/agenda")
                .post(requestBody)
                .build()
            val future = CallbackFuture()
            client.newCall(request).enqueue(future)
            val response = future.get()
            val body = response!!.body!!.string()
            var jsonBody = JSONObject(body)
            var jsonArray = JSONArray(jsonBody.getString("agenda"))
            jsonArray
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun findMyNavController(): NavController {
        return Navigation.findNavController(binding.root)
    }
}