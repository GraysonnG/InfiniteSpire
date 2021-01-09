package com.blanktheevil.infinitespire.rooms.avhari.shop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElementBase
import com.blanktheevil.infinitespire.rooms.avhari.utils.AvhariManager
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.subVoidShard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect

class ShopCard(private val card: BlackCard, cost: Int) : ShopElementBase(cost) {
  companion object {
    private const val SCALE = 0.65f
    private const val HOVER_SCALE = 0.85f
    private val SHARD_TEXTURE by lazy { Textures.ui.get("topPanel/avhari/voidShard.png").asAtlasRegion() }
    private val LOCK_TEXTURE by lazy { Textures.cards.get("ui/512/card-locked.png").asAtlasRegion() }
  }

  private val lockAlpha = Color(1f, 1f, 1f, 1f)
  private val cantAffordColor = Color.RED.cpy()

  override fun update() {
    card.update()
    card.hb.update()
    checkOnClick()
    renderOnTop = card.hb.hovered

    if (card.hb.hovered && !mouseOverSkipButton()) {
      lockAlpha.a = MathHelper.scaleLerpSnap(lockAlpha.a, 0.1f)
      card.targetDrawScale = HOVER_SCALE
    } else {
      lockAlpha.a = MathHelper.scaleLerpSnap(lockAlpha.a, 1f)
      card.targetDrawScale = SCALE
    }
  }

  override fun purchace() {
    CardCrawlGame.sound.play("SHOP_PURCHASE")
    subVoidShard(cost)
    purchaced = true
    AbstractDungeon.topLevelEffects.add(FastCardObtainEffect(card, card.current_x, card.current_y))
  }

  override fun render(sb: SpriteBatch) {
    card.render(sb)
    renderLock(sb)
    renderPrice(sb)
  }

  private fun renderLock(sb: SpriteBatch) {
    with(LOCK_TEXTURE) {
      val lockCx = halfWidth
      val lockCy = halfHeight

      sb.color = lockAlpha
      sb.draw(
        this,
        card.current_x - lockCx,
        card.current_y - lockCy,
        lockCx,
        lockCy,
        width,
        height,
        card.drawScale.scale(),
        card.drawScale.scale(),
        0f
      )
    }
  }

  override fun renderPrice(sb: SpriteBatch) {
    with(SHARD_TEXTURE) {
      val shardCx = halfWidth
      val shardCy = halfHeight
      val c = if (canAfford()) lockAlpha else cantAffordColor.cpy().also { it.a = lockAlpha.a }

      sb.color = c
      sb.draw(
        this,
        card.current_x - shardCx,
        card.current_y - shardCy,
        shardCx,
        shardCy,
        width,
        height,
        scale,
        scale,
        0f
      )

      FontHelper.cardTitleFont.data.setScale(1.0f)
      FontHelper.renderFontCentered(
        sb,
        FontHelper.cardTitleFont,
        "$cost",
        card.current_x,
        card.current_y - shardCy.div(2f).scale(),
        c
      )
    }
  }

  override fun getHitbox(): Hitbox = card.hb

  override fun placeAtPoint(position: Vector2, distance: Float, rotation: Float, index: Int, size: Int) {
    val point = AvhariManager.getPoint(position, distance, rotation, index, size)
    card.target_x = point.x
    card.target_y = point.y
  }
}