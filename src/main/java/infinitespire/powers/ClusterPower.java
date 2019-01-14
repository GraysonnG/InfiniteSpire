package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.util.TextureLoader;

public class ClusterPower extends AbstractPower {
	public ClusterPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = -1;
		this.ID = "is_ClusterPower";
		this.img = TextureLoader.getTexture("img/infinitespire/powers/cluster.png");
		this.type = PowerType.BUFF;
		this.priority = -99999;
		this.updateDescription();
	}

	@Override
	public void updateDescription(){
		if (Settings.language == Settings.GameLanguage.FRA){
			this.name = "Groupe";
			this.description = "Lorsqu'il prend des d\u00e9gats sur ses PV, #yMasse #yDe #Form se s\u00e9parera en monstres plus petits.";
		} else {
			this.name = "Cluster";
			this.description = "When taking HP damage, #yMass #yof #yShapes may break apart into smaller monsters.";
		}

	}
}
