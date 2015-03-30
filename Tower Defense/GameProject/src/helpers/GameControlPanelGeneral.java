package helpers;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
public class GameControlPanelGeneral extends JPanel  {
	private final int buttonSize = 90;
	public static final int CONTROLPANELHEIGHT = 150;
	//we have our two control panels.
	JPanel towerControlPanel = new JPanel();
	JPanel generalControlPanel = new JPanel();
	//our tower control panel will have all info pertaining to our tower
	//this includes tower buttons, upgrading, selling, etc
	JButton bSellTower = new JButton("Sell");
	JButton bUpgradeTower = new JButton("Upgrade");
	JLabel lblTowerInfo = new JLabel("Selected Tower: NONE");
	JLabel lblBuildTowerPrompt = new JLabel("							 Build tower:  ");
	ButtonGroup towerGroup = new ButtonGroup();
	JToggleButton bSpread = new JToggleButton("Spread beam");
	JToggleButton bFire = new JToggleButton("Fire beam");
	JToggleButton bIceBeam = new JToggleButton("Ice beam");
	JToggleButton bLaser = new JToggleButton("Laser beam");
	JToggleButton bNone = new JToggleButton("None");
	
	
	//our general control panel will have general info (lives, money)
	//this also includes main menu button, pausing, speeding up game, etc.
	JButton bStartWave = new JButton("Start Wave 1");
	JLabel lblInfo = new JLabel("| Lives = " + ", Money = " + ", Wavenumber =  |");
	JButton bPause = new JButton("Pause");
	JButton bReturn = new JButton("Main Menu");
	JSlider jsSpeed = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
	
	
	
	public GameControlPanelGeneral(){
		//add our buttons to a group so only one can be selected at once.
		towerGroup.add(bSpread);
		towerGroup.add(bFire);
		towerGroup.add(bIceBeam);
		towerGroup.add(bLaser);
		towerGroup.add(bNone);
		bSpread.setPreferredSize(new Dimension(buttonSize, 20));
		bFire.setPreferredSize(new Dimension(buttonSize, 20));
		bIceBeam.setPreferredSize(new Dimension(buttonSize, 20));
		bLaser.setPreferredSize(new Dimension(buttonSize,20));
		bNone.setPreferredSize(new Dimension(50, 20));
		bSpread.setName("Spread");
		bFire.setName("Fire");
		bIceBeam.setName("IceBeam");
		bLaser.setName("Laser");
		bNone.setName("None");
		Font oldFont = lblInfo.getFont();
		lblInfo.setFont(new Font(oldFont.getFontName(), Font.BOLD, oldFont.getSize()));
		//format the slider
		jsSpeed.setMajorTickSpacing(1);
		jsSpeed.setPaintTicks(true);
		jsSpeed.setPaintLabels(true);
		towerControlPanel.add(lblTowerInfo);
		towerControlPanel.add(bUpgradeTower);
		towerControlPanel.add(bSellTower);
		towerControlPanel.add(lblBuildTowerPrompt);
		towerControlPanel.add(bSpread);
		towerControlPanel.add(bFire);
		towerControlPanel.add(bIceBeam);
		towerControlPanel.add(bLaser);
		towerControlPanel.add(bSpread);
		towerControlPanel.add(bNone);
        //add everything to this panel.
		generalControlPanel.add(bStartWave);
		generalControlPanel.add(lblInfo);
		generalControlPanel.add(jsSpeed);
		generalControlPanel.add(bPause);
		generalControlPanel.add(bReturn);
        
		//add our two panels to the main panel
		this.add(towerControlPanel);
		this.add(generalControlPanel);
		this.setSize(Artist_Swing.PIXELWIDTH, CONTROLPANELHEIGHT);
		this.validate();
	}
	public JButton getSellButton(){
		return this.bSellTower;
	}
	
	public JButton getUpgradeButton(){
		return this.bUpgradeTower;
	}
	public static int getControlPanelHeight(){
		return CONTROLPANELHEIGHT;
	}
	public JLabel getInfoLabel(){
		return lblInfo;
	}
	public void setTowerInfoLabelText(String text){
		lblTowerInfo.setText(text);
	}
	public void setInfoLabelText(String text){
		lblInfo.setText(text);
	}
	
	public JButton getPauseButton(){
		return bPause;
	}
	public JButton getReturnButton(){
		return bReturn;
	}
	public JButton getStartWaveButton(){
		return bStartWave;
	}
	public JToggleButton getSpreadButton(){
		return bSpread;
	}
	public JToggleButton getFireButton(){
		return bFire;
	}
	public JToggleButton getIceButton(){
		return bIceBeam;
	}
	public JToggleButton getLaserButton(){
		return bLaser;
	}
	public JToggleButton getNoneButton(){
		return bNone;
	}
	public JSlider getSpeedSlider(){
		return jsSpeed;
	}


}
