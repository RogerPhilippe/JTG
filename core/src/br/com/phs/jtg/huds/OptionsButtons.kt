package br.com.phs.jtg.huds

import br.com.phs.jtg.GameMain
import br.com.phs.jtg.helpers.GameInfo
import br.com.phs.jtg.helpers.GameManager
import br.com.phs.jtg.scenes.MainMenu
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class OptionsButtons(private val game: GameMain) {

    private var stage: Stage
    private var gameViewport: Viewport = FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, OrthographicCamera())
    private lateinit var easyBtn: ImageButton
    private lateinit var mediumBtn: ImageButton
    private lateinit var hardBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var sign: Image

    init {

        stage = Stage(gameViewport, game.spriteBatch)

        Gdx.input.inputProcessor = stage

        createAndPositionButtons()
        setupListeners()

        stage.addActor(easyBtn)
        stage.addActor(mediumBtn)
        stage.addActor(hardBtn)
        stage.addActor(backBtn)
        stage.addActor(sign)

        positionTheSign()

    }

    private fun createAndPositionButtons() {

        easyBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Options Menu/Easy.png"))))
        mediumBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Options Menu/Medium.png"))))
        hardBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Options Menu/Hard.png"))))

        backBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Options Menu/Back.png"))))

        sign = Image(Texture("Buttons/Options Menu/Check Sign.png"))

        easyBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 + 40, Align.center)
        mediumBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 - 40, Align.center)
        hardBtn.setPosition(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2 - 120, Align.center)

        backBtn.setPosition(33f, 33f, Align.center)

        sign.setPosition(GameInfo.WIDTH / 2 + 76, mediumBtn.y + 13)

    }

    private fun setupListeners() {

        easyBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                changeDifficulty(0)
                sign.y = easyBtn.y + 13
            }

        })

        mediumBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                changeDifficulty(1)
                sign.y = mediumBtn.y + 13
            }

        })

        hardBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                changeDifficulty(2)
                sign.y = hardBtn.y + 13
            }

        })

        backBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = MainMenu(game)
            }

        })
    }

    private fun positionTheSign() {

        when {
            GameManager.getGameData()?.easyDifficulty == true -> {
                sign.y = easyBtn.y + 13
            }
            GameManager.getGameData()?.mediumDifficulty == true -> {
                sign.y = mediumBtn.y + 13
            }
            GameManager.getGameData()?.hardDifficulty == true -> {
                sign.y = hardBtn.y + 13
            }
        }
    }

    fun changeDifficulty(difficulty: Int) {

        when(difficulty) {

            0 -> {
                GameManager.getGameData()?.easyDifficulty = true
                GameManager.getGameData()?.mediumDifficulty = false
                GameManager.getGameData()?.hardDifficulty = false
            }
            1 -> {
                GameManager.getGameData()?.easyDifficulty = false
                GameManager.getGameData()?.mediumDifficulty = true
                GameManager.getGameData()?.hardDifficulty = false
            }
            2 -> {
                GameManager.getGameData()?.easyDifficulty = false
                GameManager.getGameData()?.mediumDifficulty = false
                GameManager.getGameData()?.hardDifficulty = true
            }
        }

        GameManager.saveData()
    }

    fun getStage() = this.stage

}