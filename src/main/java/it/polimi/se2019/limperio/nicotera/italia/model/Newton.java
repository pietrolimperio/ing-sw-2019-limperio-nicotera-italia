package it.polimi.se2019.limperio.nicotera.italia.model;

import java.io.Serializable;

class Newton extends PowerUpCard {
    int typeOfCard;
     Newton(ColorOfCard_Ammo color, int typeOfCard) {
        super(color, "Newton", "bla bla bla");
        this.typeOfCard=typeOfCard;
    }

    public int getTypeOfCard() {
        return typeOfCard;
    }
}
