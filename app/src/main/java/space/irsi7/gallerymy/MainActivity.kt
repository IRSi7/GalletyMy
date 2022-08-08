package space.irsi7.gallerymy

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import space.irsi7.gallerymy.databinding.ActivityMainBinding
import java.io.File
import androidx.viewpager.widget.PagerAdapter as ViewpagerWidgetPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // Перменные для хранения ссылок виджетов объявленных в файле макета
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    // Переменная для сохранения сгенерированного пути для фото и видео в этой переменной храниться
    // путь для последнего созданного файла
    var mediaPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        // Получить ссылку виджета Toolbar, который необходим для замены стандартного тулбара
        //toolbar = binding.toolbar
        // Установить в качестве системного тулбара, Toolbar объявленный в макете
        //setSupportActionBar(toolbar)
        // Установить заголовок для Activity, который показывается в Toolbar-e
        title = "Галерея"

        // ViewPager - виджет для просмотра фрагментов в виде страниц переключаемых по свайпу
        viewPager = binding.viewpager
        // TabLayout - виджет для создания вкладок
        tabLayout = binding.tabs


//        val ase = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
//            "/Camera"
//        ).list()
//        for( i in ase){
//            println(i)
//        }
//        ase.forEach { _ -> println(this) }
        println(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))

        // Если нет необходимых розрешений, тогда запросить их
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Если есть разрешеиня, вызвать функцию для настройки ViewPager-a и добавления необходимых фрагментов
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
            setupViewPager(viewPager)
        // Функция для привязки ViewPager-a к TabLayout-у
        //tabLayout!!.setupWithViewPager(viewPager)
        } else {
            setupViewPager(viewPager)
        }
    }

    // Функция для добавления фрагментов в ViewPager
    private fun setupViewPager(viewPager: ViewPager2?) {
        val adapter = ViewPager2Adapter(supportFragmentManager)
        val photoList = MediaList()
        val args = Bundle()
        args.putString("path", "Camera")
        photoList.arguments = args
        adapter.addFragment(photoList, "Фото")
        viewPager?.adapter = adapter
    }

    // Класс адаптера для ViewPager-a
    internal inner class ViewPager2Adapter(manager: FragmentManager?) :
        FragmentStateAdapter(manager!!, lifecycle) {
        // Массив для хранения фрагментов (страниц)
        private val mFragmentList: ArrayList<Fragment?> = ArrayList()

        // Массив для хранения массива заголовков фрагментов дяя показа на вкладках
        private val mFragmentTitleList: ArrayList<String?> = ArrayList()

        fun addFragment(fragment: Fragment?, title: String?) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        fun getPageTitle(position: Int): String? {
            return mFragmentTitleList[position]
        }

        override fun getItemCount(): Int {
            return mFragmentTitleList.size
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]!!
        }
    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }


}