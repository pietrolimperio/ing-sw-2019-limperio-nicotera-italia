package it.polimi.se2019.limperio.nicotera.italia.model;

import java.util.ArrayList;

import static it.polimi.se2019.limperio.nicotera.italia.model.ColorOfCard_Ammo.BLUE;

public class Furnace extends WheaponCard {


    @Override
    public void useWheapon(ArrayList<Integer> typeOfAttack) {
        return;

    }

    public Furnace() {
        super(BLUE, "Furnace", "blablabla");
        Boolean[] kindOfAttack = {true, true, false, false};
        setHasThisKindOfAttack(kindOfAttack);
        setLoad(true);
        ColorOfCard_Ammo[] buyPrice = new ColorOfCard_Ammo[]{BLUE};
        setPriceToBuy(buyPrice);
        ColorOfCard_Ammo[] reloadPrice = {BLUE, BLUE};
        setPriceToReload(reloadPrice);
    }
}