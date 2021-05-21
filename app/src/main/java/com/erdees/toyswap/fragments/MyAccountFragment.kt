package com.erdees.toyswap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdees.toyswap.databinding.FragmentMyAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MyAccountFragment : Fragment() {

    private var _binding : FragmentMyAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyAccountBinding.inflate(inflater,container,false)
        val view = binding.root

        auth = Firebase.auth

        binding.button2.setOnClickListener {
        auth.signOut()
        }

        return view
    }

    companion object {
        fun newInstance(): MyAccountFragment = MyAccountFragment()
        const val TAG = "MyAccountFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}