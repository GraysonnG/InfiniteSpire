package com.blanktheevil.infinitespire.rooms.avhari.shop

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.extensions.*
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.relics.utils.RelicManager
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElementBase
import com.blanktheevil.infinitespire.textures.Textures
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.MathHelper

class Shop : SpireElement {
  companion object {
    private val POSITION = Vector2(
      Settings.WIDTH.div(2f),
      Settings.HEIGHT.div(2f)
    )
    private val CARD_DIST = Settings.HEIGHT / 3.5f
    private val RELIC_DIST = 90f.scale()
    private const val CARD_COST = 8
    private const val RELIC_COST = 15
    private const val SPIN_SPEED = 15f
    private const val VORTEX_SPEED = 10f
    private val VORTEX_TEXTURE by lazy { Textures.ui.get("avhari/portal.png").asAtlasRegion() }
  }

  private var rotationCards = 0f
  private var rotationRelics = 0f
  private var velocityCards = 1f
  private var velocityRelics = 1f
  private var rotationVortex = 0f
  private var hoveredCards = false
  private var hoveredRelics = false
  private val randomRelic = ShopRandomRelic()
  private val cards = CardManager.getBlackCardList(5).map { card ->
    ShopCard(card, CARD_COST)
  }.toMutableList()
  private val relics = RelicManager.getRelicList(3).map { relic ->
    ShopRelic(relic, RELIC_COST)
  }.toMutableList()
  private val elements = ArrayList<ShopElementBase>().also {
    it.addAll(cards)
    it.addAll(relics)
    it.add(randomRelic)
  }

  override fun update() {
    elements.forEach { it.update() }
    elements.removeIfPurchased()
    hoveredCards = cards.any { it.getHitbox().hovered && !it.mouseOverSkipButton() }
    hoveredRelics = relics.any { it.getHitbox().hovered && !it.mouseOverSkipButton() }

    velocityCards = if (hoveredCards) MathHelper.scaleLerpSnap(velocityCards, 0f)
    else MathHelper.scaleLerpSnap(velocityCards, 1f)
    velocityRelics = if (hoveredRelics) MathHelper.scaleLerpSnap(velocityRelics, 0f)
    else MathHelper.scaleLerpSnap(velocityRelics, 1f)

    rotationCards += SPIN_SPEED.times(velocityCards).times(deltaTime)
    rotationRelics -= SPIN_SPEED.times(velocityRelics).times(deltaTime)
    rotationVortex -= VORTEX_SPEED.times(deltaTime)

    placeElements()
    movePlayerRelics()
  }

  private fun MutableList<ShopElementBase>.removeIfPurchased() {
    removeIf { element ->
      when (element) {
        is ShopCard -> cards.removeIf { it.purchaced && it == element }
        is ShopRelic -> relics.removeIf { it.purchaced && it == element }
        else -> element.purchaced
      }
    }
  }

  private fun movePlayerRelics() {
    player.relics.forEach {
      it.currentX = MathHelper.scaleLerpSnap(it.currentX, it.targetX)
      it.currentY = MathHelper.scaleLerpSnap(it.currentY, it.targetY)
      it.hb.move(it.targetX, it.targetY)
    }
  }

  override fun render(sb: SpriteBatch) {
    renderVortex(sb)
    renderOnBot(sb)
    renderOnTop(sb)
  }

  private fun renderVortex(sb: SpriteBatch) {
    with(VORTEX_TEXTURE) {
      sb.color = Color.WHITE.cpy().also { it.a = 1f }
      sb.additiveMode()
      sb.draw(
        this,
        POSITION.x.minus(halfWidth),
        POSITION.y.minus(halfHeight),
        halfWidth,
        halfHeight,
        width,
        height,
        scale,
        scale,
        rotationVortex
      )
      sb.normalMode()
    }
  }

  private fun renderOnBot(sb: SpriteBatch) {
    elements.filter { !it.renderOnTop }
      .forEach { it.render(sb) }
  }

  private fun renderOnTop(sb: SpriteBatch) {
    elements.filter { it.renderOnTop }
      .forEach { it.render(sb) }
  }

  private fun placeElements() {
    elements.forEachIndexed { index, element ->
      when (element) {
        is ShopCard -> element.placeAtPoint(POSITION, CARD_DIST, rotationCards, index, cards.size)
        is ShopRelic -> element.placeAtPoint(POSITION, RELIC_DIST, rotationRelics, index, relics.size)
      }
    }
  }
}