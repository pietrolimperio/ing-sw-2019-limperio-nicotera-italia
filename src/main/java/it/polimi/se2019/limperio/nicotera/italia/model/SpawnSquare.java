package it.polimi.se2019.limperio.nicotera.italia.model;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.ServerEvent;


import java.util.ArrayList;

/**
 * This class is used to represent the spawn squares on the map
 * @author Pietro L'Imperio
 */
public class SpawnSquare extends Square implements  Cloneable  {
    static final long serialVersionUID = 420000012;
    /**
     * It needs to send the weapon card to client through socket connection
     */
    private ArrayList<ServerEvent.AliasCard> weaponsCardsForRemoteView = new ArrayList<>();
    /**
     * Collections of weapons that the players can catch on the square
     */
    private transient ArrayList<WeaponCard> weaponCards;


    /**
     * Constructor that initialize color, row, column of the square. The boolean that show if the square has door and at the end marks true the boolean relative to the fact that is a spawn square.
     */
     SpawnSquare( ColorOfFigure_Square color, boolean hasDoor, int row, int column) {
        super(color, hasDoor, row,column);
        weaponCards=new ArrayList<>();
        setSpawn(true);
    }

    public ArrayList<ServerEvent.AliasCard> getWeaponsCardsForRemoteView() {
        return weaponsCardsForRemoteView;
    }

    public ArrayList<WeaponCard> getWeaponCards() {
        return weaponCards;
    }

    public void setWeaponsCardsForRemoteView(ArrayList<ServerEvent.AliasCard> weaponsCardsForRemoteView) {
        this.weaponsCardsForRemoteView = weaponsCardsForRemoteView;
    }

    /**
     * Override of the clone method to make possible the serialization avoiding the shallow copy.
     * @return The cloned object.
     */
    public Object clone(){
         SpawnSquare spawnSquare;
         try{
             spawnSquare = (SpawnSquare) super.clone();
         } catch (CloneNotSupportedException e) {
             spawnSquare = new SpawnSquare(this.getColor(), this.isHasDoor(), this.getRow(), this.getColumn());
         }
         spawnSquare.nicknamesOfPlayersOnThisSquare = (ArrayList<String>) this.getNicknamesOfPlayersOnThisSquare().clone();
         return spawnSquare;
    }

}
