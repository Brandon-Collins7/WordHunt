

public class ExampleWordHunt {
    public static void main(String[] args) {
       WordHuntGame game = new GameEngine();
       game.loadLexicon("Projects\\WordHuntSolver\\words.txt");

       game.setBoard(new String[]{"A", "D", "S", "T", "F", "K", "E", "S", "M", 
        "L", "B", "R", "O", "Y", "T", "C"});
       System.out.print(game.getBoard());
       System.out.print("BLESS is can be made in this way ");
       System.out.println(game.isOnBoard("BLESS"));
       System.out.print("TEST is not on the board: ");
       System.out.println(game.isOnBoard("TEST"));
       System.out.println("All words of length 5 or more: ");
       System.out.println(game.getAllScorableWords(5));

        
       
       System.out.println();
       game.setBoard(new String[]{"E", "E", "C", "A", "A", "L", "E", "P", "H", 
        "N", "B", "O", "Q", "T", "T", "Y"});
       System.out.print(game.getBoard());

       System.out.print("LENT is can be made in this way ");
       System.out.println(game.isOnBoard("LENT"));
       System.out.print("POPE is not on the board: ");
       System.out.println(game.isOnBoard("POPE"));
       System.out.println("All words of length 6 or more: ");
       System.out.println(game.getAllScorableWords(6));
    }
}



//Sample output:

/*
A D S T 
F K E S
M L B R
O Y T C
BLESS is can be made in this way [10, 9, 6, 2, 7]
All words of length 5 or more:
[BLESS, BLEST, CRESS, CREST, DAKER, DERBY, FADER, FAKER, KELTY, MOLER, MOLEST, MOLKA, MOYLE, SKELF, STERT, TREBLY, TRESS, TREST, TYLER, YOLKED]

E E C A
A L E P
H N B O
Q T T Y
LENT is on the board at the following positions: [5, 6, 9, 14]
POPE is not on the board: []
All words of length 6 or more: 
[ALEPOT, BENTHAL, PELEAN, TOECAP]
 */