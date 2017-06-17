package com.PSR_Project;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

public class CzatClient extends JFrame {

    //GUI
    private JButton polacz, rozlacz;
    private JButton dobierz, stoj;
    private JPanel panel;
    private JTextField host;
    private JPanel stol;
    private JTextArea clientHand;
    private JTextArea dealerHand;
    private JList<String> zalogowani;
    private DefaultListModel<String> listaZalogowanych;
    //Klient
    private String nazwaSerwera = "localhost";
    private Klient watekKlienta;
    private CzatClient instancjaKlienta;
    private Czat serwer;
    private ClientImpl klient;
    private BlackJack blackJack;
    private int liczbaZwycieztw;

    public void setClientHand(String newCard) {
        clientHand.insert(newCard, 0);
        clientHand.setCaretPosition(0);
    }

    public void setDealerHand(String newCard) {
        dealerHand.insert(newCard,0);
        dealerHand.setCaretPosition(0);
    }

    public CzatClient() {
        super("Klient");

        instancjaKlienta = this;

        setSize(800, 600);
        setBackground(Color.green);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        panel = new JPanel(new FlowLayout());

        stol = new JPanel();
        stol.setSize(600, 600);

        stol.repaint();
        stol.revalidate();

        stol.setBackground(Color.green);

        dobierz = new JButton("Dobierz");
        stoj = new JButton("Stoj");

        clientHand = new JTextArea(7,55);
        dealerHand = new JTextArea(7,55);

        dobierz.setEnabled(false);
        stoj.setEnabled(false);

        host = new JTextField(nazwaSerwera, 12);
        polacz = new JButton("Połącz");
        rozlacz = new JButton("Rozłącz");
        rozlacz.setEnabled(false);

        listaZalogowanych = new DefaultListModel<>();
        zalogowani = new JList<>(listaZalogowanych);
        zalogowani.setFixedCellWidth(120);

        ObslugaZdarzen obsluga = new ObslugaZdarzen();

        polacz.addActionListener(obsluga);
        rozlacz.addActionListener(obsluga);

        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                rozlacz.doClick();
                setVisible(false);
                System.exit(0);
            }
        });

        panel.add(new JLabel("Serwer RMI: "));
        panel.add(host);
        panel.add(polacz);
        panel.add(rozlacz);

        add(panel, BorderLayout.NORTH);

        clientHand.setLineWrap(true);
        dealerHand.setLineWrap(true);
        JScrollPane clientHandScroll = new JScrollPane(clientHand);
        JScrollPane dealerHandScroll = new JScrollPane(dealerHand);
        clientHandScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dealerHandScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        stol.add(clientHandScroll, BorderLayout.CENTER);
        stol.add(dealerHandScroll, BorderLayout.CENTER);

        stol.add(dobierz, BorderLayout.SOUTH);
        stol.add(stoj, BorderLayout.SOUTH);

        stol.repaint();
        stol.revalidate();

        add(stol, BorderLayout.CENTER);

        add(new JScrollPane(zalogowani), BorderLayout.EAST);

        setResizable(false);

        repaint();
        revalidate();

        setVisible(true);

        liczbaZwycieztw = 0;

        blackJack = new BlackJack(CzatClient.this);
    }


    private class ObslugaZdarzen extends KeyAdapter implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Połącz")) {
                wyswietlKomunikat("Łączę z: " + nazwaSerwera + "...");
                polacz.setEnabled(false);
                rozlacz.setEnabled(true);
                host.setEnabled(false);
                watekKlienta = new Klient();
                watekKlienta.start();

                dobierz.setEnabled(true);
                stoj.setEnabled(true);
            }
            if (e.getActionCommand().equals("Rozłącz")) {
                listaZalogowanych.clear();
                try {
                    serwer.opusc(klient);
                } catch (Exception ex) {
                    System.out.println("Błąd: " + ex);
                }
                rozlacz.setEnabled(false);
                polacz.setEnabled(true);
                host.setEnabled(true);

                dobierz.setEnabled(false);
                stoj.setEnabled(false);
            }

            dobierz.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (blackJack.graWToku() == true)
                        blackJack.kolejnaKartaGracza();
                    else
                        dobierz.setEnabled(false);
                }
            } );
            stoj.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    while (blackJack.punktyDealera() < 17) {
                        blackJack.kolejnaKartaDealera();
                    }

                    blackJack.endGame();
                }
            } );
        }
    }
    public void wygranaPartia () {
        klient.dodajZwycieztwo(++liczbaZwycieztw);
    }

    private class Klient extends Thread {

        public void run() {
            try {
                Registry rejestr = LocateRegistry.getRegistry(host.getText());
                serwer = (Czat) rejestr.lookup("RMICzat");
                wyswietlKomunikat("Połączyłem się z serwerem.");
                String nick = JOptionPane.showInputDialog(null, "Podaj nick: ");
                klient = new ClientImpl(instancjaKlienta, nick, liczbaZwycieztw);
                serwer.dolacz(klient);

                if (blackJack.graWToku() == false)
                    blackJack.zacznijGre();

            } catch (Exception e) {
                System.out.println("Błąd połączenia: " + e);
            }
        }
    }

    public void wyswietlKomunikat(String tekst) {

    }

    public void odswiezListe(Vector<Client> lista) {

        listaZalogowanych.clear();

        for (Client n : lista) {
            try {
                listaZalogowanych.addElement(n.pobierzNicka());
                System.out.println(n.pobierzNicka());
            } catch (Exception e) {
                System.out.println("Błąd: " + e);
            }
        }
    }

    public static void main(String[] args) {
        new CzatClient();
    }
}

