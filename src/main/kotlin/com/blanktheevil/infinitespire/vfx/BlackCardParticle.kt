package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireElement


class BlackCardParticle(
  private var pos: Vector2,
  private var cardScale: Float,
  private val upgraded: Boolean,
  private var vel: Vector2 = Vector2(
    MathUtils.random(-75f, 75f).scale().times(cardScale),
    MathUtils.random(-50f, 225f).scale().times(cardScale)
  )
) : SpireElement {
  companion object {
    val IMG = "particle_new.png"
    private val TEXTURE = Textures.vfx.get(IMG).asAtlasRegion()
  }

  private var lifeSpan: Float = MathUtils.random(0.1f, 0.5f)
  private var rotationSpeed: Float = MathUtils.random(-90f, 90f)
  private var rotation: Float = MathUtils.random(0f, 66f)

  private var color: Color = when {
    Math.random() < 0.25 -> InfiniteSpire.PURPLE.cpy()
    Math.random() < 0.07 && this.upgraded -> InfiniteSpire.RED.cpy()
    else -> Color.BLACK.cpy()
  }

  init {
    pos.add(
      -TEXTURE.regionWidth.div(2f),
      -TEXTURE.regionHeight.div(2f)
    )
  }

  override fun update() {
    this.lifeSpan -= deltaTime
    this.rotation += this.rotationSpeed.times(deltaTime)
    val weightedVelocity = this.vel.cpy().scl(deltaTime)
    this.pos.add(weightedVelocity)
  }

  override fun render(sb: SpriteBatch) {
    sb.color = this.color
    sb.draw(
      TEXTURE,
      pos.x,
      pos.y,
      TEXTURE.regionWidth.div(2f),
      TEXTURE.regionHeight.div(2f),
      TEXTURE.regionWidth.toFloat(),
      TEXTURE.regionHeight.toFloat(),
      lifeSpan.div(0.5f).times(cardScale).times(2f),
      lifeSpan.div(0.5f).times(cardScale).times(2f),
      rotation
    )

  }

  fun isDead(): Boolean {
    return lifeSpan <= 0f
  }
}