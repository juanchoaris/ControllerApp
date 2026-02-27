package com.example.controllerapp.presentation.tablas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.controllerapp.data.local.entity.SchemaTableEntity
import com.example.controllerapp.databinding.ItemTablaBinding

/**
 * Adapter para lista de tablas
 * Implementa el patrón ViewHolder y DiffUtil para optimización
 */
class TablasAdapter : ListAdapter<SchemaTableEntity, TablasAdapter.TablaViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TablaViewHolder {
        val binding = ItemTablaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TablaViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TablaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    /**
     * ViewHolder para cada tabla
     */
    class TablaViewHolder(
        private val binding: ItemTablaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(tabla: SchemaTableEntity) {
            binding.tvTableName.text = tabla.tableName
            binding.tvTableDescription.text = tabla.tableDescription ?: "Sin descripción"
        }
    }
    
    /**
     * DiffUtil callback para optimizar actualizaciones
     */
    class DiffCallback : DiffUtil.ItemCallback<SchemaTableEntity>() {
        override fun areItemsTheSame(oldItem: SchemaTableEntity, newItem: SchemaTableEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: SchemaTableEntity, newItem: SchemaTableEntity): Boolean {
            return oldItem == newItem
        }
    }
}
