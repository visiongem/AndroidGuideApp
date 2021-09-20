package com.pyn.criminalintent.fragment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pyn.criminalintent.bean.Crime
import com.pyn.criminalintent.databinding.CrimeListFragmentBinding
import com.pyn.criminalintent.databinding.ItemCrimeBinding
import com.pyn.criminalintent.databinding.ItemCrimePoliceBinding
import com.pyn.criminalintent.utils.DateUtil
import com.pyn.criminalintent.viewmodel.CrimeListViewModel
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var _binding: CrimeListFragmentBinding? = null
    private val mBinding get() = _binding!!
    private var mAdapter: CrimeAdapter = CrimeAdapter(emptyList())
    private var callBacks: CallBacks? = null

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
        mBinding.recyclerViewCrimes.adapter = mAdapter
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimesListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    mAdapter.crimes = it
                    mAdapter.submitList(crimes)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
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
                /*Toast.makeText(
                    context,
                    "${mCrime.title} is pressed",
                    Toast.LENGTH_SHORT
                ).show()*/
                callBacks?.onCrimeSelected(mCrime.id)
            }
        }

        fun bind(crime: Crime) {
            this.mCrime = crime
            itemBinding.tvItemCrimeTitle.text = crime.title
            itemBinding.tvItemCrimeDate.text = DateUtil.getDayAndWeek(crime.date)
            if (mCrime.isSolved) {
                itemBinding.imgItemCrimeIsSolved.visibility = View.VISIBLE
            } else {
                itemBinding.imgItemCrimeIsSolved.visibility = View.GONE
            }
        }
    }

    private inner class CrimePoliceHolder(val itemPoliceBinding: ItemCrimePoliceBinding) :
        RecyclerView.ViewHolder(itemPoliceBinding.root) {

        private lateinit var mCrime: Crime

        init {
            itemPoliceBinding.root.setOnClickListener {
                /*Toast.makeText(
                    context,
                    "${mCrime.title} is pressed",
                    Toast.LENGTH_SHORT
                ).show()*/
                callBacks?.onCrimeSelected(mCrime.id)
            }
        }

        fun bind(crime: Crime) {
            this.mCrime = crime
            itemPoliceBinding.tvItemCrimeTitle.text = crime.title
            itemPoliceBinding.tvItemCrimeDate.text = DateUtil.getDayAndWeek(crime.date)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        ListAdapter<Crime, RecyclerView.ViewHolder>(CrimeDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            return if (viewType == ITEM_TYPE.ITEM_TYPE_POLICE.ordinal) {
                CrimePoliceHolder(
                    ItemCrimePoliceBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            } else {
                CrimeHolder(
                    ItemCrimeBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
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
            if (holder is CrimeHolder) {
                holder.bind(crime)
            } else if (holder is CrimePoliceHolder) {
                holder.bind(crime)
            }
        }
    }

    enum class ITEM_TYPE {
        ITEM_TYPE_NOMAL,
        ITEM_TYPE_POLICE
    }

    interface CallBacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    class CrimeDiffCallback : DiffUtil.ItemCallback<Crime>() {

        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
}