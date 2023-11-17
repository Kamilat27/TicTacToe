package com.example.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

class MyData : ViewModel(){
    private val _twoPlayers = MutableLiveData<Boolean>(true)
    var twoPlayers: LiveData<Boolean> = _twoPlayers

    private val _player1Name = MutableLiveData<String>("Player 1")
    var player1Name: LiveData<String> = _player1Name

    private val _player2Name = MutableLiveData<String>("Player 2")
    var player2Name: LiveData<String> = _player2Name

    fun setTwoPlayers(twoPLayersA: Boolean){
        _twoPlayers.value = twoPLayersA
    }

    fun setPlayer1Name(name: String){
        _player1Name.value = name
    }

    fun setPlayer2Name(name: String){
        _player2Name.value = name
    }

}
