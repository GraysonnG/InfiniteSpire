package com.blanktheevil.infinitespire.rooms.avhari.utils

import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.vfx.utils.VFXManager

object AvhariManager {

  fun getPoint(position: Vector2, distance: Float, offset: Float, index: Int, max: Int): Vector2 {
    val angle = 360f.div(max.toFloat()).times(index).plus(offset)
    return VFXManager.generatePointAtDistanceWithAngle(position.x, position.y, distance, angle)
  }
}