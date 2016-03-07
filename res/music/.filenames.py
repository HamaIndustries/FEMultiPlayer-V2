from os import listdir, remove
files = [f if not f[0] == '.' else "" for f in listdir("./")]
with open(".audionames.txt", "w+") as out:
    for i in files:
        if i.find('.ogg')<0 and not i == '':
            remove(i)
        else:
            out.write(i[0:i.find(".ogg")] + "\n")
