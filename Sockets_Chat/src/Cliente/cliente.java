package Cliente;

import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class cliente {

    public static void main(String[] args) {

        MarcoCliente mimarco = new MarcoCliente();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

class MarcoCliente extends JFrame {

    public MarcoCliente() {
        setBounds(600, 300, 280, 350);
        LaminaMarcoCliente milamina = new LaminaMarcoCliente();

        add(milamina);
        setVisible(true);
    }

}

class LaminaMarcoCliente extends JPanel implements Runnable {

    public LaminaMarcoCliente() {
        nick = new JTextField(5);

        add(nick);

        JLabel texto = new JLabel("CHAT");

        add(texto);

        ip = new JTextField(8);
        add(ip);
        campochat = new JTextArea(12, 20);

        add(campochat);

        campo1 = new JTextField(20);

        add(campo1);

        miboton = new JButton("Enviar");

        EnviaTexto miEvento = new EnviaTexto();

        miboton.addActionListener(miEvento);

        add(miboton);

        Thread mihilo = new Thread(this);

        mihilo.start();

    }

    @Override
    public void run() {
        try {
            ServerSocket servidorCliente = new ServerSocket(9090);

            Socket cliente;

            PaqueteEnvio paqueteRecibido;

            while (true) {
                cliente = servidorCliente.accept();

                ObjectInputStream flujoentrada = new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido = (PaqueteEnvio) flujoentrada.readObject();

                campochat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private class EnviaTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            campochat.append("\n" + campo1.getText());
            try {
                //(System.out.println(campo1.getText());
                Socket misocket = new Socket("192.168.113.41", 5555);
                PaqueteEnvio datos = new PaqueteEnvio();

                datos.setNick(nick.getText());
                datos.setIp(ip.getText());
                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());

                paquete_datos.writeObject(datos);

                misocket.close();
//                DataOutputStream flujo_Salida = new DataOutputStream(misocket.getOutputStream());
//
//                flujo_Salida.writeUTF(campo1.getText());
//
//                flujo_Salida.close();
//
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
    private JTextField campo1, nick, ip;
    private JTextArea campochat;
    private JButton miboton;
}

class PaqueteEnvio implements Serializable {

    private String nick, ip, mensaje;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

}
