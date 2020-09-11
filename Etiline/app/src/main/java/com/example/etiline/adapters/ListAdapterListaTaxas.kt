package com.example.etiline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.etiline.R
import com.example.etiline.bottom.BottomSheetTaxa
import com.example.etiline.entidades.Taxa

class ListAdapterListaTaxas(
    context: Context,
    val fragmentManager: FragmentManager,
    val resource: Int,
    objects: MutableList<Taxa>
) :
    ArrayAdapter<Taxa>(context, resource, objects) {


    class ViewHolder(var hData: TextView, var hValor: TextView)

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        var convertView = convertView
        val id = getItem(position)!!.id
        val data = getItem(position)!!.data
        val valor = getItem(position)!!.taxaFinal
        val holder: ListAdapterListaTaxas.ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            convertView = inflater.inflate(resource, parent, false)
            holder = ViewHolder(
                convertView.findViewById<View>(R.id.data_taxa) as TextView,
                convertView.findViewById<View>(R.id.valor_taxa) as TextView
            )
            convertView.tag = holder
        } else {
            holder = convertView.tag as ListAdapterListaTaxas.ViewHolder
        }

        holder.hData.text = data
        holder.hValor.text = valor.toString()


        convertView!!.setOnClickListener {
            val bottomSheetTaxa = BottomSheetTaxa(id, context)
            bottomSheetTaxa.show(fragmentManager, "Histórico")
        }
        return convertView
    }

}