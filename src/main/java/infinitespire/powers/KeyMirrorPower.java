package infinitespire.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class KeyMirrorPower extends AbstractPower {

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
		this.ID = "is_KeyMirrorPower_" + keyColor.toString().toLowerCase();
		this.type = PowerType.BUFF;
		this.priority = -99999;

		switch(keyColor){
			case RUBY:
				this.name = "Ruby Mirror";
				this.img = InfiniteSpire.getTexture("img/infinitespire/powers/rubyKey.png");
				break;
			case SAPPHIRE:
				this.name = "Sapphire Mirror";
				this.img = InfiniteSpire.getTexture("img/infinitespire/powers/sapphireKey.png");
				break;
			case EMERALD:
				this.name = "Emerald Mirror";
				this.img = InfiniteSpire.getTexture("img/infinitespire/powers/emeraldKey.png");
				break;
		}

		this.updateDescription();
	}

	@Override
	public void updateDescription() {
		switch (this.keyColor) {
			case RUBY:
				this.description = owner.name + " will be more intelligent.";
				break;
			case SAPPHIRE:
				this.description = owner.name + " will be more aggressive.";
				break;
			case EMERALD:
				this.description = owner.name + " will be more powerful.";
				break;
		}
	}
}
