package Cliente;

import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class servidor {

    public static void main(String[] args) {
        MarcoServidor mimarco = new MarcoServidor();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

class MarcoServidor extends JFrame implements Runnable {

    public MarcoServidor() {
        setBounds(1200, 300, 280, 350);
        JPanel milamina = new JPanel();
        milamina.setLayout(new BorderLayout());
        areatexto = new JTextArea();
        milamina.add(areatexto, BorderLayout.CENTER);
        add(milamina);
        setVisible(true);
        Thread mihilo = new Thread(this);
        mihilo.start();

    }
    private JTextArea areatexto;

    @Override
    public void run() {
        try {
            //  System.out.println("Estoy a la escucha ");
            ServerSocket servidor = new ServerSocket(5555);

            String nick, ip, mensaje;

            PaqueteEnvio paquete_recibido;

            while (true) {
                Socket misocket = servidor.accept();
                ObjectInputStream paquete_datos = new ObjectInputStream(misocket.getInputStream());
                paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();
                nick = paquete_recibido.getNick();
                ip = paquete_recibido.getIp();
                mensaje = paquete_recibido.getMensaje();

//                DataInputStream flujo_entrada = new DataInputStream(misocket.getInputStream());
//
//                String mensaje_texto = flujo_entrada.readUTF();
//
//                areatexto.append("\n" + mensaje_texto);
                areatexto.append("\n" + nick + ": " + mensaje + " para " + ip);

                Socket enviaDestinatario = new Socket(ip, 9090);

                ObjectOutputStream paqueteRenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());

                paqueteRenvio.writeObject(paquete_recibido);

                enviaDestinatario.close();

                paqueteRenvio.close();
                misocket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MarcoServidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
