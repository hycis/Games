package entity;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.StateBasedGame;

import component.*;
import collisionGame.Constant;

public class CannonBallSystem extends Entity{

	CopyOnWriteArrayList<CannonBall> cannonBallSystem;
	PerlinLandscape landscape;

	public CannonBallSystem()
	{
		this.id = 2;
		this.cannonBallSystem = new CopyOnWriteArrayList<CannonBall>();
	}

	/**
	 * randomly generate a vector with magnitude within min and max
	 * @param min value of the vector component
	 * @param max value of the vector component
	 * @return
	 */
	public Vector2f randomGenerator(float min, float max)
	{
		Random random = new Random();
		float ranFloat1 = random.nextFloat();
		float x = 8*(ranFloat1*(max - min) + min);
		float ranFloat2 = random.nextFloat();
		float y = ranFloat2*(max - min) + min;
		return new Vector2f(x, y);
	}

	float time;
	/**
	 * Update the position of all the cannon balls
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
		if (time > Constant.FIRING_TIME_INTERVAL)
		{
		cannonBallSystem.add(new CannonBall(new Vector2f(100, 50), randomGenerator(5,20)));
		time = 0;
		}
		time += delta;

		

		for (CannonBall cb : cannonBallSystem)
		{
			cb.nextPosition();
			if (cb.isDead())
			{
				cannonBallSystem.remove(cb);
			}
			updateVelOfcollidedCannonBalls(cb, delta);

			if (this.owner.getEntity(1) != null)
			{
				this.landscape = (PerlinLandscape) this.owner.getEntity(1);
				updateVelOfCollidedPerlin(cb);
			}
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		// TODO Auto-generated method stub
		for (CannonBall cb : cannonBallSystem)
		{
			cb.render(gc, sb, gr);
		}
	}
	
	/**
	 * Update the velocity of two collided cannon balls after they collide
	 * @param myCannonBall My cannon ball that is colliding the with the other cannon balls
	 * @param delta the time interval between two successive frames
	 */
	public void updateVelOfcollidedCannonBalls(CannonBall myCannonBall, int delta)
	{	
		float myBallCenX = myCannonBall.circle.getCenterX();
		float myBallCenY = myCannonBall.circle.getCenterY();

		for (CannonBall otherCB : this.cannonBallSystem)
		{
			if (!myCannonBall.equals(otherCB))
			{
				float otherCBCenX = otherCB.circle.getCenterX();
				float otherCBCenY = otherCB.circle.getCenterY();

				float lengthSqr = lenSqr(myBallCenX, myBallCenY, otherCBCenX, otherCBCenY); 

				if (lengthSqr <= Math.pow(2*Constant.RADIUS, 2))
				{
					// relative vector displacement of two ball centers.
					Vector2f relVec = new Vector2f(otherCBCenX-myBallCenX, otherCBCenY-myBallCenY);
					Vector2f unitRelV = relVec.normalise();

					// magnitude of velocity parallel to the vector connecting the centers
					float mVParLen = myCannonBall.velocity.x * unitRelV.x 
							+ myCannonBall.velocity.y * unitRelV.y;
					float oVParLen = otherCB.velocity.x * unitRelV.x
							+ otherCB.velocity.y * unitRelV.y;

					// magnitude of velocity perpendicular to the vector connecting the centers
					float mVPerLen = crossProductLength(myCannonBall.velocity, unitRelV);
					float oVPerLen = crossProductLength(otherCB.velocity, unitRelV);

					// find the perpendicular velocity vector
					Vector2f myVper = perpenV(unitRelV, myCannonBall.velocity);
					Vector2f otherVper = perpenV(unitRelV, otherCB.velocity);

					// find unit perpendicular velocity vector
					Vector2f unitMyVper = myVper.normalise();
					Vector2f unitOtherVper = otherVper.normalise();

					// update velocity after collision					
					myCannonBall.velocity.set(mVPerLen*unitMyVper.x + oVParLen*unitRelV.x,
							mVPerLen*unitMyVper.y + oVParLen*unitRelV.y);
					otherCB.velocity.set(oVPerLen*unitOtherVper.x + mVParLen*unitRelV.x,
							oVPerLen*unitOtherVper.y + mVParLen*unitRelV.y);

					float newLenSqr = 0;
					do {
						otherCB.nextPosition();
						newLenSqr = lenSqr(myCannonBall.position.x, myCannonBall.position.y,
								otherCB.position.x, otherCB.position.y);
					} while (newLenSqr <= Math.pow(2*Constant.RADIUS, 2));
					return;
				}
			}
		}
	}

	public static float lenSqr(float x1, float y1, float x2, float y2)
	{
		return (float) (Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
	}

	/**
	 * Update the velocity of CannonBall after collision with Perlin landscape
	 * @param myCB my CannonBall
	 */
	public void updateVelOfCollidedPerlin(CannonBall myCB)
	{
		float minX = myCB.circle.getMinX();
		float maxX = myCB.circle.getMaxX();

		if (maxX >= Constant.DISPLAY_WIDTH || minX <= 0)
			return;

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

			float cenX = myCB.circle.getCenterX();
			float cenY = myCB.circle.getCenterY();

			float e = (float) 0.05;
			for (float xk=x1; xk<=xn; xk+=e)
			{
				float yk = y1 - (x1-xk)*(y1-yn)/(x1-xn);
				float lenSqr = lenSqr(xk, yk, cenX, cenY);
				if (lenSqr <= Math.pow(Constant.RADIUS, 2))
				{

					Vector2f r = new Vector2f(yn-y1, x1-xn);

					Vector2f unitR = r.normalise();
					float VParLen = myCB.velocity.x * unitR.x 
							+ myCB.velocity.y * unitR.y;

					// magnitude of velocity perpendicular to the vector connecting the centers
					float VPerLen = crossProductLength(myCB.velocity, unitR);
					// find the perpendicular velocity vector
					Vector2f Vper = perpenV(unitR, myCB.velocity);
					// find unit perpendicular velocity vector
					Vector2f unitVper = Vper.normalise();
					// perpendicular vector to unitR
					Vector2f per = new Vector2f(unitVper.x * VPerLen, unitVper.y * VPerLen);
					// parallel vector to unitR
					Vector2f par = new Vector2f(unitR.x * VParLen, unitR.y * VParLen);
					// update velocity after collision					
					myCB.velocity.set(per.add(par.negate()));

					Vector2f x1ToO = new Vector2f(cenX-x1, cenY-y1);
					Vector2f X1ToXn = new Vector2f(xn-x1, yn-y1);

					float length = crossProductLength(x1ToO, X1ToXn.normalise());;
					if (length <= Constant.RADIUS)
					{
						float rel = Constant.RADIUS - length;						
						Vector2f dp = new Vector2f(rel*unitR.x, rel*unitR.y);
						myCB.position.set(myCB.position.add(dp));
					}
					return;
				}
			} //end for
			leftIndex++;
		} //end while
	}

	/**
	 * @param r unit vector
	 * @param v velocity vector
	 * @return r CROSS v CROSS r
	 */
	public Vector2f perpenV(Vector2f r, Vector2f v)
	{
		return new Vector2f(r.y * (r.y*v.x - r.x*v.y), r.x * (r.x*v.y - r.y*v.x));
	}

	/**
	 * @param v1
	 * @param v2
	 * @return the length of the cross product of v1 and v2
	 */
	public static float crossProductLength(Vector2f v1, Vector2f v2)
	{
		return Math.abs(v1.x * v2.y - v1.y * v2.x);
	}
}
