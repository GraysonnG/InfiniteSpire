package infinitespire.cards.black;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import infinitespire.InfiniteSpire;
import infinitespire.abstracts.BlackCard;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class Virus extends BlackCard {

	private CardConsumer<Virus> useConsumer;
	private Consumer<Virus> upgradeConsumer, constructorConsumer;

	public static ArrayList<Virus> virusInstances = new ArrayList<>();
	public boolean masterUpgraded;

	public Virus(String id, String name, String imgPath, int cost, String rawDescription, CardType type, CardTarget target, boolean masterUpgraded, CardConsumer<Virus> use, Consumer<Virus> upgrade, Consumer<Virus> constructor) {
		super(id, name, imgPath, cost, rawDescription, type, target);
		if(CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			this.rawDescription = rawDescription + getPostDescription(masterUpgraded);
			this.initializeDescription();
		}
		this.masterUpgraded = masterUpgraded;
		this.useConsumer = use;
		this.upgradeConsumer = upgrade;
		this.constructorConsumer = constructor;
		constructor.accept(this);

		this.purgeOnUse = true;
	}

	public static Virus getRandomVirus(boolean masterUpgraded) {
		return virusInstances.get(AbstractDungeon.cardRng.random(0, virusInstances.size() -1)).makeActualCopy(masterUpgraded);
	}

	public static Virus getRandomVirus(boolean masterUpgraded, AbstractCard prohibit){
		Virus virus = getRandomVirus(masterUpgraded);
		if(virus.cardID.equals(prohibit.cardID)){
			return getRandomVirus(masterUpgraded, prohibit);
		}
		return virus;
	}

	public static String getPostDescription(boolean masterUpgraded){
		return " NL " + (masterUpgraded ? MasterVirus.UPGRADED_DESCRIPTION : MasterVirus.DESCRIPTION);
	}

	@Override
	public AbstractCard makeCopy() {
		return Virus.getRandomVirus(masterUpgraded);
	}

	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard card = this.makeCopy();
		card.upgraded = this.upgraded;
		if(this.upgraded){
			card.upgrade();
		}
		return card;
	}

	private Virus makeActualCopy(boolean masterUpgraded){
		Virus v = new Virus(cardID, name, textureImg, cost, rawDescription, type, target, masterUpgraded, useConsumer, upgradeConsumer, constructorConsumer);
		v.upgraded = this.upgraded;
		return v;
	}

	@Override
	public void useWithEffect(AbstractPlayer player, AbstractMonster monster) {
		useConsumer.accept(this, player, monster);
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(Virus.getRandomVirus(this.masterUpgraded, this), 1));
		if(AbstractDungeon.cardRng.randomBoolean(this.masterUpgraded ? 0.5f : 0.25f)) {
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(Virus.getRandomVirus(this.masterUpgraded, this)));
		}
	}

	@Override
	public void upgrade() {
		upgradeConsumer.accept(this);
	}

	public void upgradeName(){
		super.upgradeName();
	}

	public void upgradeBaseCost(int newCost){
		super.upgradeBaseCost(newCost);
	}

	public void upgradeDamage(int amount){
		super.upgradeDamage(amount);
	}

	public void upgradeBlock(int amount){
		super.upgradeBlock(amount);
	}

	public void upgradeMagicNumber(int amount){
		super.upgradeMagicNumber(amount);
	}

	public static class MasterVirus extends BlackCard {
		public static final String ID = InfiniteSpire.createID("Virus");
		private static final String NAME = "Virus";
		private static final String IMG = "img/infinitespire/cards/beta.png";
		private static final int COST = -2;
		public static final String DESCRIPTION = "Purge. NL Add a random [#b400ff]Virus[] to your discard pile. You have a 25% chance to generate another random [#b400ff]Virus[].";
		public static final String UPGRADED_DESCRIPTION = "Purge. NL Add a random [#b400ff]Virus[] to your discard pile. You have a [#7efc00]50%[] chance to generate another random [#b400ff]Virus[].";
		private static final CardType TYPE = CardType.STATUS;
		private static final CardTarget TARGET = CardTarget.NONE;

		public MasterVirus() {
			super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		}

		public AbstractCard makeCopy(){
			if(CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
				return Virus.getRandomVirus(upgraded);
			}
			return super.makeCopy();
		}

		@Override
		public AbstractCard makeStatEquivalentCopy() {
			if(CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
				return makeCopy();
			}
			return super.makeStatEquivalentCopy();
		}

		@Override
		public void upgrade() {
			if(!upgraded) {
				this.upgradeName();
				this.rawDescription = UPGRADED_DESCRIPTION;
				this.initializeDescription();
			}
		}

		@Override
		public void useWithEffect(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(Virus.getRandomVirus(this.upgraded)));
		}
	}

	@FunctionalInterface
	public interface CardConsumer<T> {
		void accept(T t, AbstractPlayer p, AbstractMonster m);

		default CardConsumer<T> andThen(CardConsumer<? super T> after) {
			Objects.requireNonNull(after);
			return (T t, AbstractPlayer p, AbstractMonster m) -> { accept(t, p, m); after.accept(t, p, m); };
		}
	}
}
