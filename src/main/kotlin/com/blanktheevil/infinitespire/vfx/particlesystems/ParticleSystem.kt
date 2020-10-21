package com.blanktheevil.infinitespire.vfx.particlesystems

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.vfx.particles.Particle

class ParticleSystem(
  private val numberOfParticlesPerSpawn: Int = 4,
  private val createNewParticle: () -> Particle,
  private val shouldCreateParticle: () -> Boolean = { true },
  private val particleTimerReset: () -> Float = { MathUtils.random(0.01f, 0.02f) }
) : SpireElement {

  private val particles = ArrayList<Particle?>()
  private var particleTimer = 0f

  fun addParticles(amount: Int, createNewParticle: () -> Particle = this.createNewParticle) {
    for (i in 0 until amount) {
      particles.add(
        createNewParticle.invoke()
      )
    }
  }

  fun isEmpty(): Boolean = particles.isEmpty()

  override fun update() {
    particleTimer -= deltaTime

    if (particleTimer <= 0f && shouldCreateParticle.invoke() && InfiniteSpire.saveData.settings.shouldDoParticles) {
      for (i in 0 until numberOfParticlesPerSpawn) {
        particles.add(
          createNewParticle.invoke()
        )
      }
      particleTimer = particleTimerReset.invoke()
    }

    particles.forEach { it?.update() }
    particles.removeIf { it!!.isDead() }
  }

  override fun render(sb: SpriteBatch) {
    particles.forEach { it?.render(sb) }
  }
}