import java.util.List;
import java.util.SortedSet;


public interface WordHuntGame {
   

    /** 
     * loads dictionary of words for game 
     * @throws IllegalArgumentException if file is null or can't be found/opened
     */
    void loadLexicon(String fileName);
    

    /**
     * @param grid square array starting at (0,0) in top left. can have more than one char in each space
     * @throws IllegalArgumentException if letterArray is null, or is not square.
     */
    void setBoard(String[] grid);
    

    
    //outputs a string form of the board
    String getBoard();
    
    
    /**
     * Retrieves all scorable words on the game board
     * 
     * @throws IllegalArgumentException if minimumWordLength < 1
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    SortedSet<String> getAllScorableWords(int minimumWordLength);
    
  /**
    * Computes the cummulative score for the scorable words in the given set.
    * To be scorable, a word must (1) have at least the minimum number of characters,
    * (2) be in the lexicon/dictionary, and (3) be on the board. Each scorable word is
    * awarded one point for the minimum number of characters, and one point for 
    * each character beyond the minimum number.
    *
    * @param words The set of words that are to be scored.
    * @param minimumWordLength The minimum number of characters required per word
    * @return the cummulative score of all scorable words in the set
    * @throws IllegalArgumentException if minimumWordLength < 1
    * @throws IllegalStateException if loadLexicon has not been called.
    */  
    int getScoreForWords(SortedSet<String> words, int minimumWordLength);
    
    /**
     * Determines if the given word is in the lexicon.
     * 
     * @param wordToCheck The word to validate
     * @return true if wordToCheck appears in lexicon, false otherwise.
     * @throws IllegalArgumentException if wordToCheck is null.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    boolean isValidWord(String wordToCheck);
    
    /**
     * Determines if there is at least one word in the lexicon with the 
     * given prefix.
     * 
     * @param prefixToCheck The prefix to validate
     * @return true if prefixToCheck appears in lexicon, false otherwise.
     * @throws IllegalArgumentException if prefixToCheck is null.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    boolean isValidPrefix(String prefixToCheck);
        
    /**
     * Determines if the given word is in on the game board. If so, it returns
     * the path that makes up the word.
     * @param wordToCheck The word to validate
     * @return List containing Integer objects with the path
     *     that makes up the word. If word is not on the game
     *     board, return an empty list. Positions on the board are numbered from zero
     *     top to bottom, left to right.
     * @throws IllegalArgumentException if wordToCheck is null.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    List<Integer> isOnBoard(String wordToCheck);

}
