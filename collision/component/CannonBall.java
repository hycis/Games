package component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import collisionGame.Constant;

public class CannonBall {

	public Vector2f position;
	public Vector2f velocity;
	public Vector2f acc;
	
	public Circle circle;
	
	public boolean collided;
	
	public CannonBall(Vector2f p, Vector2f v)
	{
		this.position = new Vector2f(p);
		this.velocity = new Vector2f(v);
		this.acc = new Vector2f(Constant.WIND.x, Constant.GRAVITY.y);
		this.circle = new Circle(p.x, p.y, Constant.RADIUS);
		this.collided = false;
	}
	
	public void nextPosition()
	{
		this.position.x += velocity.x * Constant.TIMESTEP + 0.5 * acc.x * Math.pow(Constant.TIMESTEP, 2);
		this.position.y += velocity.y * Constant.TIMESTEP + 0.5 * acc.y * Math.pow(Constant.TIMESTEP, 2);
		velocity.x += acc.x * Constant.TIMESTEP;
		velocity.y += acc.y * Constant.TIMESTEP;
		this.circle.setLocation(position);
	}
	
	// unfinished, still need to check with water terrain
		public boolean isDead()
		{
			float maxX = this.circle.getMaxX();
			float minX = this.circle.getMinX();
			float maxY = this.circle.getMaxY();
			float minY = this.circle.getMinY();
			
			if (maxX < 0 || minX > Constant.DISPLAY_WIDTH
				|| minY > Constant.DISPLAY_HEIGHT || maxY < 0)
				return true;
			
			// check with water terrain
			
			return false;
		}
	
	public void setVelocity(Vector2f newVelocity)
	{
		this.velocity.set(newVelocity);
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		// TODO Auto-generated method stub
		gr.draw(this.circle);
	}

}
