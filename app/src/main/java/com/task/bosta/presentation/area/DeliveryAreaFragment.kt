package com.task.bosta.presentation.area

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.task.bosta.databinding.FragmentDeliveryAreaBinding
import com.task.bosta.presentation.BostaApplication
import com.task.bosta.presentation.area.dto.CityUI
import com.task.bosta.presentation.area.viewmodel.DeliveryAreaViewModule
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeliveryAreaFragment : Fragment() {

    private lateinit var binding: FragmentDeliveryAreaBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DeliveryAreaViewModule

    @Inject
    lateinit var deliveryAreaAdapter: DeliveryAreaAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BostaApplication)
            .appComponent
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[DeliveryAreaViewModule::class.java]

        initRecyclerView()
        setupSearchListener()
        observeDeliveryAreas()

        deliveryAreaAdapter.onCityClicked = { cityId ->
            viewModel.toggleCityExpansion(cityId)
        }
    }

    private fun initRecyclerView() {
        binding.rvDeliveryArea.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = deliveryAreaAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setupSearchListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val query = editable.toString().trim()
                viewModel.filterCities(query)
            }
        })
    }

    private fun observeDeliveryAreas() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deliveryAreaStateFlow.collectLatest { state ->
                when (state) {
                    is DeliveryAreaUIState.Loading -> showLoading()
                    is DeliveryAreaUIState.OnSuccess -> handleSuccess(state.cities)
                    is DeliveryAreaUIState.OnFailed -> showError(state.msg)
                }
            }
        }
    }

    private fun handleSuccess(cities: List<CityUI>) {
        binding.apply {
            loadingProgress.visibility = View.GONE
            rvDeliveryArea.visibility = View.VISIBLE
            ivError.visibility = View.GONE
            tvError.visibility = View.GONE
        }

        val items = buildItemsList(cities)
        deliveryAreaAdapter.submitList(items)
    }

    private fun buildItemsList(cities: List<CityUI>): List<DeliveryAreaItem> {
        val items = mutableListOf<DeliveryAreaItem>()
        cities.forEach { city ->
            items.add(DeliveryAreaItem.CityItem(city))
            if (city.isExpanded) {
                items.addAll(city.districts.map { DeliveryAreaItem.DistrictItem(it) })
            }
        }
        return items
    }

    private fun showLoading() {
        binding.apply {
            loadingProgress.visibility = View.VISIBLE
            rvDeliveryArea.visibility = View.GONE
            ivError.visibility = View.GONE
            tvError.visibility = View.GONE
        }
    }

    private fun showError(errorMsg: String) {
        binding.apply {
            loadingProgress.visibility = View.GONE
            rvDeliveryArea.visibility = View.GONE
            ivError.visibility = View.VISIBLE
            tvError.visibility = View.VISIBLE
            tvError.text = errorMsg
        }
    }
}