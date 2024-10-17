package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.model.GraphDataResponse
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.example.findhomes.R
import com.example.findhomes.databinding.ItemStatisticsFacilityBinding
import com.github.mikephil.charting.charts.BarChart

class StatisticFacilityAdapter(val context: Context) : ListAdapter<GraphDataResponse, StatisticFacilityAdapter.ViewHolder>(
    DiffCallback()
) {
    inner class ViewHolder(private val binding: ItemStatisticsFacilityBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: GraphDataResponse) {
            binding.statisticsTvData1.text = item.dataName
            initGraph(item)
        }

        private fun initGraph(response: GraphDataResponse) {
            val mainColor = ContextCompat.getColor(context, R.color.button_color)
            val backgroundColor = ContextCompat.getColor(context, R.color.body_5)
            val barCharts = listOf(binding.statisticsBcFacility1, binding.statisticsBcFacility2)

            barCharts.forEach { chart ->
                // 공통 차트 설정
                setupChart(chart, backgroundColor)

                // X축 설정
                setupXAxis(chart.xAxis)

                // Y축 설정
                setupYAxis(chart.axisLeft, chart.axisRight)

                // 데이터 설정 및 애니메이션
                setupDataAndAnimation(chart, response)
            }

            barCharts.forEach { it.invalidate() }
        }

        private fun setupChart(chart: BarChart, backgroundColor: Int) {
            chart.apply {
                setDrawGridBackground(true)
                setGridBackgroundColor(backgroundColor)
                setDrawBorders(false)
                legend.isEnabled = false
                description = Description().apply { isEnabled = false }
                setScaleEnabled(false)
                animateY(1000)
                animateX(1000)
            }
        }

        private fun setupXAxis(xAxis: XAxis) {
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                textColor = Color.RED
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
        }

        private fun setupYAxis(leftAxis: YAxis, rightAxis: YAxis) {
            leftAxis.apply {
                setDrawAxisLine(false)
                textColor = Color.BLUE
            }

            rightAxis.apply {
                setDrawAxisLine(false)
                setDrawLabels(false)
            }
        }

        private fun setupDataAndAnimation(chart: BarChart, response: GraphDataResponse) {
            val valueList = ArrayList<BarEntry>()

            if (response.houseAndValues.isNotEmpty()) {
                response.houseAndValues.forEachIndexed { index, houseValue ->
                    valueList.add(BarEntry(index.toFloat(), houseValue.values[chart.id - binding.statisticsBcFacility1.id].value.toFloat()))
                }
            }

            val barDataSet = BarDataSet(valueList, "").apply {
                setColor(ContextCompat.getColor(chart.context, R.color.button_color))
            }
            chart.data = BarData(barDataSet)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsFacilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))  // ListAdapter에서는 data.size 대신 getItem
    }

    class DiffCallback : DiffUtil.ItemCallback<GraphDataResponse>() {
        override fun areItemsTheSame(oldItem: GraphDataResponse, newItem: GraphDataResponse): Boolean {
            return oldItem.dataName== newItem.dataName  // 새로운 아이템과 이전 아이템을 비교할 수 있는 고윳 값
        }

        override fun areContentsTheSame(oldItem: GraphDataResponse, newItem: GraphDataResponse): Boolean {
            return oldItem.dataName == newItem.dataName
        }
    }
}
