package com.blanktheevil.infinitespire.patches.utils.locators

import com.evacipated.cardcrawl.modthespire.lib.LineFinder
import com.evacipated.cardcrawl.modthespire.lib.Matcher
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import javassist.CtBehavior

class RemovePowerLocator : SpireInsertLocator() {
  override fun Locate(ctb: CtBehavior?): IntArray {
    val matcher = Matcher.NewExprMatcher(RemoveSpecificPowerAction::class.java)
    return try {
      LineFinder.findInOrder(ctb, matcher)
    } catch (e: Exception) {
      IntArray(0)
    }
  }
}