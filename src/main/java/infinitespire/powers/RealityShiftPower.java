package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.Nightmare;

public class RealityShiftPower extends AbstractPower {
	public RealityShiftPower(Nightmare nightmare) {
		this.owner = nightmare;
		this.amount = 50;
		this.ID = "is_Reality_Shift";
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/realityshift.png");
		this.type = PowerType.BUFF;
		this.isTurnBased = true;
		this.updateDescription();
	}



	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(!isPlayer){
			this.amount = 50;
		}
	}



	public void updateDescription() {
			if (Settings.language == Settings.GameLanguage.FRA){
				this.name = "Modifier la r\u00e9alit\u00e9";
				this.description = "Apr\u00e8s avoir prit #b" + amount + " d\u00e9gats en un toure in a single turn, Cauchemare modifiera la r\u00e9alit\u00e9 et mettera fin à votre tour.";
			} else {
			this.name = "Reality Shift";
			this.description = "After taking #b" + amount + " damage in a single turn, Nightmare will shift reality forcing you to end your turn.";
			}
	}
}
