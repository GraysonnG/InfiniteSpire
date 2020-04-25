package com.blanktheevil.infinitespire.vfx.utils

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.enums.ShapeType
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.extensions.toRadians
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

  fun generateRandomPointAlongEdgeOfTriangle(x: Float, y: Float, size: Float, angle: Float): Vector2 =
    when(MathUtils.random(0, 2)) {
      0 -> {
        val point = generatePointAtDistanceWithAngle(
          x,
          y,
          size.div(2).times(tan(30f.toRadians())),
          30f.plus(angle)
        )
        generateRandomPointAlongLine(point.x, point.y, size, (-60f).plus(angle))
      }
      1 -> {
        val point = generatePointAtDistanceWithAngle(
          x,
          y,
          -size.div(2).times(tan(30f.toRadians())),
          (-30f).plus(angle)
        )
        generateRandomPointAlongLine(point.x, point.y, size, 60f.plus(angle))
      }
      else -> {
        val point = generatePointAtDistanceWithAngle(
          x,
          y,
          -size.div(2).times(tan(30f.toRadians())),
          90f.plus(angle)
        )
        generateRandomPointAlongLine(point.x, point.y, size, angle)
      }
    }


  fun generatePointInsideOval(cX: Float, cY: Float, width: Float, height: Float): Vector2 {
    val radiusX = width.times(Math.random())
    val radiusY = height.times(Math.random())
    val angle = Math.random().times(PI).times(2f)
    val pX = cos(angle).times(radiusX).plus(cX).toFloat()
    val pY = sin(angle).times(radiusY).plus(cY).toFloat()
    return Vector2(pX, pY)
  }

  fun generatePointAtDistanceWithAngle(x: Float, y: Float, dist: Float, angle: Float): Vector2 {
    val point = Vector2(x, y)
    val vel = Vector2(
      cos(angle.toRadians()),
      sin(angle.toRadians())
    )

    point.add(vel.scl(dist))

    return point
  }

  fun generateRandomPointAlongLine(x: Float, y: Float, length: Float, angle:Float): Vector2 {
    val pos = MathUtils.randomBoolean()

    return if(pos) {
      generatePointAtDistanceWithAngle(x, y, MathUtils.random(0f, length.div(2)), angle)
    } else {
      generatePointAtDistanceWithAngle(x, y, -MathUtils.random(0f, length.div(2)), angle)
    }
  }

  fun generatePointAlongEdgeOfCircle(x: Float, y: Float, radius: Float, degrees: Float): Vector2 {
    val pX = cos(degrees).times(radius).plus(x)
    val pY = sin(degrees).times(radius).plus(y)
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

  fun getVelocityAwayFromPoint(point: Vector2, position: Vector2): Vector2 {
    var angle = atan2(
      point.y.minus(position.y).toDouble(),
      point.x.minus(position.x).toDouble()
    )
    angle = angle.times(180.div(PI)).plus(180f)

    if (angle < 0) {
      angle += 360
    }

    val speed = hypot(point.x.minus(position.x), point.y.minus(position.y))

    return Vector2(speed, speed).setAngle(angle.toFloat())
  }

  fun getRandomShapeType(): ShapeType {
    return ShapeType.values().asList().getRandomItem(AbstractDungeon.miscRng) ?: ShapeType.SPIKER
  }
}