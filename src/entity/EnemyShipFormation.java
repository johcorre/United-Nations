package entity;

import java.util.HashSet;
import java.util.Set;

import screen.Screen;
import engine.DrawManager;
import engine.DrawManager.SpriteType;

/**
 * Groups enemy ships into a formation that moves together.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public class EnemyShipFormation {

	private DrawManager drawManager = DrawManager.getInstance();
	private Screen screen;
	private Set<EnemyShip> enemyShips;
	private int sizeX;
	private int sizeY;
	private int positionX;
	private int positionY;

	private enum Direction {
		RIGHT, LEFT, DOWN
	};

	private Direction currentDirection;
	private int movementInterval;

	/**
	 * Constructor, sets the initial conditions.
	 */
	public EnemyShipFormation(int sizeX, int sizeY) {
		this.enemyShips = new HashSet<EnemyShip>();
		this.currentDirection = Direction.RIGHT;
		this.movementInterval = 0;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.positionX = 40;
		this.positionY = 40;
		SpriteType spriteType;

		for (int i = 0; i < this.sizeX; i++) {
			spriteType = SpriteType.EnemyShipC1;

			for (int j = 0; j < this.sizeY; j++) {

				if (j == (this.sizeY / 2) - 1)
					spriteType = SpriteType.EnemyShipB1;
				else if (j == this.sizeY - 2)
					spriteType = SpriteType.EnemyShipA1;

				enemyShips.add(new EnemyShip(screen, positionX * (i + 1),
						positionY * (j + 1), spriteType));
			}
		}
	}

	/**
	 * Associates the formation to a given screen.
	 * 
	 * @param newScreen
	 *            Screen to attach.
	 */
	public void attach(Screen newScreen) {
		screen = newScreen;
	}

	/**
	 * Draws every individual component of the formation.
	 */
	public void draw() {
		move();
		
		for(EnemyShip enemyShip : this.enemyShips)
			drawManager.drawEntity(enemyShip, enemyShip.getPositionX(), enemyShip.getPositionY());
	}

	/**
	 * Updates the position of the ships.
	 */
	private void move() {
		int movementX = 0;
		int movementY = 0;
		movementInterval++;
		if (movementInterval >= 60) {
			movementInterval = 0;
			//TODO cleanup
			boolean isAtBottom = positionY + 40 * (this.sizeY - 1)
					+ enemyShips.iterator().next().getHeight() > screen.getHeight() - 80;
			
			if (currentDirection == Direction.RIGHT && !isAtBottom
					&& positionX + 40 * (this.sizeX - 1)
							+ enemyShips.iterator().next().getWidht() >= screen.getWidth() - 40)
				currentDirection = Direction.DOWN;
			else if (currentDirection == Direction.LEFT && !isAtBottom && positionX <= 40)
				currentDirection = Direction.DOWN;
			else if (positionY % 40 == 0
					&& positionX <= 40)
				currentDirection = Direction.RIGHT;
			else if (positionY % 20 == 0
					&& positionX + 40 * (this.sizeX - 1)
							+ enemyShips.iterator().next().getWidht() >= screen.getWidth() - 40)
				currentDirection = Direction.LEFT;

			if (currentDirection == Direction.RIGHT)
				movementX = 8;
			else if (currentDirection == Direction.LEFT)
				movementX = -8;
			else
				movementY = 4;

			positionX += movementX;
			positionY += movementY;
			
			for(EnemyShip enemyShip : this.enemyShips) {
				enemyShip.move(movementX, movementY);
				enemyShip.update();
			}
		}
	}
}