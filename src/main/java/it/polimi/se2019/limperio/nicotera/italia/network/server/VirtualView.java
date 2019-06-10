package it.polimi.se2019.limperio.nicotera.italia.network.server;

import it.polimi.se2019.limperio.nicotera.italia.controller.Controller;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.*;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_client.AnswerInitializationEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_by_client.ClientEvent;
import it.polimi.se2019.limperio.nicotera.italia.model.KillshotTrack;
import it.polimi.se2019.limperio.nicotera.italia.model.PlayerBoard;
import it.polimi.se2019.limperio.nicotera.italia.model.Square;
import it.polimi.se2019.limperio.nicotera.italia.utils.Observable;
import it.polimi.se2019.limperio.nicotera.italia.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * View of the client in the server side. Handle the communication with the client and the exchange of event.
 * <p>
 *     Each client has her virtual view.
 * </p>
 * <p>
 *     Run in a thread.
 * </p>
 *
 * @author Pietro L'Imperio
 */

public class VirtualView extends Observable<ClientEvent> implements Observer<ServerEvent>,Runnable {
    /**
     * The reference of the client associated
     */
    private Socket client;
    /**
     * It's true until the client is online, false later.
     */
    private boolean isClientCurrentlyOnline=true;
    /**
     * The nickname of the client associated
     */
    private String nicknameOfClient;
    /**
     * The color of the client associated
     */
    private String colorOfClient;
    /**
     * The reference of the server
     */
    private Server server;
    /**
     * The object input stream
     */
    private ObjectInputStream in = null;
    /**
     * The object output stream
     */
    private ObjectOutputStream out = null;
    /**
     * The reference of the controller
     */
    private Controller controller;
    /**
     * It's true if the client associated represents the first player, false otherwise
     */
    private boolean firstPlayer;

    private ArrayList<PlayerBoard> listOfPlayerBoards = new ArrayList<>();

    private PlayerBoard myPlayerBoard;

    private Square[][] map;

    private KillshotTrack killshotTrack;

    private String IPAddress;

    private boolean terminatorMode = false;

    private int typeOfMap;

    /**
     * Sets first player attribute, initialize the object streams
     * @param client The client associated
     * @param server The server that created it
     * @param controller The controller which he will send events by client with
     */
     VirtualView(Socket client, Server server, Controller controller) {
        this.client = client;
        this.IPAddress = client.getRemoteSocketAddress().toString();
        this.server = server;
        this.controller = controller;
        register(controller);
        this.firstPlayer = server.getListOfClient().size() ==1;

        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            try {
                client.close();
            } catch (Exception er) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Manages the first phase of initialization of the client and then will remain in listening for
     * the handle of the event by client
     */
    @Override
    public void run() {
        try {
            boolean invalidInitialization = true;
            RequestInitializationEvent req;
            if(!server.isGameIsStarted()) {
                while (invalidInitialization) {
                    out.writeObject(new RequestInitializationEvent("Digit your nickname", true, false, false, false, false));
                    AnswerInitializationEvent ans = (AnswerInitializationEvent) in.readObject();
                    while (isNotValidNickname(ans.getNickname())) {
                        req = new RequestInitializationEvent("Digit your nickname: ", true, false, false, false, false);
                        req.setRetake(true);
                        out.writeObject(req);
                        ans = (AnswerInitializationEvent) in.readObject();
                    }
                    server.addNickname(ans.getNickname());
                    nicknameOfClient = ans.getNickname();
                    out.writeObject(new RequestInitializationEvent("Digit your color:", false, true, false, false, false));
                    ans = (AnswerInitializationEvent) in.readObject();
                    while (server.getListOfColor().contains(ans.getColor().toUpperCase())) {
                        req = new RequestInitializationEvent("Digit your color: ", false, true, false, false, false);
                        req.setRetake(true);
                        out.writeObject(req);
                        ans = (AnswerInitializationEvent) in.readObject();
                    }
                    server.getListOfColor().add(ans.getColor().toUpperCase());
                    if (server.getListOfColor().size() == 3)
                        server.startTimer();
                    colorOfClient = ans.getColor().toUpperCase();
                    if (firstPlayer) {
                        out.writeObject(new RequestInitializationEvent("Choose if u want frenzy:", false, false, true, false, false));
                        ans = (AnswerInitializationEvent) in.readObject();
                        server.setAnticipatedFrenzy(ans.isFrenzy());
                        out.writeObject(new RequestInitializationEvent("Choose which map do you want to play with:", false, false, false, false, true));
                        ans = (AnswerInitializationEvent) in.readObject();
                        if (ans.getMap() != 0)
                            server.setTypeMap(ans.getMap());
                        out.writeObject(new RequestInitializationEvent("Choose if u want terminator mode:", false, false, false, true, false));
                        ans = (AnswerInitializationEvent) in.readObject();
                        server.setTerminatorMode(ans.isTerminator());
                    }
                    req = new RequestInitializationEvent("", false, false, false, false, false);
                    req.setAck(true);
                    out.writeObject(req);
                    invalidInitialization = false;
                }
            }
        } catch (IOException se) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        while (isClientCurrentlyOnline) {
            ClientEvent newEvent;
            try {
                newEvent = (ClientEvent) in.readObject();
                newEvent.setMyVirtualView(this);
                notify(newEvent);
            } catch (SocketException se) {
                handleDisconnection();
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

     String handleReconnection() {
        System.out.println("Sono arrivato in handleReconnection");
        RequestInitializationEvent req = new RequestInitializationEvent("The game is already started: if you were disconnected, reinsert your previous nickname and you will be readmitted to the game", true, false, false, false, false );
         try {
             out.writeObject(req);
             AnswerInitializationEvent answer = (AnswerInitializationEvent) in.readObject();
             if(server.getListOfNickname().contains(answer.getNickname())){
                 if(!controller.findPlayerWithThisNickname(answer.getNickname()).isConnected())
                     return answer.getNickname();
                 else
                     return "Failed reconnection";
             }
             else
                 return "Failed reconnection";
         } catch (IOException e) {
             System.exit(0);
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
         }
         return "Failed reconnection";
     }

     void updateStatusAfterReconnection() throws IOException {
        PlayerBoardEvent playerBoardEvent = new PlayerBoardEvent();
        playerBoardEvent.setNicknameInvolved(nicknameOfClient);
        playerBoardEvent.setPlayerBoard(myPlayerBoard);
        out.writeObject(playerBoardEvent);
        KillshotTrackEvent killshotTrackEvent = new KillshotTrackEvent("", killshotTrack);
        killshotTrackEvent.setNicknameInvolved(nicknameOfClient);
        out.writeObject(killshotTrackEvent);
        MapEvent mapEvent = new MapEvent();
        mapEvent.setMap(map);
        mapEvent.setNicknameInvolved(nicknameOfClient);
        mapEvent.setTerminatorMode(terminatorMode);
        mapEvent.setTypeOfMap(typeOfMap);
        out.writeObject(mapEvent);
    }
    private boolean isNotValidNickname(String nickname) {
        if(server.getListOfNickname().contains(nickname))
            return true;
        if(nickname.equalsIgnoreCase("me"))
            return true;
        if(nickname.equalsIgnoreCase("terminator"))
            return true;
        return nickname.length()<2;
    }

    /**
     * Sends events received by Model and Controller to the client through socket stream only if its client is involved
     * @param event The event received
     */
    @Override
    public void update(ServerEvent event) {
        if(event.getNicknames().contains(nicknameOfClient) || event.getNicknameInvolved().equals(nicknameOfClient)) {
            try {
                out.writeObject(event);
            } catch (IOException e) {
               handleDisconnection();
            }
        }
        if(event.isKillshotTrackEvent()){
            updateKillshotTrack(event);
        }
        if(event.isMapEvent()){
            if(map == null){
                terminatorMode = ((MapEvent) event).isTerminatorMode();
                typeOfMap = ((MapEvent)event).getTypeOfMap();
            }
            updateMap(event);
        }
        if(event.isPlayerBoardEvent()){
            updatePlayerBoard(event);
        }
    }

    private void updatePlayerBoard(ServerEvent event) {
        PlayerBoardEvent eventOfPlayerBoard = (PlayerBoardEvent) event;
        if(listOfPlayerBoards.isEmpty() || !listOfPlayerBoards.contains(eventOfPlayerBoard.getPlayerBoard())){
            listOfPlayerBoards.add(eventOfPlayerBoard.getPlayerBoard());
        }
        else{
            for(PlayerBoard playerBoard : listOfPlayerBoards){
                if(playerBoard.getNicknameOfPlayer().equals(eventOfPlayerBoard.getPlayerBoard().getNicknameOfPlayer())){
                    playerBoard = eventOfPlayerBoard.getPlayerBoard();
                    break;
                }
            }
        }
        if(eventOfPlayerBoard.getNicknameInvolved().equals(nicknameOfClient)){
            myPlayerBoard = eventOfPlayerBoard.getPlayerBoard();
        }
    }

    private void updateMap(ServerEvent event) {
        MapEvent eventOfMap = (MapEvent) event;
        this.map = eventOfMap.getMap();
    }

    private void updateKillshotTrack(ServerEvent event) {
        KillshotTrackEvent eventKillshotTrack = (KillshotTrackEvent) event;
        killshotTrack = eventKillshotTrack.getKillShotTrack();
    }


    /**
     * Handles the disconnection of the client sending information to the server and the controller
     */
     void handleDisconnection(){
        isClientCurrentlyOnline=false;
        if(!server.isGameIsStarted()) {
            server.getListOfNickname().remove(nicknameOfClient);
            server.getListOfColor().remove(colorOfClient);
            server.deregister(this, client);
        }
        else{
            controller.handleDisconnection(nicknameOfClient);
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PlayerBoard> getListOfPlayerBoards() {
        return listOfPlayerBoards;
    }

    public PlayerBoard getMyPlayerBoard() {
        return myPlayerBoard;
    }

    public Square[][] getMap() {
        return map;
    }

    public KillshotTrack getKillshotTrack() {
        return killshotTrack;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void setClientCurrentlyOnline(boolean clientCurrentlyOnline) {
        isClientCurrentlyOnline = clientCurrentlyOnline;
    }
}