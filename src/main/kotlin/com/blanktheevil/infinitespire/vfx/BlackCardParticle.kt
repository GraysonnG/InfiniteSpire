package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.Textures
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireElement


class BlackCardParticle(private var pos: Vector2, private var cardScale: Float, private val upgraded: Boolean) : SpireElement {
  private val texture = Textures.vfx.get("particle.png").asAtlasRegion()

  private var lifeSpan: Float = MathUtils.random(0.1f, 0.5f)

  private var color: Color = if (Math.random() < 0.25) {
    InfiniteSpire.PURPLE.cpy()
  } else if (Math.random() < 0.07 && this.upgraded) {
    InfiniteSpire.RED.cpy()
  } else {
    Color.BLACK
  }

  private var vel: Vector2 = Vector2(
    MathUtils.random(-75f, 75f).scale().times(cardScale),
    MathUtils.random(-50f, 225f).scale().times(cardScale)
  )

  init {
    pos.add(
      -texture.regionWidth.div(2f),
      -texture.regionHeight.div(2f)
    )
  }

  override fun update() {
    this.lifeSpan -= Gdx.graphics.rawDeltaTime
    val weightedVelocity = this.vel.cpy().scl(Gdx.graphics.rawDeltaTime)
    this.pos.add(weightedVelocity)
  }

  override fun render(sb: SpriteBatch) {
    sb.color = this.color
    sb.draw(
      texture,
      pos.x,
      pos.y,
      texture.regionWidth.div(2f),
      texture.regionHeight.div(2f),
      texture.regionWidth.toFloat(),
      texture.regionHeight.toFloat(),
      lifeSpan.div(0.5f).times(cardScale).times(2f),
      lifeSpan.div(0.5f).times(cardScale).times(2f),
      0f
    )

  }

  fun isDead(): Boolean {
    return lifeSpan <= 0f
  }
}