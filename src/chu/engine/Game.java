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

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import chu.engine.event.KeyboardEvent;
import chu.engine.event.MouseEvent;
import net.fe.FEResources;

public abstract class Game {

	protected static int windowWidth = 640;

	protected static int windowHeight = 480;

	private static List<KeyboardEvent> keys;

	private static List<MouseEvent> mouseEvents;

	protected static long timeDelta;

	protected static boolean glContextExists;

	public void init(int width, int height, String name) {
		windowWidth = Math.round(width * FEResources.getWindowScale());
		windowHeight = Math.round(height * FEResources.getWindowScale());

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
		glOrtho(0, windowWidth / FEResources.getWindowScale(),
		        windowHeight / FEResources.getWindowScale(), 0, 1, -1); // It's
		                                                                       // basically
		                                                                       // a
		                                                                       // camera
		glMatrixMode(GL_MODELVIEW);

		keys = new ArrayList<KeyboardEvent>();
		mouseEvents = new ArrayList<MouseEvent>();
	}

	public abstract void loop();

	public static void getInput() {
		Keyboard.poll();
		keys.clear();
		while (Keyboard.next()) {
			int modifiers = (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) ? 1 : 0) |
							(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) ? 2 : 0) | 
							(Keyboard.isKeyDown(Keyboard.KEY_LMETA) ? 4 : 0) | 
							(Keyboard.isKeyDown(Keyboard.KEY_RMETA) ? 8 : 0);
			
			KeyboardEvent ke = new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(),
			        Keyboard.isRepeatEvent(), Keyboard.getEventKeyState(), modifiers);
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

	public static List<KeyboardEvent> getKeys() {
		return keys;
	}

	public static List<MouseEvent> getMouseEvents() {
		return mouseEvents;
	}

	public static float getDeltaSeconds() {
		return timeDelta / 1000000000.0f;
	}

	public static int getWindowWidth() {
		return windowWidth;
	}

	public static int getWindowHeight() {
		return windowHeight;
	}

	public static boolean glContextExists() {
		return glContextExists;
	}

	public static float getScaleX() {
		return FEResources.getWindowScale();
	}

	public static float getScaleY() {
		return FEResources.getWindowScale();
	}
}
