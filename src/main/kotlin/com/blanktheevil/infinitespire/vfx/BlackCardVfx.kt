package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.doNothing
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import com.megacrit.cardcrawl.vfx.BorderFlashEffect

class BlackCardVfx : AbstractGameEffect() {
  init {
    duration = 1.0f
  }

  override fun update() {
    if (duration == 1.0f) {
      AbstractDungeon.effectsQueue.apply {
        add(BorderFlashEffect(InfiniteSpire.PURPLE.cpy(), true))
        add(BorderFlashEffect(Color.BLACK.cpy(), false))
      }
    }

    duration = duration.minus(deltaTime)
    if (duration <= 0.0f) {
      isDone = true
    }
  }

  override fun render(sb: SpriteBatch) = doNothing()
  override fun dispose() = doNothing()
}