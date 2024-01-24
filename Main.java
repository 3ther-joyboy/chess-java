import java.util.Scanner;
public class Main{
    /*
        kladné čísla    ->  bílí
        záporné čísla   ->  černý
    
        0 -> volné místo
        1 -> pěšák   
        2 -> kůň
        3 -> střelec
        4 -> věž
        5 -> královna
        6 -> král
    
     */
    static String let[] = {"a","b","c","d","e","f","g","h"};
    static String piece[] = {"#","i","f","x","H","Y","0"};
    static String white = "\033[0;37m";
    static String red = "\033[0;31m";
    static String blue = "\033[0;34m";
    static boolean game = true;
    private static boolean bishopValidMove(int x1,int y1,int x2, int y2, int[][] board){
        boolean validMove = true;
        if(Math.abs(x1-x2) == Math.abs(y1-y2)){
            for(int i = 1; i < Math.abs(x1-x2); i++){
                if(board[y1 - i*((y1-y2)/Math.abs(y1-y2))][x1 - i*((x1-x2)/Math.abs(x1-x2))] != 0) {
                    validMove = false;
                    break;
                }
            }
        } else {
            validMove = false;
        }
        return  validMove;
    }
    private static boolean rookValidMove(int x1,int y1,int x2, int y2, int[][] board){
        boolean validMove = true;
        if(x1 == x2 || y1 == y2){
            if(x1 == x2){
                for(int i = 1; i<Math.abs(y1-y2); i++)
                    if(board[y1 - i*((y1-y2)/Math.abs(y1-y2))][x1] != 0) {
                        validMove = false;
                        break;
                    }
            }else{
                for(int i = 1; i<Math.abs(x1-x2); i++)
                    if(board[y1][x1 - i*((x1-x2)/Math.abs(x1-x2))] != 0) {
                        validMove = false;
                        break;
                    }
            }
        }else{validMove = false;}
        return validMove;
    }

    
    private static boolean validMove(int x1,int y1,int x2, int y2, int[][] board,boolean toMove){
        boolean validMove = true;
        if(board[y1][x1] < 0 && board[y2][x2] < 0 || board[y1][x1] > 0 && board[y2][x2] > 0 ){ validMove = false;prl("frendly fire");}
        if((board[y1][x1] < 0 && toMove)||(board[y1][x1] > 0 && !toMove)){ validMove = false;prl("nesprávný hráč");}
        if(x1 == x2 && y1 == y2) {validMove = false;prl("stejné políčko");}

            switch (Math.abs(board[y1][x1])) {
                case 1:
                    if(!(
                            (y1 - y2 == board[y1][x1] && x1 == x2 && board[y2][x2] == 0)||
                            ((y1 == 1 || y1 == 6) && y1 - y2 == 2*board[y1][x1] && x1 == x2 && board[y1 - board[y1][x1]][x1] == 0 && board[y2][x2] == 0)||
                            (Math.abs(x1 - x2) == 1 && y1 - y2 == board[y1][x1] && board[y2][x2] != 0)
                    )){
                        validMove = false;
                        prl("invalidní tah pěšcem");
                    }

                    break;
                case 2:
                    if(!(Math.abs(x1 - x2)+Math.abs(y1-y2)==3 && (Math.abs(x1 - x2) == 2 || Math.abs(x1 - x2) == 1 ))){
                        validMove = false;
                        prl("invalidní tah koněm");
                    }break;
                case 3:
                    validMove = bishopValidMove(x1, y1, x2, y2, board);
                    break;
                case 4:
                    validMove = rookValidMove(x1, y1, x2, y2, board);
                    break;
                case 5:
                    if(!(bishopValidMove(x1, y1, x2, y2, board)||rookValidMove(x1, y1, x2, y2, board)))validMove = false;
                    break;
                case 6:
                    if(!(Math.abs(x1-x2) <= 1 && Math.abs(y1-y2) <= 1))validMove = false;
                    break;
                default: validMove = false;
            }
        return validMove;
    }
    
    
    private static int toS(String input,int x){
        String z = input.substring(x,x+1);

        for (int i = 0; i < let.length; i++)
        if (z.equals(let[i])) z = ""+(i + 1);
        
        int out = Integer.parseInt(z) - 1;
        return out;
}

    private static void pr(String x){
        System.out.print(x);
    }
    private static void prl(String x){
        System.out.println(x);
    }

    private static void print(int[][] table){
        byte gamin = 0;
        pr("  ");
        for(int y = 0;y<table[0].length;y++)
            pr(white + let[y] +" ");
            prl("");

        for(int i = 0;i<table.length;i++){
            pr(white + (i + 1) + " ");
            
            for(int y = 0;y<table[i].length;y++){
                int x = Math.abs(table[i][y]);
                if(x==6)gamin++;

                if(table[i][y]!= 0){
                    pr(table[i][y]>0 ? red : blue);
                    pr(piece[x]);
                }else{
                    pr(white + piece[x]);
                }
                pr(" ");
            }
        prl("");
        }
    if(gamin < 2) game = false;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] board = {
            {-4,-2,-3,-5,-6,-3,-2,-4},
            {-1,-1,-1,-1,-1,-1,-1,-1},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0},
            {1,1,1,1,1,1,1,1},
            {4,2,3,5,6,3,2,4},
        };  

        boolean toMove = false;
        while(game){
            prl(toMove ? red + "Red to move" : blue + "Blue to move");prl("");
            print(board);
            pr(white);
            String inputTxt = "00 00";
            do{
                inputTxt = scanner.nextLine();
            }while(!validMove(toS(inputTxt,0), toS(inputTxt,1), toS(inputTxt,3), toS(inputTxt,4),board,toMove));

            board[toS(inputTxt,4)][toS(inputTxt,3)] = board[toS(inputTxt,1)][toS(inputTxt,0)];
            board[toS(inputTxt,1)][toS(inputTxt,0)] = 0;
            
            if(game)toMove = !toMove;
        };
        print(board);
        pr(toMove?blue:red);
        prl(toMove?"Red won":"Blue won");
        }
}