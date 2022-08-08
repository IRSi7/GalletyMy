package space.irsi7.gallerymy
import android.annotation.SuppressLint
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
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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
    // Локальный массив для хранения файлов
    private var items: ArrayList<File>

    // Ассоциативный массив (словарь) - массив с ключами и значениями, в отличии от обычного массива
    // в котором ключи это числовые индексы позиции элементов, в HashMap ключом может быть объект любого класса
    private val thumbs: HashMap<String, Bitmap> = HashMap()

    // Контекст в котором заружен RecyclerView к которому прикреплен этот адаптер
    private var context: Context


    // Функция для обновления списка для показа новых добавленных файлов
    fun updateDataSet(items: Array<File>) {
        // Обновить массив, используя конвертацию обычного массива в ArrayList
        this.items = ArrayList((items).asList())
        // Вызвать функцию для привязки элементов из нового источника к RecyclerView
        notifyDataSetChanged()
    }

    // Вложенный класс класса FileAdapter для хранения объектов элементов RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Ссылка на TextView в котором выводится заголовок файла
        var title: TextView

        // Ссылка на ImageView для показа миниатюры изображения или видео
        var thumb: ImageView
        //var rename: Button

        // Конструктор класса для присвоения переменным соответствующих объектов из файла макета
        init {
            // Необходимо вызвать суперкласс ViewHolder-а и передать корневой View
            // Получить ссылку к виджетам в корневом View
            title = itemView.findViewById(R.id.fileTitle)
            thumb = itemView.findViewById(R.id.thumb)
            //rename = itemView.findViewById(R.id.rename)
        }
    }

    // Функция onCreateViewHolder вызывается при первом создании объекта для хранения элементов списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Получаем View объект созданный на основе написанной структуры макета в xml файле file_item.xml
        val v: View = LayoutInflater.from(context).inflate(R.layout.picture_item, parent, false)
        // Создаётся новый объект класса ViewHolder и ему передаётся объект View полученный из файла макета
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

                    holder.itemView.setOnClickListener {
                        val fullScreen = Intent(context, FullscreenActivity::class.java)
                        fullScreen.putExtra("path", items[position].absolutePath)
                        context.startActivity(fullScreen)
                    }
                }
                // Если type == 0 значит надо сгенерировать миниатюру для изображения
                //thumb = context.contentResolver.loadThumbnail(
                //content-uri, Size(256, 256), null)
            }

        }
    }


    // Функция для переименования файла
    fun renameFile(newName: String, position: Int) {
        // Создать новый пустой файл с новым именем переименованного файла
        val file =
            File(items[position].parentFile.absolutePath.toString() + "/" + newName)
        // Переименовать выбранный файл и присвоить новое имя
        if (items[position].renameTo(file)) {
            // Если получилось переименовать тогда удалить из массива старый файл
            items.removeAt(position)
            // Добавить новый файл на место удаленного файла
            items.add(position, file)
            // Сообщить адаптеру что инфрмация изменилась
            notifyItemChanged(position)
        }
    }

    // Получить количество элементов в списке
    override fun getItemCount(): Int {
        return items.size
    }

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