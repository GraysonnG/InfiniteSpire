package com.blanktheevil.infinitespire.relics

import com.badlogic.gdx.graphics.Color
import com.blanktheevil.infinitespire.cards.utils.inPuzzleCube
import com.blanktheevil.infinitespire.extensions.actionManager
import com.blanktheevil.infinitespire.extensions.getRandomItem
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect
import kotlin.streams.toList

class PuzzleCube : Relic(ID, IMG, TIER, SOUND) {
  companion object {
    val ID = "PuzzleCube".makeID()
    private const val IMG = "puzzlecube"
    private val TIER = RelicTier.COMMON
    private val SOUND = LandingSound.SOLID
    private val DEFAULT_BLUE = Color(0.2f, 0.9f, 1f, 0.25f)
  }

  private var selectedCard: AbstractCard? = null

  override fun atTurnStart() {
    val allCards = CardGroup(CardGroup.CardGroupType.UNSPECIFIED).also {
      with(it.group) {
        addAll(player.discardPile.group)
        addAll(player.drawPile.group)
        addAll(player.hand.group)
        addAll(player.exhaustPile.group)
      }
    }

    allCards.group.forEach {
      it.inPuzzleCube = false
      it.glowColor = DEFAULT_BLUE.cpy()
    }

    if (player.drawPile.size() > 0) {
      flash()
      getCard(player.drawPile).apply {
        if (this != null) {
          selectedCard = this
          val copy = this.makeStatEquivalentCopy()
          copy.inPuzzleCube = true
          this.inPuzzleCube = true
          copy.glowColor = Color.GOLD.cpy()
          this.glowColor = Color.GOLD.cpy()
          AbstractDungeon.effectsQueue.add(ShowCardBrieflyEffect(copy))
        }
      }
    }
  }

  override fun onPlayCard(c: AbstractCard, m: AbstractMonster?) {
    if (c.inPuzzleCube) {
      actionManager.addToBottom(RelicAboveCreatureAction(player, this))
      actionManager.addToBottom(DrawCardAction(player, 1))
      c.inPuzzleCube = false
    }
  }

  private fun getCard(group: CardGroup): AbstractCard? {
    return group.group.stream()
      .filter { it.type != AbstractCard.CardType.CURSE && it.type != AbstractCard.CardType.STATUS }
      .toList()
      .getRandomItem(AbstractDungeon.cardRandomRng)
  }
}