package it.polimi.se2019.limperio.nicotera.italia.view.gui;


import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.*;
import it.polimi.se2019.limperio.nicotera.italia.model.Square;
import it.polimi.se2019.limperio.nicotera.italia.view.RemoteView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class MainFrame {

    private JFrame frame;
    private JPanel contentPane;
    private RemoteView remoteView;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private MapPanel mapPanel;
    private KillshotTrackPanel killshotTrackPanel;
    private PopupForChooseWeaponCard popupForChooseWeaponCardToCatch;


    public MainFrame(RemoteView remoteView) {
        this.remoteView = remoteView;
        frame = new JFrame("Adrenaline");
        frame.setIconImage(getResource("/favicon.jpg"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()*2/3, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*3/4));
        contentPane = new JPanel();
        frame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
       /* contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateFrame();
            }
        });*/
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                    updateFrame();
                }});

        mapPanel = new MapPanel(this);
        contentPane.add(mapPanel, BorderLayout.CENTER);

        killshotTrackPanel = new KillshotTrackPanel(this);
        contentPane.add(killshotTrackPanel, BorderLayout.NORTH);


        leftPanel = new LeftPanel(this, remoteView.getMyPlayerBoardView());
        contentPane.add(leftPanel, BorderLayout.WEST);

        rightPanel = new RightPanel(this);
        contentPane.add(rightPanel.getPanel(), BorderLayout.EAST);
        frame.setVisible(true);
    }

    RemoteView getRemoteView() {
        return remoteView;
    }

    JFrame getFrame() {
        return frame;
    }

    void setLeftPanel(LeftPanel leftPanel) {
        this.leftPanel = leftPanel;
    }

    LeftPanel getLeftPanel() {
        return leftPanel;
    }

    MapPanel getMapPanel() {
        return mapPanel;
    }


    public void showMessage(ServerEvent receivedEvent) {
        new DialogForMessage(this, receivedEvent);
    }

    public void handleRequestToDiscardPowerUpCard(ServerEvent receivedEvent) {
        new PopupForDiscardPowerUp(this, receivedEvent);
    }


    public void updateLeftPanelForWhoIsViewing(String nicknameOfThePlayerInvolvedInTheUpdate) {
        if (leftPanel.getPlayerBoardPanel().getPlayerBoardViewed().getNicknameOfPlayer().equals(nicknameOfThePlayerInvolvedInTheUpdate)) {
            if (leftPanel.getPlayerBoardPanel().getDialogForDamage() != null)
                leftPanel.getPlayerBoardPanel().getDialogForDamage().setVisible(false);
            if (leftPanel.getPlayerBoardPanel().getDialogForMarks() != null)
                leftPanel.getPlayerBoardPanel().getDialogForMarks().setVisible(false);
            LeftPanel newLeftPanel = new LeftPanel(this, remoteView.getPlayerBoardViewOfThisPlayer(nicknameOfThePlayerInvolvedInTheUpdate));
            contentPane.remove(leftPanel);
            contentPane.add(newLeftPanel, BorderLayout.WEST);
            leftPanel = newLeftPanel;
            contentPane.validate();
            contentPane.repaint();
            leftPanel.getPlayerBoardPanel().addDialogForDamage();
            leftPanel.getPlayerBoardPanel().addDialogForMarks();
        }
    }

    public void updatePanelOfAction() {
        rightPanel.getPanelOfActions().updateStateOfButton();
    }

    public void updateEnableSquares(ArrayList<Square> squaresReachableWithRunAction) {
        mapPanel.updateEnableSquares(squaresReachableWithRunAction);
    }

    public void showPopupForChooseWeapon(ServerEvent receivedEvent) {
        if (receivedEvent.isRequestForChooseAWeaponToCatch())
            popupForChooseWeaponCardToCatch = new PopupForChooseWeaponCard(receivedEvent, this);
        if (receivedEvent.isRequestToDiscardWeaponCard())
            popupForChooseWeaponCardToCatch = new PopupForChooseWeaponCard(receivedEvent, this);
        if (receivedEvent.isRequestSelectionWeaponToReload())
            popupForChooseWeaponCardToCatch = new PopupForChooseWeaponCard(receivedEvent, this);
    }

    public void hidePopup() {
        if (popupForChooseWeaponCardToCatch != null)
            popupForChooseWeaponCardToCatch.getPopupForChooseW().setVisible(false);
    }

    public void updateFigureOnMap() {
        mapPanel.addFigureOnSquare(this);
    }

    public void updateNorthPanel() {
        contentPane.remove(killshotTrackPanel);
        if (killshotTrackPanel.getDialogForNormalSkull() != null) {
            killshotTrackPanel.getDialogForNormalSkull().setVisible(false);
            killshotTrackPanel.setNullDialogForNormalSkull();
        }

        if (killshotTrackPanel.getDialogForFrenzySkull() != null) {
            killshotTrackPanel.getDialogForFrenzySkull().setVisible(false);
            killshotTrackPanel.setNullDialogForFrenzySkull();
        }

        killshotTrackPanel = new KillshotTrackPanel(this);
        contentPane.add(killshotTrackPanel, BorderLayout.NORTH);
        contentPane.validate();
        contentPane.repaint();

        killshotTrackPanel.addDialogForNormalKillshot();
        killshotTrackPanel.addDialogForFrenzyKillshot();
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public void handleRequestToChooseAnEffect(ServerEvent receivedEvent) {
        PopupForChooseEffect popupForChooseEffect = new PopupForChooseEffect(this, (RequestToChooseAnEffect) receivedEvent);
        popupForChooseEffect.getDialog().setVisible(true);
    }

    public void updatePanelOfPlayers() {
        rightPanel.getPanel().remove(rightPanel.getPanelOfPlayers());
        rightPanel.setPanelOfPlayers(new PanelOfPlayers(this));
        rightPanel.getPanel().add(rightPanel.getPanelOfPlayers(), rightPanel.getGbcPanelOfPlayers());
        contentPane.validate();
        contentPane.repaint();

    }

    public void handleRequestToPayWithAmmoOrPUC(ServerEvent receivedEvent) {
        new PopupToPayWithAmmoOrPowerUpCard(this, receivedEvent);
    }

    public void handleRequestToChooseAPlayer(ServerEvent receivedEvent) {
        new PopupToChooseAPlayer(this, receivedEvent);
    }

    KillshotTrackPanel getKillshotTrackPanel() {
        return killshotTrackPanel;
    }

    public void handleRequestToChooseMultiplePlayers(ServerEvent receivedEvent) {
        new PopupToChooseMultiplePlayers(receivedEvent, this);
    }

    int resizeInFunctionOfFrame(boolean height, int originalSize){
        int sizeOfReference;
        if(height)
            sizeOfReference = frame.getHeight();
        else
            sizeOfReference = frame.getWidth();
        return sizeOfReference/originalSize;
    }

    Image getResource(String path){
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource(path));
    }

     private void updateFrame() {
        for (JDialog dialog : mapPanel.getDialogForFigure()) {
            dialog.setVisible(false);
        }
        if (killshotTrackPanel.getDialogForNormalSkull() != null) {
            killshotTrackPanel.getDialogForNormalSkull().setVisible(false);
            killshotTrackPanel.setNullDialogForNormalSkull();
        }
        if (killshotTrackPanel.getDialogForFrenzySkull() != null) {
            killshotTrackPanel.getDialogForFrenzySkull().setVisible(false);
            killshotTrackPanel.setNullDialogForFrenzySkull();
        }

        if (leftPanel.getPlayerBoardPanel().getDialogForDamage() != null)
            leftPanel.getPlayerBoardPanel().getDialogForDamage().setVisible(false);
        if (leftPanel.getPlayerBoardPanel().getDialogForMarks() != null)
            leftPanel.getPlayerBoardPanel().getDialogForMarks().setVisible(false);
        contentPane.removeAll();

        mapPanel = new MapPanel(this);
        contentPane.add(mapPanel, BorderLayout.CENTER);

        killshotTrackPanel = new KillshotTrackPanel(this);
        contentPane.add(killshotTrackPanel, BorderLayout.NORTH);

        leftPanel = new LeftPanel(this, leftPanel.getPlayerBoardView());
        contentPane.add(leftPanel, BorderLayout.WEST);

        rightPanel = new RightPanel(this);
        contentPane.add(rightPanel.getPanel(), BorderLayout.EAST);

        contentPane.validate();
        contentPane.repaint();

        mapPanel.addFigureOnSquare(this);
        killshotTrackPanel.addDialogForNormalKillshot();
        killshotTrackPanel.addDialogForFrenzyKillshot();
        leftPanel.getPlayerBoardPanel().addDialogForDamage();
        leftPanel.getPlayerBoardPanel().addDialogForMarks();
    }



}


