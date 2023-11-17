package com.example.tictactoe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.MyData
import androidx.fragment.app.activityViewModels

class ChoosePlayerFragment : Fragment() {

    interface Callbacks{
        fun onStartGameSelected()
    }

    private var callbacks: Callbacks? = null
    private val myData: MyData by activityViewModels()
    private lateinit var startGameBtn: Button
    private lateinit var playWithCompChBox: CheckBox
    private lateinit var p1Name: EditText
    private lateinit var p2Name: EditText


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_choose_player, container, false)

        startGameBtn = view.findViewById<Button>(R.id.startBtn)
        playWithCompChBox = view.findViewById(R.id.playWComputerChBox)
        p1Name = view.findViewById(R.id.player1)
        p2Name = view.findViewById(R.id.player2)

        (activity as AppCompatActivity)!!.supportActionBar!!.hide()

        playWithCompChBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                p2Name.setEnabled(false)
                myData.setTwoPlayers(false)

            }
            else{
                p2Name.setEnabled(true)
                myData.setTwoPlayers(false)
            }
        }


        startGameBtn.setOnClickListener()
        {
            if(p1Name.text.length > 0){
            myData.setPlayer1Name(p1Name.text.toString())

            }
            if(myData.twoPlayers.value == true && p2Name.text.length > 0){
                myData.setPlayer2Name(p2Name.text.toString())
            }
            callbacks?.onStartGameSelected()

        }
        return view
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


}