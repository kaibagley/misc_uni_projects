import string,os,math

def main(corpus_file_name, test_sets_file, commonwords_file_name): #eg. main("sample.txt", "sample0_set.txt", "common.txt")
    starttime=os.times() #on my computer it takes approx. 10 sec to complete
    print('Running...')
    probwords=''
    try:
        text=open(corpus_file_name,'r').read()[:-1].replace('\n',' ').lower().split('. ') #slice is for final . makes one long line and
        comm=open(commonwords_file_name,'r').read().split('\n')
        test=open(test_sets_file,'r').read().split('\n\n') #\n is a space so '. 'works
        if len(text)<=1:
            print('Corpus text file is too short for accurate synonyms. Please use a longer file.')
            return
        if len(test)<=1: #This is to test if there is only one or no target word
            print('Please use a test set file that includes enough testing data.')
            return
    except FileNotFoundError:
        print('Please ensure the names entered are of existing files.')
        return
    bigdict={}
    for i in range(len(text)):
        sent=list(set(text[i].translate(text[i].maketrans('', '', '!?,;:\'\"[]()')).replace('--',' ').split(' ')))
        for word in sent:
            try: #adds 1 to the existing dictionary
                bigdict[word]
                addict(word,sent,bigdict,comm) 
            except KeyError: #creates a dictionary and adds 1 to it
                if remword(comm,word)==1:
                    continue
                bigdict[word]={}
                addict(word,sent,bigdict,comm)
    for experiment in test:
        badwords=set() #set of words that do not appear and do not have profiles in the text
        exp=experiment.split('\n')
        trgtk=exp.pop(0)
        wordscores=[]
        print('Target word is',trgtk)
        for testk in exp:                 #profkey is the current word tested against the target
            shared=[]                       #list of shared keys that will be found, resets for each test word
            try:
                for j in bigdict[testk]:              #j is the key of each of the entries in the profkeys profile
                    try:
                        for k in bigdict[trgtk]:             #k is key of each of the entries in the targets profile
                            if j!=k:
                                continue
                            shared.append((k,bigdict[trgtk][k],bigdict[testk][k])) #first num is in profkeys profile
                            break                           #since each key is a unique entry
                    except KeyError:
                        badwords.add(trgtk)
                        continue
            except KeyError:  #this means the word isnt in the text AT ALL
                badwords.add(testk)
                pass
            wordscores.append((testk,cosscore(shared,trgtk,testk,bigdict)))
        wordscores.sort(key=lambda tup: tup[1],reverse=True) #sorts by size of second part of tuple
        synonym=''
        similarity=0
        for ws in wordscores:
            print('\t{0}\t{1:0.3f}'.format(ws[0],ws[1]))
            if ws[1]>similarity:
                synonym=ws[0]
                similarity=ws[1]
        print('The synonym for',trgtk,'is',synonym,'\n')
    finishtime=os.times()
    print("\nExecution Times - User: {0:0.2f} Sys: {1:0.2f}".format(finishtime[0] - starttime[0],         finishtime[1] - starttime[1]))

def cosscore(sharedwords,targetkey,testkey,dictionary): #where sharedwords is a list of the tuples containing shared words and their frequencies
    num=[]
    for word in sharedwords: #word will be the tuple with freq
        num.append(word[1]*word[2])
    num=sum(num)
    if num==0:
        return 0
    targetlist=list(dictionary[targetkey].values()) #list of freq of each key in targets profile
    testlist=list(dictionary[testkey].values()) #list of freq of each key in tests profile
    den1=sum([i**2 for i in targetlist])
    den2=sum([i**2 for i in testlist])
    den=math.sqrt(den1*den2)
    return (num/den)


def addict(word,sentence,bigdictionary,commlist): #where word is current word, sentence is current sentence and
    for wrd in sentence:                 #bigdict is the current bigdictionary and commlist is the blacklist
        if (word==wrd) or (remword(commlist,wrd)==1):
            continue
        else:
            try:
                bigdictionary[word][wrd]+=1
            except KeyError:
                bigdictionary[word][wrd]=1

def remword(remlist,word): #where remlist is a list of strings and word is the current word
    for wrd in remlist:
        if word==wrd:
            return 1
    return 0