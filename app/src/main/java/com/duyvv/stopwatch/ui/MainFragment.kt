package com.duyvv.stopwatch.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyvv.stopwatch.databinding.FragmentMainBinding
import com.duyvv.stopwatch.domain.Stopwatch
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment(), StopwatchListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var job: Job? = null

    private val stopwatchAdapter: StopwatchAdapter by lazy {
        StopwatchAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
    }

    private fun setup() {
        setupListStopwatch()
    }

    private fun setupListStopwatch() {
        binding.rcvStopwatch.apply {
            adapter = stopwatchAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        stopwatchAdapter.setItems(
            listOf(
                Stopwatch(0, false)
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickStart(stopwatch: Stopwatch, position: Int) {
        stopwatch.isRunning = true
        job = lifecycleScope.launch {
            while (true) {
                delay(1000)
                stopwatch.time += 1000
                stopwatchAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickStop(stopwatch: Stopwatch, position: Int) {
        stopwatch.isRunning = false
        job?.cancel()
        stopwatchAdapter.notifyDataSetChanged()
    }

    override fun onClickReset(stopwatch: Stopwatch, position: Int) {
        stopwatch.time = 0
        stopwatchAdapter.notifyDataSetChanged()
    }
}