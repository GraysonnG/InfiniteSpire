package com.blanktheevil.infinitespire.cards.utils

import com.blanktheevil.infinitespire.cards.Card
import com.blanktheevil.infinitespire.textures.Textures
import com.blanktheevil.infinitespire.cards.black.BlackCard
import com.blanktheevil.infinitespire.patches.EnumPatches
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster

@Suppress("unused")
class CardBuilder(
  var id: String = "",
  var img: String = Textures.cards.getString("beta.png"),
  var type: AbstractCard.CardType = AbstractCard.CardType.SKILL,
  var target: AbstractCard.CardTarget = AbstractCard.CardTarget.NONE,
  var rarity: AbstractCard.CardRarity = AbstractCard.CardRarity.SPECIAL,
  var color: AbstractCard.CardColor = AbstractCard.CardColor.COLORLESS,
  var cost: Int = 0,
  var use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit = {_,_ -> },
  var init: Card.() -> Unit = {},
  var upgr: Card.() -> Unit = {}
) {
  fun id(id: String): CardBuilder {
    this.id = id
    return this
  }

  fun img(img: String): CardBuilder {
    this.img = Textures.cards.getString(img)
    return this
  }

  fun cost(cost: Int): CardBuilder {
    this.cost = cost
    return this
  }

  fun use(use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit): CardBuilder {
    this.use = use
    return this
  }

  fun init(init: Card.() -> Unit): CardBuilder {
    this.init = init
    return this
  }

  fun upgr(upgr: Card.() -> Unit): CardBuilder {
    this.upgr = upgr
    return this
  }

  fun color(color: AbstractCard.CardColor): CardBuilder {
    this.color = color
    return this
  }

  fun red(): CardBuilder {
    this.color = AbstractCard.CardColor.RED
    return this
  }

  fun green(): CardBuilder {
    this.color = AbstractCard.CardColor.GREEN
    return this
  }

  fun blue(): CardBuilder {
    this.color = AbstractCard.CardColor.BLUE
    return this
  }

  fun purple(): CardBuilder {
    this.color = AbstractCard.CardColor.PURPLE
    return this
  }

  fun colorless(): CardBuilder {
    this.color = AbstractCard.CardColor.COLORLESS
    return this
  }

  fun black(): CardBuilder {
    this.color = EnumPatches.CardColor.INFINITE_BLACK
    return this
  }

  fun rare(): CardBuilder {
    this.rarity = AbstractCard.CardRarity.RARE
    return this
  }

  fun uncommon(): CardBuilder {
    this.rarity = AbstractCard.CardRarity.UNCOMMON
    return this
  }

  fun common(): CardBuilder {
    this.rarity = AbstractCard.CardRarity.COMMON
    return this
  }

  fun special(): CardBuilder {
    this.rarity = AbstractCard.CardRarity.SPECIAL
    return this
  }

  fun curse(): CardBuilder {
    this.type = AbstractCard.CardType.CURSE
    this.rarity = AbstractCard.CardRarity.CURSE
    this.color = AbstractCard.CardColor.CURSE
    return this
  }

  fun basic(): CardBuilder {
    this.rarity = AbstractCard.CardRarity.BASIC
    return this
  }

  fun attack(): CardBuilder {
    this.type = AbstractCard.CardType.ATTACK
    return this
  }

  fun skill(): CardBuilder {
    this.type = AbstractCard.CardType.SKILL
    return this
  }

  fun power(): CardBuilder {
    this.type = AbstractCard.CardType.POWER
    return this
  }

  fun enemy(): CardBuilder {
    this.target = AbstractCard.CardTarget.ENEMY
    return this
  }

  fun allEnemy(): CardBuilder {
    this.target = AbstractCard.CardTarget.ALL_ENEMY
    return this
  }

  fun self(): CardBuilder {
    this.target = AbstractCard.CardTarget.SELF
    return this
  }

  fun selfAndEnemy(): CardBuilder {
    this.target = AbstractCard.CardTarget.SELF_AND_ENEMY
    return this
  }

  fun all(): CardBuilder {
    this.target = AbstractCard.CardTarget.ALL
    return this
  }

  fun none(): CardBuilder {
    this.target = AbstractCard.CardTarget.NONE
    return this
  }

  fun build(): Card {
    return if (this.color == EnumPatches.CardColor.INFINITE_BLACK) {
      object: BlackCard(this@CardBuilder) {}
    } else {
      object: Card(this@CardBuilder) {}
    }
  }
}