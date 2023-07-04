package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

 class GameViewModel: ViewModel() {

     private val _score = MutableLiveData(0)
     val score: LiveData<Int>
        get() = _score

     private val _currentWordCount = MutableLiveData(0)
     val currentWordCount: LiveData<Int>
        get() = _currentWordCount

     private val _currentScrambledWord =  MutableLiveData<String>()
     val currentScrambledWord: LiveData<String>
         get() = _currentScrambledWord

     private var wordsList: MutableList<String> = mutableListOf()

     private lateinit var _currentWord: String
     val currentWord: String get() = _currentWord
     
     private val _helpCounter: MutableLiveData<Int> = MutableLiveData(3)
     val helpCounter: LiveData<Int> get() = _helpCounter

     init {
         Log.d("GameFragment", "GameViewModel created!")
         getNextWord()
     }

     /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
     private fun getNextWord() {
         _currentWord = allWordsList.random()
         val tempWord = _currentWord.toCharArray().apply {
             shuffle()
             while (String(this) == _currentWord) shuffle()
         }
         if (!wordsList.contains(_currentWord)) {
             _currentScrambledWord.value = String(tempWord)
             _currentWordCount.value = (_currentWordCount.value)?.inc()
             wordsList.add(_currentWord)
         } else getNextWord()
     }

     /*
     * Decrease the help counter by one until getting 0 help.
      */
     fun decreaseHelpCounter(){
         val value = _helpCounter.value
         if (value != null && value > 0)
            _helpCounter.value = _helpCounter.value?.dec()
     }


     /*
     * Returns true if the current word count is less than MAX_NO_OF_WORDS.
     * Updates the next word.
     */
     fun nextWord(): Boolean {
         return if (_currentWordCount.value!! < MAX_NUMBER_OF_WORDS) {
             getNextWord()
             true
         } else false
     }

     private fun increaseScore() {
         _score.value = (_score.value)?.plus(SCORE_INCREASE)
     }

     fun isUserWordCorrect(playerWord: String): Boolean {
         if (playerWord.equals(_currentWord, true)) {
             increaseScore()
             return true
         }
         return false
     }

     fun reinitializeData(){
         _score.value = 0
         _currentWordCount.value = 0
         _helpCounter.value = 3
         wordsList.clear()
         getNextWord()
     }

//     override fun onCleared() {
//         super.onCleared()
//         Log.d("GameFragment", "GameViewModel destroyed!")
//     }
}