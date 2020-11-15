package br.com.phs.jtg.collectables

import br.com.phs.jtg.helpers.GameInfo
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.*

class Collectable(
        private val world: World,
        private val name: String
) : Sprite(Texture("Collectables/$name.png")) {

    private lateinit var fixture: Fixture
    private lateinit var body: Body

    fun setCollectablePosition(x: Float, y: Float) {
        setPosition(x, y)
        createCollectableBody()
    }

    private fun createCollectableBody() {

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.StaticBody

        bodyDef.position.set((x + width / 2) / GameInfo.PPM, (y + width / 2) / GameInfo.PPM)

        body = world.createBody(bodyDef)

        val shape = PolygonShape()
        shape.setAsBox((width / 2) / GameInfo.PPM, (height / 2) / GameInfo.PPM)

        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLE
        fixtureDef.isSensor = true

        fixture = body.createFixture(fixtureDef)
        fixture.userData = name

        shape.dispose()

    }

    fun updateCollectable() {
        setPosition(body.position.x * GameInfo.PPM, body.position.y * GameInfo.PPM)
    }

    fun changeFilter() {
        val filter = Filter()
        filter.categoryBits = GameInfo.DESTROYED
        fixture.filterData = filter
    }

    fun getFixture() = this.fixture

}