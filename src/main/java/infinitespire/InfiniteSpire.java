package infinitespire;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.devcommands.ConsoleCommand;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import infinitespire.abstracts.Card;
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Relic;
import infinitespire.actions.AddQuestAction;
import infinitespire.cards.Neurotoxin;
import infinitespire.cards.black.SealOfDarkness;
import infinitespire.commands.QuestCommand;
import infinitespire.crossover.BardCrossover;
import infinitespire.events.EmptyRestSite;
import infinitespire.events.HoodedArmsDealer;
import infinitespire.events.PrismEvent;
import infinitespire.events.VoidlingNest;
import infinitespire.helpers.CardHelper;
import infinitespire.helpers.QuestHelper;
import infinitespire.interfaces.IAutoQuest;
import infinitespire.interfaces.OnQuestAddedSubscriber;
import infinitespire.interfaces.OnQuestIncrementSubscriber;
import infinitespire.interfaces.OnQuestRemovedSubscriber;
import infinitespire.monsters.*;
import infinitespire.patches.CardColorEnumPatch;
import infinitespire.patches.RewardItemTypeEnumPatch;
import infinitespire.patches.SneckoEssencePatch;
import infinitespire.potions.BlackPotion;
import infinitespire.quests.DieQuest;
import infinitespire.quests.QuestLog;
import infinitespire.quests.endless.EndlessQuestPart1;
import infinitespire.quests.event.CaptainAbeQuest;
import infinitespire.relics.*;
import infinitespire.relics.crystals.EmpoweringShard;
import infinitespire.relics.crystals.FocusingShard;
import infinitespire.relics.crystals.ShieldingShard;
import infinitespire.relics.crystals.WardingShard;
import infinitespire.rewards.BlackCardRewardItem;
import infinitespire.rewards.InterestReward;
import infinitespire.rewards.QuestReward;
import infinitespire.rewards.VoidShardReward;
import infinitespire.screens.LordBackgroundEffect;
import infinitespire.screens.QuestLogScreen;
import infinitespire.ui.buttons.QuestLogButton;
import infinitespire.ui.buttons.VoidShardDisplay;
import infinitespire.util.TextureLoader;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.ClassFinder;
import org.clapper.util.classutil.ClassInfo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpireInitializer
public class InfiniteSpire implements PostInitializeSubscriber, PostBattleSubscriber, EditRelicsSubscriber,
	EditCardsSubscriber, EditKeywordsSubscriber, EditStringsSubscriber, PreDungeonUpdateSubscriber, PostUpdateSubscriber,
	PreStartGameSubscriber {
	public static final String VERSION = "0.21.1";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	private static ArrayList<OnQuestRemovedSubscriber> onQuestRemovedSubscribers = new ArrayList<>();
	private static ArrayList<OnQuestIncrementSubscriber> onQuestIncrementSubscribers = new ArrayList<>();
	private static ArrayList<OnQuestAddedSubscriber> onQuestAddedSubscribers = new ArrayList<>();

	public static QuestLog questLog = new QuestLog();
	public static final String GDX_INFINITE_PURPLE_NAME = createID("Purple");
	public static final String GDX_INFINITE_RED_NAME = createID("Red");

	// Void Shard and Avhari Constants
	public static final float VOID_SHARD_CHANCE = 0.10f;
	public static final int AVHARI_CARD_PRICE = 8;
	public static final int AVHARI_RELIC_PRICE = 15;
	public static final int AVHARI_RANDOM_RELIC_PRICE = 3;
	public static final int AVHARI_REMOVE_CARD_PRICE = 2;

	public static boolean isEndless = false;
	public static boolean hasDefeatedGuardian;
	public static boolean shouldLoad = false;
	public static boolean startWithEndlessQuest = true;
	public static boolean shouldDoParticles = true;
	public static int voidShardCount = 0;
	public static boolean shouldSpawnLords = true;

	public static boolean hasDefeatedLordOfAnnihilation;
	public static boolean hasDefeatedLordOfDawn;
	public static boolean hasDefeatedLordOfFortification;

	public static boolean isReplayLoaded = false;
	public static boolean isFruityLoaded = false;
	public static boolean isHubrisLoaded = false;
	public static boolean isBardLoaded = false;

	public static QuestLogScreen questLogScreen = new QuestLogScreen(questLog);
	public static LordBackgroundEffect lordBackgroundEffect = new LordBackgroundEffect();
	public static VoidShardDisplay voidShardDisplay;

	public static Color CARD_COLOR = new Color(0f, 0f, 0f, 1f);

	public static String createID(String id) {
		return "infinitespire:" + id;
	}
	public static String createPath(String restOfPath) {
		return "img/infinitespire/" + restOfPath;
	}

	public InfiniteSpire() {
		BaseMod.subscribe(this);
		BaseMod.subscribe(new InfiniteSpireInit());

		logger.info("Initializing Card Color: Infinite Black");
		BaseMod.addColor(CardColorEnumPatch.CardColorPatch.INFINITE_BLACK, CARD_COLOR, CARD_COLOR, CARD_COLOR,
			CARD_COLOR, CARD_COLOR, Color.BLACK.cpy(), CARD_COLOR,
			createPath("cards/ui/512/boss-attack.png"), createPath("cards/ui/512/boss-skill.png"),
			createPath("cards/ui/512/boss-power.png"),	createPath("cards/ui/512/boss-orb.png"),
			createPath("cards/ui/1024/boss-attack.png"), createPath("cards/ui/1024/boss-skill.png"),
			createPath("cards/ui/1024/boss-power.png"), createPath("cards/ui/1024/boss-orb.png"));
	}

	public static void initialize() {
		logger.info("VERSION:" + VERSION);
		new InfiniteSpire();

		InfiniteSpire.isReplayLoaded = Loader.isModLoaded("ReplayTheSpireMod");
		InfiniteSpire.isFruityLoaded = Loader.isModLoaded("fruitymod-sts");
		InfiniteSpire.isHubrisLoaded = Loader.isModLoaded("hubris");
		InfiniteSpire.isBardLoaded = Loader.isModLoaded("bard");

		logger.info("Found Mod ReplayTheSpire: " + isReplayLoaded);
		logger.info("Found Mod FruityMod: " + isFruityLoaded);
		logger.info("Found Mod Hubris: " + isHubrisLoaded);
		logger.info("Found Mod Bard: " + isBardLoaded);

		Colors.put(GDX_INFINITE_PURPLE_NAME, Color.valueOf("#3D00D6").cpy());
		Colors.put(GDX_INFINITE_RED_NAME, Color.valueOf("#FF4A4A").cpy());

	}

	@Override
	public void receivePostInitialize() {
		initializeQuestLog();

		BaseMod.registerCustomReward(
			RewardItemTypeEnumPatch.BLACK_CARD,
			(rewardSave) -> { //on load
				return new BlackCardRewardItem();
			}, (customReward) -> { //on save
				return new RewardSave(customReward.type.toString(), null);
			});

		BaseMod.registerCustomReward(
			RewardItemTypeEnumPatch.QUEST,
			(rewardSave) -> { //on load
				return new QuestReward(rewardSave.amount);
			}, (customReward) -> { // on save
				return new RewardSave(customReward.type.toString(), null, ((QuestReward)customReward).amount, 0);
			});

		BaseMod.registerCustomReward(
			RewardItemTypeEnumPatch.INTEREST,
			(rewardSave) -> new InterestReward(rewardSave.amount),
			(customReward) -> new RewardSave(customReward.type.toString(), null, ((InterestReward)customReward).amount, 0)
		);

		BaseMod.registerCustomReward(
			RewardItemTypeEnumPatch.VOID_SHARD,
			(rewardSave) -> new VoidShardReward(rewardSave.amount),
			(customReward) -> new RewardSave(customReward.type.toString(), null, ((VoidShardReward) customReward).amountOfShards, 0)
		);

		BaseMod.addEvent(EmptyRestSite.ID, EmptyRestSite.class, Exordium.ID);
		BaseMod.addEvent(HoodedArmsDealer.ID, HoodedArmsDealer.class);
		BaseMod.addEvent(PrismEvent.ID, PrismEvent.class, Exordium.ID);
		BaseMod.addEvent(VoidlingNest.ID, VoidlingNest.class, TheBeyond.ID);

		BaseMod.addMonster(LordOfAnnihilation.ID, LordOfAnnihilation::new);
		BaseMod.addMonster(LordOfFortification.ID, LordOfFortification::new);

		BaseMod.addMonster(Nightmare.ID, () -> new Nightmare(false));
		BaseMod.addMonster(Nightmare.ID + "_Alpha", () -> new Nightmare(true));
		BaseMod.addMonster(Voidling.ID, () -> new Voidling());
		BaseMod.addMonster(Voidling.SPECIAL_ENCOUNTER_ID, () -> new MonsterGroup(
			new AbstractMonster[]{
				new Voidling(-275f),
				new Voidling(-50),
				new Voidling(225f)
			}));

		BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(Voidling.ID, 0.5f));

		BaseMod.addMonster(MassOfShapes.ID, MassOfShapes::new);

		BaseMod.addBoss(TheBeyond.ID, MassOfShapes.ID,
			createPath("ui/map/massBoss.png"),
			createPath("ui/map/massBoss-outline.png"));

		// this should be removed after im done testing
//		 BaseMod.addBoss(Exordium.ID, LordOfFortification.ID,
//		 "img/infinitespire/ui/map/bossIcon.png",
//		 "img/infinitespire/ui/map/bossIcon-outline.png");

		logger.info("Adding Potions");
		BaseMod.addPotion(BlackPotion.class, Color.BLACK, new Color(61f / 255f, 0f, 1f, 1f), Color.RED, BlackPotion.ID);
		// BaseMod.addPotion(RainbowPotion.class, Color.WHITE.cpy(), Color.WHITE.cpy(), Color.WHITE.cpy(), RainbowPotion.ID);

		logger.info("Initializing Top Panel Buttons");
		voidShardDisplay = new VoidShardDisplay();

		BaseMod.addTopPanelItem(voidShardDisplay);
		BaseMod.addTopPanelItem(new QuestLogButton());

		// RegisterBottlerBottle
		if (InfiniteSpire.checkForMod("com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler")) {
			TheBottler.addBottleRelic(BottledSoul.ID);
		}

		Map<String, Class> root = (Map<String, Class>) ReflectionHacks.getPrivateStatic(ConsoleCommand.class, "root");
		root.put(QuestCommand.COMMAND_VALUE, QuestCommand.class);
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

	private String makeLocalizationPath(Settings.GameLanguage language, String fileName) {
		String retVal = "local/infinitespire/";

		switch (language) {
			default:
				retVal += "eng/";
				break;
		}

		return retVal + fileName + ".json";
	}

	private void loadLocalizationFiles(Settings.GameLanguage language) {

		BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath(language, "cards"));
		BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizationPath(language, "relics"));
		BaseMod.loadCustomStringsFile(BlightStrings.class, makeLocalizationPath(language, "blights"));
		BaseMod.loadCustomStringsFile(EventStrings.class, makeLocalizationPath(language, "events"));
		BaseMod.loadCustomStringsFile(MonsterStrings.class, makeLocalizationPath(language, "monsters"));
		BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocalizationPath(language, "potions"));
		BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath(language, "powers"));
		BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizationPath(language, "ui"));
	}


	@Override
	public void receiveEditStrings() {
		loadLocalizationFiles(Settings.GameLanguage.ENG);
		if(Settings.language != Settings.GameLanguage.ENG) {
			loadLocalizationFiles(Settings.language);
		}
	}

	@Override
	public void receiveEditKeywords() {
		String[] golemsMight = { "golem's might", "golem's", "golem"};
		String[] crit = { "critical", "crit" };
		String[] shattered = { "shredded" };
		String[] mitigation = { "mitigation" };
		String[] deenergized = {"de-energized"};

		BaseMod.addKeyword(golemsMight, "Each turn your attacks deal 5% more damage than the last turn.");
		BaseMod.addKeyword(crit, "The next attack you play will deal 2x damage.");
		BaseMod.addKeyword(shattered,
				"For each card played for the rest of combat, the enemy takes #b10% more damage from #yAttacks.");
		BaseMod.addKeyword(mitigation, "Reduces all damage by a percentage.");
		BaseMod.addKeyword(deenergized, "At the start of each turn, lose #b1 energy.");
	}

	@Override
	public void receiveEditCards() {
		initializeCards();
	}

	@Override
	public void receiveEditRelics() {
		initializeRelics();
	}

	@Deprecated
	public static Texture getTexture(final String textureString) {
		return TextureLoader.getTexture(textureString);
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
		RelicLibrary.add(new Eraser());
		RelicLibrary.add(new Chaos()); // This relic may have bugs lmao sorry
		RelicLibrary.add(new CursedDice());
		RelicLibrary.add(new BottledMercury());
		RelicLibrary.add(new EvilPickle());
		RelicLibrary.add(new BlackEgg());
		RelicLibrary.add(new CheckeredPen());
		RelicLibrary.add(new PuzzleCube());

		RelicLibrary.add(new EmpoweringShard());
		RelicLibrary.add(new WardingShard());
		RelicLibrary.add(new FocusingShard());
		RelicLibrary.add(new ShieldingShard());

		RelicLibrary.addBlue(new Freezer());
		RelicLibrary.addBlue(new SolderingIron());

		RelicLibrary.addRed(new BurningSword());

		Relic.addQuestRelic(new HolyWater());

		if (isReplayLoaded) {
			RelicLibrary.add(new BrokenMirror());
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
		logger.info("InfiniteSpire | Initializing dynamic variables...");
		BaseMod.addDynamicVariable(new Neurotoxin.PoisonVariable());
		logger.info("InfiniteSpire | Initializing cards...");

		try {
			autoAddCards();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		BaseMod.addCard(new SealOfDarkness());

		if(isBardLoaded) {
			BardCrossover.loadBardBlackCards();
		}
	}

	private static void autoAddCards() throws URISyntaxException {
		ClassFinder finder = new ClassFinder();
		URL url = InfiniteSpire.class.getProtectionDomain().getCodeSource().getLocation();
		finder.add(new File(url.toURI()));

		List<ClassInfo> foundClasses = new ArrayList<>();
		finder.findClasses(foundClasses);
		foundClasses.stream()
			.filter(clazz -> clazz.getClassName().startsWith("infinitespire.cards"))
			.distinct()
			.forEach((clazz) -> {
				try {
					CtClass ctClass = Loader.getClassPool().get(clazz.getClassName());
					if(ctClass.subclassOf(Loader.getClassPool().get(Card.class.getName()))) {
						AbstractCard card = (AbstractCard) Loader.getClassPool().getClassLoader().loadClass(ctClass.getName()).newInstance();
						CardHelper.addCard(card);
					}
				} catch (NotFoundException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			});
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
		logger.info("InfiniteSpire : publishOnQuestRemoved subscribers...");
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
				//AbstractDungeon.actionManager.addToBottom(new AddQuestAction(QuestHelper.getRandomQuests(amount)));
				room.rewards.add(new QuestReward(amount));
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

	@Override
	public void receivePostUpdate() {
		AbstractPlayer player = AbstractDungeon.player;
		if(CardCrawlGame.isInARun() && player.hasRelic(SneckoEssence.ID)){
			if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
				for(int i = AbstractDungeon.actionManager.actions.size() - 1; i >= 0; i--) {
					AbstractGameAction action = AbstractDungeon.actionManager.actions.get(i);

					if(action.source != null && action.source.isPlayer  && !action.target.isPlayer && action.actionType == AbstractGameAction.ActionType.DAMAGE && !SneckoEssencePatch.Field.isSnecked.get(action)) {
						action.target = AbstractDungeon.getRandomMonster();
						SneckoEssencePatch.Field.isSnecked.set(action, true);
					}



					/*if(action instanceof DamageAction && action.source.isPlayer) {
						DamageAction damageAction = (DamageAction) action;
						DamageInfo info = new DamageInfo(player, damageAction.amount, damageAction.damageType);
						DamageRandomEnemyAction newAction = new DamageRandomEnemyAction(info, damageAction.attackEffect);
						AbstractDungeon.actionManager.actions.set(i, newAction);
					}*/
				}
			}
		}
	}

	public static void addInitialQuests() {
		InfiniteSpire.logger.info("Clearing and Adding New Quests");
		InfiniteSpire.questLog.clear();
		if(InfiniteSpire.startWithEndlessQuest)	InfiniteSpire.questLog.add(new EndlessQuestPart1().createNew());
		InfiniteSpire.questLog.addAll(QuestHelper.getRandomQuests(9));
		InfiniteSpire.questLog.markAllQuestsAsSeen();
		QuestHelper.saveQuestLog();

	}

	@Override
	public void receivePreStartGame() {
		if(isBardLoaded) BardCrossover.removeBardBlackCards();
	}

	public static void gainVoidShards(int amount) {
		voidShardCount += amount;
		QuestHelper.playVoidShardCollectSound();
		voidShardDisplay.flash();
	}

	public static void saveData() {
		logger.info("InfiniteSpire | Saving Data...");
		try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");

			BottledSoul.save(config);
			Nightmare.save(config);
			config.setBool("isGuardianDead", hasDefeatedGuardian);
			config.setBool("isLordOfAnnihilationDead", hasDefeatedLordOfAnnihilation);
			config.setBool("isLordOfDawnDead", hasDefeatedLordOfDawn);
			config.setBool("isLordOfFortificationDead", hasDefeatedLordOfFortification);
			config.setBool("isEndless", isEndless);
			config.setBool("startWithEndless", startWithEndlessQuest);
			config.setBool("blackCardParticles", shouldDoParticles);
			config.setInt("voidShardCount", voidShardCount);
			config.setBool("shouldSpawnLords", shouldSpawnLords);
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
		Nightmare.clear();
		saveData();
	}

	public static void loadData() {
		logger.info("InfiniteSpire | Loading Data...");
		try {
			SpireConfig config = new SpireConfig("InfiniteSpire", "infiniteSpireConfig");
			config.load();
			isEndless = config.getBool("isEndless");
			if(config.has("voidShardCount")) {
				voidShardCount = config.getInt("voidShardCount");
			} else {
				voidShardCount = 0;
			}
			if(config.has("startWithEndless")) {
				startWithEndlessQuest = config.getBool("startWithEndless");
			} else {
				startWithEndlessQuest = true;
			}
			if(config.has("cardParticles")) {
				shouldDoParticles = config.getBool("blackCardParticles");
			} else {
				shouldDoParticles = true;
			}
			if(config.has("shouldSpawnLords")) {
				shouldSpawnLords = config.getBool("shouldSpawnLords");
			} else {
				shouldSpawnLords = true;
			}
			if (CardCrawlGame.isInARun()) {
				BottledSoul.load(config);
			}

			Nightmare.load(config);

		} catch (IOException | NumberFormatException e) {
			logger.error("Failed to load InfiniteSpire data!");
			e.printStackTrace();
			clearData();
		}

		QuestHelper.loadQuestLog();
	}

	public static class Textures {
		public static Texture getCardTexture(String texture) {
			return TextureLoader.getTexture(createPath("cards/") + texture);
		}

		public static Texture getEventTexture(String texture) {
			return TextureLoader.getTexture(createPath("events/") + texture);
		}

		public static Texture getMonsterTexture(String texture) {
			return TextureLoader.getTexture(createPath("monsters/") + texture);
		}

		public static Texture getPowerTexture(String texture) {
			return TextureLoader.getTexture(createPath("powers/") + texture);
		}

		public static Texture getRelicTexture(String texture) {
			return TextureLoader.getTexture(createPath("relics/") + texture);
		}

		public static Texture getScreenTexture(String texture) {
			return TextureLoader.getTexture(createPath("screen/") + texture);
		}

		public static Texture getUITexture(String texture) {
			return TextureLoader.getTexture(createPath("ui/") + texture);
		}

		public static Texture getVFXTexture(String texture) {
			return TextureLoader.getTexture(createPath("vfx/") + texture);
		}
	}
}