sum = 0

num = 10000

for i in range(num):
	n = input()
	sum = sum + n


ave = float(sum) / float(num)
print('10000 try average = ' + str(ave))
