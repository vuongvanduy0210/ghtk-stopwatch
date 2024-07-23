package com.duyvv.stopwatch.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.duyvv.stopwatch.R
import com.duyvv.stopwatch.databinding.ItemStopwatchBinding
import com.duyvv.stopwatch.domain.Stopwatch
import com.duyvv.stopwatch.utils.convertMillisToMMSS

class StopwatchAdapter(
    private val stopwatchListener: StopwatchListener
) : Adapter<StopwatchAdapter.StopwatchViewHolder>() {

    inner class StopwatchViewHolder(val binding: ItemStopwatchBinding) : ViewHolder(binding.root) {
        fun bind(stopwatch: Stopwatch) {
            binding.tvTime.text = convertMillisToMMSS(stopwatch.time)
        }
    }

    private val items = mutableListOf<Stopwatch>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Stopwatch>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun updateItem(stopwatch: Stopwatch) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {
        return StopwatchViewHolder(
            ItemStopwatchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.binding.apply {
            btnStart.setOnClickListener {
                if (!item.isRunning) {
                    btnStart.text = "Stop"
                    btnStart.setBackgroundColor(Color.RED)
                    stopwatchListener.onClickStart(item, position)
                } else {
                    btnStart.text = "Start"
                    btnStart.setBackgroundColor(
                        ResourcesCompat.getColor(root.resources, R.color.green, null)
                    )
                    stopwatchListener.onClickStop(item, position)
                }
            }

            btnReset.setOnClickListener {
                if (!item.isRunning)
                    stopwatchListener.onClickReset(item, position)
            }
        }

    }

    override fun getItemCount() = items.size
}

interface StopwatchListener {
    fun onClickStart(stopwatch: Stopwatch, position: Int)

    fun onClickStop(stopwatch: Stopwatch, position: Int)

    fun onClickReset(stopwatch: Stopwatch, position: Int)
}