package br.com.phs.jtg.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.utils.Base64Coder
import com.badlogic.gdx.utils.Json

object GameManager {

    private var gameData: GameData? = null
    private var json: Json = Json()
    private var fileHandle = Gdx.files.local("bin/GameData.json")
    var gameStartedFromMainMenu: Boolean = true
    var isPaused: Boolean = true
    var lifeScore: Int = 0
    var coinScore: Int = 0
    var score: Int = 0
    private var music: Music? = null

    fun initializeGameData() {

        if (!fileHandle.exists()) {
            gameData = GameData()
            saveData()
        } else
            loadData()
    }

    fun saveData() {
        if (gameData != null) {
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false)
        }
    }

    private fun loadData() {

        gameData = json.fromJson(GameData::class.java, Base64Coder.decodeString(fileHandle.readString()))
    }

    fun checkForNewHighScores() {

        val oldHighScore = gameData?.highScore ?: 0
        val oldCoinScore = gameData?.coinHighScore ?: 0

        if (oldHighScore < score) {
            gameData?.highScore = score
        }

        if (oldCoinScore < coinScore) {
            gameData?.coinHighScore = coinScore
        }

        saveData()
    }

    fun playMusic() {

        if (music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Background.mp3"))
        }

        if (music?.isPlaying != true) {
            music?.play()
        }
    }

    fun stopMusic() {

        if (music?.isPlaying == true) {
            music?.stop()
            music?.dispose()
        }
    }

    fun getGameData() = this.gameData

}