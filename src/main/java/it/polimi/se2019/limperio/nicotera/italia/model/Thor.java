package it.polimi.se2019.limperio.nicotera.italia.model;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_client.InvolvedPlayer;

import java.util.ArrayList;

import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.BLUE;
import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.RED;

/**
 * This class is used to represent the Thor of WeaponCard
 *
 * @author Giuseppe Italia
 */


public class Thor extends WeaponCard {


    @Override
    public void useWeapon(int typeOfAttack, ArrayList<InvolvedPlayer> involvedPlayers) {
            switch (typeOfAttack){
                case 1:
                    basicEffect(involvedPlayers.get(0).getPlayer());
                    break;
                case 2:
                    withChainReaction(involvedPlayers.get(0).getPlayer());
                    break;
                case 3:
                    withHighVoltage(involvedPlayers.get(0).getPlayer());
                    break;
                    default:
                        throw new IllegalArgumentException();
            }
        }


    public Thor() {
        super(BLUE, "THOR");
        String description = "BASIC EFFECT: \nDeal 2 damage to 1 target you can see.\n" +
                "WITH CHAIN REACTION: \nDeal 1 damage to a second target that your first target can see.\n" +
                "WITH HIGH VOLTAGE: \nDeal 2 damage to a third target that your second target can see. \nYou cannot use this effect unless you first use the chain reaction.\n" +
                "Notes: \nThis card constrains the order in which you can use its effects. (Most cards don't.) \nAlso note that each target must be a different player.";
        setDescription(description);
        Boolean[] kindOfAttack = {true, true, true, false};
        setHasThisKindOfAttack(kindOfAttack);
        getNamesOfAttack().add("BASIC EFFECT");
        getNamesOfAttack().add("WITH CHAIN REACTION");
        getNamesOfAttack().add("WITH HIGH VOLTAGE");
        getDescriptionsOfAttack().add("Deal 2 damage to 1 target you can see");
        getDescriptionsOfAttack().add("Deal 1 damage to a second target that your first target can see");
        getDescriptionsOfAttack().add("Deal 2 damage to a third target that your second target can see. \nYou cannot use this effect unless you first use the chain reaction");
        setPriceToPayForEffect1(new ColorOfCard_Ammo[]{BLUE});
        setPriceToPayForEffect2(new ColorOfCard_Ammo[]{BLUE});
        ColorOfCard_Ammo[] buyPrice = new ColorOfCard_Ammo[]{RED};
        setPriceToBuy(buyPrice);
        ColorOfCard_Ammo[] reloadPrice = {BLUE, RED};
        setPriceToReload(reloadPrice);
    }

    private void basicEffect(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 2);
    }

    private void withChainReaction(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
    }

    private void withHighVoltage(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 2);
    }


}
