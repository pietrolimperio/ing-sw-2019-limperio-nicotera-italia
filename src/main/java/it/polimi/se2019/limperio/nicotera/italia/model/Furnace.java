package it.polimi.se2019.limperio.nicotera.italia.model;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_client.InvolvedPlayer;

import java.util.ArrayList;

import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.BLUE;
import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.RED;

/**
 * This class is used to represent the Furnace of WeaponCard
 *
 * @author giuseppeitalia
 */

public class Furnace extends WeaponCard {


    @Override
    public void useWeapon(ArrayList<Integer> typeOfAttack, ArrayList<InvolvedPlayer> involvedPlayers) {
        Square squareForRoom;
        squareForRoom = involvedPlayers.get(0).getSquare();
        if(typeOfAttack.get(0)==1) {

            for (InvolvedPlayer involvedPlayer : involvedPlayers) {
                            basicMode(involvedPlayer.getPlayer());

            }


        }
        else {
            for(Player player : squareForRoom.getPlayerOnThisSquare()){
                inCozyFireMode(player);
            }

        }

        setLoad(false);
    }


    private void basicMode(Player player){
        player.assignDamage(getOwnerOfCard().getColorOfFigure(), 1);
    }

    private void inCozyFireMode(Player player){
        player.assignDamage(getOwnerOfCard().getColorOfFigure(), 1);
        player.assignMarks(getOwnerOfCard().getColorOfFigure(), 1);
    }

    public Furnace() {
        super(RED, "Furnace");
        String description = "basic mode: Choose a room you can see, but not the room you are in. Deal 1 damage to everyone in that room.\n" +
                "in cozy fire mode: Choose a square exactly one move away. Deal 1 damage and 1 mark to everyone on that square.";
        setDescription(description);
        Boolean[] kindOfAttack = {true, false, false, true};
        setHasThisKindOfAttack(kindOfAttack);
        ColorOfCard_Ammo[] buyPrice = new ColorOfCard_Ammo[]{BLUE};
        setPriceToBuy(buyPrice);
        ColorOfCard_Ammo[] reloadPrice = {BLUE, RED};
        setPriceToReload(reloadPrice);
    }
}
