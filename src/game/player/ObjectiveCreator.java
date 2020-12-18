package game.player;

import game.GameEngine;
import game.GameMap;

public class ObjectiveCreator {

    static final int HOLD_LIMIT = 5;
    static final int CAPTURE_LIMIT = 5;
    static final int AREA_TURN_LIMIT = 5; //area objectives will be given after this turn
    static final double CAPTURE_PROB = 0.5;
    static final double TERRITORY_PROB = 0.5;

    public static Objective generateObjective(Player p){
        GameEngine engine = GameEngine.getInstance();
        Objective objective;
        ObjectiveStrategy strategy;
        ObjectiveStrategy decorator;
//        boolean isArea;
        Place place;

        String objectiveName; //create objective name by adding "capture" or "hold" to the target name
        int limit;

        // select strategy
        if (Math.random() < CAPTURE_PROB) {
            limit = (int)(Math.random() * HOLD_LIMIT) + 1; //determine the turn for hold objective
            objectiveName = "Capture ";
            strategy = new CaptureObjective();
        } else {
            limit = (int)(Math.random() * CAPTURE_LIMIT) + 1;
            objectiveName = "Hold ";
            strategy = new HoldObjective();
        }

        // select decorator
        if (Math.random() > TERRITORY_PROB && engine.getTurn() > AREA_TURN_LIMIT) {
//            isArea = true;
            Area[] areas = GameMap.getInstance().getAreas();
            int areaIndex = (int)(Math.random() + areas.length);
            place = areas[areaIndex];
            decorator = new AreaDecorator(strategy);
        } else {
//            isArea = false;
            int territoryIndex = (int)(Math.random() * GameMap.TOTAL_TERRITORY_COUNT);
            place = GameMap.getInstance().getTerritories()[territoryIndex];
            decorator = new TerritoryDecorator(strategy);
        }

        objectiveName += place.getName();
        objective = new Objective(decorator, place, limit, objectiveName, p);
        return objective;
    }

}