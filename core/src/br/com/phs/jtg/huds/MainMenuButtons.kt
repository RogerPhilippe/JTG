package br.com.phs.jtg.huds

import br.com.phs.jtg.GameMain
import br.com.phs.jtg.helpers.GameInfo
import br.com.phs.jtg.helpers.GameManager
import br.com.phs.jtg.scenes.GamePlay
import br.com.phs.jtg.scenes.HighScore
import br.com.phs.jtg.scenes.Options
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class MainMenuButtons(private val game: GameMain) {

    private var stage: Stage
    private var gameViewport: Viewport = FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, OrthographicCamera())

    private lateinit var playBtn: ImageButton
    private lateinit var highScoreBtn: ImageButton
    private lateinit var optionsBtn: ImageButton
    private lateinit var quitBtn: ImageButton
    private lateinit var musicBtn: ImageButton

    init {

        stage = Stage(gameViewport, game.spriteBatch)

        Gdx.input.inputProcessor = stage

        createPositionButtons()

        stage.addActor(playBtn)
        stage.addActor(highScoreBtn)
        stage.addActor(optionsBtn)
        stage.addActor(quitBtn)
        stage.addActor(musicBtn)

        setupListeners()
        checkMusic()
    }

    private fun createPositionButtons() {

        playBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Main Menu/Start Game.png"))))
        highScoreBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Main Menu/Highscore.png"))))
        optionsBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Main Menu/Options.png"))))
        quitBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Main Menu/Quit.png"))))
        musicBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Main Menu/Music On.png"))))

        playBtn.setPosition(GameInfo.WIDTH / 2 - 80, GameInfo.HEIGHT / 2 + 50, Align.center)
        highScoreBtn.setPosition(GameInfo.WIDTH / 2 - 60, GameInfo.HEIGHT / 2 - 20, Align.center)
        optionsBtn.setPosition(GameInfo.WIDTH / 2 - 40, GameInfo.HEIGHT / 2 - 90, Align.center)
        quitBtn.setPosition(GameInfo.WIDTH / 2 - 20, GameInfo.HEIGHT / 2 - 160, Align.center)
        musicBtn.setPosition(GameInfo.WIDTH - 33, 33f, Align.center)
    }

    private fun setupListeners() {

        playBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {

                GameManager.gameStartedFromMainMenu = true

                changeScreen { game.screen = GamePlay(game) }

            }
        })

        highScoreBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                changeScreen { game.screen = HighScore(game) }
            }
        })

        optionsBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                changeScreen { game.screen = Options(game) }
            }
        })

        quitBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {

            }
        })

        musicBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                if (GameManager.getGameData()?.musicOn == true) {
                    GameManager.getGameData()?.musicOn = false
                    GameManager.stopMusic()
                } else {
                    GameManager.getGameData()?.musicOn = true
                    GameManager.playMusic()
                }

                GameManager.saveData()
            }
        })
    }

    private fun changeScreen(function: () -> Unit) {

        val run = RunnableAction()
        run.setRunnable {
            function()
        }

        val sa = SequenceAction()
        sa.addAction(Actions.fadeOut(.5f))
        sa.addAction(run)

        stage.addAction(sa)
    }

    private fun checkMusic() {

        if (GameManager.getGameData()?.musicOn == true) {
            GameManager.playMusic()
        }
    }

    fun getStage() = this.stage


}