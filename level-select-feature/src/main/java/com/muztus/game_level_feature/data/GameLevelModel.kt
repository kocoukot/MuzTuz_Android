package com.muztus.game_level_feature.data

import com.muztus.domain_layer.model.HintModel
import com.muztus.domain_layer.model.LevelHints

interface GameLevelModel {

    fun checkUserInput(userInput: String): Boolean = false

    fun hintsRow(): List<HintModel> = emptyList()

    fun getLevelImage(): Int = 0

    fun getLevelSongHint(): String = ""
    fun getLettersAmount(): String = ""
    fun getCorrectAnswer(): String = ""


    fun lettersAmountHintUse(): GameLevelModel = this
    fun onOneLetterHintUse(letterIndex: Int): GameLevelModel = this
    fun songHintUse(): GameLevelModel = this


    data class Base(
        private val index: Int,
        private val premiumIndex: Int,
        private val correctAnswers: List<String>,
        private var levelHints: LevelHints,
        private val levelImage: Int,
        private val songName: String,
        private val isSolved: Boolean,
    ) : GameLevelModel {

        override fun checkUserInput(userInput: String): Boolean =
            correctAnswers.any { it.equals(userInput.trim().lowercase(), true) }

        override fun hintsRow(): List<HintModel> = levelHints.getHintsList()

        override fun getLevelImage(): Int = levelImage

        override fun getLevelSongHint(): String = songName

        override fun getLettersAmount(): String =
            if (levelHints.letterAmountHint.isEnabled() || levelHints.oneLetterHint.isEnabled()) {
                val answer = correctAnswers.first()
                val choosenLetterIndex = levelHints.oneLetterHint.selectedLetters
                val choosenLetter =
                    if (choosenLetterIndex >= 0) answer[choosenLetterIndex].uppercase() else ""
                correctAnswers.first().mapIndexed { index, char ->
                    if (choosenLetterIndex == index) choosenLetter else {
                        if (char.equals(' ', true)) " " else "_"
                    }
                }.joinToString(" ")
            } else ""

        override fun getCorrectAnswer(): String = correctAnswers.first()

        override fun lettersAmountHintUse(): GameLevelModel {
            levelHints.letterAmountHint = levelHints.letterAmountHint.copy(isUsed = true)
            return this
        }

        override fun onOneLetterHintUse(letterIndex: Int): GameLevelModel {
            levelHints.letterAmountHint.useHint()
            levelHints.oneLetterHint.useHint()
            levelHints.oneLetterHint = levelHints.oneLetterHint.copy(selectedLetters = letterIndex)
            return this
        }

        override fun songHintUse(): GameLevelModel {
            levelHints.songHint.useHint()
            return this
        }


    }


    object Empty : GameLevelModel
}