package infinitespire;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import fruitymod.FruityMod;
import fruitymod.patches.AbstractCardEnum;
import infinitespire.abstracts.Quest;
import infinitespire.abstracts.Relic;
import infinitespire.actions.AddQuestAction;
import infinitespire.cards.Neurotoxin;
import infinitespire.cards.OneForAll;
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
import infinitespire.patches.CardColorEnumPatch;
import infinitespire.quests.DieQuest;
import infinitespire.quests.QuestLog;
import infinitespire.quests.event.CaptainAbeQuest;
import infinitespire.relics.*;
import infinitespire.screens.QuestLogScreen;
import infinitespire.util.TextureLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import replayTheSpire.ReplayTheSpireMod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@SpireInitializer
public class InfiniteSpire implements PostInitializeSubscriber, PostBattleSubscriber,
EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber, EditStringsSubscriber, PreDungeonUpdateSubscriber {
	public static final String VERSION = "0.0.10";
	public static final Logger logger = LogManager.getLogger(InfiniteSpire.class.getName());

	private static ArrayList<OnQuestRemovedSubscriber> onQuestRemovedSubscribers = new ArrayList<>();
	private static ArrayList<OnQuestIncrementSubscriber> onQuestIncrementSubscribers = new ArrayList<>();
	private static ArrayList<OnQuestAddedSubscriber> onQuestAddedSubscribers = new ArrayList<>();

    public static QuestLog questLog = new QuestLog();
    
    public static boolean isEndless = false;
    public static boolean hasDefeatedGuardian;
    public static boolean shouldLoad = false;

    public static QuestLogScreen questLogScreen = new QuestLogScreen(questLog);
    
    public static Color CARD_COLOR = new Color(0f,0f,0f,1f);
    
    public static String createID(String id) {
    	return "infinitespire:" + id;
    }

	private enum LoadType {
    	RELIC,
    	CARD,
    	KEYWORD,
		QUEST
    }
    
    public InfiniteSpire() {
    	BaseMod.subscribe(this);
    }
    
    public static void initialize() {
        logger.info("VERSION:" + VERSION);
        new InfiniteSpire();
    }
    
    @Override
	public void receivePostInitialize() {
		initializeQuestLog();
		
		Texture modBadge = getTexture("img/infinitespire/modbadge.png");
		BaseMod.registerModBadge(modBadge, "Infinite Spire", "Blank The Evil", "Adds a new way to play Slay the Spire, no longer stop after the 3rd boss. Keep fighting and gain perks as you climb.", null);
		
		BaseMod.addEvent(EmptyRestSite.ID, EmptyRestSite.class, Exordium.ID);
		BaseMod.addEvent(HoodedArmsDealer.ID, HoodedArmsDealer.class);
		BaseMod.addEvent(PrismEvent.ID,PrismEvent.class, Exordium.ID);

		BaseMod.addMonster(LordOfAnnihilation.ID, new BaseMod.GetMonster() {
            @Override
            public AbstractMonster get() {
                return new LordOfAnnihilation();
            }
        });
		BaseMod.addBoss(Exordium.ID, LordOfAnnihilation.ID, "img/infinitespire/ui/map/bossIcon.png", "img/infinitespire/ui/map/bossIcon-outline.png");
    }
    
    @Override
	public void receiveEditStrings() {
    	String relicStrings = Gdx.files.internal("local/infinitespire/relics.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(RelicStrings.class, relicStrings);
		
    	String eventStrings = Gdx.files.internal("local/infinitespire/events.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(EventStrings.class, eventStrings);
		
		String monsterStrings = Gdx.files.internal("local/infinitespire/monsters.json").readString(String.valueOf(StandardCharsets.UTF_8));
		BaseMod.loadCustomStrings(MonsterStrings.class, monsterStrings);
    }

	@Override
	public void receiveEditKeywords() {
		String[] golemsMight = {"golem's might", "golem's", "golem", "golem"};
		String[] crit = {"critical", "crit"};
		
		BaseMod.addKeyword(golemsMight, "Each turn your attacks deal 10% more damage than the last turn.");
		BaseMod.addKeyword(crit, "The next attack you play will deal 2x damage.");
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

			config.setBool("isEndless", isEndless);
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
		
		RelicLibrary.addBlue(new Freezer());
		
		RelicLibrary.addRed(new BurningSword());

		Relic.addQuestRelic(new HolyWater());
		
		initializeCrossoverRelics();
		
    	RelicLibrary.add(new BottledSoul()); //This relic is broken
    }
    
    private static void initializeQuestLog() {
    	logger.info("InfiniteSpire | Initializing questLog...");
    	
       
        QuestHelper.init();
        initializeCrossoverQuests();
        loadData();
    }
    
    private static void initializeCards() {
    	BaseMod.addColor(CardColorEnumPatch.CardColorPatch.INFINITE_BLACK, CARD_COLOR, CARD_COLOR, CARD_COLOR, CARD_COLOR, CARD_COLOR,
				Color.BLACK.cpy(), CARD_COLOR, "img/infinitespire/cards/ui/512/boss-attack.png", "img/infinitespire/cards/ui/512/boss-skill.png",
				"img/infinitespire/cards/ui/512/boss-power.png", "img/infinitespire/cards/ui/512/boss-orb.png", "img/infinitespire/cards/ui/1024/boss-attack.png", 
				"img/infinitespire/cards/ui/1024/boss-skill.png", "img/infinitespire/cards/ui/1024/boss-power.png", "img/infinitespire/cards/ui/1024/boss-orb.png");
    	
    	logger.info("InfiniteSpire | Initializing dynamic variables...");
    	BaseMod.addDynamicVariable(new Neurotoxin.PoisonVariable());
    	logger.info("InfiniteSpire | Initializing cards...");    	
    	CardHelper.addCard(new OneForAll());
    	CardHelper.addCard(new Neurotoxin());

    	//Black Cards
    	CardHelper.addCard(new FinalStrike());
    	CardHelper.addCard(new ThousandBlades());
    	CardHelper.addCard(new Gouge());
    	CardHelper.addCard(new DeathsTouch());
    	CardHelper.addCard(new Collect());
    	CardHelper.addCard(new NeuralNetwork());
    	CardHelper.addCard(new FutureSight());
    	CardHelper.addCard(new Punishment());
    	CardHelper.addCard(new UltimateForm());
    }
  
    private static void initializeCrossoverRelics() {
    	try {
			initializeReplayTheSpire(LoadType.RELIC);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect ReplayTheSpire...");
		}
    	try {
			initializeFruityMod(LoadType.RELIC);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect FruityMod...");
		}
    }

	private static void initializeCrossoverCards() {
    	try {
			initializeReplayTheSpire(LoadType.CARD);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect ReplayTheSpire...");
		}
    }

    private static void initializeCrossoverQuests(){
    	try{
    		initializeReplayTheSpire(LoadType.QUEST);
		} catch (ClassNotFoundException | NoClassDefFoundError e) {
			logger.info("InfiniteSpire failed to detect ReplayTheSpire...");
		}
	}

	@SuppressWarnings("unused")
	private static void initializeReplayTheSpire(LoadType type) throws ClassNotFoundException, NoClassDefFoundError {
		Class<ReplayTheSpireMod> replayTheSpire = ReplayTheSpireMod.class;
		logger.info("InfiniteSpire | InfiniteSpire has successfully detected Replay The Spire!");
		
		if(type == LoadType.RELIC) {
			logger.info("InfiniteSpire | Initializing Relics for Replay The Spire...");
			RelicLibrary.add(new BrokenMirror());
		}
		if(type == LoadType.CARD) {
			logger.info("InfiniteSpire | Initializing Cards for Replay The Spire...");
		}
		if(type == LoadType.QUEST) {
			QuestHelper.registerQuest(CaptainAbeQuest.class);
		}
	}
	@SuppressWarnings("unused")
	private static void initializeFruityMod(LoadType type)throws ClassNotFoundException, NoClassDefFoundError {
		Class<FruityMod> fruityMod = FruityMod.class;
		logger.info("InfiniteSpire | InfiniteSpire has successfully detected FruityMod!");
		
		if(type == LoadType.RELIC) {
			logger.info("InfiniteSpire | Initializing Relics for FruityMod...");
			BaseMod.addRelicToCustomPool(new SpectralDust(), AbstractCardEnum.SEEKER_PURPLE);
		}
		if(type == LoadType.CARD) {
			logger.info("InfiniteSpire | Initializing Cards for FruityMod...");
		}
	}

	public static void triggerDieQuests(){
    	for(Quest q : questLog){
    		if(q instanceof DieQuest){
    			q.incrementQuestSteps();
			}
		}
	}

	public static void subscribe(ISubscriber subscriber){
		subscribeIfInstance(onQuestRemovedSubscribers, subscriber, OnQuestRemovedSubscriber.class);
		subscribeIfInstance(onQuestIncrementSubscribers, subscriber, OnQuestIncrementSubscriber.class);
		subscribeIfInstance(onQuestAddedSubscribers, subscriber, OnQuestAddedSubscriber.class);
	}

	@SuppressWarnings("unchecked")
	private static <T> void subscribeIfInstance(ArrayList<T> list, ISubscriber sub, Class<T> clazz) {
		if(clazz.isInstance(sub)){
			list.add((T) sub);
		}
	}

	public static void publishOnQuestRemoved(Quest quest){
		for(OnQuestRemovedSubscriber subscriber : onQuestRemovedSubscribers){
			subscriber.receiveQuestRemoved(quest);
		}
	}

	public static void publishOnQuestIncrement(Quest quest){
    	for(OnQuestIncrementSubscriber subscriber : onQuestIncrementSubscribers){
    		subscriber.receiveQuestIncrement(quest);
		}
	}

	public static void publishOnQuestAdded(Quest quest){
    	for(OnQuestAddedSubscriber subscriber: onQuestAddedSubscribers){
    		subscriber.receiveQuestAdded(quest);
		}
	}

	@Override
	public void receivePostBattle(AbstractRoom room) {
		if(room instanceof MonsterRoomBoss) {
			int amount = 3;
			
			if(InfiniteSpire.questLog.size() + amount > 21) {
				amount -= (InfiniteSpire.questLog.size() + amount) - 21;
			}
			if(amount > 0) {
				AbstractDungeon.actionManager.addToBottom(new AddQuestAction(QuestHelper.getRandomQuests(amount)));
			}
		}
	}

	@Override
	public void receivePreDungeonUpdate() {
    	//This is code to add event quests when their shouldBegin requirements are met
		for(Class<? extends Quest> questClass: QuestHelper.questMap.values()){
			try {
				//check to see if questclass implements eventquest
				if(IAutoQuest.class.isAssignableFrom(questClass)){
					Quest quest = questClass.newInstance();
					//check to see if it shouldBegin and it isnt already in the quest log
					if(AbstractDungeon.player != null && ((IAutoQuest) quest).shouldBegin()
                            && !InfiniteSpire.questLog.hasQuest(quest)) {
						AbstractDungeon.actionManager.addToBottom(new AddQuestAction(quest.createNew()));
					}
				}
			}catch(InstantiationException | IllegalAccessException e){
				e.printStackTrace();
			}
		}
	}
}
