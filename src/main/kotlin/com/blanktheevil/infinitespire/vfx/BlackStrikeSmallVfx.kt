package com.blanktheevil.infinitespire.vfx

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.MathUtils
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.additiveMode
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.extensions.normalMode
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import kotlin.math.max
import kotlin.math.min

class BlackStrikeSmallVfx(val hb: Hitbox) : AbstractGameEffect() {

  companion object {
    private val IMG: TextureAtlas.AtlasRegion by lazy {
      Textures.vfx.get("strike/fist-s.png").asAtlasRegion()
    }

    private const val DURATION = 1f
  }

  private val whMin = max(hb.width, hb.height)
  private val pos = VFXManager.generateRandomPointAlongEdgeOfCircle(
    hb.cX,
    hb.cY,
    MathUtils.random(-(whMin / 2f), whMin / 2f)
  )
  private var angle = MathUtils.random(0f, 360f)

  init {
    this.duration = DURATION
    this.startingDuration = DURATION
    this.color = (if(MathUtils.randomBoolean())
      InfiniteSpire.RED.cpy()
    else
      InfiniteSpire.PURPLE.cpy()
    )
    this.scale = MathUtils.random(Settings.scale.div(2f), Settings.scale)
    CardCrawlGame.sound.play("BLUNT_FAST")
  }

  override fun render(sb: SpriteBatch) {
    sb.additiveMode()
    sb.color = color
    sb.draw(
      IMG,
      pos.x.minus(IMG.packedWidth.div(2f)),
      pos.y.minus(IMG.packedHeight.div(2f)),
      IMG.packedWidth.div(2f),
      IMG.packedHeight.div(2f),
      IMG.packedWidth.toFloat(),
      IMG.packedHeight.toFloat(),
      scale,
      scale,
      angle
    )
    sb.normalMode()
  }

  override fun dispose() = doNothing()
}