package it.polimi.se2019.limperio.nicotera.italia.controller;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.PlayerBoardEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.ServerEvent;
import it.polimi.se2019.limperio.nicotera.italia.model.ColorOfFigure_Square;
import it.polimi.se2019.limperio.nicotera.italia.model.Game;
import it.polimi.se2019.limperio.nicotera.italia.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RoundController {
    private Controller controller;
    private final Game game;
    private ArrayList<Integer> scoreForPlayers = new ArrayList<>();

    public RoundController(Controller controller, Game game) {
        this.controller = controller;
        this.game = game;
    }

     void updateTurn() {

         ArrayList<Player> deadPlayers = getPlayersDeadInThisTurn();
        if(!deadPlayers.isEmpty()) {
            initializeScoreForPlayers();
            assignScoreForDoubleKill();
            for (Player player : deadPlayers) {
                countScoreForPlayerDeath(player);
                player.getPlayerBoard().getScoreBarForNormalMode().remove(0);
                player.getPlayerBoard().setDamages(new ArrayList<>());
                if(game.isInFrenzy())
                    player.getPlayerBoard().setFrenzyBoardPlayer(true);
            }
            if(!game.getBoard().getKillShotTrack().getTokensOfDeath().get(7).get(0).toString().equals("SKULL")&&!game.isInFrenzy()){
                game.setInFrenzy(true);
                if(game.getPlayerOfTurn()==game.getPlayers().size())
                    game.setFirstInFrenzyMode(1);
                else
                    game.setFirstInFrenzyMode(game.getPlayerOfTurn()+1);
                for(Player player : game.getPlayers()){
                    if(player.getPlayerBoard().getDamages().isEmpty()){
                        player.getPlayerBoard().setFrenzyBoardPlayer(true);
                        PlayerBoardEvent pbEvent = new PlayerBoardEvent();
                        pbEvent.setNicknameInvolved(player.getNickname());
                        pbEvent.setNicknames(game.getListOfNickname());
                        pbEvent.setPlayerBoard(player.getPlayerBoard());
                        pbEvent.setFirstFrenzyPlayerBoard(true);
                        pbEvent.setMessageForOthers("The player board of "+ player.getNickname() + " has passed to frenzy mode");
                        pbEvent.setMessageForInvolved("Your player board has passed to frenzy mode");
                        game.notify(pbEvent);
                    }
                }
            }
            String message = "Updating score: \n";
            for(int i=0;i<scoreForPlayers.size();i++){
                game.getPlayers().get(i).updateScore(scoreForPlayers.get(i));
                message = message.concat(game.getPlayers().get(i).getNickname() + ": " + scoreForPlayers.get(i)+ " points added\n");
            }
            ServerEvent updateScoreEvent = new ServerEvent();
            updateScoreEvent.setUpdateScoreEvent(true);
            updateScoreEvent.setNicknames(game.getListOfNickname());
            updateScoreEvent.setMessageForOthers(message);
            game.notify(updateScoreEvent);
        }

        if(game.isInFrenzy()&&game.getPlayerOfTurn()==game.getPlayers().size()&&game.getFirstInFrenzyMode()==1 || game.isInFrenzy()&&game.getPlayerOfTurn()+1==game.getFirstInFrenzyMode())
            handleEndOfGame();
         if (game.getPlayerOfTurn() == game.getPlayers().size()) {
             game.setPlayerOfTurn(1);
             game.setRound(game.getRound() + 1);
         } else
             game.setPlayerOfTurn(game.getPlayerOfTurn() + 1);
         if (game.getPlayers().get(game.getPlayerOfTurn() - 1).isConnected()) {
             game.setNumOfActionOfTheTurn(0);
             if (game.isTerminatorModeActive()) {
                 game.setHasToDoTerminatorAction(true);

                 if (game.getNumOfMaxActionForTurn() == 2)
                     game.setNumOfMaxActionForTurn(3);
             }
             if(game.getBoard().getAmmoTiledeck().getAmmoTile().size()<3){
                 game.getBoard().getAmmoTiledeck().shuffleDeck();
             }
             if(game.getBoard().getPowerUpDeck().getUsedPowerUpCards().size()<3){
                 game.getBoard().getPowerUpDeck().shuffleDeck();
             }
             game.getBoard().addWeaponsInSpawnSquare();
             game.getBoard().addAmmoTileInNormalSquare();
             game.sendMapEvent();
         }
     }

    private void handleEndOfGame() {
    }

    private void initializeScoreForPlayers() {
        for(int i=0 ; i<game.getPlayers().size();i++)
            scoreForPlayers.add(0);
    }

    private void assignScoreForDoubleKill() {
        for(int i=0 ; i<game.getPlayers().size(); i++){
            if(game.getPlayers().get(i).getNumOfKillDoneInTheTurn()==2)
                scoreForPlayers.set(i, scoreForPlayers.get(i));
        }
    }

    private void countScoreForPlayerDeath(Player deadPlayer) {
        if(!deadPlayer.getPlayerBoard().isFrenzyBoardPlayer()) {
            ColorOfFigure_Square colorOfFirstDamage = deadPlayer.getPlayerBoard().getDamages().get(0);
            scoreForPlayers.set(findIndexOfPlayerOfThisColor(colorOfFirstDamage), scoreForPlayers.get(findIndexOfPlayerOfThisColor(colorOfFirstDamage) + 1));
        }
        HashMap<ColorOfFigure_Square, Integer> hashMapForDamage = countFrequencyOfDamage(deadPlayer);
        int indexOfScoreToAssign = 0;
        int scoreToAssign;
        if(deadPlayer.getPlayerBoard().isFrenzyBoardPlayer())
            scoreToAssign = deadPlayer.getPlayerBoard().getScoreBarForFrenzyMode().get(indexOfScoreToAssign);
        else
            scoreToAssign = deadPlayer.getPlayerBoard().getScoreBarForNormalMode().get(indexOfScoreToAssign);
        while(!hashMapForDamage.isEmpty()){
            int max = findMaxFrequency(hashMapForDamage);
            ColorOfFigure_Square colorOfPlayerToAddScore;
            colorOfPlayerToAddScore = findColorWithMaxFrequency(hashMapForDamage,max);
            if(!maxIsUnique(hashMapForDamage,max)){

                ArrayList<ColorOfFigure_Square> colorsWithMaxFrequency = getColorsWithMaximumFrequency(hashMapForDamage,max);
                colorOfPlayerToAddScore = getFirstOfMaximumColor(colorsWithMaxFrequency, deadPlayer.getPlayerBoard().getDamages());
            }
            scoreForPlayers.set(findIndexOfPlayerOfThisColor(colorOfPlayerToAddScore), scoreForPlayers.get(findIndexOfPlayerOfThisColor(colorOfPlayerToAddScore)+scoreToAssign));
            hashMapForDamage.remove(colorOfPlayerToAddScore);
            indexOfScoreToAssign++;
            if(deadPlayer.getPlayerBoard().isFrenzyBoardPlayer())
                scoreToAssign = deadPlayer.getPlayerBoard().getScoreBarForFrenzyMode().get(indexOfScoreToAssign);
            else
                scoreToAssign = deadPlayer.getPlayerBoard().getScoreBarForNormalMode().get(indexOfScoreToAssign);
        }

        if(deadPlayer.getPlayerBoard().isFrenzyBoardPlayer())
            deadPlayer.getPlayerBoard().getScoreBarForFrenzyMode().remove(0);
        else
            deadPlayer.getPlayerBoard().getScoreBarForNormalMode().remove(0);

    }

    private ColorOfFigure_Square getFirstOfMaximumColor(ArrayList<ColorOfFigure_Square> colorsWithMaxFrequency, ArrayList<ColorOfFigure_Square> damages) {
        for(ColorOfFigure_Square damage : damages ){
            if(colorsWithMaxFrequency.contains(damage)){
                for(ColorOfFigure_Square color : colorsWithMaxFrequency){
                    if(color.toString().equals(damage.toString()))
                        return color;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private ArrayList<ColorOfFigure_Square> getColorsWithMaximumFrequency(HashMap<ColorOfFigure_Square, Integer> hashMapForDamage, int max) {
        ArrayList<ColorOfFigure_Square> colorsToReturn = new ArrayList<>();
        for(Map.Entry<ColorOfFigure_Square,Integer> entry : hashMapForDamage.entrySet()){
            if(entry.getValue()==max)
                colorsToReturn.add(entry.getKey());
        }
        return colorsToReturn;
    }

    private boolean maxIsUnique(HashMap<ColorOfFigure_Square, Integer> hashMapForDamage, int max) {
        return Collections.frequency(hashMapForDamage.values(), max)==1;
    }

    private int findMaxFrequency(HashMap<ColorOfFigure_Square, Integer> hashMapForDamage) {
        int max=1;
        for(int num : hashMapForDamage.values()){
            if(num>max)
                max=num;
        }
        return max;
    }

    private ColorOfFigure_Square findColorWithMaxFrequency(HashMap<ColorOfFigure_Square, Integer> hashMapForDamage, int max) {
        for(Map.Entry<ColorOfFigure_Square,Integer> entry : hashMapForDamage.entrySet()){
            if(entry.getValue()==max)
                return entry.getKey();
        }
        throw new IllegalArgumentException();
    }

    private HashMap<ColorOfFigure_Square, Integer> countFrequencyOfDamage(Player deadPlayer) {
        ArrayList<ColorOfFigure_Square> damage = deadPlayer.getPlayerBoard().getDamages();
        HashMap<ColorOfFigure_Square, Integer> hashMapToReturn = new HashMap<>();
        for(ColorOfFigure_Square color : ColorOfFigure_Square.values()){
            hashMapToReturn.put(color, Collections.frequency(damage, color));
        }
        return hashMapToReturn;
    }

    private int findIndexOfPlayerOfThisColor(ColorOfFigure_Square colorOfFirstDamage) {
        for(int i=0; i<game.getPlayers().size();i++){
            if(colorOfFirstDamage.toString().equals(game.getPlayers().get(i).getColorOfFigure().toString()))
                return i;
        }
        throw new IllegalArgumentException();
    }

    private ArrayList<Player> getPlayersDeadInThisTurn() {
        ArrayList<Player> deadPlayers = new ArrayList<>();
        for(Player player : game.getPlayers()){
            if(player.isDeath())
                deadPlayers.add(player);
        }
        return deadPlayers;
    }


}
