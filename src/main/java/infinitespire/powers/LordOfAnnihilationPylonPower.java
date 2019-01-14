package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.monsters.LordOfAnnihilation;
import infinitespire.monsters.ShieldPylon;
import infinitespire.util.TextureLoader;

public class LordOfAnnihilationPylonPower extends AbstractPower{
    public LordOfAnnihilationPylonPower(LordOfAnnihilation owner){
        this.ID = "is_PylonPower";
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.img = TextureLoader.getTexture("img/infinitespire/powers/pylonPower.png");
        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(!isPlayer){
            int numOfPylons = 0;
            for(AbstractMonster m : AbstractDungeon.getMonsters().monsters){
                if(m instanceof ShieldPylon){
                    numOfPylons ++;
                }
            }

            if(numOfPylons > 0) {
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, owner, new LordOfAnnihilationIntangiblePower(owner, 1), 1));
            }
        }
    }

    @Override
    public void updateDescription() {
      if (Settings.language == Settings.GameLanguage.FRA){
        this.name = "Pylone à Bouclier";
        this.description = "Tant qu'il y a au moins #b1 #ypylon #y\u00e0 #yBouclier ,gagnez #b1 #yIntangible à la fin du tour.";
      } else {
        this.name = "Pylon Shield";
        this.description = "As long as there is at least #b1 #yShield #yPylon, gain #b1 #yIntangible at the end of the turn.";
      }

    }
}
