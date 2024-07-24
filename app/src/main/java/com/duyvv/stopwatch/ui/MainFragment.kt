package com.duyvv.stopwatch.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.duyvv.stopwatch.databinding.FragmentMainBinding
import com.duyvv.stopwatch.domain.Stopwatch

@SuppressLint("NotifyDataSetChanged")
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val stopwatchAdapter: StopwatchAdapter by lazy {
        StopwatchAdapter()
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

        setupListener()
    }

    private fun setupListener() {
        binding.btnAdd.setOnClickListener {
            stopwatchAdapter.addItem(
                Stopwatch(0, false)
            )
        }

        binding.btnStartAll.setOnClickListener {
            stopwatchAdapter.startAll()
        }

        binding.btnStopAll.setOnClickListener {
            stopwatchAdapter.stopAll()
        }

        binding.btnResetAll.setOnClickListener {
            stopwatchAdapter.resetAll()
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
            listOf(
                Stopwatch(0, false)
            )
        )
    }
}