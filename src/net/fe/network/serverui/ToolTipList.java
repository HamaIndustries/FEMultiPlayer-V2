package net.fe.network.serverui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.function.Function;

import javax.swing.JList;


/**
 * A JList with easily modifiable tool tips.
 * @author wellme
 *
 * @param <T> The type of the elements contained in this list.
 */
public class ToolTipList<T> extends JList<T> {
	
	private static final long serialVersionUID = 2664812877879740129L;
	private Function<T, String> toolTipFunction;
	

	public String getToolTipText(MouseEvent event) {
		Point p = event.getPoint();
		int index = locationToIndex(p);
		if(index == -1 || toolTipFunction == null)
			return "";
		return toolTipFunction.apply((T)getModel().getElementAt(index));
	}
	/**
	 * Gets the function returning a string representing the tool tip of an element of the list. 
	 * @return The tool tip function.
	 */
	public Function<T, String> getToolTipFunction() {
		return toolTipFunction;
	}
	/**
	 * Sets the function returning a string representing the tool tip of an element of the list. 
	 * @param toolTipFunction the tool tip function.
	 */
	public void setToolTipFunction(Function<T, String> toolTipFunction) {
		this.toolTipFunction = toolTipFunction;
	}

}
