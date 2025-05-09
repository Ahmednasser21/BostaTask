package com.task.bosta.presentation.area

import androidx.recyclerview.widget.DiffUtil

class DeliveryAreaDiffUtil:DiffUtil.ItemCallback<DeliveryAreaItem>() {
    override fun areItemsTheSame(oldItem: DeliveryAreaItem, newItem: DeliveryAreaItem): Boolean {
        return when {
            oldItem is DeliveryAreaItem.CityItem && newItem is DeliveryAreaItem.CityItem ->
                oldItem.city.cityId == newItem.city.cityId
            oldItem is DeliveryAreaItem.DistrictItem && newItem is DeliveryAreaItem.DistrictItem ->
                oldItem.district.districtId == newItem.district.districtId
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: DeliveryAreaItem, newItem: DeliveryAreaItem): Boolean {
        return oldItem == newItem
    }
}