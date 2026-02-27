package com.example.controllerapp.presentation.localidades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.controllerapp.data.remote.model.LocalidadInfo
import com.example.controllerapp.databinding.ItemLocalidadBinding

/**
 * Adapter para lista de localidades
 * Implementa el patrón ViewHolder y DiffUtil para optimización
 */
class LocalidadesAdapter : ListAdapter<LocalidadInfo, LocalidadesAdapter.LocalidadViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalidadViewHolder {
        val binding = ItemLocalidadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocalidadViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: LocalidadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    /**
     * ViewHolder para cada localidad
     */
    class LocalidadViewHolder(
        private val binding: ItemLocalidadBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(localidad: LocalidadInfo) {
            binding.tvAbreviacion.text = localidad.abreviacionCiudad ?: "N/A"
            binding.tvNombreCompleto.text = localidad.nombreCompleto ?: "Sin nombre"
        }
    }
    
    /**
     * DiffUtil callback para optimizar actualizaciones
     */
    class DiffCallback : DiffUtil.ItemCallback<LocalidadInfo>() {
        override fun areItemsTheSame(oldItem: LocalidadInfo, newItem: LocalidadInfo): Boolean {
            return oldItem.idLocalidad == newItem.idLocalidad
        }
        
        override fun areContentsTheSame(oldItem: LocalidadInfo, newItem: LocalidadInfo): Boolean {
            return oldItem == newItem
        }
    }
}
