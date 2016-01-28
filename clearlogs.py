import os

x=0
for i in os.listdir('.'):
	if i.endswith('.log'):
		os.remove(i)
		x+=1
print('removed '+str(x)+' logs')
