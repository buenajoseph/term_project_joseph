package edu.csc413.tankgame.model;

public class PlayerTank extends Tank{
    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }
    public void move(GameState gameState) {
        if (gameState.wPressed()) {
            moveForward();
        }
        if (gameState.sPressed()) {
            moveBackward();
        }
        if (gameState.aPressed()) {
            turnLeft();
        }
        if (gameState.dPressed()) {
            turnRight();
        }
    }
}
