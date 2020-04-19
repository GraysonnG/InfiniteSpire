package com.blanktheevil.infinitespire.interfaces

import com.megacrit.cardcrawl.monsters.AbstractMonster

interface OnMonsterDeathInterface: IInfiniteSpire {
  fun onMonsterDeath(monster: AbstractMonster)

  companion object {
    val subscribers = mutableListOf<OnMonsterDeathInterface>()
  }
}