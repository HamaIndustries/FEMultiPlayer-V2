package chu.engine.anim;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Stack;

import net.fe.FEResources;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;

// TODO: Auto-generated Javadoc
/**
 * The Class Renderer.
 */
public class Renderer {

	/** The camera. */
	private static Camera camera;
	
	/** The clip. */
	private static RectClip clip;
	
	/** The Constant SCALE_FILTER. */
	private static final int SCALE_FILTER = GL_NEAREST;
	
	/** The color. */
	private static Color color;
	
	/** The state stack. */
	private static Stack<RendererState> stateStack;
	
	/** The programs. */
	private static HashMap<String, Integer> programs;

	static {
		System.out.println(GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
		System.out.println(GL11.glGetInteger(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS));
		stateStack = new Stack<RendererState>();
		programs = new HashMap<String, Integer>();
		camera = new Camera(null, 0, 0);
		clip = null;
		color = Color.white;
		programs.put("default", createProgram("default", "default"));
		programs.put("greyscale", createProgram("default", "greyscale"));
		programs.put("lighten", createProgram("default", "lighten"));
		programs.put("paletteSwap", createProgram("betterSwap", "betterSwap"));
	}

	/**
	 * *
	 * Draws the given subtexture at the given coordinates.
	 *
	 * @param t            Texture to be drawn
	 * @param tx0            First texture x coord
	 * @param ty0            First texture y coord
	 * @param tx1            Second texture x coord
	 * @param ty1            Second texture y coord
	 * @param x0            First render x coord
	 * @param y0            First render y coord
	 * @param x1            Second render x coord
	 * @param y1            Second render y coord
	 * @param depth the depth
	 */
	public static void render(Texture t, float tx0, float ty0, float tx1,
			float ty1, float x0, float y0, float x1, float y1, float depth) {
		render(t, tx0, ty0, tx1, ty1, x0, y0, x1, y1, depth, null, new ShaderArgs(), BlendModeArgs.ALPHA_BLEND);
	}
	
	/**
	 * Render.
	 *
	 * @param t the t
	 * @param tx0 the tx0
	 * @param ty0 the ty0
	 * @param tx1 the tx1
	 * @param ty1 the ty1
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param depth the depth
	 * @param transform the transform
	 */
	public static void render(Texture t, float tx0, float ty0, float tx1,
			float ty1, float x0, float y0, float x1, float y1, float depth, Transform transform) {
		render(t, tx0, ty0, tx1, ty1, x0, y0, x1, y1, depth, transform, new ShaderArgs(), BlendModeArgs.ALPHA_BLEND);
	}

	/**
	 * Render.
	 *
	 * @param t the t
	 * @param tx0 the tx0
	 * @param ty0 the ty0
	 * @param tx1 the tx1
	 * @param ty1 the ty1
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param depth the depth
	 * @param transform the transform
	 * @param shader the shader
	 */
	public static void render(Texture t, float tx0, float ty0,
			float tx1, float ty1, float x0, float y0, float x1, float y1,
			float depth, Transform transform, ShaderArgs shader, BlendModeArgs blend) {
		
		blend.apply();
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, SCALE_FILTER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, SCALE_FILTER);
		int program = programs.get(shader.programName);
		ARBShaderObjects.glUseProgramObjectARB(program);
		shader.bindArgs(program);
		t.bind();
		glPushMatrix();
		if(transform != null) {
			Color c = transform.color.multiply(color);
			glColor4f(c.r, c.g, c.b, c.a);
			glTranslatef(x0, y0, depth);
			glTranslatef(transform.translateX, transform.translateY, 0);
			glScalef(transform.scaleX, transform.scaleY, 0);
			glTranslatef(-x0 + (x0 + x1) / 2, -y0 + (y0 + y1) / 2, 0);
			glRotatef(transform.rotation / (float) Math.PI * 180, 0, 0, 1);
			glTranslatef(-(x0 + x1) / 2, -(y0 + y1) / 2, -depth);
	
			// do flip operations
			if (transform.flipHorizontal) {
				float temp = tx0;
				tx0 = tx1;
				tx1 = temp;
			}
	
			if (transform.flipVertical) {
				float temp = ty0;
				ty0 = ty1;
				ty1 = temp;
			}
		} else {
			glColor4f(color.r, color.g, color.b, color.a);
		}
		// Compensation for non power of 2 images
		float txi = tx0*t.getImageWidth()/t.getTextureWidth();
		float tyi = ty0*t.getImageHeight()/t.getTextureHeight();
		float txf = tx1*t.getImageWidth()/t.getTextureWidth();
		float tyf = ty1*t.getImageHeight()/t.getTextureHeight();

		// draw quad
		glBegin(GL_QUADS);
		glTexCoord2f(txi, tyi);
		glVertex3f(x0, y0, depth);
		glTexCoord2f(txf, tyi);
		glVertex3f(x1, y0, depth);
		glTexCoord2f(txf, tyf);
		glVertex3f(x1, y1, depth);
		glTexCoord2f(txi, tyf);
		glVertex3f(x0, y1, depth);
		glEnd();

		glPopMatrix();
		if(clip != null && !clip.persistent) clip.destroy();
		ARBShaderObjects.glUseProgramObjectARB(0);
		
		BlendModeArgs.ALPHA_BLEND.apply();
	}

	/**
	 * Draw square.
	 *
	 * @param x the x
	 * @param y the y
	 * @param s the s
	 * @param depth the depth
	 * @param c the c
	 */
	public static void drawSquare(float x, float y, float s, float depth,
			Color c) {
		drawRectangle(x, y, x + s, y + s, depth, c);
	}

	/**
	 * Draw rectangle.
	 *
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param depth the depth
	 * @param c the c
	 */
	public static void drawRectangle(float x0, float y0, float x1, float y1,
			float depth, Color c) {
		drawRectangle(x0, y0, x1, y1, depth, c, c, c, c);
	}
	
	/**
	 * Draw bordered rectangle.
	 *
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param depth the depth
	 * @param c the c
	 */
	public static void drawBorderedRectangle(float x0, float y0, float x1, float y1, float depth, Color... c){
		for(int i = c.length - 1; i >= 0; i--){
			drawRectangle(x0 - i, y0 - i, x1 + i, y1 + i, depth, c[i]);
		}
	}

	/**
	 * Draw rectangle.
	 *
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param depth the depth
	 * @param c0 the c0
	 * @param c1 the c1
	 * @param c2 the c2
	 * @param c3 the c3
	 */
	public static void drawRectangle(float x0, float y0, float x1, float y1,
			float depth, Color c0, Color c1, Color c2, Color c3) {
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		c0 = c0.multiply(color);
		c1 = c1.multiply(color);
		c2 = c2.multiply(color);
		c3 = c3.multiply(color);
		glColor4f(c0.r, c0.g, c0.b, c0.a);
		glVertex3f(x0, y0, depth);
		glColor4f(c1.r, c1.g, c1.b, c1.a);
		glVertex3f(x1, y0, depth);
		glColor4f(c2.r, c2.g, c2.b, c2.a);
		glVertex3f(x1, y1, depth);
		glColor4f(c3.r, c3.g, c3.b, c3.a);
		glVertex3f(x0, y1, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}

	/**
	 * Draw line.
	 *
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x the x
	 * @param y the y
	 * @param width the width
	 * @param depth the depth
	 * @param c1 the c1
	 * @param c2 the c2
	 */
	public static void drawLine(float x0, float y0, float x, float y,
			float width, float depth, Color c1, Color c2) {
		glDisable(GL_TEXTURE_2D);
		glLineWidth(width);

		// glLoadIdentity();
		glBegin(GL_LINES);
		c1 = c1.multiply(color);
		c2 = c2.multiply(color);
		glColor4f(c1.r, c1.g, c1.b, c1.a);
		glVertex3f(x0, y0, depth);
		glColor4f(c2.r, c2.g, c2.b, c2.a);
		glVertex3f(x, y, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}

	/**
	 * Draw triangle.
	 *
	 * @param x0 the x0
	 * @param y0 the y0
	 * @param x the x
	 * @param y the y
	 * @param x2 the x2
	 * @param y2 the y2
	 * @param depth the depth
	 * @param c the c
	 */
	public static void drawTriangle(float x0, float y0, float x, float y,
			float x2, float y2, float depth, Color c) {
		c.bind();
		glDisable(GL_TEXTURE_2D);
		c = c.multiply(color);
		glColor4f(c.r, c.g, c.b, c.a);
		glBegin(GL_TRIANGLES);
		glVertex3f(x0, y0, depth);
		glVertex3f(x, y, depth);
		glVertex3f(x2, y2, depth);
		glEnd();
		glEnable(GL_TEXTURE_2D);
		if(clip != null && !clip.persistent) clip.destroy();
	}
	
	/**
	 * Draw string.
	 *
	 * @param fontName the font name
	 * @param num the num
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 */
	public static void drawString(String fontName, int num, float x, float y, float depth) {
		drawString(fontName, num+"", x, y, depth);
	}
	
	/**
	 * Draw string.
	 *
	 * @param fontName the font name
	 * @param string the string
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 */
	public static void drawString(String fontName, String string, float x, float y, float depth) {
		drawString(fontName, string, x, y, depth, null);
	}
	
	/**
	 * Draw string.
	 *
	 * @param fontName the font name
	 * @param string the string
	 * @param x the x
	 * @param y the y
	 * @param depth the depth
	 * @param t the t
	 */
	public static void drawString(String fontName, String string, float x, float y, float depth, Transform t) {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, SCALE_FILTER);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, SCALE_FILTER);
		FEResources.getBitmapFont(fontName).render(string, x, y, depth, t);
		if(clip != null && !clip.persistent) clip.destroy();
	}
	
	/**
	 * Translate.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public static void translate(float x, float y) {
		glTranslatef(x, y, 0);
	}
	
	/**
	 * Scale.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public static void scale(float x, float y) {
		glScalef(x, y, 0);
	}
	
	/**
	 * Sets the color.
	 *
	 * @param c the new color
	 */
	public static void setColor(Color c) {
		if(c == null) color = Color.white;
		else color = c;
	}
	
	/**
	 * Push matrix.
	 */
	public static void pushMatrix() {
		glPushMatrix();
		RendererState state = new RendererState();
		state.color = color;
		state.clip = clip;
		stateStack.push(state);
	}
	
	/**
	 * Pop matrix.
	 */
	public static void popMatrix() {
		glPopMatrix();
		RendererState state = stateStack.pop();
		color = state.color;
		clip = state.clip;
		stateStack.push(state);
	}
	
	/**
	 * Sets the camera.
	 *
	 * @param c the new camera
	 */
	public static void setCamera(Camera c) {
		camera = c;
	}

	/**
	 * Gets the camera.
	 *
	 * @return the camera
	 */
	public static Camera getCamera() {
		return camera;
	}
    
    /**
     * Adds the clip.
     *
     * @param f the f
     * @param g the g
     * @param h the h
     * @param i the i
     * @param persistent the persistent
     */
    public static void addClip(float f, float g, float h, float i, boolean persistent) {
    	clip = new RectClip(f, g, h, i, persistent);
    }
    
    /**
     * Removes the clip.
     */
    public static void removeClip() {
    	if(clip != null) clip.destroy();
    }
    
    /**
     * Creates the shader.
     *
     * @param filename the filename
     * @param shaderType the shader type
     * @return the int
     * @throws Exception the exception
     */
    /*
    * With the exception of syntax, setting up vertex and fragment shaders
    * is the same.
    * @param the name and path to the vertex shader
    */
    private static int createShader(String filename, int shaderType) throws Exception {
    	int shader = 0;
    	try {
	        shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
	        
	        if(shader == 0)
	        	return 0;
	        
	        ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
	        ARBShaderObjects.glCompileShaderARB(shader);
	        
	        if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
	            throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
	        
	        return shader;
    	}
    	catch(Exception exc) {
    		ARBShaderObjects.glDeleteObjectARB(shader);
    		throw exc;
    	}
    }
    
    /**
     * Creates the program.
     *
     * @param vertShader the vert shader
     * @param fragShader the frag shader
     * @return the int
     */
    private static int createProgram(String vertShader, String fragShader) {
    	// Create program object
    	int prog = ARBShaderObjects.glCreateProgramObjectARB();
    	// Create vertex shader
    	try {
			int vertexShader = createShader("shaders/"+vertShader+".vert",ARBVertexShader.GL_VERTEX_SHADER_ARB);
			if(vertexShader == 0) throw new Exception();
			ARBShaderObjects.glAttachObjectARB(prog, vertexShader);
		} catch (Exception e) {
			System.err.println("Unable to create vertex shader: "+vertShader);
			e.printStackTrace();
		}
    	// Create fragment shader
    	try {
			int fragmentShader = createShader("shaders/"+fragShader+".frag",ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
			if(fragmentShader == 0) throw new Exception();
			ARBShaderObjects.glAttachObjectARB(prog, fragmentShader);
		} catch (Exception e) {
			System.err.println("Unable to create fragment shader: "+fragShader);
			e.printStackTrace();
		}
    	// Link program
    	ARBShaderObjects.glLinkProgramARB(prog);
	    if (ARBShaderObjects.glGetObjectParameteriARB(prog, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE) {
	        System.err.println(getLogInfo(prog));
	        System.out.println("crap");
	        return -1;
	    }
	    
	    // Use program
	    ARBShaderObjects.glUseProgramObjectARB(prog);
        
	    // Validate program
	    ARBShaderObjects.glValidateProgramARB(prog);
	    if (ARBShaderObjects.glGetObjectParameteriARB(prog, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL_FALSE) {
	    	System.err.println(getLogInfo(prog));
	    	System.out.println("shit");
	    	return -1;
	    }
	    // Bind texture units to uniforms
	    int loc = GL20.glGetUniformLocation(prog, "texture1");
        GL20.glUniform1i(loc, 0);
        int loc2 = GL20.glGetUniformLocation(prog, "texture2");
        GL20.glUniform1i(loc2, 8);
	    
    	return prog;
    }

	/**
	 * Gets the log info.
	 *
	 * @param obj the obj
	 * @return the log info
	 */
	private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, 
        		ARBShaderObjects.glGetObjectParameteriARB(obj,
        				ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
	
    /**
     * I copied this method from the lwjgl website but this code makes me want
     * to kill myself. I might fix later
     *
     * @param filename the filename
     * @return the string
     * @throws Exception the exception
     */
    private static String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();
        InputStream in = ResourceLoader.getResourceAsStream(filename);
        Exception exception = null;
        BufferedReader reader;
        try{
            reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            
            Exception innerExc= null;
            try {
            	String line;
                while((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            }
            catch(Exception exc) {
            	exception = exc;
            }
            finally {
            	try {
            		reader.close();
            	}
            	catch(Exception exc) {
            		if(innerExc == null)
            			innerExc = exc;
            		else
            			exc.printStackTrace();
            	}
            }
            
            if(innerExc != null)
            	throw innerExc;
        }
        catch(Exception exc) {
        	exception = exc;
        }
        finally {
        	try {
        		in.close();
        	}
        	catch(Exception exc) {
        		if(exception == null)
        			exception = exc;
        		else
					exc.printStackTrace();
        	}
        	
        	if(exception != null)
        		throw exception;
        }
        
        return source.toString();
    }
    
    /**
     * Adds the program.
     *
     * @param name the name
     * @param vertShader the vert shader
     * @param fragShader the frag shader
     */
    public static void addProgram(String name, String vertShader, String fragShader) {
    	programs.put(name, createProgram(vertShader, fragShader));
    }
    
    /**
     * The Class RectClip.
     */
    static class RectClip {
    	
	    /** The persistent. */
	    boolean persistent;
    	
	    /**
	     * Instantiates a new rect clip.
	     *
	     * @param x0 the x0
	     * @param y0 the y0
	     * @param w the w
	     * @param h the h
	     * @param p the p
	     */
	    public RectClip(float x0, float y0, float w, float h, boolean p) {
    		persistent = p;
    		glEnable(GL_SCISSOR_TEST);
    		glScissor(
    				Math.round(Game.getScaleX()*(int)x0), 
    				(int)(Game.getWindowHeight()-Math.round(Game.getScaleY()*(y0+h))), 
    				Math.round(Game.getScaleX()*(int)w), 
    				Math.round(Game.getScaleY()*(int)h));
    	}
    	
    	/**
	     * Destroy.
	     */
	    public void destroy() {
    		glDisable(GL_SCISSOR_TEST);
    	}
    }

    /**
     * The Class RendererState.
     */
    static class RendererState {
		
		/** The color. */
		Color color;
		
		/** The clip. */
		RectClip clip;
	}
    
}
