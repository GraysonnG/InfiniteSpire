package com.blanktheevil.infinitespire.vfx.particles

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

abstract class Particle : SpireElement {
  abstract fun isDead(): Boolean

  fun toStsEffect(): AbstractGameEffect = object : AbstractGameEffect() {
    var particle: Particle? = this@Particle

    init {
      renderBehind = MathUtils.randomBoolean(.2f)
    }

    override fun update() {
      if (particle != null) {
        particle!!.update()
        if (particle!!.isDead()) {
          this.isDone = true
        }
      } else {
        this.isDone = true
      }
    }

    override fun render(sb: SpriteBatch) {
      particle?.render(sb)
    }

    override fun dispose() {
      particle = null
    }
  }
}