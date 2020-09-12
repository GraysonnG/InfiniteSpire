package com.blanktheevil.infinitespire.patches.gameactionmanager

import com.blanktheevil.infinitespire.interfaces.AfterTurnEndInterface
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.actions.GameActionManager

@SpirePatch(clz = GameActionManager::class, method = "callEndOfTurnActions")
object EndTurnPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun hookEndOfTurn(actionManager: GameActionManager) {
    AfterTurnEndInterface.subscribers.forEach { it.afterEndTurn() }
  }
}