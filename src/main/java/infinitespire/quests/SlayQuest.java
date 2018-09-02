package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.Quest;
import infinitespire.util.StringManip;

import java.util.ArrayList;
import java.util.HashMap;

public class SlayQuest extends Quest {
	
	private static HashMap<String, String> monsterMap = new HashMap<String, String>();
	private static HashMap<String, String> eliteMap = new HashMap<String, String>();
	
	public static final String ID = SlayQuest.class.getName().toString();
	private static final Color COLOR = new Color(1f, 0.1f, 0.1f, 1f);
	private static final int MAX_STEPS = 999;
	private static final QuestType TYPE = QuestType.RED;
	
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
		AbstractRelic relic = AbstractDungeon.returnRandomRelic(AbstractDungeon.returnRandomRelicTier());
		relic.instantObtain();
		relic.playLandingSFX();
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
			monsterMap.put(AcidSlime_L.ID, "Large Acid Slime");
			monsterMap.put(AcidSlime_M.ID, "Medium Acid Slime");
			monsterMap.put(AcidSlime_S.ID, "Small Acid Slime");
			monsterMap.put(SpikeSlime_L.ID, "Large Spike Slime");
			monsterMap.put(SpikeSlime_M.ID, "Medium Spike Slime");
			monsterMap.put(SpikeSlime_S.ID, "Small Spike Slime");
			monsterMap.put(Cultist.ID, "Cultist");
			monsterMap.put(FungiBeast.ID, "Fungi Beast");
			monsterMap.put(JawWorm.ID, "Jaw Worm");
			monsterMap.put(Looter.ID, "Looter");
			monsterMap.put(LouseNormal.ID, "Louse Normal");
			monsterMap.put(LouseDefensive.ID, "Louse Defensive");
			monsterMap.put(SlaverBlue.ID, "Slaver Blue");
			monsterMap.put(SlaverRed.ID, "Slaver Red");

			//ELITES FROM EXORDIUM
			eliteMap.put(GremlinNob.ID, "Gremlin Nob");
			eliteMap.put(Lagavulin.ID, "Lagavulin");
			eliteMap.put(Sentry.ID, "Sentry");

			//MONSTERS IN CITY
			monsterMap.put(Healer.ID, "Healer");
			monsterMap.put(Mugger.ID, "Mugger");
			monsterMap.put(Byrd.ID, "Byrd");
			monsterMap.put(ShelledParasite.ID, "Shelled Parasite");
			monsterMap.put(SnakePlant.ID, "Snake Plant");
			
			//ELITES FROM CITY
			eliteMap.put(GremlinLeader.ID, "Gremlin Leader");
			eliteMap.put(Snecko.ID, "Snecko");
			eliteMap.put(BookOfStabbing.ID, "Book Of Stabbing");
			eliteMap.put(Taskmaster.ID, "Taskmaster");
			
			//MONSTERS IN THEBEYOND
			monsterMap.put(Exploder.ID, "Exploder");
			monsterMap.put(Spiker.ID, "Spiker");
			monsterMap.put(Repulsor.ID, "Repulsor");
			monsterMap.put(Reptomancer.ID, "Reptomancer");
			monsterMap.put(OrbWalker.ID, "Orb Walker");
			
			//ELITES IN THEBEYOND
			eliteMap.put(GiantHead.ID, "Giant Head");
			eliteMap.put(Nemesis.ID, "Nemesis");
			eliteMap.put(SpireGrowth.ID, "Spire Growth");
			eliteMap.put(Transient.ID, "Transient");
		}
	}

	@Override
	public String getTitle() {
		HashMap<String, String> map = monsterMap;
		
		if(isElite(monster)) {
			map = eliteMap;
		}
		
		return "Kill " + this.maxSteps + " " + (this.maxSteps > 1 ? StringManip.pluralOfString(map.get(monster)) : map.get(monster));
	}
	
	@Override
	public String getRewardString() {
		return "Recieve a Random Relic";
	}

	public int getCost(String string) {
		int silverGain = 0;
		if(isElite(string)) {
			silverGain = 300;
		}else
		{
			silverGain = 200;
		}
		
		return MathUtils.round(silverGain * AbstractDungeon.merchantRng.random(0.95f, 1.05f));
	}

	@Override
	public Quest createNew() {
		this.preInitialize();
		this.monster = getRandomMonster();
		this.maxSteps = isElite(monster) ? (monster.equals(Sentry.ID) ? 3 : 1) : 3;
		return this;
	}

	@Override
	public Quest getCopy() {
		return new SlayQuest();
	}
}
