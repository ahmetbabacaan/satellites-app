package com.ahmetbabacan.satellites.ui.adapters

import android.graphics.Color
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.ahmetbabacan.satellites.R
import com.ahmetbabacan.satellites.data.models.Satellite
import com.ahmetbabacan.satellites.databinding.ItemSatelliteBinding
import com.ahmetbabacan.satellites.ui.search.SearchFragmentDirections
import com.skydoves.bindables.binding

class SatelliteAdapter : RecyclerView.Adapter<SatelliteAdapter.SatelliteViewHolder>() {

    private val items: MutableList<Satellite> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        val binding = parent.binding<ItemSatelliteBinding>(R.layout.item_satellite)
        return SatelliteViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { pos -> pos != NO_POSITION }
                    ?: return@setOnClickListener
                it.findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToDetailFragment(items[position])
                )
            }
        }
    }

    fun setSatelliteList(satelliteList: List<Satellite>) {
        val previousItemSize = items.size
        items.clear()
        items.addAll(satelliteList)
        if (previousItemSize == 0) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(previousItemSize, satelliteList.size)
        }
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        holder.binding.apply {
            satellite = items[position]
            items[position].let {
                itemSatelliteTvTitle.text = it.name
                if (it.active) {
                    itemSatelliteIvStatus.setImageResource(R.drawable.green_circle)
                    itemSatelliteTvStatus.text =
                        holder.itemView.context.getString(R.string.status_active)
                    itemSatelliteTvTitle.setTextColor(Color.BLACK)
                    itemSatelliteTvStatus.setTextColor(Color.BLACK)
                } else {
                    itemSatelliteIvStatus.setImageResource(R.drawable.red_circle)
                    itemSatelliteTvStatus.text =
                        holder.itemView.context.getString(R.string.status_passive)
                    itemSatelliteTvStatus.setTextColor(Color.GRAY)
                    itemSatelliteTvTitle.setTextColor(Color.GRAY)
                }
            }
            executePendingBindings()
        }
    }

    override fun getItemCount() = items.size

    class SatelliteViewHolder(val binding: ItemSatelliteBinding) :
        RecyclerView.ViewHolder(binding.root)
}
