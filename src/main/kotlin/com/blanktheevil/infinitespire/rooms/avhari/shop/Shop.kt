package com.blanktheevil.infinitespire.rooms.avhari.shop

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.blanktheevil.infinitespire.cards.utils.CardManager
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.SpireElement
import com.blanktheevil.infinitespire.relics.utils.RelicManager
import com.blanktheevil.infinitespire.rooms.avhari.interfaces.ShopElementBase
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.MathHelper

class Shop : SpireElement {
  companion object {
    private val POSITION = Vector2((Settings.WIDTH.div(3f).times(2f)).plus(75f.scale()), Settings.HEIGHT.div(2f))
    private val CARD_DIST = Settings.HEIGHT / 3.5f
    private val RELIC_DIST = 90f.scale()
    private const val CARD_COST = 8
    private const val RELIC_COST= 15
    private const val SPIN_SPEED = 15f
  }

  private var rotationCards = 0f
  private var rotationRelics = 0f
  private var velocityCards = 1f
  private var velocityRelics = 1f
  private var hoveredCards = false
  private var hoveredRelics = false
  private val cards = CardManager.getBlackCardList(5).map {
    ShopCard(it, CARD_COST)
  }
  private val relics = RelicManager.getRelicList(3).map {
    ShopRelic(it, RELIC_COST)
  }
  private val elements = ArrayList<ShopElementBase>().also {
    it.addAll(cards)
    it.addAll(relics)
  }

  override fun update() {
    elements.forEach { it.update() }
    hoveredCards = cards.any { it.getHitbox().hovered }
    hoveredRelics = relics.any { it.getHitbox().hovered }

    velocityCards = if (hoveredCards) MathHelper.scaleLerpSnap(velocityCards, 0f)
    else MathHelper.scaleLerpSnap(velocityCards, 1f)
    velocityRelics = if (hoveredRelics) MathHelper.scaleLerpSnap(velocityRelics, 0f)
    else MathHelper.scaleLerpSnap(velocityRelics, 1f)

    rotationCards += SPIN_SPEED.times(velocityCards).times(deltaTime)
    rotationRelics += -SPIN_SPEED.times(velocityRelics).times(deltaTime)

    placeElements()
  }

  override fun render(sb: SpriteBatch) {
    renderOnBot(sb)
    renderOnTop(sb)
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