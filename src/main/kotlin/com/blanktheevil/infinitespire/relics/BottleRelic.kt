package com.blanktheevil.infinitespire.relics

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.extensions.currentRoom
import com.blanktheevil.infinitespire.extensions.player
import com.blanktheevil.infinitespire.extensions.scale
import com.blanktheevil.infinitespire.interfaces.Savable
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.input.InputHelper
import com.megacrit.cardcrawl.rooms.AbstractRoom
import kotlin.streams.toList

abstract class BottleRelic (
  id: String,
  img: String,
  tier: RelicTier,
  sound: LandingSound
)
  : Relic(id, img, tier, sound), Savable {

  init {
    InfiniteSpire.subscribe(this)
  }

  var selectedCard: AbstractCard? = null
  private var renderCard: AbstractCard? = null
  protected var cardSelected: Boolean = false

  abstract fun filterGridSelectBy(card: AbstractCard): Boolean
  abstract fun actionWhenSelected(card: AbstractCard)
  abstract fun actionWhenUnEquipped(card: AbstractCard)
  abstract fun isCardBottled(card: AbstractCard): Boolean

  override fun onEquip() {
    cardSelected = false
    val group = CardGroup(CardGroup.CardGroupType.UNSPECIFIED).apply {
      group.addAll(player.masterDeck.group.stream()
        .filter {
          filterGridSelectBy(it)
        }.toList())
    }

    if(group.size() > 0) {
      if (AbstractDungeon.isScreenUp) {
        if (InfiniteSpire.questLogScreen.isOpen()) {
          InfiniteSpire.questLogScreen.toggle()
        }

        AbstractDungeon.dynamicBanner.hide()
        AbstractDungeon.overlayMenu.cancelButton.hide()
        AbstractDungeon.previousScreen = AbstractDungeon.screen
      }

      currentRoom!!.phase = AbstractRoom.RoomPhase.INCOMPLETE
      AbstractDungeon.gridSelectScreen.open(
        CardGroup.getGroupWithoutBottledCards(group),
        1,
        "Select a card.",
        false,
        false,
        false,
        false)
    } else {
      cardSelected = true
    }
  }

  override fun onUnequip() {
    if (selectedCard != null) {
      val cardInDeck = player.masterDeck.getSpecificCard(selectedCard)
      if (cardInDeck != null) {
        actionWhenUnEquipped(cardInDeck)
      }
    }
  }

  override fun update() {
    super.update()

    if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.isNotEmpty()) {
      cardSelected = true
      selectedCard = AbstractDungeon.gridSelectScreen.selectedCards[0].also {
        actionWhenSelected(it)
      }
      currentRoom!!.phase = AbstractRoom.RoomPhase.COMPLETE
      AbstractDungeon.gridSelectScreen.selectedCards.clear()
    }
  }

  private fun renderCardPreview(sb: SpriteBatch) {
    renderCard = if (renderCard == null && selectedCard != null) {
      selectedCard!!.makeStatEquivalentCopy()
    } else renderCard
    if (renderCard != null) {
      with(renderCard!!) {
        drawScale = 0.5f
        current_x = InputHelper.mX
          .plus(this.hb.width.div(2f))
          .plus(10f.scale())
          .plus(this@BottleRelic.hb.width)
          .plus(280f.scale())
        current_y = InputHelper.mY
          .minus(this.hb.height.div(2f))
        render(sb)
      }
    }
  }

  override fun renderTip(sb: SpriteBatch) {
    super.renderTip(sb)

    if(InputHelper.mX < 1400f.scale()) {
      renderCardPreview(sb)
    }
  }
}
