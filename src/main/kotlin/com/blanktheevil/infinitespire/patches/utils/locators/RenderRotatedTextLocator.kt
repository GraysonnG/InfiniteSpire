package com.blanktheevil.infinitespire.patches.utils.locators

import com.evacipated.cardcrawl.modthespire.lib.LineFinder
import com.evacipated.cardcrawl.modthespire.lib.Matcher
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator
import com.megacrit.cardcrawl.helpers.FontHelper
import javassist.CtBehavior

class RenderRotatedTextLocator : SpireInsertLocator() {
  override fun Locate(ctb: CtBehavior?): IntArray {
    val matcher = Matcher.MethodCallMatcher(FontHelper::class.java, "renderRotatedText")
    return LineFinder.findInOrder(ctb, matcher)
  }
}