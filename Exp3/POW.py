import time
import hashlib
import matplotlib.pyplot as plt

difficulty=[0,1,2,3,4,5,6]
time_elapsed=[]
nonce_list=[]
solution_list=[]

def calculate_pow(diff):
  flag=0
  nonce=0
  while flag==0:
    ans="test"+str(nonce)
    shaHash=hashlib.sha256()
    shaHash.update(ans.encode('utf-8'))
    solution=shaHash.hexdigest()  
    if(solution.startswith(diff)):
      nonce_list.append(nonce)
      solution_list.append(solution)
      flag=1
    nonce=nonce+1

diff=""
for i in range(0,7):
  start = time.process_time()
  calculate_pow(diff)
  time_taken=time.process_time() - start
  time_elapsed.append(time_taken)
  diff=diff+"0"

print("Diff\tNonce\t\tSolution\t\t\t\t\t\t\t\tTime")
for i in range(0,7):
  print(str(difficulty[i])+"\t"+str(nonce_list[i])+"\t\t"+solution_list[i]+"\t"+str(time_elapsed[i]))

plt.plot(difficulty, time_elapsed)
plt.xlabel("Difficulty Level")
plt.ylabel("Time")
plt.show()