import java.io.File;
import java.io.FileNotFoundException;
//import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;



public class GameEngine implements WordHuntGame{
    private String[][] grid;
    private boolean[][] visited;
    private boolean loaded;
    private TreeSet<String> lexicon;
    private SortedSet<String> scorableWords;
    private List<Integer> path;
    private boolean pathFound;

    
    //each position on grid may be more than one character
    public GameEngine(){
    
        String[][] gridTemp = { {"E","E","C","A"} , {"A", "L", "E", "P"} , {"H","N","B","O"} , {"Q", "T", "T", "Y"} };
      
        grid = new String[4][4];
        for(int r = 0; r < 4; r++){
            for(int c = 0; c < 4; c++){
                grid[r][c] = gridTemp[r][c];
            }
        }

    }

    public void loadLexicon(String file){

        if(file == null){
            throw new IllegalArgumentException();
        }


        lexicon = new TreeSet<String>();

        
        try{
            Scanner sc = new Scanner(new File(file));
            while (sc.hasNextLine()){
                
                //make words uppercase for comparison
                String s = sc.next().toUpperCase();
                lexicon.add(s);
                sc.nextLine();

            }
            sc.close();
        }
        catch(FileNotFoundException e){
            throw new IllegalArgumentException();
        }
        
        loaded = true;
    }

    public void setBoard(String[] array){
        if(array == null){
            throw new IllegalArgumentException();
        }

        //check if array is square
        double width = Math.sqrt(array.length);
        if(width != (int) width){
            throw new IllegalArgumentException();
        }
       
        grid = new String[(int)width][(int)width];
        int index = 0;
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                grid[r][c] = array[index++];
            }
        }
    }

    public String getBoard(){
        String out = "";
        for(String[] r : grid){
            for(String c : r){
                out+= c + " ";
            }

            out+="\n";
        }

        return out;
    }

    public SortedSet<String> getAllScorableWords(int reqLen){
        if(reqLen<1){
            throw new IllegalArgumentException();
        }
        if(!loaded){
            throw new IllegalStateException();
        }
        

        scorableWords = new TreeSet<String>();
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid.length; y++){
                
                //otherwise no reason to start dfs
                //also prevents error with out of bounds when dfs reaches 
                //where buildWord has a length of 0
                    if(isValidPrefix(grid[x][y])){

                    Position first = new Position(x, y);
                    boolean[][] visitedTemp = new boolean[grid.length][grid.length];
                    visited = visitedTemp;
                    //set all to false
                    
                    
                    visited[first.x][first.y] = true;
                
                    
                    String buildWord=grid[first.x][first.y];
                
                    dfsAll(first, reqLen, buildWord);
            
                }
            }
        }
        
        return scorableWords;
        
    }



    //Uses DFS in helper method
    public List<Integer> isOnBoard(String word){
        if (word == null){
            throw new IllegalArgumentException();
        }
        if (!loaded){
            throw new IllegalStateException();
        }
    
        pathFound = false;

        //make words uppercase for comparison
        word.toUpperCase();

        path = new ArrayList<Integer>();

        
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid.length; y++){
                Position first = new Position(x, y);
                boolean[][] visitedTemp = new boolean[grid.length][grid.length];
                visited = visitedTemp;
                //set all to false

                visited[first.x][first.y] = true;
                    
                
                String buildWord=grid[first.x][first.y];
                path = new ArrayList<Integer>();
                path.add(grid.length*x+y);
                dfsOnBoard(first, buildWord, word);
                if(path.size() >= word.length()){
                    return path;
                }

            
                
            }
        }
        
        path = new ArrayList<Integer>();
        return path;
        

    }

    
    private void dfsOnBoard(Position start, String buildWord, String targetWord) {
        //start basically becomes position
        
        for (Position neighbor : start.neighbors()) {

            //quick exit and don't add extra values to path
            if(pathFound){
                return;
            }
            
            if (!isVisited(neighbor)) {
                
                int l = buildWord.length();
                if(targetWord.length()>= buildWord.length() && (buildWord).equals(targetWord.substring(0,l))){
                    
                        
                    buildWord += grid[neighbor.x][neighbor.y];
                    

                    visit(neighbor);
                    
                    path.add(grid.length*neighbor.x+neighbor.y);    
                    
                    if(buildWord.equals(targetWord)){
                        pathFound = true;
                        return;
                    }

                    dfsOnBoard(neighbor, buildWord, targetWord);

                    buildWord=buildWord.substring(0,buildWord.length()-grid[start.x][start.y].length());
                    visited[neighbor.x][neighbor.y]=false;
                    if(!pathFound){
                        path.remove(path.size()-1);
                    }
                    //unvisit so that word backtracking can use letter part of other prefix
                    //best shown with Alepot and Albee
                    
                }
            }
        }
        return;
    }


    private void dfsAll(Position start, int reqLen, String buildWord) {

        if(isValidWord(buildWord) && buildWord.length() >= reqLen){
            scorableWords.add(buildWord);
        }
        
        for (Position neighbor : start.neighbors()) {
                
            if (!isVisited(neighbor)) {
               
                if(isValidPrefix(buildWord+grid[neighbor.x][neighbor.y])){
                    visit(neighbor);
                        
                        
                    buildWord += grid[neighbor.x][neighbor.y];

                        
                    dfsAll(neighbor, reqLen, buildWord);
                    buildWord=buildWord.substring(0,buildWord.length()-grid[neighbor.x][neighbor.y].length());
                    

                    visited[neighbor.x][neighbor.y]=false;
                    //unvisit so that word backtracking can use letter part of other prefix
                    //best shown with Alepot and Albee
                }
                }
            }
        

    }


    


    public int getScoreForWords(SortedSet<String> set, int reqLen){
        if(reqLen < 1){
            throw new IllegalArgumentException();
        }
        if(!loaded){
            throw new IllegalStateException();
        }

        int sum = 0;
        for(String word: set){
            sum+=word.length()-reqLen+1;
        }

        return sum;
    }







    //if load hasn't been called, this must
    //throw new IllegalStateException();
    public boolean isValidWord(String word){
        if(word == null){
            throw new IllegalArgumentException();
        }
        if(!loaded){
            throw new IllegalStateException();
        }

        //make words uppercase for comparison
        word = word.toUpperCase();
        return lexicon.contains(word);
    }

    //if load hasn't been called, this must
    //throw new IllegalStateException();
    public boolean isValidPrefix(String prefix){
        if(prefix == null){
            throw new IllegalArgumentException();
        }
        if(!loaded){
            throw new IllegalStateException();
        }

        prefix = prefix.toUpperCase();
        String ceil = lexicon.ceiling(prefix);
        int length = prefix.length();
        return (ceil != null && ceil.length() >= prefix.length() && prefix.equals(ceil.substring(0,length)));
        //need to make sure each adj is long enough
        
        //when taking substring, could be the whole adj word as prefix could equal one adj
    }






    //***POSITION CLASS AND RELATED METHODS***//

    class Position {
            int x;
            int y;
            public Position(int x, int y) { 
                this.x = x;
                this.y = y;
            }

            public Position[] neighbors() {
            
                final int MAX_NEIGHBORS = 8;
                Position[] nbrs = new Position[MAX_NEIGHBORS];
                int count = 0;
                Position p;
                // generate all eight neighbor positions
                // add to return value if valid
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (!((i == 0) && (j == 0))) {
                            p = new Position(x + i, y + j);
                            if (isValid(p)) {
                                nbrs[count++] = p;
                            }
                        }
                    }
                }
                return Arrays.copyOf(nbrs, count);
            }
            
            
        }



   
    //check if position is in bounds
    private boolean isValid(Position p) {
        return (p.x >= 0) && (p.x < grid.length) && 
               (p.y >= 0) && (p.y < grid[0].length);
    }

    
    //check if position has been visited
    private boolean isVisited(Position p) {
        return visited[p.x][p.y];
    }


    //mark this valid position as visited
    private void visit(Position p) {
        visited[p.x][p.y] = true;
    }




}
