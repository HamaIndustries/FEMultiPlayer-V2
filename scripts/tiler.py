from PIL import Image as i
import os

imgs=[]
for item in os.listdir('.\\'):
    if item.find('.png')>0:
       imgs.append(item)

img1 = i.open(imgs[0])
num = len(imgs)
dim1 = img1.size[0]
dim2 = img1.size[1]
grid = (dim1*5, dim2*(num//5+num%5))
out = i.new("RGBA", grid)
print(len(imgs))
print(img1.size)
print(grid)

ind=0

for y in range(num//5+num%5):
    for x in range(5):
        if(ind<num):
            out.paste(i.open(imgs[ind]), (dim1*x, dim2*y))
            ind+=1
out.save("aaaa.png")
