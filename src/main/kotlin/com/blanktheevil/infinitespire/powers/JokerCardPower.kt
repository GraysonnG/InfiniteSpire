package com.blanktheevil.infinitespire.powers

import com.blanktheevil.infinitespire.extensions.actionManager
import com.blanktheevil.infinitespire.extensions.makeID
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.powers.utils.PowerBuilder
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction
import com.megacrit.cardcrawl.actions.utility.UseCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardQueueItem
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower

class JokerCardPower(owner: AbstractPlayer) : Power(
  owner,
  1,
  BUILDER
) {
  companion object {
    val powerID = "JokerCardPower".makeID()
    private val BUILDER = PowerBuilder(powerID)
      .img("jokercard.png")
      .buff()
      .priority(6)
  }

  override fun updateDesc() {
    this.description = strings.DESCRIPTIONS[0]
  }

  override fun onUseCard(card: AbstractCard, action: UseCardAction) {
    if (!card.purgeOnUse) {
      var m: AbstractMonster? = if (action.target != null) {
        action.target as AbstractMonster
      } else null

      card.makeSameInstanceOf().apply {
        player.limbo.addToBottom(this)
        current_x = card.current_x
        current_y = card.current_y
        target_x = Settings.WIDTH.div(2.0f).minus(300f.scale())
        target_y = Settings.HEIGHT.div(2.0f)
        freeToPlayOnce = true
        if (m != null) {
          calculateCardDamage(m)
        }
        purgeOnUse = true
        actionManager.cardQueue.add(CardQueueItem(this, m, card.energyOnUse))
        actionManager.addToBottom(RemoveSpecificPowerAction(owner, owner, this@JokerCardPower.ID))
      }
    }
  }

  override fun makeCopy(): AbstractPower = JokerCardPower(this.owner as AbstractPlayer)
}