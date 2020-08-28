package com.blanktheevil.infinitespire.rooms.avhari

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.helpers.Hitbox

class Avhari : SpireElement, SpireClickable {
  private val hb = Hitbox(0f,0f)

  override fun getHitbox(): Hitbox = hb

  override fun update() {
  }

  override fun render(sb: SpriteBatch) {
  }
}