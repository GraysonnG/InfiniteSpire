package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class CriticalPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("CriticalPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public CriticalPower(AbstractPlayer player) {
		this.owner = player;
		this.amount = -1;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("crit.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
	}

	@Override
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType info) {
		if(info == DamageInfo.DamageType.NORMAL) {
			return damageAmount *= 2;
		}
	
		return damageAmount;
	}
	
	

	@Override
	public void onAfterUseCard(AbstractCard card, UseCardAction action) {
		if(card.type == AbstractCard.CardType.ATTACK) {
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
		}
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}
}