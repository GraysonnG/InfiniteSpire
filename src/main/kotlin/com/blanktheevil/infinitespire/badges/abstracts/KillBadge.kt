package com.blanktheevil.infinitespire.badges.abstracts

import basemod.AutoAdd
import basemod.interfaces.OnCardUseSubscriber
import com.blanktheevil.infinitespire.badges.Badge
import com.blanktheevil.infinitespire.extensions.addToBot
import com.blanktheevil.infinitespire.interfaces.hooks.OnMonsterDeathInterface
import com.blanktheevil.infinitespire.utils.addVoidShard
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.monsters.AbstractMonster

@AutoAdd.Ignore
abstract class KillBadge(id: String, val numberOfKillsToTrigger: Int) : Badge(id), OnMonsterDeathInterface, OnCardUseSubscriber {
  private var killedByThisCard = 0

  override fun onMonsterDeath(monster: AbstractMonster) {
    killedByThisCard++
    if (killedByThisCard == numberOfKillsToTrigger) {
      completed()
    }
  }

  override fun receiveCardUsed(c: AbstractCard) {
    addToBot(object : AbstractGameAction() {
      override fun update() {
        addToBot(object : AbstractGameAction() {
          override fun update() {
            reset()
            this.isDone = true
          }
        })
        this.isDone = true
      }
    })
  }

  override fun reset() {
    killedByThisCard = 0
  }

  override fun giveReward() {
    addVoidShard(1)
  }
}