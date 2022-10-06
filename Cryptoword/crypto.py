
usercounter = 0
def finish():
	if usercounter!=0:
    		print ("Πήρες ",usercounter,'στους',len(wordsfound)+usercounter,'πόντους')
	else:
		print('Δεν βρήκες καμία απο τις',len(wordsfound)+usercounter,'λέξεις και δεν πήρες κανένα πόντο')

def wordfound (board,wrd , row,col,incr,incc , N):     
     
     for k in range(len(wrd)):
          if row >= N:
               return False
          if col >= N:
               return False
          
          if wrd[k]!=board[row][col]:
               return False
          
          row+=incr
          col+=incc
          
     return True     

def wordinboard (board,word , row , col , N):     
     found = True     
     if  not wordfound(board , word , row ,col,0,1,N): #right
          if not wordfound(board , word ,row,col, 0,-1,N): #left
               if  not wordfound(board , word ,row,col, -1,0,N): #up
                    if not wordfound(board , word ,row,col, 1,0,N): #down
                            found = False             
     return found


def wordsinboard(board,wordlist,N):     
     foundcounter = 0     
     wordsfound=[None for i in range(len(wordlist))]
     
     for row in range(N):          
          for col in range(N):               
               for w in range(len(wordlist)):                    
                    if wordinboard(board,wordlist[w],row,col,N):                        
                        wordsfound[foundcounter]= wordlist[w]
                        foundcounter += 1     
     for i in range(foundcounter,len(wordlist),1):
          del wordsfound[foundcounter]
     return wordsfound
     
def putword(board,wrd,row,col,incr,incc):
     for k in range(len(wrd)):          
          board[row][col]=wrd[k]
          row+=incr
          col+=incc
     return board

#PLACE WORDS RANDOMLY ON THE BOARD     
def putonboard(direction  , wrd , board , N):
     row=choice(range(N))
     col=choice(range(N))

     if direction == 'Down' and row+len(wrd)-1 >= N:
          direction = 'Up'
     elif direction == 'Up' and row-len(wrd)+1 <0:
          direction = 'Down'
   
     if direction == 'Right' and col+len(wrd)-1 >= N:
          direction = 'Left'
     elif direction == 'Left' and col-len(wrd)+1 <0:
          direction = 'Right'
          
                    
     if direction == 'Down':
        putword(board,wrd,row,col,1,0)
     
     if direction == 'Up':          
        putword(board,wrd,row,col,-1,0)
     
     if direction == 'Right':          
        putword(board,wrd,row,col,0,1)
     
     if direction == 'Left':          
        putword(board,wrd,row,col,0,-1)
        
     return board        
     

from random import choice
import sys 
from threading import Timer
import time

f=list(open('wordlist.txt'))
if len(f)==0:
	print('Δεν υπάρχουν λέξεις')	
	exit()


# FINDING THE WORD WITH THE MAXIMUM LENGTH
maxw=f[0]
n=len(maxw)
for i in range(1,len(f)):
     f[i]=f[i].strip()
     #print(f[i])
     if len(f[i])>n:
          maxw=f[i]
          n=len(maxw)


# ΕΜΦΑΝΙΣΗ ΛΕΞΗΣ ΜΕ ΜΕΓΙΣΤΟ ΜΗΚΟΣ ΚΑΙ ΜΕΓΙΣΤΟ ΜΗΚΟΣ		
#print('LONGEST WORD =',maxw,'MAXIMUM LENGTH=',n)

N=2*n
#INITIALIZATION OF THE BOARD(1.2)
board=[["0" for i in range(N)] for i in range(N)]

#PLACING THE WORDS IN THE BOARD(1.3)
tempf=list(open('wordlist.txt'))
for i in range(len(tempf)):
     tempf[i]=tempf[i].strip()     
directions=['Up','Down','Left','Right']
loopcounter = len(tempf)
for i in range(loopcounter):
	direction=choice(directions)
	wrd=choice(tempf)		
	board=putonboard(direction,wrd,board,N)
	for j in range(len(tempf)):
		if wrd==tempf[j]:
			sim=j
	del tempf[sim]
#END PLACING WORDS

#RANDOM VALUES FOR EMPTY SPACES(1.4)
for row in range(N):
        for col in range(N):
                if(board[row][col]=='0'):
                        board[row][col]=chr(choice(range(65,90,1)))

#SHOW BOARD
for i in range(N):
        letters=''
        for j in range(N):
               letters=letters+board[i][j]+' '
        print(letters)
        

#LOCATION OF WORDS ENTERED INTO THE BOARD(2)
wordsfound=[None for i in range(len(f))]
wordsfound=wordsinboard(board,f,N)
#print (' words in board =',len(wordsfound))

#for i in range(len(wordsfound)):
#        print(wordsfound[i])

timeout = time.time() + 1*60
userword=''
while True:
    if time.time() < timeout:
        userword=str(input("ΔΩΣΕ ΤΗΝ ΕΠΟΜΕΝΗ ΛΕΞΗ:"))
        userword=userword.strip()
    if time.time() > timeout:
         break
    index=-1
    counter = len(wordsfound)
    #print(userword)
    for k in range(counter):          
         if userword==wordsfound[k]:
            index=k
    if index!=-1:
        usercounter+=1
        del wordsfound[index]
finish()

          
          
     
          
     
