package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.MyData
import androidx.fragment.app.activityViewModels
import kotlin.random.Random

class GameFragment : Fragment() {
    private var callbacks: ChoosePlayerFragment.Callbacks? = null
    private val myData: MyData by activityViewModels()
    lateinit var resetBtn: Button
    lateinit var startAgainBtn: Button
    lateinit var player1ScTV: TextView
    lateinit var player2ScTV: TextView
    lateinit var message: TextView
    lateinit var playTable: TableLayout


    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var empty = ArrayList<Int>()
    var tableButtons = ArrayList<Button>()
    var activePlayer = 1
    var player1Score = 0
    var player2Score = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as ChoosePlayerFragment.Callbacks?

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        resetBtn = view.findViewById<Button>(R.id.resetBtn)
        startAgainBtn = view.findViewById<LinearLayout>(R.id.startAgainLayout).findViewById<Button>(R.id.startAgainBtn)
        player1ScTV = view.findViewById<TextView>(R.id.player1Score)
        player2ScTV = view.findViewById<TextView>(R.id.player2Score)
        message = view.findViewById<LinearLayout>(R.id.msgLayout).findViewById<TextView>(R.id.messageTxt)
        playTable = view.findViewById(R.id.tableLayout)
        myData.setPlayer1Name(myData.player1Name.value?.capitalize().toString())
        if (myData.twoPlayers.value == true){
            myData.setPlayer2Name(myData.player2Name.value?.capitalize().toString())
        }

        (activity as AppCompatActivity)!!.supportActionBar!!.hide()
        storeKeyboardButtons()
        renderScores()

        for (b in tableButtons) {
            b.setOnClickListener() {
                var cellID = 0

                when (b.id) {
                    R.id.b1 -> cellID = 1
                    R.id.b2 -> cellID = 2
                    R.id.b3 -> cellID = 3
                    R.id.b4 -> cellID = 4
                    R.id.b5 -> cellID = 5
                    R.id.b6 -> cellID = 6
                    R.id.b7 -> cellID = 7
                    R.id.b8 -> cellID = 8
                    R.id.b9 -> cellID = 9
                }

                PlayGame(cellID, b)
                if(myData.twoPlayers.value == false && empty.size < 9) {

                    renderMessage("Computer makes its turn")
//                                  toggleBlockTable(false)
                    object : CountDownTimer(500, 20) {

                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {
                            var selectedNum = Random.nextInt(1, 9)

                            while (empty.contains(selectedNum)){
                                selectedNum = Random.nextInt(1, 9)
                            }
                            val btnId = getResources().getIdentifier("b" + selectedNum, "id", "com.example.tictactoe")
                            val selectedBtn = view.findViewById<Button>(btnId)


                            PlayGame(selectedNum, selectedBtn)

                        }
                    }.start()
                }
            }
        }

        resetBtn.setOnClickListener(){
            init()
        }

        startAgainBtn.setOnClickListener(){
            player1Score = 0
            player2Score = 0
            renderScores()
            init()
        }
        init()
        return view
    }


    fun init(){
        player1.clear()
        player2.clear()
        empty.clear()
        resetTable()
        activePlayer = 1
        toggleBlockTable(true)
    }

    fun renderMessage(text: String){
        message.text = text
    }

    fun declareWinner(winner: Int){
        renderMessage("Game is over!")
        if (winner == 1) {
            Toast.makeText(activity,  myData.player1Name.value?.toString() + " wins the game!", Toast.LENGTH_LONG).show()
            player1Score++
            renderMessage(myData.player1Name.value?.toString() + " wins the game!")
        } else {
            if(myData.twoPlayers.value == true){
                Toast.makeText(activity, myData.player2Name.value?.toString() + " wins the game!", Toast.LENGTH_LONG).show()
                renderMessage(myData.player2Name.value?.toString() + " wins the game!")

            }else{
                Toast.makeText(activity, "Computer wins the game!", Toast.LENGTH_LONG).show()
                renderMessage("Computer wins the game!")
            }

            player2Score++
        }

        renderScores()
    }

    fun renderScores(){
        player1ScTV.text = myData.player1Name.value?.capitalize() + " " + player1Score

        if(myData.twoPlayers.value == true){
            player2ScTV.text = myData.player2Name.value?.capitalize() + "  "  + player2Score

        }else{
            player2ScTV.text = "Computer  "  + player2Score
        }
    }


    fun storeKeyboardButtons(){
        val layoutButtons = playTable.getTouchables();
        for(v in layoutButtons){
            if( v is Button ) {
                tableButtons.add(v);
            }
        }
    }

    fun toggleBlockTable(whichState: Boolean){
        //whichState == true enables ALL the buttons
        //whichState == false disables ALL the buttons
        for (v in tableButtons) {
            if(!(empty.contains(v.id))){
                v.setEnabled(whichState);
            }

        }
    }

    fun resetTable(){
        for (v in tableButtons) {
            v.text = ""
        }

    }

    fun PlayGame(cellID:Int, bSelected:Button){

        if (activePlayer == 1){
            bSelected.text = "X"
            bSelected.setBackgroundColor(Color.rgb(34, 92, 13))
            player1.add(cellID)
            empty.add(cellID)
            bSelected.isEnabled = false
            activePlayer = 2
            renderMessage(myData.player2Name.value.toString() + "'s turn")

        }
        else
        {
            bSelected.text = "O"
            bSelected.setBackgroundColor(Color.rgb(13, 90, 92))
            player2.add(cellID)
            empty.add(cellID)
            bSelected.isEnabled = false
            activePlayer = 1
            renderMessage(myData.player1Name.value.toString() + "'s turn")
        }
        CheckWinner()


    }




    fun CheckWinner() {
        var winner = -1

        if (player1.contains(1) && player1.contains(2) && player1.contains(3)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)) {
            winner = 2
        }

        if (player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner = 1
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner = 2
        }

        if (player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner = 2
        }

        if (player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner = 2
        }

        if (player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner = 1
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner = 2
        }

        if (player1.contains(3) && player1.contains(6) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)) {
            winner = 2
        }

        if (player1.contains(1) && player1.contains(5) && player1.contains(9)) {
            winner = 1
        }
        if (player2.contains(1) && player2.contains(5) && player2.contains(9)) {
            winner = 2
        }

        if (player1.contains(3) && player1.contains(5) && player1.contains(7)) {
            winner = 1
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner = 2
        }

        if (winner != -1) {
            declareWinner(winner)
            toggleBlockTable(false)
        }

        if(empty.size >= 9){
            renderMessage("Game is over! Reset the game")
        }


    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


}





