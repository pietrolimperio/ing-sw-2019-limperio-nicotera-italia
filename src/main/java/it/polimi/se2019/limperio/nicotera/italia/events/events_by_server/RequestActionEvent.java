package it.polimi.se2019.limperio.nicotera.italia.events.events_by_server;

import java.util.ArrayList;

public class RequestActionEvent extends ServerEvent {

    private boolean canRun = false;
    private boolean canCatch = false;
    private boolean canShoot = false;
    private boolean canUseNewton = false;
    private boolean canUseTeleporter = false;
    private boolean canUseTagbackGranade = false;
    private boolean canUseWeapon1 = false;
    private boolean canUseWeapon2 = false;
    private boolean canUseWeapon3 = false;
    private boolean hasToDoTerminatorAction = false;

    private int round;
    private int numOfAction;
    private ArrayList<String> nicknamesOfPlayersAttacked = new ArrayList<>();
    private ArrayList<String> messageForPlayersAttacked = new ArrayList<>();

    public boolean isCanShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public boolean isCanUseNewton() {
        return canUseNewton;
    }

    public void setCanUseNewton(boolean canUseNewton) {
        this.canUseNewton = canUseNewton;
    }

    public boolean isCanUseTeleporter() {
        return canUseTeleporter;
    }

    public void setCanUseTeleporter(boolean canUseTeleporter) {
        this.canUseTeleporter = canUseTeleporter;
    }

    public boolean isCanUseTagbackGranade() {
        return canUseTagbackGranade;
    }



    public void setCanUseTagbackGranade(boolean canUseTagbackGranade) {
        this.canUseTagbackGranade = canUseTagbackGranade;
    }

    public boolean isCanUseWeapon1() {
        return canUseWeapon1;
    }

    public void setCanUseWeapon1(boolean canUseWeapon1) {
        this.canUseWeapon1 = canUseWeapon1;
    }

    public boolean isCanUseWeapon2() {
        return canUseWeapon2;
    }

    public void setCanUseWeapon2(boolean canUseWeapon2) {
        this.canUseWeapon2 = canUseWeapon2;
    }

    public boolean isCanUseWeapon3() {
        return canUseWeapon3;
    }

    public void setCanUseWeapon3(boolean canUseWeapon3) {
        this.canUseWeapon3 = canUseWeapon3;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isHasToDoTerminatorAction() {
        return hasToDoTerminatorAction;
    }

    public void setHasToDoTerminatorAction(boolean hasToDoTerminatorAction) {
        this.hasToDoTerminatorAction = hasToDoTerminatorAction;
    }

    public boolean isCanRun() {
        return canRun;
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public boolean isCanCatch() {
        return canCatch;
    }

    public void setCanCatch(boolean canCatch) {
        this.canCatch = canCatch;
    }

    @Override
    public int getNumOfAction() {
        return numOfAction;
    }

    @Override
    public void setNumOfAction(int numOfAction) {
        this.numOfAction = numOfAction;
    }

    public ArrayList<String> getNicknamesOfPlayersAttacked() {
        return nicknamesOfPlayersAttacked;
    }

    public void setNicknamesOfPlayersAttacked(ArrayList<String> nicknamesOfPlayersAttacked) {
        this.nicknamesOfPlayersAttacked = nicknamesOfPlayersAttacked;
    }

    public ArrayList<String> getMessageForPlayersAttacked() {
        return messageForPlayersAttacked;
    }

    public void setMessageForPlayersAttacked(ArrayList<String> messageForPlayersAttacked) {
        this.messageForPlayersAttacked = messageForPlayersAttacked;
    }
}