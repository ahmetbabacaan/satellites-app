package com.ahmetbabacan.satellites.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmetbabacan.satellites.ui.adapters.SatelliteAdapter
import com.ahmetbabacan.satellites.data.models.Satellite

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.adapter = adapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        }
    }

    @JvmStatic
    @BindingAdapter("adapterSatelliteList")
    fun bindAdapterSatelliteList(view: RecyclerView, satelliteList: List<Satellite>?) {
        if (!satelliteList.isNullOrEmpty()) {
            (view.adapter as SatelliteAdapter).setSatelliteList(satelliteList)
        }
    }
}
