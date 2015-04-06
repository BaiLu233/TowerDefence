package models;
import helpers.Artist_Swing;
import helpers.GameClock;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/*
 * Critter abstract class from which all critters extend. Has certain attributes and methods including 
 * taking a step, getting damaged, etc.
 */
public abstract class Critter extends Subject implements DrawableEntity {
	//Constants
	public static final int MAXWAVENUMBER = 50;
	//attributes of the critter
	//tangible properties of critter
	protected double currHitPoints;
	protected double maxHitPoints;
	protected double speed;
	protected int size;
	protected double regen;
	protected double resistance;
	protected Color cColor;
	
	//intangible properties
	protected int reward;
	protected int level;
	protected double levelMultiplier;
	protected String name;
	protected double slowFactor;
	protected int slowTime;
	protected int beenSlowedFor;
	private double damageOverTimeVal;
	private int dotTime;
	private int beenDOTFor;
	private boolean burning;
	
	//state properties
	protected Point _pixelPosition;
	protected boolean active;
	protected boolean alive;
	protected boolean reachedEnd;
	protected ArrayList<Point> pixelPathToFollow;
	//protected ArrayList<Point> newPixelPathToFollow;
	protected double indexInPixelPath;
	protected int intIndexInPixelPath;

	
	 
	//constructor
	public Critter(int level, TDMap m){
		//set the level from input
		this.level = level;
		//set the default values for all critters
		levelMultiplier = gameLevel();
		reachedEnd = false; //none have reached end to start
		active = false; //none are active to start
		alive = true;
		pixelPathToFollow = m.getPath_ListOfPixels();
		indexInPixelPath = 0; //all start at beginning of path
		size = 6; //this can be changed...
		this._pixelPosition = new Point(-1,-1);
		slowFactor = 0;
		slowTime = 0;
		beenSlowedFor = 0;
		damageOverTimeVal = 0;
		dotTime = 0;
		beenDOTFor = 0;
		burning =false;
	}
	
	//getters and setters
	public double getIndexInPixelPath(){
		return this.indexInPixelPath;
	}
	public ArrayList<Point> getListPixelPath(){
		return this.pixelPathToFollow;
	}
	/*public ArrayList<Point> getNewListPixelPath(){
		return this.newPixelPathToFollow;
	}*/
	public void setSlowFactor(double slowFactor){
		if(this.slowFactor < slowFactor){
			this.slowFactor = slowFactor;
		}
		beenSlowedFor = 0;
	}
	private void setDOTAmount(double dot) {
		if(this.damageOverTimeVal < dot){
			this.damageOverTimeVal = dot;
		}
		beenDOTFor = 0;
	}
	
	public Color getColor(){
		return cColor;
	}
	
	
	public void setlevelMultiplier(double levelMultiplier){
		this.levelMultiplier = levelMultiplier;
	}
	
	public Point getPixelPosition(){
		return _pixelPosition;
	}
	public boolean hasReachedEnd(){
		return reachedEnd;
	}
	public boolean isAlive(){
		return alive;
	}
	public boolean isBurning() {
		return burning;
	}
	public int getSize(){
		return size;
	}
	public int getLoot(){
		return reward;
	}
	public void setHitboxRadius(int size){
		this.size = size;
	}
	public double getHitPoints() {
		return currHitPoints;
	}
	public void setHitPoints(double hitPoints) {
		this.currHitPoints = hitPoints;
	}
	public double getMaxHitPoints(){
		return maxHitPoints;
	}
	public double getRegen() {
		return regen;
	}
	public void setRegen(double regen) {
		this.regen = regen;
	}
	public double getRawSpeed() {
		return speed;
	}
	public void setRawSpeed(int speed) {
		this.speed = speed;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isActive(){
		return active;
	}
	public void setActive(boolean act){
		active = act;
	}
	public double getSpeed(){
		return speed;
	}
	//END OF Getters and Setters
	
	public double gameLevel(){
		
			double i =1 + 1*((double)(level -1))/((double)MAXWAVENUMBER);
			
			if (level > 20)
			{
				 i =1 + (level+1)*((double)(level -1))/((double)MAXWAVENUMBER);
			}
			
			if (level > 10)
			{
				 i =1 + 2*(level+1)*((double)(level -1))/((double)MAXWAVENUMBER);
			}
			
			if (level > 5)
			{
				 i =1 + (level+1)*((double)(level -1))/((double)MAXWAVENUMBER);
			}
			return i;
			
	}
	/*
	 * @see entities.DrawableEntity#updateAndDraw()
	 * This must have all properties of the critter that change with time
	 * These properties are its position and its health.
	 */

	public void updateAndDraw(Graphics g){
		if(this.isActive()){
			//do slowing
			if(beenSlowedFor < this.slowTime){
				beenSlowedFor +=1*GameClock.getInstance().deltaTime();
			}else{
				slowFactor = 0;
			}
			//do damages over time
			if(beenDOTFor < this.dotTime){
				beenDOTFor +=1*GameClock.getInstance().deltaTime();
				burning = true;
			}else{
				damageOverTimeVal = 0;
				burning = false;
			}
			
			updateHealth();
			updatePositionAndDraw(g);
		}
	}
	/*
	 * updates the health of the critter (called on every "tick" of the clock)
	 */
	private void updateHealth(){
		int timePassed = GameClock.getInstance().deltaTime();
		double dotPerTime = damageOverTimeVal*timePassed;
		//simply update the hitpoints. This should be called every update instance.
		if(this.currHitPoints + this.regen*timePassed - dotPerTime <=0){
			this.currHitPoints = 0;
			this.active = false;
			this.alive = false;
			this.notifyObs();
			//if our regen will not push us over our limit, simply regen
		}else if(this.currHitPoints + this.regen*timePassed - dotPerTime < this.maxHitPoints){
			this.currHitPoints = this.currHitPoints + this.regen*timePassed - dotPerTime;	
		}else{
			//otherwise just set us to the max regen value.
			this.currHitPoints = this.maxHitPoints;
		}
		
	}
	/*
	 * updates the position (and draws it), called on every tick of clock
	 */
	private void updatePositionAndDraw(Graphics g){
		//if we haven't yet moved, 
		if(indexInPixelPath == 0){
			//place us on the map at the initial position.
			_pixelPosition = pixelPathToFollow.get(0);
		}
		
		//the next index is our current index + our speed*our clock
		indexInPixelPath += (1.0-slowFactor)*this.speed*GameClock.getInstance().deltaTime(); //synced with time
		int indexToMoveTo = (int) indexInPixelPath;
		//If we aren't going to pass the end, we move our critter.
		if(indexInPixelPath < pixelPathToFollow.size()){
			moveAndDrawCritter(indexToMoveTo, g);
		}else{
			//we have reached the end
			reachedEnd = true; 
			//this critter is no longer active
			active = false;
			//notify our observers.
			this.notifyObs();
		}
	}
	/*
	 * Moves the critter to a given position and draws it as it moves.
	 */
	private void moveAndDrawCritter(int index, Graphics g){
		if(intIndexInPixelPath == index){
			this.drawCritter(g);
		}else{
			while(intIndexInPixelPath<index){
				intIndexInPixelPath +=1;
				this._pixelPosition.setPoint(this.pixelPathToFollow.get(intIndexInPixelPath).getX(), this.pixelPathToFollow.get(intIndexInPixelPath).getY());
				this.drawCritter(g);
			}
		}
	}
	
	private void drawCritter(Graphics g) {
		//System.out.println("Just tried to draw a critter at " + this._pixelPosition.toString());
		Artist_Swing.drawCritter(this,g);
    }
	
	//Damages the critter for a certain amount (Will likely be removed in final version)
	public void damage(double dam){
		if(this.currHitPoints - dam > 0){
			this.currHitPoints -= dam;
		}else{
			this.currHitPoints = 0;
			this.active = false;
			this.alive = false;
			this.notifyObs();
		}
	}
	public void slowCritter(double sFactor, int sTime){
		this.setSlowFactor(sFactor);
		this.slowTime = sTime;
	}

	public void damageOverTimeCritter(double dot, int damageOverTimeLength) {
		this.setDOTAmount(dot);
		this.dotTime = damageOverTimeLength;
		
	}
	
	//ToString
	public String toString(){
		String result = "";
		result += "\nHP: " + currHitPoints + "/" + maxHitPoints + "\n";
		result += "Regen = " + this.regen + "\n";
		return result;
	}
	
	
	}

