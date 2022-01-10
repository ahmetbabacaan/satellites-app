package com.ahmetbabacan.satellites.ui.search

import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.ahmetbabacan.satellites.R
import com.ahmetbabacan.satellites.data.models.Satellite
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    BindingViewModel() {

    private lateinit var satelliteListResponse: LiveData<ArrayList<Satellite>>
    var satelliteListLiveData: MutableLiveData<MutableList<Satellite>> =
        MutableLiveData<MutableList<Satellite>>()

    val queryData: MutableStateFlow<String?> =
        MutableStateFlow(null)

    private var lastQuery: String? = null

    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var isError: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var errorText: Int by bindingProperty(R.string.error_no_satellite_found)
        private set

    @get:Bindable
    var toast: String? by bindingProperty(null)
        private set

    init {
        fetchList()
    }

    fun fetchList() {

        satelliteListResponse = queryData.asLiveData()
            .switchMap { queryAndIndex ->
                searchRepository.searchSatellites(
                    queryAndIndex,
                    onStart = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { toast = it }
                ).asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
            }

        viewModelScope.launch {
            satelliteListResponse.asFlow().collect { response ->
                if (response.isNullOrEmpty()) {
                    errorText = R.string.error_no_satellite_found
                    isError = true
                    isLoading = false
                } else {
                    satelliteListLiveData.value = response
                }
            }
        }
    }

    @MainThread
    fun onSearchKeywordChanged(query: String?) {
        query?.let {
            isError = false
            if(lastQuery!=null){
                if(it.length > 2){
                    queryData.value = it
                }else{
                    queryData.value = null
                    lastQuery = null
                }
            }else {
                if (it.isBlank()) {
                    queryData.value = null
                } else {
                    if(it.length > 2) {
                        queryData.value = it
                        lastQuery = it
                    }
                }
            }
        }

    }
}