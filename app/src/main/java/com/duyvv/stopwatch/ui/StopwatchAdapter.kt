package com.duyvv.stopwatch.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.duyvv.stopwatch.R
import com.duyvv.stopwatch.databinding.ItemStopwatchBinding
import com.duyvv.stopwatch.domain.Stopwatch
import com.duyvv.stopwatch.utils.convertMillisToMMSS

@SuppressLint("NotifyDataSetChanged")
class StopwatchAdapter(
    private val stopwatchListener: StopwatchListener
) : RecyclerView.Adapter<StopwatchAdapter.StopwatchViewHolder>() {

    inner class StopwatchViewHolder(
        val binding: ItemStopwatchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stopwatch: Stopwatch) {
            binding.apply {
                tvTime.text = convertMillisToMMSS(stopwatch.time)
                updateButtonText(stopwatch.isRunning)
            }
        }

        fun updateButtonText(isRunning: Boolean) {
            binding.btnStart.apply {
                text = if (isRunning) "Stop" else "Start"
                setBackgroundColor(
                    if (isRunning) Color.RED else
                        ResourcesCompat.getColor(binding.root.resources, R.color.green, null)
                )
            }
            binding.btnReset.apply {
                isEnabled = !isRunning
                setBackgroundColor(
                    if (!isRunning)
                        ResourcesCompat.getColor(
                            binding.root.resources,
                            android.R.color.holo_blue_light,
                            null
                        )
                    else
                        Color.GRAY
                )
                setTextColor(Color.WHITE)
            }
        }
    }

    val items = mutableListOf<Stopwatch>()

    fun setItems(items: List<Stopwatch>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(stopwatch: Stopwatch) {
        items.add(stopwatch)
        notifyItemInserted(items.size - 1)
        notifyItemRangeChanged(items.size - 1, 1)
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
        val stopwatch = items[position]
        holder.bind(stopwatch)
        holder.binding.apply {
            btnStart.setOnClickListener {
                if (!stopwatch.isRunning) {
                    stopwatchListener.onStart(stopwatch, position)
                } else {
                    stopwatchListener.onStop(stopwatch, position)
                }
                holder.updateButtonText(stopwatch.isRunning)
            }

            btnReset.setOnClickListener {
                stopwatchListener.onReset(stopwatch, position)
            }
        }
    }

    override fun onBindViewHolder(
        holder: StopwatchViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val stopwatch = items[position]
        if (payloads.isEmpty())
            return super.onBindViewHolder(holder, position, payloads)

        when (payloads[0]) {
            START_FLAG, RESET_FLAG -> {
                holder.binding.apply {
                    tvTime.text = convertMillisToMMSS(stopwatch.time)
                }
            }
        }
    }

    override fun getItemCount() = items.size

    companion object {
        const val START_FLAG = "START_FLAG"
        const val STOP_FLAG = "STOP_FLAG"
        const val RESET_FLAG = "RESET_FLAG"
    }
}

interface StopwatchListener {

    fun onStart(stopwatch: Stopwatch, position: Int)

    fun onStop(stopwatch: Stopwatch, position: Int)

    fun onReset(stopwatch: Stopwatch, position: Int)
}
