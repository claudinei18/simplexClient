/****************************************
* Esta classe representa uma sala de
* jogo entre dois jogadores. No inicio,
* sera solicitado o IP do jogador. Estando
* ambos, conectados, é iniciada a partida
* de jogo da velha até que um dos jogadores
* saia da sala. 	
*										
* @author Claudinei Gomes				
* @author Felipe Belem					
****************************************/
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

// Esta classe representa o servidor entre
// dois clientes
class SalaJogo {
	// Constantes
	private final String VOCE_PERD = "\nQue pena!!! Voce perdeu o jogo!\n";
	private final String VOCE_VENC = "\nPARABENS!!! Voce Venceu o jogo!\n";
	private final String DEU_VELHA = "\nQue azar!!! Deu velha!\n";
	private final String PERG_COLUNA = "Selecione a coluna: ";
	private final String PERG_LINHA = "Selecione a linha: ";
	private final String ESPERA = "Aguarde, a rodada e de seu oponente...";
	private final String FIM_JOGO = "F";
	private final String PERGUNTA = "P";
	private final String AVISO = "A";
	private final String NAO_PODE = "O local selecionado e invalido, por favor, "+
									"selecione outro!";
	private final long id;
	// Variaveis
	private JogoVelha jogo;
	// Jogadores
	private Socket jog1;
	private Socket jog2;
	// Entrada de dados dos jogadores
	private ObjectInputStream inputJog1;
	private ObjectInputStream inputJog2;
	// Saida de dados dos jogadores
	private ObjectOutputStream outJog1;
	private ObjectOutputStream outJog2;

	/**
	* Construtor: Estabelece quais sao as portas
	* que realizarao a conexao com os jogadores
	*
	* @param portJ1 - Porta do jogador 1
	* @param portJ2 - Porta do jogador 2
	*/
	public SalaJogo ( Socket jog1, Socket jog2, long id) {
		// Define as portas para cada jogador
		this.jog1 = jog1;
		this.jog2 = jog2;
		this.id = id;
	}

	/**
	* Envia uma mensagem para o jogador 1
	* 
	* @param msg - Mensagem enviada
	*/
	public void enviarMsgJog1( Object msg ) {
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
	public void enviarMsgJog2( Object msg ) {
		try{
			outJog2.writeObject( msg );
			outJog2.flush();
		} catch ( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto enviava"+
				" uma mensagem ao jogador 2!");
			e.printStackTrace();
		}
	}

	/**
	* Le uma mensagem proveniente do jogador 1
	* 
	* @return Conteúdo da mensagem no formato
	*			de String.
	*/
	public String lerMsgJog1() {
		String resp = "";
		try{
			resp = inputJog1.readObject().toString();
		} catch ( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto lia"+
				" uma mensagem do jogador 1!");
			e.printStackTrace();
		}

		return resp;
	}
	
	/**
	* Le uma mensagem proveniente do jogador 2
	* 
	* @return Conteúdo da mensagem no formato
	*			de String.
	*/
	public String lerMsgJog2() {
		String resp = "";
		try {
			resp = inputJog2.readObject().toString();
		} catch ( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto lia"+
				" uma mensagem do jogador 2!");
			e.printStackTrace();
		}

		return resp;
	}

	/**
	* Conecta este jogo com os jogadores que se desejam
	* enfrentar
	*/
	private void conectar() {
		try {
			// Aguarda o estabelecimento da conexao dos jogadores
			inputJog1 = new ObjectInputStream(jog1.getInputStream());
			outJog1 = new ObjectOutputStream(jog1.getOutputStream());


			inputJog2 = new ObjectInputStream(jog2.getInputStream());
			outJog2 = new ObjectOutputStream(jog2.getOutputStream());

			System.out.println("ENVIANDO MENSAGEM");

			enviarMsgJog1( AVISO );
			enviarMsgJog1( NAO_PODE );

		} catch ( Exception e ) {
			System.err.println("Ocorreu um erro enquanto realizava" +
				" as conexoes com os jogadores!" );
			e.printStackTrace();

		}
	}

	/**
	* Inicia o jogo, realizando as conexoes
	* entre os jogadores e gerenciando as rodadas
	*/
	public void run() {
		try {
			jogo = new JogoVelha();
			boolean fim = false;

			conectar();

			while( fim == false ) {
				boolean posValida = true;
				int linJog1, linJog2, colJog1, colJog2;
				
				do{
					if(posValida == false){
						enviarMsgJog1( AVISO );
						enviarMsgJog1( NAO_PODE );
					}
					enviarMsgJog1( PERGUNTA );
					enviarMsgJog1( jogo.toString() + PERG_LINHA );
					linJog1 = Integer.parseInt( lerMsgJog1() );


					enviarMsgJog1( PERG_COLUNA );
					colJog1 = Integer.parseInt( lerMsgJog1() );
					
					posValida = jogo.selPos(linJog1, colJog1, 1 );
				}while(posValida == false);
	

				if( jogo.voceVenceu( linJog1, colJog1, 1) ) {
					enviarMsgJog1( AVISO );
					enviarMsgJog1( VOCE_VENC );
					enviarMsgJog2( AVISO );
					enviarMsgJog2( VOCE_PERD );
					fim = true;
				}
				else if( jogo.fimRod() ) {
					enviarMsgJog1( AVISO );
					enviarMsgJog1( DEU_VELHA );
					enviarMsgJog2( AVISO );
					enviarMsgJog2( DEU_VELHA );
					fim = true;
				}
				else {
					enviarMsgJog1( AVISO );
					enviarMsgJog1( ESPERA );

					posValida = true;

					do{
						if(posValida == false){
							enviarMsgJog2( AVISO );
							enviarMsgJog2( NAO_PODE );
						}
						enviarMsgJog2( PERGUNTA );
						enviarMsgJog2( jogo.toString() + PERG_LINHA );
						linJog2 = Integer.parseInt( lerMsgJog2() );

						enviarMsgJog2( PERG_COLUNA );
						colJog2 = Integer.parseInt( lerMsgJog2() );
						
						posValida = jogo.selPos(linJog2, colJog2, 2 );
					}while(posValida == false);
					
					if( jogo.voceVenceu( linJog2, colJog2, 2) ) {
						enviarMsgJog2( AVISO );
						enviarMsgJog2( VOCE_VENC );
						enviarMsgJog1( AVISO );
						enviarMsgJog1( VOCE_PERD );
						fim = true;
					}
					else { 
						enviarMsgJog2( AVISO );
						enviarMsgJog2( ESPERA ); 
					}
				}
			}

			enviarMsgJog1( FIM_JOGO );
			enviarMsgJog2( FIM_JOGO );

			jog1.close();
			jog2.close();


		} catch ( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto realizava" +
				" a conexao com os jogadores!" );
			e.printStackTrace();
			try{
				jog1.close();
				jog2.close();
			}catch (Exception ee){
				ee.printStackTrace();
			}
		}
	}
}