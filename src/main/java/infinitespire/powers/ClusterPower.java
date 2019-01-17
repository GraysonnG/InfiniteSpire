package infinitespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class ClusterPower extends AbstractPower {

	public static final String powerID = InfiniteSpire.createID("ClusterPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public ClusterPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = -1;
		this.name = strings.NAME;
		this.ID = powerID;
		this.img = InfiniteSpire.Textures.getPowerTexture("cluster.png");
		this.type = PowerType.BUFF;
		this.priority = -99999;
		this.updateDescription();
	}

	@Override
	public void updateDescription(){
		this.description = strings.DESCRIPTIONS[0];
	}
}
