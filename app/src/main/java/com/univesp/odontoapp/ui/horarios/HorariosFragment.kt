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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.univesp.odontoapp.R
import com.univesp.odontoapp.adapter.HorarioAdapter
import com.univesp.odontoapp.databinding.FragmentHorariosBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture


class HorariosFragment: Fragment() {

    private var _binding: FragmentHorariosBinding? = null

    private val binding get() = _binding!!

    private lateinit var horario: String
    private lateinit var profissional: String
    private lateinit var data: String
    private var profissionalId: Int = 0

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
        profissionalId = arguments?.getInt("profissionalId")!!
        data = arguments?.getString("data").toString()
        val horarios = arguments?.getString("agenda").toString()
        val jsonHorarios = JSONArray(horarios)
        var horariosList: Array<String> = arrayOf()
        for (i in 0 until jsonHorarios.length()) {
            var value: String = jsonHorarios[i].toString()
            horariosList += value
        }
        binding.profissional.text = profissional

        val listAdapter = HorarioAdapter(requireActivity(), horariosList)
        binding.listaHorario.adapter = listAdapter

        binding.listaHorario.setOnItemClickListener { parent, view, position, id ->
            binding.textProfissional.text = profissional
            horario = parent.adapter.getItem(position).toString()
            binding.textHorario.text = horario
        }

        binding.continuar.setOnClickListener {

            lifecycleScope.launch {
                val jsonRequest = JSONObject()
                jsonRequest.put("data", data)
                jsonRequest.put("horario", horario)
                jsonRequest.put("nomePaciente", binding.nomePaciente.text)
                jsonRequest.put("profissionalID", profissionalId)
                val response = request(jsonRequest.toString())
            }

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

    internal class CallbackFuture : CompletableFuture<Response?>(),
        Callback {

        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            super.complete(response)
        }
    }

    private suspend fun request(json: String): String {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

            // put your json here
            val requestBody = RequestBody.create(JSON, json)
            val request = Request.Builder()
                .url("http://10.0.2.2:8080/v1/consulta")
                .post(requestBody)
                .build()
            val future = CallbackFuture()
            client.newCall(request).enqueue(future)
            val response = future.get()
            val body = response!!.body!!.string()
            body
        }
    }

    private fun findMyNavController(): NavController {
        return Navigation.findNavController(binding.root)
    }
}