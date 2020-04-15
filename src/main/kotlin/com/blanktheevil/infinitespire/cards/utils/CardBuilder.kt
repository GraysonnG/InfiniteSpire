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
  var exhaust: Boolean = false,
  var use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit = {_,_ -> },
  var init: Card.() -> Unit = {},
  var upgr: Card.() -> Unit = {}
) {
  fun id(id: String): CardBuilder = apply { this.id = id }
  fun img(img: String): CardBuilder = apply { this.img = Textures.cards.getString(img) }
  fun cost(cost: Int): CardBuilder = apply { this.cost = cost }
  fun use(use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit): CardBuilder = apply { this.use = use }
  fun init(init: Card.() -> Unit): CardBuilder = apply { this.init = init }
  fun exhaust(): CardBuilder = apply { exhaust = true }
  fun upgr(upgr: Card.() -> Unit): CardBuilder = apply { this.upgr = upgr }
  fun color(color: AbstractCard.CardColor): CardBuilder = apply { this.color = color }
  fun red(): CardBuilder = apply { this.color = AbstractCard.CardColor.RED }
  fun green(): CardBuilder = apply { this.color = AbstractCard.CardColor.GREEN }
  fun blue(): CardBuilder = apply { this.color = AbstractCard.CardColor.BLUE }
  fun purple(): CardBuilder = apply { this.color = AbstractCard.CardColor.PURPLE }
  fun colorless(): CardBuilder = apply { this.color = AbstractCard.CardColor.COLORLESS }
  fun black(): CardBuilder = apply { this.color = EnumPatches.CardColor.INFINITE_BLACK }
  fun rare(): CardBuilder = apply { this.rarity = AbstractCard.CardRarity.RARE }
  fun uncommon(): CardBuilder = apply { this.rarity = AbstractCard.CardRarity.UNCOMMON }
  fun common(): CardBuilder = apply { this.rarity = AbstractCard.CardRarity.COMMON }
  fun special(): CardBuilder = apply { this.rarity = AbstractCard.CardRarity.SPECIAL }
  fun curse(): CardBuilder = apply {
    this.type = AbstractCard.CardType.CURSE
    this.rarity = AbstractCard.CardRarity.CURSE
    this.color = AbstractCard.CardColor.CURSE
  }
  fun basic(): CardBuilder = apply { this.rarity = AbstractCard.CardRarity.BASIC }
  fun attack(): CardBuilder = apply { this.type = AbstractCard.CardType.ATTACK }
  fun skill(): CardBuilder = apply { this.type = AbstractCard.CardType.SKILL }
  fun power(): CardBuilder = apply { this.type = AbstractCard.CardType.POWER }
  fun enemy(): CardBuilder = apply { this.target = AbstractCard.CardTarget.ENEMY }
  fun allEnemy(): CardBuilder = apply { this.target = AbstractCard.CardTarget.ALL_ENEMY }
  fun self(): CardBuilder = apply { this.target = AbstractCard.CardTarget.SELF }
  fun selfAndEnemy(): CardBuilder = apply { this.target = AbstractCard.CardTarget.SELF_AND_ENEMY }
  fun all(): CardBuilder = apply { this.target = AbstractCard.CardTarget.ALL }
  fun none(): CardBuilder = apply { this.target = AbstractCard.CardTarget.NONE }
  fun build(): Card = if (this.color == EnumPatches.CardColor.INFINITE_BLACK) {
    object: BlackCard(this@CardBuilder) {}
  } else {
    object: Card(this@CardBuilder) {}
  }
}