package views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

public class MapPanel extends JPanel {
    
	public MapPanel(){
		
		//set panel properties
		setBackground(Color.BLACK);
        setPreferredSize(new Dimension(GameApplicationFrame.PIXELWIDTH , GameApplicationFrame.PIXELHEIGHT- GameControlPanel.getControlPanelHeight()));
        setDoubleBuffered(true);
        setVisible(true);
        setFocusable(true);
        requestFocus();
	}

}
