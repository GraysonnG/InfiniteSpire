package com.blanktheevil.infinitespire.interfaces

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface SpireElement {
  fun update()
  fun render(sb: SpriteBatch)
}