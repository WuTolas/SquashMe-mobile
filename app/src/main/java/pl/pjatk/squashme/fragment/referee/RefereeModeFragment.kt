package pl.pjatk.squashme.fragment.referee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.pjatk.squashme.R
import pl.pjatk.squashme.activity.TournamentDashboardNavigation
import pl.pjatk.squashme.di.component.DaggerRefereeFragmentComponent
import pl.pjatk.squashme.di.module.RoomModule
import pl.pjatk.squashme.fragment.referee.PopupOption.*
import pl.pjatk.squashme.model.Match
import pl.pjatk.squashme.model.Player
import pl.pjatk.squashme.model.Result
import pl.pjatk.squashme.model.custom.MatchWithPlayers
import pl.pjatk.squashme.service.MatchService
import pl.pjatk.squashme.service.ResultService
import javax.inject.Inject
import kotlin.math.abs

class RefereeModeFragment : Fragment() {

    companion object {
        private const val MATCH_PARAM = "match"
        private const val TAG = "RefereeModeFragment"
    }

    @Inject
    lateinit var matchService: MatchService

    @Inject
    lateinit var resultService: ResultService

    private lateinit var model: RefereeModel

    private lateinit var matchWithPlayers: MatchWithPlayers
    private lateinit var match: Match

    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player

    private var setsToWin: Int = 0
    private var playerOneScore: Int = 0
    private var playerTwoScore: Int = 0
    private var playerOneSet: Int = 0
    private var playerTwoSet: Int = 0
    private lateinit var playerOneNameHolder: Button
    private lateinit var playerTwoNameHolder: Button
    private lateinit var playerOneScoreBtn: Button
    private lateinit var playerTwoScoreBtn: Button
    private lateinit var playerOneSetNumber: TextView
    private lateinit var playerTwoSetNumber: TextView
    private lateinit var endGameButton: Button
    private lateinit var nextSetButton: Button
    private lateinit var revertPointButton: Button
    private lateinit var playerSidesGroup: RadioGroup
    private lateinit var playerOneLeftSide: RadioButton
    private lateinit var playerOneRightSide: RadioButton
    private lateinit var playerTwoLeftSide: RadioButton
    private lateinit var playerTwoRightSide: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (requireActivity() is TournamentDashboardNavigation) {
            (requireActivity() as TournamentDashboardNavigation).hideBottomNavigation()
        }
        arguments?.let {
            matchWithPlayers = it.getSerializable(MATCH_PARAM) as MatchWithPlayers
            match = matchWithPlayers.match
            playerOne = matchWithPlayers.player1
            playerTwo = matchWithPlayers.player2
        }
        DaggerRefereeFragmentComponent.builder()
                .roomModule(RoomModule(requireActivity().application))
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_referee_mode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        setsToWin = (match.bestOf?.plus(1))?.div(2) ?: -1
        playerOneNameHolder.text = playerOne.name
        playerTwoNameHolder.text = playerTwo.name

        initModel()
    }

    private fun initModel() {
        model = ViewModelProvider(this, RefereeModelFactory(matchWithPlayers.results))
                .get(RefereeModel::class.java)

        model.results.observe(viewLifecycleOwner, { results ->
            val lastResult = results.lastOrNull()
            resultObserver(lastResult)
        })
    }

    private fun init(view: View) {
        playerOneNameHolder = view.findViewById(R.id.player_one_name)
        playerTwoNameHolder = view.findViewById(R.id.player_two_name)
        playerOneScoreBtn = view.findViewById(R.id.player_one_add_point_btn)
        playerTwoScoreBtn = view.findViewById(R.id.player_two_add_point_btn)
        playerOneSetNumber = view.findViewById(R.id.player_one_set)
        playerTwoSetNumber = view.findViewById(R.id.player_two_set)
        endGameButton = view.findViewById(R.id.end_game_btn)
        nextSetButton = view.findViewById(R.id.next_set_btn)
        revertPointButton = view.findViewById(R.id.revert_point_btn)
        playerSidesGroup = view.findViewById(R.id.player_sides)
        playerOneLeftSide = view.findViewById(R.id.player_one_left_side_radio)
        playerOneRightSide = view.findViewById(R.id.player_one_right_side_radio)
        playerTwoLeftSide = view.findViewById(R.id.player_two_left_side_btn)
        playerTwoRightSide = view.findViewById(R.id.player_two_right_side_btn)

        playerOneScoreBtn.setOnClickListener { scoreButtonListener(1) }
        playerTwoScoreBtn.setOnClickListener { scoreButtonListener(2) }
        endGameButton.setOnClickListener { endGameListener() }
        nextSetButton.setOnClickListener { nextSetListener() }
        revertPointButton.setOnClickListener { revertPointListener() }
    }

    private fun resultObserver(lastResult: Result?) {
        playerOneScore = lastResult?.playerOneScore ?: 0
        playerOneSet = lastResult?.playerOneSet ?: 0
        playerTwoScore = lastResult?.playerTwoScore ?: 0
        playerTwoSet = lastResult?.playerTwoSet ?: 0

        playerOneScoreBtn.text = playerOneScore.toString()
        playerOneSetNumber.text = playerOneSet.toString()
        playerTwoScoreBtn.text = playerTwoScore.toString()
        playerTwoSetNumber.text = playerTwoSet.toString()

        if (!match.isFinished && checkIfSetEnded()) {
            endSet(lastResult)
        }
    }

    private fun scoreButtonListener(player: Int) {
        val side = setSide(player)

        val result = if (player == 1) {
            Result(++playerOneScore, playerTwoScore, side, player, playerOneSet, playerTwoSet, match.id)
        } else {
            Result(playerOneScore, ++playerTwoScore, side, player, playerOneSet, playerTwoSet, match.id)
        }
        saveResult(result)
    }

    private fun setSide(player: Int): String {

        return if (player == 1) {
            if (playerSidesGroup.checkedRadioButtonId == playerOneLeftSide.id) {
                playerOneRightSide.isChecked = true
                playerOneRightSide.text.toString()
            } else {
                playerOneLeftSide.isChecked = true
                playerOneLeftSide.text.toString()
            }
        } else {
            if (playerSidesGroup.checkedRadioButtonId == playerTwoLeftSide.id) {
                playerTwoRightSide.isChecked = true
                playerTwoRightSide.text.toString()
            } else {
                playerTwoLeftSide.isChecked = true
                playerTwoLeftSide.text.toString()
            }
        }
    }

    private fun saveResult(result: Result) {
        runBlocking {
            launch(Dispatchers.IO) {
                resultService.addPoint(result).also {
                    model.addPoint(it)
                }
            }
        }
    }

    private fun checkIfSetEnded(): Boolean {
        var ended = false;
        if (playerOneScore >= 11 || playerTwoScore >= 11) {
            if (match.isTwoPointsAdvantage) {
                if (abs(playerOneScore - playerTwoScore) >= 2) {
                    ended = true
                }
            } else {
                ended = true
            }
        }
        return ended
    }

    private fun endSet(result: Result?) {
        if ((result?.serve == 1 && playerOneSet.plus(1) == setsToWin)
                || (result?.serve == 2 && playerTwoSet.plus(1) == setsToWin)) {
            endGame()
        } else {
            // todo show this popup only when point added
            val popupDialogFragment = PopupDialogFragment(END_SET)
            popupDialogFragment.show(parentFragmentManager, TAG)
            changeFinishedSetVisibility()
        }
    }

    private fun changeFinishedSetVisibility() {
        endGameButton.visibility = View.GONE
        nextSetButton.visibility = View.VISIBLE
        playerOneScoreBtn.isEnabled = false
        playerTwoScoreBtn.isEnabled = false
    }

    private fun changeToStandardVisibility() {
        endGameButton.visibility = View.VISIBLE
        nextSetButton.visibility = View.GONE
        playerOneScoreBtn.isEnabled = true
        playerTwoScoreBtn.isEnabled = true
    }

    private fun endGame() {
        playerOneScoreBtn.isEnabled = false
        playerTwoScoreBtn.isEnabled = false
        finishMatch(true)
        val popupDialogFragment = PopupDialogFragment(END_GAME)
        popupDialogFragment.show(parentFragmentManager, TAG)
    }

    private fun endGameListener() {
        if (match.isFinished) {
            val result = if (playerOneScore > playerTwoScore) {
                Result(0, 0, getString(R.string.left_side), 1, ++playerOneSet, playerTwoSet, match.id)
            } else {
                Result(0, 0, getString(R.string.left_side), 1, playerOneSet, ++playerTwoSet, match.id)
            }
            saveResult(result)
            exitRefereeMode()
        } else {
            val popupDialogFragment = PopupDialogFragment(WALKOVER)
            popupDialogFragment.show(parentFragmentManager, TAG)
            finishMatch(true)
            exitRefereeMode()
        }
    }

    private fun exitRefereeMode() {
        if (parentFragmentManager.backStackEntryCount > 0) {
            parentFragmentManager.popBackStackImmediate()
        } else {
            requireActivity().finish()
        }
    }

    private fun finishMatch(finished: Boolean) {
        match.isFinished = finished
        runBlocking {
            launch(Dispatchers.IO) {
                matchService.updateMatch(match)
            }
        }
    }

    private fun revertPointListener() {
        if (match.isFinished) {
            playerOneScoreBtn.isEnabled = true
            playerTwoScoreBtn.isEnabled = true
            finishMatch(false)
        }
        revertPoint()?.also {
            if (it.playerOneScore == 0
                    && it.playerTwoScore == 0) {
                changeFinishedSetVisibility()
            } else {
                changeToStandardVisibility()
            }
        }
    }

    private fun revertPoint(): Result? {
        return model.revertPoint().also {
            runBlocking {
                launch(Dispatchers.IO) {
                    resultService.revertPoint(it)
                }
                checkSides()
            }
        }
    }

    private fun checkSides() {
        val lastResult = getLastResult()

        playerSidesGroup.clearCheck()

        if (lastResult?.serve == 1) {
            if (lastResult.side == getString(R.string.right_side)) {
                playerOneRightSide.isChecked = true
            } else {
                playerOneLeftSide.isChecked = true
            }
        } else {
            if (lastResult?.side == getString(R.string.right_side)) {
                playerTwoRightSide.isChecked = true
            } else {
                playerTwoLeftSide.isChecked = true
            }
        }
    }

    private fun nextSetListener() {
        val result = if (playerOneScore > playerTwoScore) {
            Result(0, 0, getString(R.string.left_side), 1, ++playerOneSet, playerTwoSet, match.id)
        } else {
            Result(0, 0, getString(R.string.left_side), 1, playerOneSet, ++playerTwoSet, match.id)
        }
        saveResult(result)
        changeToStandardVisibility()
    }

    private fun getLastResult(): Result? {
        return model.getLastResult()
    }
}
