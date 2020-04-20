package com.blanktheevil.infinitespire.patches.campfire

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.megacrit.cardcrawl.rooms.CampfireUI

@Suppress("unused", "UNUSED_PARAMETER")
@SpirePatch(clz = CampfireUI::class, method = "renderCampfireButtons")
object VoidOptionRenderPatch {
  @JvmStatic
  @SpirePostfixPatch
  fun run(instance: CampfireUI, sb: SpriteBatch) {
    InfiniteSpire.voidOption.render(sb)
  }
}