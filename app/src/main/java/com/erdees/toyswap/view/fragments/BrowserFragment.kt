package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erdees.toyswap.databinding.FragmentBrowserBinding
import com.erdees.toyswap.view.adapters.BrowserFragmentRVAdapter
import com.erdees.toyswap.viewModel.BrowserFragmentRvAdapterViewModel
import com.erdees.toyswap.viewModel.BrowserFragmentViewModel


class BrowserFragment : Fragment(){

    private var _binding : FragmentBrowserBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : BrowserFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowserBinding.inflate(inflater,container,false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(BrowserFragmentViewModel::class.java)
        val rvViewModel = ViewModelProvider(this).get(BrowserFragmentRvAdapterViewModel::class.java)

        val options = viewModel.getOptions(this)

        val adapter = BrowserFragmentRVAdapter(options,requireActivity(),parentFragmentManager,rvViewModel)
        binding.browserFragmentRV.adapter = adapter
        binding.browserFragmentRV.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

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