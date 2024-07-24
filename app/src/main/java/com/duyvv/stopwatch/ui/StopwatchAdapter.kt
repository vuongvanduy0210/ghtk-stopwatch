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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
class StopwatchAdapter : RecyclerView.Adapter<StopwatchAdapter.StopwatchViewHolder>() {

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

    private val items = mutableListOf<Stopwatch>()

    fun setItems(items: List<Stopwatch>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(stopwatch: Stopwatch) {
        items.add(stopwatch)
        notifyItemInserted(items.size - 1)
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
                    startStop(stopwatch, this)
                } else {
                    stop(stopwatch)
                }
                holder.updateButtonText(stopwatch.isRunning)
            }

            btnReset.setOnClickListener {
                reset(stopwatch, this)
            }
        }
    }

    override fun onBindViewHolder(
        holder: StopwatchViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        val stopwatch = items[position]
        if (payloads.isEmpty()) return

        when (payloads[0]) {
            START_ALL_FLAG -> {
                holder.binding.apply {
                    if (!stopwatch.isRunning) {
                        startStop(stopwatch, this)
                    }
                    holder.updateButtonText(stopwatch.isRunning)
                }
            }

            STOP_ALL_FLAG -> {
                holder.binding.apply {
                    if (stopwatch.isRunning) {
                        stop(stopwatch)
                    }
                    holder.updateButtonText(stopwatch.isRunning)
                }
            }

            RESET_ALL_FLAG -> {
                holder.binding.apply {
                    if (!stopwatch.isRunning) {
                        reset(stopwatch, this)
                    }
                    holder.updateButtonText(stopwatch.isRunning)
                }
            }
        }
    }


    private fun startStop(stopwatch: Stopwatch, binding: ItemStopwatchBinding) {
        stopwatch.isRunning = true
        stopwatch.job = CoroutineScope(Dispatchers.Main).launch {
            while (stopwatch.isRunning) {
                delay(1000)
                stopwatch.time += 1000
                binding.tvTime.text = convertMillisToMMSS(stopwatch.time)
            }
        }
    }

    private fun stop(stopwatch: Stopwatch) {
        stopwatch.isRunning = false
        stopwatch.job?.cancel()
        stopwatch.job = null
    }

    private fun reset(stopwatch: Stopwatch, binding: ItemStopwatchBinding) {
        if (!stopwatch.isRunning) {
            stopwatch.time = 0
            stopwatch.job?.cancel()
            stopwatch.job = null
            binding.tvTime.text = convertMillisToMMSS(0)
        }
    }

    override fun getItemCount() = items.size

    fun startAll() {
        for (i in items.indices) {
            if (!items[i].isRunning) notifyItemChanged(i, START_ALL_FLAG)
        }
    }

    fun stopAll() {
        for (i in items.indices) {
            if (items[i].isRunning) notifyItemChanged(i, STOP_ALL_FLAG)
        }
    }

    fun resetAll() {
        for (i in items.indices) {
            if (!items[i].isRunning) notifyItemChanged(i, RESET_ALL_FLAG)
        }
    }

    companion object {
        const val START_ALL_FLAG = "START_ALL_FLAG"
        const val STOP_ALL_FLAG = "STOP_ALL_FLAG"
        const val RESET_ALL_FLAG = "RESET_ALL_FLAG"
    }
}
