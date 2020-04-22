package com.blanktheevil.infinitespire.vfx.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.enums.ShapeType
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.vfx.BlackCardParticle
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.random.Random
import kotlin.math.*

object VFXManager {
  private val generator = Random()

  fun generateRandomPointAlongEdgeOfHitbox(hb: Hitbox): Vector2 {
    return Vector2().also {
      with(generator) {
        val topOrBottom = randomBoolean()
        val leftOrRight = randomBoolean()
        val verticalOrHorizontal = randomBoolean()

        if (verticalOrHorizontal) {
          it.x = random(
            hb.cX.minus(hb.width.div(2f)),
            hb.cX.plus(hb.width.div(2f)))
          it.y = if (topOrBottom)
            hb.cY.plus(hb.height.div(2f)) else
            hb.cY.minus(hb.height.div(2f))
        } else {
          it.x = if (leftOrRight)
            hb.cX.plus(hb.width.div(2f)) else
            hb.cX.minus(hb.width.div(2f))
          it.y = random(
            hb.cY.minus(hb.height.div(2f)),
            hb.cY.plus(hb.height.div(2f))
          )
        }
      }
    }
  }

  fun generateRandomPointAlongEdgeOfCircle(x: Float, y: Float, radius: Float): Vector2 {
    val angle = Math.random().times(PI).times(2f)
    val pX = cos(angle).times(radius).plus(x).toFloat()
    val pY = sin(angle).times(radius).plus(y).toFloat()
    return Vector2(pX, pY)
  }

  fun getVelocityToPoint(target: Vector2, position: Vector2): Vector2 {
    var angle = atan2(
      target.y.minus(position.y).toDouble(),
      target.x.minus(position.x).toDouble()
    )
    angle = angle.times(180).div(PI)

    if (angle < 0) {
      angle += 360
    }

    val speed = hypot(target.x.minus(position.x), target.y.minus(position.y))

    return Vector2(speed, speed).setAngle(angle.toFloat())
  }

  fun getVelocityAwayFromPoint(target: Vector2, position: Vector2): Vector2 {
    var angle = atan2(
      target.y.minus(position.y).toDouble(),
      target.x.minus(position.x).toDouble()
    )
    angle = angle.times(180.div(PI)).plus(180f)

    if (angle < 0) {
      angle += 360
    }

    val speed = hypot(target.x.minus(position.x), target.y.minus(position.y))

    return Vector2(speed, speed).setAngle(angle.toFloat())
  }

  fun getRandomShapeType(): ShapeType {
    return ShapeType.values().asList().getRandomItem(AbstractDungeon.miscRng) ?: ShapeType.SPIKER
  }
}