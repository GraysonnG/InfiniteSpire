package com.blanktheevil.infinitespire.rooms.avhari.interfaces

import basemod.ReflectionHacks
import com.blanktheevil.infinitespire.utils.getVoidShardCount
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.ui.buttons.ProceedButton

abstract class ShopElementBase(protected val cost: Int) : ShopElement {
  companion object {
    private val proceedHb
      get() = ReflectionHacks.getPrivate(AbstractDungeon.overlayMenu.proceedButton, ProceedButton::class.java, "hb") as Hitbox
  }

  protected var angle = 0f
  var purchaced = false
  var renderOnTop = false

  protected fun canAfford(): Boolean {
    return cost <= getVoidShardCount()
  }

  protected fun checkOnClick() {
    if (leftClicked() && !mouseOverSkipButton()) {
      CardCrawlGame.sound.play("UI_CLICK_1")
      if (canAfford() && !purchaced) {
        purchace()
      }
    }
  }

  override fun mouseOverSkipButton(): Boolean {
    return proceedHb.hovered
  }
}