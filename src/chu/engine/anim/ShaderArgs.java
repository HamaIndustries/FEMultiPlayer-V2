package chu.engine.anim;

import org.lwjgl.opengl.GL20;

// TODO: Auto-generated Javadoc
/**
 * The Class ShaderArgs.
 */
public class ShaderArgs {

	/** The program name. */
	public String programName;

	/** The args. */
	public float[] args;

	/**
	 * Instantiates a new shader args.
	 *
	 * @param prog the prog
	 * @param args the args
	 */
	public ShaderArgs(String prog, float... args) {
		this.programName = prog;
		this.args = args;
	}

	/**
	 * Instantiates a new shader args.
	 */
	public ShaderArgs() {
		this.programName = "default";
		args = new float[0];
	}

	/**
	 * Bind args.
	 *
	 * @param program the program
	 */
	public void bindArgs(int program) {
		for (int i = 0; i < args.length; i++) {
			int loc = GL20.glGetUniformLocation(program, "arg" + i);
			GL20.glUniform1f(loc, args[i]);
		}
	}
}
