package com.erdees.toyswap.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erdees.toyswap.databinding.MyItemsFragmentBinding


/**TODO THIS WILL BE A FRAGMENT WHERE USER CAN BROWSE HIS/HERS ITEMS AND TAKE THEM OUT OR CHANGE PRICE ETC.
 * */
class MyItemsFragment : Fragment() {

    private var _binding : MyItemsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyItemsFragmentBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    companion object{
        const val TAG = "MyItemsFragment"
        fun newInstance() : MyItemsFragment = MyItemsFragment()
    }
}