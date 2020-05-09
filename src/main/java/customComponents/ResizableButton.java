package customComponents;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Component;

import javax.swing.JButton;

public class ResizableButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResizableButton() {
		super();
		super.setFocusPainted(false);
	}
	
	public ResizableButton(String arg0) {
		super();
		super.setText(arg0);
		super.setFocusPainted(false);
	}
	
	@Override
	public void setText(String arg0) {
	    super.setText(arg0);
	    FontMetrics metrics = getFontMetrics(getFont()); 
	    int width = metrics.stringWidth( getText() );
	    int height = metrics.getHeight();
	    Dimension newDimension =  new Dimension(width+40,height+10);
	    setPreferredSize(newDimension);
	    setBounds(new Rectangle(getLocation(), getPreferredSize()));
	}

}
