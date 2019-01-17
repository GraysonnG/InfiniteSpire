package infinitespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class KeyMirrorPower extends AbstractPower {

	public String powerID;
	private PowerStrings strings;

	public enum KeyColor {
		RUBY,
		SAPPHIRE,
		EMERALD
	}

	private KeyColor keyColor;

	public KeyMirrorPower(AbstractCreature owner, KeyColor keyColor){
		this.keyColor = keyColor;
		this.owner = owner;
		this.amount = -1;
		powerID = InfiniteSpire.createID("KeyMirrorPower") + "_" + keyColor.toString().toLowerCase();
		strings = CardCrawlGame.languagePack.getPowerStrings(powerID);
		this.ID = powerID;
		this.type = PowerType.BUFF;
		this.priority = -99999;
		this.name = strings.NAME;

		switch(keyColor){
			case RUBY:
				this.img = InfiniteSpire.Textures.getPowerTexture("rubyKey.png");
				break;
			case SAPPHIRE:
				this.img = InfiniteSpire.Textures.getPowerTexture("sapphireKey.png");
				break;
			case EMERALD:
				this.img = InfiniteSpire.Textures.getPowerTexture("emeraldKey.png");
				break;
		}

		this.updateDescription();
	}

	@Override
	public void updateDescription() {
		this.description = this.owner.name + strings.DESCRIPTIONS[0];
	}
}
