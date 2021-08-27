package com.pyn.criminalintent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pyn.criminalintent.databinding.CrimeFragmentBinding

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime

    // fragment 的生命周期与 activity 的生命周期不同，并且该fragment 可以超出其视图的生命周期，因此如果不将其设置为null，则可能会发生内存泄漏。
    // 另一个变量通过 !! 使一个变量为可空值而使另一个变量为非空值避免了空检查。
    private var _binding: CrimeFragmentBinding? = null
    private val mBinding get() = _binding!!

    companion object {
        fun newInstance() = CrimeFragment()
    }

    private lateinit var viewModel: CrimeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CrimeFragmentBinding.inflate(layoutInflater, container, false)
        mBinding.btnCrimeDate.apply {
            text = crime.date.toString()
            isEnabled = false
        }
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CrimeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        mBinding.edtCrimeTitle.addTextChangedListener(titleWatcher)
        mBinding.cboxCrimeSolved.apply {
            setOnCheckedChangeListener { _, isChecked -> crime.isSolved = isChecked }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}