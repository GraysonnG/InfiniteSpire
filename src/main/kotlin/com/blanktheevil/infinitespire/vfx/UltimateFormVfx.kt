package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.vfx.particles.BlackCardParticle
import com.blanktheevil.infinitespire.vfx.particlesystems.BlackCardParticleSystem
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import kotlin.math.max

class UltimateFormVfx(player: AbstractPlayer) : AbstractGameEffect() {

  init {
    duration = Settings.ACTION_DUR_XLONG
  }

  val particles = BlackCardParticleSystem(
    numberOfParticlesPerSpawn = 8,
    createNewParticle = {
      BlackCardParticle(
        VFXManager.generateRandomPointAlongEdgeOfTriangle(
          player.hb.cX,
          player.hb.cY,
          max(
            player.hb.width,
            player.hb.height
          ),
          30f
        ),
        1f,
        true
      )
    },
    shouldCreateParticle = { duration > 0 }
  )

  override fun update() {
    duration -= deltaTime
    particles.update()
    if (particles.isEmpty()) {
      this.isDone
    }
  }

  override fun render(sb: SpriteBatch) {
    particles.render(sb)
  }

  override fun dispose() = doNothing()
}