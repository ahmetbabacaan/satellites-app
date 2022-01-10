package com.ahmetbabacan.satellites.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ahmetbabacan.satellites.R
import com.ahmetbabacan.satellites.databinding.FragmentDetailBinding
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val vm: DetailViewModel by viewModels()

    val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            lifecycleOwner = this@DetailFragment
            viewModel = vm
            satellite = args.satellite
            fragmentDetailToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }.root


    }


}