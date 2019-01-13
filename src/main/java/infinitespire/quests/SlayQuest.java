package infinitespire.quests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.Settings.GameLanguage;
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

	public static final String ID = SlayQuest.class.getName();
	private static final Color COLOR = new Color(1f, 0.1f, 0.1f, 1f);
	private static final int MAX_STEPS = 999;
	private static final QuestType TYPE = QuestType.RED;

	public String monster;
	public String[] monstersStrings = new String[35];
	public String kill;
	public String reward;

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
		AbstractRelic relic = AbstractDungeon.returnRandomRelicEnd(AbstractDungeon.returnRandomRelicTier());
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

		//Language
		//FR
		if (Settings.language == Settings.GameLanguage.FRA){
			this.reward = "Recevez une relique al\u00e9atoire";
			this.kill ="Tuez ";
			this.monstersStrings[0] = "Gros Slime Acide" ;
			this.monstersStrings[1]  = "Slime Acide Moyen";
			this.monstersStrings[2] = "Petit Slime Acide";
			this.monstersStrings[3] = "Gros Slime Epineux ";
			this.monstersStrings[4] = "Slime Épineux Moyen";
			this.monstersStrings[5] = "Petit Slime Epineux ";
			this.monstersStrings[6] = "Adepte";
			this.monstersStrings[7] = "Champi-b\u00eates";
			this.monstersStrings[8] = "M\u00e2chouilleur";
			this.monstersStrings[9] = "Brigand";
			this.monstersStrings[10] = "Pou Normal";
			this.monstersStrings[11] = "Pou Défensif";
			this.monstersStrings[12] = "Esclavagiste Bleu";
			this.monstersStrings[13] = "Esclavagiste Rouge";

			this.monstersStrings[14] = "Diablotin Nob";
			this.monstersStrings[15] = "Lagavulin";
			this.monstersStrings[16] = "Sentinelle";

			this.monstersStrings[17] = "Soigneur";
			this.monstersStrings[18] = "Pickpocket";
			this.monstersStrings[19] = "Oizo";
			this.monstersStrings[20] = "Parasite en carapace";
			this.monstersStrings[21] = "Serplante";

			this.monstersStrings[22] = "Diablotin en chef";
			this.monstersStrings[23] = "Geckobra";
			this.monstersStrings[24] = "Manuel d'assassinat";
			this.monstersStrings[25] = "Tyran";

			this.monstersStrings[26] = "Explosif";
			this.monstersStrings[27] = "Epineux";
			this.monstersStrings[28] = "R\u00e9pugnant";
			this.monstersStrings[29] = "Orbiste";

			this.monstersStrings[30] = "T\u00eate G\u00e9ante";
			this.monstersStrings[31] = "N\u00e9m\u00e9sis";
			this.monstersStrings[32] = "Excroissance de la tour";
			this.monstersStrings[33] = "Eph\u00e9m\u00e9re";
			this.monstersStrings[34] = "Reptomancien";
		} else {
			this.reward = "Receive a Random Relic";
			this.kill ="Kill ";
			this.monstersStrings[0] = "Large Acid Slime" ;
			this.monstersStrings[1] = "Medium Acid Slime";
			this.monstersStrings[2] = "Small Acid Slime";
			this.monstersStrings[3] = "Large Spike Slime";
			this.monstersStrings[4] = "Medium Spike Slime";
			this.monstersStrings[5] = "Small Spike Slime";
			this.monstersStrings[6] = "Cultist";
			this.monstersStrings[7] = "Fungi Beast";
			this.monstersStrings[8] = "Jaw Worm";
			this.monstersStrings[9] = "Looter";
			this.monstersStrings[10] = "Louse Normal";
			this.monstersStrings[11] = "Louse Defensive";
			this.monstersStrings[12] = "Slaver Blue";
			this.monstersStrings[13] = "Slaver Red";
			this.monstersStrings[14] = "Gremlin Nob";
			this.monstersStrings[15] = "Lagavulin";
			this.monstersStrings[16] = "Sentry";
			this.monstersStrings[17] = "Healer";
			this.monstersStrings[18] = "mugger";
			this.monstersStrings[19] = "Byrd";
			this.monstersStrings[20] = "Shelled Parasite";
			this.monstersStrings[21] = "Snake Plant";
			this.monstersStrings[22] = "Gremlin Leader";
			this.monstersStrings[23] = "Snecko";
			this.monstersStrings[24] = "Book of Stabbing";
			this.monstersStrings[25] = "Taskmaster";
			this.monstersStrings[26] = "Exploder";
			this.monstersStrings[27] = "Spiker";
			this.monstersStrings[28] = "Repulsor";
			this.monstersStrings[29] = "Orb Walker";
			this.monstersStrings[30] = "Giant Head";
			this.monstersStrings[31] = "Nemesis";
			this.monstersStrings[32] = "Spire Growth";
			this.monstersStrings[33] = "Transient";
			this.monstersStrings[34] = "Reptomancer";
		}

		if(monsterMap.size() <= 0 || eliteMap.size() <= 0) {
			monsterMap.clear();
			eliteMap.clear();



			//MONSTERS IN EXORDIUM
			monsterMap.put(AcidSlime_L.ID, this.monstersStrings[0]);
			monsterMap.put(AcidSlime_M.ID, this.monstersStrings[1]);
		monsterMap.put(AcidSlime_S.ID, this.monstersStrings[2]);
			monsterMap.put(SpikeSlime_L.ID, this.monstersStrings[3]);
			monsterMap.put(SpikeSlime_M.ID, this.monstersStrings[4]);
			monsterMap.put(SpikeSlime_S.ID, this.monstersStrings[5]);
			monsterMap.put(Cultist.ID, this.monstersStrings[6]);
			monsterMap.put(FungiBeast.ID, this.monstersStrings[7]);
			monsterMap.put(JawWorm.ID, this.monstersStrings[8]);
			monsterMap.put(Looter.ID, this.monstersStrings[9]);
			monsterMap.put(LouseNormal.ID, this.monstersStrings[10]);
			monsterMap.put(LouseDefensive.ID, this.monstersStrings[11]);
			monsterMap.put(SlaverBlue.ID, this.monstersStrings[12]);
			monsterMap.put(SlaverRed.ID, this.monstersStrings[13]);

			//ELITES FROM EXORDIUM
			eliteMap.put(GremlinNob.ID, this.monstersStrings[14]);
			eliteMap.put(Lagavulin.ID, this.monstersStrings[15]);
			eliteMap.put(Sentry.ID, this.monstersStrings[16]);

			//MONSTERS IN CITY
			monsterMap.put(Healer.ID, this.monstersStrings[17]);
			monsterMap.put(Mugger.ID, this.monstersStrings[18]);
			monsterMap.put(Byrd.ID, this.monstersStrings[19]);
			monsterMap.put(ShelledParasite.ID, this.monstersStrings[20]);
			monsterMap.put(SnakePlant.ID, this.monstersStrings[21]);

			//ELITES FROM CITY
			eliteMap.put(GremlinLeader.ID, this.monstersStrings[22]);
			eliteMap.put(Snecko.ID, this.monstersStrings[23]);
			eliteMap.put(BookOfStabbing.ID, this.monstersStrings[24]);
			eliteMap.put(Taskmaster.ID, this.monstersStrings[25]);

			//MONSTERS IN THEBEYOND
			monsterMap.put(Exploder.ID, this.monstersStrings[26]);
			monsterMap.put(Spiker.ID, this.monstersStrings[27]);
			monsterMap.put(Repulsor.ID, this.monstersStrings[28]);
			monsterMap.put(OrbWalker.ID, this.monstersStrings[29]);

			//ELITES IN THEBEYOND
			eliteMap.put(GiantHead.ID, this.monstersStrings[30]);
			eliteMap.put(Nemesis.ID, this.monstersStrings[31]);
			eliteMap.put(SpireGrowth.ID, this.monstersStrings[32]);
			eliteMap.put(Transient.ID, this.monstersStrings[33]);
			eliteMap.put(Reptomancer.ID, this.monstersStrings[34]);
		}
	}

	@Override
	public String getTitle() {
		HashMap<String, String> map = monsterMap;

		if(isElite(monster)) {
			map = eliteMap;
		}
	if (Settings.language == Settings.GameLanguage.FRA){
			return this.kill + this.maxSteps + " " + map.get(monster);
	} else {
			return this.kill + this.maxSteps + " " + (this.maxSteps > 1 ? StringManip.pluralOfString(map.get(monster)) : map.get(monster));
	}

	}

	@Override
	public String getRewardString() {

		return this.reward;
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
