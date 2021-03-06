package game.scene;

import game.GameEngine;
import game.GameMap;
import game.SoundEngine;
import game.player.Player;
import game.player.Territory;
import game.state.WarState;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;


import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MapSceneController implements Initializable, EventHandler<ActionEvent> {

    private static final String[] ABILITY_DESCRIPTIONS = {"Everything by Design: Once a turn, you may choose to swap the result of your lowest die roll with the opponent's highest one",
            "Eastern Affinity: You win ties when attacking in the East region.",
            "The Moderate Depression: Once a turn, you may reduce 1 from the maximum die result of the enemy.",
            "Reeducate: Once a turn you may discard your objective card and draw another one.",
            "Knowledge Above All: Your rewards from completed objectives are doubled.",
            "The Pen is Mightier: If the attacker has more armies, gain an extra die.",
            "Lawyered: Once a game, the player may choose to place 2 times the troops they have in the deployment phase.\n",
            "Hostile Takeover: Once a game, take over an opponent's territory along with the soldiers.",
            "Over-Engineered: You can't roll a 1.",
            "Smoke and Mirrors: Once a game, you may choose to play an extra turn right after your normal one."};
    private static final String[] TERRITORY_NAMES = {"East Dorms","East Sports Center", "East Library", "Prep Buildings", "Health Center", "East Cafeteria", "ATM",
            "East Coffee Break", "East Mozart Cafe", "East Entrance", "Bilkent 1 & 2", "Sports International", "Ankuva", "Bilkent Center", "Bilkent Hotel", "MSSF",
            "Concert Hall", "Dorms", "V Building", "F Buildings", "Dorm 76", "Mescit", "Starbucks", "M Building", "Meteksan", "Sports Center", "Nanotam",
            "Mayfest", "A Building", "S Building", "T Building", "G Building", "Coffee Break", "Square", "CafeIn", "Statue", "B Building", "Cyber Park", "ODEON",
            "Library", "Mozart Cafe", "Cafeteria", "EA Building", "Meteksan", "EE Building", "Mithat Coruh", "Entrance"};

    // East
    @FXML ImageView dormsEast;
    @FXML ImageView sportsEast;
    @FXML ImageView libraryEast;
    @FXML ImageView prepEast;
    @FXML ImageView healthCenterEast;
    @FXML ImageView cafeteriaEast;
    @FXML ImageView atmEast;
    @FXML ImageView coffeeBreakEast;
    @FXML ImageView mozartEast;
    @FXML ImageView entranceEast;

    // Island
    @FXML ImageView bilkent12Island;
    @FXML ImageView sportsInternationalIsland;
    @FXML ImageView ankuvaIsland;
    @FXML ImageView centerIsland;
    @FXML ImageView hotelIsland;

    // Upper Main
    @FXML ImageView mssfUpperMain;
    @FXML ImageView concertHallUpperMain;
    @FXML ImageView dormsUpperMain;
    @FXML ImageView vBuildingUpperMain;
    @FXML ImageView fBuildingsUpperMain;
    @FXML ImageView dorm76UpperMain;
    @FXML ImageView mescitUpperMain;
    @FXML ImageView starbucksUpperMain;
    @FXML ImageView mBuildingUpperMain;
    @FXML ImageView meteksanUpperMain;
    @FXML ImageView sportsCenterUpperMain;
    @FXML ImageView nanotamUpperMain;
    @FXML ImageView mayfestUpperMain;
    @FXML ImageView aBuildingUpperMain;
    @FXML ImageView sBuildingUpperMain;
    @FXML ImageView tBuildingUpperMain;

    // Lower Main
    @FXML ImageView gBuildingLowerMain;
    @FXML ImageView coffeeBreakLowerMain;
    @FXML ImageView squareLowerMain;
    @FXML ImageView cafeInLowerMain;
    @FXML ImageView statueLowerMain;
    @FXML ImageView bBuildingLowerMain;
    @FXML ImageView cyberParkLowerMain;
    @FXML ImageView odeonLowerMain;
    @FXML ImageView libraryLowerMain;
    @FXML ImageView mozartLowerMain;
    @FXML ImageView cafeteriaLowerMain;
    @FXML ImageView eaBuildingLowerMain;
    @FXML ImageView meteksanLowerMain;
    @FXML ImageView eeBuildingLowerMain;
    @FXML ImageView mithatCoruhLowerMain;
    @FXML ImageView entranceLowerMain;

    // Troop Counters for territories
    @FXML
    Text troops0,troops1,troops2,troops3,troops4,troops5,troops6,troops7,troops8,troops9,troops10,troops11,troops12,
            troops13,troops14,troops15,troops16,troops17,troops18,troops19,troops20,troops21,troops22,troops23,troops24,
            troops25,troops26,troops27,troops28,troops29,troops30,troops31,troops32,troops33,troops34,troops35,troops36,
            troops37,troops38,troops39,troops40,troops41,troops42,troops43,troops44,troops45,troops46;

    // Player Info
    @FXML
    Rectangle p1Bg, p2Bg, p3Bg, p4Bg, p5Bg, p6Bg;
    @FXML
    Circle p1IconBg,p2IconBg,p3IconBg,p4IconBg,p5IconBg,p6IconBg;
    @FXML
    ImageView p1Icon,p2Icon,p3Icon,p4Icon,p5Icon,p6Icon, p1TerritoryIcon, p2TerritoryIcon, p3TerritoryIcon, p4TerritoryIcon, p5TerritoryIcon, p6TerritoryIcon;
    @FXML
    Label p1TerritoryCount,p2TerritoryCount,p3TerritoryCount,p4TerritoryCount,p5TerritoryCount,p6TerritoryCount;

    // Pause button
    @FXML
    Button pauseButton;

    // Used to block interaction with the map.
    @FXML
    Rectangle mapBlocker, troopBlocker;

    // Used for displaying battle results.
    @FXML
    Pane battleResultPane;
    @FXML
    Button battleOKButton;
    @FXML
    ImageView attackerIcon, defenderIcon, attackerDie1, attackerDie2, attackerDie3, defenderDie1, defenderDie2, defenderDie3;
    @FXML
    Circle attackerCircle, defenderCircle;

    // Used for selecting troop amount.
    @FXML
    Pane selectorPane;
    @FXML
    ImageView selectorCancel, selectorConfirm, selectorLeft, selectorRight;
    @FXML
    Label selector1, selector2, selector3;

    // Used to display current state
    @FXML
    Rectangle stateBg, state0, state1, state2;
    @FXML
    ImageView stateIcon;
    @FXML
    Label stateLabel;

    // Buttons for ability pass and objective
    @FXML
    Button passButton, abilityButton;

    // Objective display
    @FXML ImageView objectiveIcon;
    @FXML Label objectiveText, objectiveTurns;
    @FXML Pane objectiveDisplay;

    // Troop number pane
    @FXML Pane troopNumberPane;
    @FXML Label troopNumberLabel, objectiveReward;

    // Objective result
    @FXML Pane objectiveResult;
    @FXML Label objectiveResultLabel, objectiveRewardLabel;

    // Used to set ability tooltip
    @FXML Pane abilityTooltip;

    private ArrayList<Player> players;

    private ImageView[] territories;
    private Text[] troops;

    private ImageView[] pIcons;
    private Shape[] pIconBgs;
    private Shape[] pInfoBgs;
    private Label[] pTerritoryCounts;
    private ImageView[] pTerritoryIcons;

    private GameMap map;
    private GameEngine gameEngine;
    private ImageView[] defenderDiceImages, attackerDiceImages;

    private int selectionMin, selectionMax;
    private Label[] selectionLabels;

    private int curTurn;
    private int curState;
    private int sourceTerritory;
    private int destinationTerritory;


    public MapSceneController(){
    }

    public void init(){
        destinationTerritory = -1;
        sourceTerritory = -1;
        // Arrays for map info
        territories = new ImageView[]{dormsEast, sportsEast, libraryEast, prepEast, healthCenterEast, cafeteriaEast, atmEast,
                coffeeBreakEast, mozartEast, entranceEast, bilkent12Island, sportsInternationalIsland, ankuvaIsland, centerIsland, hotelIsland,
                mssfUpperMain, concertHallUpperMain, dormsUpperMain, vBuildingUpperMain, fBuildingsUpperMain, dorm76UpperMain, mescitUpperMain,
                starbucksUpperMain, mBuildingUpperMain, meteksanUpperMain, sportsCenterUpperMain, nanotamUpperMain, mayfestUpperMain, aBuildingUpperMain,
                sBuildingUpperMain, tBuildingUpperMain, gBuildingLowerMain, coffeeBreakLowerMain, squareLowerMain, cafeInLowerMain, statueLowerMain,
                bBuildingLowerMain, cyberParkLowerMain, odeonLowerMain, libraryLowerMain, mozartLowerMain, cafeteriaLowerMain, eaBuildingLowerMain, meteksanLowerMain,
                eeBuildingLowerMain, mithatCoruhLowerMain, entranceLowerMain};
        troops = new Text[]{troops0,troops1,troops2,troops3,troops4,troops5,troops6,troops7,troops8,troops9,troops10,
                troops11,troops12,troops13,troops14,troops15,troops16,troops17,troops18,troops19,troops20,troops21,
                troops22,troops23,troops24,troops25,troops26,troops27,troops28,troops29,troops30,troops31,troops32,
                troops33,troops34,troops35,troops36,troops37,troops38,troops39,troops40,troops41,troops42,troops43,
                troops44,troops45,troops46};

        // Arrays for player info
        pIcons = new ImageView[]{p1Icon,p2Icon,p3Icon,p4Icon,p5Icon,p6Icon};
        pIconBgs = new Shape[]{p1IconBg,p2IconBg,p3IconBg,p4IconBg,p5IconBg,p6IconBg};
        pInfoBgs = new Shape[]{p1Bg, p2Bg, p3Bg, p4Bg, p5Bg, p6Bg};
        pTerritoryCounts = new Label[]{p1TerritoryCount,p2TerritoryCount,p3TerritoryCount,p4TerritoryCount,p5TerritoryCount,p6TerritoryCount};
        pTerritoryIcons = new ImageView[]{p1TerritoryIcon, p2TerritoryIcon, p3TerritoryIcon, p4TerritoryIcon, p5TerritoryIcon, p6TerritoryIcon};


        int i = 0;
        for(ImageView iv : territories){
            if( iv != null) {
                iv.setPickOnBounds(false);
                iv.setId(Integer.toString(i));

                iv.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    gameEngine.mapSelect(Integer.parseInt(iv.getId()));
                    SoundEngine.getInstance().clickTerritory();
                    update();
                    event.consume();
                });

                iv.setCache(true);
                iv.setCacheHint(CacheHint.SPEED);
                Tooltip tp = new Tooltip(TERRITORY_NAMES[i]);
                tp.setShowDelay(new Duration(0));
                tp.setHideDelay(new Duration(0));
                tp.setShowDuration(Duration.INDEFINITE);
                tp.setId("territory_tooltip");
                Tooltip.install(iv, tp);
            }
            else{
                System.out.println("ImageView is null");
            }
            i++;
        }

        // Add handler to pause button
        pauseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> gameEngine.pause());

        // Make mapBlocker inactive.
        mapBlocker.setVisible(false);
        mapBlocker.setMouseTransparent(true);
        troopBlocker.setVisible(false);
        troopBlocker.setMouseTransparent(true);

        // Initialize battle result components
        battleResultPane.setVisible(false);
        battleResultPane.setMouseTransparent(true);
        battleOKButton.setOnAction(this);
        defenderDiceImages = new ImageView[]{defenderDie1, defenderDie2, defenderDie3};
        attackerDiceImages = new ImageView[]{attackerDie1, attackerDie2, attackerDie3};

        // Initialize selector components
        selectorPane.setVisible(false);
        selectorPane.setMouseTransparent(true);
        selectionLabels = new Label[]{selector1,selector2,selector3};
        selectorCancel.setPickOnBounds(false);
        selectorConfirm.setPickOnBounds(false);

        try {
            selectorLeft.setImage(new Image(getClass().getResource("/img/left_arrow.png").toURI().toString()));
            selectorRight.setImage(new Image(getClass().getResource("/img/right_arrow.png").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        selectorLeft.setPickOnBounds(false);
        selectorRight.setPickOnBounds(false);
        ColorAdjust hover = new ColorAdjust();
        hover.setBrightness(0.3);

        ColorAdjust placebo = new ColorAdjust(); // This is needed for some reason, couldn't find another way

        selectorLeft.effectProperty().bind(
                Bindings
                        .when(selectorLeft.hoverProperty())
                        .then((Effect) hover)
                        .otherwise(placebo)
        );
        selectorRight.effectProperty().bind(
                Bindings
                        .when(selectorRight.hoverProperty())
                        .then((Effect) hover)
                        .otherwise(placebo)
        );
        selectorCancel.effectProperty().bind(
                Bindings
                        .when(selectorCancel.hoverProperty())
                        .then((Effect) hover)
                        .otherwise(placebo)
        );
        selectorConfirm.effectProperty().bind(
                Bindings
                        .when(selectorConfirm.hoverProperty())
                        .then((Effect) hover)
                        .otherwise(placebo)
        );

        selectorLeft.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            selectorGoLeft();
            event.consume();
        });
        selectorRight.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            selectorGoRight();
            event.consume();
        });
        selectorCancel.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            deselect();
            selectionCancel();
            update();
            event.consume();
        });
        selectorConfirm.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            selectionConfirm();
            event.consume();
        });
        objectiveIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            displayObjective();
            event.consume();
        });
        objectiveIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            objectiveDisplay.setVisible(false);
            event.consume();
        });
        objectiveIcon.setVisible(false);
        passButton.setOnAction(this);

        troopNumberPane.setVisible(false);
        objectiveDisplay.setVisible(false);
        objectiveResult.setOpacity(0.0);
        objectiveResult.setMouseTransparent(true);
        abilityButton.setDisable(true);
        abilityButton.setOnAction(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
        for(int i = 0; i < players.size(); i++){
            pIconBgs[i].setFill(players.get(i).getColor());
            pInfoBgs[i].setFill(players.get(i).getColor());
            try {
                pIcons[i].setImage(new Image(getClass().getResource(players.get(i).getFaculty().getIconName()).toURI().toString()));
                pTerritoryIcons[i].setImage(new Image(getClass().getResource("/img/territory_icon.png").toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

        // Set extra player information invisible
        for(int i = players.size(); i < 6; i++){
            pIconBgs[i].setVisible(false);
            pInfoBgs[i].setVisible(false);
            pIcons[i].setVisible(false);
        }
    }

    public void setMap(GameMap map){
        this.map = map;
    }

    public void setGameEngine(GameEngine gameEngine){
        this.gameEngine = gameEngine;
    }

    public void update(){
        setPlayers(gameEngine.getPlayers());
        for(int i = 0; i < territories.length; i++){
            // Update territory colors
            ColorAdjust base;
            ColorAdjust hover;
            if(GameMap.getInstance().getTerritory(i).getNumOfArmies() == 0){
                base = new ColorAdjust();
                base.setSaturation(-0.95);
                ColorAdjust pCa = GameEngine.getInstance().getCurrentPlayer().getCa();
                hover = new ColorAdjust(pCa.getHue(), pCa.getSaturation() - 0.5, pCa.getBrightness(), pCa.getContrast());
            }else {
                base = map.getTerritories()[i].getCa();
                hover = new ColorAdjust(base.getHue(), base.getSaturation(), base.getBrightness() + 0.25, base.getContrast());

            }

            territories[i].effectProperty().bind(
                    Bindings
                            .when(territories[i].hoverProperty())
                            .then((Effect) hover)
                            .otherwise(base)
            );
        }

        if(sourceTerritory != -1){
            ColorAdjust base = map.getTerritories()[sourceTerritory].getCa();
            ColorAdjust selected = new ColorAdjust(base.getHue(), base.getSaturation(), base.getBrightness() + -0.5, base.getContrast());
            territories[sourceTerritory].effectProperty().bind(
                    Bindings
                            .when(territories[sourceTerritory].hoverProperty())
                            .then((Effect) selected)
                            .otherwise(selected)
            );
        }

        if(destinationTerritory != -1){
            ColorAdjust base = map.getTerritories()[sourceTerritory].getCa();
            ColorAdjust selected = new ColorAdjust(base.getHue(), base.getSaturation(), base.getBrightness() + -0.5, base.getContrast());
            territories[sourceTerritory].effectProperty().bind(
                    Bindings
                            .when(territories[sourceTerritory].hoverProperty())
                            .then((Effect) selected)
                            .otherwise(selected)
            );
        }

        // Update player territory counts
        int i;
        for(i = 0; i < players.size(); i++){
            pTerritoryCounts[i].setText(Integer.toString(players.get(i).getNumOfTerritory()));
        }

        // Make eliminated player info invisible
        for(; i < 6; i++){
            pTerritoryCounts[i].setVisible(false);
            pIcons[i].setVisible(false);
            pIconBgs[i].setVisible(false);
            pInfoBgs[i].setVisible(false);
            pTerritoryIcons[i].setVisible(false);
        }

        // Update territory troop numbers
        for(i = 0; i < troops.length; i++){
            troops[i].setText(Integer.toString(map.getTerritory(i).getNumOfArmies()));
        }

        // Stick out current player's infobg out a bit
        for(i = 0; i < players.size(); i++){
            double DEFAULT_INFOBG_X = 41.0;
            if(i == curTurn){
                pInfoBgs[i].setLayoutX(DEFAULT_INFOBG_X - 50);
            } else {
                pInfoBgs[i].setLayoutX(DEFAULT_INFOBG_X);
            }
        }
    }

    public void displayBattleResult(int[] attackerDice, int[] defenderDice, boolean[] attackerWon, Player attacker, Player defender){
        // Set all image views for the dice invisible, set dice effects to null
        abilityButton.setDisable(true);
        for(ImageView iv : attackerDiceImages){
            iv.setVisible(false);
            iv.setEffect(null);
        }

        for(ImageView iv : defenderDiceImages){
            iv.setVisible(false);
            iv.setEffect(null);
        }

        // Set images for available dice and set visible
        for(int i = 0; i < attackerDice.length; i++){
            try {
                attackerDiceImages[i].setImage(new Image(getClass().getResource("/img/die_" + attackerDice[i] + ".png").toURI().toString()));
                attackerDiceImages[i].setVisible(true);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < defenderDice.length; i++){
            try {
                defenderDiceImages[i].setImage(new Image(getClass().getResource("/img/die_" + defenderDice[i] + ".png").toURI().toString()));
                defenderDiceImages[i].setVisible(true);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        // Set images for attacking and defending players
        try {
            attackerIcon.setImage(new Image(getClass().getResource(attacker.getFaculty().getIconName()).toURI().toString()));
            defenderIcon.setImage(new Image(getClass().getResource(defender.getFaculty().getIconName()).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Gray out losing and unused dice, count attacker wins
        ColorAdjust grayOut = new ColorAdjust();
        grayOut.setSaturation(-1.0);
        int won = 0;
        int numberOfBattles = Math.min(attackerDice.length,defenderDice.length);
        for(int i = 0; i < numberOfBattles; i++){
            if(attackerWon[i]){
                defenderDiceImages[i].setEffect(grayOut);
                won++;
            }
            else{
                attackerDiceImages[i].setEffect(grayOut);
            }
        }

        if(attackerDice.length - defenderDice.length > 0){
            if(defenderDice.length == 1){
                attackerDiceImages[1].setEffect(grayOut);
            }
            attackerDiceImages[2].setEffect(grayOut);
        }
        else{
            defenderDiceImages[1].setEffect(grayOut);
        }

        if(won > numberOfBattles / 2){
            attackerCircle.setStyle("-fx-fill: green; -fx-opacity: 0.5;");
            defenderCircle.setStyle("-fx-opacity: 0;");
        } else if (won == numberOfBattles / 2){
            attackerCircle.setStyle("-fx-fill: yellow; -fx-opacity: 0.5;");
            defenderCircle.setStyle("-fx-fill: yellow; -fx-opacity: 0.5;");
        } else {
            defenderCircle.setStyle("-fx-fill: green; -fx-opacity: 0.5;");
            attackerCircle.setStyle("-fx-opacity: 0;");
        }

        // Set mapBlocker active
        mapBlocker.setVisible(true);
        mapBlocker.setMouseTransparent(false);

        // Set battleResultPane visible
        battleResultPane.setVisible(true);
        battleResultPane.setMouseTransparent(false);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if(actionEvent.getSource() == battleOKButton){
            deselect();
            updateAbilityButton();
            battleResultPane.setVisible(false);
            battleResultPane.setMouseTransparent(true);
            mapBlocker.setVisible(false);
            mapBlocker.setMouseTransparent(true);
            WarState.getInstance().terminating();
        } else if (actionEvent.getSource() == passButton){
            deselect();
            gameEngine.pass();
        } else if (actionEvent.getSource() == abilityButton){
            GameEngine.getInstance().getCurrentPlayer().getFaculty().useAbility();
            updateAbilityButton();
        }
    }

    public void displayTroopSelector(int max){
        disablePassButton();
        selectorCancel.setVisible(true);
        selectorCancel.setDisable(false);
        selectionMax = max;
        selectionMin = 1;
        selectorPane.setVisible(true);
        selectorPane.setMouseTransparent(false);
        troopBlocker.setVisible(true);
        troopBlocker.setMouseTransparent(false);
        if(max == 1){
            selectionLabels[0].setVisible(false);
            selectionLabels[1].setText("1");
            selectionLabels[1].setVisible(true);
            selectionLabels[2].setVisible(false);
            selectorRight.setDisable(true);
            selectorLeft.setDisable(true);
            selectorRight.setVisible(false);
            selectorLeft.setVisible(false);
        } else if (max == 2) {
            selectionLabels[0].setVisible(false);
            selectionLabels[1].setText("1");
            selectionLabels[1].setVisible(true);
            selectionLabels[2].setText("2");
            selectionLabels[2].setVisible(true);
            selectorRight.setDisable(false);
            selectorLeft.setDisable(true);
            selectorRight.setVisible(true);
            selectorLeft.setVisible(false);
        } else {
            selectionLabels[0].setText("1");
            selectionLabels[0].setVisible(true);
            selectionLabels[1].setText("2");
            selectionLabels[1].setVisible(true);
            selectionLabels[2].setText("3");
            selectionLabels[2].setVisible(true);
            selectorRight.setDisable(false);
            selectorLeft.setDisable(false);
            selectorRight.setVisible(true);
            selectorLeft.setVisible(true);
        }
    }

    public void displayTroopSelector(int min, int max){
        disablePassButton();
        selectorCancel.setVisible(true);
        selectorCancel.setDisable(false);
        selectionMax = max;
        selectionMin = min;
        selectorPane.setVisible(true);
        selectorPane.setMouseTransparent(false);
        troopBlocker.setVisible(true);
        troopBlocker.setMouseTransparent(false);
        int distance = max - min + 1;
        if(distance == 1){
            selectionLabels[0].setVisible(false);
            selectionLabels[1].setText(Integer.toString(selectionMin));
            selectionLabels[1].setVisible(true);
            selectionLabels[2].setVisible(false);
            selectorRight.setDisable(true);
            selectorLeft.setDisable(true);
            selectorRight.setVisible(false);
            selectorLeft.setVisible(false);
        } else if (distance == 2) {
            selectionLabels[0].setVisible(false);
            selectionLabels[1].setText(Integer.toString(selectionMin));
            selectionLabels[1].setVisible(true);
            selectionLabels[2].setText(Integer.toString(selectionMin + 1));
            selectionLabels[2].setVisible(true);
            selectorRight.setDisable(false);
            selectorLeft.setDisable(true);
            selectorRight.setVisible(true);
            selectorLeft.setVisible(false);
        } else {
            selectionLabels[0].setText(Integer.toString(selectionMin));
            selectionLabels[0].setVisible(true);
            selectionLabels[1].setText(Integer.toString(selectionMin + 1));
            selectionLabels[1].setVisible(true);
            selectionLabels[2].setText(Integer.toString(selectionMin + 2));
            selectionLabels[2].setVisible(true);
            selectorRight.setDisable(false);
            selectorLeft.setDisable(false);
            selectorRight.setVisible(true);
            selectorLeft.setVisible(true);
        }
    }

    public void setTurn(int i){
        curTurn = i;
        update();
    }

    public void selectorGoLeft(){
        selectionLabels[2].setText(selectionLabels[1].getText());
        selectionLabels[2].setVisible(true);
        selectionLabels[1].setText(selectionLabels[0].getText());
        if(Integer.parseInt(selectionLabels[0].getText()) > selectionMin){
            selectionLabels[0].setText(Integer.toString(Integer.parseInt(selectionLabels[0].getText()) - 1));
        }
        else{
            selectionLabels[0].setVisible(false);
            selectorLeft.setVisible(false);
            selectorLeft.setDisable(true);
        }
        selectorRight.setDisable(false);
        selectorRight.setVisible(true);
    }

    public void selectorGoRight(){
        selectionLabels[0].setText(selectionLabels[1].getText());
        selectionLabels[0].setVisible(true);
        selectionLabels[1].setText(selectionLabels[2].getText());
        if(Integer.parseInt(selectionLabels[2].getText()) < selectionMax){
            selectionLabels[2].setText(Integer.toString(Integer.parseInt(selectionLabels[2].getText()) + 1));
        }
        else{
            selectionLabels[2].setVisible(false);
            selectorRight.setVisible(false);
            selectorRight.setDisable(true);
        }
        selectorLeft.setVisible(true);
        selectorLeft.setDisable(false);
    }

    public void selectionCancel(){
        selectorPane.setVisible(false);
        selectorPane.setMouseTransparent(true);
        troopBlocker.setVisible(false);
        troopBlocker.setMouseTransparent(true);
        gameEngine.enablePassButton();
        gameEngine.back();
    }

    public void selectionConfirm(){
        selectorPane.setVisible(false);
        selectorPane.setMouseTransparent(true);
        troopBlocker.setVisible(false);
        troopBlocker.setMouseTransparent(true);
        gameEngine.setArmyCount(Integer.parseInt(selectionLabels[1].getText()));
        update();
    }

    // state = -1 for initial placement 0 for placement, 1 for attack, 2 for fortify
    public void setState(int state){
        curState = state;
        curTurn = gameEngine.getPlayerTurn();
        setTurn(curTurn);
        Player current = players.get(curTurn);
        abilityButton.setDisable(!current.getFaculty().canUseAbility());
        setAbilityTooltip();
        objectiveIcon.setVisible(true);
        if(state == -1){
            passButton.setDisable(true);
            troopNumberPane.setVisible(true);
            objectiveIcon.setVisible(false);
            try {
                stateIcon.setImage(new Image(getClass().getResource(current.getFaculty().getIconName()).toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            stateBg.setFill(current.getColor());
            state0.setFill(Color.DARKGRAY);
            state0.setOpacity(0.3);
            state1.setFill(Color.DARKGRAY);
            state1.setOpacity(0.3);
            state2.setFill(Color.DARKGRAY);
            state2.setOpacity(0.3);
            stateLabel.setText("INITIAL");
            stateLabel.setLayoutX(155.0);
        }else if(state == 0){
            troopNumberPane.setVisible(true);
            passButton.setDisable(true);
            try {
                stateIcon.setImage(new Image(getClass().getResource(current.getFaculty().getIconName()).toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            stateBg.setFill(current.getColor());
            state0.setFill(current.getColor());
            state0.setOpacity(1);
            state1.setFill(Color.DARKGRAY);
            state1.setOpacity(0.3);
            state2.setFill(Color.DARKGRAY);
            state2.setOpacity(0.3);
            stateLabel.setText("PLACEMENT");
            stateLabel.setLayoutX(134.0);
        } else if (state == 1){
            troopNumberPane.setVisible(false);
            passButton.setDisable(false);
            state1.setFill(current.getColor());
            state1.setOpacity(1);
            state0.setFill(Color.DARKGRAY);
            state0.setOpacity(0.3);
            state2.setFill(Color.DARKGRAY);
            state2.setOpacity(0.3);
            stateLabel.setText("ATTACK");
            stateLabel.setLayoutX(157.0);
        } else if (state == 2){
            troopNumberPane.setVisible(false);
            passButton.setDisable(false);
            state2.setFill(current.getColor());
            state2.setOpacity(1);
            state0.setFill(Color.DARKGRAY);
            state0.setOpacity(0.3);
            state1.setFill(Color.DARKGRAY);
            state1.setOpacity(0.3);
            stateLabel.setText("FORTIFY");
            stateLabel.setLayoutX(157.0);
        }
    }

    public void disableCancel(){
        selectorCancel.setVisible(false);
        selectorCancel.setDisable(true);
    }

    public void displayObjective(){
        objectiveDisplay.setVisible(true);
        objectiveText.setText(GameEngine.getInstance().getCurrentPlayer().getObjective().getName());
        objectiveTurns.setText(GameEngine.getInstance().getCurrentPlayer().getObjective().getRemainingTurn() + " turn/s remaining.");
        objectiveReward.setText("Reward: " + GameEngine.getInstance().getCurrentPlayer().getObjective().getBonus() + " troops");
    }

    public void updateTroopNumber(int i){
        troopNumberLabel.setText(Integer.toString(i));
    }

    public void displayObjectiveSuccess(int reward){
        FadeTransition fade = new FadeTransition();
        objectiveResultLabel.setText("Objective Completed!");
        objectiveRewardLabel.setText("You receive " + reward + " troops");
        fade.setDuration(Duration.seconds(10));
        fade.setCycleCount(1);
        fade.setAutoReverse(false);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setNode(objectiveResult);
        fade.play();
    }

    public void displayObjectiveFail(){
        FadeTransition fade = new FadeTransition();
        objectiveResultLabel.setText("Objective Failed!");
        objectiveRewardLabel.setText("You receive nothing...");
        fade.setDuration(Duration.seconds(10));
        fade.setCycleCount(1);
        fade.setAutoReverse(false);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setNode(objectiveResult);
        fade.play();
    }

    public void setSourceTerritory(int i){
        sourceTerritory = i;
    }

    public void setDestinationTerritory(int i){
        destinationTerritory = i;
    }

    public void deselect(){
        sourceTerritory = -1;
        destinationTerritory = -1;
    }

    public void updateAbilityButton(){
        abilityButton.setDisable(!GameEngine.getInstance().getCurrentPlayer().getFaculty().canUseAbility());
    }

    public void disablePassButton(){
        passButton.setDisable(true);
    }

    public void enablePassButton(){
        passButton.setDisable(false);
    }

    public void setAbilityTooltip(){
        Tooltip tp = new Tooltip(ABILITY_DESCRIPTIONS[GameEngine.getInstance().getCurrentPlayer().getFaculty().getSaveId()]);
        tp.setHideDelay(new Duration(0));
        tp.setShowDelay(new Duration(0));
        tp.setId("ability_tooltip");
        tp.setShowDuration(Duration.INDEFINITE);
        tp.setStyle("-fx-font-weight: normal;");
        Tooltip.install(abilityTooltip, tp);
        Tooltip.install(abilityButton, tp);
    }
}