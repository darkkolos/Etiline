package com.example.etiline.ui.historico

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.etiline.R
import com.example.etiline.adapters.ListAdapterListaTaxas
import com.example.etiline.database.DataBaseAdapter
import com.example.etiline.entidades.Taxa
import kotlinx.android.synthetic.main.fragment_historico.*
import java.util.*

class HistoricoFragment : Fragment() {


    private lateinit var dbAdapter: DataBaseAdapter


    private lateinit var listaTaxas: ArrayList<Taxa>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_historico, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radio_data.setOnClickListener { carregarNovaLista() }
        radio_taxa.setOnClickListener { carregarNovaLista() }
        radio_ascendente.setOnClickListener { carregarNovaLista() }
        radio_descendente.setOnClickListener { carregarNovaLista() }

        carregarTaxas(true, true)

    }


    private fun carregarTaxas(tipo: Boolean, ordem: Boolean) {
        listaTaxas = ArrayList<Taxa>()
        dbAdapter = DataBaseAdapter(context)
        dbAdapter.abrir()
        val cursor: Cursor? = dbAdapter.obterTaxas(tipo, ordem)
        if (cursor != null) {
            if (cursor.count == 0) {
                Toast.makeText(
                    context,
                    "Não existe nada no histórico",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                listaTaxas.add(
                    Taxa(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4) > 0,
                        cursor.getInt(5) > 0,
                        cursor.getFloat(6),
                        cursor.getString(7),
                        cursor.getString(8)
                    )
                )
                cursor.moveToNext()
            }
        }
        dbAdapter.fechar()
        val listaTaxasListAdapter = context?.let {
            ListAdapterListaTaxas(
                it,
                parentFragmentManager,
                R.layout.adapter_view_ver_taxas,
                listaTaxas
            )
        }
        lista_taxas.adapter = listaTaxasListAdapter
    }

    private fun carregarNovaLista() {
        if (radio_data.isChecked) {
            if (radio_descendente.isChecked) {
                carregarTaxas(true, true)
            } else {
                carregarTaxas(true, false)
            }
        } else {
            if (radio_descendente.isChecked) {
                carregarTaxas(false, true)
            } else {
                carregarTaxas(false, false)
            }
        }
    }
}
