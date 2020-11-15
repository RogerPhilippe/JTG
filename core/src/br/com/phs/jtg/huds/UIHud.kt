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
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

class UIHud(private val game: GameMain) {

    private var gameViewport: Viewport = FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, OrthographicCamera())
    private var stage: Stage
    private lateinit var coinImage: Image
    private lateinit var lifeImg: Image
    private lateinit var scoreImg: Image
    private lateinit var coinLabel: Label
    private lateinit var lifeLabel: Label
    private lateinit var scoreLabel: Label
    private lateinit var pauseBtn: ImageButton
    private lateinit var pausePanel: Image
    private lateinit var resumeBtn: ImageButton
    private lateinit var quitBtn: ImageButton

    init {

        stage = Stage(gameViewport, game.spriteBatch)

        Gdx.input.inputProcessor = stage

        if (GameManager.gameStartedFromMainMenu) {
            // Set initial values
            GameManager.gameStartedFromMainMenu = false
            GameManager.lifeScore = 2
            GameManager.coinScore = 0
            GameManager.score = 0
        }

        createLabels()
        createImages()
        createAndListenerBtn()

        val lifeAndCoinTable = Table()
        lifeAndCoinTable.top().left()
        lifeAndCoinTable.setFillParent(true)
        lifeAndCoinTable.add(lifeImg).padLeft(10f).padTop(10f)
        lifeAndCoinTable.add(lifeLabel).padLeft(5f)
        lifeAndCoinTable.row()
        lifeAndCoinTable.add(coinImage).padLeft(10f).padTop(10f)
        lifeAndCoinTable.add(coinLabel).padLeft(5f)

        val scoreTable = Table()
        scoreTable.top().right()
        scoreTable.setFillParent(true)
        scoreTable.add(scoreImg).padRight(10f).padTop(10f)
        scoreTable.row()
        scoreTable.add(scoreLabel).padRight(20f).padTop(15f)

        stage.addActor(lifeAndCoinTable)
        stage.addActor(scoreTable)
        stage.addActor(pauseBtn)
    }

    private fun createLabels() {

        val generator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"))
        val parameters = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameters.size = 40

        val font = generator.generateFont(parameters)

        coinLabel = Label("x${GameManager.coinScore}", Label.LabelStyle(font, Color.WHITE))
        lifeLabel = Label("x${GameManager.lifeScore}", Label.LabelStyle(font, Color.WHITE))
        scoreLabel = Label("${GameManager.score}", Label.LabelStyle(font, Color.WHITE))

    }

    private fun createImages() {

        coinImage = Image(Texture("Collectables/Coin.png"))
        lifeImg = Image(Texture("Collectables/Life.png"))
        scoreImg = Image(Texture("Buttons/Gameplay/Score.png"))
    }

    private fun createAndListenerBtn() {

        pauseBtn = ImageButton(SpriteDrawable(Sprite((Texture("Buttons/Gameplay/Pause.png")))))
        pauseBtn.setPosition(460f, 17f, Align.bottomRight)
        pauseBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {

                if (!GameManager.isPaused) {
                    GameManager.isPaused = true
                    createPausePanel()
                }
            }
        })
    }

    private fun createPausePanel() {

        pausePanel = Image(Texture("Buttons/Pause/Pause Panel.png"))
        resumeBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Pause/Resume.png"))))
        quitBtn = ImageButton(SpriteDrawable(Sprite(Texture("Buttons/Pause/Quit 2.png"))))

        pausePanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center)
        resumeBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2 + 50, Align.center)
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2 - 80, Align.center)

        resumeBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                GameManager.isPaused = false
                removePausePanel()
            }
        })

        quitBtn.addListener( object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                game.screen = MainMenu(game)
            }
        })

        stage.addActor(pausePanel)
        stage.addActor(resumeBtn)
        stage.addActor(quitBtn)
    }

    private fun removePausePanel() {

        pausePanel.remove()
        resumeBtn.remove()
        quitBtn.remove()
    }

    fun createGameOverPanel() {

        val gameOverPanel = Image(Texture("Buttons/Pause/Show Score.png"))
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 60
        val font = generator.generateFont(parameter)
        val endScore = Label(GameManager.score.toString(), Label.LabelStyle(font, Color.WHITE))
        val endCoinScore = Label(GameManager.coinScore.toString(), Label.LabelStyle(font, Color.WHITE))
        gameOverPanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center)
        endScore.setPosition(GameInfo.WIDTH / 2f - 30, GameInfo.HEIGHT / 2f + 20, Align.center)
        endCoinScore.setPosition(GameInfo.WIDTH / 2f - 30, GameInfo.HEIGHT / 2f + 90, Align.center)

        stage.addActor(gameOverPanel)
        stage.addActor(endScore)
        stage.addActor(endCoinScore)
    }

    fun incrementScore(score: Int) {

        GameManager.score += score
        scoreLabel.setText("x${GameManager.score}")
    }

    fun incrementCoin() {

        GameManager.coinScore ++
        coinLabel.setText("x${GameManager.coinScore}")
        incrementScore(100)
    }

    fun incrementLife() {

        GameManager.lifeScore ++
        lifeLabel.setText("x${GameManager.lifeScore}")
        incrementScore(200)
    }

    fun decrementLife() {

        if (GameManager.lifeScore >= 0) {
            GameManager.lifeScore--
            lifeLabel.setText("x${GameManager.lifeScore}")
            incrementScore(-50)
        }
    }

    fun getStage() = this.stage

}