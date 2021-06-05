package com.erdees.toyswap.view.fragments

import androidx.fragment.app.Fragment
import com.erdees.toyswap.databinding.FragmentChatBinding

class ChatFragment : Fragment(){
    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val TAG = "ChatFragment"
        fun newInstance() : ChatFragment = ChatFragment()
    }

}