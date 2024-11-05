from collections import defaultdict
from typing import Counter
import random
import string


class WordHunt:
    
    ''' setup functions '''
    
    #initialize WordHunt object; create board and 
    def __init__(self, board = [], lexicon = []):
        self.board = board
        self.lexicon = {}
        
        #build lexicon as trie
        for word in lexicon:
            curr = self.lexicon
            for letter in word:
                if letter not in curr:
                    curr[letter] = {}
                curr = curr[letter]
                
            curr["END"] = word #means a word ends here, and gives you the word
        self.wordsOnBoard = []
        
        
    def setBoard(self, board: list[list[str]]):
        self.board = board
        self.wordsOnBoard = [] #reset wordsOnBoard
        
    def createRandomBoard(self, rows: int, columns: int):
        self.board = [[random.choice(string.ascii_uppercase) for _ in range(columns)] for _ in range(rows)]
        self.wordqOnBoard = [] #reset wordsOnBoard

    def getBoard(self):
        return self.board
    
    #print board in a readable format
    def printBoard(self):
        print('\n'.join([' '.join(row) for row in self.board]))
    
    
    ''' algorithmic functions '''
        
    #find all words in a 2D board that are in a lexicon
    #O(N*M * 3^L) time complexity; N = grid rows, M = grid columns, L = length of word
    def findWords(self) -> list[str]:
            if self.wordsOnBoard:
                return self.wordsOnBoard
            
            #can go up, right, down, left
            self.directions = [(0,1), (1,0), (0,-1), (-1,0)]

            for r in range(len(self.board)):
                for c in range(len(self.board[0])):
                    if self.board[r][c] in self.lexicon:
                        self.dfsAllWords(r, c, self.lexicon)
            
            return self.wordsOnBoard

    #helper function for findWords
    def dfsAllWords(self, r : int, c : int, currNode : dict):

        letter = self.board[r][c]
        nextNode = currNode[letter]
        
        #reached end of word, delete for pruning
        if "END" in nextNode:
            self.wordsOnBoard.append(nextNode["END"])
            del nextNode["END"]

        #tracking visited without using extra visited set
        self.board[r][c] = "#"

        for d in self.directions:
            nextR = r + d[0]
            nextC = c + d[1]
            
            #in bounds
            if nextR >= len(self.board) or nextR < 0 or nextC >= len(self.board[0]) or nextC < 0:
                continue
            #not a valid prefix
            if self.board[nextR][nextC] not in nextNode:
                continue
                
            self.dfsAllWords(r + d[0], c + d[1], nextNode)

        #backtrack, marking grid space back to what it was
        self.board[r][c] = letter
        
        #nothing left to explore with this prefix
        if not nextNode:
            del currNode[letter]
            
    
    #find if a word exists in a 2D board
    #O(N*M * 3^L) time complexity; N = grid rows, M = grid columns, L = length of word
    def exist(self, word: str) -> bool:
        
        #PRUNING - if # of each letter that make up word aren't on board, return False
        wordFreq = Counter(word)
        boardFreq = defaultdict(int)
        for row in self.board:
            for cell in row:
                boardFreq[cell]+=1
        
        for key, value in wordFreq.items():
            if value > boardFreq[key]:
                return False
    
        #PRUNING - if last letter of word appears less on board that first letter, reverse word
        if boardFreq[word[-1]] < boardFreq[word[0]]:
            word = word[::-1]


        self.board = self.board
        self.directions = [(0,1), (1,0), (0,-1), (-1,0)]

        for r in range(len(self.board)):
            for c in range(len(self.board[0])):
                self.visited = set()
                
                #only need one instance where word is found
                if self.dfsExist(r, c, word):
                    return True
        
        return False

    #helper function for exist
    def dfsExist(self, r : int, c : int, wordLeft : str) -> bool:
        if len(wordLeft)==0:
            return True

        #in bounds
        if r >= len(self.board) or r < 0 or c >= len(self.board[0]) or c < 0:
            return False

        #correct letter, not visited
        if self.board[r][c] != wordLeft[0] or (r,c) in self.visited:
            return False
        
        self.visited.add((r,c))
        for d in self.directions:
            if self.dfsExist(r + d[0], c + d[1], wordLeft[1:]):
                return True

        self.visited.remove((r,c)) #can visit again if backtracking
        
        return False
    
    
    ''' game functions '''
    
    #prints the best numWords words on the board based on length
    #longer words give more points in WordHunt/Boggle game
    def printBestWords(self, numWords: int):
        if not self.wordsOnBoard:
            self.findWords()
        self.wordsOnBoard.sort(key = lambda x: len(x), reverse = True)
        for i in range(0, min(numWords, len(self.wordsOnBoard))):
            print(self.wordsOnBoard[i])

    
    
