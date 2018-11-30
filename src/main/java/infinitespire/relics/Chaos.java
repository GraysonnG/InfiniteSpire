package infinitespire.relics;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Relic;

import java.util.ArrayList;

public class Chaos extends Relic {
	public static final String ID = InfiniteSpire.createID("Chaos");
	private int relicsToAdd = -1;
	private int relicsAdded = -1;
	private AbstractDungeon.CurrentScreen screenStateAtEquip = null;

	public Chaos() {
		super(ID, "chaos", RelicTier.SHOP, LandingSound.MAGICAL);
	}

	@Override
	public void onEquip() {
		relicsToAdd = AbstractDungeon.player.relics.size() - 1;
		relicsAdded = 0;
		screenStateAtEquip = AbstractDungeon.CurrentScreen.valueOf(AbstractDungeon.screen.toString());
		for(int i = 0; i < relicsToAdd; i++) {
			AbstractDungeon.player.relics.get(i).onUnequip();
		}
		for(int i = 0; i < relicsToAdd; i++) {
			AbstractRelic emptyRelic = (new EmptyRelic()).makeCopy();
			emptyRelic.instantObtain(AbstractDungeon.player, i, false);
		}
	}

	private boolean isFullyEquipped() {
		return relicsToAdd == relicsAdded;
	}

	private boolean hasScreenChanged() {
		return !screenStateAtEquip.equals(AbstractDungeon.screen);
	}

	private AbstractRelic getRandomRelic() {
		ArrayList<AbstractRelic> list = null;

		switch(AbstractDungeon.relicRng.random(0, 6)){
			case 0:
				list = RelicLibrary.commonList;
				break;
			case 1:
				list = RelicLibrary.uncommonList;
				break;
			case 2:
				list = RelicLibrary.rareList;
				break;
			case 3:
				list = RelicLibrary.shopList;
				break;
			case 4:
				list = RelicLibrary.bossList;
				break;
			case 5:
				list = RelicLibrary.starterList;
				break;
			case 6:
				list = RelicLibrary.specialList;
				break;
			default:
				list = RelicLibrary.commonList;
				break;
		}
		if(list != null) {
			AbstractRelic relic = list.get(AbstractDungeon.relicRng.random(list.size() - 1));

			for(AbstractRelic r : AbstractDungeon.player.relics) {
				if(r.relicId.equals(relic.relicId)){
					relic = getRandomRelic();
				}
			}
			return removeRelicFromPools(relic);
		}
		return new Circlet();
	}

	private AbstractRelic removeRelicFromPools(AbstractRelic relic){
		AbstractDungeon.commonRelicPool.remove(relic.relicId);
		AbstractDungeon.uncommonRelicPool.remove(relic.relicId);
		AbstractDungeon.rareRelicPool.remove(relic.relicId);
		AbstractDungeon.shopRelicPool.remove(relic.relicId);
		AbstractDungeon.bossRelicPool.remove(relic.relicId);

		return relic;
	}

	private AbstractRelic getChaosRelic(){
		String relicKey = getRandomRelic().relicId;
		AbstractRelic relic = RelicLibrary.getRelic(relicKey).makeCopy();
		if(relic instanceof Chaos){
			return new Circlet();
		}
		return relic;
	}

	@Override
	public void update(){
		super.update();
		while(CardCrawlGame.isInARun() && !isFullyEquipped() && !hasScreenChanged()){
			AbstractRelic relicToAdd = getChaosRelic();
			InfiniteSpire.logger.info(relicToAdd.name);
			relicToAdd.instantObtain(AbstractDungeon.player, relicsAdded, true);
			relicsAdded++;
		}
	}
}
