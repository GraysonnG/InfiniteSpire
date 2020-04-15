package com.blanktheevil.infinitespire.actions

import basemod.BaseMod
import com.blanktheevil.infinitespire.extensions.deltaTime
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction
import com.megacrit.cardcrawl.cards.SoulGroup
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.powers.NoDrawPower

class DrawCardAndUpgrade(
  source: AbstractCreature,
  amount: Int
) : AbstractGameAction() {
  private var shuffleDeck = false

  init {
    if (player.hasPower(NoDrawPower.POWER_ID)) {
      player.getPower(NoDrawPower.POWER_ID).flash()
      setValues(player, source, amount)
      isDone = true
      duration = 0f
      actionType = ActionType.WAIT
    } else {
      setValues(player, source, amount)
      actionType = ActionType.DRAW
      this.duration = if (Settings.FAST_MODE) {
        Settings.ACTION_DUR_XFAST
      } else {
        Settings.ACTION_DUR_FASTER
      }
    }
  }


  override fun update() {
    if (amount <= 0) {
      this.isDone = true
      return
    }

    if (SoulGroup.isActive()) return

    val drawPile = player.drawPile
    val discPile = player.discardPile
    val drawSize = drawPile.size()
    val discSize = discPile.size()

    when {
      amount <= 0 || drawSize + discSize == 0 -> {
        this.isDone = true
        return
      }
      SoulGroup.isActive() -> return
      player.hand.size() == BaseMod.MAX_HAND_SIZE -> {
        player.createHandIsFullDialog()
        this.isDone = true
        return
      }
      !shuffleDeck -> {
        if (amount > drawSize) {
          val tmp = amount - drawSize
          addToTop(DrawCardAndUpgrade(player, tmp))
          addToTop(EmptyDeckShuffleAction())
          if (drawSize != 0) {
            addToTop(DrawCardAndUpgrade(player, drawSize))
          }
          this.amount = 0
          this.isDone = true
        }
      }
    }

    this.duration -= deltaTime

    if (this.amount != 0 && this.duration > 0f) {
      this.duration = if (Settings.FAST_MODE) {
        Settings.ACTION_DUR_XFAST
      } else {
        Settings.ACTION_DUR_FASTER
      }
      --amount
      if (!drawPile.isEmpty) {
        if (drawPile.topCard.canUpgrade()) {
          drawPile.topCard.upgrade()
        }
        player.draw()
        player.hand.refreshHandLayout()
      }
    }
  }
}