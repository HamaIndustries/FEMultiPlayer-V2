package chu.engine;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glAlphaFunc;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import chu.engine.event.KeyboardEvent;
import chu.engine.event.MouseEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class Game.
 */
public abstract class Game {

	/** The window width. */
	protected static int windowWidth = 640;

	/** The window height. */
	protected static int windowHeight = 480;

	/** The keys. */
	private static List<KeyboardEvent> keys;

	/** The mouse events. */
	private static List<MouseEvent> mouseEvents;

	/** The time delta. */
	protected static long timeDelta;

	/** The gl context exists. */
	protected static boolean glContextExists;

	/**
	 * Inits the.
	 *
	 * @param width the width
	 * @param height the height
	 * @param name the name
	 */
	public void init(int width, int height, String name) {
		windowWidth = Math.round(width * net.fe.FEResources.getWindowScale());
		windowHeight = Math.round(height * net.fe.FEResources.getWindowScale());

		try {
			Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
			Display.create();
			Display.setTitle(name);
			Keyboard.create();
			Keyboard.enableRepeatEvents(true);
			Mouse.create();
			glContextExists = true;
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// init OpenGL
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.01f);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glClearDepth(1.0f);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0, 0, windowWidth, windowHeight);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, windowWidth / net.fe.FEResources.getWindowScale(),
		        windowHeight / net.fe.FEResources.getWindowScale(), 0, 1, -1); // It's
		                                                                       // basically
		                                                                       // a
		                                                                       // camera
		glMatrixMode(GL_MODELVIEW);

		keys = new ArrayList<KeyboardEvent>();
		mouseEvents = new ArrayList<MouseEvent>();
	}

	/**
	 * Loop.
	 */
	public abstract void loop();

	/**
	 * Gets the input.
	 *
	 * @return the input
	 */
	public static void getInput() {
		Keyboard.poll();
		keys.clear();
		while (Keyboard.next()) {
			KeyboardEvent ke = new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(),
			        Keyboard.isRepeatEvent(), Keyboard.getEventKeyState());
			keys.add(ke);
		}
		Mouse.poll();
		mouseEvents.clear();
		while (Mouse.next()) {
			MouseEvent me = new MouseEvent(Mouse.getEventX(), Mouse.getEventY(), Mouse.getEventDWheel(),
			        Mouse.getEventButton(), Mouse.getEventButtonState());
			mouseEvents.add(me);
		}
	}

	/**
	 * Gets the keys.
	 *
	 * @return the keys
	 */
	public static List<KeyboardEvent> getKeys() {
		return keys;
	}

	/**
	 * Gets the mouse events.
	 *
	 * @return the mouse events
	 */
	public static List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}

	/**
	 * Gets the delta seconds.
	 *
	 * @return the delta seconds
	 */
	public static float getDeltaSeconds() {
		return timeDelta / 1000000000.0f;
	}

	/**
	 * Gets the window width.
	 *
	 * @return the window width
	 */
	public static int getWindowWidth() {
		return windowWidth;
	}

	/**
	 * Gets the window height.
	 *
	 * @return the window height
	 */
	public static int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * Gl context exists.
	 *
	 * @return true, if successful
	 */
	public static boolean glContextExists() {
		return glContextExists;
	}

	/**
	 * Gets the scale x.
	 *
	 * @return the scale x
	 */
	public static float getScaleX() {
		return net.fe.FEResources.getWindowScale();
	}

	/**
	 * Gets the scale y.
	 *
	 * @return the scale y
	 */
	public static float getScaleY() {
		return net.fe.FEResources.getWindowScale();
	}
}
