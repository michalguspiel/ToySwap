package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdees.toyswap.databinding.FragmentAddItemBinding

class AddItemFragment : Fragment() {

    private var _binding : FragmentAddItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater,container,false)
        val view = binding.root



        return view
    }

    companion object {
        fun newInstance(): AddItemFragment = AddItemFragment()
        const val TAG = "AddItemFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}