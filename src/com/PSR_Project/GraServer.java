package com.PSR_Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

public class GraServer extends JFrame {

    //GUI
    private JButton uruchom, zatrzymaj;
    private JPanel panel;
    private JTextField port;
    private JTextArea komunikaty;
    private JList<String> zalogowani;
    private DefaultListModel<String> listaZalogowanych;
    //Serwer
    private int numerPortu = 1099;
    GraServer instancjaSerwera;
    private Serwer srw;

    public GraServer() {
        super("Serwer");

        instancjaSerwera = this;

        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new JPanel(new FlowLayout());
        komunikaty = new JTextArea();
        komunikaty.setLineWrap(true);
        komunikaty.setEditable(false);

        port = new JTextField((new Integer(numerPortu)).toString(), 8);
        uruchom = new JButton("Uruchom");
        zatrzymaj = new JButton("Zatrzymaj");
        zatrzymaj.setEnabled(false);

        listaZalogowanych = new DefaultListModel<>();
        zalogowani = new JList<>(listaZalogowanych);
        zalogowani.setFixedCellWidth(120);

        panel.add(new JLabel("Port RMI: "));
        panel.add(port);
        panel.add(uruchom);
        panel.add(zatrzymaj);

        add(new JScrollPane(zalogowani), BorderLayout.EAST);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(komunikaty), BorderLayout.CENTER);

        setVisible(true);

        uruchom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                srw = new Serwer();
                srw.start();
                uruchom.setEnabled(false);
                zatrzymaj.setEnabled(true);
                port.setEnabled(false);
                repaint();
            }
        });
        zatrzymaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                srw.kill();
                zatrzymaj.setEnabled(false);
                uruchom.setEnabled(true);
                port.setEnabled(true);
                repaint();
            }
        });
    }

    public void odswiezListe(Vector<Client> lista) {

        listaZalogowanych.clear();

        for (Client n : lista) {
            try {
                listaZalogowanych.addElement(n.pobierzNicka());
                System.out.println(n.pobierzNicka());
            } catch (Exception e) {
                System.out.println("Blad: " + e);
            }
        }
    }

    private class Serwer extends Thread {

        Registry rejestr;

        public void kill() {
            try {
                rejestr.unbind("RMIBlackjack");
                wyswietlKomunikat("Serwer zostal wyrejestrowany.");
            } catch (Exception e) {
                wyswietlKomunikat("Nie udało sie wyrejestrowac serwera.");
            }
        }

        public void run() {

            try {
                rejestr = LocateRegistry.createRegistry(new Integer(port.getText()));
                wyswietlKomunikat("Utworzylem nowy rejestr na porcie: " + port.getText());
            } catch (Exception e) {
                wyswietlKomunikat("Nie powiodlo sie utworzenie rejestru...\nProba skorzystania z istniejacego...");
            }
            if (rejestr == null) {
                try {
                    rejestr = LocateRegistry.getRegistry();
                } catch (Exception e) {
                    wyswietlKomunikat("Brak uruchomionego rejestru.");
                }
            }
            try {
                GraImpl serwer = new GraImpl(instancjaSerwera);
                rejestr.rebind("RMIBlackjack", serwer);
                wyswietlKomunikat("Serwer zostal poprawnie zarejestrowany i uruchomiony.");
            } catch (Exception e) {
                wyswietlKomunikat("Nie udalo sie zarejestrowac i uruchomic serwera.");
            }
        }
    }

    public void wyswietlKomunikat(String tekst) {
        komunikaty.append(tekst + "\n");
        komunikaty.setCaretPosition(komunikaty.getDocument().getLength());
    }

    public static void main(String[] args) {
        new GraServer();
    }
}

