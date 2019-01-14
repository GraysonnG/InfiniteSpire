package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import infinitespire.InfiniteSpire;

public class SpireBlightPower extends AbstractPower {
	public SpireBlightPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = -1;
		this.ID = "is_SpireBlight";
		this.img = InfiniteSpire.getTexture("img/infinitespire/powers/spireblight.png");
		this.type = PowerType.DEBUFF;
		this.updateDescription();
	}

	public void updateDescription() {
		if (Settings.language == Settings.GameLanguage.FRA){
			this.name = "Lance rouillée";
			this.description = "Infligez #25% de dégats en moins, et gagnez #b25% d'Armure en moins.";
		} else {
			this.name = "Spire Blight";
			this.description = "Deal #b25% less damage, and gain #b25% less block.";
		}

	}

	@Override
	public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType type) {
		if(type == DamageInfo.DamageType.NORMAL) {
			return (float) Math.ceil(damageAmount *= 0.75f);
		}

		return damageAmount;
	}

	@Override
	public float modifyBlock(float blockAmount) {
		blockAmount *= 0.75f;
		return blockAmount;
	}
}
