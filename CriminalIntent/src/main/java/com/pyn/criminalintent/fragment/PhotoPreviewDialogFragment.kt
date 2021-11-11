package com.pyn.criminalintent.fragment

import androidx.fragment.app.DialogFragment
import android.os.Bundle

import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import com.pyn.criminalintent.databinding.FragmentDialogPreviewPhotoBinding
import com.pyn.criminalintent.utils.PictureUtil

private const val IMG_PATH = "img_path"

class PhotoPreviewDialogFragment : DialogFragment() {

    private var _binding: FragmentDialogPreviewPhotoBinding? = null
    private val mBinding get() = _binding!!

    fun newInstance(args: Bundle?): PhotoPreviewDialogFragment {
        val fragment = PhotoPreviewDialogFragment()
        if (args != null) {
            fragment.arguments = args
        }
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogPreviewPhotoBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.get(IMG_PATH) != null) {
            val imgPath: String = arguments?.get(IMG_PATH) as String
            mBinding.imgDialogPreviewPhoto.setImageBitmap(
                PictureUtil.getScaledBitmap(
                    imgPath,
                    requireActivity()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}