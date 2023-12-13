package com.example.smartmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmobile.R
import com.example.smartmobile.model.Tariff

class TariffAdapter(
    private val list: List<Tariff>,
    private val onClick: (id : String?) -> Unit
): RecyclerView.Adapter<TariffAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val countSms: TextView = itemView.findViewById(R.id.countSms)
        val packageInternet: TextView = itemView.findViewById(R.id.packageInternet)
        val countMinutes: TextView = itemView.findViewById(R.id.countMinutes)
        private val informationButton: Button = itemView.findViewById(R.id.infoButton)
        init {
            informationButton.setOnClickListener {
                onClick(list[adapterPosition].tariffId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tariff_recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TariffAdapter.ViewHolder, position: Int) {
        val tariff = list[position]
        holder.name.text = tariff.name
        holder.description.text = tariff.description
        holder.countMinutes.text = tariff.countMinutes
        holder.countSms.text = tariff.countSms
        holder.packageInternet.text = tariff.packageInternet+" Гб"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}