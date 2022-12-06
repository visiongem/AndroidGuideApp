package com.pyn.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pyn.photogallery.base.BaseBindingQuickAdapter
import com.pyn.photogallery.bean.GalleryItem
import com.pyn.photogallery.databinding.FragmentPhotoGalleryBinding
import com.pyn.photogallery.databinding.ItemPhotoBinding

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var adapter: PhotoAdapter

    //    private val viewModel: PhotoGalleryViewModel by viewModels()
    private lateinit var viewModel: PhotoGalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = PhotoAdapter()
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater, container, false)
        mBinding.recyclerviewPhoto.layoutManager = LinearLayoutManager(context)
        mBinding.recyclerviewPhoto.adapter = adapter
        return mBinding.root
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
        /*val flickrLiveData: LiveData<List<GalleryItem>> = FlickrFetchr().fetchPhotos()
        flickrLiveData.observe(this) { gallerayItems ->
            Log.d(TAG, "Response received:$gallerayItems")
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.galleryItemLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "Response received:$it")
            adapter.setList(it)
        }
    }

/*    private class PhotoHolder(itemTextView: TextView) : RecyclerView.ViewHolder(itemTextView) {
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }

    private class PhotoAdapter(private val galleryItems: List<GalleryItem>) : RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val textView = TextView(parent.context)
            return PhotoHolder(textView)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bindTitle(galleryItem.title)
        }

        override fun getItemCount(): Int {
            return galleryItems.size
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class PhotoAdapter : BaseBindingQuickAdapter<GalleryItem, ItemPhotoBinding>() {

        override fun convert(holder: BaseBindingHolder, item: GalleryItem) {
            with(holder.getViewBinding<ItemPhotoBinding>()) {
                tvTitle.text = item.title
            }
        }
    }
}