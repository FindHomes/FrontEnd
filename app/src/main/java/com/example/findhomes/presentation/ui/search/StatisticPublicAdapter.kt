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
import com.example.findhomes.databinding.ItemStatisticsPublicBinding

class StatisticPublicAdapter(val context: Context) : ListAdapter<GraphDataResponse, StatisticPublicAdapter.ViewHolder>(
    DiffCallback()
) {
    inner class ViewHolder(private val binding: ItemStatisticsPublicBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: GraphDataResponse) {
            binding.statisticsTvData1.text = item.dataName
            initGraph(item)
        }

        private fun initGraph(response: GraphDataResponse) {
            val mainColor = ContextCompat.getColor(context, R.color.button_color)
            val backgroundColor = ContextCompat.getColor(context, R.color.body_5)
            val barChart = binding.statisticsBcPublic
            // 차트 회색 배경 설정 (default = false)
            barChart.setDrawGridBackground(true)
            barChart.setGridBackgroundColor(backgroundColor)

            // 차트 테두리 설정 (default = false)
            barChart.setDrawBorders(false)

            val description = Description()
            // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
            description.isEnabled = false
            barChart.description = description

            // X, Y 바의 애니메이션 효과
            barChart.animateY(1000)
            barChart.animateX(1000)

            // 바텀 좌표 값
            val xAxis: XAxis = barChart.xAxis

            // x축 위치 설정
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            // 그리드 선 수평 거리 설정
            xAxis.granularity = 1f
            // x축 텍스트 컬러 설정
            xAxis.textColor = Color.RED
            // x축 선 설정 (default = true)
            xAxis.setDrawAxisLine(false)
            // 격자선 설정 (default = true)
            xAxis.setDrawGridLines(false)

            val leftAxis: YAxis = barChart.axisLeft
            // 좌측 선 설정 (default = true)
            leftAxis.setDrawAxisLine(false)
            // 좌측 텍스트 컬러 설정
            leftAxis.textColor = Color.BLUE

            // 바차트의 타이틀
            barChart.legend.isEnabled = false

            val rightAxis: YAxis = barChart.axisRight
            // 오른쪽 축 텍스트 비활성화
            rightAxis.setDrawAxisLine(false)
            rightAxis.setDrawLabels(false)

            // Zoom In / Out 가능 여부 설정
            barChart.setScaleEnabled(false)

            val valueList = ArrayList<BarEntry>()

            // 데이터 예외 처리
            if (response.houseAndValues.isNotEmpty()) {
                for (i in 0 until response.houseAndValues.size) {
                    val houseValues = response.houseAndValues[i]
                    valueList.add(BarEntry(i.toFloat(), houseValues.values[0].value.toFloat()))
                }
            }

            val barDataSet = BarDataSet(valueList, "")
            // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
            barDataSet.setColor(mainColor)

            val data = BarData(barDataSet)
            barChart.data = data

            barChart.invalidate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsPublicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
