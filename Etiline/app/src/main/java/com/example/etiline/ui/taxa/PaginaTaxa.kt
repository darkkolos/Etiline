package com.example.etiline.ui.taxa

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.etiline.R
import com.example.etiline.database.DataBaseAdapter
import com.example.etiline.entidades.Taxa
import kotlinx.android.synthetic.main.activity_pagina_taxa.*

class PaginaTaxa : AppCompatActivity() {

    private lateinit var taxa: Taxa

    private lateinit var dbAdapter: DataBaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_taxa)

        voltar.setOnClickListener(View.OnClickListener { finish() })
        carregarValores()
    }


    private fun carregarValores() {
        obterTaxa()
        data.text = taxa.data
        percentagem.text = taxa.percentagem.toString()
        quantidade.text = taxa.quantidade.toString()
        peso.text = taxa.peso.toString()
        if (taxa.feminino) {
            sexo.text = "Feminino"
        } else {
            sexo.text = "Masculino"
        }
        if (taxa.refeicao) {
            jejum.text = "Com refeição"
        } else {
            jejum.text = "Em jejum"
        }
        taxa_valor.text = taxa.taxaFinal.toString()
        descricao.text = taxa.descricao
    }

    /**
     * Método para obter a taxa da bd
     */
    private fun obterTaxa() {
        dbAdapter = DataBaseAdapter(this)
        dbAdapter.abrir()
        val cursor: Cursor? =
            dbAdapter.obterTaxa(intent.getIntExtra("id", 0))
        if (cursor != null) {
            if (cursor.count == 0) {
                Toast.makeText(applicationContext, "Erro ao obter taxa", Toast.LENGTH_LONG)
                    .show()
                return
            }
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                taxa = Taxa(
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
                cursor.moveToNext()
            }
        }
        dbAdapter.fechar()
    }
}
