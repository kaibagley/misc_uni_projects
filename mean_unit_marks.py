import os

def problem_check():
    if len(problem)>0:
            print("A problem was discovered regarding {}. Please review your files and try again.".format(problem[0]))
            return True

def filecheck(file_name):
    global permission
    if os.path.isfile(".\\{}".format(file_name)):
        permission = True
    else:
        permission = False
        problem.append(file_name)
        

def open_file(file_name):
    if os.path.isfile(".\\{}".format(file_name)):
        return open("{}".format(file_name),"r")

def get_units(unit_file):
    list = []
    for row in unit_file:
        if not len(row.split(","))==2: #makes sure that the units file is actually a units file
            problem.append("incorrect units file format, this must only be a list of pairs of items")
            return
        else:
            x=row.replace("\n","").split(",")[0]
            y=float(row.replace("\n","").split(",")[1])
            list.append(tuple([x,y]))
    return list

def check_units(list):
    return len(list)

def vtest(val):         #tests strings for integers, None or student name
    try:
        int(val)
        return 1
    except ValueError:
        if val=="":
            return 2
        elif val=="\n":
            return 3
        elif val=="None":
            return 4
        else:
            return
        

def get_student_record(student_file,unit_count):
    list=[]
    for row in student_file: 
        tup=[]
        for i in row.split(","):                        #builds the tuples
            if vtest(i)==1:             #tests and builds from int strings
                tup.append(int(i))
            elif (vtest(i)==2) or (vtest(i)==3):   #test/build string None
                tup.append(None)
            else:                                 #test/build name strings
                tup.append(i)
        if not len(tup)-1==unit_count: #tests number of units
            problem.append("number of units taken and/or possible for a student")
            break
        list.append(tuple(tup))          #builds the actual list of tuples
    return list

def normalise(student_list,unit_list):
    list=[]
    for i in student_list:
        tup=[i[0]]
        for j in range(len(i[1:])):
            if vtest("{}".format(i[j+1]))==1:      #test for actual number
                n=i[j+1]/unit_list[j][1]             #calc for normalising
                tup.append(n)
            else:                      #accounts for None not being an int
                tup.append(None)
        list.append(tup)
    return list

def compute_mean_pc(student_pclist):
    list=[]
    for i in student_pclist:
        x=[j for j in i[1:] if j is not None]          #list without Nones
        y=sum(x)/len(x)                                     #calc for mean
        list.append((i[0],y))           #puts back names where they belong
    return list
    
def print_final_list(mean_pclist):
    for i in mean_pclist:
        print(" ".join(map(str,i))) #joins tuple parts, makes it printable

def main():
    units = str(input("Enter name of units file (with extension): "))
    marks = str(input("Enter name of marks file (with extension): "))
    global problem
    problem = []
    filecheck(units)
    filecheck(marks)
    if permission: #ask for permission first
        unitfile         = open("{}".format(units),"r")
        studentfile      = open("{}".format(marks),"r")
        unitlist         = get_units(unitfile)
        if problem_check():
            return
        unitcount        = check_units(unitlist)
        studentlist      = get_student_record(studentfile,unitcount)
        if problem_check():
            return
        studentpclist   = normalise(studentlist,unitlist)
        meanpclist      = compute_mean_pc(studentpclist)
        print_final_list(meanpclist)
    else:
        if len(problem)==1:
            print("The file \"{}\" does not exist. Please try again.".format(problem[0]))
        else:
            print("The files \"{}\" and \"{}\" do not exist. Please try again.".format(problem[0],problem[1]))


main()