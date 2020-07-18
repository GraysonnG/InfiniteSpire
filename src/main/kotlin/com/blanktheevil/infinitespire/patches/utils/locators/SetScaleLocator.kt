package com.blanktheevil.infinitespire.patches.utils.locators

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.evacipated.cardcrawl.modthespire.lib.LineFinder
import com.evacipated.cardcrawl.modthespire.lib.Matcher
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator
import javassist.CtBehavior

class SetScaleLocator : SpireInsertLocator() {
  override fun Locate(ctb: CtBehavior?): IntArray {
    val matcher = Matcher.MethodCallMatcher(BitmapFont.BitmapFontData::class.java, "setScale")
    val lines = LineFinder.findAllInOrder(ctb, matcher)
    val lastElement = lines[lines.lastIndex]
    return arrayOf(lastElement + 1).toIntArray()
  }
}