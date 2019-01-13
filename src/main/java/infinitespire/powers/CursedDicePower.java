package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;

public class CursedDicePower extends AbstractPower {

	public static final String powerID = "is_DicePower";

	public CursedDicePower(AbstractPlayer player) {
		this.owner = player;
		this.amount = -1;

		this.ID = powerID;
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/crit.png");
		this.type = PowerType.BUFF;
		this.priority = 99;
		this.updateDescription();
	}

	public void updateDescription() {
		if (Settings.language == Settings.GameLanguage.FRA){
						this.name = "D\u00e9s maudit";
						this.description = "Vous ne prenez plus de d\u00e9gats.";
		} else {
						this.name = "Cursed Dice";
						this.description = "You no longer take damage.";
		}

	}
}
