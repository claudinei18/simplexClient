import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.DoubleSummaryStatistics;

/**
 * Created by claudinei on 18/03/17.
 */
public class main {
    public static void main(String[] args) {
        try {
            Simplex s = new Simplex();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Simplex {
    private Matriz matriz;


    public Simplex() throws IOException {
        BufferedReader inReader;
        inReader = new BufferedReader(new InputStreamReader(System.in));
        int numLin = 0, numCol = 0;

        try {
            String nVar = inReader.readLine();
            System.out.println(nVar);
            String nRes = inReader.readLine();
            int numRes = Integer.parseInt(nRes);
            numLin = 2 + numRes;
            int numVar = Integer.parseInt(nVar);
            numCol = 2 + numVar;
            matriz = new Matriz(numLin, numCol);
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
2
2
vn\vnb  ml      x       y
f(x)    0       -7      -8.5
a       -16     -0.6    -0.8
b       1800    24      20
*/


        for (int l = 0; l < numLin; l++) {
            String linha = inReader.readLine();
            String[] linhaAux = linha.split(" ");
            for (int c = 0; c < numCol; c++) {
                if (l == 0) {
                    if (c == 0) {
                        matriz.setNo(l, c, new No("vn/vnb", 0, 0));

                    } else {
                        matriz.setNo(l, c, new No(linhaAux[c], 0, 0));
                    }

                } else {
                    if (c == 0) {
                        matriz.setNo(l, c, new No(linhaAux[c], 0, 0));
                    } else {
                        matriz.setNo(l, c, new No("", Double.parseDouble(linhaAux[c]), 0));
                    }
                }

            }
        }

        matriz.print();
        executarSimplex();
//        System.out.println(getElemPerm().getNo().getSup());

    }

    private void executarSimplex() {
        while (condParada() == false) {
            ElemPerm elemPerm = getElemPerm();
            if(elemPerm == null){
                System.out.println("SOLUÇÃO PERMISSÍVEL NÃO EXISTE");
            }else{
                algoritmoTroca(elemPerm);
                matriz.print();
            }
        }
    }

    private void algoritmoTroca(ElemPerm elemPerm){
        //Calculando e setando novo valor do inverso do elemento permissivo
        double invrsOfElemPerm = (1 / elemPerm.getNo().getSup());
        No novoNo = new No("", elemPerm.getNo().getSup(), invrsOfElemPerm);
        matriz.setNo(elemPerm.getLin(), elemPerm.getCol(), novoNo);


        //Multiplicando linha pelo inverso do elemento permissivo
        for(int col = 1; col < matriz.getNumCol(); col++){
            if(col != elemPerm.getCol()){
                double sup = matriz.getNo(elemPerm.getLin(), col).getSup();
                double mult = sup * invrsOfElemPerm;
                matriz.setNo(elemPerm.getLin(), col, new No("", sup, mult));
            }
        }

        //Multiplicando coluna pelo menos inverso do elemento permissivo
        for(int lin = 1; lin < matriz.getNumLin(); lin++){
            if(lin != elemPerm.getLin()){
                double sup = matriz.getNo(lin, elemPerm.getCol()).getSup();
                double mult = sup * ( (-1) * invrsOfElemPerm);
                matriz.setNo(lin, elemPerm.getCol(), new No("", sup, mult));
            }
        }

        montarQuadrado(elemPerm);

        //inverte o nome das variaveis
        No noAux = matriz.getNo(0, elemPerm.getCol());
        matriz.setNo(0, elemPerm.getCol(), new No(matriz.getVar(elemPerm.getLin(), 0), 0, 0));
        matriz.setNo(elemPerm.getLin(), 0, noAux);

        matriz.print();

        //SOBE se LINHA ou COLUNA do ELEMENTO PERMISSVO
        //o resto SOMA

        for(int lin = 1; lin < matriz.getNumLin(); lin++){
            for(int col = 0; col < matriz.getNumCol(); col++){
                if( lin == elemPerm.getLin() || col == elemPerm.getCol()){
                    matriz.getNo(lin, col).setSup( matriz.getInf(lin, col) ); // INF vai para SUP
                    matriz.getNo(lin, col).setInf( 0 ); // ZERA inf

                }else{
                    double sup = matriz.getSup(lin, col);
                    double inf = matriz.getInf(lin, col);
                    double soma = sup + inf;
                    matriz.getNo(lin, col).setSup(soma);
                    matriz.getNo(lin, col).setInf(0);
                }
            }
        }



    }

    private void montarQuadrado(ElemPerm elemPerm){
        System.out.println(elemPerm.getLin() + " " + elemPerm.getCol());
        for(int lin = 1; lin < matriz.getNumLin(); lin++){
            for(int col = 1; col < matriz.getNumCol(); col++){
                if( lin == elemPerm.getLin() ||
                    col == elemPerm.getCol()){

                }else{
                    double supParaMult = matriz.getSup(elemPerm.getLin(), col);
                    double infParaMult = matriz.getInf(lin, elemPerm.getCol());
                    double mult = supParaMult * infParaMult;

                    matriz.getNo(lin, col).setInf(mult);
                }
            }
        }
    }


    private ElemPerm getElemPerm() {
        ElemPerm elemPerm = null;
        //ACHAR LINHA
        for (int lin = 2; lin < matriz.getNumLin(); lin++) {
            if (matriz.getSup(lin, 1) < 0) {
                //ACHAR COLUNA
                for (int col = 2; col < matriz.getNumCol(); col++) {
                    if (matriz.getSup(lin, col) < 0) {
                        //PROCURAR MENOR QUOCIENTE DE ML E VNB
                        double ml = matriz.getSup(lin, 1);
                        double elmt = matriz.getSup(lin, col);
                        double menor = Double.POSITIVE_INFINITY;
                        if (ml * elmt > 0) {
                            menor = ml / elmt;
                            elemPerm = new ElemPerm(lin, col, matriz.getNo(lin, col));
                        }
                        for (int linProc = 3; linProc < matriz.getNumLin(); linProc++) {
                            ml = matriz.getSup(linProc, 1);
                            elmt = matriz.getSup(linProc, col);
                            if (ml * elmt > 0) {
                                double menorLocal = (ml / elmt);
                                if(menorLocal < menor){
                                    menor = menorLocal;
                                    elemPerm = new ElemPerm(lin, col, matriz.getNo(lin, col));
                                }
                            }
                        }
                        return elemPerm;
                    }
                }
            }
            if(lin == matriz.getNumLin() - 1){
                for(int col = 2; col < matriz.getNumCol(); col++){
                    if(matriz.getSup(1, col) > 0){
                        //PROCURAR MENOR QUOCIENTE DE ML E VNB
                        double ml = matriz.getSup(2, 1);
                        double elmt = matriz.getSup(2, col);
                        double menor = Double.POSITIVE_INFINITY;
                        if (ml * elmt > 0) {
                            menor = ml / elmt;
                            elemPerm = new ElemPerm(2, col, matriz.getNo(2, col));
                        }
                        for (int linProc = 3; linProc < matriz.getNumLin(); linProc++) {
                            ml = matriz.getSup(linProc, 1);
                            elmt = matriz.getSup(linProc, col);
                            if (ml * elmt > 0) {
                                double menorLocal = (ml / elmt);
                                if(menorLocal < menor){
                                    menor = menorLocal;
                                    elemPerm = new ElemPerm(linProc, col, matriz.getNo(linProc, col));
                                }
                            }
                        }
                        return elemPerm;
                    }
                }
            }
        }


        return elemPerm;
    }

    private boolean condParada() {
        boolean resp = false;

        /*
          Solução impossível
         */
        for (int lin = 2; lin < matriz.getNumLin(); lin++) {
            if (matriz.getSup(lin, 1) < 0) {
                for (int col = 2; col < matriz.getNumCol(); col++) {
                    if (matriz.getSup(lin, col) <= 0) {
                        resp = false;
                        break;
                    }
                    if (col == matriz.getNumCol() - 1) {
                        System.out.println("SOLUÇÃO IMPOSSIVEL");
                        return true;
                    }
                }
            }
        }

        /*
        Solução Ótima
         */
        int col = 1;
        for (int lin = 2; lin < matriz.getNumLin(); lin++) {
            if (matriz.getSup(lin, col) < 0) {
                resp = false;
                break;
            }
            if (lin == matriz.getNumLin() - 1) {
                for (col = 2; col < matriz.getNumCol(); col++) {
                    if (matriz.getSup(1, col) >= 0) {
                        resp = false;
                        break;
                    }
                    if (col == matriz.getNumCol() - 1) {
                        System.out.println("SOLUÇÃO OTIMA");
                        return true;
                    }
                }
            }
        }

        /*
        Múltiplas soluções
         */
        col = 1;
        for (int lin = 2; lin < matriz.getNumLin(); lin++) {
            if (matriz.getSup(lin, col) < 0) {
                resp = false;
                break;
            }
            if (lin == matriz.getNumLin() - 1) {
                for (col = 2; col < matriz.getNumCol(); col++) {
                    if (matriz.getSup(1, col) == 0) {
                        for (int colAux = 2; colAux < matriz.getNumCol(); colAux++) {
                            if (colAux != col && matriz.getSup(1, colAux) > 0) {
                                resp = false;
                                break;
                            } else if (colAux == matriz.getNumCol() - 1) {
                                System.out.println("MULTIPLAS SOLUCOES");
                                resp = true;
                                return resp;
                            }
                        }
                    }
                }
            }
        }

        /*
        Solução Ilimitada
        */
        col = 1;
        for (int lin = 2; lin < matriz.getNumLin(); lin++) {
            if (matriz.getSup(lin, col) < 0) {
                resp = false;
                break;
            }
            if (lin == matriz.getNumLin() - 1) {
                for (col = 2; col < matriz.getNumCol(); col++) {
                    if (matriz.getSup(1, col) > 0) {
                        for (int linAux = 2; linAux < matriz.getNumLin(); linAux++) {
                            if (matriz.getSup(linAux, col) >= 0) {
                                resp = false;
                                break;
                            }
                            if (linAux == matriz.getNumLin() - 1) {
                                System.out.println("SOLUÇÃO ILIMITADA");
                                resp = true;
                                return resp;
                            }
                        }
                    }
                }
            }
        }

        return resp;
    }

}

class Matriz {
    private No[][] matriz;

    public Matriz(int lin, int col) {
        this.matriz = new No[lin][col];
    }

    public void setNo(int lin, int col, No no) {
        this.matriz[lin][col] = no;
    }

    public double getSup(int lin, int col) {
        return matriz[lin][col].getSup();
    }

    public double getInf(int lin, int col) {
        return matriz[lin][col].getInf();
    }

    public int getNumLin() {
        return matriz.length;
    }

    public int getNumCol() {
        return matriz[0].length;
    }

    public String getVar(int lin, int col) {
        return matriz[lin][col].getVar();
    }

    public No getNo(int lin, int col) {
        return matriz[lin][col];
    }


    public void print() {
        System.out.println("Print matriz");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                if (i == 0 || j == 0) {
                    System.out.print("\t" + getVar(i, j) + "\t");
                } else {
                    System.out.print(getSup(i, j) + " " + getInf(i, j) + " ");
                }
            }
            System.out.println("\n");
        }
    }
}

class ElemPerm{
    private No no;
    private int lin;
    private int col;

    public ElemPerm(int lin, int col, No no) {
        this.no = no;
        this.lin = lin;
        this.col = col;
    }

    public No getNo() {
        return no;
    }

    public void setNo(No no) {
        this.no = no;
    }

    public int getLin() {
        return lin;
    }

    public void setLin(int lin) {
        this.lin = lin;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

class No {
    private double sup;
    private double inf;
    private String var;

    public No(String var, double sup, double inf) {
        this.var = var;
        this.sup = sup;
        this.inf = inf;
    }

    public double getSup() {
        return sup;
    }

    public void setSup(double sup) {
        this.sup = sup;
    }

    public double getInf() {
        return inf;
    }

    public void setInf(double inf) {
        this.inf = inf;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
