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
import pl.pjatk.squashme.R

class PopupDialogFragment(
        private val option: PopupOption,
        private var player1Name: String = "",
        private var player2Name: String = ""
) : DialogFragment() {

    companion object {
        const val WINNER_KEY = "winner"
        const val END_GAME_KEY = "confirmGameEnds"
    }

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
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerOneName = player1Name
        playerTwoName = player2Name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init(view)

        when (option) {
            PopupOption.END_GAME -> {
                showEndGameOption()
            }
            PopupOption.WALKOVER -> {
                showWalkoverOption()
            }
            PopupOption.END_FRIENDLY_GAME -> {
                showEndFriendlyGameOption()
            }
            PopupOption.END_SET -> {
                showEndSetOption()
            }
        }
        updatePopupDetails()
    }

    /**
     * update title and additional info in popup window
     * based on option
     */
    private fun updatePopupDetails() {
        popupTitle.text = title
        popupAdditionalInfo.text = additionalInfo
    }

    /**
     * show 90 seconds counter
     * to visualise break time
     */
    private fun showEndSetOption() {
        progressBar.visibility = View.VISIBLE
        timer = object : CountDownTimer(90000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val currentValue: Int = (millisUntilFinished / 1000).toInt()
                counter.text = "$currentValue"
                progressBar.progress = currentValue
            }

            override fun onFinish() {
                counter.setTextColor(Color.RED)
                counter.text = getString(R.string.time_is_up)
            }
        }.start()
        title = getString(R.string.set_finished)
        additionalInfo = getString(R.string.ninety_seconds_break)
    }

    /**
     * show buttons to confirm ending game
     */
    private fun showEndFriendlyGameOption() {
        endGameConfirm.visibility = View.VISIBLE
        title = getString(R.string.ending_game)
        additionalInfo = getString(R.string.confirm_ending_game)
    }

    /**
     * show buttons to manually choose winner
     */
    private fun showWalkoverOption() {
        playerOneButton.text = playerOneName
        playerTwoButton.text = playerTwoName
        playerNamesWrapper.visibility = View.VISIBLE
        title = getString(R.string.walkover)
        additionalInfo = getString(R.string.choose_who_won)
    }

    private fun init(view: View) {
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
    }

    /**
     * change only title and additional info after finished game
     */
    private fun showEndGameOption() {
        title = getString(R.string.match_finished)
        additionalInfo = getString(R.string.click_end_game)
    }


    /**
     * set winner to propagate result to another fragment
     * @param player Int
     */
    private fun playerButtonListener(player: Int) {
        setFragmentResult(WINNER_KEY, bundleOf(WINNER_KEY to player))
        dismiss()
    }

    /**
     * set decision to end game to propagate result to another fragment
     * @param finish Boolean
     */
    private fun confirmEndGameListener(finish: Boolean) {
        setFragmentResult(END_GAME_KEY, bundleOf(END_GAME_KEY to finish))
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
