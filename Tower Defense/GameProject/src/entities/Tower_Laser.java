package entities;

import java.awt.Color;
import java.util.ArrayList;

public class Tower_Laser extends Tower{

	public Tower_Laser(String name, Point p, int size, ArrayList<Critter> crittersOnMap) {
		super(name,p, size, crittersOnMap);
		//these variables are all explicitly written as all laser towers will have the same starting stats
		//all values are place holders
		damage = 0.5;
		rateOfFire = 5;
		range = 300;
		sellPrice = 10;
		upCost = 10;
		slowFactor = 0;
		damageOverTime = false;
		areaOfAffect = false;
		tColor = new Color(150,150,150);
		buyCost = 100;
	}

	
}
