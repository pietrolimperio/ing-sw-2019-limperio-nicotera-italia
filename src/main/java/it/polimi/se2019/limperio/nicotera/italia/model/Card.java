package it.polimi.se2019.limperio.nicotera.italia.model;

import java.io.Serializable;

public abstract class Card  {
    private ColorOfCard_Ammo color;
    private String name;
    private String description;
    private Player ownerOfCard;

    public Card() {
    }

    public Card(ColorOfCard_Ammo color, String name) {
        this.color = color;
        this.name = name;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ColorOfCard_Ammo getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Player getOwnerOfCard() {
        return ownerOfCard;
    }

    public void setOwnerOfCard(Player ownerOfCard) {
        this.ownerOfCard = ownerOfCard;
    }


}
