package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.vfx.particles.MenaceParticle
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

class MenacingVfx(
  private val amountOfMenace: Int = 3
) : AbstractGameEffect() {
  companion object {
    private const val PARTICLE_SIZE = 0.3f
    private const val PARTICLE_SIZE_SUB = 0.1f
  }

  private val particles = mutableListOf<MenaceParticle>().also { list ->
    list.addAll(List(amountOfMenace) {
      val pos = VFXManager.generatePointAlongEdgeOfCircle(
        player.hb.cX + player.hb.width.div(4f),
        player.hb.cY + player.hb.height.div(4f),
        player.hb.height,
        15f.times(it)
      )
      MenaceParticle(
        pos.x,
        pos.y,
        PARTICLE_SIZE.scale() - PARTICLE_SIZE_SUB.scale().times(it),
        0.5f
      )
    })
  }

  override fun update() {
    particles.forEach {
      it.update()
    }
    particles.removeIf { it.isDead() }
    if (particles.isEmpty()) {
      isDone = true
    }
  }

  override fun render(sb: SpriteBatch) {
    particles.forEach { it.render(sb) }
  }

  override fun dispose() = doNothing()
}