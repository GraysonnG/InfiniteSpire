package infinitespire;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import fruitymod.seeker.patches.AbstractCardEnum;
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Relic;
import infinitespire.actions.AddQuestAction;
import infinitespire.cards.Neurotoxin;
import infinitespire.cards.OneForAll;
import infinitespire.cards.Pacifist;
import infinitespire.cards.black.*;
import infinitespire.events.EmptyRestSite;
import infinitespire.events.HoodedArmsDealer;
import infinitespire.events.PrismEvent;
import infinitespire.helpers.CardHelper;
import infinitespire.helpers.QuestHelper;
import infinitespire.interfaces.IAutoQuest;
import infinitespire.interfaces.OnQuestAddedSubscriber;
import infinitespire.interfaces.OnQuestIncrementSubscriber;
import infinitespire.interfaces.OnQuestRemovedSubscriber;
import infinitespire.monsters.LordOfAnnihilation;
import infinitespire.monsters.MassOfShapes;
import infinitespire.patches.CardColorEnumPatch;
import infinitespire.potions.BlackPotion;
import infinitespire.quests.DieQuest;
import infinitespire.quests.QuestLog;
import infinitespire.quests.event.CaptainAbeQuest;
import infinitespire.relics.*;
import infinitespire.relics.crystals.EmpoweringShard;
import infinitespire.relics.crystals.FocusingShard;
import infinitespire.relics.crystals.HealingShard;
import infinitespire.relics.crystals.WardingShard;
import infinitespire.screens.LordBackgroundEffect;
import infinitespire.screens.QuestLogScreen;
import infinitespire.ui.buttons.QuestLogButton;
import infinitespire.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpireInitializer
public class InfiniteSpire implements PostInitializeSubscriber, PostBattleSubscriber, EditRelicsSubscriber,
	EditCardsSubscriber, EditKeywordsSubscriber, EditStringsSubscriber, PreDungeonUpdateSubscriber {
	public static final String VERSION = "0.5.2";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	private static ArrayList<OnQuestRemovedSubscriber> onQuestRemovedSubscribers = new ArrayList<>();
	private static ArrayList<OnQuestIncrementSubscriber> onQuestIncrementSubscribers = new ArrayList<>();
	private static ArrayList<OnQuestAddedSubscriber> onQuestAddedSubscribers = new ArrayList<>();

	public static QuestLog questLog = new QuestLog();
	public static final String GDX_INFINITE_PURPLE_NAME = createID("Purple");
	public static final String GDX_INFINITE_RED_NAME = createID("Red");

	public static boolean isEndless = false;
	public static boolean hasDefeatedGuardian;
	public static boolean shouldLoad = false;
	public static boolean startWithEndlessQuest = true;

	public static boolean isReplayLoaded = false;
	public static boolean isFruityLoaded = false;
	public static boolean isHubrisLoaded = false;

	public static QuestLogScreen questLogScreen = new QuestLogScreen(questLog);
	public static LordBackgroundEffect lordBackgroundEffect = new LordBackgroundEffect();

	public static Color CARD_COLOR = new Color(0f, 0f, 0f, 1f);

	public static String createID(String id) {
		return "infinitespire:" + id;
	}

	public InfiniteSpire() {
		BaseMod.subscribe(this);
		BaseMod.subscribe(new InfiniteSpireInit());
	}

	public static void initialize() {
		logger.info("VERSION:" + VERSION);
		new InfiniteSpire();

		InfiniteSpire.isReplayLoaded = Loader.isModLoaded("ReplayTheSpireMod");
		InfiniteSpire.isFruityLoaded = Loader.isModLoaded("fruitymod-sts");
		InfiniteSpire.isHubrisLoaded = Loader.isModLoaded("hubris");

		logger.info("Found Mod ReplayTheSpire: " + isReplayLoaded);
		logger.info("Found Mod FruityMod: " + isFruityLoaded);
		logger.info("Found Mod Hubris: " + isHubrisLoaded);

		Colors.put(GDX_INFINITE_PURPLE_NAME, Color.valueOf("#3D00D6").cpy());
		Colors.put(GDX_INFINITE_RED_NAME, Color.valueOf("#FF4A4A").cpy());
	}

	@Override
	public void receivePostInitialize() {
		initializeQuestLog();

		BaseMod.addEvent(EmptyRestSite.ID, EmptyRestSite.class, Exordium.ID);
		BaseMod.addEvent(HoodedArmsDealer.ID, HoodedArmsDealer.class);
		BaseMod.addEvent(PrismEvent.ID, PrismEvent.class, Exordium.ID);

		BaseMod.addMonster(LordOfAnnihilation.ID, LordOfAnnihilation::new);
		BaseMod.addMonster(MassOfShapes.ID, MassOfShapes::new);

		BaseMod.addBoss(TheBeyond.ID, MassOfShapes.ID,
			"img/infinitespire/ui/map/massBoss.png",
			"img/infinitespire/ui/map/massBoss-outline.png");

		// this should be removed after im done testing
//		 BaseMod.addBoss(Exordium.ID, LordOfAnnihilation.ID,
//		 "img/infinitespire/ui/map/bossIcon.png",
//		 "img/infinitespire/ui/map/bossIcon-outline.png");


		BaseMod.addPotion(BlackPotion.class, Color.BLACK, new Color(61f / 255f, 0f, 1f, 1f), Color.RED, BlackPotion.ID);
		BaseMod.addTopPanelItem(new QuestLogButton());

		// RegisterBottlerBottle
		if (InfiniteSpire.checkForMod("com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler")) {
			TheBottler.addBottleRelic(BottledSoul.ID);
		}
	}

	public static boolean checkForMod(String classPath) {
		try {
			Class.forName(classPath);
			InfiniteSpire.logger.info("Found mod: " + classPath);
			return true;
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			InfiniteSpire.logger.info("Could not find mod: " + classPath);
			return false;
		}
	}

	@Override
	public void receiveEditStrings() {
		String relicStrings = Gdx.files.internal("local/infinitespire/relics.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);

		String eventStrings = Gdx.files.internal("local/infinitespire/events.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);

		String monsterStrings = Gdx.files.internal("local/infinitespire/monsters.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);

		String potionStrings = Gdx.files.internal("local/infinitespire/potions.json")
			.readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(PotionStrings.class, potionStrings);
	}

	@Override
	public void receiveEditKeywords() {
		String[] golemsMight = { "golem's might", "golem's", "golem", "golem" };
		String[] crit = { "critical", "crit" };
		String[] shattered = { "shredded" };
		String[] mitigation = { "mitigation" };

		BaseMod.addKeyword(golemsMight, "Each turn your attacks deal 10% more damage than the last turn.");
		BaseMod.addKeyword(crit, "The next attack you play will deal 2x damage.");
		BaseMod.addKeyword(shattered,
				"For each card played for the rest of combat, the enemy takes #b10% more damage from #yAttacks.");
		BaseMod.addKeyword(mitigation, "Reduces all damage by a percentage.");
	}

	@Override
	public void receiveEditCards() {
		initializeCards();
	}

	@Override
	public void receiveEditRelics() {
		initializeRelics();
	}

	public static Texture getTexture(final String textureString) {
		return TextureLoader.getTexture(textureString);
	}

	public static void saveData() {
		logger.info("InfiniteSpire | Saving Data...");
		try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");

			BottledSoul.save(config);
			config.setBool("isGuardianDead", hasDefeatedGuardian);
			config.setBool("isEndless", isEndless);
			config.setBool("startWithEndlessQuest", startWithEndlessQuest);
			config.save();
		} catch (IOException e) {
			e.printStackTrace();
		}

		QuestHelper.saveQuestLog();
	}

	public static void clearData() {
		logger.info("InfiniteSpire | Clearing Saved Data...");
		isEndless = false;
		QuestHelper.clearQuestLog();
		BottledSoul.clear();
		saveData();
	}

	public static void loadData() {
		logger.info("InfiniteSpire | Loading Data...");
		try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.load();
			isEndless = config.getBool("isEndless");
			startWithEndlessQuest = config.getBool("startWithEndlessQuest");

			if (AbstractDungeon.player != null)
				BottledSoul.load(config);

		} catch (IOException | NumberFormatException e) {
			logger.error("Failed to load InfiniteSpire data!");
			e.printStackTrace();
			clearData();
		}

		QuestHelper.loadQuestLog();
	}

	private static void initializeRelics() {
		logger.info("InfiniteSpire | Initializing relics...");

		RelicLibrary.add(new GolemsMask());
		RelicLibrary.add(new LycheeNut());
		RelicLibrary.add(new Cupcake());
		RelicLibrary.add(new MagicFlask());
		RelicLibrary.add(new CubicDiamond());
		RelicLibrary.add(new MidasBlood());
		RelicLibrary.add(new BeetleShell());
		RelicLibrary.add(new BlanksBlanky());
		RelicLibrary.add(new LuckyRock());
		RelicLibrary.add(new JokerCard());
		RelicLibrary.add(new Satchel());
		RelicLibrary.add(new BottledSoul()); // This relic is broken
		RelicLibrary.add(new MutagenicDexterity());
		RelicLibrary.add(new DarkRift());

		RelicLibrary.add(new EmpoweringShard());
		RelicLibrary.add(new WardingShard());
		RelicLibrary.add(new FocusingShard());
		RelicLibrary.add(new HealingShard());

		RelicLibrary.addBlue(new Freezer());
		RelicLibrary.addBlue(new SolderingIron());

		RelicLibrary.addRed(new BurningSword());

		Relic.addQuestRelic(new HolyWater());

		if (isReplayLoaded) {
			RelicLibrary.add(new BrokenMirror());
		}
		if (isFruityLoaded) {
			BaseMod.addRelicToCustomPool(new SpectralDust(), AbstractCardEnum.SEEKER_PURPLE);
		}
		if (isHubrisLoaded) {
			// RelicLibrary.add(new ShieldingShard());
		}
	}

	private static void initializeQuestLog() {
		logger.info("InfiniteSpire | Initializing questLog...");

		QuestHelper.init();

		if (isReplayLoaded) {
			QuestHelper.registerQuest(CaptainAbeQuest.class);
		}

		loadData();
	}

	private static void initializeCards() {
		BaseMod.addColor(CardColorEnumPatch.CardColorPatch.INFINITE_BLACK, CARD_COLOR, CARD_COLOR, CARD_COLOR,
				CARD_COLOR, CARD_COLOR, Color.BLACK.cpy(), CARD_COLOR, "img/infinitespire/cards/ui/512/boss-attack.png",
				"img/infinitespire/cards/ui/512/boss-skill.png", "img/infinitespire/cards/ui/512/boss-power.png",
				"img/infinitespire/cards/ui/512/boss-orb.png", "img/infinitespire/cards/ui/1024/boss-attack.png",
				"img/infinitespire/cards/ui/1024/boss-skill.png", "img/infinitespire/cards/ui/1024/boss-power.png",
				"img/infinitespire/cards/ui/1024/boss-orb.png");

		logger.info("InfiniteSpire | Initializing dynamic variables...");
		BaseMod.addDynamicVariable(new Neurotoxin.PoisonVariable());
		logger.info("InfiniteSpire | Initializing cards...");
		CardHelper.addCard(new OneForAll());
		CardHelper.addCard(new Neurotoxin());

		// Black Cards
		CardHelper.addCard(new FinalStrike());
		// CardHelper.addCard(new ThousandBlades());
		CardHelper.addCard(new Gouge());
		CardHelper.addCard(new DeathsTouch());
		CardHelper.addCard(new Collect());
		CardHelper.addCard(new NeuralNetwork());
		CardHelper.addCard(new FutureSight());
		CardHelper.addCard(new Punishment());
		CardHelper.addCard(new UltimateForm());
		CardHelper.addCard(new Execution());
		CardHelper.addCard(new TheBestDefense());
		CardHelper.addCard(new Fortify());
		CardHelper.addCard(new Pacifist());
		//CardHelper.addCard(new UNNAMED_1());
	}

	@SuppressWarnings("unused")

	public static void triggerDieQuests() {
		for (Quest q : questLog) {
			if (q instanceof DieQuest) {
				q.incrementQuestSteps();
			}
		}
	}

	public static void subscribe(ISubscriber subscriber) {
		subscribeIfInstance(onQuestRemovedSubscribers, subscriber, OnQuestRemovedSubscriber.class);
		subscribeIfInstance(onQuestIncrementSubscribers, subscriber, OnQuestIncrementSubscriber.class);
		subscribeIfInstance(onQuestAddedSubscribers, subscriber, OnQuestAddedSubscriber.class);
	}

	@SuppressWarnings("unchecked")
	private static <T> void subscribeIfInstance(ArrayList<T> list, ISubscriber sub, Class<T> clazz) {
		if (clazz.isInstance(sub)) {
			list.add((T) sub);
		}
	}

	public static void publishOnQuestRemoved(Quest quest) {
		for (OnQuestRemovedSubscriber subscriber : onQuestRemovedSubscribers) {
			subscriber.receiveQuestRemoved(quest);
		}
	}

	public static void publishOnQuestIncrement(Quest quest) {
		for (OnQuestIncrementSubscriber subscriber : onQuestIncrementSubscribers) {
			subscriber.receiveQuestIncrement(quest);
		}
	}

	public static void publishOnQuestAdded(Quest quest) {
		for (OnQuestAddedSubscriber subscriber : onQuestAddedSubscribers) {
			subscriber.receiveQuestAdded(quest);
		}
	}

	@Override
	public void receivePostBattle(AbstractRoom room) {
		if (room instanceof MonsterRoomBoss) {
			int amount = 3;

			if (InfiniteSpire.questLog.size() + amount > 21) {
				amount -= (InfiniteSpire.questLog.size() + amount) - 21;
			}
			if (amount > 0) {
				AbstractDungeon.actionManager.addToBottom(new AddQuestAction(QuestHelper.getRandomQuests(amount)));
			}
		}
	}

	@Override
	public void receivePreDungeonUpdate() {
		// This is code to add event quests when their shouldBegin requirements are met
		for (Class<? extends Quest> questClass : QuestHelper.questMap.values()) {
			try {
				// check to see if questclass implements eventquest
				if (IAutoQuest.class.isAssignableFrom(questClass)) {
					Quest quest = questClass.newInstance();
					// check to see if it shouldBegin and it isnt already in the quest log
					if (AbstractDungeon.player != null && ((IAutoQuest) quest).shouldBegin()
							&& !InfiniteSpire.questLog.hasQuest(quest)) {
						AbstractDungeon.actionManager.addToBottom(new AddQuestAction(quest.createNew()));
					}
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}