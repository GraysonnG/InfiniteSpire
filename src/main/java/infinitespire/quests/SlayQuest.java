package infinitespire.quests;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;

import infinitespire.InfiniteSpire;
import infinitespire.lang.MalformedQuestException;
import infinitespire.util.StringManip;

public class SlayQuest extends Quest{
	
	private static HashMap<String, String> monsterMap = new HashMap<String, String>();
	private static HashMap<String, String> eliteMap = new HashMap<String, String>();
	
	public String monster;
	
	public SlayQuest(String uniqueQuestID) throws MalformedQuestException {
		super(uniqueQuestID, QuestType.RED);
		
		this.monster = this.id.split("-")[4];
	}
	
	public SlayQuest() throws MalformedQuestException {
		this(null);
	}

	public void onEnemyKilled(AbstractCreature creature) {
		
		if(creature.id.equals(this.monster)) {
			this.incrementQuestSteps();
			InfiniteSpire.logger.info(this.getID());
		}
		
	}

	@Override
	protected String generateID() {
		StringBuilder builder = new StringBuilder();
		String monster = getRandomMonster();
		
		builder.append(Quest.createIDWithoutData(
				SlayQuest.class.getName(), 
				(isElite(monster) ? (monster.equals(Sentry.ID) ? 3 : 1) : 3), 0, Color.red));
		
		builder.append("-" + monster);
		builder.append("-" + getCost(monster));
		
		return builder.toString();
	}
	@Override
	public void giveReward() {
		CardCrawlGame.sound.play("GOLD_GAIN");
		InfiniteSpire.points += cost;
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
	
	@Override
	protected void preInitialize() {
		//if(monsterMap.size() <= 0 || eliteMap.size() <= 0);
		
		
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
			eliteMap.put(GremlinLeader.ID, "Gemlin Leader");
			eliteMap.put(Snecko.ID, "Snecko");
			eliteMap.put(BookOfStabbing.ID, "Book Of Stabbing");
			eliteMap.put(Taskmaster.ID, "Taskmaster");
			
			//MONSTERS IN THEBEYOND
			monsterMap.put(Exploder.ID, "Exploder");
			monsterMap.put(Spiker.ID, "Spiker");
			monsterMap.put(Repulsor.ID, "Repulsor");
			monsterMap.put(SnakeMage.ID, "idek");
			monsterMap.put(OrbWalker.ID, "Orb Walker");
			
			//ELITES IN THEBEYOND
			eliteMap.put(GiantHead.ID, "Giant Head");
			eliteMap.put(Nemesis.ID, "Nemisis");
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
		
		return "Kill " + this.maxQuestSteps + " " + (this.maxQuestSteps > 1 ? StringManip.pluralOfString(map.get(monster)) : map.get(monster));
	}
	
	@Override
	public String getRewardString() {
		return "" + this.cost + "s";
	}

	@Override
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
}
