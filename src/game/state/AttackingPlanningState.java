package game.state;

import game.GameEngine;
import game.player.Territory;
import javafx.event.Event;
import menu.MenuState;

public class AttackingPlanningState extends PlanningState implements MenuState {
    public AttackingPlanningState(int width, int height, GameEngine engine) {
        super(width, height, engine);
    }

    //Select the source territory
    public void selectSource(Event e) {
        if(destination.equals("")) {
            Territory[] territories = engine.getMap().getTerritories();
            for (Territory territory : territories) {
                //check the territory name && if player is the ruler
                if (territory.getName() == e.getSource().toString()
                        && ((engine.getPlayers()).get(engine.getPlayerTurn()) == territory.getRuler())) {
                    source = territory;
                }
            }
        }
    }

    //Select the target territory
    public void selectDestination(Event e) {
        if(!(source.equals(""))) {
            Territory[] territories = engine.getMap().getTerritories();
            for (Territory territory : territories) {
                if (territory.getName() == e.getSource().toString()) {
                    if(source.isAdjacent(territory)) { //check if destination is adjacent to the source
                        destination = territory;
                    }
                }
            }
        }
    }

}
