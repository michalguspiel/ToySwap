package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdees.toyswap.databinding.FragmentBrowserBinding


class BrowserFragment : Fragment(){

    private var _binding : FragmentBrowserBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowserBinding.inflate(inflater,container,false)
        val view = binding.root



        return view
    }

    companion object {
        fun newInstance(): BrowserFragment = BrowserFragment()
        const val TAG = "MainFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}