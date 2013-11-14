package gameStates;

import org.newdawn.slick.state.BasicGameState;

import entity.Entity;

public abstract class State extends BasicGameState{

	protected int id;
	public abstract void addEntity(Entity entity);
	public abstract Entity getEntity(int id);
	
	@Override
	public int getID()
	{
		return id;
	}
	
}
