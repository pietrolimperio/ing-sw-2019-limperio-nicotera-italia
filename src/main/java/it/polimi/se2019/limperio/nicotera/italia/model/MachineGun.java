package it.polimi.se2019.limperio.nicotera.italia.model;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_client.InvolvedPlayer;

import java.util.ArrayList;

import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.*;

/**
 * This class is used to represent the MachineGun of WeaponCard
 *
 * @author giuseppeitalia
 */

public class MachineGun extends WeaponCard {


    @Override
    public void useWeapon(int typeOfAttack, ArrayList<InvolvedPlayer> involvedPlayers) {

            switch (typeOfAttack){
                case 1:
                  for(InvolvedPlayer involvedPlayer  : involvedPlayers){
                            basicEffect(involvedPlayer.getPlayer());
                    }
                    break;
                case 2:
                       withFocusShot(involvedPlayers.get(0).getPlayer());
                    break;
                case 3:
                    withTurretTripod(involvedPlayers.get(0).getPlayer());
                    break;

                    default:
                        throw new IllegalArgumentException();
            }

    }

    public MachineGun() {
        super(BLUE, "Machine gun");
        String description;
        description = "BASIC EFFECT:\n Choose 1 or 2 targets you can see and deal 1 damage to each.\n" +
                "WITH FOCUS SHOT:\n Deal 1 additional damage to one of those targets.\n" +
                "WITH TURRET TRIPOD:\n Deal 1 additional damage to the other of those targets and/or deal 1 damage to a different target you can see.\n" +
                "Notes:\n If you deal both additional points of damage, they must be dealt to 2 different targets. If you see only 2 targets, you deal 2 to each if you use both optional effects. If you use the basic effect on only 1 target, you can still use the the turret tripod to give it 1 additional damage.";
        setDescription(description);
        Boolean[] kindOfAttack = {true, true, true, false};
        getNamesOfAttack().add("BASIC EFFECT");
        getNamesOfAttack().add("WITH FOCUS SHOT");
        getNamesOfAttack().add("WITH TURRET TRIPOD");
        getDescriptionsOfAttack().add("Choose 1 or 2 targets you can see and deal 1 damage to each");
        getDescriptionsOfAttack().add("Deal 1 additional damage to one of those targets");
        getDescriptionsOfAttack().add("Deal 1 additional damage to the other of those targets and/or deal 1 damage to a different target you can see");
        setPriceToPayForEffect1(new ColorOfCard_Ammo[]{YELLOW});
        setPriceToPayForEffect2(new ColorOfCard_Ammo[]{BLUE});
        setHasThisKindOfAttack(kindOfAttack);
        ColorOfCard_Ammo[] buyPrice = new ColorOfCard_Ammo[]{RED};
        setPriceToBuy(buyPrice);
        ColorOfCard_Ammo[] reloadPrice = {BLUE, RED};
        setPriceToReload(reloadPrice);
    }

    private void basicEffect(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
    }

    private void withFocusShot(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
    }

    private void withTurretTripod(Player player){
        player.assignDamage(this.getOwnerOfCard().getColorOfFigure(), 1);
    }


}
