package space.irsi7.gallerymy
import android.annotation.SuppressLint
import android.app.Activity
import android.app.job.JobWorkItem
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import space.irsi7.gallerymy.R
//import space.irsi7.gallerymy.PictureAdapter.GenerateThumb
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.coroutines.CoroutineContext

// Класс адаптера для привязки и показа данных в RecyclerView
class PictureAdapter internal constructor(context: Context?, items: Array<File>?) :
    RecyclerView.Adapter<PictureAdapter.ViewHolder?>() {

    var items: ArrayList<File>

    private val thumbs: HashMap<String, Bitmap> = HashMap()

    private var context: Context


    // Функция для обновления списка для показа новых добавленных файлов
    fun updateDataSet(items: Array<File>) {
        this.items = ArrayList((items).asList())
        notifyDataSetChanged()
    }

    // Вложенный класс класса FileAdapter для хранения объектов элементов RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView
        var thumb: ImageView

        init {
            title = itemView.findViewById(R.id.fileTitle)
            thumb = itemView.findViewById(R.id.thumb)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.picture_item, parent, false)
        return ViewHolder(v)
    }

    // Функция вызывается при присвоении значений переменным класса ViewHolder
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.title.text = items[position].name

        if (thumbs.containsKey(items[position].absolutePath)) {
            holder.thumb.setImageBitmap(thumbs[items[position].absolutePath])
        } else {
            holder.thumb.setImageDrawable(
                context.resources.getDrawable(R.drawable.placeholder)
            )

            var thumb: Bitmap
            val job: Job = GlobalScope.launch { withContext(Dispatchers.IO) {
                thumb =
                    ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(items[position].absolutePath),
                        256,
                        256
                    )
            }
                withContext(Dispatchers.Main){
                    thumbs[items[position].absolutePath] = thumb
                    holder.thumb.setImageBitmap(thumb)


//                ActivityResultContracts.StartActivityForResult(bind).launch(intent)
//                ActivityResult(fullScreen, Constants.RENAME_FILE_REQUEST)
                    }
                    }
                }

//            val activityLauncher = ActivityResultContracts.StartActivityForResult(mynameContract()) { result ->
//                // используем result
//            }
        holder.itemView.setOnClickListener {
            val fullScreen = Intent(context, FullscreenActivity::class.java)
            fullScreen.putExtra("path", items[position].absolutePath)
            fullScreen.putExtra("position", position)

            startActivity(context, fullScreen, null)
        }

    }

    fun renameFile(newName: String, position: Int) {
        val file =
            File(items[position].parentFile.absolutePath.toString() + "/" + newName)
        if (items[position].renameTo(file)) {
            items.removeAt(position)
            items.add(position, file)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

//    class MyRenameContract : ActivityResultContract<String, Int?>() {
//
//        override fun createIntent(context: Context, input: String?): Intent {
//            return Intent(context, RenameActivity::class.java)
//        }
//
//        override fun parseResult(resultCode: Int, intent: Intent?): Int? = when {
//            resultCode != Activity.RESULT_OK -> null
//            else -> intent?.getIntExtra(Constants.RENAME_FILE_REQUEST, 1)
//        }
//
//        override fun getSynchronousResult(context: Context, input: String?): SynchronousResult<Int?>? {
//            return if (input.isNullOrEmpty()) SynchronousResult(1) else null
//        }
//    }

//    // Поток для генерации миниатюр изображений
//    class GenerateThumb(path: String, iv: ImageView): CoroutineScope {
//        // Ссылка на ImageView в котором необходимо показать сгенерированную миниатюру
//        private var job: Job = Job()
//        override val coroutineContext: CoroutineContext
//        get() = Dispatchers.Main + job
//
//
//        fun execute(){
//            onPreExecute()
//            doInBackground()
//            onPostExecute()
//        }
//
//            @SuppressLint("UseCompatLoadingForDrawables")
//            private fun onPreExecute() {
//                // show progress
//                thumb.setImageDrawable(
//                    context.resources.getDrawable(R.drawable.placeholder)
//                )
//            }
//
//            private fun onPostExecute() {
//                        // hide progress
//                holder.itemView.setOnClickListener {
//                    val fullScreen = Intent(context, FullscreenActivity::class.java)
//                    fullScreen.putExtra("path", items[position].absolutePath)
//                    context.startActivity(fullScreen)
//                }
//            }
//
//        private fun doInBackground() = launch {
//            val thumb: Bitmap =
//                    ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 256, 256)
//        }
//
//        private fun onProgressUpdate(vararg values: Bitmap?) {
//            super.onProgressUpdate(values)
//            // Устанавливает в ImageView сгенерированную миниатюру
//            iv?.setImageBitmap(values[0])
//        }
//    }


    // Функция вызывается для обновления пользовательского интерфейса в промежутках обработки
    // фонового потока если не вызывать эту функцию то пользователю придется ждать пока все миниатюры не будут сгенерированы
    // и только потом показать их, а вызывая эту функцию при генерации каждой миниатюры они постепенно появляются
    // уже в пользовательском интерфейсе



    // Конструктор класса PictureAdapter где получаются значения
    // из вызываюшего кода и присваиваются локальным переменным класса

    init {
        this.items = ArrayList((items)!!.asList())
        this.context = context!!
    }
}