package com.blanktheevil.infinitespire.rooms.avhari.shop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.asAtlasRegion
import com.blanktheevil.infinitespire.extensions.halfHeight
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElementBase
import com.blanktheevil.infinitespire.rooms.avhari.utils.AvhariManager
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.subVoidShard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.MathHelper
import com.megacrit.cardcrawl.relics.AbstractRelic

class ShopRelic(private val relic: AbstractRelic, cost: Int) : ShopElementBase(cost) {
  companion object {
    private val SHARD_TEXTURE by lazy { Textures.ui.get("topPanel/avhari/voidShard.png").asAtlasRegion() }
  }

  val hb = Hitbox(relic.hb.x, relic.hb.y, relic.hb.width, relic.hb.height)

  override fun update() {
    relic.update()
    moveRelic()
    hb.move(relic.currentX, relic.currentY)
    hb.update()
    checkOnClick()
    renderOnTop = relic.hb.hovered
  }

  override fun render(sb: SpriteBatch) {
    relic.render(sb)
    hb.render(sb)
    renderPrice(sb)
  }

  override fun purchace() {
    CardCrawlGame.sound.play("SHOP_PURCHASE")
    subVoidShard(cost)
    purchaced = true
    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(
      relic.currentX,
      relic.currentY,
      relic.makeCopy()
    )
  }

  override fun renderPrice(sb: SpriteBatch) {
    with(SHARD_TEXTURE) {
      val xPos = relic.currentX.minus(packedWidth.div(2f))
      val yPos = relic.currentY.minus(packedHeight).minus(10f.scale())

      var fontColor = Color.WHITE.cpy().also { it.a = 1f }
      if (!canAfford()) {
        fontColor = Color.RED.cpy().also { it.a = 1f }
      }

      sb.color = fontColor
      sb.draw(
        this,
        xPos,
        yPos,
        packedWidth.div(2f),
        packedHeight.div(2f),
        packedWidth.toFloat(),
        packedHeight.toFloat(),
        Settings.scale,
        Settings.scale,
        1f
      )

      FontHelper.cardTitleFont.data.setScale(1.0f)
      FontHelper.renderFontCentered(
        sb,
        FontHelper.cardTitleFont,
        "$cost",
        relic.currentX,
        yPos + halfHeight.div(2f).scale(),
        fontColor
      )
    }
  }

  private fun moveRelic() {
    relic.currentX = MathHelper.uiLerpSnap(relic.currentX, relic.targetX)
    relic.currentY = MathHelper.uiLerpSnap(relic.currentY, relic.targetY)
  }

  override fun getHitbox(): Hitbox = hb

  override fun placeAtPoint(position: Vector2, distance: Float, rotation: Float, index: Int, size: Int) {
    val point = AvhariManager.getPoint(position, distance, rotation, index, size)
    relic.targetX = point.x
    relic.targetY = point.y
  }
}