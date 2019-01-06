package infinitespire.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.BloodShotEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import infinitespire.InfiniteSpire;
import infinitespire.actions.SpawnShapeAction;
import infinitespire.powers.ClusterPower;
import infinitespire.powers.TempThornsPower;
import infinitespire.util.TextureLoader;

import java.util.ArrayList;

public class MassOfShapes extends AbstractMonster {
	public static final String ID = "infinitespire:MassOfShapes";
	private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	private static final String NAME = monsterStrings.NAME;

	private static final int MAX_HP = 550;
	private static final int SMALL_BLOCK = 15;
	private static final int LARGE_BLOCK = 30;
	private static final int EXPLODE = 25;
	private static final int SPAWN_CHANCE = 33;
	private static final int SLAM = 2;

	private static final float FPS_SCALE = (240f / Settings.MAX_FPS);

	private float coreRotation = 0.0f;
	private Texture coreTexture = InfiniteSpire.getTexture("img/infinitespire/monsters/massofshapes/core.png");

	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Shape> shapes = new ArrayList<>();

	private int turn = 0;

	public MassOfShapes(){
		super(NAME, ID, MAX_HP, 0.0f, 0.0f, 458f, 415f, null);

		this.type = EnemyType.BOSS;
		this.dialogX = 0f;
		this.dialogY = 0f;
		this.img = TextureLoader.getTexture("img/infinitespire/monsters/massofshapes/massofshapes.png");
		this.damage.add(new DamageInfo(this, EXPLODE));
		this.damage.add(new DamageInfo(this, SLAM));

		for(int i = 0; i < 30; i++){
			shapes.add(new Shape(getRandomShapeType(), this));
		}
	}

	@Override
	public void usePreBattleAction() {
		CardCrawlGame.music.unsilenceBGM();
		AbstractDungeon.scene.fadeOutAmbiance();
		AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ClusterPower(this)));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TempThornsPower(this, 2, 3), 3));
	}

	public void update(){
		super.update();
		this.coreRotation += (Gdx.graphics.getDeltaTime() * 8);
		if(coreRotation >= 360.0f){
			coreRotation = 0.0f;
		}
		minions.removeIf((minion) -> minion.minion.isDead);
		shapes.removeIf(Shape::isDead);
	}

	public void render(SpriteBatch sb) {
		super.render(sb);
		sb.setColor(this.tint.color);
		sb.draw(
			coreTexture,
			this.hb.cX - coreTexture.getWidth() / 2f,
			this.hb.cY - coreTexture.getHeight() / 2f,
			coreTexture.getWidth() / 2f,
			coreTexture.getWidth() / 2f,
			coreTexture.getWidth(),
			coreTexture.getHeight(),
			1f,
			1,
			coreRotation,
			0,0,
			coreTexture.getWidth(),
			coreTexture.getWidth(),
			false,
			false);

		for(Shape shape : shapes){
			shape.render(sb);
		}
	}

	@Override
	public void die() {
		this.useFastShakeAnimation(5.0f);
		CardCrawlGame.screenShake.rumble(4.0f);
		this.deathTimer += 1.5f;
		super.die();
		this.onBossVictoryLogic();
		for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!m.isDead && !m.isDying) {
				AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
				AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
				AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2f));
			}
		}
	}

	@Override
	public void damage(DamageInfo info) {
		super.damage(info);

		if(this.currentBlock == 0) {
			if (info.owner != null && info.owner.equals(AbstractDungeon.player) && !AbstractDungeon.actionManager.turnHasEnded) {
				if (this.minions.size() < 3 && AbstractDungeon.miscRng.random(0, 99) <= SPAWN_CHANCE) {
					AbstractDungeon.actionManager.addToTop(new SpawnShapeAction(getNextOpenMinionSlot(), this));
				}
			}
		}
		if(this.currentHealth < (this.maxHealth / 3) * 2 && shapes.size() > 20) {
			for(int i = 0; i < 10; i++){
				shapes.get(i).die();
			}
		}
		if(this.currentHealth < (this.maxHealth / 3) && shapes.size() > 10) {
			for(int i = 0; i < 10; i++){
				shapes.get(i).die();
			}
		}
		if(this.isDead){
			for (Minion minion : minions){
				minion.minion.escape();
			}
		}
	}

	public void addMinion(AbstractMonster m) {
		Minion minion = new Minion(getNextOpenMinionSlot());
		minion.setMinion(m);
		minions.add(minion);
	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;
		AbstractMonster m = this;
		GameActionManager a = AbstractDungeon.actionManager;

		switch (this.nextMove){
			case MoveBytes.GAIN_THORNS:
				a.addToBottom(new ApplyPowerAction(m, m, new TempThornsPower(this, 3, 5), 3));
				a.addToBottom(new GainBlockAction(m, m, SMALL_BLOCK));
				break;
			case MoveBytes.EXPLODE:
				a.addToBottom(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.FIRE));
				AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(this.hb.cX, this.hb.cY));
				break;
			case MoveBytes.ADD_DAZED:
				AbstractCard dazed = CardLibrary.getCard(Dazed.ID).makeCopy();
				a.addToBottom(new MakeTempCardInDrawPileAction(dazed, 2, true, true));
				break;
			case MoveBytes.GROUP_SLAM:
				a.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, p.hb.cX, p.hb.cY, 10), 0.25f));
				for(int i = 0; i < 10; i++) {
					AbstractGameAction.AttackEffect effect = AbstractGameAction.AttackEffect.FIRE;
					switch(new Random().random(2)){
						case 0:
							effect = AbstractGameAction.AttackEffect.FIRE;
							break;
						case 1:
							effect = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
							break;
						case 2:
							effect = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
							break;
					}

					a.addToBottom(new DamageAction(p, damage.get(1), effect));
				}
			case MoveBytes.SHED:
				a.addToBottom(new GainBlockAction(m, m, LARGE_BLOCK));
		}
		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	@Override
	protected void getMove(int roll) {
		switch(this.turn % 3) {
			case 0:
				if(roll < 50) {
					this.setMove(MoveBytes.EXPLODE, Intent.ATTACK, this.damage.get(0).base);
				} else {
					this.setMove(MoveBytes.GROUP_SLAM, Intent.ATTACK, damage.get(1).base, 10, true);
				}
				break;
			case 1:
				this.setMove(MoveBytes.ADD_DAZED, Intent.DEBUFF);
				break;
			case 2:
				this.setMove(MoveBytes.GAIN_THORNS, Intent.DEFEND_BUFF);
				break;
		}
		this.turn++;
	}

	private int getNextOpenMinionSlot() {
		boolean[] slots = {true, true, true};
		for(Minion minion : minions) {
			slots[minion.slot] = false;
		}
		for(int i = 0; i < slots.length; i ++){
			if(slots[i]){
				return i;
			}
		}

		return -1;
	}

	private Shape.ShapeType getRandomShapeType(){
		double rand = Math.random();

		if(rand < 0.33) {
			return Shape.ShapeType.EXPLODER;
		} else if(rand >= 0.33 && rand < 0.66) {
			return Shape.ShapeType.SPIKER;
		} else {
			return Shape.ShapeType.REPULSER;
		}
	}

	private class MoveBytes {
		private static final byte GAIN_THORNS = 0;
		private static final byte EXPLODE = 1;
		private static final byte ADD_DAZED = 2;
		private static final byte GROUP_SLAM = 3;
		private static final byte SHED = 4;
	}

	private class Minion {
		public AbstractMonster minion;
		public int slot;
		public Minion(int slot) {
			this.slot = slot;
		}

		public Minion() {
			this(0);
		}

		public void setMinion(AbstractMonster m){
			this.minion = m;
		}
	}

	private static class Shape {

		private Skeleton skeleton;
		private TextureAtlas atlas;
		private ShapeType shape;
		private AnimationStateData stateData;
		private AnimationState state;
		private Vector2 position;
		private Vector2 velocity = new Vector2();
		private float rotation;
		private MassOfShapes mon;

		private enum ShapeType {
			EXPLODER,
			SPIKER,
			REPULSER
		}

		public Shape(ShapeType shape, MassOfShapes mon) {
			this.shape = shape;
			this.mon = mon;
			switch (shape){
				default:
				case EXPLODER:
					createShapeData("exploder");
					break;
				case SPIKER:
					createShapeData("spiker");
					break;
				case REPULSER:
					createShapeData("repulser");
					break;
			}
			this.rotation = (float) Math.random();

			Random random = new Random();

			float farLeft = mon.hb.cX - (mon.hb.width / 2 * 0.8f);
			float farRight = mon.hb.cX + (mon.hb.width / 2 * 0.8f);
			float top = mon.hb.cY + (mon.hb.height / 2 * 0.625f) ;
			float bottom = mon.hb.cY - (mon.hb.height / 2 * 0.5f);

			this.position = new Vector2(
				random.random(farLeft, farRight),
				random.random(bottom, top));
			this.skeleton.getRootBone().setRotation(rotation * 360);

			AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
			e.setTime(e.getEndTime() * MathUtils.random());
		}

		private void die() {
			velocity = new Vector2(1, new Random().random()).nor().scl(5 * FPS_SCALE);
		}

		public boolean isDead(){
			return this.position.x > Settings.WIDTH + 100 * Settings.scale;
		}

		private void createShapeData(String name) {
			atlas = getAtlas(name);
			SkeletonJson json = new SkeletonJson(this.atlas);
			json.setScale(Settings.scale / 1.0f);
			SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/monsters/theForest/" + name + "/skeleton.json"));
			this.skeleton = new Skeleton(skeletonData);
			this.skeleton.setColor(Color.WHITE.cpy());
			this.stateData = new AnimationStateData(skeletonData);
			this.state = new AnimationState(this.stateData);
		}

		private TextureAtlas getAtlas(String name) {
			return new TextureAtlas(Gdx.files.internal("images/monsters/theForest/" + name + "/skeleton.atlas"));
		}

		public void render(SpriteBatch sb) {
			this.state.update(Gdx.graphics.getDeltaTime());
			this.state.apply(this.skeleton);
			this.skeleton.updateWorldTransform();
			this.position.add(this.velocity);
			this.skeleton.setPosition(this.position.x, this.position.y);
			this.skeleton.setColor(mon.tint.color);
			this.skeleton.setFlip(mon.flipHorizontal, mon.flipVertical);
			this.skeleton.findBone("shadow").setScale(0.0f);
			this.skeleton.updateWorldTransform();

			sb.end();
			CardCrawlGame.psb.begin();
			sr.draw(CardCrawlGame.psb, this.skeleton);
			CardCrawlGame.psb.end();
			sb.begin();
			sb.setBlendFunction(770, 771);
		}
	}
}
