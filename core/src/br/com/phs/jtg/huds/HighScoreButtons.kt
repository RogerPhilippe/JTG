package br.com.phs.jtg.huds

import br.com.phs.jtg.GameMain
import br.com.phs.jtg.helpers.GameInfo
import br.com.phs.jtg.helpers.GameManager
import br.com.phs.jtg.scenes.MainMenu
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class HighScoreButtons(private val game: GameMain) {

    private var stage: Stage
    private var gameViewport: Viewport = FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, OrthographicCamera())
    private lateinit var backBtn: ImageButton
    private lateinit var scoreLabel: Label
    private lateinit var coinLabel: Label

    init {

        stage = Stage(gameViewport, game.spriteBatch)

        Gdx.input.inputProcessor = stage

        createAndPositionUIElements()

        stage.addActor(backBtn)
        stage.addActor(scoreLabel)
        stage.addActor(coinLabel)
    }

    private fun createAndPositionUIElements() {

        backBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Options Menu/Back.png"))))
        backBtn.setPosition(33f, 33f, Align.center)
        backBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = MainMenu(game)
            }
        })

        val generator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 40

        val scoreFont = generator.generateFont(parameter)
        val coinFont = generator.generateFont(parameter)

        scoreLabel = Label(GameManager.getGameData()?.highScore.toString(), Label.LabelStyle(scoreFont, Color.WHITE))
        coinLabel = Label(GameManager.getGameData()?.coinHighScore.toString(), Label.LabelStyle(coinFont, Color.WHITE))

        scoreLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2 - 120)
        coinLabel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2 - 220)

    }

    fun getStage() = this.stage

}