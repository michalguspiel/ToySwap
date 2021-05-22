package com.erdees.toyswap.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdees.toyswap.activities.MainActivity
import com.erdees.toyswap.databinding.FragmentMyAccountBinding
import com.erdees.toyswap.fragments.dialogs.ChangeAddressDialog
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

        binding.changeAddressBtn.setOnClickListener {
            val changeAddressDialog = ChangeAddressDialog.newInstance()
            changeAddressDialog.show(parentFragmentManager,ChangeAddressDialog.TAG)
        }

        binding.logoutButton.setOnClickListener {
        auth.signOut()
            restartActivity()
        }

        return view
    }

    companion object {
        fun newInstance(): MyAccountFragment = MyAccountFragment()
        const val TAG = "MyAccountFragment"
    }

    private fun restartActivity(){
        val mainActivity = Intent(context,MainActivity::class.java)
        startActivity(mainActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}