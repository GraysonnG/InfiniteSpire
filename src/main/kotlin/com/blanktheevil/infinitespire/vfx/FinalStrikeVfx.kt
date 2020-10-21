package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.vfx.particles.BlackCardParticle
import com.blanktheevil.infinitespire.vfx.particlesystems.ParticleSystem
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import kotlin.math.max

class FinalStrikeVfx(private val creature: AbstractCreature) : AbstractGameEffect() {
  companion object {
    val MAX_DURATION = Settings.ACTION_DUR_LONG
  }

  init {
    duration = MAX_DURATION
  }

  private var angle = 0f
  private val startPos = Vector2(player.hb.cX, player.hb.cY)
  private val pos = startPos.cpy()
  private val endPos = Vector2(creature.hb.cX, creature.hb.cY)
  private var tScale = 2f
  private var exploded = false

  private val particles = ParticleSystem(
    numberOfParticlesPerSpawn = 6,
    createNewParticle = {
      BlackCardParticle(
        VFXManager.generateRandomPointAlongEdgeOfTriangle(
          pos.x,
          pos.y,
          max(player.hb.width, player.hb.height).times(tScale),
          angle
        ),
        1f,
        true,
        vel = Vector2(0f, 0f)
      )
    },
    shouldCreateParticle = { duration > 0f }
  )

  init {
    CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1")
  }

  override fun update() {
    duration -= deltaTime
    if (duration <= 0f) {
      if (!exploded) {
        CardCrawlGame.sound.playA("ATTACK_IRON_1", -0.4f)
        exploded = true
        particles.addParticles(100) {
          val point = VFXManager.generateRandomPointAlongEdgeOfTriangle(
            pos.x,
            pos.y,
            max(creature.hb.width, creature.hb.height),
            angle
          )
          BlackCardParticle(
            point,
            1f,
            true,
            vel = VFXManager.getVelocityAwayFromPoint(
              Vector2(pos.x, pos.y),
              point
            )
          )
        }
      }
    } else {
      val progress = MAX_DURATION.minus(duration) / MAX_DURATION

      tScale = Interpolation.exp10Out.apply(2f, 0.3f, progress)
      angle = Interpolation.exp10Out.apply(0f, 359f, progress)

      pos.set(
        Interpolation.exp10In.apply(startPos.x, endPos.x, progress),
        Interpolation.exp10In.apply(startPos.y, endPos.y, progress)
      )
    }

    particles.update()

    if (particles.isEmpty()) {
      isDone = true
    }
  }

  override fun render(sb: SpriteBatch) {
    particles.render(sb)
  }

  override fun dispose() = doNothing()
}