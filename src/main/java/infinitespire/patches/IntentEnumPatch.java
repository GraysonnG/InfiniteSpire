package infinitespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IntentEnumPatch {

	@SpireEnum
	public static AbstractMonster.Intent INFINITE_TEMPHP;

	@SpireEnum
	public static AbstractMonster.Intent ATTACK_INFINITE_TEMPHP;

}
