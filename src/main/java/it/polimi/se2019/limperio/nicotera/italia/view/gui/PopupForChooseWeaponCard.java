package it.polimi.se2019.limperio.nicotera.italia.view.gui;

import it.polimi.se2019.limperio.nicotera.italia.events.events_by_server.ServerEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class PopupForChooseWeaponCard {
    private JDialog popupForChooseW;
    private String nameOfCardToStoreForDiscardEvent = null;

     PopupForChooseWeaponCard(ServerEvent receivedEvent, MainFrame mainFrame) {
        popupForChooseW = new JDialog(mainFrame.getFrame());
        popupForChooseW.setUndecorated(receivedEvent.isRequestSelectionWeaponToReload());
         popupForChooseW.addWindowListener(new WindowAdapter() {
             @Override
             public void windowClosing(WindowEvent e) {
                 mainFrame.getRightPanel().getPanelOfActions().updateStateOfButton();
             }});

         JPanel contentPanel = new JPanel();
         int topBottomBorder = mainFrame.getFrame().getHeight()/mainFrame.resizeInFunctionOfFrame(true, 20);
         int leftRightBorder = mainFrame.getFrame().getWidth()/mainFrame.resizeInFunctionOfFrame(false, 20);
         contentPanel.setBorder(new EmptyBorder(topBottomBorder, leftRightBorder, topBottomBorder, leftRightBorder));
         popupForChooseW.getContentPane().add(contentPanel);
         PanelForWeapons panelForWeapons;
         panelForWeapons = new PanelForWeapons(mainFrame, null, receivedEvent, this);
         contentPanel.add(panelForWeapons.getContentPane());

         popupForChooseW.pack();
         popupForChooseW.setLocation((int) (mainFrame.getFrame().getLocation().getX() + mainFrame.getFrame().getSize().getWidth() - popupForChooseW.getWidth()) / 2,
                 (int) (mainFrame.getFrame().getLocation().getY() + mainFrame.getFrame().getSize().getHeight() - popupForChooseW.getHeight()) / 2);
         popupForChooseW.setVisible(true);
     }


     String getNameOfCardToStoreForDiscardEvent() {
        return nameOfCardToStoreForDiscardEvent;
    }

     JDialog getPopupForChooseW() {
        return popupForChooseW;
    }

}
