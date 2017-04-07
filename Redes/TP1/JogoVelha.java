/****************************************
* Esta classe representa o tabuleiro de *
* um jogo da velha. O local selecionado *
* pelos jogadores são através de posico-*
* es relativas ao proprio.				*
*										*
* @author Claudinei Gomes				*
* @author Felipe Belem					*
****************************************/
import java.util.*;


class JogoVelha {
	// Constantes
	private static final int TAM = 3;
	private static final char VAZIO = '-';
	private static final char JOG_UM = 'X';
	private static final char JOG_DOIS = 'O';
	private static final int MAX_ROD = 9;
	private static final Scanner leitor = new Scanner( System.in );
	// Variaveis
	private char[][] tabuleiro;
	private int rodada;

	/**
	* Constutor da classe: Constroi
	* um tabuleiro 3x3 de Jogo da Velha
	* cujas posicoes estao nulas
	*/
	public JogoVelha() {
		tabuleiro = new char[TAM][TAM];

		rodada = 0;

		// Todo o tabuleiro está vazio
		for( int i = 0; i < TAM; i++ ) {
			for( int j = 0; j < TAM; j++ ) {
				tabuleiro[i][j] = VAZIO;
			}
		}
	}

	/**
	* Estabelece que a respectiva posicao (definida
	* pelos indices i e j) pertence, agora, ao jogador
	* cujo codigo esta em parametro
	*
	* @param lin - Linha da posicao selecionada
	* @param col - Coluna da posicao selecionada
	* @param cod_jog - Codigo do jogador que selecionou
	*					esta posicao.
	* @return TRUE: se a insercao foi bem sucedida
	*			FALSE: Caso contrário
	*/
	public boolean selPos ( int lin, int col, int cod_jog ) {
		if( lin < 0 || lin > TAM || col < 0 || col > TAM || tabuleiro[lin][col] != VAZIO ) {
			return false;
		} else {
			tabuleiro[lin][col] = ( cod_jog == 1 ) ? JOG_UM : JOG_DOIS;
			rodada++;
			return true;		
		}
	}

	/**
	* Apos ter selecionado uma posicao, verifica-se se
	* o jogador atual venceu o jogo
	*
	* @param lin - Linha da posicao selecionada
	* @param col - Coluna da posicao selecionada
	* @param cod_jog - Codigo do jogador que selecionou
	*					esta posicao
	* @return TRUE: Se voce formou uma linha com o seu codigo
	*			FALSE: Caso contrário
	*/
	public boolean voceVenceu ( int lin, int col, int cod_jog ) {
		char char_jog;
		
		char_jog = ( cod_jog == 1 ) ? JOG_UM : JOG_DOIS; 

		//Verificar a coluna
		int i;
		for( i = 0; i < TAM; i++ ) {
			if( tabuleiro[i][col] != char_jog ) { break;} 
		}

		//Caso todas pertencerem a este jogador, ele venceu
		if( i == 3 ) { return true; }

		//Verificar a linha
		for( i = 0; i < TAM; i++ ) {
			if( tabuleiro[lin][i] != char_jog ) { break;} 
		}

		//Caso todas pertencerem a este jogador, ele venceu
		if( i == 3 ) { return true; }

		//Verificar a diagonal primaria
		for( i = 0; i < TAM; i++ ) {
			if( tabuleiro[i][i] != char_jog ) { break;} 
		}

		//Caso todas pertencerem a este jogador, ele venceu
		if( i == 3 ) { return true; }

		//Verificar a diagonal secundaria
		for( i = 0; i < TAM; i++ ) {
			if( tabuleiro[i][TAM-i-1] != char_jog ) { break;} 
		}

		//Caso todas pertencerem a este jogador, ele venceu
		if( i == 3 ) { return true; }

		//Se chegou a este ponto, voce nao venceu
		return false;
	}

	/**
	* Verifica se o tabuleiro foi totalmente preenchido.
	* @return TRUE: Caso nao haja mais movimentos para se
	*				fazer
	*			FALSE: Caso contrário
	*/
	public boolean fimRod () {
		return rodada >= MAX_ROD;
	}	 

	/**
	* Converte o tabuleiro corrente em
	* uma unica String
	*
	* @return Uma unica String contendo
	*			as infos do tabuleiro
	*/
	public String toString () {
		String resp = "";

		resp += "Condição atual do tabuleiro:\n";
		for( int i = 0; i < TAM ; i++ ) {
			for( int j = 0; j < TAM ; j++ ) {
				resp += tabuleiro[i][j] + "\t";
			}
			resp += "\n";
		}

		return resp;
	}

	/**
	* Imprime o tabuleiro corrente
	*/
	public void imprTab () {
		System.out.println( "Condição atual do tabuleiro:");
		for( int i = 0; i < TAM ; i++ ) {
			for( int j = 0; j < TAM ; j++ ) {
				System.out.print( tabuleiro[i][j] + "\t");
			}
			System.out.println();
		}
	}
}