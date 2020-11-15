package br.com.phs.jtg.player

import br.com.phs.jtg.helpers.GameInfo
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.*
import kotlin.experimental.or

class Player(
        private val world: World,
        pX: Float,
        pY: Float
) : Sprite(Texture("Player/Player 1.png")) {

    private lateinit var body: Body

    private var playerAtlas: TextureAtlas
    private lateinit var animation: Animation<TextureAtlas.AtlasRegion>
    private var elapsedTime: Float = 0f

    private var isWalking = false
    private var isDead = false

    init {

        setPosition(pX, pY)
        createBody()
        playerAtlas = TextureAtlas("Player Animation/Player Animation.atlas")
        isDead = false

    }

    private fun createBody() {

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.position.set(x / GameInfo.PPM, y / GameInfo.PPM)

        body = world.createBody(bodyDef)
        body.isFixedRotation = true

        val shape = PolygonShape()
        shape.setAsBox((width / 2 - 14) / GameInfo.PPM, (height / 2 - 10) / GameInfo.PPM)

        val fixtureDef = FixtureDef()
        fixtureDef.density = 4f // The mass of the body
        fixtureDef.friction = 40f // Will make player not slide on surface
        fixtureDef.shape = shape
        fixtureDef.filter.categoryBits = GameInfo.PLAYER
        fixtureDef.filter.maskBits = GameInfo.DEFAULT or GameInfo.COLLECTABLE

        val fixture = body.createFixture(fixtureDef)
        fixture.userData = "Player"

        this.flip(true, false)

        shape.dispose()

    }

    fun movePlayer(x: Float) {

        if (x < 0 && !this.isFlipX)
            this.flip(true, false)
        else if (x > 0 && this.isFlipX)
            this.flip(true, false)

        isWalking = true
        body.setLinearVelocity(x, body.linearVelocity.y)
    }

    fun drawPlayerIdle(batch: SpriteBatch) {

        if (!this.isWalking)
            batch.draw(this, x - width / 2f, y - height / 2f)
    }

    fun drawPlayerAnimation(batch: SpriteBatch) {

        if (this.isWalking) {
            elapsedTime += Gdx.graphics.deltaTime

            val frames = playerAtlas.regions
            frames.forEach {
                if (body.linearVelocity.x < 0 && !it.isFlipX) {
                    it.flip(true, false)
                } else if (body.linearVelocity.x > 0 && it.isFlipX) {
                    it.flip(true, false)
                }
            }

            animation = Animation(1f/10f, playerAtlas.regions)

            batch.draw(animation.getKeyFrame(elapsedTime, true), x - width / 2f, y - height / 2f)
        }
    }

    fun updatePlayer() {

        setPosition(body.position.x * GameInfo.PPM, body.position.y * GameInfo.PPM)
    }

    fun setWalking(isWalking: Boolean) { this.isWalking = isWalking }

    fun setDead(dead: Boolean) { this.isDead = dead }

    fun isDead() = this.isDead

}