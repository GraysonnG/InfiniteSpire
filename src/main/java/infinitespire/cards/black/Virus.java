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

	public Virus(String id, String name, String imgPath, int cost, String rawDescription, CardType type, CardTarget target, CardConsumer<Virus> use, Consumer<Virus> upgrade, Consumer<Virus> constructor) {
		super(id, name, imgPath, cost, rawDescription, type, target);
		if(CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			this.rawDescription = rawDescription + getPostDescription();
			this.initializeDescription();
		}
		this.useConsumer = use;
		this.upgradeConsumer = upgrade;
		this.constructorConsumer = constructor;
		constructor.accept(this);

		this.purgeOnUse = true;
	}

	public static Virus getRandomVirus() {
		return virusInstances.get(AbstractDungeon.cardRng.random(0, virusInstances.size() -1)).makeActualCopy();
	}

	public static Virus getRandomVirus(AbstractCard prohibit){
		Virus virus = getRandomVirus();

		if(virus.cardID.equals(prohibit.cardID)){
			return getRandomVirus(prohibit);
		}
		return virus;
	}

	public static String getPostDescription(){
		return " NL " + MasterVirus.DESCRIPTION;
	}

	@Override
	public AbstractCard makeCopy() {
		return Virus.getRandomVirus();
	}

	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard card = this.makeCopy();
		card.upgraded = this.upgraded;
		if(this.upgraded){
			card.upgrade();
		}
		return card;
	}

	private Virus makeActualCopy(){
		Virus v = new Virus(cardID, name, textureImg, cost, rawDescription, type, target, useConsumer, upgradeConsumer, constructorConsumer);
		v.upgraded = this.upgraded;
		return v;
	}

	@Override
	public void useWithEffect(AbstractPlayer player, AbstractMonster monster) {
		useConsumer.accept(this, player, monster);
		Virus v = Virus.getRandomVirus(this);
		if(this.upgraded){
			v.upgrade();
			v.upgraded = true;
		}
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(v, 1));
	}

	@Override
	public void triggerWhenDrawn() {
		Virus v = Virus.getRandomVirus(this);
		if(this.upgraded){
			v.upgrade();
			v.upgraded = true;
		}
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(v, 1));
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
		private static final String IMG = "img/infinitespire/cards/virus.png";
		private static final int COST = -2;
		public static final String DESCRIPTION = "When Drawn, add a random [#9166ff]Virus[] to your hand. NL When played, add a random [#9166ff]Virus[] to your discard pile. NL Exhaust.";
		public static final String UPGRADED_DESCRIPTION = "";
		private static final CardType TYPE = CardType.STATUS;
		private static final CardTarget TARGET = CardTarget.NONE;

		public MasterVirus() {
			super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, TARGET);
		}

		public AbstractCard makeCopy(){
			if(!AbstractDungeon.loading_post_combat && CardCrawlGame.isInARun() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
				return Virus.getRandomVirus();
			}
			return super.makeCopy();
		}

		@Override
		public boolean canUpgrade() {
			return false;
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
				this.initializeDescription();
			}
		}

		@Override
		public void useWithEffect(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(Virus.getRandomVirus()));
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
