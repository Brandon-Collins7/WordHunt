from WordHunt import WordHunt
#Test Case: Find all words in a 2D board that are in a lexicon

#Input:
#[["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]]
#["oath","pea","eat","rain"]

#Output:
#["oath", "eat"]


boardInput = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]]
lexiconInput = ["oath","pea","eat","rain"]
a = WordHunt(boardInput, lexiconInput)
a.printBoard()
result = a.findWords()
assert(result == ["oath", "eat"])

print("\n\n")

#Test Case: Find if a word exists in a 2D board

#Input:
#[["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]]
#"SEE"

#Output:
#True

boardInput = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]]
wordInput = "SEE"
b = WordHunt(boardInput, [])
b.printBoard()
result = b.exist(wordInput)
assert(result == True)
