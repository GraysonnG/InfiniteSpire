package infinitespire.helpers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.actions.DrawCardAndUpgradeAction;
import infinitespire.cards.black.Virus;
import infinitespire.patches.CardColorEnumPatch;

import java.util.ArrayList;
import java.util.Map;

public class CardHelper {
	private static ArrayList<BlackCard> blackCards = new ArrayList<BlackCard>();
	
	public static ArrayList<AbstractCard> getBlackRewardCards() {
		ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
		int amountOfCardsToGive = 3;
		int attempts = 1000;
		
		do {
			boolean isUnique = true;
			BlackCard card = getRandomBlackCard();
			for(AbstractCard c : cards) {
				if(c.cardID.equals(card.cardID)) {
					isUnique = false;
				}
			}
			if(isUnique) {
				cards.add(card);
				amountOfCardsToGive--;
			}
			attempts--;
			if(attempts == 0) InfiniteSpire.logger.info("Could not find enough cards for reward.");
		} while(amountOfCardsToGive > 0 && attempts >= 0);
		return cards;
	}
	
	public static BlackCard getRandomBlackCard() {
		ArrayList<String> keys = new ArrayList<>();
		for(Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
			if(c.getValue().color == CardColorEnumPatch.CardColorPatch.INFINITE_BLACK){
				keys.add(c.getKey());
			}
		}
		AbstractCard card = CardLibrary.cards.get(keys.get(AbstractDungeon.cardRng.random(0, keys.size() - 1)));
		return (BlackCard) card.makeStatEquivalentCopy();
	}
	
	public static void addCard(AbstractCard card) {
		if(card instanceof BlackCard) {
			blackCards.add((BlackCard) card);
		}
		BaseMod.addCard(card);
	}

	public static void addVirusTypes() {
		Virus burning = new Virus(
			InfiniteSpire.createID("Virus: Burning"), "Virus: Burning",
			"img/infinitespire/cards/virus.png", 0,
			"Deal !D! damage.",
			AbstractCard.CardType.ATTACK, AbstractCard.CardTarget.ENEMY, false,
			(me, player, monster) -> {
				AbstractDungeon.actionManager.addToBottom(
					new DamageAction(
						monster,
						new DamageInfo(player, me.damage), AbstractGameAction.AttackEffect.FIRE));
			},
			(me) -> {
				if(!me.upgraded){
					me.upgradeName();
					me.upgradeDamage(3);
				}
			},
			(me) -> {
				me.baseDamage = 8;
			}
		);
		Virus coagulating = new Virus(
			InfiniteSpire.createID("Virus: Coagulating"), "Virus: Coagulating",
			"img/infinitespire/cards/virus.png", 0,
			"Gain !B! block.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false,
			(me, player, monster) -> {
				AbstractDungeon.actionManager.addToBottom(
					new GainBlockAction(player, player, me.block)
				);
			},
			(me) -> {
				if(!me.upgraded) {
					me.upgradeName();
					me.upgradeBlock(3);
				}
			},
			(me) -> {
				me.baseBlock = 6;
			}
		);
		Virus toxic = new Virus(
			InfiniteSpire.createID("Virus: Toxic"), "Virus: Toxic",
			"img/infinitespire/cards/virus.png", 0,
			"Apply !M! Poison.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.ENEMY, false,
			(me, player, monster) -> {
				AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(monster, player,
						new PoisonPower(monster, player, me.magicNumber), me.magicNumber)
				);
			},
			(me) -> {
				if(!me.upgraded){
					me.upgradeName();
					me.upgradeMagicNumber(2);
				}
			},
			(me) -> {
				me.baseMagicNumber = 3;
				me.magicNumber = 3;
			}
		);
		Virus creeping = new Virus(
			InfiniteSpire.createID("Virus: Growing"), "Virus: Creeping",
			"img/infinitespire/cards/virus.png", 0,
			"Draw 1 Card.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false,
			(me, player, monster) -> {
				if(!me.upgraded) {
					AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, 1));
				}else{
					AbstractDungeon.actionManager.addToBottom(new DrawCardAndUpgradeAction(player, 1));
				}
			},
			(me) -> {
				if(!me.upgraded){
					me.upgradeName();
					me.rawDescription = "Draw 1 Card and Upgrade it." + Virus.getPostDescription(me.masterUpgraded);
					me.initializeDescription();
				}
			},
			(me) -> {}
		);
		Virus spreading = new Virus(
			InfiniteSpire.createID("Virus: Spreading"), "Virus: Spreading",
			"img/infinitespire/cards/virus.png", 3,
			"Add a [#9166ff]Virus[] to your deck.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false,
			(me, player, monster) -> {
				Virus.MasterVirus card = new Virus.MasterVirus();
				if(me.upgraded){
					card.upgrade();
				}
				card.upgraded = me.upgraded;
				AbstractDungeon.actionManager.addToBottom(new AddCardToDeckAction(card));
			},
			(me) -> {
				if(!me.upgraded){
					me.upgradeName();
					me.rawDescription = "Add an upgraded [#b400ff]Virus[] to your deck." + Virus.getPostDescription(me.masterUpgraded);
					me.initializeDescription();
				}
			},
			(me) -> {}
		);
		Virus weakening = new Virus(
			InfiniteSpire.createID("Virus: Weakening"), "Virus: Weakening",
			"img/infinitespire/cards/virus.png", 0,
			"Apply !M! Weak.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.ENEMY, false,
			(me, player, monster) -> {
				if (!me.upgraded) {
					AbstractDungeon.actionManager.addToBottom(
						new ApplyPowerAction(monster, player, new WeakPower(monster, me.magicNumber, false))
					);
				} else {
					for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
						AbstractDungeon.actionManager.addToBottom(
							new ApplyPowerAction(mo, player, new WeakPower(mo, me.magicNumber, false))
						);
					}
				}
			},
			(me) -> {
				if (!me.upgraded) {
					me.upgradeName();
					me.target = AbstractCard.CardTarget.ALL_ENEMY;
					me.rawDescription = "Apply !M! Weak to all enemies." + Virus.getPostDescription(me.masterUpgraded);
					me.initializeDescription();
				}
			},
			(me) -> {
				me.baseMagicNumber = 2;
				me.magicNumber = 2;
			}
		);
		Virus afflicting = new Virus(
			InfiniteSpire.createID("Virus: Afflicting"), "Virus: Afflicting",
			"img/infinitespire/cards/virus.png", 0,
			"Apply !M! Vulnerable.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.ENEMY, false,
			(me, player, monster) -> {
				if (!me.upgraded) {
					AbstractDungeon.actionManager.addToBottom(
						new ApplyPowerAction(monster, player, new VulnerablePower(monster, me.magicNumber, false))
					);
				} else {
					for(AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
						AbstractDungeon.actionManager.addToBottom(
							new ApplyPowerAction(mo, player, new VulnerablePower(mo, me.magicNumber, false))
						);
					}
				}
			},
			(me) -> {
				if (!me.upgraded) {
					me.upgradeName();
					me.target = AbstractCard.CardTarget.ALL_ENEMY;
					me.rawDescription = "Apply !M! Vulnerable to all enemies." + Virus.getPostDescription(me.masterUpgraded);
					me.initializeDescription();
				}
			},
			(me) -> {
				me.baseMagicNumber = 2;
				me.magicNumber = 2;
			}
		);
		Virus energetic = new Virus(
			InfiniteSpire.createID("Virus: Energetic"), "Virus: Energetic",
			"img/infinitespire/cards/virus.png", 0,
			"Gain [E] .",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF, false,
			(me, p, m) -> {
				AbstractDungeon.actionManager.addToBottom(
					new GainEnergyAction(1)
				);
			},
			(me) -> {
				if(!me.upgraded) {
					me.upgradeName();
				}
			},
			(me) -> {}
		);


		Virus.virusInstances.add(burning);
		Virus.virusInstances.add(coagulating);
		Virus.virusInstances.add(toxic);
		Virus.virusInstances.add(creeping);
		Virus.virusInstances.add(spreading);
		Virus.virusInstances.add(weakening);
		Virus.virusInstances.add(afflicting);
		Virus.virusInstances.add(energetic);
	}


	public static BlackCard[] getBlackCards() {
		return blackCards.toArray(new BlackCard[0]);
	}
}
