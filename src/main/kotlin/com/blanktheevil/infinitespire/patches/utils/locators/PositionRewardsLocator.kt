package com.blanktheevil.infinitespire.patches.utils.locators

import com.evacipated.cardcrawl.modthespire.lib.LineFinder
import com.evacipated.cardcrawl.modthespire.lib.Matcher
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator
import com.megacrit.cardcrawl.screens.CombatRewardScreen
import javassist.CtBehavior

class PositionRewardsLocator : SpireInsertLocator() {
  override fun Locate(ctb: CtBehavior): IntArray {
    return LineFinder.findInOrder(ctb, Matcher.MethodCallMatcher(
      CombatRewardScreen::class.java,
      "positionRewards"
    ))
  }
}