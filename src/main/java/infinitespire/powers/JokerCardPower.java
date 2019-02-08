package infinitespire.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class JokerCardPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("JokerCardPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public JokerCardPower(AbstractPlayer owner) {
		this.owner = owner;
		this.amount = 1;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("jokercard.png");
		this.type = PowerType.BUFF;
		this.updateDescription();
		this.priority = 6;
	}

	@Override
	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if(!card.purgeOnUse) {
			AbstractMonster m = null;
			if(action.target != null) {
				m = (AbstractMonster)action.target;
			}
			AbstractCard copy = card.makeSameInstanceOf();
			AbstractDungeon.player.limbo.addToBottom(copy);
			copy.current_x = card.current_x;
			copy.current_y = card.current_y;
			copy.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
			copy.target_y = Settings.HEIGHT / 2.0f;
			copy.freeToPlayOnce = true;
			if(m != null) {
				copy.calculateCardDamage(m);
			}
			copy.purgeOnUse = true;
			AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(copy, m, card.energyOnUse));

			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this.ID));
		}
	}	
}
