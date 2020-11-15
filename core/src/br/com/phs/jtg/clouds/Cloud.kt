package br.com.phs.jtg.clouds

import br.com.phs.jtg.helpers.GameInfo
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*

class Cloud(
        private val world: World,
        private val cloudName: String
) : Sprite(Texture("Clouds/$cloudName.png")) {

    private lateinit var body: Body

    fun setSpritePosition(x: Float, y: Float) {

        setPosition(x, y)
        createBody()
    }

    private fun createBody() {

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody

        bodyDef.position.set( x / GameInfo.PPM, y / GameInfo.PPM )

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox((width / 2 - 10) / GameInfo.PPM, (height / 2 - 10) / GameInfo.PPM)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape

        val fixture = body.createFixture(fixtureDef)
        fixture.userData = cloudName

        shape.dispose()
    }

    fun getCloudName() = this.cloudName

}