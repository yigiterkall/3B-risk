package game.state;

import game.GameEngine;
import game.player.Player;
import game.player.Territory;
import javafx.event.ActionEvent;
import java.util.ArrayList;

public class InitialArmyPlacementState implements GameState {

    private static InitialArmyPlacementState instance;
    private GameEngine engine;
    private int currentPlayer;

    private InitialArmyPlacementState() {
        engine = GameEngine.getInstance();
        currentPlayer = 0;
    }

    public static InitialArmyPlacementState getInstance() {
        if (instance == null) {
            synchronized (InitialArmyPlacementState.class) {
                if (instance == null) {
                    instance = new InitialArmyPlacementState();
                }
            }
        }
        return instance;
    }

    // When player selects a map territory
    public void mapSelect(int territory) {
        Territory t = engine.getMap().getTerritory(territory);
        ArrayList<Player> players = engine.getPlayers();
            // getName() can also be getId() if ids are implemented and the other part requires something to compare
            // when the map is implemented fix this
            // && territory.getRuler() == null this can also be checked but if it is not null disabling the button is
            // better
            if (t.getRuler() == null) {
                t.setRuler(players.get(currentPlayer));
            }
        checkIfStateOver();
        currentPlayer++;
    }

    @Override
    public void start() {
        engine.mapScene.getController().setState(0);
        currentPlayer = 0;
    }

    // Is the state really over when no territory left empty?
    public void checkIfStateOver() {
        Territory[] territories = engine.getMap().getTerritories();
        boolean stateOver = true;
        for (Territory territory : territories) {
            if (territory.getRuler() == null) {
                stateOver = false;
            }
        }
        if (stateOver) {
            engine.switchState(ArmyPlacementState.getInstance());
        }
    }
}
