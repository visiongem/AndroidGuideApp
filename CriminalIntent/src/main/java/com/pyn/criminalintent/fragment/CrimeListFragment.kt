package com.pyn.criminalintent.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.pyn.criminalintent.R
import com.pyn.criminalintent.bean.Crime
import com.pyn.criminalintent.databinding.CrimeFragmentBinding
import com.pyn.criminalintent.databinding.CrimeListFragmentBinding
import com.pyn.criminalintent.databinding.ItemCrimeBinding
import com.pyn.criminalintent.databinding.ItemCrimePoliceBinding
import com.pyn.criminalintent.viewmodel.CrimeListViewModel

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var _binding: CrimeListFragmentBinding? = null
    private val mBinding get() = _binding!!
    private var mAdapter: CrimeAdapter? = null

    companion object {
        fun newInstance() = CrimeListFragment()
    }

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CrimeListFragmentBinding.inflate(layoutInflater, container, false)
        mBinding.recyclerViewCrimes.layoutManager = LinearLayoutManager(context)
        updateUI()
        return mBinding.root
    }

    private fun updateUI() {
        var crimes = crimeListViewModel.crimes
        mAdapter = CrimeAdapter(crimes)
        mBinding.recyclerViewCrimes.adapter = mAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class CrimeHolder(val itemBinding: ItemCrimeBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        private lateinit var mCrime: Crime

        init {
            itemBinding.root.setOnClickListener {
                Toast.makeText(
                    context,
                    "${mCrime.title} is pressed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun bind(crime: Crime) {
            this.mCrime = crime
            itemBinding.tvItemCrimeTitle.text = crime.title
            itemBinding.tvItemCrimeDate.text = crime.date.toString()
        }
    }

    private inner class CrimePoliceHolder(val itemPoliceBinding: ItemCrimePoliceBinding) :
        RecyclerView.ViewHolder(itemPoliceBinding.root) {

        private lateinit var mCrime: Crime

        init {
            itemPoliceBinding.root.setOnClickListener {
                Toast.makeText(
                    context,
                    "${mCrime.title} is pressed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        fun bind(crime: Crime) {
            this.mCrime = crime
            itemPoliceBinding.tvItemCrimeTitle.text = crime.title
            itemPoliceBinding.tvItemCrimeDate.text = crime.date.toString()
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            return if (viewType == ITEM_TYPE.ITEM_TYPE_POLICE.ordinal) {
                CrimePoliceHolder(ItemCrimePoliceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            } else {
                CrimeHolder(ItemCrimeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

        }

        override fun getItemViewType(position: Int): Int {

            if (crimes[position].requiresPolice) {
                return ITEM_TYPE.ITEM_TYPE_POLICE.ordinal
            } else {
                return ITEM_TYPE.ITEM_TYPE_NOMAL.ordinal
            }
        }

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val crime = crimes[position]
            if(holder is CrimeHolder){
                holder.bind(crime)
            }else if (holder is CrimePoliceHolder){
                holder.bind(crime)
            }
        }
    }

    enum class ITEM_TYPE {
        ITEM_TYPE_NOMAL,
        ITEM_TYPE_POLICE
    }

}