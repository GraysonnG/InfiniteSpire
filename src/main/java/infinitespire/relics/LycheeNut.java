package infinitespire.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class LycheeNut extends Relic {
	public static final String ID = "Lychee Nut";
	public static final String NAME = "Lychee Nut";

	public LycheeNut() {
		super(ID, "lycheenut", RelicTier.RARE, LandingSound.FLAT);
	}
	
	@Override
	public void onEquip() {
		AbstractDungeon.player.decreaseMaxHealth((int)Math.ceil(AbstractDungeon.player.maxHealth * 0.25f));
	}

	@Override
	public void atBattleStart() {
		this.flash();
		AbstractPlayer p = AbstractDungeon.player;
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RegenPower(p, 5), 5));
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new LycheeNut();
	}
}
