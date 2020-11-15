package br.com.phs.jtg.helpers

data class GameData (
    var highScore: Int = 0,
    var coinHighScore: Int  = 0,
    var easyDifficulty: Boolean = false,
    var mediumDifficulty: Boolean  = true,
    var hardDifficulty: Boolean  = false,
    var musicOn: Boolean  = true
)

