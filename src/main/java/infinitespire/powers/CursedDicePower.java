package infinitespire.powers;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class CursedDicePower extends AbstractPower {

	public static final String powerID = "is_DicePower";

	public CursedDicePower(AbstractPlayer player) {
		this.owner = player;
		this.amount = -1;
		this.name = "Cursed Dice";
		this.ID = powerID;
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/crit.png");
		this.type = PowerType.BUFF;
		this.priority = 99;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = "You no longer take damage.";
	}
}
