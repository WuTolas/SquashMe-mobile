package pl.pjatk.squashme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val PLAYER_ONE_NAME: String = "paramNameOne"
private const val PLAYER_TWO_NAME: String = "paramNameTwo"

class RefereeFragment : Fragment() {
    private var playerOneName: String? = null
    private var playerTwoName: String? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playerOneName = it.getString(PLAYER_ONE_NAME)
            playerTwoName = it.getString(PLAYER_TWO_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_referee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        playerOneNameHolder.text = playerOneName
        playerTwoNameHolder.text = playerTwoName
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
    }

    companion object {
        /**
         * @param playerOneName Parameter 1.
         * @param playerTwoName Parameter 2.
         * @return A new instance of fragment RefereeFragment.
         */
        @JvmStatic
        fun newInstance(playerOneName: String, playerTwoName: String) =
                RefereeFragment().apply {
                    arguments = Bundle().apply {
                        putString(PLAYER_ONE_NAME, playerOneName)
                        putString(PLAYER_TWO_NAME, playerTwoName)
                    }
                }
    }
}
