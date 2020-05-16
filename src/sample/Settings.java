package sample;

import javax.swing.*;
import java.awt.event.*;                                                        //libreria degli eventi
import java.awt.*;                                                              //libreria awt

/**
 * Created by andrealissak on 02/04/18.
 */
public class Settings extends JFrame implements ActionListener                     //la classe cassa è sottoclasse di jframe che implementa actionlistener
{
    private JPanel f=new JPanel(new GridBagLayout());                           //nuovo pannello della finestra di default con il grid bag layout

    private JTextField nEuro=new JTextField(10);
    private JTextField prezzo=new JTextField(10);
    private JTextField nome=new JTextField(10);
    private JTextField descrizione=new JTextField(10);
    private JTextField iva=new JTextField(10);                                          //textfield del programma
    private JTextArea resto=new JTextArea(25,1);                                        //textarea del risultato
    private JButton accetta=new JButton("accetta pagamento e dai resto");               //unico bottone

    private JButton dx=new JButton("<");               //unico bottone
    private JButton sx=new JButton(">");               //unico bottone
    private JButton su=new JButton("˅");               //unico bottone
    private JButton gi=new JButton("˄");               //unico bottone

    private Choice n50=new Choice();
    private Choice n20=new Choice();
    private Choice n10=new Choice();
    private Choice n5=new Choice();
    private Choice n2=new Choice();
    private Choice n1=new Choice();                                                     //combo box per il numero di monete di ogni tipo

    private Label lNome=new Label("nome:",Label.LEFT);
    private Label lPrezzo=new Label("PREZZO:",Label.RIGHT);
    private Label lDescrizione=new Label("desc:",Label.LEFT);
    private Label lIva=new Label("% IVA:",Label.LEFT);
    private Label lEuro=new Label("n° €:",Label.LEFT);
    private Label l50=new Label("n° 50c:",Label.LEFT);
    private Label l20=new Label("n° 20c:",Label.LEFT);
    private Label l10=new Label("n° 10c:",Label.LEFT);
    private Label l5=new Label("n° 5c:",Label.LEFT);
    private Label l2=new Label("n° 2c:",Label.LEFT);
    private Label l1=new Label("n° 1c:",Label.LEFT);
    private Label lVuota=new Label("",Label.LEFT);
    private Label lVuota1=new Label("",Label.LEFT);
    private Label titolo=new Label("CASSA",Label.LEFT);                                 //varie label


    private double importoCliente;
    private double restoCliente;
    private double ivaCliente;
    private double totale;
    private double perc;
    private int i;                                                              //variabili di classe per il "vero programma"
    private UsingGraphics2D simulation;

    private static volatile boolean wPressed = false;
    private static volatile boolean dPressed = false;
    private static volatile boolean sPressed = false;
    private static volatile boolean aPressed = false;
    public static boolean isWPressed() {
        synchronized (Settings.class) {
            return wPressed;
        }
    }
    public static boolean isDPressed() {
        synchronized (Settings.class) {
            return dPressed;
        }
    }
    public static boolean isSPressed() {
        synchronized (Settings.class) {
            return sPressed;
        }
    }
    public static boolean isAPressed() {
        synchronized (Settings.class) {
            return aPressed;
        }
    }

    public Settings(UsingGraphics2D simulation)                                                              //costruttore
    {
        this.simulation=simulation;
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (Settings.class) {
                    System.out.println("either rel or press: "+ke.getID());
                    char key=ke.getKeyChar();
                    System.out.println("either rel or press: "+key);
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            switch (key)
                            {
                                case 'w':simulation.moveCameraDown();break;
                                case 'd':simulation.moveCameraLeft();break;
                                case 's':simulation.moveCameraUp();break;
                                case 'a':simulation.moveCameraRight();break;
                                default:System.out.println("other key: "+key);break;
                            }


							/*if (ke.getKeyCode() == KeyEvent.VK_W) {
								wPressed=true;
							}
							else
							{
								if (ke.getKeyCode() == KeyEvent.VK_D) {
									dPressed=true;
								}
								else
								{
									if (ke.getKeyCode() == KeyEvent.VK_S) {
										sPressed=true;
									}
									else
									{
										if (ke.getKeyCode() == KeyEvent.VK_A) {
											aPressed=true;
										}
									}
								}
							}*/


                            break;

                        case KeyEvent.KEY_RELEASED:
                            System.out.println("release: "+ke.getKeyChar());


                            switch (key)
                            {
                                case 'w':wPressed=false;break;
                                case 'd':dPressed=false;break;
                                case 's':sPressed=false;break;
                                case 'a':aPressed=false;break;
                                default:System.out.println("other key rel: "+key);break;
                            }


							/*
							if (ke.getKeyCode() == KeyEvent.VK_W) {
								wPressed = false;
							}
							else
							{
								if (ke.getKeyCode() == KeyEvent.VK_D) {
									dPressed = false;
								}
								else
								{
									if (ke.getKeyCode() == KeyEvent.VK_S) {
										sPressed = false;
									}
									else
									{
										if (ke.getKeyCode() == KeyEvent.VK_A) {
											aPressed = false;
										}
									}
								}
							}*/

                            break;
                    }
                    return false;
                }
            }
        });
        n50.addItem("0");n50.addItem("1");n50.addItem("2");n50.addItem("3");n50.addItem("4");n50.addItem("5");n50.addItem("6");n50.addItem("7");n50.addItem("8");n50.addItem("9");n50.addItem("10");
        n20.addItem("0");n20.addItem("1");n20.addItem("2");n20.addItem("3");n20.addItem("4");n20.addItem("5");n20.addItem("6");n20.addItem("7");n20.addItem("8");n20.addItem("9");n20.addItem("10");
        n10.addItem("0");n10.addItem("1");n10.addItem("2");n10.addItem("3");n10.addItem("4");n10.addItem("5");n10.addItem("6");n10.addItem("7");n10.addItem("8");n10.addItem("9");n10.addItem("10");
        n5.addItem("0");n5.addItem("1");n5.addItem("2");n5.addItem("3");n5.addItem("4");n5.addItem("5");n5.addItem("6");n5.addItem("7");n5.addItem("8");n5.addItem("9");n5.addItem("10");
        n2.addItem("0");n2.addItem("1");n2.addItem("2");n2.addItem("3");n2.addItem("4");n2.addItem("5");n2.addItem("6");n2.addItem("7");n2.addItem("8");n2.addItem("9");n2.addItem("10");
        n1.addItem("0");n1.addItem("1");n1.addItem("2");n1.addItem("3");n1.addItem("4");n1.addItem("5");n1.addItem("6");n1.addItem("7");n1.addItem("8");n1.addItem("9");n1.addItem("10");
        //massimo di 10 monete per tipo
        lNome.setForeground(Color.white);
        lPrezzo.setForeground(Color.white);
        lDescrizione.setForeground(Color.white);
        lIva.setForeground(Color.white);
        lEuro.setForeground(Color.white);
        l50.setForeground(Color.white);
        l20.setForeground(Color.white);
        l10.setForeground(Color.white);
        l5.setForeground(Color.white);
        l2.setForeground(Color.white);
        l1.setForeground(Color.white);
        titolo.setForeground(Color.green);                                      //label bianche

        f.setLocation(700,100);                                                 //posizione
        f.setBackground(Color.blue);                                            //sfondo blu

        accetta.addActionListener(this);                                        //metodo che aggiunge il bottone
        dx.addActionListener(this);
        sx.addActionListener(this);
        su.addActionListener(this);
        gi.addActionListener(this);

        f.setLayout(new GridBagLayout());                                       //gridbaglayout
        GridBagConstraints c = new GridBagConstraints();

        //c.gridheight = 1;
        //c.gridwidth = 1;
        //c.ipady = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;                                                            //pos x (parte da zero)
        c.gridy = 0;                                                            //pos y (parte da zero)
        f.add(titolo,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        f.add(lNome,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        f.add(nome,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 1;
        f.add(gi,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        //c.gridwidth = 2;
        c.gridx = 3;
        c.gridy = 1;
        f.add(descrizione,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        f.add(lIva,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 1;
        f.add(iva,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        f.add(lEuro,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        f.add(dx,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 2;
        f.add(sx,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;
        c.gridx = 5;
        c.gridy = 2;
        f.add(prezzo,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        f.add(l50,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        f.add(n50,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 3;
        f.add(su,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 3;
        f.add(n20,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 3;
        f.add(l10,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 3;
        f.add(n10,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 4;
        f.add(l5,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 4;
        f.add(n5,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 4;
        f.add(l2,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 4;
        f.add(n2,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 4;
        f.add(l1,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 5;
        c.gridy = 4;
        f.add(n1,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 5;
        f.add(lVuota,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;
        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 6;
        f.add(accetta,c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 6;
        c.gridx = 0;


        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 6;
        c.gridx = 0;
        c.gridy = 8;
        f.add(resto,c);

        this.getContentPane().add(f, BorderLayout.CENTER);                      //imposta il pannello nella zona centrale
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                    //prepara la finestra a essere chiusa

        importoCliente=0;
        restoCliente=0;
        ivaCliente=0;
        totale=0;
        perc=0;
        i=0;
    }

    public void riceviPagamento()
    {
        resto.setText("");

        resto.append("_____________ _____ ____ ___ __ _\n");
        resto.append("nome:\t"+nome.getText()+"\n");
        resto.append("descrizione:\t"+descrizione.getText()+"\n");
        resto.append("iva:\t"+iva.getText()+"\n");
        resto.append("€:\t"+nEuro.getText()+"\n");
        resto.append("n50:\t"+n50.getSelectedItem()+"\n");
        resto.append("n20:\t"+n20.getSelectedItem()+"\n");
        resto.append("n10:\t"+n10.getSelectedItem()+"\n");
        resto.append("n5:\t"+n5.getSelectedItem()+"\n");
        resto.append("n2:\t"+n2.getSelectedItem()+"\n");
        resto.append("n1:\t"+n1.getSelectedItem()+"\n\n");
        resto.append("¯¯¯¯¯¯¯¯¯¯¯¯¯ ¯¯¯¯¯ ¯¯¯¯ ¯¯¯ ¯¯ ¯\n");                    //riporta i dati inseriti

        double valMon50=(double)Integer.parseInt(n50.getSelectedItem(), 10)/2;
        double valMon20=(double)Integer.parseInt(n20.getSelectedItem(), 10)/5;
        double valMon10=(double)Integer.parseInt(n10.getSelectedItem(), 10)/10;
        double valMon5=(double)Integer.parseInt(n5.getSelectedItem(), 10)/20;
        double valMon2=(double)Integer.parseInt(n2.getSelectedItem(), 10)/50;
        double valMon1=(double)Integer.parseInt(n1.getSelectedItem(), 10)/100;  //calcola il valore delle monete dopo aver assegnato agli elementi della combo il valore espresso attraverso le stringhe che rappresentano i numeri

        importoCliente=Integer.parseInt(nEuro.getText(), 10)+valMon50+valMon20+valMon10+valMon5+valMon2+valMon1;//somma tutte le monete e gli €
        importoCliente=Math.floor(importoCliente * 100)/100;                    //2 cifre dopo la virgola
        resto.append("IMPORTO: "+importoCliente);

        restoCliente=importoCliente-Double.parseDouble(prezzo.getText());       //calcola il resto
        restoCliente=Math.floor(restoCliente * 100)/100;
        resto.append("\t\t\tRESTO: "+restoCliente);

        ivaCliente=(Double.parseDouble(prezzo.getText()))*((double)Integer.parseInt(iva.getText(), 10))/100;//calcola iva
        ivaCliente=Math.floor(ivaCliente * 100)/100;
        double senzaIva=Double.parseDouble(prezzo.getText())-ivaCliente;        //calcola prezzo senza iva
        senzaIva=Math.floor(senzaIva * 100)/100;
        resto.append("\nIVA: "+ivaCliente+"   prezzo senza IVA: "+senzaIva);

        totale=totale+Double.parseDouble(prezzo.getText());                     //calcola la somma dei prodotti
        double roundTotale=Math.floor(totale * 100)/100;
        resto.append("\tTOTALE: "+roundTotale);
        perc=100*Double.parseDouble(prezzo.getText())/totale;                   //percentuale del prodotto acquistato rispetto alla somma di tutti i prodotti precedenti
        perc=Math.floor(perc * 100)/100;
        resto.append("\n% sul TOT: "+perc+"%");

        resto.append("\n¯¯¯¯¯¯¯¯¯¯¯¯¯ ¯¯¯¯¯ ¯¯¯¯ ¯¯¯ ¯¯ ¯\n\n");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String bottone=e.getActionCommand();                                    //alla variabile bottone è assegnato il nome che è stato dato al bottone premuto
        if(bottone.equals("accetta pagamento e dai resto"))                     //se il bottone è schiacciato allora esegue le istruzioni
        {
            try
            {
                simulation.moveCameraRight();
                riceviPagamento();                                              //esegue il metodo soprastante
            }
            catch(Exception exc){}
        }
        if(bottone.equals("˅"))
        {
            try
            {
                simulation.moveCameraUp();
            }
            catch(Exception exc){}
        }
        if(bottone.equals("˄"))
        {
            try
            {
                simulation.moveCameraDown();
            }
            catch(Exception exc){}
        }
        if(bottone.equals("<"))
        {
            try
            {
                simulation.moveCameraRight();
            }
            catch(Exception exc){}
        }
        if(bottone.equals(">"))
        {
            try
            {
                simulation.moveCameraLeft();
            }
            catch(Exception exc){}
        }
    }
}
