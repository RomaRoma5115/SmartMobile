package com.example.smartmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobile.R
import com.example.smartmobile.model.Tariff

class TariffAdminAdapter(
    private val list: List<Tariff>,
): RecyclerView.Adapter<TariffAdminAdapter.ViewHolder>() {

    var onUpdateClick : ((Tariff) -> Unit)? = null

    var onDeleteClick : ((Tariff) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val countSms: TextView = itemView.findViewById(R.id.countSms)
        val packageInternet: TextView = itemView.findViewById(R.id.packageInternet)
        val countMinutes: TextView = itemView.findViewById(R.id.countMinutes)
        val updateButton: Button = itemView.findViewById(R.id.updateTariffButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteTariffButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffAdminAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tariff_recycler_item_admin,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TariffAdminAdapter.ViewHolder, position: Int) {

        val tariff = list[position]
        holder.name.text = tariff.name
        holder.description.text = tariff.description
        holder.countMinutes.text = tariff.countMinutes
        holder.countSms.text = tariff.countSms
        holder.packageInternet.text = tariff.packageInternet+" Гб"

        holder.updateButton.setOnClickListener {
            onUpdateClick!!.invoke(list[position])
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick!!.invoke(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}