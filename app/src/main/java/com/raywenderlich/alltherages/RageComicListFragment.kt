package com.raywenderlich.alltherages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.alltherages.databinding.RecyclerItemRageComicBinding

class RageComicListFragment : Fragment() {

    private lateinit var imageResIds: IntArray
    private lateinit var names: Array<String>
    private lateinit var descriptions: Array<String>
    private lateinit var urls: Array<String>
    private lateinit var listener: OnRageComicSelected

    companion object {
        fun newInstance(): RageComicListFragment {
            return RageComicListFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnRageComicSelected) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement onRageComicSelected")
        }

        val resources = context!!.resources
        names = resources.getStringArray(R.array.names)
        descriptions = resources.getStringArray(R.array.descriptions)
        urls = resources.getStringArray(R.array.urls)

        //get Rage face images
        val typedArray = resources.obtainTypedArray(R.array.images)
        val imageCount = names.size
        imageResIds = IntArray(imageCount)

        for (i in 0..imageCount - 1) {
            imageResIds[i] = typedArray.getResourceId(i, 0)
        }

        // PZrecycle basically means..free/clearing all the data associated with corresponding resource.
        // In Android we can find recycle for Bitmap and TypedArray.
        typedArray.recycle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_rage_comic_list, container, false)
        val activity = requireActivity()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.adapter = RageComicAdapter(activity)
        return view
    }


    internal inner class RageComicAdapter(context: Context) : RecyclerView.Adapter<ViewHolder>() {

        private val layoutInflater: LayoutInflater

        init {
            layoutInflater = LayoutInflater.from(context)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val recyclerItemRageComicBinding = RecyclerItemRageComicBinding.inflate(layoutInflater,
                    viewGroup, false)
            return ViewHolder(recyclerItemRageComicBinding.root, recyclerItemRageComicBinding)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val comic = Comic(imageResIds[position], names[position],
                    descriptions[position], urls[position])
            viewHolder.setData(comic)
            viewHolder.itemView.setOnClickListener({ listener.onRageComicSeleceted(comic) })
        }

        override fun getItemCount(): Int {
            return names.size
        }
    }

    internal inner class ViewHolder constructor(itemView: View,
                                                val recyclerItemRageComicBinding:
                                                RecyclerItemRageComicBinding) :
            RecyclerView.ViewHolder(itemView) {

        fun setData(comic: Comic) {
            recyclerItemRageComicBinding.comic = comic
        }
    }

    interface OnRageComicSelected {
        fun onRageComicSeleceted(comic: Comic)
    }
}
