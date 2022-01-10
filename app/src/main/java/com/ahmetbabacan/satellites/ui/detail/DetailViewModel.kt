package com.ahmetbabacan.satellites.ui.detail

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.ahmetbabacan.satellites.data.models.Position
import com.ahmetbabacan.satellites.data.models.Satellite
import com.ahmetbabacan.satellites.data.models.SatelliteDetail
import com.ahmetbabacan.satellites.util.Constants.argKeySatellite
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    state: SavedStateHandle
) : BindingViewModel() {

    var detailResponse: LiveData<SatelliteDetail> = MutableLiveData()
    var currentPosition: LiveData<Position> = MutableLiveData()
    var satelliteModel: MutableLiveData<Satellite> = MutableLiveData()

    val satellite = state.get<Satellite>(argKeySatellite)

    @get:Bindable
    var toastMessage: String? by bindingProperty("")
        private set

    @get:Bindable
    var isLoading: Boolean by bindingProperty(true)
        private set

    init {
        satelliteModel.value = satellite

        fetchDetail()
        fetchPositions()
    }

    fun fetchPositions() {
        currentPosition = detailRepository.positions(satelliteId = satellite!!.id,
            onStart = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { toastMessage = it })
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }

    fun fetchDetail() {
        detailResponse = detailRepository.satelliteDetail(satelliteId = satellite!!.id,
            onStart = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { toastMessage = it })
            .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }
}