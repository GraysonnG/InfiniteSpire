package com.blanktheevil.infinitespire.patches.sevenwalls

import basemod.ReflectionHacks
import basemod.abstracts.CustomCard
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.cards.black.SevenWalls
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.screens.SingleCardViewPopup

@SpirePatch(clz = SingleCardViewPopup::class, method = "renderPortrait")
object SevenWallsPortraitPatch {
  @JvmStatic
  @SpirePrefixPatch
  fun swapPortraitImg(instance: SingleCardViewPopup, sb: SpriteBatch) {
    val card = ReflectionHacks.getPrivate(instance, SingleCardViewPopup::class.java, "card") as AbstractCard
    if (card is SevenWalls) {
      ReflectionHacks.setPrivate(instance, SingleCardViewPopup::class.java, "portraitImg", CustomCard.getPortraitImage(card))
    }
  }
}