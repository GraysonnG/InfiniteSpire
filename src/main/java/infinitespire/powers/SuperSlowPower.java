package infinitespire.powers;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.util.TextureLoader;

public class SuperSlowPower extends AbstractPower {

    public SuperSlowPower(AbstractMonster owner, int amount){
        this.owner = owner;
        this.amount = amount;
        this.ID = "is_Shattered";
        this.img = TextureLoader.getTexture("img/infinitespire/powers/superslow.png");
        this.type = PowerType.DEBUFF;
        this.updateDescription();
    }

    @Override
    public void onAfterUseCard(final AbstractCard card, final UseCardAction action) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SuperSlowPower((AbstractMonster) this.owner, 1), 1));
    }

    @Override
    public float atDamageReceive(final float damage, final DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1.0f + this.amount * 0.1f);
        }
        return damage;
    }

    @Override
    public void updateDescription() {
      	if (Settings.language == Settings.GameLanguage.FRA){
          this.name = "D\u00e9chiquet\u00e9";
          this.description = "Pour chaque carte jou\u00e9e jusqu'à la fin du combat, " + owner.name + " prends #b10% de dégats en plus des #yAttaques. NL (Prends #b" + amount * 10 + "% dégats en plus)";
        } else {
          this.name = "Shredded";
          this.description = "For each card played for the rest of combat, " + owner.name + " takes #b10% more damage from #yAttacks. NL (Takes #b" + amount * 10 + "% more damage)";
        }

    }
}
