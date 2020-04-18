package com.blanktheevil.infinitespire.crossover.cards.black

import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.makeID
import com.evacipated.cardcrawl.mod.bard.actions.common.QueueNoteAction
import com.evacipated.cardcrawl.mod.bard.notes.WildCardNote

class DarkHarmony : BlackCard(BUILDER) {
  companion object {
    val ID = "DarkHarmony".makeID()
    private const val MAGIC = 2
    private const val UPG_MAGIC = 1
    private val BUILDER = CardBuilder(ID)
      .img("beta.png")
      .cost(2)
      .skill()
      .self()
      .init {
        baseMagicNumber = MAGIC
        magicNumber = MAGIC
      }
      .upgr {
        upgradeMagicNumber(UPG_MAGIC)
      }
      .use { _, _ ->
        addToBot(QueueNoteAction(WildCardNote.get()))
      }
  }
}