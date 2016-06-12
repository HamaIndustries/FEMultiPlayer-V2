package net.fe.editor;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

import static java.util.Collections.emptyList;

import chu.engine.Game;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

// TODO: Auto-generated Javadoc
/**
 * The Class LevelEditor.
 */
public class LevelEditor extends Game {
	
	/** The current stage. */
	private static Stage currentStage;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LevelEditor game = new LevelEditor();
		game.init(960, 640, "Fire Emblem Level Editor");
		game.loop();
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Game#init(int, int, java.lang.String)
	 */
	@Override
	public void init(int width, int height, String name) {
		super.init(width, height, name);
		currentStage = new LevelEditorStage("seven?");
	}
	
	/* (non-Javadoc)
	 * @see chu.engine.Game#loop()
	 */
	@Override
	public void loop() {
		while(!Display.isCloseRequested()) {
			final long time = System.nanoTime();
			glClear(GL_COLOR_BUFFER_BIT |
			        GL_DEPTH_BUFFER_BIT |
			        GL_STENCIL_BUFFER_BIT);
			glClearDepth(1.0f);
			getInput();
			glPushMatrix();
				currentStage.beginStep(emptyList());
				currentStage.onStep();
				currentStage.processAddStack();
				currentStage.processRemoveStack();
				Renderer.getCamera().lookThrough();
				currentStage.render();
				currentStage.endStep();
			glPopMatrix();
			Display.update();
			timeDelta = System.nanoTime()-time;
		}
		AL.destroy();
		Display.destroy();
	}

}
