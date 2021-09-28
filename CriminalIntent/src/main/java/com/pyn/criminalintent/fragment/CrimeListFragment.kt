package com.pyn.criminalintent.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pyn.criminalintent.R
import com.pyn.criminalintent.bean.Crime
import com.pyn.criminalintent.databinding.CrimeListFragmentBinding
import com.pyn.criminalintent.databinding.ItemCrimeBinding
import com.pyn.criminalintent.databinding.ItemCrimePoliceBinding
import com.pyn.criminalintent.databinding.ItemEmptyViewBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CrimeListFragmentBinding.inflate(layoutInflater, container, false)
        mBinding.recyclerViewCrimes.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerViewCrimes.adapter = mAdapter
        mAdapter.showEmptyView()
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBacks = context as CallBacks?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appCompatActivity = activity as AppCompatActivity
        val appBar = appCompatActivity.supportActionBar
        appBar?.title = appCompatActivity.resources.getString(R.string.some_cool_title)
        crimeListViewModel.crimesListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    mAdapter.crimes = it
                    mAdapter.notifyDataSetChanged()
//                    mAdapter.submitList(crimes)
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callBacks?.onCrimeSelected(crime.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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

    private inner class EmptyHolder(val itemEmptyViewBinding: ItemEmptyViewBinding) :
        RecyclerView.ViewHolder(itemEmptyViewBinding.root) {

        init {
            itemEmptyViewBinding.imgAddCrime.setOnClickListener {
                val crime = Crime()
                crimeListViewModel.addCrime(crime)
                callBacks?.onCrimeSelected(crime.id)
            }
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        // 是否显示空布局，默认不显示
        private var showEmptyView = false

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            return when (viewType) {
                ITEM_TYPE.ITEM_TYPE_POLICE.ordinal ->
                    CrimePoliceHolder(
                        ItemCrimePoliceBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                ITEM_TYPE.ITEM_TYPE_NOMAL.ordinal ->
                    CrimeHolder(
                        ItemCrimeBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                else ->
                    EmptyHolder(
                        ItemEmptyViewBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
            }
        }

        override fun getItemViewType(position: Int): Int {

            return if (position == 0 && isEmptyPosition()) {
                ITEM_TYPE.ITEM_TYPE_EMPTY.ordinal
            } else {
                if (crimes[position].requiresPolice) {
                    ITEM_TYPE.ITEM_TYPE_POLICE.ordinal
                } else {
                    ITEM_TYPE.ITEM_TYPE_NOMAL.ordinal
                }
            }
        }

        override fun getItemCount(): Int {
            val count = crimes.size
            return if (count > 0) {
                showEmptyView = false
                count
            } else {
                if (showEmptyView) {
                    1
                } else {
                    0
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            if (!(position == 0 && isEmptyPosition())) {
                val crime = crimes[position]
                if (holder is CrimeHolder) {
                    holder.bind(crime)
                } else if (holder is CrimePoliceHolder) {
                    holder.bind(crime)
                }
            }
        }

        /**
         * Show empty view
         * 显示空布局
         */
        fun showEmptyView() {
            showEmptyView = true
            notifyDataSetChanged()
        }

        /**
         * 判断是否是空布局
         */
        fun isEmptyPosition(): Boolean {
            val count = if (crimes.isEmpty()) 0 else crimes.size
            return showEmptyView && count == 0
        }
    }

    enum class ITEM_TYPE {
        ITEM_TYPE_NOMAL,
        ITEM_TYPE_POLICE,
        ITEM_TYPE_EMPTY
    }

    interface CallBacks {
        fun onCrimeSelected(crimeId: UUID)
    }

/*    class CrimeDiffCallback : DiffUtil.ItemCallback<Crime>() {

        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }*/
}