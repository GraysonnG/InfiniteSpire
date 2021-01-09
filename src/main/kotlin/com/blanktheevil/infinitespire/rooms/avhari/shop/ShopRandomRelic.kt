package com.blanktheevil.infinitespire.rooms.avhari.shop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElementBase
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.utils.subVoidShard
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.helpers.RelicLibrary
import com.megacrit.cardcrawl.random.Random
import com.megacrit.cardcrawl.relics.AbstractRelic
import com.megacrit.cardcrawl.relics.Circlet
import com.megacrit.cardcrawl.vfx.AbstractGameEffect

class ShopRandomRelic : ShopElementBase(COST) {
  companion object {
    private const val TIMER_MAX = 0.1f
    private val relicRng = Random()
    private const val COST = 1
    private val SHARD_TEXTURE by lazy { Textures.ui.get("topPanel/avhari/voidShard.png").asAtlasRegion() }
    private const val X_POS = 1460
    private const val Y_POS = 800
  }

  private var relic: AbstractRelic = Circlet()
  private var changeTimer = 0f
  private val hb = Hitbox(relic.hb.width, relic.hb.height)

  override fun getHitbox(): Hitbox = hb

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

  override fun placeAtPoint(position: Vector2, distance: Float, rotation: Float, index: Int, size: Int) {
    relic.targetX = X_POS.toFloat()
    relic.targetY = Y_POS.toFloat()
  }

  override fun update() {
    if (purchaced) return
    relic.update()
    hb.move(relic.currentX, relic.currentY)
    hb.update()
    checkOnClick()
    changeTimer -= deltaTime

    if (changeTimer <= 0) {
      changeTimer = TIMER_MAX
      relic = getUniqueRandomRelic(relic)
      relic.currentX = X_POS.toFloat()
      relic.currentY = Y_POS.toFloat()
    }
  }

  override fun render(sb: SpriteBatch) {
    if (!purchaced) {
      relic.renderOutline(sb, false)
      relic.renderWithoutAmount(sb, Color.BLACK.cpy().also { it.a = .25f })
    }
    hb.render(sb)
  }

  private fun getUniqueRandomRelic(currentRelic: AbstractRelic): AbstractRelic {
    val newRelic = randomRelic(randomRelicTier())
    return if (newRelic.relicId == currentRelic.relicId) getUniqueRandomRelic(currentRelic) else newRelic
  }

  private fun randomRelic(relicTier: AbstractRelic.RelicTier): AbstractRelic {
    return RelicLibrary.getRelic(when (relicTier) {
      AbstractRelic.RelicTier.COMMON -> {
        AbstractDungeon.commonRelicPool[relicRng.random(AbstractDungeon.commonRelicPool.lastIndex)]
      }
      AbstractRelic.RelicTier.UNCOMMON -> {
        AbstractDungeon.uncommonRelicPool[relicRng.random(AbstractDungeon.uncommonRelicPool.lastIndex)]
      }
      AbstractRelic.RelicTier.RARE -> {
        AbstractDungeon.rareRelicPool[relicRng.random(AbstractDungeon.rareRelicPool.lastIndex)]
      }
      AbstractRelic.RelicTier.SHOP -> {
        AbstractDungeon.shopRelicPool[relicRng.random(AbstractDungeon.shopRelicPool.lastIndex)]
      }
      else -> {
        Circlet.ID
      }
    }).makeCopy()
  }

  private fun randomRelicTier(): AbstractRelic.RelicTier = when (relicRng.random(0, 3)) {
    0 -> AbstractRelic.RelicTier.COMMON
    1 -> AbstractRelic.RelicTier.UNCOMMON
    2 -> AbstractRelic.RelicTier.RARE
    else -> AbstractRelic.RelicTier.SHOP
  }
}