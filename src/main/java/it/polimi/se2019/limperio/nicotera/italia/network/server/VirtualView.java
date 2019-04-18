package it.polimi.se2019.limperio.nicotera.italia.network.server;

import it.polimi.se2019.limperio.nicotera.italia.controller.Controller;
import it.polimi.se2019.limperio.nicotera.italia.events.events_of_model.ModelEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_of_model.RequestNicknameEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_of_view.AnswerNicknameEvent;
import it.polimi.se2019.limperio.nicotera.italia.events.events_of_view.ViewEvent;
import it.polimi.se2019.limperio.nicotera.italia.utils.Observable;
import it.polimi.se2019.limperio.nicotera.italia.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VirtualView extends Observable<ViewEvent> implements Observer<ModelEvent>,Runnable {
    Socket client;
    String nicknameOfClient;
    String colorOfClient;
    Server server;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    Controller controller;
    boolean firstPlayer;

    public VirtualView(Socket client, Server server, Controller controller) {
        this.client = client;
        this.server = server;
        register(controller);
        if(server.getListOfClient().size()==1)
            this.firstPlayer=true;
        else
            this.firstPlayer=false;
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            try { client.close(); }
            catch(Exception er) { System.out.println(e.getMessage());}
        }
    }

    @Override
    public void run() {
        try {
            boolean invalidInizialization = true;
            while (invalidInizialization) {
                out.writeObject(new RequestNicknameEvent("Chiedo informazioni al mio player", null, true, false, false, firstPlayer));

                AnswerNicknameEvent ans = (AnswerNicknameEvent) in.readObject();
                if(server.getListOfNickname().contains(ans.getNickname()) && server.getListOfColor().contains(ans.getColor())){
                    out.writeObject(new RequestNicknameEvent("nickname e colori gia scelti, riprova al prossimo tentativo", null, false, true, false, firstPlayer));
                }
                else {
                    if (server.getListOfColor().contains(ans.getColor())) {
                        out.writeObject(new RequestNicknameEvent("colore gia scelto, riprova al prossimo tentativo", null, false, true, false, firstPlayer));
                    }
                    if (server.getListOfNickname().contains(ans.getNickname())) {
                        out.writeObject(new RequestNicknameEvent("nickname gia scelto, riprova al prossimo tentativo", null,  false, true, false, firstPlayer));
                    }
                    if(!(server.getListOfNickname().contains(ans.getNickname()))&&!(server.getListOfColor().contains(ans.getColor()))){
                        out.writeObject(new RequestNicknameEvent("Tutto ok " + ans.getNickname(), ans.getNickname(), false, true, true, firstPlayer));
                        server.addNickname(ans.getNickname(),ans.getColor());
                        nicknameOfClient=ans.getNickname();
                        colorOfClient=ans.getColor();
                        invalidInizialization=false;
                        if(firstPlayer)
                        {
                            server.setAnticipatedFrenzy(ans.isFrenzy());
                            System.out.println("Aggiornato frenzy a "+ server.isAnticipatedFrenzy());
                            server.setTypeMap(ans.getMap());
                            System.out.println("Aggiornata mappa a "+ server.getTypeMap());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        while(client.isConnected()){
            ViewEvent newEvent = null;
            try {
                System.out.println("La virtual view di "+ nicknameOfClient + " e in attesa di messaggi provenienti dalla remote view..");
                newEvent = (ViewEvent) in.readObject();
                notify(newEvent);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        server.getListOfNickname().remove(nicknameOfClient);
        server.getListOfColor().remove(colorOfClient);
        server.deregister(this, client);
        try {
            in.close();
            out.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void notify(ViewEvent message) {
        controller.update(message);
    }

    @Override
    public void update(ModelEvent message) {
        try {
            message.setNickname(nicknameOfClient);
            out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(Observer<ViewEvent> observer) {
        this.controller= (Controller) observer;
    }

    @Override
    public void deregister(Observer<ViewEvent> observer) {}
}