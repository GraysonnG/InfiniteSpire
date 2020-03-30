package com.blanktheevil.infinitespire.patches.utils.locators

import com.evacipated.cardcrawl.modthespire.lib.LineFinder
import com.evacipated.cardcrawl.modthespire.lib.Matcher
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator
import com.megacrit.cardcrawl.actions.common.ReducePowerAction
import javassist.CtBehavior

class ReducePowerLocator : SpireInsertLocator() {
  override fun Locate(ctb: CtBehavior?): IntArray {
    val matcher = Matcher.NewExprMatcher(ReducePowerAction::class.java)
    return try {
      LineFinder.findInOrder(ctb, matcher)
    } catch (e: Exception) {
      IntArray(0)
    }
  }
}