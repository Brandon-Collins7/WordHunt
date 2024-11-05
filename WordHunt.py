from collections import defaultdict
from typing import Counter


class WordHunt:
    
    #O(N*M * 3^L) time complexity; N = grid rows, M = grid columns, L = length of word
    def findWords(self, board: list[list[str]], words: list[str]) -> list[str]:
        
            #build trie
            trie = {}
            for word in words:
                curr = trie
                for letter in word:
                    if letter not in curr:
                        curr[letter] = {}
                    curr = curr[letter]
                
                curr["END"] = word #means a word ends here, and gives you the word


            self.wordsOnBoard = []
            self.board = board
            
            #can go up, right, down, left
            self.directions = [(0,1), (1,0), (0,-1), (-1,0)]

            for r in range(len(board)):
                for c in range(len(board[0])):
                    if board[r][c] in trie:
                        self.dfsAllWords(r, c, trie)
            
            return self.wordsOnBoard


    def dfsAllWords(self, r, c, currNode):

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
            
            
    #O(N*M * 3^L) time complexity; N = grid rows, M = grid columns, L = length of word
    def exist(self, board: list[list[str]], word: str) -> bool:
        
        #PRUNING - if # of each letter that make up word aren't on board, return False
        wordFreq = Counter(word)
        boardFreq = defaultdict(int)
        for row in board:
            for cell in row:
                boardFreq[cell]+=1
        
        for key, value in wordFreq.items():
            if value > boardFreq[key]:
                return False
    
        #PRUNING - if last letter of word appears less on board that first letter, reverse word
        if boardFreq[word[-1]] < boardFreq[word[0]]:
            word = word[::-1]


        self.board = board
        self.directions = [(0,1), (1,0), (0,-1), (-1,0)]

        for r in range(len(board)):
            for c in range(len(board[0])):
                self.visited = set()
                
                #only need one instance where word is found
                if self.dfsExist(r, c, word):
                    return True
        
        return False


    def dfsExist(self, r, c, wordLeft):
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


#Test Case: Find all words in a 2D board that are in a lexicon

#Input:
#[["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]]
#["oath","pea","eat","rain"]

#Output:
#["oath", "eat"]

boardInput = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]]
lexiconInput = ["oath","pea","eat","rain"]
a = WordHunt()
result = a.findWords(boardInput, lexiconInput)
assert(result == ["oath", "eat"])



#Test Case: Find if a word exists in a 2D board

#Input:
#[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]]
#"SEE"

#Output:
#True

boardInput = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]]
wordInput = "SEE"
result = a.exist(boardInput, wordInput)
assert(result == True)
