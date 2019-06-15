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
     * The nickname of the Player who is sending the event.
     */
   private String nickname;

    /**
     * Reference to the Virtual View of the player that is sending the event
     *
     */
   private transient VirtualView myVirtualView = null;
   private boolean isDrawPowerUpCard = false;
   private boolean isDiscardPowerUpCardToSpawn = false;
   private boolean isRequestToRunByPlayer = false;
   private boolean isRequestToCatchByPlayer = false;
   private boolean isRequestToShootByPlayer = false;
   private boolean isCatchEvent = false;
   private boolean isSelectionSquareForRun = false;
   private boolean isSelectionWeaponToCatch = false;
   private boolean isSelectionWeaponToDiscard = false;
   private boolean isRequestTerminatorActionByPlayer = false;
   private boolean isGenerationTerminatorEvent = false;
   private boolean isRequestToMoveTerminator = false;
   private boolean isRequestToShootWithTerminator = false;
   private boolean isRequestToGoOn = false;
   private boolean isMoveTerminatorEvent = false;
   private boolean isTerminatorShootEvent = false;
   private boolean isRequestToUseWeaponCard = false;
   private boolean isRequestToUseEffect = false;
   private boolean isDiscardPowerUpCardAsAmmo = false;
   private boolean isDiscardAmmoOrPowerUpToPayTargeting = false;
   private boolean isChoosePlayer = false;
   private boolean isRequestToUseTeleporter = false;
   private boolean isSelectionSquareToUseTeleporter = false;
   private boolean isRequestToUseNewton = false;
   private boolean isSelectionSquareToUseNewton = false;


    public ClientEvent(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
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

    public void setRequestToShootByPlayer(boolean requestToShootByPlayer) {
        isRequestToShootByPlayer = requestToShootByPlayer;
    }

    public boolean isChoosePlayer() {
        return isChoosePlayer;
    }

    public void setChoosePlayer(boolean choosePlayer) {
        isChoosePlayer = choosePlayer;
    }

    public boolean isRequestToUseEffect() {
        return isRequestToUseEffect;
    }

    public boolean isDiscardPowerUpCardAsAmmo() {
        return isDiscardPowerUpCardAsAmmo;
    }

    public void setDiscardPowerUpCardAsAmmo(boolean discardPowerUpCardAsAmmo) {
        isDiscardPowerUpCardAsAmmo = discardPowerUpCardAsAmmo;
    }

    public void setRequestToUseEffect(boolean requestToUseEffect) {
        isRequestToUseEffect = requestToUseEffect;
    }

    public boolean isMoveTerminatorEvent() {
        return isMoveTerminatorEvent;
    }

    public void setMoveTerminatorEvent(boolean moveTerminatorEvent) {
        isMoveTerminatorEvent = moveTerminatorEvent;
    }

    public void setRequestToCatchByPlayer(boolean requestToCatchByPlayer) {
        isRequestToCatchByPlayer = requestToCatchByPlayer;
    }

    public boolean isDiscardAmmoOrPowerUpToPayTargeting() {
        return isDiscardAmmoOrPowerUpToPayTargeting;
    }

    public void setDiscardAmmoOrPowerUpToPayTargeting(boolean discardAmmoOrPowerUpToPayTargeting) {
        isDiscardAmmoOrPowerUpToPayTargeting = discardAmmoOrPowerUpToPayTargeting;
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

    public void setRequestToUseWeaponCard(boolean requestToUseWeaponCard) {
        isRequestToUseWeaponCard = requestToUseWeaponCard;
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

     void setDiscardPowerUpCardToSpawn(boolean discardPowerUpCardToSpawn) {
        isDiscardPowerUpCardToSpawn = discardPowerUpCardToSpawn;
    }

    public boolean isSelectionWeaponToDiscard() {
        return isSelectionWeaponToDiscard;
    }

    public void setSelectionWeaponToDiscard(boolean selectionWeaponToDiscard) {
        isSelectionWeaponToDiscard = selectionWeaponToDiscard;
    }

    public boolean isCatchEvent() {
        return isCatchEvent;
    }

    public void setCatchEvent(boolean catchEvent) {
        isCatchEvent = catchEvent;
    }

    public boolean isSelectionSquareForRun() {
        return isSelectionSquareForRun;
    }

    public void setSelectionSquareForRun(boolean selectionSquareForRun) {
        isSelectionSquareForRun = selectionSquareForRun;
    }

    public boolean isRequestTerminatorActionByPlayer() {
        return isRequestTerminatorActionByPlayer;
    }

    public void setRequestTerminatorActionByPlayer(boolean requestTerminatorActionByPlayer) {
        isRequestTerminatorActionByPlayer = requestTerminatorActionByPlayer;
    }

    public boolean isSelectionWeaponToCatch() {
        return isSelectionWeaponToCatch;
    }

    public void setSelectionWeaponToCatch(boolean selectionWeaponToCatch) {
        isSelectionWeaponToCatch = selectionWeaponToCatch;
    }

    public boolean isGenerationTerminatorEvent() {
        return isGenerationTerminatorEvent;
    }

    public void setGenerationTerminatorEvent(boolean generationTerminatorEvent) {
        isGenerationTerminatorEvent = generationTerminatorEvent;
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

    public void setTerminatorShootEvent(boolean terminatorShootEvent) {
        isTerminatorShootEvent = terminatorShootEvent;
    }

    public boolean isSelectionSquareToUseTeleporter() {
        return isSelectionSquareToUseTeleporter;
    }

    public void setSelectionSquareToUseTeleporter(boolean selectionSquareToUseTeleporter) {
        isSelectionSquareToUseTeleporter = selectionSquareToUseTeleporter;
    }

    public boolean isRequestToUseTeleporter() {
        return isRequestToUseTeleporter;
    }

    public void setRequestToUseTeleporter(boolean requestToUseTeleporter) {
        isRequestToUseTeleporter = requestToUseTeleporter;
    }

    public boolean isRequestToUseNewton() {
        return isRequestToUseNewton;
    }

    public void setRequestToUseNewton(boolean requestToUseNewton) {
        isRequestToUseNewton = requestToUseNewton;
    }

    public boolean isSelectionSquareToUseNewton() {
        return isSelectionSquareToUseNewton;
    }

    public void setSelectionSquareToUseNewton(boolean selectionSquareToUseNewton) {
        isSelectionSquareToUseNewton = selectionSquareToUseNewton;
    }
}
