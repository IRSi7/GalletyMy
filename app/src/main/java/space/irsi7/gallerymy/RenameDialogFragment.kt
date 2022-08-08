package space.irsi7.gallerymy

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment

/**
 * Диалог на получение разрешения на работу с файлами от пользователя
 */
class RenameDialogFragment : DialogFragment() {

    lateinit var adapter: PictureAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.give_permission,
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setNegativeButton(R.string.exit,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}