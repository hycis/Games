package gameStates;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import entity.Entity;

public class Win extends State {

	public Win()
	{
		this.id = 5;
	}
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		g.drawString("You Win", 460, 120);
		Image image = new Image("gameStates/win.png");
		g.drawImage(image, 400, 200);

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		Input input = container.getInput();
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		boolean inMenu = false;
		
		if (mouseX > 400 && mouseX < 626 && mouseY > 200 && mouseY < 276)
			inMenu = true;
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && inMenu)
		{
			game.enterState(1);
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
