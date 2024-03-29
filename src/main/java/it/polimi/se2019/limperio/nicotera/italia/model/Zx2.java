package it.polimi.se2019.limperio.nicotera.italia.model;

import it.polimi.se2019.limperio.nicotera.italia.controller.InvolvedPlayer;

import java.util.ArrayList;

import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.*;

/**
 * Represents the weapon card Zx-2.
 * @author Giuseppe Italia
 */

public class Zx2 extends WeaponCard {

    @Override
    public void useWeapon(int typeOfAttack, ArrayList<InvolvedPlayer> involvedPlayers) {

            switch (typeOfAttack) {
                case 1:
                    basicEffect(involvedPlayers.get(0).getPlayer());
                    break;
                case 4:
                    for (InvolvedPlayer involvedPlayer : involvedPlayers) {
                        scannerMode(involvedPlayer.getPlayer());
                    }
                    break;
                    default:
                        throw new IllegalArgumentException();
            }

    }

    /**
     * Constructor that calls the super constructor to initializes color and name. Then initializes the description, the list of the names of the effects with the relative single descriptions.
     * Sets the price to buy the weapon and to reload it. At the end initializes the array of boolean that shows what kind of effect the weapon has.
     */

    public Zx2() {
        super(YELLOW, "Zx-2");
        String description;
        description = "BASIC MODE: \nDeal 1 damage and 2 marks to 1 target you can see.\n" +
                "IN SCANNER MODE: \nChoose up to 3 targets you can see and deal 1 mark to each.\n" +
                "Notes: \nRemember that the 3 targets can be in 3 different rooms.";
        setDescription(description);
        Boolean[] kindOfAttack = {true, false, false, true};
        getNamesOfAttack().add("BASIC MODE");
        getNamesOfAttack().add("");
        getNamesOfAttack().add("");
        getNamesOfAttack().add("IN SCANNER MODE");
        getDescriptionsOfAttack().add("Deal 1 damage and 2 marks to 1 target you can see");
        getDescriptionsOfAttack().add("");
        getDescriptionsOfAttack().add("");
        getDescriptionsOfAttack().add("Choose up to 3 targets you can see and deal 1 mark to each");
        setHasThisKindOfAttack(kindOfAttack);
        setLoad(true);
        ColorOfCard_Ammo[] buyPrice = new ColorOfCard_Ammo[]{RED};
        setPriceToBuy(buyPrice);
        ColorOfCard_Ammo[] reloadPrice = {YELLOW, RED};
        setPriceToReload(reloadPrice);
    }

    /**
     * Assigns a damage and two marks to the player passed by parameter.
     */
    private void basicEffect (Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
        player.assignMarks(this.getOwnerOfCard().getColorOfFigure(), 2);
    }

    /**
     * Assigns a mark to the player passed by parameter.
     */
    private void scannerMode(Player player){
        player.assignMarks(this.getOwnerOfCard().getColorOfFigure(), 1);
    }

}
