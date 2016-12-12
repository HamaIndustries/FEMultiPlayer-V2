package chu.engine.entity;

import chu.engine.Stage;
import chu.engine.anim.Sprite;

public abstract class Entity {

	public float x;

	public float y;

	public float width;

	public float height;

	public float prevX;

	public float prevY;

	public float renderDepth;

	public final Sprite sprite;


	public Stage stage;

	public boolean willBeRemoved;

	public boolean solid;

	public boolean hasRun = false;

	public Entity(float x, float y) {
		this.x = x;
		this.y = y;
		this.prevX = x;
		this.prevY = y;
		sprite = new Sprite();
		willBeRemoved = false;
		solid = false;
		width = 0;
		height = 0;
	}

	public void onStep() {
		if (sprite != null)
			sprite.update();
		prevX = x;
		prevY = y;
	}

	public void beginStep() {

	}

	public void endStep() {

	}

	public void render() {
		sprite.render(x, y, renderDepth);
	}

	public final void destroy() {
		if (stage == null) {
			flagForRemoval();
		} else {
			stage.removeEntity(this);
		}
	}

	public final boolean willBeRemoved() {
		return willBeRemoved;
	}

	public final void flagForRemoval() {
		willBeRemoved = true;
	}

}
