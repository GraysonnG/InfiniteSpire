package com.blanktheevil.infinitespire.vfx.particles

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.*
import com.megacrit.cardcrawl.helpers.ImageMaster

class SparkParticle(
  private var pos: Vector2,
  private var particleScale: Float,
  private var vel: Vector2 = Vector2(
    MathUtils.random(-100f, 100f),
    MathUtils.random(100f, 100f)
  ).nor(),
  private var lifeSpan: Float = MathUtils.random(0.1f, 0.5f),
  private var color: Color = COLOR.cpy()
) : Particle() {
  companion object {
    private val TEXTURE: TextureAtlas.AtlasRegion by lazy {
      ImageMaster.GLOW_SPARK_2
    }
    private val COLOR: Color by lazy {
      Color(0.6f, 0.7f, 1f, .33f)
    }
  }

  private val drag = 0.01f

  override fun isDead(): Boolean = lifeSpan <= 0f

  override fun update() {
    this.lifeSpan -= deltaTime
    val velW = this.vel.cpy().scl(deltaTime).scl(scale).scl(1f - drag)
    this.pos.add(velW)
  }

  override fun render(sb: SpriteBatch) {
    sb.additiveMode()
    sb.color = this.color
    sb.draw(
      TEXTURE,
      pos.x.minus(TEXTURE.halfWidth),
      pos.y.minus(TEXTURE.halfHeight),
      TEXTURE.halfWidth,
      TEXTURE.halfHeight,
      TEXTURE.width,
      TEXTURE.height,
      lifeSpan.div(0.5f).times(particleScale).times(2f).scale(),
      lifeSpan.div(0.5f).times(particleScale).times(2f).scale(),
      0f
    )
    sb.normalMode()
  }
}