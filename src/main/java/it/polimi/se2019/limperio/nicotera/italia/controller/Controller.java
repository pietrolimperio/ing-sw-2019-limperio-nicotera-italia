package it.polimi.se2019.limperio.nicotera.italia.controller;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_client.*;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.RequestActionEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.ServerEvent;
import it.polimi.se2019.limperio.nicotera.italia.model.*;
import it.polimi.se2019.limperio.nicotera.italia.utils.Observer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for checking the correctness of the action of the players, according to MVC pattern
 * @author Pietro L'Imperio
 *
 */
public class Controller implements Observer<ClientEvent> {

    /**
     * The logger of the class to track possibly exception.
     */
    private static Logger myLogger = Logger.getLogger("it.limperio.nicotera.italia.progettoINGSFTWPolimi");
    /**
     * The handler of the logger.
     */
    private static ConsoleHandler consoleHandler = new ConsoleHandler();
    /**
     * The reference of the game.
     */
    private final Game game;
    /**
     * The reference of the catch controller.
     */
    private CatchController catchController;
    /**
     * The reference of the power up controller.
     */
    private PowerUpController powerUpController;
    /**
     * The reference of the round controller.
     */
    private RoundController roundController;
    /**
     * The reference of the run controller.
     */
    private RunController runController;
    /**
     * The reference of the shoot controller.
     */
    private ShootController shootController;
    /**
     * The reference of the weapon controller.
     */
    private WeaponController weaponController;
    /**
     * The reference of the terminator controller.
     */
    private TerminatorController terminatorController;
    /**
     * The reference of the death controller.
     */
    private DeathController deathController;
    /**
     * The reference of the reload controller.
     */
    private ReloadController reloadController;
    /**
     * Timer for the turn.
     */
    private Timer timer = null;
    /**
     * Task that starts at the end of the timer for the turn.
     */
    private TurnTask turnTask;
    /**
     * It's true if has been already asked to realod to the player, false otherwise.
     */
    private boolean isAlreadyAskedToReload = false;

    /**
     * Constructor of the class: it creates the instances of the other controller classes
     * @param game: the reference to model part of MVC pattern
     */
    public Controller(Game game) {
        this.game = game;
        catchController = new CatchController(game, this);
        powerUpController = new PowerUpController(game, this);
        roundController = new RoundController(this,game);
        runController = new RunController(game, this);
        shootController = new ShootController(game, this);
        weaponController = new WeaponController(game, this);
        deathController = new DeathController(game, this);
        reloadController = new ReloadController(game, this);
        myLogger.addHandler(consoleHandler);


    }


    /**
     * This method finds the player who has the nickname that is passed as parameter
     * @param nickname the nickname of the player that is looked for
     * @return the player that is looked for
     * @throws IllegalArgumentException when nickname parameter isn't associated to a player
     */
    public Player findPlayerWithThisNickname (String nickname){
        for(Player player : game.getPlayers()){
            if(player.getNickname().equals(nickname)){
                return player;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Finds the squares that can be reached with the number of movements that are passed as parameter
     * @param currentPosition start position
     * @param numOfMovements number of the movements usable to reach the squares
     * @param listOfSquareReachable It is filled with the reachable squares
     */
    void findSquaresReachableWithThisMovements(Square currentPosition, int numOfMovements, ArrayList<Square> listOfSquareReachable){
        if(numOfMovements == 0 && !listOfSquareReachable.contains(currentPosition)){
            listOfSquareReachable.add(0,currentPosition);
            return;
        }
        for(Square square : currentPosition.getAdjSquares()) {
            if(numOfMovements-1 >= 0)
            {
                findSquaresReachableWithThisMovements(square, numOfMovements -1, listOfSquareReachable);

            }
        }
        if(!listOfSquareReachable.contains(currentPosition)){
            listOfSquareReachable.add(0,currentPosition);
        }

    }


    /**
     * This method checks if is the turn of the player who has the nickname that is passed
     * @param nickname the nickname of the player
     * @return boolean that is true if it is the turn of the player with the nickname parameter
     */
    //
      boolean isTheTurnOfThisPlayer(String nickname){
        return nickname.equals(game.getPlayers().get(game.getPlayerOfTurn()-1).getNickname());
    }

    ArrayList<ServerEvent.AliasCard> substituteWeaponsCardWithTheirAlias(ArrayList<WeaponCard> weapons){
        ArrayList<ServerEvent.AliasCard> weaponsAsAlias = new ArrayList<>();
        for(WeaponCard weapon : weapons){
            weaponsAsAlias.add(new ServerEvent.AliasCard(weapon.getName(), weapon.getDescription(), weapon.getColor()));
        }
        return weaponsAsAlias;
    }


    /**
     * Handles the end of the action and checks if the player has to do other actions sending the correct request or calls the update of the turn through the round controller.
     * @param endOfUsePUCard It's true if the method is called following the use of a power up card.
     */
    void handleTheEndOfAnAction(boolean endOfUsePUCard){
         Player player = game.getPlayers().get(game.getPlayerOfTurn()-1);
        if(!endOfUsePUCard)
            game.incrementNumOfActionsOfThisTurn();

        if(game.getNumOfActionOfTheTurn()<game.getNumOfMaxActionForTurn()){
            sendRequestForAction();
            return;
        }
            if (!game.isInFrenzy() && reloadController.playerCanReload(player) && !isAlreadyAskedToReload) {
                reloadController.sendRequestToReload(player, true);
                isAlreadyAskedToReload = true;

        }
        else {
            isAlreadyAskedToReload = false;
            if(timer!=null)
                timer.cancel();
            timer = null;
            turnTask = null;
            roundController.updateTurn();
            if (!isSomeoneDied() && !game.isGameOver()) {
                    if (game.getRound() > 1)
                        sendRequestForAction();
                    else {
                        sendRequestToDrawPowerUpCard(game.getPlayers().get(game.getPlayerOfTurn() - 1), 2);
                    }
            }
        }
    }

    /**
     * Check if someone during the turn died.
     * @return True if some players died during the turn, false otherwise.
     */
    private boolean isSomeoneDied() {
         for(Player player : game.getPlayers()){
             if(player.isDead())
                 return true;
         }
         return false;
    }


    /**
     * Sends to the player the request to do an action, checking what he can do and what he can't.
     */
    void sendRequestForAction() {
         if(game.getRound()>1 && game.getNumOfActionOfTheTurn()==0 && timer!=null) {

             timer.cancel();
             timer = null;
             turnTask = null;

         }
        RequestActionEvent requestActionEvent = new RequestActionEvent();
        requestActionEvent.setNicknameInvolved(game.getPlayers().get(game.getPlayerOfTurn() - 1).getNickname());
        requestActionEvent.setNumOfAction(game.getNumOfActionOfTheTurn() + 1);
        if (game.getRound() > 1 && game.getNumOfActionOfTheTurn() == 0) {
            requestActionEvent.setMessageForInvolved("It's your turn! Choose you first action!");
            requestActionEvent.setMessageForOthers("Change turn! Now it's the turn of " + game.getPlayers().get(game.getPlayerOfTurn() - 1).getNickname() + "\nWait for some news!");
            requestActionEvent.setNicknames(game.getListOfNickname());
        } else
            requestActionEvent.setMessageForInvolved("Choose your action number " + requestActionEvent.getNumOfAction() + " you want to do.\nRemember you can use enabled power up cards .");
        requestActionEvent.setCanUseNewton(!(game.getRound() == 1 && game.getPlayerOfTurn() == 1));
        requestActionEvent.setCanUseTeleporter(true);
        requestActionEvent.setCanShoot(checkIfPlayerCanShoot(findPlayerWithThisNickname(requestActionEvent.getNicknameInvolved()).getPlayerBoard().getWeaponsOwned()));
        requestActionEvent.setHasToDoTerminatorAction(game.isHasToDoTerminatorAction());
        if (game.isHasToDoTerminatorAction() && game.getNumOfActionOfTheTurn() + 1 == game.getNumOfMaxActionForTurn()) {
            requestActionEvent.setCanCatch(false);
            requestActionEvent.setCanRun(false);
            requestActionEvent.setCanShoot(false);
        } else {
            requestActionEvent.setCanCatch(true);
            if(!game.isInFrenzy())
                requestActionEvent.setCanRun(true);
            else
                requestActionEvent.setCanRun(game.getFirstInFrenzyMode()!=1 && game.getPlayers().get(game.getPlayerOfTurn() - 1).getPosition() >= game.getFirstInFrenzyMode());

        }
        game.notify(requestActionEvent);
        if (game.getNumOfActionOfTheTurn() == 0 && game.getRound() > 1) {
            setTimerForTurn(false,false);
        }



    }

    /**
     * Sends to the player the request to draw a power up card to be spawn.
     * @param playerHasToDraw The player that has to draw the power up card.
     * @param numOfPowerUpCardToDraw The number of power up cards that the player has to draw.
     */
    public void sendRequestToDrawPowerUpCard(Player playerHasToDraw, int numOfPowerUpCardToDraw) {
        playerHasToDraw.setHasToBeGenerated(true);
        if(timer!=null){
            timer.cancel();
            timer=null;
            turnTask = null;
        }
        String messageForInvolved;
        String messageForOthers;
        ServerEvent event = new ServerEvent();

        if(numOfPowerUpCardToDraw==1){
            messageForInvolved = "Draw a power up card and then discard one of yours to decide where you will be spawn";
            messageForOthers = playerHasToDraw.getNickname() + " has to draw power up card to decide where he'll be spawn";
            event.setRequestForDrawOnePowerUpCardEvent();
        }
        else{
            messageForInvolved = "Let's start! \nIt's your first turn and you have to draw two powerUp cards to decide where you will spawn. \nPress DRAW to draw powerUp cards!";
            messageForOthers = "Wait! It's not your turn but the turn of " + game.getPlayers().get(game.getPlayerOfTurn() - 1).getNickname() + ". Press OK and wait for some news!";
            event.setRequestForDrawTwoPowerUpCardsEvent();
        }

        event.setMessageForInvolved(messageForInvolved);
        event.setMessageForOthers(messageForOthers);
        event.setNicknames(game.getListOfNickname());
        event.setNicknameInvolved(playerHasToDraw.getNickname());
        game.notify(event);
        setTimerForTurn(numOfPowerUpCardToDraw==2,numOfPowerUpCardToDraw==1);


    }


    /**
     * Check if the weapon passed by parameter is usable in the current situation of the player that own it.
     * @param weaponCard Weapon card involved in the check.
     * @param movementCanDoBeforeReloadAndShoot The movement that the player could do before to shoot.
     * @return True if the weapon is usable by player, false otherwise.
     */
     boolean checkIfThisWeaponIsUsable(WeaponCard weaponCard, int movementCanDoBeforeReloadAndShoot) {
         if(game.getRound()==1 && game.getPlayerOfTurn()==1)
            return false;
         if(!weaponCard.isLoad() && !game.isInFrenzy() || !weaponCard.isLoad() && game.isInFrenzy() && !reloadController.isThisWeaponReloadable(weaponCard))
             return false;
         return (weaponController.isThisWeaponUsable(weaponCard, movementCanDoBeforeReloadAndShoot));

    }


    /**
     * Check if the player of the turn can shoot checking if there is at least one of his weapon usable.
     * @param weaponDeck Weapon deck of the player that could want to shoot.
     * @return True if the player can shoot, false otherwise.
     */
    boolean checkIfPlayerCanShoot(ArrayList<WeaponCard> weaponDeck){
         int movementCanDoBeforeReloadAndShoot = 0;
        if(game.isInFrenzy()){
            if(game.getPlayers().get(game.getPlayerOfTurn()-1).getPosition()>=game.getFirstInFrenzyMode())
                movementCanDoBeforeReloadAndShoot=1;
            else
                movementCanDoBeforeReloadAndShoot=2;
        }
        if(!game.isInFrenzy() && !weaponDeck.isEmpty() && weaponDeck.get(0).getOwnerOfCard().isOverSixDamage()){
            movementCanDoBeforeReloadAndShoot = 1;
        }
         for(WeaponCard weaponCard : weaponDeck){
             if(checkIfThisWeaponIsUsable(weaponCard, movementCanDoBeforeReloadAndShoot))
                 return true;
         }
         return false;
    }


    /**
     * Sets the timer for the turn at the begin of the turn of a player.
     * @param isForFirstRound It's true if the timer it will be active for the first turn of the player adding to the normal time a minute, false otherwise.
     * @param isForRegeneration It's true if the timer it will be active only for the spawn after a death dividing the normal time for three, false otherwise.
     */
     void setTimerForTurn(boolean isForFirstRound, boolean isForRegeneration) {
        timer = new Timer();
        turnTask = new TurnTask();
        try {
            if(isForFirstRound)
                timer.schedule(turnTask, game.getDelay()+6000);
            else {
                if(!isForRegeneration)
                    timer.schedule(turnTask, game.getDelay());
                else
                    timer.schedule(turnTask, game.getDelay()/3);
            }
        } catch (IllegalStateException er) {
            myLogger.log(Level.ALL, "error");
        }

    }


    /**
     * Handles the disconnection of a client, marking false the boolean attribute that specify if a player is connected or not and removing the nickname of the player involved from the list of the nicknames in the game.
     * @param nicknameOfPlayerDisconnected The nickname of the player disconnected.
     */
    public void handleDisconnection(String nicknameOfPlayerDisconnected){
         Player player = findPlayerWithThisNickname(nicknameOfPlayerDisconnected);
         ServerEvent disconnectionEvent = new ServerEvent();
         disconnectionEvent.setDisconnectionEvent();
         disconnectionEvent.setMessageForOthers(nicknameOfPlayerDisconnected + " is not with us anymore. \nLet's pray for him!");
         game.getListOfNickname().remove(nicknameOfPlayerDisconnected);
         disconnectionEvent.setNicknameInvolved(nicknameOfPlayerDisconnected);
         disconnectionEvent.setNicknames(game.getListOfNickname());
         game.notify(disconnectionEvent);
         player.setConnected(false);
        if(isTheTurnOfThisPlayer(nicknameOfPlayerDisconnected)) {
             game.setNumOfActionOfTheTurn(game.getNumOfMaxActionForTurn());
             if(player.getPositionOnTheMap() == null)
                getRoundController().randomSpawn(player);
             handleTheEndOfAnAction(false);
         }
    }


    /**
     * Handles the reconnection of the client marking the boolean attribute that specifies if the player is connected or not and adding again him to the list of the nickname in the game.
     * @param nicknameOfPlayer The nickname of the player reconnected.
     */
    public void handleReconnection(String nicknameOfPlayer) {
         findPlayerWithThisNickname(nicknameOfPlayer).setConnected(true);
         ServerEvent reconnectionEvent = new ServerEvent();
         reconnectionEvent.setReconnectionEvent();
         reconnectionEvent.setNicknames(game.getListOfNickname());
         reconnectionEvent.setNicknameInvolved(nicknameOfPlayer);
         reconnectionEvent.setMessageForInvolved("You are in again, let's be ready for the battle!");
         reconnectionEvent.setMessageForOthers(nicknameOfPlayer + " is up again!");
         game.notify(reconnectionEvent);
         game.setListOfNickname();
         for(Player player : game.getPlayers()){
             if(!player.isConnected())
                 game.getListOfNickname().remove(player.getNickname());
         }
      }

    /**
     * <p>
     *     This method handles the messages that VirtualView sends to Controller.
     * </p>
     * <p>
     *     It recognize which specific controller have to be called to handle the message through the if case.
     * </p>
     *
     * @param message it contains the type of action that the player has done.
     */
    public void update(ClientEvent message) {
        if (isTheTurnOfThisPlayer(message.getNickname())&&game.getPlayerHasToRespawn()==null || findPlayerWithThisNickname(message.getNickname()).isDead() ||( message.isDiscardPowerUpCard() && ((DiscardPowerUpCard)message).isToTagback())) {
            if (message.isDrawPowerUpCard() && findPlayerWithThisNickname(message.getNickname()).isHasToBeGenerated()) {
                if(game.getRound()==1 && game.getPlayerOfTurn()==1 && game.isTerminatorModeActive()){
                    terminatorController = new TerminatorController(this, game);
                }
                powerUpController.handleDrawOfPowerUpCards((DrawPowerUpCards) message);
            }

            if (message.isDiscardPowerUpCardToSpawn()&&findPlayerWithThisNickname(message.getNickname()).isHasToBeGenerated()) {
                powerUpController.handleDiscardOfCardToSpawn((DiscardPowerUpCardToSpawnEvent) message);
            }
            if (message.isRequestToCatchByPlayer()) {
                catchController.replyToRequestToCatch(message);
            }

            if(message.isSelectionSquare()){
                SelectionSquare selectionSquareEvent = ((SelectionSquare)message);
                if(selectionSquareEvent.isCatchEvent() && game.getPlayers().get(game.getPlayerOfTurn()-1).getNickname().equals(message.getNickname()))
                    catchController.handleCatching(selectionSquareEvent);
                if(selectionSquareEvent.isGenerationTerminatorEvent())
                    terminatorController.handleSpawnOfTerminator(selectionSquareEvent);
                if(selectionSquareEvent.isMoveTerminatorEvent())
                    terminatorController.handleMove(selectionSquareEvent);
                if(selectionSquareEvent.isRunEvent())
                    runController.doRunAction(selectionSquareEvent, false);
                if(selectionSquareEvent.isBeforeToShoot())
                    shootController.handleMovementBeforeShoot(selectionSquareEvent);
                if(selectionSquareEvent.isSelectionSquareForShootAction())
                    shootController.setSquareInInvolvedPlayers(selectionSquareEvent);
                if(selectionSquareEvent.isSelectionSquareToUseTeleporter())
                    powerUpController.useTeleporter(selectionSquareEvent);
                if(selectionSquareEvent.isSelectionSquareToUseNewton())
                    powerUpController.useNewton(selectionSquareEvent);
            }


            if(message.isRequestTerminatorActionByPlayer()){
                terminatorController.handleFirstRequestAction(message);
            }

            if(message.isRequestToShootWithTerminator()){
                terminatorController.sendRequestToChoosePlayerToAttack(message);
            }

            if (message.isRequestToMoveTerminator()) {
                terminatorController.handleRequestMove(message);
            }

            if(message.isRequestToGoOn()){
                game.setHasToDoTerminatorAction(false);
                handleTheEndOfAnAction(false);
            }

            if(message.isSelectionWeaponToCatch())
                catchController.handleSelectionWeaponToCatch((SelectionWeaponToCatch) message);

            if (message.isRequestToRunByPlayer())
                runController.handleRunActionRequest(message);


            if(message.isTerminatorShootEvent())
                terminatorController.handleTerminatorShootAction(message);


            if(message.isSelectionWeaponToReload()) {
                if(((SelectionWeaponToReload)message).getNameOfWeaponCardToReload() == null)
                    reloadController.handleRequestToReloadWeapon(findPlayerWithThisNickname(message.getNickname()), null);
                else
                    reloadController.handleRequestToReloadWeapon(findPlayerWithThisNickname(message.getNickname()), catchController.getWeaponCardFromName(((SelectionWeaponToReload) message).getNameOfWeaponCardToReload()));
            }

            if(message.isSelectionWeaponToDiscard())
                catchController.handleSelectionWeaponToCatchAfterDiscard((SelectionWeaponToDiscard) message);

            if (message.isRequestToShootByPlayer())
                shootController.replyToRequestToShoot(message);

            if(message.isRequestToUseWeaponCard())
                shootController.replyWithUsableEffectsOfThisWeapon(((RequestToUseWeaponCard)message).getWeaponWantUse().getName(), findPlayerWithThisNickname(message.getNickname()));

            if(message.isRequestToUseEffect())
                shootController.handleRequestToUseEffect((RequestToUseEffect) message);

            if(message.isDiscardPowerUpCard()) {
                if(((DiscardPowerUpCard)message).isToCatch())
                    catchController.handleRequestToDiscardPowerUpCardAsAmmo((DiscardPowerUpCard) message);
                if(((DiscardPowerUpCard)message).isToPayAnEffect())
                    shootController.handleDiscardPowerUpToPayAnEffect(message);
                if(((DiscardPowerUpCard)message).isToTargeting())
                    shootController.handleDiscardPowerUpToUseTargeting((DiscardPowerUpCard) message);
                if(((DiscardPowerUpCard)message).isToTagback())
                    shootController.handleRequestToUseTagbackGranade((DiscardPowerUpCard) message);
                if(((DiscardPowerUpCard)message).isToReload())
                    reloadController.handleDiscardOfPowerUpCard((DiscardPowerUpCard) message);

            }

            if(message.isDiscardAmmoOrPowerUpToPayTargeting())
                shootController.handlePaymentForTargeting((DiscardAmmoOrPowerUpToPayTargeting) message);

            if(message.isChoosePlayer()) {
                ChoosePlayer choosePlayer = (ChoosePlayer) message;
                if (choosePlayer.isToTargeting())
                    shootController.handleUseOfTargeting(choosePlayer);
                else if (choosePlayer.isToNewton())
                    powerUpController.handleChoosePlayerForNewton(choosePlayer);
                else if (choosePlayer.isForAttack()) {
                    ArrayList<Player> arrayList = new ArrayList<>();
                    if(choosePlayer.getNameOfPlayer()!=null)
                        arrayList.add(findPlayerWithThisNickname(choosePlayer.getNameOfPlayer()));
                    shootController.setPlayersInInvolvedPlayers(arrayList);
                }
            }


            if(message.isSelectionMultiplePlayers()) {
                ArrayList<Player> players = new ArrayList<>();
                for(String name : ((SelectionMultiplePlayers)message).getNamesOfPlayers()){
                    players.add(findPlayerWithThisNickname(name));
                }
                shootController.setPlayersInInvolvedPlayers(players);
            }

            if(message.isRequestToUseNewton())
                powerUpController.handleRequestToUseNewton(message);

            if(message.isRequestToUseTeleporter())
                powerUpController.handleRequestToUseTeleporter(message);

        }
    }


    RunController getRunController() {
        return runController;
    }

    ReloadController getReloadController() {
        return reloadController;
    }

    public RoundController getRoundController() {
        return roundController;
    }

    ShootController getShootController() {
        return shootController;
    }

    CatchController getCatchController() {
        return catchController;
    }

    WeaponController getWeaponController() {
        return weaponController;
    }

    DeathController getDeathController() {
        return deathController;
    }

    PowerUpController getPowerUpController() {
        return powerUpController;
    }

    /**
     * The Task that be active at the end of the timer for the turn. It calls the update of turn of the round controller but before check if the player of the previous turn has to be generated and in the case spawn it randomly.
     */
    private class TurnTask extends TimerTask {


        @Override
        public void run() {
            Player previousPlayer = game.getPlayers().get(game.getPlayerOfTurn()-1);
            roundController.updateTurn();
            PowerUpCard powerUpCard;
            if(previousPlayer.isHasToBeGenerated()){
                ColorOfCard_Ammo color;
                Square square = null;
                if(previousPlayer.getPlayerBoard().getPowerUpCardsOwned().size()==2 && game.getRound()==1){
                    powerUpCard = previousPlayer.getPlayerBoard().getPowerUpCardsOwned().remove(1);
                    color = powerUpCard.getColor();
                    Square[][] matrix = game.getBoard().getMap().getMatrixOfSquares();
                    for (Square[] matrix1 : matrix) {
                        for (Square square1 : matrix1) {
                            if (square1 != null && square1.isSpawn() && color.toString().equals(square1.getColor().toString()))
                                square = square1;
                        }
                    }

                }
                else {
                    powerUpCard = game.getBoard().getPowerUpDeck().getPowerUpCards().get(0);
                    game.getBoard().getPowerUpDeck().getUsedPowerUpCards().add(game.getBoard().getPowerUpDeck().getPowerUpCards().remove(0));
                    game.getBoard().getPowerUpDeck().getUsedPowerUpCards().get(game.getBoard().getPowerUpDeck().getUsedPowerUpCards().size() - 1).setInTheDeckOfSomePlayer(true);
                    powerUpCard.setOwnerOfCard(previousPlayer);
                    previousPlayer.drawPowerUpCard(powerUpCard);
                    square = game.getBoard().getMap().getMatrixOfSquares()[1][0];
                }
                powerUpController.spawnPlayer(previousPlayer, square);
            }
            ServerEvent timerOverEvent = new ServerEvent();
            timerOverEvent.setNicknameInvolved(previousPlayer.getNickname());
            timerOverEvent.setMessageForInvolved("The timer for your turn is over. \nWait for the next turn!");
            timerOverEvent.setMessageForOthers("The timer for the turn of " + previousPlayer.getNickname() + " is over. \nChange turn!" );
            timerOverEvent.setTimerOverEvent();
            game.notify(timerOverEvent);
            if(game.getRound()!=1){
                sendRequestForAction();
            }
            else
                sendRequestToDrawPowerUpCard(game.getPlayers().get(game.getPlayerOfTurn()-1),2);
        }
    }



}



