package chu.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import chu.engine.entity.Entity;
import chu.engine.entity.SortByRender;
import net.fe.network.Message;

public abstract class Stage {

	protected final LinkedList<Entity> entities; // can't be a sorted set
	                                             // because it's impossible
	                                             // to make a compare that is
	                                             // consistant with equals

	private final Stack<Entity> addStack;

	private final Stack<Entity> removeStack;

	public final String soundTrack;

	public Stage(String soundTrack) {
		entities = new LinkedList<Entity>();
		addStack = new Stack<Entity>();
		removeStack = new Stack<Entity>();
		this.soundTrack = soundTrack;
	}

	public final List<Entity> getAllEntities() {
		return entities;
	}

	public final void addEntity(Entity e) {
		addStack.push(e);
		e.willBeRemoved = false;
	}

	public final void removeEntity(Entity e) {
		if (e != null) {
			e.flagForRemoval();
			if (removeStack.contains(e)) {
				return;
			}
			removeStack.push(e);
		}
	}

	public void update() {
		for (Entity e : entities) {
			e.onStep();
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}

	public void render() {
		SortByRender comparator = new SortByRender();
		PriorityQueue<Entity> renderQueue = new PriorityQueue<Entity>(entities.size() + 1, comparator);
		renderQueue.addAll(entities);
		while (!renderQueue.isEmpty()) {
			renderQueue.poll().render();
		}
	}

	public final Entity instanceAt(int x, int y) {
		for (Entity e : entities) {
			if (e.x == x && e.y == y && !e.willBeRemoved())
				return e;
		}
		return null;
	}

	public final Entity[] allInstancesAt(int x, int y) {
		ArrayList<Entity> ans = new ArrayList<Entity>();
		for (Entity e : entities) {
			if (e.x == x && e.y == y && !e.willBeRemoved())
				ans.add(e);
		}

		for (Entity e : addStack) {
			if (e.x == x && e.y == y && !e.willBeRemoved())
				ans.add(e);
		}

		Entity[] ret = new Entity[ans.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ans.get(i);
		}
		return ret;
	}

	public final void processAddStack() {
		while (!addStack.isEmpty()) {
			Entity e = addStack.pop();
			entities.add(e);
			e.stage = this;
		}
	}

	public final boolean willBeRemoved(Entity e) {
		return removeStack.contains(e);
	}

	public final void processRemoveStack() {
		while (!removeStack.isEmpty()) {
			Entity e = removeStack.pop();
			entities.remove(e);
			addStack.remove(e); // Otherwise some weird shit happens and
			                    // entities get stuck in limbo
		}
	}

	public abstract void beginStep(List<Message> messages);

	public abstract void onStep();

	public abstract void endStep();

}
