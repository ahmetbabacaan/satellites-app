package com.ahmetbabacan.satellites.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.ahmetbabacan.satellites.R
import com.ahmetbabacan.satellites.databinding.FragmentSearchBinding
import com.ahmetbabacan.satellites.ui.adapters.SatelliteAdapter
import com.ahmetbabacan.satellites.util.textChanges
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import java.util.*


@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val vm: SearchViewModel by viewModels()

    private var previousQuery: String? = null

    private var delay: Long = 600

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            lifecycleOwner = this@SearchFragment
            viewModel = vm
            adapter = SatelliteAdapter()
            fragmentSearchRvSatellites.setHasFixedSize(true)
            fragmentSearchRvSatellites.addItemDecoration( DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL))
        }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.queryData.asLiveData().observe(viewLifecycleOwner, {
            if (it != previousQuery) {
                previousQuery = it
                binding.adapter?.clearItems()
                vm.satelliteListLiveData.value?.clear()
            }
        })

        binding.fragmentSearchEtSearchView.textChanges()
            .debounce(delay)
            .onEach {
                vm.onSearchKeywordChanged(it.toString())
            }
            .launchIn(lifecycleScope)

    }
}