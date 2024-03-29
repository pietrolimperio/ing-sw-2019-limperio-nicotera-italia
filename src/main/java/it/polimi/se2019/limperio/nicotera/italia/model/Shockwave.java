package it.polimi.se2019.limperio.nicotera.italia.model;

import it.polimi.se2019.limperio.nicotera.italia.controller.InvolvedPlayer;

import java.util.ArrayList;

import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.YELLOW;

/**
 * Represents the weapon card Shockwave.
 * @author Giuseppe Italia
 */


public class Shockwave extends WeaponCard {

    @Override
    public void useWeapon(int typeOfAttack, ArrayList<InvolvedPlayer> involvedPlayers) {

        if(typeOfAttack==1)
        {
            for(InvolvedPlayer involvedPlayer : involvedPlayers)
                basicEffect(involvedPlayer.getPlayer());
        }
        else
            this.tsunamiMode();
    }


    /**
     * Constructor that calls the super constructor to initializes color and name. Then initializes the description, the list of the names of the effects with the relative single descriptions.
     * Sets the price to buy the weapon and to reload it. At the end initializes the array of boolean that shows what kind of effect the weapon has.
     */
    public Shockwave() {
        super(YELLOW, "Shockwave");
        String description;
        description = "BASIC MODE: \nChoose up to 3 targets on different squares, each exactly 1 move away. \nDeal 1 damage to each target.\n" +
        "IN TSUNAMI MODE: \nDeal 1 damage to all targets that are exactly 1 move away.\n";
        setDescription(description);
        Boolean[] kindOfAttack = {true, false, false, true};
        getNamesOfAttack().add("BASIC MODE");
        getNamesOfAttack().add("");
        getNamesOfAttack().add("");
        getNamesOfAttack().add("IN TSUNAMI MODE");
        getDescriptionsOfAttack().add("Choose up to 3 targets on different squares, each exactly 1 move away. \nDeal 1 damage to each target");
        getDescriptionsOfAttack().add("");
        getDescriptionsOfAttack().add("");
        getDescriptionsOfAttack().add("Deal 1 damage to all targets that are exactly 1 move away");
        setPriceToPayForAlternativeMode(new ColorOfCard_Ammo[]{YELLOW});
        setHasThisKindOfAttack(kindOfAttack);
        setLoad(true);
        ColorOfCard_Ammo[] buyPrice = new ColorOfCard_Ammo[]{};
        setPriceToBuy(buyPrice);
        ColorOfCard_Ammo[] reloadPrice = {YELLOW};
        setPriceToReload(reloadPrice);
    }

    /**
     * Assigns one damage to the player passed by parameter.
     */
    private void basicEffect(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
    }

    /**
     * Assigns one damage to every player in the squares on the adjacency of the square of the owner of the card.
     */
    private void tsunamiMode(){
        for(Square square : this.getOwnerOfCard().getPositionOnTheMap().getAdjSquares()){
            for(Player enemy : square.getPlayerOnThisSquare()){
                enemy.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
            }
        }
    }

}
