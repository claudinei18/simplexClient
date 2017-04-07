import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

class Executavel {
    private static class Port {
        public int port;
    }

    // Servidores das conexoes com os jogadores
    private static ServerSocket servScktJog;
    // Jogadores
    private static Socket jog1;
    private static Socket jog2;
    // Entrada de dados dos jogadores
    private static ObjectInputStream inputJog1;
    private static ObjectInputStream inputJog2;
    // Saida de dados dos jogadores
    private static ObjectOutputStream outJog1;
    private static ObjectOutputStream outJog2;
    private static long id = 0;

//    private static ArrayList<ServerHelper> list = new ArrayList<ServerHelper>();


    public static void finalizarConexaoComJogadores(){
        try{
            jog1.close();
            jog2.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Le uma mensagem proveniente do jogador 1
     *
     * @return Conteúdo da mensagem no formato
     * de String.
     */
    public static String lerMsgJog1() {
        String resp = "";
        try {
            resp = inputJog1.readObject().toString();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro enquanto lia" +
                    " uma mensagem do jogador 1!");
            e.printStackTrace();
            finalizarConexaoComJogadores();
        }

        return resp;
    }

    /**
     * Le uma mensagem proveniente do jogador 2
     *
     * @return Conteúdo da mensagem no formato
     * de String.
     */
    public static String lerMsgJog2() {
        String resp = "";
        try {
            resp = inputJog2.readObject().toString();
        } catch (Exception e) {
            System.err.println("Ocorreu um erro enquanto lia" +
                    " uma mensagem do jogador 2!");
            e.printStackTrace();
            finalizarConexaoComJogadores();
        }

        return resp;
    }

    /**
     * Envia uma mensagem para o jogador 1
     *
     * @param msg - Mensagem enviada
     */
    public static void enviarMsgJog1( Object msg ) {
        try{
            outJog1.writeObject( msg );
            outJog1.flush();
        } catch ( Exception e ) {
            System.err.println( "Ocorreu um erro enquanto enviava"+
                    " uma mensagem ao jogador 1!");
            e.printStackTrace();
        }
    }

    /**
     * Envia uma mensagem para o jogador 2
     *
     * @param msg - Mensagem enviada
     */
    public static void enviarMsgJog2( Object msg ) {
        try{
            outJog2.writeObject( msg );
            outJog2.flush();
        } catch ( Exception e ) {
            System.err.println( "Ocorreu um erro enquanto enviava"+
                    " uma mensagem ao jogador 2!");
            e.printStackTrace();
        }
    }


    public static void conectar(){


        try{
            ServerSocket server = new ServerSocket(8000);
            while(true){
                System.out.println("Thread Running");
                Socket client1 = server.accept();
                Socket client2 = server.accept();

                Thread thread = new Thread(){
                    public void run(){
                        try{
                            SalaJogo sala = new SalaJogo(client1, client2, ++id);
                            sala.run();
                        }catch(Exception ee){
                            ee.printStackTrace();
                        }finally {
                            --id;
                        }
                    }
                };

                thread.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        while(true){
//            Socket client1 = server.accept();
//            ServerHelper helper1 = new ServerHelper(server, client1);
//            helper1.start();
//
//            list.add(helper1);
//
//            Socket client2 = server.accept();
//            ServerHelper helper2 = new ServerHelper(server, client2);
//            helper2.start();
//
//            list.add(helper2);
//
//            helper2.startGame();
//
//        }



        /*final Port port = new Port();
        port.port = 6999;

        try{
            // Aguarda o estabelecimento da conexao dos jogadores
            servScktJog = new ServerSocket( 6999 ); //porta 6999 para escutar
        }catch (Exception e ){
            e.printStackTrace();
        }

        while (true) {
            try {
                jog1 = servScktJog.accept();

                String portJog1 = "" + ++port.port;
                String portJog2 = "" + ++port.port;
                Thread thread = new Thread(){
                    public void run(){
                        System.out.println("Thread Running");
                        SalaJogo sj = new SalaJogo( Integer.parseInt(portJog1), Integer.parseInt(portJog2) );
                        sj.run();
                    }
                };

                thread.start();

                inputJog1 = new ObjectInputStream(jog1.getInputStream());
                outJog1 = new ObjectOutputStream(jog1.getOutputStream());

                String msgnJog1 = lerMsgJog1();
                if(msgnJog1.equals("QUAL_PORTA")){
                    enviarMsgJog1( portJog1 );
                }

                jog2 = servScktJog.accept();
                inputJog2 = new ObjectInputStream(jog2.getInputStream());
                outJog2 = new ObjectOutputStream(jog2.getOutputStream());

                String msgnJog2 = lerMsgJog2();
                if(msgnJog2.equals("QUAL_PORTA")){
                    enviarMsgJog2( portJog2 );
                }

                jog1.close();
                jog2.close();

            } catch (Exception e) {
                try {
                    servScktJog.close();
                }catch (Exception ee){
                    e.printStackTrace();
                }
                System.err.println("Ocorreu um erro enquanto realizava" +
                        " as conexoes com os jogadores!");
                e.printStackTrace();
                System.exit(1);
            }
        }*/
    }
    public static void main(String... args) {
        conectar();
    }
}

//class ServerHelper extends Thread {
//    ServerSocket server;
//    Socket client;
//    BufferedReader br;
//    PrintWriter pw;
//
//    public ServerHelper(ServerSocket server, Socket client){
//        this.server = server;
//        this.client = client;
//    }
//
//    public void receiveMessage(){
//        String s;
//        while(true){
//            s = br.readLine();
//        }
//    }
//
//    public void sendMessageToClient(String s){
//        client.writeObject(s);
//    }
//
//    public void startGame(){
//
//    }
//
//    public void run(){
//        sendMessageToClient("JOGUE");
//    }
//
//}