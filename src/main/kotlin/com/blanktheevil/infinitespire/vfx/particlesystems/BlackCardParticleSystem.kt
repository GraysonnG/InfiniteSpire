package com.blanktheevil.infinitespire.vfx.particlesystems

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.vfx.BlackCardParticle

class BlackCardParticleSystem(
  private val numberOfParticlesPerSpawn: Int = 4,
  private val createNewParticle: () -> BlackCardParticle,
  private val shouldCreateParticle: () -> Boolean = { true },
  private val particleTimerReset: () -> Float = { MathUtils.random(0.01f, 0.02f) }
): SpireElement {
  private val particles = mutableListOf<BlackCardParticle>()
  private var particleTimer = 0f

  fun addParticles(amount: Int, createNewParticle: () -> BlackCardParticle = this.createNewParticle) {
    for (i in 0 until amount) {
      particles.add(
        createNewParticle.invoke()
      )
    }
  }

  override fun update() {
    particleTimer -= deltaTime

    if (particleTimer <= 0f && shouldCreateParticle.invoke()) {
      for (i in 0 until numberOfParticlesPerSpawn) {
        particles.add(
          createNewParticle.invoke()
        )
      }
      particleTimer = particleTimerReset.invoke()
    }

    particles.asSequence()
      .forEach { it.update() }
    particles.removeIf { it.isDead() }
  }

  override fun render(sb: SpriteBatch) {
    particles.asSequence()
      .forEach { it.render(sb) }
  }
}