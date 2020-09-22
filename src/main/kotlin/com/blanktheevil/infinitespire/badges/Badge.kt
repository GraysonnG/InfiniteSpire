package com.blanktheevil.infinitespire.badges

import basemod.AutoAdd
import basemod.interfaces.ISubscriber
import basemod.interfaces.OnStartBattleSubscriber
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.interfaces.hooks.IInfiniteSpire
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.log
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.rooms.AbstractRoom

@AutoAdd.Ignore
abstract class Badge(val id: String) : IInfiniteSpire, ISubscriber, OnStartBattleSubscriber, SpireElement {
  companion object {
    private val VOID_SHARD: TextureAtlas.AtlasRegion by lazy {
      Textures.ui.get("topPanel/avhari/voidShard.png").asAtlasRegion()
    }
    private val GLOW: TextureAtlas.AtlasRegion by lazy {
      Textures.vfx.get("form-bg.png").asAtlasRegion()
    }
  }

  private var obtainedThisCombat = false
  private val strings = InfiniteSpire.badgeStringsKt[id]
  protected var badgesThisCombat = 0
  val name = strings!!.NAME
  val description = strings!!.DESCRIPTION
  private var maxLife = 0f
  private var life = 0f
  var pos = getStartPos()
  var targetPos: Vector2 = pos.cpy()

  abstract fun giveReward()
  abstract fun reset()

  protected fun completed() {
    if (!obtainedThisCombat) {
      badgeCompletedHook()
      obtainedThisCombat = true
      badgesThisCombat++
      InfiniteSpire.badgeOverlay.addBadge(this)
      giveReward()
    }
  }

  open fun badgeCompletedHook() {}

  override fun receiveOnBattleStart(r: AbstractRoom) {
    log.info("Badges Reset...")
    obtainedThisCombat = false
    badgesThisCombat = 0
    reset()
  }

  fun spawn(target: Vector2, lifeSpan: Float) {
    pos = getStartPos()
    maxLife = lifeSpan
    life = lifeSpan
    targetPos = target
  }

  fun updateTarget(target: Vector2) {
    if (life < 0.5f) {
      target.y = Settings.HEIGHT.plus(200f.scale())
    }

    targetPos = target
  }

  override fun update() {
    life -= deltaTime
    pos.x = MathHelper.scaleLerpSnap(pos.x, targetPos.x)
    pos.y = MathHelper.scaleLerpSnap(pos.y, targetPos.y)
  }

  override fun render(sb: SpriteBatch) {
    val fWidth = FontHelper.getWidth(FontHelper.topPanelInfoFont, name, 1f.div(scale))


    val glowPos = Vector2(
      pos.x.minus(fWidth).minus(GLOW.packedWidth.div(2f).scale()).minus(5f.scale()).minus(VOID_SHARD.packedWidth.div(2f).scale()),
      pos.y.minus(GLOW.packedHeight.div(2f).scale())
    )
    val shardPos = Vector2(
      pos.x.minus(fWidth).minus(VOID_SHARD.packedWidth.scale()).minus(5f.scale()),
      pos.y.minus(VOID_SHARD.packedHeight.div(2f).scale())
    )

    FontHelper.renderFontRightAligned(
      sb,
      FontHelper.topPanelInfoFont,
      name,
      pos.x,
      pos.y,
      Color.WHITE.cpy()
    )

    sb.color = InfiniteSpire.PURPLE.cpy().also { it.a = 0.6f }
    sb.additiveMode()
    sb.draw(
      GLOW,
      glowPos.x,
      glowPos.y,
      GLOW.packedWidth.div(2f),
      GLOW.packedHeight.div(2f),
      GLOW.packedWidth.toFloat(),
      GLOW.packedHeight.toFloat(),
      0.1f,
      0.1f,
      0f
    )

    sb.color = Color.WHITE.cpy()
    sb.normalMode()
    sb.draw(
      VOID_SHARD,
      shardPos.x,
      shardPos.y,
      VOID_SHARD.packedWidth.div(2f),
      VOID_SHARD.packedHeight.div(2f),
      VOID_SHARD.packedWidth.toFloat(),
      VOID_SHARD.packedHeight.toFloat(),
      scale,
      scale,
      0f
    )
  }

  fun isDead(): Boolean = life < 0

  private fun getStartPos() = Vector2((-500f).scale(), (-500f).scale())
}