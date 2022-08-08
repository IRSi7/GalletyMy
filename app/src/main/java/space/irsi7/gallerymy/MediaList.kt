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
        // Получить через файл макета RecyclerView и присвоить локальной переменной
        fileList = inflater.inflate(R.layout.pictures_list_fragment, container, false) as RecyclerView

        // Получить переданный путь и присвоить локальной переменной
        path = arguments?.get("path").toString()

        // Создать экземпляр GridLayoutManager для показа содержимого RecyclerView в виде плитки
        glm = GridLayoutManager(context, 2)
        // Присвоить экземпляру RecyclerView экзмепляр класса GridLayoutManager
        fileList.layoutManager = glm

        // Вызов функции для установки адаптера для RecyclerView
        setAdapter()
        // Вернуть полученный через LayoutInflater RecyclerView в качестве объекта View для показа в Activity
        return fileList
    }

    private fun setAdapter() {
        // Переменная для хранения объекта класса File, который хранит в себе путь к папке с изображениями и видео
        base = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "/$path"
        )
        val b = base.listFiles { f -> f.name.endsWith(".jpg") }
        // Создать адптер, куда передаётся путь к файлам
        adapter = PictureAdapter(context, b)
                // Установить адаптер для показа элементов в списке
        fileList.adapter = adapter
    }

    // Функция служит для обновления списка с файлами, которая вызывается из MainActivity при съемке новых фото и видео
    fun UpdateAdapter() {
        // Вызвать функцию updateDataSet из адаптера для передачи списка файлов вместе с новыми файлами для обновления списка
        adapter?.updateDataSet(base!!.listFiles())
    }
}