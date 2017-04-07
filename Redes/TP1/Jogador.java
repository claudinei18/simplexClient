import java.net.Socket;
import java.io.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;


class Jogador {
	// Variaveis
	private static BufferedReader leitor;
	private static Socket scktJogo;
	private static ObjectOutputStream saida;
	private static ObjectInputStream entrada;

	/**
	* Imprime o menu de opcoes do jogador
	*/
	public static void menu () {
		System.out.println( "MENU DE OPCOES");
		System.out.println("------------------------------");
		System.out.println("[1] - Conectar-se a um jogo." );
		System.out.println("[2] - Sair\n" );
		System.out.print("Opcao: ");
	}

	/**
	* Responsavel por enviar a mensagem ao servidor
	*
	* @param msg - Mensagem proveniente do usuario
	*/
	public static void enviarMsg( Object msg ) {
		try{
			saida.writeObject( msg );
			saida.flush();
		} catch ( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto enviava"+
				" uma mensagem ao servidor do jogo!");
			e.printStackTrace();
		}
	}

	/**
	* Responsavel por ler a mensagem enviada
	* do servidor
	*
	* @param msg - Mensagem proveniente do servidor
	* @return A String com o conteudo da mensagem
	*/
	public static String lerMsg() {
		String resp = "";
		try{
			resp = entrada.readObject().toString();
		} catch ( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto lia"+
				" uma mensagem do servidor do jogo!");
			e.printStackTrace();
			try{
				System.out.println("FINALIZANDO CONEXAO COM O JOGO ");
				scktJogo.close();
				System.exit(1);
			}catch (Exception ee){
				ee.printStackTrace();
			}
		}

		return resp;
	}

	/**
	* Realiza a conexao entre o servidor e o cliente
	* com o IP e a porta informados pelo usuário
	*/
	public static void conectar() {
		try{ 
			String ipServ;
			int portServ;

			System.out.print("Digite o IP do Servidor: ");
			ipServ = leitor.readLine();

			portServ = 8000;

			scktJogo = new Socket( ipServ, portServ );

			saida = new ObjectOutputStream( scktJogo.getOutputStream() );
			entrada = new ObjectInputStream( scktJogo.getInputStream() );

		} catch( Exception e ) {
			System.err.println("Ocorreu um erro enquanto o jogador" + 
				" se comunicava com o jogo!");
			e.printStackTrace();
		}

	}

	/**
	* Gerencia a parte do cliente durante o jogo
	*/
	public static void jogar() {
		String msg = lerMsg();
		boolean fim = false;

		try {
			// Enquanto o jogo nao chegou ao seu fim
			while( !msg.equals("F") ) {
				// Caso seja uma pergunta
				if( msg.equals("P") ) {
					msg = lerMsg();
					System.out.println( msg );

					String lin = leitor.readLine();
					enviarMsg( lin );

					msg = lerMsg();
					System.out.println( msg );
					
					String col = leitor.readLine();
					enviarMsg( col );
				}
				else { // Caso seja um aviso
					msg = lerMsg();
					System.out.println( msg );
				}

				msg = lerMsg();
			}

			scktJogo.close();

		} catch( Exception e ) {
			System.err.println( "Ocorreu um erro enquanto o jogador" +
				" processava a mensagem recebida pelo servidor!" );
			e.printStackTrace();
		}
	}

	/**
	* Main: Executa o programa Jogador
	*/
	public static void main( String[] args ) {
		// Caso haja algum erro, a operacao deve ser de saida
		int op = 2;

		leitor = new BufferedReader( new InputStreamReader( System.in ));

		do {
			menu();
			
			try {// Le a opcao desejada pelo usuario
				op = Integer.parseInt( leitor.readLine() );
			} catch( Exception e ) {
				System.err.println( "Ocorreu um erro enquanto o programa" +
				" processava a opcao escolhida pelo usuario!" );
				e.printStackTrace();
			}

			switch( op ) {
				case 1: // O usuario quer se conectar
					try {
						conectar();
						jogar();
						scktJogo.close();
					} catch ( Exception e ) {
						System.err.println("Ocorreu um erro enquanto" + 
							 " o jogo estava em andamento!" );
						e.printStackTrace();
					}

					break;
				case 2: // O usuario quer sair do programa
					break;
				default: // Houve um desentendimento
					break;
			}
		} while( op != 2 );
	}

}