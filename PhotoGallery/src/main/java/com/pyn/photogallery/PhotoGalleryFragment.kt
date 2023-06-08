package com.pyn.photogallery

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import coil.load
import com.chad.library.adapter.base.module.LoadMoreModule
import com.pyn.photogallery.base.BaseBindingQuickAdapter
import com.pyn.photogallery.bean.GalleryItem
import com.pyn.photogallery.databinding.FragmentPhotoGalleryBinding
import com.pyn.photogallery.databinding.ListItemGalleryBinding
import com.pyn.photogallery.net.ThumbnailDownloader
import com.pyn.photogallery.utils.PollWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment() {

    private var _binding: FragmentPhotoGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PhotoAdapter

    //    private val viewModel: PhotoGalleryViewModel by viewModels()
    private lateinit var viewModel: PhotoGalleryViewModel
    lateinit var thumbnailDownloader: ThumbnailDownloader<BaseBindingQuickAdapter.BaseBindingHolder>


    // 当前请求页面
    private var currentPage = 1

    // 每次请求条数
    private val limit = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adapter = PhotoAdapter()
        _binding = FragmentPhotoGalleryBinding.inflate(layoutInflater, container, false)
        binding.recyclerviewPhoto.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerviewPhoto.adapter = adapter
        return binding.root
    }

    companion object {
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this)[PhotoGalleryViewModel::class.java]
        val responseHandler = Handler(Looper.getMainLooper())
        thumbnailDownloader = ThumbnailDownloader(responseHandler)
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
//        val workRequest = OneTimeWorkRequest.Builder(PollWorker::class.java).setConstraints(constraints).build()
        val workRequest = OneTimeWorkRequestBuilder<PollWorker>().setConstraints(constraints).build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
        /*val flickrLiveData: LiveData<List<GalleryItem>> = FlickrFetchr().fetchPhotos()
        flickrLiveData.observe(this) { gallerayItems ->
            Log.d(TAG, "Response received:$gallerayItems")
        }*/
        /*viewLifecycleOwnerLiveData.observe(viewLifecycleOwner){
            it?.lifecycle?.addObserver(
                thumbnailDownloader.viewLifecycleObserver
            )
        }*/
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    Log.d(TAG, "onQueryTextSubmit:$query")
                    viewModel.fetchPhotos(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    Log.d(TAG, "onQueryTextChange:$newText")
                    return false
                }
            })
            setOnSearchClickListener { searchView.setQuery(viewModel.searchTerm, false) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                viewModel.fetchPhotos("")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader.viewLifecycleObserver)
        viewModel.galleryItemLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "Response received:$it")
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.setList(it)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                if (binding.swipeRefreshLayout.isRefreshing) {
                    delay(3000)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
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
        thumbnailDownloader.clearDownloadTasks()
//        viewLifecycleOwner.lifecycle.removeObserver(thumbnailDownloader.viewLifecycleObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    /*    inner class PhotoAdapter : BaseBindingQuickAdapter<GalleryItem, ItemPhotoBinding>() {

            override fun convert(holder: BaseBindingHolder, item: GalleryItem) {
                with(holder.getViewBinding<ItemPhotoBinding>()) {
                    tvTitle.text = item.title
                }
            }
        }*/

    inner class PhotoAdapter : BaseBindingQuickAdapter<GalleryItem, ListItemGalleryBinding>(),
        LoadMoreModule {

        override fun convert(holder: BaseBindingHolder, item: GalleryItem) {

            with(holder.getViewBinding<ListItemGalleryBinding>()) {
                this.root.load(item.url) {
                    placeholder(R.drawable.mio)
                }
            }

            /*this@PhotoGalleryFragment.thumbnailDownloader.setThumbnailDownloader { holder, bitmap->
                with(holder.getViewBinding<ListItemGalleryBinding>()) {
                    this.root.setImageBitmap(bitmap)
                }
            }
            this@PhotoGalleryFragment.thumbnailDownloader.queueThumbnail(holder, item.url)
            with(holder.getViewBinding<ListItemGalleryBinding>()) {
                this.root.setImageDrawable(context.getDrawable(R.drawable.mio))
            }*/
        }
    }
}
