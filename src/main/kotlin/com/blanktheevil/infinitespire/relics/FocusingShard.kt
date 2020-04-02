package com.blanktheevil.infinitespire.relics

import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.relics.abstracts.CrystalRelic
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.miscRng
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect

class FocusingShard : CrystalRelic(ID, IMG) {
  companion object {
    val ID = "FocusingShard".makeID()
    private const val IMG = "focusingshard"
  }

  // fairly certain this will crash or not work as expected
  override fun atBattleStartPreDraw() {
    player.drawPile.upgradableCards.group.forEachIndexed { index: Int, _: AbstractCard ->
      if (counter > index) {
        player.drawPile.upgradableCards.group.getRandomItem(miscRng)?.also {
          it.upgrade()
          AbstractDungeon.effectList.add(
            ShowCardBrieflyEffect(it.makeStatEquivalentCopy())
          )
        }
      }
    }
  }
}