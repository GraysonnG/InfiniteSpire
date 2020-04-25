package com.blanktheevil.infinitespire.stances

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.particles.BlackCardParticle
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.StrengthPower
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import kotlin.math.max

class UltimateStance : AbstractStance() {
  companion object {
    val STANCE_ID = "UltimateStance".makeID()
    private val strings = languagePack.getStanceString(STANCE_ID)
    private val PARTICLE_TEXTURE = Textures.vfx.get("form-bg.png").asAtlasRegion()
  }

  private var rotation = 0f
  private var snap = 0f
  private var snapProgress = 0f

  init {
    this.ID = STANCE_ID
    this.name = strings.NAME
    updateDescription()
  }

  override fun updateDescription() {
    this.description = strings.DESCRIPTION[0]
  }

  override fun updateAnimation() {
    rotation += 15f.times(deltaTime)

    if (!Settings.DISABLE_EFFECTS) {
      particleTimer -= deltaTime
      if (particleTimer < 0f) {
        particleTimer = 0.01f
        AbstractDungeon.effectsQueue.add(createParticle())
      }
    }
  }

  override fun render(sb: SpriteBatch) {
    val size = max(player.hb.width, player.hb.height).times(2f)
    val r = size.div(2f)
    val x = player.hb.cX - r
    val y = player.hb.cY - r

    snapProgress += deltaTime
    snapProgress = snapProgress.clamp(0f, 1f)
    snap = Interpolation.pow2In.apply(0f, 1f, snapProgress)

    sb.color = InfiniteSpire.PURPLE.cpy().also {
      it.a = 0.6f
    }
    sb.additiveMode()
    sb.draw(
      PARTICLE_TEXTURE,
      x,
      y,
      r,
      r,
      size,
      size,
      snap,
      snap,
      rotation
    )
    sb.draw(
      PARTICLE_TEXTURE,
      x,
      y,
      r,
      r,
      size,
      size,
      snap,
      snap,
      rotation,
      true
    )
    sb.normalMode()
    super.render(sb)
  }

  private fun createParticle(): AbstractGameEffect {
    val radius = max(player.hb.width, player.hb.height)
    val point = VFXManager.generatePointInsideOval(
      player.hb.cX,
      player.hb.cY,
      radius.div(2f),
      radius.div(2f)
    )
    return BlackCardParticle(
      point,
      1.5f,
      false,
      color = getParticleColor()
    ).toStsEffect()
  }

  private fun getParticleColor(): Color {
    val roll = Math.random()
    return when {
      roll < .75f -> Color.BLACK.cpy()
      roll < .9f -> InfiniteSpire.PURPLE.cpy()
      else -> InfiniteSpire.RED.cpy()
    }
  }

  override fun onEndOfTurn() {
    player.applyPower(
      StrengthPower(player, 3)
    )
    player.applyPower(
      DexterityPower(player, 3)
    )
  }
}