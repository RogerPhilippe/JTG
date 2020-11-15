package br.com.phs.jtg.clouds

import br.com.phs.jtg.collectables.Collectable
import br.com.phs.jtg.helpers.GameInfo
import br.com.phs.jtg.helpers.GameManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import java.util.*

class CloudsController(private val world: World) {

    private val clouds: MutableList<Cloud> = arrayListOf()
    private val collectables: MutableList<Collectable> = arrayListOf()
    private var minX: Float = GameInfo.WIDTH / 2f - 120
    private var maxX: Float = GameInfo.WIDTH / 2f + 120
    private var lastCloudPositionY = 0F

    private var cameraY: Float = 0f

    private val random = Random()

    companion object {
        const val DISTANCE_BETWEEN_CLOUDS = 250f
    }

    init {

        createClouds()
        positionClouds(true)
    }

    private fun createClouds() {

        for (i in 0..1) {
            clouds.add(Cloud(world, "Dark Cloud"))
        }

        var index = 1

        for (i in 0..5) {
            clouds.add(Cloud(world, "Cloud $index"))
            index ++
            if (index == 4)
                index = 1
        }

        clouds.shuffle()

    }

    private fun positionClouds(firstTimeArranging: Boolean) {

        while (clouds[0].getCloudName() == "Dark Cloud") {
            clouds.shuffle()
        }

        var positionY = if (firstTimeArranging)
            GameInfo.HEIGHT / 2f
        else
            lastCloudPositionY

        var controlX = 0

        clouds.forEach {

            if (it.x == 0f && it.y == 0f) {

                var tempX = 0f

                if (controlX == 0) {
                    tempX = randomBetweenNumbers(maxX - 40, maxX)
                    controlX = 1
                } else if (controlX == 1) {
                    tempX = randomBetweenNumbers(minX + 40, minX)
                    controlX = 0
                }

                it.setSpritePosition(tempX, positionY)
                positionY -= DISTANCE_BETWEEN_CLOUDS
                lastCloudPositionY = positionY

                if (!firstTimeArranging && it.getCloudName() != "Dark Cloud") {

                    val rand = random.nextInt(10)
                    if (rand > 4) {

                        val randomCollectable = random.nextInt(2)
                        if (randomCollectable == 0) {

                            if (GameManager.lifeScore < 2) {

                                collectables.add(Collectable(world, "Life"))
                                collectables.last().setCollectablePosition(it.x, it.y + 40)
                            } else {
                                collectables.add(Collectable(world, "Coin"))
                                collectables.last().setCollectablePosition(it.x, it.y + 40)
                            }
                        } else {
                            collectables.add(Collectable(world, "Coin"))
                            collectables.last().setCollectablePosition(it.x, it.y + 40)
                        }
                    }
                }

            }

        }

    }

    fun drawClouds(batch: SpriteBatch) {

        clouds.forEach {
            batch.draw(it, it.x - it.width / 2f, it.y - it.height / 2f)
        }
    }

    fun drawCollectables(batch: SpriteBatch) {

        collectables.forEach {

            //it.updateCollectable()
            batch.draw(it, it.x, it.y)
        }
    }

    fun removeCollectables() {

        var collectableToRemove: Collectable? = null
        collectables.forEach {

            if (it.getFixture().userData == "Remove") {
                it.changeFilter()
                it.texture.dispose()
                collectableToRemove = it
            }
        }

        if (collectableToRemove != null)
            collectables.remove(collectableToRemove!!)
    }

    fun createAndArrangeNewClouds() {

        var cloudToRemove: Cloud? = null
        clouds.forEach cloudsEach@ {
            if ((it.y - GameInfo.HEIGHT / 2 - 24) > cameraY) {
                cloudToRemove = it
                return@cloudsEach
            }
        }

        if (cloudToRemove != null) {
            cloudToRemove!!.texture.dispose()
            clouds.remove(cloudToRemove!!)
        }

        if (clouds.size == 4) {
            createClouds()
            positionClouds(false)
        }

    }

    fun removeOffScreenCollectables() {

        var collectableForRemove: Collectable? = null
        collectables.forEach {

            if ((it.y - GameInfo.HEIGHT / 2f - 15) > cameraY) {
                it.texture.dispose()
                collectableForRemove = it
            }
        }

        if (collectableForRemove != null)
            collectables.remove(collectableForRemove!!)
    }

    fun setCameraY(cameraY: Float) {
        this.cameraY = cameraY
    }

    fun getFirstCloud() = clouds[0]

    private fun randomBetweenNumbers(min: Float, max: Float) = random.nextFloat() * (max - min) + min


}