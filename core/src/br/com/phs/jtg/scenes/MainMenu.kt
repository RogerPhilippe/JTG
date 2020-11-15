package br.com.phs.jtg.scenes

import br.com.phs.jtg.GameMain
import br.com.phs.jtg.helpers.GameInfo
import br.com.phs.jtg.huds.MainMenuButtons
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

class MainMenu(private val game: GameMain) : Screen {

    private var mainCamera = OrthographicCamera()
    private var gameViewport: Viewport
    private var bg = Texture("Backgrounds/Menu BG.png")
    private var btns: MainMenuButtons

    init {

        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT)
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0f)

        gameViewport = StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera)

        btns = MainMenuButtons(game)

    }

    override fun show() {
    }

    override fun render(delta: Float) {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.spriteBatch.begin()
        game.spriteBatch.draw(bg, 0f, 0f)
        game.spriteBatch.end()

        game.spriteBatch.projectionMatrix = btns.getStage().camera.combined
        btns.getStage().draw()
        btns.getStage().act()
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
        bg.dispose()
        btns.getStage().dispose()
    }
}