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


    // todo: Non-working MenuSelectListener
    /*public class MenuSelectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actCommand = e.getActionCommand();
            if (actCommand.equals("start-ac")) {
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                runGame();
            }
            else if (actCommand.equals("exit-ac")) {
                mainView.closeGame();
            }
        }
    }*/

    public GameDriver() {

        gameState = new GameState();
        mainView = new MainView(this, gameState);
        runGameView = mainView.getRunGameView();
    }

    public void start() {
        // TODO: Implement.
        // This should set the MainView's screen to the start menu screen.
        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);

        //mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        //runGame();
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

        gameState.addTank(playerTank);
        gameState.addTank(aiTank);

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
        for (Tank tank : gameState.getTanks()) {
            tank.move(gameState);
        }

        // Ask all entities to check bounds
        for (Tank tank : gameState.getTanks()) {
            if (tank.getX() > gameState.TANK_X_LOWER_BOUND) {

            }
            if (tank.getX() < gameState.TANK_X_UPPER_BOUND) {

            }
            if (tank.getY() > gameState.TANK_Y_LOWER_BOUND) {

            }
            if (tank.getY() > gameState.TANK_Y_UPPER_BOUND) {

            }

        }

        // Collision check

        // set new locations and angles of every drawable entity
        for (Tank tank : gameState.getTanks()) {
            runGameView.setDrawableEntityLocationAndAngle(tank.getId(), tank.getX(), tank.getY(), tank.getAngle());
        }
        return true;
    }

    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }


}
