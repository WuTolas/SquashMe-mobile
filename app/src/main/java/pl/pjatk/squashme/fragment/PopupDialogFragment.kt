package pl.pjatk.squashme.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pl.pjatk.squashme.R

class PopupDialogFragment(
        private val title: String,
        private val message: String = ""
        ) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                    .setTitle(title)
                    .setMessage(message)
                    .setNeutralButton("Close") { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
