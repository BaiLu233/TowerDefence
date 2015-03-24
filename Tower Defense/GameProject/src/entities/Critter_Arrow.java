package entities;

import helpers.EntityColor;

/*
 * Arrow critter is fast but weak compared to other critters
 */
public class Critter_Arrow extends Critter{
	public Critter_Arrow(int level, TDMap m) {
		super(level, m);
		//average reward
		reward = (int) (10*levelMultiplier);
		//low hitpoints
		currHitPoints = (30*levelMultiplier);
		maxHitPoints = currHitPoints;
		//does not regenerate health
		regen = 0;
		//fast
		speed = (int) (15*levelMultiplier);
		//does not resist effects
		resistance = 0;
		//set name
		name = "Arrow Critter";
		strength = 1;
		color = "Blue";
	}
}
