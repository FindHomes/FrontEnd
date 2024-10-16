package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findhomes.data.model.GraphDataResponse
import com.example.findhomes.data.model.SearchCompleteResponse
import com.example.findhomes.databinding.ItemStatisticsInfoBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class StatisticsDataAdapter() : ListAdapter<GraphDataResponse, StatisticsDataAdapter.ViewHolder>(
    DiffCallback()
) {
    inner class ViewHolder(private val binding: ItemStatisticsInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: GraphDataResponse) {
            binding.statisticsTvData1.text = item.dataName
            initGraph(item)
        }

        private fun initGraph(response: GraphDataResponse) {
            val barChart = binding.statisticsBcFacility1
            val barChart2 = binding.statisticsBcFacility2
            // 차트 회색 배경 설정 (default = false)
            barChart.setDrawGridBackground(false)
            barChart2.setDrawGridBackground(false)
            // 막대 그림자 설정 (default = false)
            barChart.setDrawBarShadow(false)
            barChart2.setDrawBarShadow(false)
            // 차트 테두리 설정 (default = false)
            barChart.setDrawBorders(false)
            barChart2.setDrawBorders(false)

            val description = Description()
            // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
            description.isEnabled = false
            barChart.description = description
            barChart2.description = description

            // X, Y 바의 애니메이션 효과
            barChart.animateY(1000)
            barChart.animateX(1000)
            barChart2.animateY(1000)
            barChart2.animateX(1000)

            // 바텀 좌표 값
            val xAxis: XAxis = barChart.xAxis
            val xAxis2: XAxis = barChart2.xAxis

            // x축 위치 설정
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis2.position = XAxis.XAxisPosition.BOTTOM
            // 그리드 선 수평 거리 설정
            xAxis.granularity = 1f
            xAxis2.granularity = 1f
            // x축 텍스트 컬러 설정
            xAxis.textColor = Color.RED
            xAxis2.textColor = Color.RED
            // x축 선 설정 (default = true)
            xAxis.setDrawAxisLine(false)
            xAxis2.setDrawAxisLine(false)
            // 격자선 설정 (default = true)
            xAxis.setDrawGridLines(false)
            xAxis2.setDrawGridLines(false)

            val leftAxis: YAxis = barChart.axisLeft
            val leftAxis2: YAxis = barChart2.axisLeft
            // 좌측 선 설정 (default = true)
            leftAxis.setDrawAxisLine(false)
            leftAxis2.setDrawAxisLine(false)
            // 좌측 텍스트 컬러 설정
            leftAxis.textColor = Color.BLUE
            leftAxis2.textColor = Color.BLUE

            // 바차트의 타이틀
            val legend: Legend = barChart.legend
            val legend2: Legend = barChart2.legend
            // 범례 모양 설정 (default = 정사각형)
            legend.form = Legend.LegendForm.LINE
            legend2.form = Legend.LegendForm.LINE
            // 타이틀 텍스트 사이즈 설정
            legend.textSize = 20f
            legend2.textSize = 20f
            // 타이틀 텍스트 컬러 설정
            legend.textColor = Color.BLACK
            legend2.textColor = Color.BLACK
            // 범례 위치 설정
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend2.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend2.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            // 범례 방향 설정
            legend.orientation = Legend.LegendOrientation.HORIZONTAL
            legend2.orientation = Legend.LegendOrientation.HORIZONTAL
            // 차트 내부 범례 위치하게 함 (default = false)
            legend.setDrawInside(false)
            legend2.setDrawInside(false)

            // Zoom In / Out 가능 여부 설정
            barChart.setScaleEnabled(false)
            barChart2.setScaleEnabled(false)

            val valueList = ArrayList<BarEntry>()
            val valueList2 = ArrayList<BarEntry>()

            // 데이터 예외 처리
            if (response.houseAndValues.isNotEmpty()) {
                for (i in 0 until response.houseAndValues.size) {
                    val houseValues = response.houseAndValues[i]
                    valueList.add(BarEntry(i.toFloat(), houseValues.values[0].value.toFloat()))
                    valueList2.add(BarEntry(i.toFloat(), houseValues.values[1].value.toFloat()))
                }
            }

            val barDataSet = BarDataSet(valueList, "")
            val barDataSet2 = BarDataSet(valueList2, "")
            // 바 색상 설정 (ColorTemplate.LIBERTY_COLORS)
            barDataSet.setColors(
                Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
                Color.rgb(118, 174, 175), Color.rgb(42, 109, 130))

            val data = BarData(barDataSet)
            val data2 = BarData(barDataSet2)
            barChart.data = data
            barChart2.data = data2

            barChart.invalidate()
            barChart2.invalidate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStatisticsInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
