package net.fe.unit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public final class ResourcesJsonTest {
	
	@Test
	public void eachResource_hasAName() throws IOException, ParseException {
		forEachResource(texture -> {
			if (texture.containsKey("name")) {
				if (texture.get("name") instanceof String) {
					return Optional.empty();
				} else {
					return Optional.of("Texture has non-string name: " + texture.get("name"));
				}
			} else {
				return Optional.of("Texture does not have a name");
			}
		});
	}
	
	@Test
	public void eachResource_hasAPath() throws IOException, ParseException {
		forEachResource(texture -> {
			final String declaredName = texture.getOrDefault("name", "???").toString();
			if (texture.containsKey("path")) {
				if (texture.get("path") instanceof String) {
					final java.net.URL file = this.getClass().getClassLoader().getResource((String) texture.get("path"));
					if (null != file) {
						return Optional.empty();
					} else {
						return Optional.of("`textures[" + declaredName + "].path` points to nonextant resource: " + texture.get("path"));
					}
				} else {
					return Optional.of("`textures[" + declaredName + "]` has non-string path: " + texture.get("name"));
				}
			} else {
				return Optional.of("`textures[" + declaredName + "]` does not have a path");
			}
		});
	}
	
	@Test
	public void eachResource_hasReasonableWidth() throws IOException, ParseException {
		forEachResource(texture -> {
			final String declaredName = texture.getOrDefault("name", "???").toString();
			if (texture.containsKey("path") && texture.containsKey("width")) {
				try {
					final long declaredWidth = (long) texture.get("width");
					final String declaredPath = (String) texture.get("path");
					
					long actualWidth;
					try {
						final java.net.URL file = this.getClass().getClassLoader().getResource(declaredPath);
						final java.awt.Image image = javax.imageio.ImageIO.read(file);
						actualWidth = image.getWidth(null);
					} catch (IOException ex) {
						actualWidth = -1;
					}
					
					if (actualWidth % declaredWidth != 0) {
						return Optional.of("textures[" + declaredName + "] has an image width that is not a multiple of the declared frame width"); 
					} else {
						return Optional.empty();
					}
				} catch (ClassCastException ex2) {
					return Optional.of("Either `textures[" + declaredName + "].width` or `textures[" + declaredName + "].path` does not have expected type");
				}
			} else {
				// nothing to check, so ignore
				return Optional.empty();
			}
		});
	}
	
	@Test
	public void eachResource_hasReasonableHeight() throws IOException, ParseException {
		forEachResource(texture -> {
			final String declaredName = texture.getOrDefault("name", "???").toString();
			if (texture.containsKey("path") && texture.containsKey("height")) {
				try {
					final long declaredHeight = (long) texture.get("height");
					final String declaredPath = (String) texture.get("path");
					
					long actualHeight;
					try {
						final java.net.URL file = this.getClass().getClassLoader().getResource(declaredPath);
						final java.awt.Image image = javax.imageio.ImageIO.read(file);
						actualHeight = image.getHeight(null);
					} catch (IOException ex) {
						actualHeight = -1;
					}
					
					if (actualHeight % declaredHeight != 0) {
						return Optional.of("textures[" + declaredName + "] has an image height that is not a multiple of the declared frame height"); 
					} else {
						return Optional.empty();
					}
				} catch (ClassCastException ex2) {
					return Optional.of("Either `textures[" + declaredName + "].height` or `textures[" + declaredName + "].path` does not have expected type");
				}
			} else {
				// nothing to check, so ignore
				return Optional.empty();
			}
		});
	}
	
	@Test
	public void eachResource_hasReasonableFrameCount() throws IOException, ParseException {
		forEachResource(texture -> {
			final String declaredName = texture.getOrDefault("name", "???").toString();
			if (texture.containsKey("path") && texture.containsKey("height") && texture.containsKey("width") && texture.containsKey("frames")) {
				try {
					final long declaredWidth = (long) texture.get("width");
					final long declaredHeight = (long) texture.get("height");
					final long declaredFrames = (long) texture.get("frames");
					final String declaredPath = (String) texture.get("path");
					
					if (! "res/battle_anim/bg_effect_null.png".equals(declaredPath)) {
						long actualHeight;
						long actualWidth;
						try {
							final java.net.URL file = this.getClass().getClassLoader().getResource(declaredPath);
							final java.awt.Image image = javax.imageio.ImageIO.read(file);
							actualWidth = image.getWidth(null);
							actualHeight = image.getHeight(null);
						} catch (IOException ex) {
							actualWidth = -1;
							actualHeight = -1;
						}
						
						final long columns = actualWidth / declaredWidth;
						final long rows = actualHeight / declaredHeight;
						final long maxFrames = columns * rows;
						
						if (maxFrames < declaredFrames) {
							return Optional.of("textures[" + declaredName + "] declares more frames than fit in physical image"); 
						} else {
							return Optional.empty();
						}
					} else {
						// bg_effect_null is fully transparent, thus there's no issue with wrapping or whatever
						return Optional.empty();
					}
				} catch (ClassCastException ex2) {
					return Optional.of("`textures[" + declaredName + "].???` does not have expected type");
				}
			} else {
				// nothing to check, so ignore
				return Optional.empty();
			}
		});
	}
	
	@Test
	public void eachResource_hasCorrectColumnCount() throws IOException, ParseException {
		forEachResource(texture -> {
			final String declaredName = texture.getOrDefault("name", "???").toString();
			if (texture.containsKey("path") && texture.containsKey("width") && texture.containsKey("columns")) {
				try {
					final long declaredWidth = (long) texture.get("width");
					final long declaredColumns = (long) texture.get("columns");
					final long expectedWidth = declaredWidth * declaredColumns;
					final String declaredPath = (String) texture.get("path");
					
					if (! "res/battle_anim/bg_effect_null.png".equals(declaredPath)) {
						long actualWidth;
						try {
							final java.net.URL file = this.getClass().getClassLoader().getResource(declaredPath);
							final java.awt.Image image = javax.imageio.ImageIO.read(file);
							actualWidth = image.getWidth(null);
						} catch (IOException ex) {
							actualWidth = -1;
						}
						
						if (actualWidth != expectedWidth) {
							return Optional.of("`textures[" + declaredName + "].columns * textures[" + declaredName + "].width` does not equal image's physical width"); 
						} else {
							return Optional.empty();
						}
					} else {
						// bg_effect_null is fully transparent, thus there's no issue with wrapping or whatever
						return Optional.empty();
					}
				} catch (ClassCastException ex2) {
					return Optional.of("`textures[" + declaredName + "].???` does not have expected type");
				}
			} else {
				// nothing to check, so ignore
				return Optional.empty();
			}
		});
	}
	
	private void forEachResource(Function<JSONObject, Optional<String>> func) throws IOException, ParseException {
		final ArrayList<String> results = new ArrayList<>();
		
		final java.io.Reader reader = new java.io.InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("res/resources.json"));
		final JSONObject root = (JSONObject) JSONValue.parseWithException(reader);
		reader.close();
		
		final JSONArray textures = (JSONArray) root.get("textures");
		for (Object textureAny : textures) {
			try {
				JSONObject texture = (JSONObject) textureAny;
				Optional<String> result = func.apply(texture);
				result.ifPresent(str -> results.add(str));
				
			} catch (ClassCastException ex) {
				results.add("Subelement of `textures` is not a json object: " + textureAny);
			}
		}
		
		if (! results.isEmpty()) {
			fail(mkString(results, "Problems detected: \n\t", "\n\t", "\n"));
		}
	}
	
	private String mkString(java.util.List<String> elems, String prefix, String delimeter, String suffix) {
		final StringBuilder builder = new StringBuilder();
		boolean isFirst = true;
		builder.append(prefix);
		for (String elem : elems) {
			if (! isFirst) {
				builder.append(delimeter);
			}
			isFirst = false;
			builder.append(elem);
		}
		builder.append(suffix);
		return builder.toString();
	}
}
