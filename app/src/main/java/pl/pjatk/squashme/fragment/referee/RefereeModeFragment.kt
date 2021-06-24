package pl.pjatk.squashme.fragment.referee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
import kotlin.math.max

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
    private var matchToFinish: Boolean = false
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
        setFragmentResultsListeners()
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

    /**
     * fragment result listeners monitor result of popup fragment
     * to take proper actions based on user's decision
     */
    private fun setFragmentResultsListeners() {
        setFragmentResultListener(PopupDialogFragment.WINNER_KEY) { key, bundle ->
            val winner = bundle.getInt(key)
            setWinnerByWalkover(winner)
        }
        setFragmentResultListener(PopupDialogFragment.END_GAME_KEY) { key, bundle ->
            val finishGame = bundle.getBoolean(key)
            if (finishGame) {
                endGameAndExit()
            }
        }
    }

    /**
     * initializes model and sets up observer on scores
     */
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

    /**
     * prepare data based on last result or set default
     * @param lastResult: Result?
     */
    private fun resultObserver(lastResult: Result?) {
        playerOneScore = lastResult?.playerOneScore ?: 0
        playerOneSet = lastResult?.playerOneSet ?: 0
        playerTwoScore = lastResult?.playerTwoScore ?: 0
        playerTwoSet = lastResult?.playerTwoSet ?: 0

        playerOneScoreBtn.text = playerOneScore.toString()
        playerOneSetNumber.text = playerOneSet.toString()
        playerTwoScoreBtn.text = playerTwoScore.toString()
        playerTwoSetNumber.text = playerTwoSet.toString()

        checkSides(lastResult)
        matchToFinish = checkIfMatchEnded()

        if (!matchToFinish && checkIfSetEnded()) {
            changeFinishedSetVisibility()
        }
    }

    /**
     * keep track on scores
     * @param player: Int
     */
    private fun scoreButtonListener(player: Int) {
        val side = setSide(player)

        val result = if (player == 1) {
            Result(++playerOneScore, playerTwoScore, side, player, playerOneSet, playerTwoSet, match.id)
        } else {
            Result(playerOneScore, ++playerTwoScore, side, player, playerOneSet, playerTwoSet, match.id)
        }
        saveResult(result)

        if (!matchToFinish && checkIfSetEnded()) {
            endSet(result)
        }
    }

    /**
     * change color of side indicator to visualise which side and which player should serve
     * @param player: Int
     */
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

    /**
     * use coroutines to update database and live data model
     * with updated result (score)
     * @param result: Result
     */
    private fun saveResult(result: Result) {
        runBlocking {
            launch(Dispatchers.IO) {
                resultService.addPoint(result).also {
                    model.addPoint(it)
                }
            }
        }
    }

    /**
     * check if set ended based on players' score
     * @return information if set is ended
     */
    private fun checkIfSetEnded(): Boolean {
        var ended = false
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

    /**
     * check if match ended based on players' score
     * @return information if match is ended
     */
    private fun checkIfMatchEnded(): Boolean {
        var ended = false
        val setNumber = max(playerOneSet, playerTwoSet)
        if (checkIfSetEnded() && setNumber == setsToWin - 1) {
            ended = true
        }
        return ended
    }

    /**
     * end set and show popup with 90 seconds break
     * or end game if appropriate
     * @param result Result?
     */
    private fun endSet(result: Result?) {
        if ((result?.serve == 1 && playerOneSet.plus(1) == setsToWin)
                || (result?.serve == 2 && playerTwoSet.plus(1) == setsToWin)) {
            endGame()
        } else {
            val popupDialogFragment = PopupDialogFragment(END_SET)
            popupDialogFragment.show(parentFragmentManager, TAG)
            changeFinishedSetVisibility()
        }
    }

    /**
     * disable score buttons
     * show next set button
     */
    private fun changeFinishedSetVisibility() {
        endGameButton.visibility = View.GONE
        nextSetButton.visibility = View.VISIBLE
        changeScoreButtonsAccessibility(false)
    }

    /**
     * enable score buttons
     * hide next set button
     */
    private fun changeToStandardVisibility() {
        endGameButton.visibility = View.VISIBLE
        nextSetButton.visibility = View.GONE
        changeScoreButtonsAccessibility(true)
    }

    /**
     * enable / disable score buttons
     * @param visible Boolean
     */
    private fun changeScoreButtonsAccessibility(visible: Boolean) {
        playerOneScoreBtn.isEnabled = visible
        playerTwoScoreBtn.isEnabled = visible
    }

    /**
     * disable score buttons
     * show popup with end game info
     */
    private fun endGame() {
        changeScoreButtonsAccessibility(false)
        matchToFinish = true
        val popupDialogFragment = PopupDialogFragment(END_GAME)
        popupDialogFragment.show(parentFragmentManager, TAG)
    }

    /**
     * add last point when match is finished
     * show popup to end game or walkover
     */
    private fun endGameListener() {
        if (matchToFinish) {
            Log.i(TAG, "match to finish")
            finishMatch(true)
            val result = if (playerOneScore > playerTwoScore) {
                Result(0, 0, getString(R.string.left_side), 1, ++playerOneSet, playerTwoSet, match.id)
            } else {
                Result(0, 0, getString(R.string.left_side), 1, playerOneSet, ++playerTwoSet, match.id)
            }
            saveResult(result)
            exitRefereeMode()
        } else {
            Log.i(TAG, "ending not finished match")
            if (setsToWin == -1) {
                val popupDialogFragment =
                        PopupDialogFragment(END_FRIENDLY_GAME)
                popupDialogFragment.show(parentFragmentManager, TAG)
            } else {
                val popupDialogFragment =
                        PopupDialogFragment(WALKOVER, playerOne.name, playerTwo.name)
                popupDialogFragment.show(parentFragmentManager, TAG)
            }
        }
    }

    /**
     * add remaining sets to winner's score on walkover
     * @param winner Int
     */
    private fun setWinnerByWalkover(winner: Int) {
        if (winner == 1) {
            val remainingSets = setsToWin - playerOneSet
            for (i in 0 until remainingSets) {
                saveResult(Result(0, 0, getString(R.string.left_side), 1, ++playerOneSet, playerTwoSet, match.id))
            }
        } else {
            val remainingSets = setsToWin - playerTwoSet
            for (i in 0 until remainingSets) {
                saveResult(Result(0, 0, getString(R.string.left_side), 1, playerOneSet, ++playerTwoSet, match.id))
            }
        }
        endGameAndExit()
    }

    /**
     * finish game and exit view
     */
    private fun endGameAndExit() {
        finishMatch(true)
        exitRefereeMode()
    }

    private fun exitRefereeMode() {
        if (parentFragmentManager.backStackEntryCount > 0) {
            parentFragmentManager.popBackStackImmediate()
        } else {
            requireActivity().finish()
        }
    }

    /**
     * update match in database using coroutines
     * @param finished Boolean
     */
    private fun finishMatch(finished: Boolean) {
        matchToFinish = finished
        match.isFinished = finished
        runBlocking {
            launch(Dispatchers.IO) {
                matchService.updateMatch(match)
            }
        }
    }

    /**
     * revert point and change buttons visibility if applicable
     */
    private fun revertPointListener() {
        if (matchToFinish) {
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

    /**
     * delete reverted point from database and model
     */
    private fun revertPoint(): Result? {
        return model.revertPoint().also {
            runBlocking {
                launch(Dispatchers.IO) {
                    resultService.revertPoint(it)
                }
            }
        }
    }

    /**
     * mark set side for player to serve (left/right)
     * @param lastResult Result
     */
    private fun checkSides(lastResult: Result?) {
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

    /**
     * change set number and save result
     */
    private fun nextSetListener() {
        val result = if (playerOneScore > playerTwoScore) {
            Result(0, 0, getString(R.string.left_side), 1, ++playerOneSet, playerTwoSet, match.id)
        } else {
            Result(0, 0, getString(R.string.left_side), 1, playerOneSet, ++playerTwoSet, match.id)
        }
        saveResult(result)
        changeToStandardVisibility()
    }
}
