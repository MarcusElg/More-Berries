import os

for filename in os.listdir(os.getcwd()):
    with open(os.path.join(os.getcwd(), filename), 'r') as f:  # open in readonly mode
        new_text = f.read().replace("moreberries:block/", "TEMP").replace("moreberries:",
                                                                          "moreberries:block/").replace("TEMP", "moreberries:block/")
    with open(os.path.join(os.getcwd(), filename), 'w') as f:  # open in readonly mode
        f.write(new_text)
