package space.irsi7.gallerymy

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*
import kotlin.streams.toList

//Класс хранящий картинки из одной директории
class MediaList : Fragment() {
    private lateinit var fileList: RecyclerView
    private lateinit var adapter: PictureAdapter
    private lateinit var glm: GridLayoutManager
    private lateinit var base: File
    private lateinit var path: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fileList = inflater.inflate(R.layout.pictures_list_fragment, container, false) as RecyclerView
        path = arguments?.get("path").toString()
        glm = GridLayoutManager(context, 2)
        fileList.layoutManager = glm

        setAdapter()
        return fileList
    }

    private fun setAdapter() {
        base = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "/$path"
        )
        val b = base.listFiles { f -> f.name.endsWith(".jpg") }
        adapter = PictureAdapter(context, b)
        fileList.adapter = adapter
    }

    fun UpdateAdapter() {
        adapter?.updateDataSet(base!!.listFiles())
    }
}