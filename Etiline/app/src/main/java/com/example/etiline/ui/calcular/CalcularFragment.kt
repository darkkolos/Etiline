package com.example.etiline.ui.calcular

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.etiline.R
import com.example.etiline.database.DataBaseAdapter
import com.example.etiline.entidades.Taxa
import kotlinx.android.synthetic.main.fragment_calcular.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.roundToInt

class CalcularFragment : Fragment(), View.OnClickListener {

    var dbAdapter: DataBaseAdapter? = null

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calcular, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(context)

        imagem_cerveja.setOnClickListener(this)
        imagem_vinho.setOnClickListener(this)
        imagem_shot.setOnClickListener(this)
        imagem_licor.setOnClickListener(this)


        barra_quantidade.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                val total = progress * 20
                quantidade.setText("" + total)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        quantidade.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus -> atualizarSeekBar() }

        quantidade.setOnEditorActionListener { v, actionId, event ->
            atualizarSeekBar()
            false
        }

        guardar_botao.setOnClickListener(this)

        limpar_botao.setOnClickListener(this)


    }

    private fun calcularDados() {
        val percentagemTotal: Int = percentagem.text.toString().toInt()
        val quantidadeMl: Int = quantidade.text.toString().toInt()
        val peso: Int = peso.text.toString().toInt()

        val volume = (percentagemTotal * (quantidadeMl * 0.01)).toFloat()

        val alcoolGramas = (volume * 0.8).toFloat()

        val coeficiente: Float = if (radio_em_refeicao.isChecked()) {
            if (radio_masculino.isChecked()) {
                0.7.toFloat()
            } else {
                0.6.toFloat()
            }
        } else {
            1.1.toFloat()
        }

        var taxaAlcolemia = alcoolGramas / (peso * coeficiente)

        val symbols =
            DecimalFormatSymbols(Locale.US)
        val df = DecimalFormat("#.##", symbols)
        df.roundingMode = RoundingMode.CEILING
        taxaAlcolemia = df.format(taxaAlcolemia.toDouble()).toFloat()

        var descricao = ""
        descricao = if (taxaAlcolemia < 0.5) {
            "Não há contraordenação. \nSorte a tua."
        } else {
            if (taxaAlcolemia < 0.8) {
                "Contra ordenação grave.\nCoima de 250€ a 1250€.\nInibição de conduzir de 1 mês a 1 ano."
            } else {
                if (taxaAlcolemia < 1.2) {
                    "Contra ordenação muito grave.\nCoima de 500€ a 2500€.\nInibição de conduzir de 2 meses a 2 anos."
                } else {
                    "Contra ordenação considerada crime.\nPena de prisão até 1 ano ou multa de 120 dias.\nProibição de conduzir de 3 meses a 3 anos."
                }
            }
        }

        guardarDados(
            Taxa(
                0,
                percentagemTotal,
                quantidadeMl,
                peso,
                radio_em_refeicao.isChecked,
                radio_masculino.isChecked,
                taxaAlcolemia,
                null,
                descricao
            )
        )
        val alert = AlertDialog.Builder(context)
        if (taxaAlcolemia >= 0.5) {
            alert.setTitle("Cuidado!")
        } else {
            alert.setTitle("Taxa aceitável")
        }
        alert.setMessage("Taxa = $taxaAlcolemia\n$descricao")

        alert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, whichButton -> return@OnClickListener })
        alert.show()
    }


    private fun guardarDados(taxa: Taxa) {
        dbAdapter = DataBaseAdapter(context)
        dbAdapter!!.abrir()
        val result = dbAdapter!!.inserirTaxa(taxa)
        if (result > 0) {
            Toast.makeText(context, "Taxa inserida com sucesso", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(context, "Erro ao inserir Taxa", Toast.LENGTH_LONG)
                .show()
        }
        dbAdapter!!.fechar()
        progressDialog.dismiss()
        limparInputs()
    }

    private fun verificarDados(): Boolean {
        var retornar = true
        try {
            if (percentagem.text == null) {
                retornar = false
                percentagem.error = "Dados incorretos"
            } else {
                if (percentagem.text.toString().toInt() in 101..0
                ) {
                    retornar = false
                    percentagem.error = "Valor tem de ser maior que 0 e menor que 100"
                }
            }
        } catch (e: NumberFormatException) {
            retornar = false
            percentagem.error = "Dados incorretos"
        } catch (nullPointerException: NullPointerException) {
            retornar = false
            percentagem.error = "Dados incorretos"
        }


        try {
            if (quantidade.text == null) {
                retornar = false
                quantidade.error = "Dados incorretos"
            } else {
                if (quantidade.text.toString().toInt() <= 0) {
                    retornar = false
                    quantidade.error = "Valor tem de ser maior do que 0"
                }
            }
        } catch (e: NumberFormatException) {
            retornar = false
            quantidade.error = "Dados incorretos"
        } catch (nullPointerException: NullPointerException) {
            retornar = false
            quantidade.error = "Dados incorretos"
        }


        try {
            if (peso.text == null) {
                retornar = false
                peso.error = "Dados incorretos"
            } else {
                if (peso.text.toString().toInt() <= 0) {
                    retornar = false
                    peso.error = "Valor tem de ser maior do que 0"
                }
            }
        } catch (e: NumberFormatException) {
            retornar = false
            peso.error = "Dados incorretos"
        } catch (nullPointerException: NullPointerException) {
            retornar = false
            peso.error = "Dados incorretos"
        }
        return retornar
    }

    override fun onClick(v: View?) {
        if (v === imagem_cerveja) {
            percentagem.setText("5")
        } else if (v === imagem_vinho) {
            percentagem.setText("12")
        } else if (v === imagem_licor) {
            percentagem.setText("20")
        } else if (v === imagem_shot) {
            percentagem.setText("40")
        } else if (v === guardar_botao) {
            if (verificarDados()) {
                progressDialog.setMessage("A calcular e guardar dados")
                progressDialog.show()
                calcularDados()
            }
        } else if (v === limpar_botao) {
            limparInputs()
        }
    }

    private fun limparInputs() {
        percentagem.text = null
        quantidade.text = null
        barra_quantidade.progress = 0
        peso.text = null
        radio_masculino.isChecked = true
        radio_em_refeicao.isChecked = true
    }

    private fun atualizarSeekBar() {
        if (quantidade.text != null && !quantidade.text.toString().equals("")) {
            barra_quantidade.max = 100
            val progresso =
                (quantidade.text.toString().toFloat() / 20).roundToInt().toInt()
            barra_quantidade.progress = progresso
        }
    }
}
