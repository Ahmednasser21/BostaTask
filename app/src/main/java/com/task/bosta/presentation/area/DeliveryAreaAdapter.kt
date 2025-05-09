package com.task.bosta.presentation.area

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.task.bosta.R
import com.task.bosta.databinding.DeliveryAreaItemBinding
import com.task.bosta.databinding.DistrictItemBinding
import com.task.bosta.presentation.area.dto.CityUI
import com.task.bosta.presentation.area.dto.DistrictUI
import javax.inject.Inject

class DeliveryAreaAdapter @Inject constructor() :
    ListAdapter<DeliveryAreaItem, RecyclerView.ViewHolder>(DeliveryAreaDiffUtil()) {

    var onCityClicked: ((String) -> Unit)? = null

    companion object {
        private const val VIEW_TYPE_CITY = 0
        private const val VIEW_TYPE_DISTRICT = 1
    }

    inner class CityViewHolder(private val binding: DeliveryAreaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(city: CityUI) {
            binding.tvDeliveryArea.text = city.cityName

            binding.ivArrow.setImageResource(
                if (city.isExpanded) R.drawable.arrow_drop_up_icon
                else R.drawable.arrow_drop_down_icon
            )

            binding.root.setOnClickListener {
                onCityClicked?.invoke(city.cityId)
            }
        }
    }

    inner class DistrictViewHolder(private val binding: DistrictItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(district: DistrictUI) {
            binding.tvDistrictName.text = district.districtName

            val isCovered = district.pickupAvailability
            val textColor = if (isCovered)
                binding.root.context.getColor(R.color.black)
            else
                binding.root.context.getColor(R.color.gray)

            binding.tvDistrictName.setTextColor(textColor)
            binding.tvCoverageStatus.visibility = if (isCovered) View.GONE else View.VISIBLE
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DeliveryAreaItem.CityItem -> VIEW_TYPE_CITY
            is DeliveryAreaItem.DistrictItem -> VIEW_TYPE_DISTRICT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CITY -> {
                val binding = DeliveryAreaItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                CityViewHolder(binding)
            }

            VIEW_TYPE_DISTRICT -> {
                val binding =
                    DistrictItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DistrictViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is DeliveryAreaItem.CityItem -> (holder as CityViewHolder).bind(item.city)
            is DeliveryAreaItem.DistrictItem -> (holder as DistrictViewHolder).bind(item.district)
        }
    }

}