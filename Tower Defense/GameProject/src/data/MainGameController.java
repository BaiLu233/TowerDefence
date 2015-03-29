package data;
import helpers.GameActivity;
import helpers.CritterGenerator;
import helpers.GameControlPanel;
import helpers.GamePlayPanel;
import helpers.GameClock;
import helpers.MainMenuActivity;
import helpers.MouseHandler;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import entities.*;

import java.util.ArrayList;

import javax.swing.Icon;

public class MainGameController extends GamePlayPanel implements ActionListener, IObserver {
		
	//declare game specific variables
	protected GamePlayPanel gamePanel;
	protected GameControlPanel controlPanel;
	protected GameActivity activity;
	
	//declare frame specific variables
	private Timer timer;
	private int lives, money, waveStartLives, waveStartMoney;
	private int waveNumber;
	private int activeCritterIndex;
	ArrayList<DrawableEntity> drawableEntities;
	private TDMap tdMap;
	ArrayList<Critter> crittersInWave;
	ArrayList<Tower> towersOnMap;
	private boolean gamePaused;

	private boolean gameOver;
	JFrame mainFrame;
	private JButton bPause;
	private JButton bReturn;
	private JButton bStartWave;
	private JButton bBomb;
	private JButton bFire;
	private JButton bIceBeam;
	private JButton bLaser;
	private String selectedTower;
	
	Tower tf1, tf2, tf3;
	ArrayList<Subject> subjects;
	public MainGameController(TDMap map)
	{
		setPanelAndButtonProperties();
		setInitialValues();	
		this.tdMap = map;
		//add the map back into the drawable entities
		drawableEntities.add(tdMap);
		
		//start the timer
		timer = new Timer(GameActivity.TIMEOUT,this);
		timer.start();
		
		//initialize arraylists
		updateInfoLabelText();
	/*
		//create a couple towers and add them to the drawableEntitites.
		tf1 = new Tower_IceBeam("tf1", tdMap.getPosOfBlock_pixel(5, 1), tdMap.xBlock, crittersInWave);
		tf2 = new Tower_Laser("tf2", tdMap.getPosOfBlock_pixel(25, 1), tdMap.xBlock, crittersInWave);
		tf3 = new Tower_Laser("tf3", tdMap.getPosOfBlock_pixel(15, 1), tdMap.xBlock, crittersInWave);
		towersOnMap.add(tf1);
		towersOnMap.add(tf2);
		towersOnMap.add(tf3);	
		*/
		MouseHandler handler = new MouseHandler(this);
		gamePanel.addMouseListener(handler);
	}
	public void setPanelAndButtonProperties(){
		//create Field pointer defined in controller
		gamePanel = this;
		controlPanel = new GameControlPanel();
		bPause = this.getControlPanel().getPauseButton();
		bPause.addActionListener(this);
		bReturn = this.getControlPanel().getReturnButton();
		bReturn.addActionListener(this);
		bStartWave = this.getControlPanel().getStartWaveButton();
		bStartWave.addActionListener(this);
		bBomb = this.getControlPanel().getBombButton();
		bBomb.addActionListener(this);
		Icon bombTowerIcon = new ImageIcon("BombTowerIcon.png");
		bBomb.setIcon(bombTowerIcon);
		bFire = this.getControlPanel().getFireButton();
		bFire.addActionListener(this);
		bLaser = this.getControlPanel().getLaserButton();
		bLaser.addActionListener(this);
		bIceBeam = this.getControlPanel().getIceButton();
		bIceBeam.addActionListener(this);
	}
	public void setInitialValues(){
		GameClock.getInstance().pause();
		gamePaused = true;
		gameOver = false;
		subjects = new ArrayList<Subject>();
		drawableEntities = new ArrayList<DrawableEntity>();
		towersOnMap = new ArrayList<Tower>();
		waveNumber = 0;
		lives = 10;
		waveStartLives = lives;
		money = 2000;
		waveStartMoney = money;
		selectedTower = "Bomb";
		bBomb.getModel().setPressed(true);
	}
	public void setMainFrame(JFrame mFrame){
		mainFrame = mFrame;
	}
	private void startNewWave(){
		//increment the wave number
		waveNumber +=1;
		bStartWave.setText("Start Wave " + (waveNumber+1));
		this.updateInfoLabelText();
		//reset the active critter index
		activeCritterIndex = 0;
		//record the amount of money they have as the start wave money (and same for lives)
		waveStartMoney = money;
		waveStartLives = lives;
		//remove all current entities
		drawableEntities.clear();
		//remove all current subjects
		subjects.clear();

		drawableEntities.add(tdMap);
		
		//get the critters for this wave level
		crittersInWave = CritterGenerator.getGeneratedCritterWave(waveNumber, tdMap);
		for(int i = 0; i < crittersInWave.size(); i++){
			//add them to the drawableEntitiesList
			drawableEntities.add(crittersInWave.get(i));
			crittersInWave.get(i).addObserver(this); //makes this an observer of critter
			subjects.add(crittersInWave.get(i));
		}
		//add all of the towers back into the drawable entities
		for(Tower t : towersOnMap){
			drawableEntities.add(t);
			t.setCrittersOnMap(crittersInWave);
		}
	}

	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//redraw the snake and the food
		//update and draw all drawableEntities.
		for(int i = 0; i < drawableEntities.size(); i++){
			drawableEntities.get(i).updateAndDraw(g);
		}
        Toolkit.getDefaultToolkit().sync();

	}



	//This method is called every time the timer times out
	//The basic Game loop
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
			if(arg0.getSource() == bPause && !gameOver){
				if(gamePaused == false){
					GameClock.getInstance().pause();
					gamePaused = true;
					bPause.setText("Play");
				}else{
					GameClock.getInstance().unPause();
					gamePaused =false;
					bPause.setText("Pause");
				}
			}else if(arg0.getSource() == bReturn){
				mainFrame.dispose();
				new MainMenuActivity();
			}else if(arg0.getSource() == bStartWave && !gameOver){
				GameClock.getInstance().unPause();
				gamePaused =false;
				bStartWave.setEnabled(false);
				startNewWave();
			}else if(arg0.getSource() == bFire || arg0.getSource() == bLaser || arg0.getSource() == bIceBeam || arg0.getSource() == bBomb){
				clearTowerButtonSelection();
				selectTowerButton((JButton) arg0.getSource());
				selectedTower = ((JButton) arg0.getSource()).getName();
			}else if(!gameOver){
				if(gamePaused == false){
					if(activeCritterIndex == 0){
						crittersInWave.get(activeCritterIndex).setActive(true);
						activeCritterIndex +=1;
					}else if(activeCritterIndex < crittersInWave.size()){
						if(crittersInWave.get(activeCritterIndex-1).getPixelPosition().getX() > 50 || crittersInWave.get(activeCritterIndex-1).isAlive()==false){
							crittersInWave.get(activeCritterIndex).setActive(true);
							activeCritterIndex +=1;
						}
					}
				}
				Draw();
			}
			
		
	}
	
	public void Draw(){
		//calls the paintComponent function
		gamePanel.repaint();
	}
	//one of my subjects has changed. Go through them all and check stats.
	public void observerUpdate(){
		if(gamePaused ==false){
			resetPlayerStats();
			boolean anyCrittersLeft = false;
			for(Subject s : subjects){
				if(s instanceof Critter){
					//it is a critter. Check to see if it is dead or if it has reached the end.
					Critter c = (Critter) s;
					//if it is dead, give the appropriate money to the player
					if(c.isAlive() && c.hasReachedEnd()==false){
						anyCrittersLeft = true;
					}else if(c.isAlive()==false){
						money += c.getLoot();
					}else if(c.hasReachedEnd()==true){//if it has reached the end, take away a life
						this.lives -=1;
						if(lives==0){
							endGame();
						}
					}
				}
			}
			//if no critters are left, start a new wave
			if(anyCrittersLeft == false){
				bStartWave.setEnabled(true);

				//startNewWave();
			}
			updateInfoLabelText();
		}
	}
	private void clearTowerButtonSelection(){
		bFire.getModel().setPressed(false);
		bIceBeam.getModel().setPressed(false);
		bLaser.getModel().setPressed(false);
		bBomb.getModel().setPressed(false);
	}
	private void selectTowerButton(JButton selectionButton){
		selectionButton.getModel().setPressed(true);
	}
	
	private void endGame(){
		gameOver = true;
		gamePaused =true;
		this.getControlPanel().setInfoLabelText("GAME OVER. You reached wave: " + waveNumber + " with $" + money);
		GameClock.getInstance().pause();
	}
	private void resetPlayerStats() {
		lives = waveStartLives;
		money = waveStartMoney;
		
	}

	public GameControlPanel getControlPanel(){
		return controlPanel;
	}
	public GamePlayPanel getPlayPanel(){
		return gamePanel;
	}
	public void updateInfoLabelText(){
		if(gamePaused ==false){
			this.getControlPanel().setInfoLabelText("Lives = " + lives + ", Money = " + money + ", Wavenumber = " + waveNumber);
		}
	}
	
	public void spendMoney(int amount){
		money = money - amount;
		updateInfoLabelText();
	}
	public void tryToBuildTower(Point point){
		//check which tower we want to place
		if(selectedTower.equalsIgnoreCase("Bomb")){
			if(this.money > Tower_Bomb.getBuyPrice()){
				spendMoney(Tower_Bomb.getBuyPrice());
				buildTower(new Tower_Bomb("Bomb", point, tdMap.xBlock, crittersInWave));
			}else{
				alertUserInsufficientFundsForBuying();
			}
		}else if(selectedTower.equalsIgnoreCase("Fire")){
			if(this.money > Tower_Fire.getBuyPrice()){
				spendMoney(Tower_Fire.getBuyPrice());
				buildTower(new Tower_Fire("Fire", point, tdMap.xBlock, crittersInWave));
			}else{
				alertUserInsufficientFundsForBuying();
			}
		}else if(selectedTower.equalsIgnoreCase("IceBeam")){
			if(this.money > Tower_IceBeam.getBuyPrice()){
				spendMoney(Tower_IceBeam.getBuyPrice());
				buildTower(new Tower_IceBeam("IceBeam", point, tdMap.xBlock, crittersInWave));
			}else{
				alertUserInsufficientFundsForBuying();
			}
		}else if(selectedTower.equalsIgnoreCase("Laser")){
			if(this.money > Tower_Laser.getBuyPrice()){
				spendMoney(Tower_Laser.getBuyPrice());
				buildTower(new Tower_Laser("Laser", point, tdMap.xBlock, crittersInWave));
			}else{
				alertUserInsufficientFundsForBuying();
			}
		}else{
			System.out.println("Error: No appropriate tower type (coding error)");
		}
			
	}
	public void buildTower(Tower t){
		towersOnMap.add(t);
		drawableEntities.add(t);
	}
	public void alertUserInsufficientFundsForBuying(){
		System.out.println("The " + money + " dollars that you have is not enough for the " + selectedTower + " tower.");
	}
	
}
