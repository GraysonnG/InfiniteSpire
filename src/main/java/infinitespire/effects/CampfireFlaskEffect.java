package infinitespire.effects;

import com.megacrit.cardcrawl.vfx.campfire.CampfireSleepEffect;

public class CampfireFlaskEffect extends CampfireSleepEffect {
	public void update() {
		super.update();
		if(this.duration < this.startingDuration / 2.0f) {
			this.isDone = true;
		}
	}
}
