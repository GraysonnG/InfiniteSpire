package com.blanktheevil.infinitespire.cards

import basemod.AutoAdd
import basemod.abstracts.CustomCard
import com.blanktheevil.infinitespire.InfiniteSpire
import com.blanktheevil.infinitespire.cards.utils.CardBuilder
import com.blanktheevil.infinitespire.extensions.doNothing
import com.blanktheevil.infinitespire.models.CardStringsKt
import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster

@AutoAdd.Ignore
abstract class Card(
  id: String,
  img: String,
  type: CardType,
  target: CardTarget,
  rarity: CardRarity,
  color: CardColor,
  cost: Int,
  private val use: Card.(player: AbstractPlayer?, monster: AbstractMonster?) -> Unit = {_,_ -> },
  private var init: Card.() -> Unit = {},
  private var upgr: Card.() -> Unit = {}
): CustomCard(
  id,
  strings(id).NAME,
  img,
  cost,
  strings(id).DESCRIPTION,
  type,
  color,
  rarity,
  target
)  {
  companion object {
    fun strings(id: String): CardStringsKt = InfiniteSpire.cardStringsKt[id] ?: CardStringsKt()
  }

  init {
    this.init.invoke(this)
  }

  constructor(builder: CardBuilder):
      this(
        builder.id,
        builder.img,
        builder.type,
        builder.target,
        builder.rarity,
        builder.color,
        builder.cost,
        builder.use,
        builder.init,
        builder.upgr
      )

  open fun onUpgrade() = doNothing()

  override fun use(p: AbstractPlayer?, m: AbstractMonster?) {
    use.invoke(this, p, m)
  }

  public override fun upgradeMagicNumber(amount: Int) {
    super.upgradeMagicNumber(amount)
  }

  public override fun upgradeBaseCost(newBaseCost: Int) {
    super.upgradeBaseCost(newBaseCost)
  }

  public override fun upgradeBlock(amount: Int) {
    super.upgradeBlock(amount)
  }

  public override fun upgradeDamage(amount: Int) {
    super.upgradeDamage(amount)
  }

  public override fun addToBot(action: AbstractGameAction?) {
    super.addToBot(action)
  }

  public override fun addToTop(action: AbstractGameAction?) {
    super.addToTop(action)
  }

  override fun upgrade() {
    if (!upgraded) {
      upgradeName()
      onUpgrade()
      upgr.invoke(this)
    }
  }
}