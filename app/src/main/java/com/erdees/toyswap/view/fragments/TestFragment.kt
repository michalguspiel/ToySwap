package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdees.toyswap.databinding.FragmentBinding

class TestFragment: Fragment() {

    private var _binding : FragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.helloFragmentTV.text = "Hello from fragment view binding!"




        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "TestFragment"
        fun newInstance(): TestFragment = TestFragment()
    }


}