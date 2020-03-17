package com.blanktheevil.infinitespire.models

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.helpers.Hitbox

abstract class Quest(val type: QuestType) : SpireElement, SpireClickable {

  val hb = Hitbox(100f, 200f)

  abstract fun isDone(): Boolean

  override fun update() {

  }

  override fun render(sb: SpriteBatch) {

  }

  override fun getHitbox(): Hitbox {
    return hb
  }
}