package edu.csc413.tankgame;

import com.sun.tools.javac.Main;
import edu.csc413.tankgame.model.GameState;
import edu.csc413.tankgame.view.MainView;
import edu.csc413.tankgame.view.RunGameView;
import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.StartMenuView;

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
    private final MainView mainView; //changed to static from final
    private final RunGameView runGameView;
    //private static String state = "start-game";
    private final GameState gameState;


    // todo: Non-working MenuSelectListener

    /*public static class MenuSelectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals(StartMenuView.START_BUTTON_ACTION_COMMAND)) {
                state = "run-game";
            }
            if (actionCommand.equals(StartMenuView.EXIT_BUTTON_ACTION_COMMAND)) {
                state = "exit";
            }
        }
    }*/


    public GameDriver() {
        mainView = new MainView();
        runGameView = mainView.getRunGameView();
        gameState = new GameState();
    }

    public void start() {
        // TODO: Implement.
        // This should set the MainView's screen to the start menu screen.
        mainView.setScreen(MainView.Screen.START_MENU_SCREEN);

        // Todo: change me
        mainView.setScreen((MainView.Screen.RUN_GAME_SCREEN));
        runGame();
        /*while (state.equals("start-game")) {
            if (state.equals("run-game")) {
                mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
                runGame();
            }
            else if (state.equals("exit")) {
                mainView.closeGame();
            }
        }*/
    }

    private void runGame() {
        Tank playerTank = new Tank(GameState.PLAYER_TANK_ID, RunGameView.PLAYER_TANK_INITIAL_X,
                RunGameView.PLAYER_TANK_INITIAL_Y, RunGameView.PLAYER_TANK_INITIAL_ANGLE);
        Tank aiTank = new Tank(GameState.AI_TANK_ID, RunGameView.AI_TANK_INITIAL_X,
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
            tank.moveForward();
        }

        // Ask all entities to check bounds

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
