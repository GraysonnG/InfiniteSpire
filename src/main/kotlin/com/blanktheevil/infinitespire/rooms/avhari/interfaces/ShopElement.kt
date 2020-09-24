package com.blanktheevil.infinitespire.rooms.avhari.interfaces

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement

interface ShopElement : SpireElement, SpireClickable {
  fun purchace()
  fun renderPrice(sb: SpriteBatch)
  fun placeAtPoint(position: Vector2, distance: Float, rotation: Float, index: Int, size: Int)
  fun mouseOverSkipButton(): Boolean
}