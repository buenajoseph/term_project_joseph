package edu.csc413.tankgame;

import com.sun.tools.javac.Main;
import edu.csc413.tankgame.model.GameState;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;
import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.StartMenuView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameDriver is the primary controller class for the tank game. The game is launched from GameDriver.main, and
 * GameDriver is responsible for running the game loop while coordinating the views and the data models.
 */

public class GameDriver {
    // TODO: Implement.
    // Add the instance variables, constructors, and other methods needed for this class. GameDriver is the centerpiece
    // for the tank game, and should store and manage the other components (i.e. the views and the models). It also is
    // responsible for running the game loop.
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameState gameState;


    public GameDriver() {

        gameState = new GameState();
        mainView = new MainView(this, gameState);
        runGameView = mainView.getRunGameView();
    }

    public void start() {
        // TODO: Implement.
        // This should set the MainView's screen to the start menu screen.
        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);
    }

    public void startRun() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        runGame();
    }

    public void exitGame() {
        mainView.closeGame();
    }


    private void runGame() {
        PlayerTank playerTank = new PlayerTank(GameState.PLAYER_TANK_ID, RunGameView.PLAYER_TANK_INITIAL_X,
                RunGameView.PLAYER_TANK_INITIAL_Y, RunGameView.PLAYER_TANK_INITIAL_ANGLE);
        AiTank aiTank = new AiTank(GameState.AI_TANK_ID, RunGameView.AI_TANK_INITIAL_X,
                RunGameView.AI_TANK_INITIAL_Y, RunGameView.AI_TANK_INITIAL_ANGLE);

        gameState.addEntity(playerTank);
        gameState.addEntity(aiTank);

        runGameView.addDrawableEntity(GameState.PLAYER_TANK_ID, RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(), playerTank.getY(), playerTank.getAngle());
        runGameView.addDrawableEntity(GameState.AI_TANK_ID, RunGameView.AI_TANK_IMAGE_FILE,
                aiTank.getX(), aiTank.getY(), aiTank.getAngle());

        Runnable gameRunner = () -> {
            while (update()) {
                runGameView.repaint();
                try {
                    Thread.sleep(8L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
        };
        new Thread(gameRunner).start();
    }

    // TODO: Implement.
    // update should handle one frame of gameplay. All tanks and shells move one step, and all drawn entities
    // should be updated accordingly. It should return true as long as the game continues.
    private boolean update() {
        // Ask all entities to move
        if(gameState.exitButtonPressed()) {
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            return false;
        }
        for (Entity entity : gameState.getEntities()) {
            entity.move(gameState);
        }

        // Ask all entities to check bounds
        for (Entity entity : gameState.getEntities()) {
            if (!entity.getId().contains("shell")) {
                if (entity.getX() < gameState.TANK_X_LOWER_BOUND) {
                    entity.setPosition(gameState.TANK_X_LOWER_BOUND, entity.getY());
                }
                if (entity.getX() > gameState.TANK_X_UPPER_BOUND) {
                    entity.setPosition(gameState.TANK_X_UPPER_BOUND, entity.getY());
                }
                if (entity.getY() < gameState.TANK_Y_LOWER_BOUND) {
                    entity.setPosition(entity.getX(), gameState.TANK_Y_LOWER_BOUND);
                }
                if (entity.getY() > gameState.TANK_Y_UPPER_BOUND) {
                    entity.setPosition(entity.getX(), gameState.TANK_Y_UPPER_BOUND);
                }
            }
            else {
                if (entity.getX() < gameState.SHELL_X_LOWER_BOUND ||
                        entity.getX() > gameState.SHELL_X_UPPER_BOUND ||
                        entity.getY() < gameState.SHELL_Y_LOWER_BOUND ||
                        entity.getY() > gameState.SHELL_Y_UPPER_BOUND) {
                    entity.setLive(false);
                }
            }

        }

        // Collision check

        // GameState - new entities to draw
        // if so, call addDrawableEntity
            for (int in = 0; in < gameState.getEntities().size(); in++) {
                Entity entity = gameState.getEntities().get(in);
                if (!entity.getId().contains("shell")) {
                    if (entity.readyToShoot()) {
                        Shell shell;
                        double x, y, angle;
                        if (entity.getId().equals(GameState.PLAYER_TANK_ID) && gameState.spacePressed()) {
                            x = gameState.getEntity(GameState.PLAYER_TANK_ID).getX() + 30.0 *
                                    (Math.cos(gameState.getEntity(GameState.PLAYER_TANK_ID).getAngle()) + 0.5);
                            y = gameState.getEntity(GameState.PLAYER_TANK_ID).getY() + 30.0 *
                                    (Math.sin(gameState.getEntity(GameState.PLAYER_TANK_ID).getAngle()) + 0.5);
                            angle = gameState.getEntity(GameState.PLAYER_TANK_ID).getAngle();
                            gameState.setPressSpace(false);
                        }
                        else {
                            x = gameState.getEntity(GameState.AI_TANK_ID).getX() + 30.0 *
                                    (Math.cos(gameState.getEntity(GameState.AI_TANK_ID).getAngle()) + 0.5);
                            y = gameState.getEntity(GameState.AI_TANK_ID).getY() + 30.0 *
                                    (Math.sin(gameState.getEntity(GameState.AI_TANK_ID).getAngle()) + 0.5);
                            angle = gameState.getEntity(GameState.AI_TANK_ID).getAngle();
                        }
                        shell = new Shell(x, y, angle);
                        gameState.addEntity(shell);
                        runGameView.addDrawableEntity(shell.getId(), RunGameView.SHELL_IMAGE_FILE,
                                shell.getX(), shell.getY(), shell.getAngle());
                        entity.setReadyToShoot(false);
                    }
                }
            }

        // GameState - new entities to remove
        // if so, call removeDrawableEntity
        for (int in = 0; in < gameState.getEntities().size(); in++) {
            if (!gameState.getEntities().get(in).getLive() && gameState.getEntities().get(in).getId().contains("shell")) {
                runGameView.removeDrawableEntity(gameState.getEntities().get(in).getId());
                gameState.getEntities().remove(in);
                in--;
            }
        }

        // set new locations and angles of every drawable entity
        for (Entity entity : gameState.getEntities()) {
            runGameView.setDrawableEntityLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
        }
        return true;
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }


}
