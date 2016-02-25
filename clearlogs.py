import os

x=0
for i in os.listdir('.'):
	if '.log' in i:
		os.remove(i)
		x+=1
		print('removed '+i)
print('\nremoved '+str(x)+' logs')
raw_input('press any key to finish.')
