package infinitespire.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import infinitespire.InfiniteSpire;
import infinitespire.monsters.LordOfAnnihilation;
import infinitespire.monsters.ShieldPylon;

public class PylonExplosionPower extends AbstractPower{

    public static final String powerID = InfiniteSpire.createID("LifeLinkPower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

    LordOfAnnihilation boss;

    public PylonExplosionPower(ShieldPylon owner, LordOfAnnihilation boss){
        this.name = strings.NAME;
        this.ID = powerID;
        this.owner = owner;
        this.boss = boss;
        this.amount = -1;
        this.type = PowerType.DEBUFF;
        this.img = InfiniteSpire.Textures.getPowerTexture("lifelink.png");
        this.updateDescription();
    }

    @Override
    public void onDeath() {
        if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && this.owner.currentHealth <= 0){
            AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(boss, boss, boss.currentBlock));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(
                    boss,
                    new DamageInfo(owner, owner.maxHealth, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + owner.maxHealth + strings.DESCRIPTIONS[1] + boss.name;
    }
}
