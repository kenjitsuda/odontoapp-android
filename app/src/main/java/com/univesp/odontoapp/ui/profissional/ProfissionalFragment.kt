package com.univesp.odontoapp.ui.profissional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.univesp.odontoapp.R
import com.univesp.odontoapp.adapter.ProfissionalAdapter
import com.univesp.odontoapp.databinding.FragmentProfissionalBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CompletableFuture


class ProfissionalFragment : Fragment() {

    private var _binding: FragmentProfissionalBinding? = null

    private val binding get() = _binding!!

    var profissionais: Array<String> = arrayOf()
    var descricao: Array<String> = arrayOf()
    var id: Array<Int> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profissionalViewModel =
            ViewModelProvider(this).get(ProfissionalViewModel::class.java)

        _binding = FragmentProfissionalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        lifecycleScope.launch {
            val jsonArray:JSONArray = request()
            popularLista(jsonArray)
            val profissionalAdapter = ProfissionalAdapter(requireActivity(), profissionais, descricao, id)
            binding.listaProfissional.adapter = profissionalAdapter
        }

        binding.listaProfissional.setOnItemClickListener { parent, view, position, id ->
            val profissional = parent.adapter.getItem(position)
            val bundle = Bundle()
            bundle.putString("profissional", profissional.toString())
            bundle.putInt("profissionalId", position+1)
            val nav = findMyNavController()
            nav.navigate(R.id.action_nav_profissional_to_nav_agendarconsulta, bundle)
        }

        return root
    }

    private suspend fun request(): JSONArray {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("http://10.0.2.2:8080/v1/profissional")
                .build()
            val future = CallbackFuture()
            client.newCall(request).enqueue(future)
            val response = future.get()
            val body = response!!.body!!.string()
            var jsonBody = JSONObject(body)
            var jsonArray = JSONArray(jsonBody.getString("listaProfissionais"))
            jsonArray
        }
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

    private fun popularLista(jsonArray: JSONArray) {
        for (i in 0 until jsonArray.length()) {
            var aux = jsonArray.getJSONObject(i)
            profissionais += aux.getString("Profissional")
            descricao += aux.getString("Especialidade")
            id += aux.getInt("ProfissionalID")
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