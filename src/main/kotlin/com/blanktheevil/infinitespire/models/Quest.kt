package com.blanktheevil.infinitespire.models

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.enums.QuestType
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.helpers.Hitbox
import java.util.*

abstract class Quest(
  val type: QuestType,
  val hb: Hitbox = Hitbox(100f, 200f),
  val uuid: String = UUID.randomUUID().toString()
) : SpireElement, SpireClickable {

  abstract fun isDone(): Boolean

  override fun update() {

  }

  override fun render(sb: SpriteBatch) {

  }

  override fun getHitbox(): Hitbox {
    return hb
  }
}