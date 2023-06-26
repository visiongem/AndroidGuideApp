package com.pyn.photogallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.pyn.photogallery.databinding.FragmentPhotoPageBinding
import com.pyn.photogallery.utils.VisibleFragment

/**
 * Description：
 * @author Created by pengyanni
 * @e-mail 393507488@qq.com
 * @time   2023/6/26 16:49
 */
private const val ARG_URI = "photo_page_url"

class PhotoPageFragment : VisibleFragment() {

    private var _binding: FragmentPhotoPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri = arguments?.getParcelable(ARG_URI)?:Uri.EMPTY
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.max = 100
        with(binding.webView){
            settings.javaScriptEnabled = true
            webChromeClient = object :WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100){
                        binding.progressBar.visibility = View.GONE
                    }else{
                        binding.progressBar.visibility = View.VISIBLE
                        binding.progressBar.progress = newProgress
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    (activity as AppCompatActivity).supportActionBar?.subtitle = title
                }
            }
            webViewClient = WebViewClient()
            loadUrl(uri.toString())
        }
    }

    companion object {
        fun newInstance(uri: Uri): PhotoPageFragment {
            return PhotoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URI, uri)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}