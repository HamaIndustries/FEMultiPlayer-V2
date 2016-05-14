import os, platform
deer = os.listdir('..\\')
count = 0
for i in range(len(deer)):
	if '.log' in deer[i]:
		os.remove('..\\'+deer[i])
		print('removed '+deer[i])
		count+=1
print('\nremoved '+str(count)+' logs')

if platform.python_build()[0].find('v3')>=0:
        input('press enter to finish.')
else:
        raw_input('press enter to finish.')
