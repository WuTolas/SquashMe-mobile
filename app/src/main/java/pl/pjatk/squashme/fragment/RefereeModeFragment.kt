package pl.pjatk.squashme.fragment

import android.os.Bundle
import android.util.Log
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
import pl.pjatk.squashme.model.Match
import pl.pjatk.squashme.service.MatchService
import javax.inject.Inject

private const val MATCH_PARAM = "match"

class RefereeModeFragment : Fragment() {

    @Inject
    lateinit var matchService: MatchService

    private lateinit var match: Match
    private val setsToWin: Int = match.bestOf / 2 + 1

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            match = it.getSerializable(MATCH_PARAM) as Match
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
        playerOneNameHolder.text = match.player1
        playerTwoNameHolder.text = match.player2
        playerOneScoreBtn.text = playerOneScore.toString()
        playerTwoScoreBtn.text = playerTwoScore.toString()
        playerOneSetNumber.text = playerOneSet.toString()
        playerTwoSetNumber.text = playerTwoSet.toString()
    }

    private fun init(view: View) {
        playerOneNameHolder = view.findViewById(R.id.player_one_name)
        playerTwoNameHolder = view.findViewById(R.id.player_two_name)
        playerOneScoreBtn = view.findViewById(R.id.player_one_add_point_btn)
        playerTwoScoreBtn = view.findViewById(R.id.player_two_add_point_btn)
        playerOneSetNumber = view.findViewById(R.id.player_one_set)
        playerTwoSetNumber = view.findViewById(R.id.player_two_set)
        endGameButton = view.findViewById(R.id.end_game_btn)

        playerOneScoreBtn.setOnClickListener { scoreButtonListener("player1") }
        playerTwoScoreBtn.setOnClickListener { scoreButtonListener("player2") }
        endGameButton.setOnClickListener { endGameListener() }
    }

    private fun scoreButtonListener(player: String) {
        if (player == "player1") {
            checkIfWon(++playerOneScore, player)
            playerOneScoreBtn.text = playerOneScore.toString()
            matchService.addPoint()
        } else {
            checkIfWon(++playerTwoScore, player)
            playerTwoScoreBtn.text = playerTwoScore.toString()
            matchService.addPoint()
        }
    }

    private fun checkIfWon(score: Int, player: String) {
        Log.i("referee", "score: $score")
        if (score >= 11) {
            if (match.isTwoPointsAdvantage) {
                // check if two points advantage
            } else {
                // end set [call service]
                // check if match ends
                endSet(player)
            }
        }
    }

    private fun endSet(player: String) {
        // popup with timer 90 secs - countdown
        if (player == "player1") {
            playerOneSetNumber.text = (++playerOneSet).toString()
            if (playerOneSet == setsToWin) {
                endGameListener()
            }
        } else {
            playerTwoSetNumber.text = (++playerTwoSet).toString()
            if (playerTwoSet == setsToWin) {
                endGameListener()
            }
        }
        playerOneScore = 0
        playerTwoScore = 0
    }

    private fun endGameListener() {
        match.isFinished = true
        runBlocking {
            launch(Dispatchers.IO) {
                matchService.updateMatch(match)
                requireActivity().finish()
            }
        }
    }

    private fun revertPointListener() {

    }
}
