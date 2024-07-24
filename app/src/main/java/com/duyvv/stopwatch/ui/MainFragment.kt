package com.duyvv.stopwatch.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyvv.stopwatch.databinding.FragmentMainBinding
import com.duyvv.stopwatch.domain.Stopwatch
import com.duyvv.stopwatch.ui.StopwatchAdapter.Companion.RESET_FLAG
import com.duyvv.stopwatch.ui.StopwatchAdapter.Companion.START_FLAG

@SuppressLint("NotifyDataSetChanged")
class MainFragment : Fragment(), StopwatchListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val stopwatchAdapter: StopwatchAdapter by lazy {
        StopwatchAdapter(this)
    }

    private val viewModel: MainViewModel by viewModels()

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

        setupListener()
    }

    private fun setupListener() {
        binding.btnAdd.setOnClickListener {
            stopwatchAdapter.addItem(
                Stopwatch(0, false)
            )
        }

        binding.btnStartAll.setOnClickListener {
            stopwatchAdapter.items.forEach {
                if (!it.isRunning) {
                    onStart(it, stopwatchAdapter.items.indexOf(it))
                }
            }
        }

        binding.btnStopAll.setOnClickListener {
            stopwatchAdapter.items.forEach {
                if (it.isRunning) {
                    onStop(it, stopwatchAdapter.items.indexOf(it))
                }
            }
        }

        binding.btnResetAll.setOnClickListener {
            stopwatchAdapter.items.forEach {
                if (!it.isRunning) {
                    onReset(it, stopwatchAdapter.items.indexOf(it))
                }
            }
        }
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
            listOf(Stopwatch(0, false))
        )
    }

    override fun onStart(stopwatch: Stopwatch, position: Int) {
        viewModel.startStopwatch(stopwatch) {
            stopwatchAdapter.notifyItemChanged(position, START_FLAG)
        }
        stopwatchAdapter.notifyItemChanged(position)
    }

    override fun onStop(stopwatch: Stopwatch, position: Int) {
        viewModel.stopStopwatch(stopwatch)
        stopwatchAdapter.notifyItemChanged(position)
    }

    override fun onReset(stopwatch: Stopwatch, position: Int) {
        viewModel.resetStopwatch(stopwatch) {
            stopwatchAdapter.notifyItemChanged(position, RESET_FLAG)
        }
        stopwatchAdapter.notifyItemChanged(position)
    }
}