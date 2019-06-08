package infinitespire.helpers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;
import infinitespire.virus.Virus;

import java.util.ArrayList;

public class CardHelper {
	private static ArrayList<BlackCard> blackCards = new ArrayList<BlackCard>();

	public static ArrayList<AbstractCard> getBlackRewardCards() {
		return getBlackRewardCards(3);
	}

	public static ArrayList<AbstractCard> getBlackRewardCards(int amount) {
		ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
		int amountOfCardsToGive = amount;
		int attempts = 1000;
		
		do {
			boolean isUnique = true;
			AbstractCard card = getRandomBlackCard().makeStatEquivalentCopy();
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
		BlackCard card =  blackCards.get(AbstractDungeon.cardRng.random(blackCards.size() -1));
		UnlockTracker.markCardAsSeen(card.cardID);
		card.isSeen = true;
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
			InfiniteSpire.createID(Virus.MasterVirus.ID), "Virus: Burning",
			"img/infinitespire/cards/virus.png", 0,
			"Deal !D! damage.",
			AbstractCard.CardType.ATTACK, AbstractCard.CardTarget.ENEMY,
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
				me.baseDamage = 16;
			}
		);
		Virus coagulating = new Virus(
			InfiniteSpire.createID(Virus.MasterVirus.ID), "Virus: Coagulating",
			"img/infinitespire/cards/virus.png", 0,
			"Gain !B! block.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF,
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
				me.baseBlock = 9;
			}
		);
		Virus toxic = new Virus(
			InfiniteSpire.createID(Virus.MasterVirus.ID), "Virus: Toxic",
			"img/infinitespire/cards/virus.png", 0,
			"Apply !M! Poison.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.ENEMY,
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
				me.baseMagicNumber = 6;
				me.magicNumber = 6;
			}
		);
		Virus energetic = new Virus(
			InfiniteSpire.createID(Virus.MasterVirus.ID), "Virus: Energetic",
			"img/infinitespire/cards/virus.png", 0,
			"Gain [E] .",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF,
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
		Virus healthy = new Virus(
			InfiniteSpire.createID(Virus.MasterVirus.ID), "Virus: Healthy"	,
			"img/infinitespire/cards/virus.png", 1,
			"Heal !M! hp.",
			AbstractCard.CardType.SKILL, AbstractCard.CardTarget.SELF,
			(me, p, m) -> {
				AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, me.magicNumber));
			},
			(me) -> {
				if(!me.upgraded){
					me.upgradeName();
					me.upgradeMagicNumber(1);
				}
			},
			(me) -> {
				me.baseMagicNumber = 2;
				me.magicNumber = 2;
			}
		);

		Virus.virusInstances.add(burning);
		Virus.virusInstances.add(coagulating);
		Virus.virusInstances.add(toxic);
		Virus.virusInstances.add(energetic);
		Virus.virusInstances.add(healthy);
	}


	public static BlackCard[] getBlackCards() {
		return blackCards.toArray(new BlackCard[0]);
	}
}
