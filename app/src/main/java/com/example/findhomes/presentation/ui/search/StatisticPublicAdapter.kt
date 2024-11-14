package com.example.findhomes.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
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
import com.example.findhomes.databinding.ItemStatisticsPublicBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class StatisticPublicAdapter(val context: Context) : ListAdapter<GraphDataResponse, StatisticPublicAdapter.ViewHolder>(
    DiffCallback()
) {
    inner class ViewHolder(private val binding: ItemStatisticsPublicBinding) : RecyclerView.ViewHolder(binding.root) {
        private var selectedBarIndex = -1

        @SuppressLint("SetTextI18n")
        fun bind(item: GraphDataResponse) {
            binding.statisticsTvData1.text = item.dataName
            initGraph(item)
        }

        private fun initGraph(response: GraphDataResponse) {
            val mainColor = ContextCompat.getColor(context, R.color.button_color)
            val backgroundColor = ContextCompat.getColor(context, R.color.body_5)
            val barChart = binding.statisticsBcPublic

            barChart.let { chart ->
                // 공통 차트 설정
                setupChart(chart, backgroundColor)

                // X축 설정
                setupXAxis(chart.xAxis)

                // Y축 설정
                setupYAxis(chart.axisLeft, chart.axisRight)

                // 데이터 설정 및 애니메이션
                setupDataAndAnimation(chart, response)

                // 막대 클릭 리스너 설정
                chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        selectedBarIndex = e?.x?.toInt() ?: -1  // 선택된 막대의 인덱스 업데이트
                        chart.highlightValue(h)  // 막대 하이라이트
                        chart.invalidate()  // 차트 갱신
                    }

                    override fun onNothingSelected() {
                        selectedBarIndex = -1  // 선택 해제
                        chart.invalidate()  // 차트 갱신
                    }
                })
            }

            barChart.invalidate()
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
                textColor = Color.BLACK
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
        }

        private fun setupYAxis(leftAxis: YAxis, rightAxis: YAxis) {
            leftAxis.apply {
                setDrawAxisLine(false)
                textColor = Color.BLACK
            }

            rightAxis.apply {
                setDrawAxisLine(false)
                setDrawLabels(false)
            }
        }

        private fun setupDataAndAnimation(chart: BarChart, response: GraphDataResponse) {
            val valueList = ArrayList<BarEntry>()

            response.houseAndValues.forEachIndexed { index, houseValue ->
                valueList.add(BarEntry(index.toFloat(), houseValue.values[chart.id - binding.statisticsBcPublic.id].value.toFloat()))
            }

            val barDataSet = BarDataSet(valueList, "").apply {
                setColor(ContextCompat.getColor(chart.context, R.color.button_color))
                valueFormatter = object : ValueFormatter() {
                    override fun getBarLabel(barEntry: BarEntry): String {
                        // 선택된 막대만 값 표시
                        return if (barEntry.x.toInt() == selectedBarIndex) barEntry.y.toString() else ""
                    }
                }
            }
            chart.data = BarData(barDataSet)
            chart.invalidate()
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
