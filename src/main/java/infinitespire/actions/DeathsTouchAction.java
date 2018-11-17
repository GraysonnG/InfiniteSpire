package infinitespire.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class DeathsTouchAction extends AbstractGameAction {

	private AbstractPlayer p;
	private AbstractMonster m;
	private int damage;
	private int magicNumber;
	private DamageInfo.DamageType damageType;
	private boolean isFree;
	private int energyAmount;

	public DeathsTouchAction(AbstractPlayer player, AbstractMonster monster, int damage, int magicNumber, DamageInfo.DamageType damageType, boolean freeToPlayOnce, int energyOnUse) {
		this.p = player;
		this.m = monster;
		this.damage = damage;
		this.target = monster;
		this.magicNumber = magicNumber;
		this.damageType = damageType;
		this.isFree = freeToPlayOnce;
		this.energyAmount = energyOnUse;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.actionType = ActionType.SPECIAL;
	}

	@Override
	public void update() {
		int effect = EnergyPanel.totalCount;
		if(this.energyAmount != -1) {
			effect = this.energyAmount;
		}

		effect += 1; //this is the + 1 part of the X + 1

		if(this.p.hasRelic(ChemicalX.ID)) {
			this.p.getRelic(ChemicalX.ID).flash();
			effect += 2;
		}

		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageType), AttackEffect.SLASH_DIAGONAL));

		if(effect > 0) {
			for(int i = 0; i < effect; i++) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
			}

			if(!this.isFree) {
				this.p.energy.use(EnergyPanel.totalCount);
			}
		}

		this.isDone = true;
	}
}
