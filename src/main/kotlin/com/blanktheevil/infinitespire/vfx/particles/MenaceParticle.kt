package com.blanktheevil.infinitespire.vfx.particles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.clamp
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.random.Random

class MenaceParticle(
  val x: Float,
  val y: Float,
  var size: Float,
  var lifespan: Float
) : Particle() {
  companion object {
    private const val PARTICLE_MAX_SIZE = 0.45f
    private const val PARTICLE_MIN_SIZE = 0.1f
    private const val PARTICLE_JITTER_SIZE = 0.025f
    private val TEXTURE: TextureAtlas.AtlasRegion by lazy {
      Textures.vfx.get("menacing.png").asAtlasRegion()
    }
  }

  val random = Random()

  override fun update() {
    this.lifespan -= deltaTime
    size = if (random.randomBoolean()) {
      size - PARTICLE_JITTER_SIZE.scale()
    } else {
      size + PARTICLE_JITTER_SIZE.scale()
    }
    size = size.clamp(
      PARTICLE_MIN_SIZE.scale(),
      PARTICLE_MAX_SIZE.scale()
    )
  }

  override fun isDead() = lifespan <= 0.0f

  override fun render(sb: SpriteBatch) {
    val w = TEXTURE.packedWidth.toFloat()
    val h = TEXTURE.packedHeight.toFloat()

    sb.color = Color.WHITE.cpy()
    sb.draw(
      TEXTURE,
      x,
      y,
      w.div(2f),
      h.div(2f),
      w,
      h,
      size,
      size,
      0f
    )
  }
}