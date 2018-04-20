sum = 0

num = 10000

mx = -100

mn = 114514


for i in range(num):
	n = input()
	sum = sum + n
	mx = max(n, mx)
	mn = min(n, mn)

ave = float(sum) / float(num)
print('10000 try average = ' + str(ave))
print('max = ' + str(mx))
print('min = ' + str(mn))
