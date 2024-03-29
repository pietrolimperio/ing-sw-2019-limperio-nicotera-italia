package it.polimi.se2019.limperio.nicotera.italia.events.events_by_client;

import it.polimi.se2019.limperio.nicotera.italia.network.server.VirtualView;

import java.io.Serializable;

/**
 * Event generated by some components located in client side.
 * <p>
 *     Represents a kind of general event and is usually adapted to the different
 *     situations according to the values of the different boolean attributes.
 * </p>
 * <p>
 *     The way to understand, by the virtual view of a player, which wind of Event was sent by the client is evaluated
 *     the state of the relative boolean attribute.
 *     Example: if an event is the type of RequestToRunByPlayer, all the boolean attributes will be false apart
 *     isRequestToRunByPlayer.
 *
 * </p>
 * @author Pietro L'Imperio
 */
public class ClientEvent implements Serializable {
    static final long serialVersionUID = 420000014;
    /**
     * The message stored in event.
     */
   private String message;
    /**
     * The nickname of the player who is sending the event.
     */
   private String nickname;

    /**
     * Reference of the Virtual View of the player that is sending the event
     *
     */
   private transient VirtualView myVirtualView = null;
    /**
     * It's true if the event is generated to draw one or two power up cards, false otherwise.
     */
   private boolean isDrawPowerUpCard = false;
    /**
     * It's true if the event is generated to discard a  power up card to be spawn, false otherwise.
     */
   private boolean isDiscardPowerUpCardToSpawn = false;
    /**
     * It's true if the event is generated to request to the game to run, false otherwise.
     */
   private boolean isRequestToRunByPlayer = false;
    /**
     * It's true if the event is generated to request to the game to catch, false otherwise.
     */
   private boolean isRequestToCatchByPlayer = false;
    /**
     * It's true if the event is generated to request to the game to shoot, false otherwise.
     */
   private boolean isRequestToShootByPlayer = false;
    /**
     * It's true if the event is generated to communicate a choice of a square, false otherwise.
     */
   private boolean isSelectionWeaponToCatch = false;
    /**
     * It's true if the event is generated to select a weapon to discard it, false otherwise.
     */
   private boolean isSelectionWeaponToDiscard = false;
    /**
     * It's true if the event is generated to request to the game to do a terminator action, false otherwise.
     */
   private boolean isRequestTerminatorActionByPlayer = false;
    /**
     * It's true if the event is generated to request to the game to move terminator, false otherwise.
     */
   private boolean isRequestToMoveTerminator = false;
    /**
     * It's true if the event is generated to request to the game to shoot with terminator, false otherwise.
     */
   private boolean isRequestToShootWithTerminator = false;
    /**
     * It's true if the event is generated to request to the game to go on with the terminator action, false otherwise.
     */
   private boolean isRequestToGoOn = false;
    /**
     * It's true if the event is generated to shoot with terminator, false otherwise.
     */
   private boolean isTerminatorShootEvent = false;
    /**
     * It's true if the event is generated to request to the game to use a weapon, false otherwise.
     */
   private boolean isRequestToUseWeaponCard = false;
    /**
     * It's true if the event is generated to request to the game to use an effect, false otherwise.
     */
   private boolean isRequestToUseEffect = false;
    /**
     * It's true if the event is generated to discard a power up card, false otherwise.
     */
   private boolean isDiscardPowerUpCard = false;
    /**
     * It's true if the event is generated to discard power up card or use ammo to pay the effect of targeting scope, false otherwise.
     */
   private boolean isDiscardAmmoOrPowerUpToPayTargeting = false;
    /**
     * It's true if the event is generated to choose a player, false otherwise.
     */
   private boolean isChoosePlayer = false;
    /**
     * It's true if the event is generated to select a weapon to reload, false otherwise.
     */
   private boolean isSelectionWeaponToReload = false;
    /**
     * It's true if the event is generated to select more than players for an attack, false otherwise.
     */
   private boolean isSelectionMultiplePlayers = false;
    /**
     * It's true if the event is generated to select a square, false otherwise.
     */
   private boolean isSelectionSquare = false;
    /**
     * It's true if the event is generated to request to the game to use teleporter, false otherwise.
     */
   private boolean isRequestToUseTeleporter = false;
    /**
     * It's true if the event is generated to request to the game to use Newton, false otherwise.
     */
   private boolean isRequestToUseNewton = false;


    /**
     * Constructor of the class that initialize the message for the server e the nickname of the player that is sending the event.
     */
    public ClientEvent(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }

    public ClientEvent() {
    }



    public VirtualView getMyVirtualView() {
        return myVirtualView;
    }

    public void setMyVirtualView(VirtualView myVirtualView) {
        this.myVirtualView = myVirtualView;
    }

    public boolean isRequestToRunByPlayer() {
        return isRequestToRunByPlayer;
    }

    public boolean isRequestToCatchByPlayer() {
        return isRequestToCatchByPlayer;
    }

    public boolean isRequestToShootByPlayer() {
        return isRequestToShootByPlayer;
    }

    public boolean isSelectionSquare() {
        return isSelectionSquare;
    }

     void setSelectionSquare() {
        isSelectionSquare = true;
    }

    public boolean isSelectionWeaponToReload() {
        return isSelectionWeaponToReload;
    }


    public boolean isSelectionMultiplePlayers() {
        return isSelectionMultiplePlayers;
    }

     void setSelectionMultiplePlayers() {
        isSelectionMultiplePlayers = true;
    }

     void setSelectionWeaponToReload() {
        isSelectionWeaponToReload = true;
    }

    public void setRequestToShootByPlayer(boolean requestToShootByPlayer) {
        isRequestToShootByPlayer = requestToShootByPlayer;
    }

    public boolean isChoosePlayer() {
        return isChoosePlayer;
    }

     void setChoosePlayer() {
        isChoosePlayer = true;
    }

    public boolean isRequestToUseEffect() {
        return isRequestToUseEffect;
    }

    public boolean isDiscardPowerUpCard() {
        return isDiscardPowerUpCard;
    }

     void setDiscardPowerUpCard() {
        isDiscardPowerUpCard = true;
    }

     void setRequestToUseEffect() {
        isRequestToUseEffect = true;
    }


    public void setRequestToCatchByPlayer(boolean requestToCatchByPlayer) {
        isRequestToCatchByPlayer = requestToCatchByPlayer;
    }

    public boolean isDiscardAmmoOrPowerUpToPayTargeting() {
        return isDiscardAmmoOrPowerUpToPayTargeting;
    }

     void setDiscardAmmoOrPowerUpToPayTargeting() {
        isDiscardAmmoOrPowerUpToPayTargeting = true;
    }

    public void setRequestToRunByPlayer(boolean requestToRunByPlayer) {
        isRequestToRunByPlayer = requestToRunByPlayer;
    }

    public String getMessage() {
        return message;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isRequestToUseWeaponCard() {
        return isRequestToUseWeaponCard;
    }

     void setRequestToUseWeaponCard() {
        isRequestToUseWeaponCard = true;
    }

    public boolean isDrawPowerUpCard() {
        return isDrawPowerUpCard;
    }

     void setDrawPowerUpCard(boolean drawPowerUpCard) {
        isDrawPowerUpCard = drawPowerUpCard;
    }

    public boolean isDiscardPowerUpCardToSpawn() {
        return isDiscardPowerUpCardToSpawn;
    }

     void setDiscardPowerUpCardToSpawn() {
        isDiscardPowerUpCardToSpawn = true;
    }

    public boolean isSelectionWeaponToDiscard() {
        return isSelectionWeaponToDiscard;
    }

     void setSelectionWeaponToDiscard() {
        isSelectionWeaponToDiscard = true;
    }


    public boolean isRequestTerminatorActionByPlayer() {
        return isRequestTerminatorActionByPlayer;
    }

     public void setRequestTerminatorActionByPlayer() {
        isRequestTerminatorActionByPlayer = true;
    }

    public boolean isSelectionWeaponToCatch() {
        return isSelectionWeaponToCatch;
    }

     void setSelectionWeaponToCatch() {
        isSelectionWeaponToCatch = true;
    }


    public boolean isRequestToMoveTerminator() {
        return isRequestToMoveTerminator;
    }

    public void setRequestToMoveTerminator(boolean requestToMoveTerminator) {
        isRequestToMoveTerminator = requestToMoveTerminator;
    }

    public boolean isRequestToShootWithTerminator() {
        return isRequestToShootWithTerminator;
    }

    public void setRequestToShootWithTerminator(boolean requestToShootWithTerminator) {
        isRequestToShootWithTerminator = requestToShootWithTerminator;
    }

    public boolean isRequestToGoOn() {
        return isRequestToGoOn;
    }

    public void setRequestToGoOn(boolean requestToGoOn) {
        isRequestToGoOn = requestToGoOn;
    }

    public boolean isTerminatorShootEvent() {
        return isTerminatorShootEvent;
    }

     void setTerminatorShootEvent() {
        isTerminatorShootEvent = true;
    }


    public boolean isRequestToUseTeleporter() {
        return isRequestToUseTeleporter;
    }

     void setRequestToUseTeleporter() {
        isRequestToUseTeleporter = true;
    }

    public boolean isRequestToUseNewton() {
        return isRequestToUseNewton;
    }

     void setRequestToUseNewton() {
        isRequestToUseNewton = true;
    }

}
