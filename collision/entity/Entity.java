package entity;

import gameStates.State;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Entity {
	protected int id;
	protected State owner;

	public int getId()
	{
		return id;
	}

	public void setOwnerState(State owner)
	{
		this.owner = owner;
	}
	
	public State getOwner()
	{
		return owner;
	}
	
	public abstract void init(GameContainer container, StateBasedGame game);

	public abstract void update(GameContainer gc, StateBasedGame sb, int delta);
	
	public abstract void render(GameContainer gc, StateBasedGame sb, Graphics gr);

}
