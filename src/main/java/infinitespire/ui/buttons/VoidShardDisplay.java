package infinitespire.ui.buttons;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import infinitespire.InfiniteSpire;

public class VoidShardDisplay extends TopPanelItem {

	private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);

	private static final Texture IMAGE = InfiniteSpire.Textures.getUITexture("topPanel/avhari/voidShard.png");
	public static final String ID = InfiniteSpire.createID("VoidShardItem");

	public VoidShardDisplay() {
		super(IMAGE, ID);
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, Integer.toString(InfiniteSpire.voidShardCount), this.x + (this.hb_w / 2), this.y + 16f * Settings.scale, Color.WHITE.cpy());

		if(this.getHitbox().hovered) {
			float xPos = this.x + (this.hb_w / 2);
			TipHelper.renderGenericTip(xPos, tipYpos, "Void Shards", "Mystical crystal shards that can be used to purchase weapons and tools from Avhari.");
		}
	}

	@Override
	protected void onClick() {
		CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
	}
}
