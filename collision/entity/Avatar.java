package entity;

import java.awt.Point;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import collisionGame.Constant;

import component.CannonBall;
import component.StickMan;

public class Avatar extends Entity{

	StickMan stickman;
	float x;
	float y;

	PerlinLandscape landscape;
	CannonBallSystem cannonBallSystem;
	Water water;

	public Avatar() throws SlickException
	{
		this.id = 3;
		stickman = new StickMan();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		// TODO Auto-generated method stub
	}

	/**
	 * update the motion of the avatar everything it's called
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
	
		if (owner.getEntity(2) != null)
			cannonBallSystem = (CannonBallSystem) owner.getEntity(2);
		
		if (owner.getEntity(4) != null)
		{
			water = (Water) owner.getEntity(4);
			//System.out.println("waterVol" + water.waterpoints.size());
		}
		
		Input input = gc.getInput();
		
		if (input.isKeyDown(Input.KEY_DOWN))
		{
			stickman.vel.set(0,0);
		}

		if (input.isKeyDown(Input.KEY_RIGHT))
		{
			nextRightPosition(sb, delta);
		}

		if (input.isKeyDown(Input.KEY_LEFT))
		{
			nextLeftPosition(sb, delta);
		}

		if (input.isKeyDown(Input.KEY_SPACE))
		{
			jump(sb);
		}
		nextPosition(sb, Constant.TIMESTEP);
	}

	/**
	 * render the avatar on the screen
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		// TODO Auto-generated method stub
		gr.drawImage(stickman.image, stickman.pos.x, stickman.pos.y);
	}

	/**
	 * Generate the next position of the avatar when it's not under key control
	 * @param sb the game state
	 * @param timeStep the timeStep which determine it's next position
	 */
	public void nextPosition(StateBasedGame sb, float timeStep) 
	{
		Vector2f acc = new Vector2f(Constant.WIND.x + Constant.WEIGHT.x,
				Constant.WIND.y + Constant.WEIGHT.y);
		stickman.acc.set(acc);

		stickman.updatePosition(timeStep);
		
		if (owner.getEntity(1) == null)
			return;
		landscape = (PerlinLandscape) owner.getEntity(1);

		float minX = stickman.minX();
		float maxX = stickman.maxX();
		
		if (maxX >= Constant.DISPLAY_WIDTH-10) 
		{
			Constant.LEVEL++;
			Constant.SEED++;
			sb.enterState(2);
		}

		else if (minX <= 10)
		{
			Constant.LEVEL--;
			Constant.SEED--;
			sb.enterState(2);
		}
		if (hitByCannon() || drown())
			sb.enterState(4);

		float numOfSeg = (float) Math.pow(2, landscape.octave);
		float width = Constant.DISPLAY_WIDTH / numOfSeg;

		int leftIndex = (int) Math.floor(minX / width);
		int rightIndex = (int) Math.ceil(maxX / width);

		while (leftIndex < rightIndex)
		{			
			int x1 = landscape.points[leftIndex].x;
			int xn = landscape.points[leftIndex+1].x;
			int y1 = landscape.points[leftIndex].y;
			int yn = landscape.points[leftIndex+1].y;

			float cenX = stickman.cenX();
			float cenY = stickman.cenY();

			float e = (float) 0.05;
			for (float xk=x1; xk<xn; xk+=e)
			{
				float yk = y1 - (x1-xk)*(y1-yn)/(x1-xn);
				float lenSqr = lenSqr(xk, yk, cenX,cenY);
				if (lenSqr <= Math.pow(Constant.HALF_HEIGHT, 2) + 50)
				{

					Vector2f r = new Vector2f(yn-y1, x1-xn);

					Vector2f unitR = r.normalise();

					// magnitude of velocity perpendicular to the vector connecting the centers
					float VPerLen = crossProductLength(stickman.vel, unitR);
					// find the perpendicular velocity vector
					Vector2f Vper = perpenV(unitR, stickman.vel);
					// find unit perpendicular velocity vector
					Vector2f unitVper = Vper.normalise();
					// perpendicular vector to unitR
					Vector2f per = new Vector2f(unitVper.x * VPerLen, unitVper.y * VPerLen);

					stickman.vel.set(per);

					Vector2f x1ToO = new Vector2f(cenX-x1, cenY-y1);
					Vector2f X1ToXn = new Vector2f(xn-x1, yn-y1);

					float length = crossProductLength(x1ToO, X1ToXn.normalise());;
					if (length <= Constant.HALF_HEIGHT)
					{
						float rel = Constant.HALF_HEIGHT - length;						
						Vector2f dp = new Vector2f(rel*unitR.x, rel*unitR.y);
						stickman.pos.set(stickman.pos.add(dp));
					}
					return;
				}
			} //end for
			leftIndex++;
		} //end while
	}

	/**
	 * Determine if the avatar is hit by the cannon
	 * @return true if hit, false if not
	 */
	public boolean hitByCannon()
	{
		if (owner.getEntity(2) == null)
			return false;
		
		cannonBallSystem = (CannonBallSystem) owner.getEntity(2);
		
		for (CannonBall cb : cannonBallSystem.cannonBallSystem)
		{
			float x1 = cb.circle.getCenterX();
			float y1 = cb.circle.getCenterY();
			float x2 = stickman.cenX();
			float y2 = stickman.cenY();
			
			if (Math.pow(x1-x2,2)+Math.pow(y1-y2,2) 
					< Math.pow(Constant.RADIUS+Constant.HALF_HEIGHT, 2))
				return true;
		}
		return false;
	}
	
	/**
	 * Check if the avatar is drown
	 * @return
	 */
	public boolean drown()
	{
		float x = stickman.cenX();
		float y = stickman.maxY();
		
		//---------Insert for WaterWorld---------
		if (owner.getID() == 3)
		{
			if (x > Constant.PITLEFT.x && x < Constant.PITRIGHT.x && y > Constant.PITLEFT.y)
				return true;
			return false;
		}
		//---------------------------------------
		
		if (owner.getEntity(4) == null)
		{
			return false;
		}
		
		water = (Water) owner.getEntity(4);
		
		if (water.waterpoints == null )
		{
			return false;
		}
		
		if( water.waterpoints.size() == 0)
		{
			return false;
		}
				
		boolean connect = true;		
		Point prev = water.waterpoints.pop();
		while (water.waterpoints.size() != 0)
		{
			if (connect)
			{
				if (y > prev.y && x < prev.x && x > water.waterpoints.peek().x)
				{
					return true;
				}
				connect = false;
			}
			else 
				connect = true;
			prev = water.waterpoints.pop();
		}
		return false;
	}

	/** Find the next position after the RIGHT_KEY is pressed
	 * 
	 * @param delta the time interval between two successive frames
	 * @param sb the state of the game
	 */
	public void nextRightPosition(StateBasedGame sb, int delta) 
	{
		float timeStep = delta * Constant.DELTA_FACTOR;

		float minX = stickman.minX();
		float maxX = stickman.maxX();

		if (maxX >= Constant.DISPLAY_WIDTH-10) 
		{
			Constant.LEVEL++;
			Constant.SEED++;
			sb.enterState(2);
		}

		else if (minX <= 10)
		{
			Constant.LEVEL--;
			Constant.SEED--;
			sb.enterState(2);
		}

		if (owner.getEntity(1) == null)
			return;
		landscape = (PerlinLandscape) owner.getEntity(1);

		float numOfSeg = (float) Math.pow(2, landscape.octave);
		float width = Constant.DISPLAY_WIDTH / numOfSeg;

		int leftIndex = (int) Math.floor(minX / width);
		int rightIndex = (int) Math.ceil(maxX / width);

		while (leftIndex < rightIndex)
		{			
			int x1 = landscape.points[leftIndex].x;
			int xn = landscape.points[leftIndex+1].x;
			int y1 = landscape.points[leftIndex].y;
			int yn = landscape.points[leftIndex+1].y;

			float cenX = stickman.cenX();
			float cenY = stickman.cenY();

			float e = (float) 0.05;
			for (float xk=x1; xk<xn; xk+=e)
			{
				float yk = y1 - (x1-xk)*(y1-yn)/(x1-xn);
				float lenSqr = lenSqr(xk, yk, cenX,cenY);
				if (lenSqr <= Math.pow(Constant.HALF_HEIGHT, 2) + 50)
				{
					Vector2f r = new Vector2f(yn-y1, x1-xn);

					Vector2f unitR = r.normalise();
					Vector2f x1ToO = new Vector2f(cenX-x1, cenY-y1);
					Vector2f X1ToXn = new Vector2f(xn-x1, yn-y1);

					float length = crossProductLength(x1ToO, X1ToXn.normalise());;
					if (length <= Constant.HALF_HEIGHT)
					{
						float rel = Constant.HALF_HEIGHT - length;						
						Vector2f dp = new Vector2f(rel*unitR.x, rel*unitR.y);
						stickman.pos.set(stickman.pos.add(dp));
					}
					
					if (xk == xn)
					{
						System.out.println("xk==xn");
						stickman.pos.set(stickman.pos.x+e, stickman.pos.y);
					}
					
					Vector2f parallel = new Vector2f(xn-x1, yn-y1);

					parallel.normalise().scale(Constant.MAX_VELOCITY);

					if (parallel.x < 0)
					{
						parallel.x = -parallel.x;
						parallel.y = -parallel.y;
					}
					stickman.vel.set(parallel);

					stickman.setAcc(new Vector2f(0,0));
					stickman.updatePosition(timeStep);
					if (hitByCannon() || drown())
						sb.enterState(4);
					return;
				}				
			} //end for
			leftIndex++;
		} //end while
	}

	/**
	 * generate the next position after the LEFT_KEY is pressed
	 * @param sb
	 * @param delta
	 */
	public void nextLeftPosition(StateBasedGame sb, int delta) 
	{
		float timeStep = delta * Constant.DELTA_FACTOR;


		float minX = stickman.minX();
		float maxX = stickman.maxX();

		if (maxX >= Constant.DISPLAY_WIDTH-10) 
		{
			Constant.LEVEL++;
			Constant.SEED++;
			sb.enterState(2);
		}

		else if (minX <= 10)
		{
			Constant.LEVEL--;
			Constant.SEED--;
			sb.enterState(2);
		}
		
		if (owner.getEntity(1) == null)
			return;
		landscape = (PerlinLandscape) owner.getEntity(1);
		

		float numOfSeg = (float) Math.pow(2, landscape.octave);
		float width = Constant.DISPLAY_WIDTH / numOfSeg;

		int leftIndex = (int) Math.floor(minX / width);
		int rightIndex = (int) Math.ceil(maxX / width);

		while (leftIndex < rightIndex)
		{			
			int x1 = landscape.points[leftIndex].x;
			int xn = landscape.points[leftIndex+1].x;
			int y1 = landscape.points[leftIndex].y;
			int yn = landscape.points[leftIndex+1].y;

			float cenX = stickman.cenX();
			float cenY = stickman.cenY();

			float e = (float) 0.05;
			for (float xk=x1; xk<xn; xk+=e)
			{
				float yk = y1 - (x1-xk)*(y1-yn)/(x1-xn);
				float lenSqr = lenSqr(xk, yk, cenX,cenY);
				if (lenSqr <= Math.pow(Constant.HALF_HEIGHT, 2) + 50)
				{
					Vector2f r = new Vector2f(yn-y1, x1-xn);

					Vector2f unitR = r.normalise();
					Vector2f x1ToO = new Vector2f(cenX-x1, cenY-y1);
					Vector2f X1ToXn = new Vector2f(xn-x1, yn-y1);
					float length = crossProductLength(x1ToO, X1ToXn.normalise());
					if (length <= Constant.HALF_HEIGHT)
					{
						float rel = Constant.HALF_HEIGHT - length;						
						Vector2f dp = new Vector2f(rel*unitR.x, rel*unitR.y);
						stickman.pos.set(stickman.pos.add(dp));
					}
					
					if (xk == xn)
					{
						System.out.println("xk==xn");
						stickman.pos.set(stickman.pos.x-e, stickman.pos.y);
					}
					
					Vector2f parallel = new Vector2f(xn-x1, yn-y1);

					parallel.normalise().scale(Constant.MAX_VELOCITY);

					if (parallel.x > 0)
					{
						parallel.x = -parallel.x;
						parallel.y = -parallel.y;
					}

					stickman.vel.set(parallel);

					stickman.setAcc(new Vector2f(0,0));
					stickman.updatePosition(timeStep);
					if (hitByCannon() || drown())
						sb.enterState(4);
					return;
				}				
			} //end for
			leftIndex++;
		} //end while
	}

	/**
	 * Generate the next position of the avatar after the jump button is pressed
	 * @param sb
	 */
	public void jump(StateBasedGame sb)
	{
		float minX = stickman.minX();
		float maxX = stickman.maxX();

		if (maxX >= Constant.DISPLAY_WIDTH-10) 
		{
			Constant.LEVEL++;
			Constant.SEED++;
			sb.enterState(2);
		}

		else if (minX <= 10)
		{
			Constant.LEVEL--;
			Constant.SEED--;
			sb.enterState(2);
		}
		
		if (owner.getEntity(1) == null)
			return;
		landscape = (PerlinLandscape) owner.getEntity(1);

		float numOfSeg = (float) Math.pow(2, landscape.octave);
		float width = Constant.DISPLAY_WIDTH / numOfSeg;


		int leftIndex = (int) Math.floor(minX / width);
		int x1 = landscape.points[leftIndex].x;
		int xn = landscape.points[leftIndex+1].x;
		int y1 = landscape.points[leftIndex].y;
		int yn = landscape.points[leftIndex+1].y;

		while (xn < stickman.cenX()) {
			leftIndex++;
			x1 = landscape.points[leftIndex].x;
			xn = landscape.points[leftIndex+1].x;
			y1 = landscape.points[leftIndex].y;
			yn = landscape.points[leftIndex+1].y;
		}
		
		if (aboveTerrain(stickman.cenX(), stickman.maxY(), x1, y1, xn, yn))
		{
			return;
		}
		stickman.vel.set(stickman.vel.x, Constant.JUMP_VEL.y);
	}


	public static boolean aboveTerrain(float xp, float yp, float x1, float y1, float xn, float yn)
	{
		float ytrue = yn + (xp-xn)*(y1-yn)/(x1-xn);
		return yp - ytrue < -20;
	}

	public static float crossProductLength(Vector2f v1, Vector2f v2)
	{
		return Math.abs(v1.x * v2.y - v1.y * v2.x);
	}

	public static float lenSqr(float x1, float y1, float x2, float y2)
	{
		return (float) (Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

	public Vector2f perpenV(Vector2f r, Vector2f v)
	{
		return new Vector2f(r.y * (r.y*v.x - r.x*v.y), r.x * (r.x*v.y - r.y*v.x));
	}

}
