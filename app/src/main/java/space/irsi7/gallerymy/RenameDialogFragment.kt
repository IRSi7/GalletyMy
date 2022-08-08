package space.irsi7.gallerymy

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import space.irsi7.gallerymy.databinding.FragmentDialogRenameBinding


/**
 * Диалог на переиминование файла
 */
class RenameDialogFragment : DialogFragment() {

    lateinit var adapter: PictureAdapter
    lateinit var binding: FragmentDialogRenameBinding

    private var mEditText: EditText? = null

    fun EditNameDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

//    fun newInstance(title: String?): RenameDialogFragment? {
//        var frag = EditNameDialogFragment()
//        val args = Bundle()
//        args.putString("title", title)
//        //frag.setArguments(args)
//        return frag
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        //return inflater.inflate(R.layout.fragment_edit_name, container)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        mEditText = view.findViewById<View>(R.id.edit) as EditText
        // Fetch arguments from bundle and set title
        val title = requireArguments().getString("title", "Enter Name")
        dialog!!.setTitle(title)
        // Show soft keyboard automatically and request focus to field
        mEditText!!.requestFocus()
        dialog!!.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        binding = infl
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//            builder.setMessage(R.string.rename_message)
//                .setPositiveButton(R.string.rename_submit,
//                    DialogInterface.OnClickListener { dialog, id ->
//                    adapter.renameFile()
//                    })
//                .setNegativeButton(R.string.rename_cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
}