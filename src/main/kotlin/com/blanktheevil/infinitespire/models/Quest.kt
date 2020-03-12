package com.blanktheevil.infinitespire.models

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.QuestType
import com.blanktheevil.infinitespire.interfaces.SpireElement

abstract class Quest(val type: QuestType) : SpireElement {
  abstract fun isDone(): Boolean

  override fun update() {

  }

  override fun render(sb: SpriteBatch) {

  }
}