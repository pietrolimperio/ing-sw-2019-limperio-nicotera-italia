package it.polimi.se2019.limperio.nicotera.italia.controller;

import it.polimi.se2019.limperio.nicotera.italia.model.ColorOfDeathToken;
import it.polimi.se2019.limperio.nicotera.italia.model.ColorOfFigure_Square;
import it.polimi.se2019.limperio.nicotera.italia.model.Game;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeathController {
    private Game game = Game.instanceOfGame();

    private Controller controller = new Controller(game);


    @Before
    public void setUp(){
        game.createPlayer("player1", true, 1, "BLUE");
        game.createPlayer("player2", false, 2, "YELLOW");
        game.createPlayer("player3", false, 3, "GREY");
        game.createPlayer("player4", false, 4, "PURPLE");
        game.setGameOver(false);
        game.initializeGame(true, 1, false);
    }
    @After
    public void cleanUp() {
        game.setInstanceOfGameNullForTesting();
        game.getBoard().getKillShotTrack().setInstanceOfKillShotTrackNullForTesting();
    }

    @Test
    public void handleDeathTest() {

        game.setInFrenzy(false);
        game.getPlayers().get(0).assignDamage(ColorOfFigure_Square.YELLOW, 12);
        Assert.assertEquals(12, game.getPlayers().get(0).getPlayerBoard().getDamages().size());
        Assert.assertTrue(game.getPlayers().get(0).isDirectlyOverkilled());
        controller.getDeathController().handleDeath(game.getPlayers().get(1), game.getPlayers().get(0));
        Assert.assertEquals("YELLOW", game.getBoard().getKillShotTrack().getTokensOfDeath().get(0).get(0).toString());
        game.getPlayers().get(0).getPlayerBoard().getDamages().clear();
        game.getPlayers().get(0).assignDamage(ColorOfFigure_Square.YELLOW, 11);
        controller.getDeathController().handleDeath(game.getPlayers().get(1), game.getPlayers().get(0));
        Assert.assertEquals("YELLOW", game.getBoard().getKillShotTrack().getTokensOfDeath().get(1).get(0).toString());
        //Test in frenzy mode
        game.setInFrenzy(true);
        game.getPlayers().get(0).getPlayerBoard().getDamages().clear();
        game.getPlayers().get(0).assignDamage(game.getPlayers().get(1).getColorOfFigure(), 12);
        Assert.assertEquals(12, game.getPlayers().get(0).getPlayerBoard().getDamages().size());
        controller.getDeathController().handleDeath(game.getPlayers().get(1), game.getPlayers().get(0));
        assertEquals(game.getBoard().getKillShotTrack().getTokenOfFrenzyMode().get(0), ColorOfDeathToken.YELLOW);
        Assert.assertEquals("YELLOW", game.getBoard().getKillShotTrack().getTokenOfFrenzyMode().get(1).toString());
        //check if the death controller call the controller when the last skull is removed
        cleanUp();
        game = Game.instanceOfGame();
        game.createPlayer("player1", true, 1, "BLUE");
        game.createPlayer("player2", false, 2, "YELLOW");
        game.createPlayer("player3", false, 3, "GREY");
        game.createPlayer("player4", false, 4, "PURPLE");
        game.setGameOver(false);
        game.initializeGame(false, 1, false);
        game.getBoard().getKillShotTrack().getTokensOfDeath().get(game.getNumOfSkullToRemoveToPassToFrenzy()-1).set(0, ColorOfDeathToken.YELLOW);
        controller.getDeathController().handleDeath(game.getPlayers().get(1), game.getPlayers().get(0));
    }


}



