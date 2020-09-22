package com.blanktheevil.infinitespire.ui.campfire

import actlikeit.savefields.BehindTheScenesActNum
import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.acts.TheVoid
import com.blanktheevil.infinitespire.acts.utils.ActManager
import com.blanktheevil.infinitespire.extensions.languagePack
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireClickable
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.vfx.particles.BlackCardParticle
import com.blanktheevil.infinitespire.vfx.particlesystems.BlackCardParticleSystem
import com.blanktheevil.infinitespire.vfx.utils.VFXManager
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.rooms.CampfireUI
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption
import java.util.*

class VoidOption : SpireElement, SpireClickable {
  companion object {
    private const val IMG_W = 556f
    private const val IMG_H = 256f
    private val NORM_SCALE = 0.9f.scale()
    private val HOVER_SCALE = 1f.scale()
    private val START_X = Settings.WIDTH.div(2f)
    private val BREAKPOINT_X = 1550f.scale()
    private val START_Y = 210f.scale()
    private val BREAKPOINT_Y = 720f.scale()
    private val TEXT = languagePack.getUIString("VoidOption".makeID()).TEXT[0]
    private const val TEXTURE = "campfire/voidoption.png"
    private const val TEXTURE_HG = "campfire/voidoption-hg.png"
  }

  var scale = NORM_SCALE
  val hb = Hitbox(512f.scale(), 140f.scale())
  var buttons: ArrayList<AbstractCampfireOption>? = null
  private val particleSystem = BlackCardParticleSystem(
    createNewParticle = {
      BlackCardParticle(
        VFXManager.generateRandomPointAlongEdgeOfHitbox(hb),
        scale,
        true,
        glow = false
      )
    },
    shouldCreateParticle = {
      hb.hovered
    }
  )

  override fun update() {
    if (shouldNotRender()) return

    hb.update()

    if (hb.justHovered) {
      CardCrawlGame.sound.play("UI_HOVER")
    }

    particleSystem.update()

    scale = if (hb.hovered) {
      MathHelper.scaleLerpSnap(scale, HOVER_SCALE)
    } else {
      MathHelper.scaleLerpSnap(scale, NORM_SCALE)
    }

    if (leftClicked()) {
      CardCrawlGame.nextDungeon = TheVoid.ID
      closeDungeon()
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun updatePosition(cui: CampfireUI) {
    if (buttons == null) {
      buttons = ReflectionHacks.getPrivate(cui, CampfireUI::class.java, "buttons")
          as ArrayList<AbstractCampfireOption>
    }
    if (buttons?.size!! > 4) {
      hb.move(BREAKPOINT_X, BREAKPOINT_Y)
    } else {
      hb.move(START_X, START_Y)
    }
  }

  override fun render(sb: SpriteBatch) {
    if (shouldNotRender()) return

    // Shadow
    sb.color = Color.BLACK.cpy().also {
      it.a = 1.div(5f)
    }
    renderImage(sb, TEXTURE)

    // Highlight
    val scaler = (scale - NORM_SCALE) * 10f / Settings.scale
    sb.color = InfiniteSpire.PURPLE.cpy().also {
      it.a = scaler
    }
    renderImage(sb, TEXTURE_HG)

    // Particles
    particleSystem.render(sb)

    // Image
    sb.color = Color.WHITE.cpy()
    renderImage(sb, TEXTURE)

    FontHelper.renderFontCenteredTopAligned(
      sb,
      FontHelper.topPanelInfoFont,
      TEXT,
      hb.cX,
      hb.cY - 60f * Settings.scale - 50f * Settings.scale * (scale / Settings.scale),
      Settings.GOLD_COLOR.cpy()
    )

    hb.render(sb)
  }

  private fun renderImage(sb: SpriteBatch, textureString: String) {
    sb.draw(
      Textures.ui.get(textureString),
      hb.cX.minus(IMG_W.div(2f)),
      hb.cY.minus(IMG_H.div(2f)),
      IMG_W.div(2f),
      IMG_H.div(2f),
      IMG_W,
      IMG_H,
      scale * 1.075f,
      scale * 1.075f,
      0f,
      0,
      0,
      IMG_W.toInt(),
      IMG_H.toInt(),
      false,
      false
    )
  }

  private fun shouldNotRender(): Boolean = (!ActManager.atLastCampfireInDungeon() || BehindTheScenesActNum.getActNum().rem(2) != 0 || !InfiniteSpire.saveData.settings.shouldDoEndless) && !Settings.isDebug

  override fun getHitbox(): Hitbox = hb

  private fun closeDungeon() {
    CardCrawlGame.music.fadeOutBGM()
    CardCrawlGame.music.fadeOutTempBGM()
    AbstractDungeon.fadeOut()
    AbstractDungeon.isDungeonBeaten = true
  }
}