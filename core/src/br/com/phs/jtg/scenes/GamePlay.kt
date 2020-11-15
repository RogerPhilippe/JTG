package br.com.phs.jtg.scenes

import br.com.phs.jtg.GameMain
import br.com.phs.jtg.clouds.CloudsController
import br.com.phs.jtg.helpers.GameInfo
import br.com.phs.jtg.helpers.GameManager
import br.com.phs.jtg.huds.UIHud
import br.com.phs.jtg.player.Player
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import kotlin.math.abs

class GamePlay(private val game: GameMain) : Screen, ContactListener {

    private val bgs: HashMap<Int, Sprite> = hashMapOf()
    private var lastYPosition: Float = 0f
    private var mainCamera: OrthographicCamera = OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT)
    private var gameViewport: Viewport
    private var box2dCamera: OrthographicCamera
    private var debugRenderer: Box2DDebugRenderer
    private var world: World
    private var cloudsController: CloudsController
    private var player: Player
    private var hud: UIHud
    private var touchedForFistTime = false
    private var lastPLayerY: Float = 0f
    private var cameraSpeed: Float = 10f
    private var maxSpeed: Float = 10f
    private var acceleration = 10f
    private var coinSound: Sound
    private var lifeSound: Sound

    init {

        mainCamera.position.set(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2, 0f)

        gameViewport = StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera)

        box2dCamera = OrthographicCamera()
        box2dCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM)
        box2dCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0f)

        debugRenderer = Box2DDebugRenderer()

        hud = UIHud(game)

        world = World(Vector2(0f, -9.8f), true)
        world.setContactListener(this)

        cloudsController = CloudsController(world)
        val firstCloud = cloudsController.getFirstCloud()
        player = Player(world, firstCloud.x, firstCloud.y + 100)

        for (i in 0..2) {
            bgs[i] = Sprite(Texture("Backgrounds/Game BG.png"))
            bgs[i]?.setPosition(0f, -(i * (bgs[i]?.height ?: 0f)))
            lastYPosition = abs(bgs[i]?.y ?: 0f)
        }

        setCameraSpeed()

        coinSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Coin Sound.wav"))
        lifeSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Life Sound.wav"))

    }

    private fun handleInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A) )
            player.movePlayer(-2f)
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)  || Gdx.input.isKeyPressed(Input.Keys.D) )
            player.movePlayer(2f)
        else
            player.setWalking(false)
    }

    private fun handleInputAndroid() {

        if (Gdx.input.isTouched) {
            if (Gdx.input.x > GameInfo.WIDTH / 2f)
                player.movePlayer(2f)
            else
                player.movePlayer(-2f)

        } else
            player.setWalking(false)
    }

    private fun checkForFirstTouch() {

        if (!touchedForFistTime) {
            if (Gdx.input.justTouched()) {
                touchedForFistTime = true
                GameManager.isPaused = false
                lastPLayerY = player.y
            }
        }
    }

    private fun update(dt: Float) {

        this.checkForFirstTouch()

        if (!GameManager.isPaused) {
            this.handleInput()
            this.handleInputAndroid()
            this.moveCamera(dt)
            this.checkBackgroundsOutOfBounds()
            cloudsController.setCameraY(mainCamera.position.y)
            cloudsController.createAndArrangeNewClouds()
            cloudsController.removeOffScreenCollectables()
            checkPlayersBounds()
            countScore()
        }
    }

    private fun moveCamera(delta: Float) {

        mainCamera.position.y -= cameraSpeed * delta

        cameraSpeed += acceleration * delta

        if (cameraSpeed > maxSpeed)
            cameraSpeed = maxSpeed

    }

    private fun setCameraSpeed() {

        when {

            GameManager.getGameData()?.easyDifficulty == true -> {
                cameraSpeed = 80f
                maxSpeed = 100f
            }

            GameManager.getGameData()?.mediumDifficulty == true -> {
                cameraSpeed = 100f
                maxSpeed = 120f
            }

            GameManager.getGameData()?.hardDifficulty == true -> {
                cameraSpeed = 120f
                maxSpeed = 140f
            }
        }
    }

    private fun drawBackgrounds() {

        bgs.forEach {
            game.spriteBatch.draw(it.value, it.value.x, it.value.y)
        }
    }

    private fun checkBackgroundsOutOfBounds() {

        bgs.forEach {
            if ((it.value.y - it.value.height / 2f -5) > mainCamera.position.y) {
                val newPosition = it.value.height + lastYPosition
                it.value.setPosition(0f, -newPosition)
                lastYPosition = abs(newPosition)
            }
        }
    }

    private fun checkPlayersBounds() {

        if ((player.y - GameInfo.HEIGHT / 2f - player.height / 2f) > mainCamera.position.y ||
                (player.y + GameInfo.HEIGHT / 2f + player.height / 2f) < mainCamera.position.y) {
            if (!player.isDead())
                playerDied()
        }

        if (player.x - player.width > GameInfo.WIDTH || player.x + player.width < 0) {
            if (!player.isDead())
                playerDied()
        }

    }

    private fun countScore() {

        if (lastPLayerY > player.y) {
            hud.incrementScore(1)
            lastPLayerY = player.y
        }
    }

    private fun playerDied() {

        GameManager.isPaused = true
        hud.decrementLife()
        player.setDead(true)
        player.setWalking(false)
        player.set(Sprite(Texture("Collectables/Life.png")))

        if (GameManager.lifeScore < 0) {

            GameManager.checkForNewHighScores()

            hud.createGameOverPanel()
            changeScreen { game.screen = MainMenu(game) }
        } else {
            changeScreen { game.screen = GamePlay(game) }
        }

    }

    private fun changeScreen(function: () -> Unit) {

        val run = RunnableAction()
        run.setRunnable {
            function()
        }

        val sa = SequenceAction()
        sa.addAction(Actions.delay(3f))
        sa.addAction(Actions.fadeOut(.5f))
        sa.addAction(run)

        hud.getStage().addAction(sa)
    }

    override fun show() {
    }

    override fun render(delta: Float) {

        this.update(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.spriteBatch.begin()
        this.drawBackgrounds()
        cloudsController.drawClouds(game.spriteBatch)
        cloudsController.drawCollectables(game.spriteBatch)
        player.drawPlayerIdle(game.spriteBatch)
        player.drawPlayerAnimation(game.spriteBatch)
        game.spriteBatch.end()

        //debugRenderer.render(world, box2dCamera.combined)

        game.spriteBatch.projectionMatrix = hud.getStage().camera.combined
        hud.getStage().draw()
        hud.getStage().act()

        game.spriteBatch.projectionMatrix = mainCamera.combined
        mainCamera.update()

        player.updatePlayer()
        world.step(Gdx.graphics.deltaTime, 6, 2)

    }

    override fun resize(width: Int, height: Int) {
        gameViewport.update(width, height)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        world.dispose()
        bgs.forEach {
            it.value.texture.dispose()
        }
        player.texture.dispose()
        debugRenderer.dispose()
        coinSound.dispose()
        lifeSound.dispose()
    }

    override fun beginContact(contact: Contact?) {

        var bodyOne: Fixture? = null
        var bodyTwo: Fixture? = null

        if (contact?.fixtureA?.userData == "Player") {

            bodyOne = contact.fixtureA
            bodyTwo = contact.fixtureB
        } else if (contact != null) {

            bodyOne = contact.fixtureB
            bodyTwo = contact.fixtureA
        }

        if (bodyOne?.userData == "Player" && bodyTwo?.userData == "Coin") {
            bodyTwo.userData = "Remove"
            cloudsController.removeCollectables()
            hud.incrementCoin()
            coinSound.play()
        }

        if (bodyOne?.userData == "Player" && bodyTwo?.userData == "Life") {
            bodyTwo.userData = "Remove"
            cloudsController.removeCollectables()
            hud.incrementLife()
            lifeSound.play()
        }

        if (bodyOne?.userData == "Player" && bodyTwo?.userData == "Dark Cloud") {

            if (!player.isDead())
                playerDied()
        }

    }

    override fun endContact(contact: Contact?) { }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) { }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) { }

}