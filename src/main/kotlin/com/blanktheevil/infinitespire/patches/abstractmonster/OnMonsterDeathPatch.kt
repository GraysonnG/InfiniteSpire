package com.blanktheevil.infinitespire.patches.abstractmonster

import com.blanktheevil.infinitespire.interfaces.OnMonsterDeathInterface
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.monsters.AbstractMonster

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = AbstractMonster::class, method = "die", paramtypez = [Boolean::class])
object OnMonsterDeathPatch {
  @JvmStatic
  @SpirePrefixPatch
  fun run(instance: AbstractMonster, trigger: Boolean) {
    if (!instance.isDying) {
      OnMonsterDeathInterface.subscribers.forEach {
        it.onMonsterDeath(instance)
      }
    }
  }
}