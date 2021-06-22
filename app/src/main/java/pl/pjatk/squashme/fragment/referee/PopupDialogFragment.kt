package pl.pjatk.squashme.fragment.referee

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import pl.pjatk.squashme.R

class PopupDialogFragment(
        private val option: PopupOption,
        private var player1Name: String = "",
        private var player2Name: String = ""
) : DialogFragment() {

    private lateinit var counter: TextView
    private lateinit var popupTitle: TextView
    private lateinit var popupAdditionalInfo: TextView
    private lateinit var closeButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var title: String
    private lateinit var additionalInfo: String
    private lateinit var playerNamesWrapper: LinearLayout
    private lateinit var playerOneButton: Button
    private lateinit var playerTwoButton: Button
    private lateinit var endGameConfirm: LinearLayout
    private lateinit var noButton: Button
    private lateinit var yesButton: Button
    private lateinit var playerOneName: String
    private lateinit var playerTwoName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerOneName = player1Name
        playerTwoName = player2Name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        popupTitle = view.findViewById(R.id.popup_title)
        popupAdditionalInfo = view.findViewById(R.id.popup_additional_info)
        counter = view.findViewById(R.id.counter)
        closeButton = view.findViewById(R.id.close_popup_btn)
        progressBar = view.findViewById(R.id.popup_progress_bar)
        playerNamesWrapper = view.findViewById(R.id.popup_player_names)
        playerOneButton = view.findViewById(R.id.popup_player_one_name)
        playerTwoButton = view.findViewById(R.id.popup_player_two_name)
        endGameConfirm = view.findViewById(R.id.popup_end_game_confirmation)
        noButton = view.findViewById(R.id.popup_no)
        yesButton = view.findViewById(R.id.popup_yes)

        playerOneButton.setOnClickListener { playerButtonListener(1) }
        playerTwoButton.setOnClickListener { playerButtonListener(2) }
        noButton.setOnClickListener { confirmEndGameListener(false) }
        yesButton.setOnClickListener { confirmEndGameListener(true) }
        closeButton.setOnClickListener { this.dismiss() }

        when (option) {
            PopupOption.END_GAME -> {
                title = getString(R.string.match_finished)
                additionalInfo = "Click 'END GAME' to finish"
            }
            PopupOption.WALKOVER -> {
                playerOneButton.text = playerOneName
                playerTwoButton.text = playerTwoName
                playerNamesWrapper.visibility = View.VISIBLE
                title = getString(R.string.walkover)
                additionalInfo = "Choose who won the game:"
            }
            PopupOption.END_FRIENDLY_GAME -> {
                endGameConfirm.visibility = View.VISIBLE
                title = "Ending game"
                additionalInfo = "Are you sure you want to end game?"
            }
            else -> {
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
                title = getString(R.string.set_finished)
                additionalInfo = "90 seconds break"
            }
        }

        popupTitle.text = title
        popupAdditionalInfo.text = additionalInfo
    }

    private fun playerButtonListener(player: Int) {
        setFragmentResult("winner", bundleOf("winner" to player))
        dismiss()
    }

    private fun confirmEndGameListener(finish: Boolean) {
        setFragmentResult("confirmGameEnds", bundleOf("confirmGameEnds" to finish))
        dismiss()
    }
}
