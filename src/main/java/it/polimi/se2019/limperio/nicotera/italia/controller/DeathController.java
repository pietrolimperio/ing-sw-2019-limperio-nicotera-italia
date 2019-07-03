package it.polimi.se2019.limperio.nicotera.italia.controller;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.KillshotTrackEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.PlayerBoardEvent;
import it.polimi.se2019.limperio.nicotera.italia.model.*;
import java.util.ArrayList;

class DeathController {

    private Game game;
    private Controller controller;

     DeathController(Game game, Controller controller) {
        this.game = game;
        this.controller = controller;
    }

     void handleDeath(Player killerPlayer, Player deadPlayer) {
         String messageForInvolved;
         String messageForOthers;
         if(deadPlayer.getPlayerBoard().getDamages().size()==11){
             messageForInvolved = "You have been killed by " + killerPlayer.getNickname();
             messageForOthers = deadPlayer.getNickname() + " has been killed by " + killerPlayer.getNickname();
         }
         else{
             messageForInvolved = "You have been overkilled by " + killerPlayer.getNickname();
             messageForOthers = deadPlayer.getNickname() + " has overkilled by " + killerPlayer.getNickname();
         }
         PlayerBoardEvent pbEvent = new PlayerBoardEvent();
         pbEvent.setNicknameInvolved(deadPlayer.getNickname());
         pbEvent.setNicknames(game.getListOfNickname());
         pbEvent.setPlayerBoard(deadPlayer.getPlayerBoard());
         pbEvent.setMessageForInvolved(messageForInvolved);
         pbEvent.setMessageForOthers(messageForOthers);
         pbEvent.setNotifyAboutActionDone();
         game.notify(pbEvent);

         Player playerOfTheTurn = game.getPlayers().get(game.getPlayerOfTurn()-1);
         playerOfTheTurn.setNumOfKillDoneInTheTurn(playerOfTheTurn.getNumOfKillDoneInTheTurn()+1);

         ColorOfDeathToken colorOfKiller = convertColorOfFigureInColorOfToken(killerPlayer.getColorOfFigure());
         deadPlayer.setDead(true);

         if(game.isInFrenzy()) {
             game.getBoard().getKillShotTrack().getTokenOfFrenzyMode().add(colorOfKiller);
         }
         else{
             if(deadPlayer.getPlayerBoard().getDamages().size()==11){
                     game.getBoard().getKillShotTrack().getTokensOfDeath().get(getFirstSkullPosition()).set(0, colorOfKiller);
             }
             else {
                 killerPlayer.assignMarks(deadPlayer.getColorOfFigure(), 1);
                 if(deadPlayer.isDirectlyOverkilled())
                     game.getBoard().getKillShotTrack().getTokensOfDeath().get(getFirstSkullPosition()).set(0, colorOfKiller);
                 game.getBoard().getKillShotTrack().getTokensOfDeath().get(getFirstSkullPosition()-1).add(colorOfKiller);
                 deadPlayer.setDirectlyOverkilled(false);
             }
         }
         KillshotTrackEvent killshotTrackEvent = new KillshotTrackEvent("", game.getBoard().getKillShotTrack());
         killshotTrackEvent.setNicknameInvolved(game.getPlayers().get(game.getPlayerOfTurn()-1).getNickname());
         killshotTrackEvent.setNicknames(game.getListOfNickname());
         game.notify(killshotTrackEvent);

         if(!game.isAnticipatedFrenzy() && !game.getBoard().getKillShotTrack().getTokensOfDeath().get(game.getNumOfSkullToRemoveToPassToFrenzy()-1).get(0).equals(ColorOfDeathToken.SKULL)) {
             ArrayList<Player> deadPlayers = new ArrayList<>();
             for(Player player : game.getPlayers()){
                 if (player.isDead())
                     deadPlayers.add(player);
             }
             controller.getRoundController().countScoreForDeaths(deadPlayers);
             controller.getRoundController().handleEndOfGame(false);
         }
    }

    private int getFirstSkullPosition() {
         for(int i=0;i<game.getBoard().getKillShotTrack().getTokensOfDeath().size();i++){
             if(game.getBoard().getKillShotTrack().getTokensOfDeath().get(i).get(0).toString().equals("SKULL"))
                 return i;
         }
         throw new IllegalArgumentException();
    }

    private ColorOfDeathToken convertColorOfFigureInColorOfToken(ColorOfFigure_Square colorOfFigure) {
         for(ColorOfDeathToken colorOfToken : ColorOfDeathToken.values()){
             if(colorOfToken.toString().equals(colorOfFigure.toString()))
                 return colorOfToken;
         }
         throw new IllegalArgumentException();
    }


 }
