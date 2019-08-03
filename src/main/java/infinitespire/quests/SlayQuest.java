package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.monsters.Voidling;
import infinitespire.util.StringManip;

import java.util.ArrayList;
import java.util.HashMap;

public class SlayQuest extends Quest {
	
	private static HashMap<String, String> monsterMap = new HashMap<String, String>();
	private static HashMap<String, String> eliteMap = new HashMap<String, String>();
	
	public static final String ID = SlayQuest.class.getName();
	private static final Color COLOR = new Color(1f, 0.1f, 0.1f, 1f);
	private static final int MAX_STEPS = 999;
	private static final QuestType TYPE = QuestType.RED;
	private static final int REWARD_AMOUNT = 2;
	
	public String monster;
	
	public SlayQuest() {
		super(ID, COLOR, MAX_STEPS, TYPE, QuestRarity.COMMON);
		this.preInitialize();
	}

	@Override
	public Texture getTexture() {
		Texture retVal = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/slay.png");
		if(this.isElite(this.monster))
			retVal = InfiniteSpire.getTexture("img/infinitespire/ui/questLog/questIcons/elite.png");
		return retVal;
	}

	public void onEnemyKilled(AbstractCreature creature) {
		if(creature.id.equals(this.monster)) {
			this.incrementQuestSteps();
			InfiniteSpire.logger.info(this.monster);
		}
	}
	
	@Override
	public void giveReward() {
		InfiniteSpire.gainVoidShards(REWARD_AMOUNT);
	}
	
	private static String getRandomMonster() {
		String monster = null;
		
		if(AbstractDungeon.miscRng.randomBoolean(0.4f)) {
			int rand = AbstractDungeon.miscRng.random(eliteMap.size() - 1);
			ArrayList<String> list = new ArrayList<String>();
			list.addAll(eliteMap.keySet());

			monster = list.get(rand);
		} else {
			int rand = AbstractDungeon.miscRng.random(monsterMap.size() - 1);
			ArrayList<String> list = new ArrayList<String>();
			list.addAll(monsterMap.keySet());
			monster = list.get(rand);
		}
		
		return monster; 
	}
	
	private boolean isElite(String id) {
		for(String s : eliteMap.keySet())
			if(id != null && id.equals(s)) return true;
		return false;
	}
	
	protected void preInitialize() {
		
		if(monsterMap.size() <= 0 || eliteMap.size() <= 0) {
			monsterMap.clear();
			eliteMap.clear();
			
			//MONSTERS IN EXORDIUM
			monsterMap.put(AcidSlime_L.ID, AcidSlime_L.NAME);
			monsterMap.put(AcidSlime_M.ID, AcidSlime_M.NAME);
			monsterMap.put(AcidSlime_S.ID, AcidSlime_S.NAME);
			monsterMap.put(SpikeSlime_L.ID, SpikeSlime_L.NAME);
			monsterMap.put(SpikeSlime_M.ID, SpikeSlime_M.NAME);
			monsterMap.put(SpikeSlime_S.ID, SpikeSlime_S.NAME);
			monsterMap.put(Cultist.ID, Cultist.NAME);
			monsterMap.put(FungiBeast.ID, FungiBeast.NAME);
			monsterMap.put(JawWorm.ID, JawWorm.NAME);
			monsterMap.put(Looter.ID, Looter.NAME);
			monsterMap.put(LouseNormal.ID, LouseNormal.NAME);
			monsterMap.put(LouseDefensive.ID, LouseDefensive.NAME);
			monsterMap.put(SlaverBlue.ID, SlaverBlue.NAME);
			monsterMap.put(SlaverRed.ID, SlaverRed.NAME);
			monsterMap.put(Voidling.ID, Voidling.NAME);

			//ELITES FROM EXORDIUM
			eliteMap.put(GremlinNob.ID, GremlinNob.NAME);
			eliteMap.put(Lagavulin.ID, Lagavulin.NAME);
			eliteMap.put(Sentry.ID, Sentry.NAME);

			//MONSTERS IN CITY
			monsterMap.put(Healer.ID, Healer.NAME);
			monsterMap.put(Mugger.ID, Mugger.NAME);
			monsterMap.put(Byrd.ID, Byrd.NAME);
			monsterMap.put(ShelledParasite.ID, ShelledParasite.NAME);
			monsterMap.put(SnakePlant.ID, SnakePlant.NAME);
			
			//ELITES FROM CITY
			eliteMap.put(GremlinLeader.ID, GremlinLeader.NAME);
			eliteMap.put(Snecko.ID, Snecko.NAME);
			eliteMap.put(BookOfStabbing.ID, BookOfStabbing.NAME);
			eliteMap.put(Taskmaster.ID, Taskmaster.NAME);
			
			//MONSTERS IN THEBEYOND
			monsterMap.put(Exploder.ID, Exploder.NAME);
			monsterMap.put(Spiker.ID, Spiker.NAME);
			monsterMap.put(Repulsor.ID, Repulsor.NAME);
			monsterMap.put(OrbWalker.ID, OrbWalker.NAME);
			
			//ELITES IN THEBEYOND
			eliteMap.put(GiantHead.ID, GiantHead.NAME);
			eliteMap.put(Nemesis.ID, Nemesis.NAME);
			eliteMap.put(SpireGrowth.ID, SpireGrowth.NAME);
			eliteMap.put(Transient.ID, Transient.NAME);
			eliteMap.put(Reptomancer.ID, Reptomancer.NAME);
		}
	}

	@Override
	public String getTitle() {
		HashMap<String, String> map = monsterMap;
		
		if(isElite(monster)) {
			map = eliteMap;
		}
		
		return questStrings.TEXT[0] + this.maxSteps + " " + (this.maxSteps > 1 ? StringManip.pluralOfString(map.get(monster)) : map.get(monster));
	}
	
	@Override
	public String getRewardString() {
		return voidShardStrings.TEXT[2] + REWARD_AMOUNT + voidShardStrings.TEXT[4];
	}

	@Override
	public Quest createNew() {
		return createNew(new Object[0]);
	}
	// TODO: Allow player to set monster with command
	@Override
	public Quest createNew(Object[] params) {
		this.preInitialize();
		this.monster = getRandomMonster();
		InfiniteSpire.logger.info(monster);
		this.maxSteps = isElite(monster) ? (monster.equals(Sentry.ID) ? 3 : 1) : 3;
		return this;
	}

	@Override
	public Quest getCopy() {
		return new SlayQuest();
	}
}
