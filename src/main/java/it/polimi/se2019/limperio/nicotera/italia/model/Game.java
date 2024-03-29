package it.polimi.se2019.limperio.nicotera.italia.model;


import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.*;
import it.polimi.se2019.limperio.nicotera.italia.utils.Observable;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Contains all of the informations about the game
 *
 * @author Pietro L'Imperio
 */
public class Game extends Observable<ServerEvent> {

    /**
     * It's true if is enable the terminator mode, false otherwise. False for default
     */
    private boolean terminatorModeActive = false;
    /**
     * The reference of the board
     */
    private Board board;
    /**
     * The list of players in the game
     */
    private ArrayList<Player> players = new ArrayList<>();
    /**
     * It's true if the game is in the frenzy phase, false otherwise. False for default
     */
    private boolean isInFrenzy = false;
    /**
     * The instance of the game in order to implement the Singleton pattern
     */
    private static Game instanceOfGame = null;
    /**
     * The number of the player is playing the turn
     */
    private int playerOfTurn=1;
    /**
     * The current number of the action did by the player of the turn
     */
    private int numOfActionOfTheTurn=0;
    /**
     * It's true when the game is over, false otherwise. False for default
     */
    private boolean isGameOver = false;
    /**
     * It's true if there will be the frenzy phase at the end of the game, false otherwise
     */
    private int numOfSkullToRemoveToPassToFrenzy = 7;

    private boolean anticipatedFrenzy;
    /**
     * The number of the player that is the first at the beginning of the frenzy phase
     */
    private int firstInFrenzyMode;
    /**
     * The list of nicknames of the players in game
     */
    private ArrayList<String> listOfNickname = new ArrayList<>();
    /**
     * The number of the maximum actions that a player could do during a turn. 2 for default.
     */
    private int numOfMaxActionForTurn = 2;
    /**
     * The number of in which round is the game
     */
    private int round=1;

    /**
     * The delay for the timer of the turn.
     */
    private long delay;

    /**
     * It's true if the player of the turn has still to do the terminator action.
     */
    private boolean hasToDoTerminatorAction = false;

    /**
     * The reference of the player that has to be spawn before the start of the turn of the current player.
     */
    private Player playerHasToRespawn = null;

    /**
     * Logger of the class to track possibly excpetion.
     */
    private static Logger myLogger = Logger.getLogger("it.limperio.nicotera.italia.progettoINGSFTWPolimi");
    /**
     * The handler of the logger.
     */
    private static Handler handlerGame = new ConsoleHandler();


    public Game(){
        myLogger.addHandler(handlerGame);
    }


    /**
     * Handles the beginning of the game with the initialization of its parameter and the creation of
     * player boards for each player, map and killshot track. It creates moreover the powerUp deck,
     * weapons deck and ammo tiles deck.
     * @param anticipatedFrenzy The boolean value that indicate if there will be the last phase in frenzy mode
     * @param typeMap The type of map
     * @param terminatorModeActive The boolean value that indicate if there will be the terminator mode if the number of players is 3
     */
    public void initializeGame(boolean anticipatedFrenzy, int typeMap, boolean terminatorModeActive){
        try {
            BufferedReader timerReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/timer/timerForTurn.txt")));
            delay = Long.parseLong(timerReader.readLine());
            timerReader.close();
            BufferedReader numOfSkullReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/textfile/numOfSkull.txt")));
            numOfSkullToRemoveToPassToFrenzy = Integer.parseInt(numOfSkullReader.readLine());
            numOfSkullReader.close();

        } catch (IOException e) {
            myLogger.log(Level.ALL, "error");
        }
        this.anticipatedFrenzy=anticipatedFrenzy;
        setListOfNickname();
        if(players.size()==3 || players.size()==4)
            this.terminatorModeActive = terminatorModeActive;
        if(terminatorModeActive){
            players.add(new Player("terminator", false, players.size()+1, findColorAvailable()));
        }
        int position=1;
        PlayerBoardEvent pbEvent;
        for (Player player : players){
            player.setPosition(position);
            player.createPlayerBoard();
            pbEvent = new PlayerBoardEvent();
            pbEvent.setNicknameInvolved(player.getNickname());
            pbEvent.setNicknames(listOfNickname);
            pbEvent.setPlayerBoard(player.getPlayerBoard());
            notify(pbEvent);
            position++;
        }
        createBoard();
        board.createWeaponsDeck();
        board.createMap(typeMap);
        board.createKillShotTrack();
        KillshotTrackEvent killshotTrackEvent = new KillshotTrackEvent("", board.getKillShotTrack());
        killshotTrackEvent.setNicknamePlayerOfTheTurn(players.get(playerOfTurn-1).getNickname());
        killshotTrackEvent.setNicknames(listOfNickname);
        notify(killshotTrackEvent);
        board.createAmmoTileDeck();
        board.createPowerUpDeck();
        board.addAmmoTileInNormalSquare();
        board.addWeaponsInSpawnSquare();
        sendMapEvent();
    }

    /**
     * Finds an available color of figure to give it to the terminator.
     */
    private ColorOfFigure_Square findColorAvailable() {
        ArrayList<ColorOfFigure_Square> colors = new ArrayList<>();
        colors.add(ColorOfFigure_Square.YELLOW);
        colors.add(ColorOfFigure_Square.BLUE);
        colors.add(ColorOfFigure_Square.GREEN);
        colors.add(ColorOfFigure_Square.GREY);
        colors.add(ColorOfFigure_Square.PURPLE);
        for (Player player : players){
            colors.remove(player.getColorOfFigure());
        }
        return colors.get(0);
    }



    /**
     * Update the map and send an event of type {@link MapEvent}
     */
    public void sendMapEvent(){
        MapEvent mapEvent = new MapEvent();
        mapEvent.setTerminatorMode(isTerminatorModeActive());
        mapEvent.setNicknames(listOfNickname);
        mapEvent.setNicknameInvolved(players.get(playerOfTurn-1).getNickname());
        mapEvent.setMap(board.getMap().getMatrixOfSquares());
        mapEvent.setTypeOfMap(board.getMap().getTypeOfMap());
        notify(mapEvent);
    }


    /**
     * Creates the instance of the Board.
     */
    private void createBoard(){
        this.board = Board.instanceOfBoard();
    }


    /**
     * Creates the instance of the game.
     */
    public static Game instanceOfGame(){
        if(instanceOfGame == null)
             instanceOfGame = new Game();
        return instanceOfGame;
    }

    /**
     * Increments the number of the action of the turn.
     */
    public void incrementNumOfActionsOfThisTurn(){
        numOfActionOfTheTurn++;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getNumOfSkullToRemoveToPassToFrenzy() {
        return numOfSkullToRemoveToPassToFrenzy;
    }

    /**
     * Creates player in accordance with the parameter
     * @param nickname The nickname of the player
     * @param isFirst True if it is the first, false otherwhise
     * @param position The position in the round
     * @param color The color of the figure of the player
     */
    public void createPlayer(String nickname, boolean isFirst, int position, String color){
        ColorOfFigure_Square colorOfThisPlayer;
        switch (color){
            case "YELLOW":
                colorOfThisPlayer=ColorOfFigure_Square.YELLOW;
                break;
            case "BLUE":
                colorOfThisPlayer=ColorOfFigure_Square.BLUE;
                break;
            case "GREEN":
                colorOfThisPlayer=ColorOfFigure_Square.GREEN;
                break;
            case "PURPLE":
                colorOfThisPlayer=ColorOfFigure_Square.PURPLE;
                break;
            case "GREY":
                colorOfThisPlayer=ColorOfFigure_Square.GREY;
                break;
            default: throw new IllegalArgumentException();
        }
        players.add(new Player(nickname, isFirst, position, colorOfThisPlayer));
    }

    /**
     * Adds for each player his nickname
     */
    public void setListOfNickname() {
        for(Player player : players)
        {
            listOfNickname.add(player.getNickname());
        }
    }

    public boolean isAnticipatedFrenzy() {
        return anticipatedFrenzy;
    }

    public int getPlayerOfTurn() {
        return playerOfTurn;
    }

    public Player getPlayerHasToRespawn() {
        return playerHasToRespawn;
    }

    public void setPlayerHasToRespawn(Player playerHasToRespawn) {
        this.playerHasToRespawn = playerHasToRespawn;
    }

    public void setGameOver(boolean over) {
        isGameOver = over;
    }

    public boolean isHasToDoTerminatorAction() {
        return hasToDoTerminatorAction;
    }

    public void setHasToDoTerminatorAction(boolean hasToDoTerminatorAction) {
        this.hasToDoTerminatorAction = hasToDoTerminatorAction;
    }

    public int getRound() {
        return round;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public int getNumOfActionOfTheTurn() {
        return numOfActionOfTheTurn;
    }

    public boolean isInFrenzy() {
        return isInFrenzy;
    }

    public void setInFrenzy(boolean inFrenzy) {
        isInFrenzy = inFrenzy;
    }

    public void setFirstInFrenzyMode(int firstInFrenzyMode) {
        this.firstInFrenzyMode = firstInFrenzyMode;
    }

    public int getFirstInFrenzyMode() {
        return firstInFrenzyMode;
    }

    public ArrayList<String> getListOfNickname() {
        return listOfNickname;
    }

    public int getNumOfMaxActionForTurn() {
        return numOfMaxActionForTurn;
    }

    public boolean isTerminatorModeActive() {
        return terminatorModeActive;
    }


    public long getDelay() {
        return delay;
    }

    public void setPlayerOfTurn(int playerOfTurn) {
        this.playerOfTurn = playerOfTurn;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setNumOfActionOfTheTurn(int numOfActionOfTheTurn) {
        this.numOfActionOfTheTurn = numOfActionOfTheTurn;
    }

    public void setNumOfMaxActionForTurn(int numOfMaxActionForTurn) {
        this.numOfMaxActionForTurn = numOfMaxActionForTurn;
    }

    public void setInstanceOfGameNullForTesting(){
        instanceOfGame = null;
    }

}
