package com.PSR_Project;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;

public class CzatClient extends JFrame {

    //GUI
    private JButton polacz, rozlacz;
    private JButton dobierz, stoj, nastepneRozdanie;
    private JPanel sterowanieRozgrywka;
    private JPanel panel;
    private JTextField host;
    private JPanel stol;
    private JTextArea clientHand;
    private JTextArea dealerHand;
    private JLabel punktacjaClienta;
    private JLabel punktacjaDealera;
    private JList<String> zalogowani;
    private DefaultListModel<String> listaZalogowanych;
    private JPanel kartyGracza;
    private JPanel kartyDealera;
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
    public void setPunktacjaClientaLabel (String punktacja) {
        punktacjaClienta.setText(punktacja);
    }
    public void setPunktacjaDealeraLabel (String punktacja) { punktacjaDealera.setText(punktacja); }

    public CzatClient() {
        super("Klient");

        instancjaKlienta = this;

        setSize(800, 700);
        setBackground(Color.green);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout());

        panel = new JPanel(new FlowLayout());

        stol = new JPanel();
        stol.setSize(600, 600);

        stol.repaint();
        stol.revalidate();

        stol.setBackground(new Color(18,175,18));

        sterowanieRozgrywka = new JPanel(new FlowLayout());

        dobierz = new JButton("Dobierz");
        stoj = new JButton("Stoj");
        nastepneRozdanie = new JButton("Nastepne rozdanie");

        dobierz.setEnabled(false);
        stoj.setEnabled(false);

        punktacjaClienta = new JLabel("Punktacja klienta");
        punktacjaClienta.setBounds(296, 256, 150, 20);
        punktacjaDealera = new JLabel("Punktacja Dealera");
        punktacjaDealera.setBounds(296, 130, 150, 20);

        kartyGracza = new JPanel(new FlowLayout());
        kartyGracza.setBounds(10, 382, 642, 110);
        kartyGracza.setBackground(Color.green);

        kartyDealera = new JPanel(new FlowLayout());
        kartyDealera.setBounds(10, 19, 642, 110);
        kartyDealera.setBackground(Color.green);

        host = new JTextField(nazwaSerwera, 12);
        polacz = new JButton("Polacz");
        rozlacz = new JButton("Rozlacz");
        rozlacz.setEnabled(false);

        listaZalogowanych = new DefaultListModel<>();
        zalogowani = new JList<>(listaZalogowanych);
        zalogowani.setFixedCellWidth(120);

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

        clientHand = new JTextArea(7,55);
        dealerHand = new JTextArea(7,55);

        getContentPane().add(panel, BorderLayout.NORTH);
        JScrollPane rekaGracza = new JScrollPane(clientHand);
        rekaGracza.setBounds(191, 281, 281, 90);
        JScrollPane rekaDealera = new JScrollPane(dealerHand);
        rekaDealera.setBounds(191, 155, 281, 90);
        rekaGracza.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rekaDealera.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        stol.setLayout(null);

        stol.add(kartyDealera);
        stol.add(rekaDealera);

        stol.add(punktacjaDealera);
        stol.add(punktacjaClienta);
        stol.add(rekaGracza);
        stol.add(kartyGracza);

        sterowanieRozgrywka.add(dobierz, BorderLayout.SOUTH);
        sterowanieRozgrywka.add(stoj, BorderLayout.SOUTH);
        sterowanieRozgrywka.add(nastepneRozdanie, BorderLayout.SOUTH);

        getContentPane().add(sterowanieRozgrywka, BorderLayout.SOUTH);
        getContentPane().add(stol, BorderLayout.CENTER);

        JLabel blackJokerLabel = new JLabel();
        JLabel redJokerLabel = new JLabel();
        BufferedImage blackJoker = null;
        BufferedImage redJoker = null;
        try {
            blackJoker = ImageIO.read(new File("KartyPNG\\black_joker.png"));
            redJoker = ImageIO.read(new File("KartyPNG\\red_joker.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        blackJokerLabel.setIcon(new ImageIcon(blackJoker.getScaledInstance(70, 100, Image.SCALE_SMOOTH)));
        blackJokerLabel.setBounds(53, 198, 71, 108);
        stol.add(blackJokerLabel);

        redJokerLabel.setIcon(new ImageIcon(redJoker.getScaledInstance(70, 100, Image.SCALE_SMOOTH)));
        redJokerLabel.setBounds(543, 198, 71, 108);
        stol.add(redJokerLabel);

        getContentPane().add(new JScrollPane(zalogowani), BorderLayout.EAST);

        setResizable(false);

        stol.repaint();
        stol.revalidate();

        repaint();
        revalidate();

        setVisible(true);

        liczbaZwycieztw = 0;

        nastepneRozdanie.setEnabled(false);
        blackJack = new BlackJack(CzatClient.this);

        // Listeners
        polacz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                polacz.setEnabled(false);
                rozlacz.setEnabled(true);
                host.setEnabled(false);
                watekKlienta = new Klient();
                watekKlienta.start();
            }
        });
        rozlacz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        });
        dobierz.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (blackJack.graWToku()) {
                    blackJack.kolejnaKartaGracza();
                } else {
                    dobierz.setEnabled(false);
                }
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
        nastepneRozdanie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wlaczSterowanie();
                setDealerHand("\n\n\n\n");
                setClientHand("\n\n\n\n");
                blackJack.zacznijGre();
            }
        });
    }

    public void wylaczSterowanie () {
        dobierz.setEnabled(false);
        stoj.setEnabled(false);
        nastepneRozdanie.setEnabled(true);
    }
    public void wlaczSterowanie () {
        dobierz.setEnabled(true);
        stoj.setEnabled(true);
        nastepneRozdanie.setEnabled(false);
    }
    public void noweRozdanie () {
        kartyGracza.removeAll();
        kartyDealera.removeAll();
        revalidate();
        repaint();
    }
    public void dodajKarteGracz(File file) {
        BufferedImage kartaPNG = null;
        try {
            kartaPNG = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel kartaLABEL = new JLabel(new ImageIcon(kartaPNG.getScaledInstance(70, 100, Image.SCALE_SMOOTH)));
        kartaLABEL.setBounds(0,0,70,100);

        kartyGracza.add(kartaLABEL);

        revalidate();
        repaint();
    }
    public void dodajKarteDealera(File file) {
        BufferedImage kartaPNG = null;
        try {
            kartaPNG = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel kartaLABEL = new JLabel(new ImageIcon(kartaPNG.getScaledInstance(70, 100, Image.SCALE_SMOOTH)));
        kartaLABEL.setBounds(0,0,70,100);

        kartyDealera.add(kartaLABEL);

        revalidate();
        repaint();
    }
    public void wygranaPartia () {
        try {
            klient.dodajZwycieztwo(++liczbaZwycieztw);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private class Klient extends Thread {

        public void run() {
            try {
                Registry rejestr = LocateRegistry.getRegistry(host.getText());
                serwer = (Czat) rejestr.lookup("RMIBlackjack");
                String nick = "";

                while (nick.isEmpty() || nick.length() < 3 || nick.length() > 9) {
                    nick = JOptionPane.showInputDialog(null, "Podaj nick (3 do 9 znakow) : ").trim();
                }

                klient = new ClientImpl(serwer, instancjaKlienta, nick, liczbaZwycieztw);
                serwer.dolacz(klient);

                blackJack.zacznijGre();

            } catch (Exception e) {
                System.out.println("Błąd połączenia: " + e);
                wylaczSterowanie();
                nastepneRozdanie.setEnabled(false);
            }
        }
    }

    public void odswiezListe(Vector<Client> lista) {

        listaZalogowanych.clear();

        for (Client n : lista) {
            try {
                listaZalogowanych.addElement(n.pobierzNicka());
                System.out.println(n.pobierzNicka());
            } catch (Exception e) {
                System.out.println("Błąd: " + e);
                wylaczSterowanie();
                nastepneRozdanie.setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        new CzatClient();
    }
}