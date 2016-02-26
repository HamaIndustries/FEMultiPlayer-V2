from os import listdir
files = [f for f in listdir("./")]
with open(".audionames.txt", "w+") as out:
    for i in files:
        if i.find(".ogg")<0:
            files.remove(i)
        else:
            out.write(i[0:i.find(".ogg")] + "\n")
