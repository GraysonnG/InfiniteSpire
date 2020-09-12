package com.blanktheevil.infinitespire.badges

import basemod.AutoAdd
import basemod.interfaces.ISubscriber
import basemod.interfaces.OnStartBattleSubscriber
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.effectsQueue
import com.blanktheevil.infinitespire.interfaces.IInfiniteSpire
import com.blanktheevil.infinitespire.utils.log
import com.blanktheevil.infinitespire.vfx.BadgeObtainVfx
import com.megacrit.cardcrawl.rooms.AbstractRoom

@AutoAdd.Ignore
abstract class Badge(val id: String) : IInfiniteSpire, ISubscriber, OnStartBattleSubscriber {
  private var obtainedThisCombat = false
  private val strings = InfiniteSpire.badgeStringsKt[id]
  val name = strings!!.NAME
  val description = strings!!.DESCRIPTION
  var life = 0f


  abstract fun giveReward()
  abstract fun reset()

  protected fun completed() {
    if (!obtainedThisCombat) {
      obtainedThisCombat = true
//      effectsQueue.add(BadgeObtainVfx(this)) // replace with custom render
      InfiniteSpire.badgeOverlay.addBadge(this)
      giveReward()
    }
  }

  override fun receiveOnBattleStart(r: AbstractRoom) {
    log.info("Badges Reset...")
    obtainedThisCombat = false
    reset()
  }
}