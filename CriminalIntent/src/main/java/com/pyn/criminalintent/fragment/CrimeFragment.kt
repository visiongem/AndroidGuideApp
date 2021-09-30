package com.pyn.criminalintent.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.pyn.criminalintent.R
import com.pyn.criminalintent.bean.Crime
import com.pyn.criminalintent.databinding.CrimeFragmentBinding
import com.pyn.criminalintent.viewmodel.CrimeDetailViewModel
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "dialogDate"
private const val REQUEST_DATE = 0
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var mCrime: Crime

    // fragment 的生命周期与 activity 的生命周期不同，并且该fragment 可以超出其视图的生命周期，因此如果不将其设置为null，则可能会发生内存泄漏。
    // 另一个变量通过 !! 使一个变量为可空值而使另一个变量为非空值避免了空检查。
    private var _binding: CrimeFragmentBinding? = null
    private val mBinding get() = _binding!!
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this).get(CrimeDetailViewModel::class.java)
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val contactUri: Uri = it.data?.data!!
                val queryFilds = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = requireActivity().contentResolver.query(
                    contactUri, queryFilds, null,
                    null, null
                )
                cursor.use {
                    if (it?.count == 0) {
                        return@use
                    }
                    it?.moveToFirst()
                    val suspect = it?.getString(0)
                    mCrime.suspect = suspect!!
                    crimeDetailViewModel.saveCrime(mCrime)
                    mBinding.btnCrimeSuspect.text = suspect
                }
            }
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
                updateUI()
            }
        })
        mBinding.btnCrimeDate.setOnClickListener {
            DatePickerFragment.newInstance(mCrime.date).apply {
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)
                show(this@CrimeFragment.parentFragmentManager, DIALOG_DATE)
            }
        }
    }

    private fun updateUI() {
        mBinding.edtCrimeTitle.setText(mCrime.title)
        mBinding.btnCrimeDate.text = mCrime.date.toString()
        mBinding.cboxCrimeSolved.apply {
            isChecked = mCrime.isSolved
            jumpDrawablesToCurrentState()
        }
        if (mCrime.suspect.isNotEmpty()) {
            mBinding.btnCrimeSuspect.text = mCrime.suspect
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

        mBinding.btnCrimeReport.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject))
            }.also { intent -> startActivity(intent) }
        }

        mBinding.btnCrimeSuspect.apply {
            val pickContactIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startForResult.launch(pickContactIntent)
            }
            val packageManager: PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }
        }

        mBinding.btnCallSuspect.setOnClickListener {

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

    override fun onDateSelected(date: Date) {
        mCrime.date = date
        updateUI()
    }

    private fun getCrimeReport(): String {
        val solvedStr = if (mCrime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateStr = android.text.format.DateFormat.format(DATE_FORMAT, mCrime.date).toString()
        var suspect = if (mCrime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, mCrime.suspect)
        }

        return String.format(
            resources.getString(R.string.crime_report),
            mCrime.title,
            dateStr,
            solvedStr,
            suspect
        )
    }

}