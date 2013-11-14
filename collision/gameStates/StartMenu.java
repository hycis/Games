package gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import collisionGame.Constant;

import entity.Entity;

public class StartMenu extends State {

	public StartMenu() 
	{
		this.id = 1;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	/**
	 * enter into the state
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		Constant.LEVEL = 0;
	}

	/**
	 * render the state on the screen
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		Image image = new Image("gameStates/start.png");
		g.drawImage(image, 400, 270);
		g.drawString("This Game Consists of 5 Levels", 200, 50);
		g.drawString("From Level 2 Onwards, Landscape will be Perlin Generated", 200 , 70);
		g.drawString("CannonBalls Will be firing randomly from Top Left Hand Corner", 200, 90);
		g.drawString("There will be wind which can be changed with (+) or (-)", 200, 110);
		g.drawString("Both Avatar and CannonBalls are affected by Wind", 200, 130);
		g.drawString("CannonBalls are also affected by Gravity", 200, 150);
		g.drawString("CannonBalls collide elastically with each other and with Terrain", 200, 170);
		g.drawString("CannonBalls firing rate can be adjusted by (9) or (0)", 200, 190); 
		g.drawString("Avatar is killed if hit by CannonBall or Drown in Water", 200, 210);
		g.drawString("Controls: SPACE: Jump, LEFT: move left, RIGHT: move right, DOWN: freeze", 200, 240);
	}
	
	/**
	 * update the state
	 */
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

		Input input = container.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		boolean inMenu = false;

		if (mouseX > 400 && mouseX < 626 && mouseY > 270 && mouseY < 346)
			inMenu = true;
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && inMenu)
		{
			game.enterState(3);
		}


	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Entity getEntity(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
