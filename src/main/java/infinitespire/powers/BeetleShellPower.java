package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import basemod.interfaces.CloneablePowerInterface;
import infinitespire.InfiniteSpire;

public class BeetleShellPower extends AbstractPower implements CloneablePowerInterface {

	public static final String powerID = InfiniteSpire.createID("BeetleShellPower");
	private static PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public BeetleShellPower(AbstractPlayer player, boolean shouldRemove) {
		this(player);
	}
	
	public BeetleShellPower(AbstractPlayer player) {
		this.owner = player;
		this.amount = 1;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("beetleshell.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
        this.priority = 6;
	}
	
	@Override
	public float modifyBlock(float blockAmount) {
		return blockAmount * 2f;
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if(card.baseBlock > 0) {
			AbstractPlayer p = AbstractDungeon.player;
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, ID));
		}
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}

	@Override
	public AbstractPower makeCopy() {
		return new BeetleShellPower((AbstractPlayer) this.owner);
	}
}
