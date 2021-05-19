package pl.pjatk.squashme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.pjatk.squashme.R
import pl.pjatk.squashme.di.component.DaggerRefereeFragmentComponent
import pl.pjatk.squashme.di.module.RoomModule
import pl.pjatk.squashme.model.*
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

    private lateinit var matchWithPlayers: MatchWithPlayers
    private lateinit var match: Match
    private lateinit var results: MutableList<Result>
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
    private lateinit var revertPointButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            matchWithPlayers = it.getSerializable(MATCH_PARAM) as MatchWithPlayers
            match = matchWithPlayers.match
            results = matchWithPlayers.results
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
        return inflater.inflate(R.layout.fragment_referee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        loadSavedResults()

        setsToWin = (match.bestOf + 1) / 2
        playerOneNameHolder.text = playerOne.name
        playerTwoNameHolder.text = playerTwo.name
        playerOneScoreBtn.text = playerOneScore.toString()
        playerTwoScoreBtn.text = playerTwoScore.toString()
        playerOneSetNumber.text = playerOneSet.toString()
        playerTwoSetNumber.text = playerTwoSet.toString()
    }

    /**
    check saved results to display
     */
    private fun loadSavedResults() {
        if (results.isNotEmpty()) { // to refactor
            val playerOneSavedResult = results
                    .sortedByDescending { result -> result.id }
                    .firstOrNull { result -> result.player == 1 }
            playerOneScore = playerOneSavedResult?.point ?: 0
            playerOneSet = playerOneSavedResult?.set ?: 0
            val playerTwoSavedResult = results
                    .sortedByDescending { result -> result.id }
                    .firstOrNull { result -> result.player == 2 }
            playerTwoScore = playerTwoSavedResult?.point ?: 0
            playerTwoSet = playerTwoSavedResult?.set ?: 0
        }
    }

    private fun init(view: View) {
        playerOneNameHolder = view.findViewById(R.id.player_one_name)
        playerTwoNameHolder = view.findViewById(R.id.player_two_name)
        playerOneScoreBtn = view.findViewById(R.id.player_one_add_point_btn)
        playerTwoScoreBtn = view.findViewById(R.id.player_two_add_point_btn)
        playerOneSetNumber = view.findViewById(R.id.player_one_set)
        playerTwoSetNumber = view.findViewById(R.id.player_two_set)
        endGameButton = view.findViewById(R.id.end_game_btn)
        revertPointButton = view.findViewById(R.id.revert_point_btn)

        playerOneScoreBtn.setOnClickListener { scoreButtonListener(1) }
        playerTwoScoreBtn.setOnClickListener { scoreButtonListener(2) }
        endGameButton.setOnClickListener { endGameListener() }
        revertPointButton.setOnClickListener { revertPointListener() }
    }

    private fun scoreButtonListener(player: Int) {
        val result = if (player == 1) {
            Result(player, 'L', ++playerOneScore, playerOneSet, match.id).also {
                playerOneScoreBtn.text = it.point.toString()
                checkIfWon(playerOneScore, player)
            }
        } else {
            Result(player, 'L', ++playerTwoScore, playerTwoSet, match.id).also {
                playerTwoScoreBtn.text = it.point.toString()
                checkIfWon(playerTwoScore, player)
            }
        }
        saveResult(result)
        results.add(result)
    }

    private fun saveResult(result: Result) {
        runBlocking {
            launch(Dispatchers.IO) {
                resultService.addPoint(result)
            }
        }
    }

    private fun checkIfWon(score: Int, player: Int) {
        if (score >= 11) {
            if (match.isTwoPointsAdvantage) {
                if (abs(playerOneScore - playerTwoScore) >= 2) {
                    endSet(player)
                }
            } else {
                endSet(player)
            }
        }
    }

    private fun endSet(player: Int) {
        if (player == 1) {
            playerOneSetNumber.text = (++playerOneSet).toString()
            if (playerOneSet == setsToWin) {
                endGame()
            }
        } else {
            playerTwoSetNumber.text = (++playerTwoSet).toString()
            if (playerTwoSet == setsToWin) {
                endGame()
            }
        }
        if (!match.isFinished) {
            PopupDialogFragment().show(parentFragmentManager, TAG)
            playerOneScore = 0
            playerTwoScore = 0
            playerOneScoreBtn.text = playerOneScore.toString()
            playerTwoScoreBtn.text = playerTwoScore.toString()
        }
    }

    private fun endGame() {
        playerOneScoreBtn.isEnabled = false
        playerTwoScoreBtn.isEnabled = false
        finishedMatch(true)
    }

    private fun endGameListener() {
        finishedMatch(true)
        requireActivity().finish()
    }

    private fun finishedMatch(finished: Boolean) {
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
            finishedMatch(false)
        }

        if (results.isNotEmpty()) {
            val lastResult = results.last()
            if (lastResult.player == 1) {
                playerOneScoreBtn.text = lastResult.point.minus(1).toString()
            } else {
                playerTwoScoreBtn.text = lastResult.point.minus(1).toString()
            }
            results.remove(lastResult)

            runBlocking {
                launch(Dispatchers.IO) {
                    resultService.revertPoint(lastResult)
                }
            }
        }
    }
}
