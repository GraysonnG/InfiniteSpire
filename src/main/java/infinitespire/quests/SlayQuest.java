package infinitespire.quests;

import java.awt.Color;
import java.util.ArrayList;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.Repulsor;
import com.megacrit.cardcrawl.monsters.beyond.Spiker;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_S;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_S;

import infinitespire.InfiniteSpire;

public class SlayQuest extends Quest{
	
	private static ArrayList<String> monsterList = new ArrayList<String>();
	private static ArrayList<String> eliteList = new ArrayList<String>();
	
	private String monsterName;
	
	public SlayQuest(String uniqueQuestID) {
		super(uniqueQuestID);
		
		this.monsterName = this.id.split("-")[4];
		InfiniteSpire.logger.info(id);
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
		
		builder.append(Quest.createIDWithoutData("SlayQuest", (eliteList.contains(monster) ? (monster.equals(Sentry.ID) ? 3 : 1) : 5), 0, Color.DARK_GRAY));
		
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
			monsterList.add(Byrd.ID);
			monsterList.add(Spiker.ID);
			monsterList.add(Repulsor.ID);
			monsterList.add(AcidSlime_M.ID);
			monsterList.add(AcidSlime_S.ID);
			monsterList.add(SpikeSlime_M.ID);
			monsterList.add(SpikeSlime_S.ID);
			
			eliteList.add(GremlinNob.ID);
			eliteList.add(Lagavulin.ID);
			eliteList.add(Sentry.ID);
		}
	}
}
