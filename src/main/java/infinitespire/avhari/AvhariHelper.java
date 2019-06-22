package infinitespire.avhari;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import infinitespire.InfiniteSpire;
import infinitespire.helpers.CardHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static infinitespire.patches.CardColorEnumPatch.CardColorPatch.INFINITE_BLACK;

public class AvhariHelper {



	private static final Texture shardTexture = InfiniteSpire.Textures.getUITexture("topPanel/avhari/voidShard.png");

	public static class SpinningCardItems {
		private ArrayList<CardItem> cardItems = new ArrayList<>();

		private float deltaSpeed = 1f;

		public SpinningCardItems(float cX, float cY) {

			Vector2 rotationPoint = new Vector2(cX, cY);

			ArrayList<AbstractCard> cards = CardHelper.getBlackRewardCards(5);

			float dist = Settings.HEIGHT / 3.5f;

			cardItems.add(new CardItem(cards.get(0), rotationPoint, dist, 0f));
			cardItems.add(new CardItem(cards.get(1), rotationPoint, dist, 72f));
			cardItems.add(new CardItem(cards.get(2), rotationPoint, dist, 72f * 2));
			cardItems.add(new CardItem(cards.get(3), rotationPoint, dist, 72f * 3));
			cardItems.add(new CardItem(cards.get(4), rotationPoint, dist, 72f * 4));
		}

		public void render(SpriteBatch sb) {
			for(CardItem cardItem : cardItems) {
				cardItem.render(sb);
			}
		}

		public void update() {
			boolean shouldRotate = true;
			for(CardItem cardItem : cardItems) {
				cardItem.update();
				if(cardItem.isHovered()) {
					cardItem.card.drawScale = MathUtils.lerp(0.4f, .85f, cardItem.card.drawScale);
					shouldRotate = false;
				} else {
					cardItem.card.drawScale = MathUtils.lerp(0.85f, 0.4f, cardItem.card.drawScale);
				}
			}

			if(shouldRotate) {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 1.0f);
			}else {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 0.0f);
			}

			float deltaAngle = (-15 * deltaSpeed) * Gdx.graphics.getDeltaTime();

			for(CardItem cardItem : cardItems) {
				cardItem.rotateByAmount(deltaAngle);
			}

			cardItems.removeIf((cardItem) -> {
				if(cardItem.isHovered() && InputHelper.justClickedLeft) {
					if(cardItem.canBuy()) {
						CardCrawlGame.sound.play("SHOP_PURCHASE");
						InfiniteSpire.voidShardCount -= cardItem.price;
						AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(cardItem.card, cardItem.card.current_x, cardItem.card.current_y));
						return true;
					}
					CardCrawlGame.sound.play("UI_CLICK_2");
				}
				return false;
			});
		}
	}

	public static class CardItem {

		private Texture locked = InfiniteSpire.Textures.getCardTexture("ui/512/card-locked.png");
		private Color lockAlpha = new Color(1.0f, 1.0f, 1.0f, 1.0f);
		public int price = -1;
		private AbstractCard card;
		private Vector2 position;
		private Vector2 center;
		private float angleFromCenter;
		private Hitbox hb;

		public CardItem(AbstractCard card, Vector2 centerOfCircle, float distanceFromCenter, float angleFromCenter) {
			this.card = card;
			switch (card.rarity) {
				case COMMON:
					price = 1;
					break;
				case UNCOMMON:
					price = 2;
					break;
				case RARE:
					price = 3;
					break;
				default:
					price = 99;
			}

			if(card.color.equals(INFINITE_BLACK)) price = InfiniteSpire.AVHARI_CARD_PRICE;

			float xPos = MathUtils.cosDeg(angleFromCenter) * distanceFromCenter;
			float yPos = MathUtils.sinDeg(angleFromCenter) * distanceFromCenter;

			position = new Vector2(centerOfCircle.x + xPos, centerOfCircle.y + yPos);
			center = centerOfCircle;

			card.drawScale = 0.4f;
			card.current_x = centerOfCircle.x;
			card.current_y = centerOfCircle.y;

			hb = new Hitbox(position.x, position.y ,card.hb.width, card.hb.height);
		}

		public void render(SpriteBatch sb) {
			sb.setColor(Color.WHITE.cpy());
			card.render(sb);
			renderLock(sb);
			renderPrice(sb);
			this.hb.render(sb);
		}

		public void renderPrice(SpriteBatch sb) {
			float scale = Settings.scale;
			float shardCX = shardTexture.getWidth() / 2f;
			float shardCY = shardTexture.getHeight() / 2f;
			float width = shardTexture.getWidth();
			float height = shardTexture.getHeight();
			Color fontColor = Color.WHITE.cpy();

			sb.setColor(lockAlpha);
			sb.draw(shardTexture,
				position.x - shardCX,
				position.y - shardCY,
				width/2,
				height/2,
				width,
				height,
				scale,
				scale,
				1.0f,
				0,
				0,
				shardTexture.getWidth(),
				shardTexture.getHeight(),
				false,
				false);

			if(!this.canBuy()) {
				fontColor = Color.RED.cpy();
			}

			fontColor.a = lockAlpha.a;

			FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, "" + this.price, position.x, position.y - (16f * Settings.scale), fontColor);

		}

		public void renderLock(SpriteBatch sb) {
			float offset = 256f;

			try {
				Method method = AbstractCard.class.getDeclaredMethod("renderHelper", SpriteBatch.class, Color.class, Texture.class, float.class, float.class, float.class);
				method.setAccessible(true);
				method.invoke(card,
					sb,
					lockAlpha,
					locked,
					card.current_x - offset,
					card.current_y - offset,
					1.0f);
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

			sb.setColor(Color.WHITE.cpy());
		}

		public void update() {
			card.target_x = position.x;
			card.target_y = position.y;
			card.update();
			this.hb.update();
			if(hb.hovered) {
				lockAlpha.a = MathHelper.fadeLerpSnap(lockAlpha.a, 0.2f);
			}else {
				lockAlpha.a = MathHelper.fadeLerpSnap(lockAlpha.a, 1.0f);
			}
		}

		public void rotateByAmount(float angle) {
			float angleR = angle * (float)(Math.PI / 180d);
			float newX = MathUtils.cos(angleR) * (position.x - center.x) - MathUtils.sin(angleR) * (position.y - center.y) + center.x;
			float newY = MathUtils.sin(angleR) * (position.x - center.x) + MathUtils.cos(angleR) * (position.y - center.y) + center.y;

			this.position.set(newX, newY);
			this.hb.update(newX - (hb.width / 2f), newY - (hb.height / 2f));
		}

		public AbstractCard getCard() {
			return card.makeStatEquivalentCopy();
		}

		public float getAngleFromCenter() {
			return angleFromCenter;
		}

		public Vector2 getPosition() {
			return position;
		}

		public void setAngleFromCenter(float angleFromCenter) {
			this.angleFromCenter = angleFromCenter;
		}

		public boolean isHovered() {
			return hb.hovered;
		}

		public boolean canBuy() {
			return InfiniteSpire.voidShardCount >= this.price;
		}
	}

	public static class SpinningRelicItems {
		public ArrayList<RelicItem> relicItems = new ArrayList<>();

		private float deltaSpeed = 1f;

		public SpinningRelicItems(float cX, float cY) {
			Vector2 rotationPoint = new Vector2(cX, cY);
			float dist = 90f * Settings.scale;

			relicItems.add(new RelicItem(getRandomBossRelic(), rotationPoint, dist, 0f));
			relicItems.add(new RelicItem(getRandomBossRelic(), rotationPoint, dist, 120f));
			relicItems.add(new RelicItem(getRandomBossRelic(), rotationPoint, dist, 240f));
		}

		public void render(SpriteBatch sb) {
			for (RelicItem relicItem : relicItems) {
				relicItem.render(sb);
			}
		}

		public void update() {
			boolean shouldRotate = true;
			boolean hasPurchaced = false;
			for(RelicItem relicItem : relicItems) {
				relicItem.update();
				if(relicItem.isHovered()) {
					shouldRotate = false;
				}
			}

			if(shouldRotate) {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 1.0f);
			}else {
				deltaSpeed = MathHelper.scaleLerpSnap(deltaSpeed, 0.0f);
			}

			final float deltaAngle = (-15 * deltaSpeed) * Gdx.graphics.getDeltaTime();

			for(RelicItem relicItem : relicItems) {
				relicItem.rotateByAmount(deltaAngle);
				if(relicItem.isHovered() && InputHelper.justClickedLeft) {
					hasPurchaced = relicItem.purchace();
					CardCrawlGame.sound.play("UI_CLICK_2");
				}
			}

			if(hasPurchaced) relicItems.clear();
		}
	}

	public static AbstractRelic getRandomBossRelic() {
		return AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.BOSS);
	}

	public static class RelicItem {
		public int price = InfiniteSpire.AVHARI_RELIC_PRICE;
		private Vector2 center, position;
		private AbstractRelic relic;
		private Hitbox hb;
		private float angleFromCenter;

		public RelicItem(AbstractRelic relic, Vector2 centerOfCircle, float distanceFromCenter, float angleFromCenter) {
			this.relic = relic;

			float xPos = MathUtils.cosDeg(angleFromCenter) * distanceFromCenter;
			float yPos = MathUtils.sinDeg(angleFromCenter) * distanceFromCenter;

			position = new Vector2(centerOfCircle.x + xPos, centerOfCircle.y + yPos);
			center = centerOfCircle;

			hb = new Hitbox(position.x, position.y, relic.hb.width, relic.hb.height);
		}

		public void render(SpriteBatch sb) {
			sb.setColor(Color.WHITE.cpy());
			relic.renderOutline(sb, false);
			relic.renderWithoutAmount(sb, new Color(0.0f, 0.0f, 0.0f, 0.25f));
			renderPrice(sb);

			if(this.isHovered()) {
				relic.renderTip(sb);
			}

			this.hb.render(sb);
		}

		private void renderPrice(SpriteBatch sb) {

			float width = shardTexture.getWidth();
			float height = shardTexture.getHeight();
			float xPos = this.relic.currentX - (width / 2f);
			float yPos = this.relic.currentY - height - (10f * Settings.scale);

			TextureRegion region = new TextureRegion(shardTexture);

			sb.draw(region,
				xPos,
				yPos,
				width / 2f,
				height / 2f,
				width,
				height,
				Settings.scale, Settings.scale,
				1.0f);

			Color fontColor = Color.WHITE.cpy();
			if(!this.canBuy()) {
				fontColor = Color.RED.cpy();
			}

			FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, "" + this.price,
				relic.currentX,
				yPos + (16f * Settings.scale * Settings.scale), fontColor);
		}

		public void update() {
			relic.currentX = position.x;
			relic.currentY = position.y;
			this.hb.update();
			//hover logic
		}

		public void rotateByAmount(float angle) {
			float angleR = angle * (float)(Math.PI / 180d);
			float newX = MathUtils.cos(angleR) * (position.x - center.x) - MathUtils.sin(angleR) * (position.y - center.y) + center.x;
			float newY = MathUtils.sin(angleR) * (position.x - center.x) + MathUtils.cos(angleR) * (position.y - center.y) + center.y;

			this.position.set(newX, newY);
			this.hb.update(newX - (hb.width / 2f), newY - (hb.height / 2f));
		}

		public boolean isHovered() {
			return this.hb.hovered;
		}

		public boolean canBuy() {
			return InfiniteSpire.voidShardCount >= this.price;
		}

		public boolean purchace() {
			if(this.canBuy()) {
				CardCrawlGame.sound.play("SHOP_PURCHASE");
				this.relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
				InfiniteSpire.voidShardCount -= this.price;
				return true;
			}
			return false;
		}
	}
}
