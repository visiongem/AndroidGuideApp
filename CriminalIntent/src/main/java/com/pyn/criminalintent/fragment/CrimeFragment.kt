package com.pyn.criminalintent.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pyn.criminalintent.bean.Crime
import com.pyn.criminalintent.databinding.CrimeFragmentBinding
import com.pyn.criminalintent.viewmodel.CrimeDetailViewModel
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"

class CrimeFragment : Fragment() {

    private lateinit var mCrime: Crime

    // fragment 的生命周期与 activity 的生命周期不同，并且该fragment 可以超出其视图的生命周期，因此如果不将其设置为null，则可能会发生内存泄漏。
    // 另一个变量通过 !! 使一个变量为可空值而使另一个变量为非空值避免了空检查。
    private var _binding: CrimeFragmentBinding? = null
    private val mBinding get() = _binding!!
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCrime = Crime()
        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        Log.d(TAG, "args bundle crime ID:$crimeId")
        crimeDetailViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CrimeFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                this.mCrime = it
                updateUI(it)
            }
        })
    }

    private fun updateUI(crime:Crime) {
        mBinding.edtCrimeTitle.setText(crime.title)
        mBinding.btnCrimeDate.apply {
            text = crime.date.toString()
            isEnabled = false
        }
        mBinding.cboxCrimeSolved.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        mBinding.edtCrimeTitle.addTextChangedListener(titleWatcher)
        mBinding.cboxCrimeSolved.apply {
            setOnCheckedChangeListener { _, isChecked -> mCrime.isSolved = isChecked }
        }
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(mCrime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}