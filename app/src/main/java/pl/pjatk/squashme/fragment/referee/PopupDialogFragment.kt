package pl.pjatk.squashme.fragment.referee

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pl.pjatk.squashme.R

class PopupDialogFragment(
        private val option: PopupOption
) : DialogFragment() {

    private lateinit var counter: TextView
    private lateinit var popupTitle: TextView
    private lateinit var closeButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var title: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        popupTitle = view.findViewById(R.id.popup_title)
        counter = view.findViewById(R.id.counter)
        closeButton = view.findViewById(R.id.close_popup_btn)
        progressBar = view.findViewById(R.id.popup_progress_bar)
        closeButton.setOnClickListener { this.dismiss() }

        title = if (option == PopupOption.END_GAME) {
            "Match finished"
        } else if (option == PopupOption.WALKOVER) {
            "Choose who won" // todo add buttons to choose who won the game
        } else {
            progressBar.visibility = View.VISIBLE
            object : CountDownTimer(90000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val currentValue: Int = (millisUntilFinished / 1000).toInt()
                    counter.text = "$currentValue"
                    progressBar.progress = currentValue
                }

                override fun onFinish() {
                    counter.setTextColor(Color.RED)
                    counter.text = "0"
                }
            }.start()
            "Set finished"
        }

        popupTitle.text = title
    }
}
