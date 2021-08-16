package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.erdees.toyswap.databinding.FragmentChatBinding
import com.erdees.toyswap.viewModel.ChatFragmentViewModel

class ChatFragment : Fragment(){

    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : ChatFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this).get(ChatFragmentViewModel::class.java)

        binding.imageView2.setOnClickListener {
            if(viewModel.getItemCategory(1).icon != null) {
                Glide.with(requireContext())
                    .load(
                        ContextCompat.getDrawable(
                            requireContext(),
                            viewModel.getItemCategory(1).icon!!
                        )
                    )
                    .into(binding.imageView2)
            }


        }


        return view
    }

    companion object {
        const val TAG = "ChatFragment"
        fun newInstance() : ChatFragment = ChatFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}