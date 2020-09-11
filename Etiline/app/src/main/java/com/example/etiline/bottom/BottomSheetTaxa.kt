package com.example.etiline.bottom


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.etiline.*
import com.example.etiline.database.DataBaseAdapter
import com.example.etiline.ui.calcular.CalcularFragment
import com.example.etiline.ui.historico.HistoricoFragment
import com.example.etiline.ui.taxa.PaginaTaxa
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * Classe que vai criar o bottom sheet
 */
class BottomSheetTaxa
/**
 * Construtor
 *
 * @param idTaxa
 * @param aContext
 */(private val idTaxa: Int, context: Context) :
    BottomSheetDialogFragment() {
    /**
     * Identificação das variáveis globais
     */
    private var dbAdapter: DataBaseAdapter? = null

    /**
     * O que fazer quando é criado
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =
            inflater.inflate(R.layout.bottom_sheet_taxa_layout, container, false)

        val eliminar =
            v.findViewById<Button>(R.id.botao_eliminar)
        eliminar.setOnClickListener {
            dbAdapter = DataBaseAdapter(context)
            dbAdapter!!.abrir()
            dbAdapter!!.removerTaxa(idTaxa)
            Toast.makeText(context, "Item eliminado", Toast.LENGTH_LONG).show()
            dbAdapter!!.fechar()
            dismiss()
            context?.startActivity(Intent(context, MainActivity::class.java))
        }
        val ver = v.findViewById<Button>(R.id.botao_ver)
        ver.setOnClickListener {

            dismiss()
            context?.startActivity(
                Intent(context, PaginaTaxa::class.java).putExtra(
                    "id",
                    idTaxa
                )
            )


        }
        return v
    }

}
