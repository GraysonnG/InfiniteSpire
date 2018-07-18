package infinitespire.quests;

import java.awt.Color;
import java.util.ArrayList;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;

public class SlayQuest extends Quest{
	
	private static ArrayList<String> monsterList = new ArrayList<String>();
	private static ArrayList<String> eliteList = new ArrayList<String>();
	
	private String monsterName;
	
	public SlayQuest(String uniqueQuestID) {
		super(uniqueQuestID);
		
		this.monsterName = this.id.split("-")[4];
	}
	
	public SlayQuest() {
		this(null);
	}

	public void onEnemyKilled(AbstractCreature creature) {
		if(creature.id.equals(this.monsterName)) this.incrementQuestSteps();
		
	}

	@Override
	protected String generateID() {
		StringBuilder builder = new StringBuilder();
		String monster = getRandomMonster();
		
		builder.append(Quest.createIDWithoutData(SlayQuest.class.getName(), (eliteList.contains(monster) ? (monster.equals(Sentry.ID) ? 3 : 1) : 3), 0, Color.DARK_GRAY));
		
		builder.append("-" + monster);
		
		return builder.toString();
	}
	@Override
	protected void giveReward() {
		//give some silver
	}
	
	private static String getRandomMonster() {
		String monster = null;
		
		if(AbstractDungeon.miscRng.randomBoolean(0.1f)) {
			int rand = AbstractDungeon.miscRng.random(eliteList.size() - 1);
			monster = eliteList.get(rand);
		} else {
			int rand = AbstractDungeon.miscRng.random(monsterList.size() - 1);
			monster = monsterList.get(rand);
		}
		
		return monster; 
	}

	@Override
	protected void preInitialize() {
		if(monsterList.size() <= 0 || eliteList.size() <= 0) {
			monsterList = new ArrayList<String>();
			eliteList = new ArrayList<String>();
			
			//MONSTERS IN EXORDIUM
			monsterList.add(AcidSlime_L.ID);
			monsterList.add(AcidSlime_M.ID);
			monsterList.add(AcidSlime_S.ID);
			monsterList.add(SpikeSlime_L.ID);
			monsterList.add(SpikeSlime_M.ID);
			monsterList.add(SpikeSlime_S.ID);
			monsterList.add(Cultist.ID);
			monsterList.add(FungiBeast.ID);
			monsterList.add(JawWorm.ID);
			monsterList.add(Looter.ID);
			monsterList.add(LouseNormal.ID);
			monsterList.add(LouseDefensive.ID);
			monsterList.add(SlaverBlue.ID);
			monsterList.add(SlaverRed.ID);

			//ELITES FROM EXORDIUM
			eliteList.add(GremlinNob.ID);
			eliteList.add(Lagavulin.ID);
			eliteList.add(Sentry.ID);

			//MONSTERS IN CITY
			monsterList.add(Healer.ID);
			monsterList.add(Mugger.ID);
			monsterList.add(Byrd.ID);
			monsterList.add(ShelledParasite.ID);
			monsterList.add(SnakePlant.ID);
			
			//ELITES FROM CITY
			eliteList.add(GremlinLeader.ID);
			eliteList.add(Snecko.ID);
			eliteList.add(BookOfStabbing.ID);
			eliteList.add(Taskmaster.ID);
			
			//MONSTERS IN THEBEYOND
			monsterList.add(Exploder.ID);
			monsterList.add(Spiker.ID);
			monsterList.add(Repulsor.ID);
			monsterList.add(SnakeMage.ID);
			monsterList.add(OrbWalker.ID);
			
			//ELITES IN THEBEYOND
			eliteList.add(GiantHead.ID);
			eliteList.add(Nemesis.ID);
			eliteList.add(SpireGrowth.ID);
			eliteList.add(Transient.ID);
		}
	}
}
