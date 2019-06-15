package it.polimi.se2019.limperio.nicotera.italia.controller;

import it.polimi.se2019.limperio.nicotera.italia.model.Game;
import it.polimi.se2019.limperio.nicotera.italia.model.Square;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWeaponController {

    Game game = Game.instanceOfGame();

    Controller controller = new Controller(game);

    WeaponController weaponController= new WeaponController(game, controller);


    @Before
    public void setUp(){
        game.setController(this.controller);
        game.createPlayer("player1", true, 1, "BLUE");
        game.createPlayer("player2", false, 2, "YELLOW");
        game.createPlayer("player3", false, 3, "GREY");
        game.createPlayer("player4", false, 4, "PURPLE");
        game.setGameOver(true);
        game.initializeGame(false, 1, false);
    }

    @Test
    public void addSquaresForCardinalDirectionsTest()
    {

        ArrayList<Square> squares= new ArrayList<>();
        weaponController.addSquaresForCardinalDirections(game.getBoard().getMap().getMatrixOfSquares()[1][1], squares, 1);
        assertEquals(squares.size(), 4);
    }
}
