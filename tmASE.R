
R version 3.1.2 (2014-10-31) -- "Pumpkin Helmet"
Copyright (C) 2014 The R Foundation for Statistical Computing
Platform: x86_64-w64-mingw32/x64 (64-bit)

R is free software and comes with ABSOLUTELY NO WARRANTY.
You are welcome to redistribute it under certain conditions.
Type 'license()' or 'licence()' for distribution details.

  Natural language support but running in an English locale

R is a collaborative project with many contributors.
Type 'contributors()' for more information and
'citation()' on how to cite R or R packages in publications.

Type 'demo()' for some demos, 'help()' for on-line help, or
'help.start()' for an HTML browser interface to help.
Type 'q()' to quit R.

[Previously saved workspace restored]

> ### Sep #1: Data Preparation:
> #
> mydata<-read.csv("C:/Temp4/OH_frac.csv")
> tmfracking<-as.data.frame(mydata)
> library(tm)
Loading required package: NLP
Warning messages:
1: package ëtmí was built under R version 3.1.3 
2: package ëNLPí was built under R version 3.1.3 
> myCorpus <- Corpus(VectorSource(tmfracking$text))
> #
> ### Step #2: Transforming Text
> #
> # convert to lower case
> myCorpus <- tm_map(myCorpus, content_transformer(tolower))
> # remove URLs
> removeURL <- function(x) gsub("http[[:alnum:][:punct:]]*", "", x)
> myCorpus <- tm_map(myCorpus, removeURL)
> # add two extra stop words: "available", "via" and "damn"
> myStopwords <- c(stopwords('english'), "available", "via", "damn")
> #
> #combine all combination for "frackin*" to fracking
> myCorpus<- tm_map(myCorpus, gsub, pattern="frackin", replacement="fracking")
> myCorpus<- tm_map(myCorpus, gsub, pattern="frackingg", replacement="fracking")
> myCorpus<- tm_map(myCorpus, gsub, pattern="frackfreeeu", replacement="fracking")
> myCorpus<- tm_map(myCorpus, gsub, pattern="frackfree", replacement="fracking")
> myCorpus<- tm_map(myCorpus, gsub, pattern="fracknation", replacement="fracking")
> myCorpus<- tm_map(myCorpus, gsub, pattern="frackingeu", replacement="fracking")
> #
> # remove stopwords from corpus
> myCorpus <- tm_map(myCorpus, removeWords, myStopwords)
> # remove punctuation
> myCorpus <- tm_map(myCorpus, removePunctuation)
> # strip whitespace
> myCorpus <- tm_map(myCorpus,stripWhitespace)
> # remove numbers
> myCorpus <- tm_map(myCorpus, removeNumbers)
> # plain text doc
> myCorpus <- tm_map(myCorpus, PlainTextDocument)
> # keep a copy of corpus to use later as a dictionary for stem completion
> myCorpusCopy <- myCorpus
> # stem words
> myCorpus <- tm_map(myCorpus, stemDocument)
> # inspect documents (tweets) numbered 11 to 15
> # inspect(myCorpus[11:15])
> # The code below is used for to make text fit for paper width
> for(i in 11:15){
+ cat(paste("[[",i,"]]",sep=""))
+ writeLines(strwrap(myCorpus[[i]],width=73))
+ }
[[11]]rt energyindepth chesapeak test waterless frack ohio well shale
[[12]]rt energyindepth chesapeak test waterless frack ohio well shale
[[13]]rt energyindepth chesapeak test waterless frack ohio well shale
[[14]]rt energyindepth chesapeak test waterless frack ohio well shale
[[15]]rt energyindepth chesapeak test waterless frack ohio well shale
> ##############################stemming words#############################
> #
> ### Step #3: Stemming Words
> # Define modified stemCompletion function, the original stemCom
> stemCompletion_mod <- function(x,dict=myCorpusCopy) {
+   PlainTextDocument(stripWhitespace(paste(stemCompletion(unlist(strsplit(as.character(x)," ")),dictionary=dict, type="shortest"),sep="", collapse=" ")))
+ }
> # apply the function to myCorpus
> myCorpus <- lapply(myCorpus, stemCompletion_mod, myCorpusCopy)
> myCorpus.dataframe <- data.frame(text=unlist(sapply(myCorpus, `[`, "content")), 
+     stringsAsFactors=F)
> #convert vector to corpus
> myCorpus <- Corpus(VectorSource(myCorpus))
> 
> 
> inspect(myCorpus[11:15])
<<VCorpus (documents: 5, metadata (corpus/indexed): 0/0)>>

[[1]]
<<PlainTextDocument (metadata: 7)>>
rt energyindepth chesapeake testing waterless fracking ohio well shale

[[2]]
<<PlainTextDocument (metadata: 7)>>
rt energyindepth chesapeake testing waterless fracking ohio well shale

[[3]]
<<PlainTextDocument (metadata: 7)>>
rt energyindepth chesapeake testing waterless fracking ohio well shale

[[4]]
<<PlainTextDocument (metadata: 7)>>
rt energyindepth chesapeake testing waterless fracking ohio well shale

[[5]]
<<PlainTextDocument (metadata: 7)>>
rt energyindepth chesapeake testing waterless fracking ohio well shale

> ###############################word cloud##############################
> #
> ### Step #4: Building a Term-Document Matrix
> #
> myTdm <- TermDocumentMatrix(myCorpus)
> myTdm
<<TermDocumentMatrix (terms: 275, documents: 124)>>
Non-/sparse entries: 1208/32892
Sparsity           : 96%
Maximal term length: 18
Weighting          : term frequency (tf)
> #
> ### Step #5: Inspect Frequent Words
> # 
> findFreqTerms(myTdm, lowfreq=1)
  [1] "Ärt"                "‚Äì"                "‚Ärt"              
  [4] "‚Äôs"               "accepts"            "acquisition"       
  [7] "activist"           "actonclimate"       "adopt"             
 [10] "alliancerepublican" "allow"              "amendment"         
 [13] "amp"                "analysis"           "announces"         
 [16] "another"            "apart"              "appchronicle"      
 [19] "ask"                "avalon"             "awesome"           
 [22] "backed"             "bag"                "ban"               
 [25] "becoming"           "begin‚Ä"            "benefits"          
 [28] "biased"             "bill"               "billycorriher"     
 [31] "biomethane"         "bites"              "bizjournals"       
 [34] "books"              "boom"               "buchanannews"      
 [37] "burpeesrfun"        "businesses"         "buyer"             
 [40] "can"                "cast"               "caused"            
 [43] "center"             "change"             "chesapeake"        
 [46] "chesterland"        "christinahagan"     "cities"            
 [49] "citizens"           "cleanwater"         "cleveland"         
 [52] "clevelandsc‚Ä"      "clevelandscene"     "closed"            
 [55] "clydearoo"          "columbus"           "communities"       
 [58] "companies"          "confirms"           "conflict"          
 [61] "considers"          "continues"          "control"           
 [64] "conwayjoanehe"      "costs"              "council"           
 [67] "court"              "cracked"            "csg"               
 [70] "date"               "deal"               "december"          
 [73] "decisions"          "deficiencies"       "demand"            
 [76] "discovered"         "discuss"            "dispatchalerts"    
 [79] "disposal"           "donrichardson"      "dontfrackmd"       
 [82] "door"               "doortje"            "drilling"          
 [85] "drinking"           "dust"               "earthquakes"       
 [88] "ease"               "editor"             "ellengilmer"       
 [91] "energyfromshale"    "energyindepth"      "energynewsblog"    
 [94] "energytomorrow"     "environment"        "estimated"         
 [97] "event"              "fed"                "ferc"              
[100] "fight"              "file"               "finds"             
[103] "flip"               "flops"              "flowback"          
[106] "forests"            "fracking"           "frackingeu"        
[109] "fri"                "friendly"           "gallons"           
[112] "gas"                "gasfrac"            "gatesmills"        
[115] "get"                "gibsondunn"         "going"             
[118] "governor"           "govts"              "groups"            
[121] "hall"               "halliburton"        "health"            
[124] "homerule"           "hosts"              "house"             
[127] "icymi"              "igsolidaritynet"    "incident"          
[130] "increases"          "individuals"        "innovation"        
[133] "inside"             "interest"           "jobs"              
[136] "judge"              "jumps"              "just"              
[139] "justice"            "kasich‚Äôs"         "keep"              
[142] "land"               "larryhogan"         "law"               
[145] "leaders"            "leaves"             "legal"             
[148] "legislative"        "legislature"        "letter"            
[151] "level"              "light"              "limits"            
[154] "links"              "local"              "loisgibbs"         
[157] "lose"               "lost"               "low"               
[160] "make"               "march"              "mdsenate"          
[163] "members"            "michigan"           "mixed"             
[166] "motion"             "much"               "municipal"         
[169] "natgas"             "natural"            "naturalgas"        
[172] "nswpol"             "nswpolrt"           "nswvotes"          
[175] "occupyytown"        "ohio‚Äôs"           "ohio"              
[178] "oil"                "ontario"            "oogahq"            
[181] "open"               "ordinances"         "organizations"     
[184] "others"             "othrs"              "ourcarbon"         
[187] "overturns"          "panel"              "parks"             
[190] "penalties"          "pennsylvania"       "people"            
[193] "percent"            "permit"             "pick"              
[196] "plan"               "politicians"        "preserves"         
[199] "press"              "prices"             "produced"          
[202] "production"         "projectcensored"    "prophets"          
[205] "proposed"           "protections"        "public"            
[208] "question"           "quijibo"            "raydowd"           
[211] "recent"             "recorder"           "regulate"          
[214] "reject"             "repdavidleland"     "report"            
[217] "rights"             "righttoknow"        "river"             
[220] "rtchej"             "rteidohio"          "rule"              
[223] "safe"               "safeguards"         "sand"              
[226] "saradorn"           "says"               "seeks"             
[229] "senator"            "shale"              "shalegasnow"       
[232] "shall"              "shocking"           "shows"             
[235] "shrinking"          "soars"              "speak"             
[238] "stand"              "state"              "stop"              
[241] "stopnuclearwar"     "stricter"           "strikes"           
[244] "stuartsmithlaw"     "supreme"            "swhitebear"        
[247] "take"               "tax"                "testing"           
[250] "thank"              "theathensnews"      "think"             
[253] "time"               "towards"            "town"              
[256] "townhall"           "trillion"           "trumbull"          
[259] "two"                "undisclosed"        "unregulated"       
[262] "utica"              "vienna"             "waste"             
[265] "water"              "waterless"          "wel‚Ä"             
[268] "well"               "wheelingjesuit"     "will"              
[271] "written"            "wrlc"               "year"              
[274] "youngstown"         "zanesville"        
> termFrequency <- rowSums(as.matrix(myTdm))
> termFrequency <- subset(termFrequency, termFrequency>=30)
> library(ggplot2)

Attaching package: ëggplot2í

The following object is masked from ëpackage:NLPí:

    annotate

Warning message:
package ëggplot2í was built under R version 3.1.3 
> qplot(names(termFrequency), termFrequency, geom="bar", stat="identity", xlab="Terms") + coord_flip()     
> #
> ### Step #6: Plot Words Cloud Sorted by Frequency
> library(wordcloud)
Loading required package: RColorBrewer
Warning message:
package ëwordcloudí was built under R version 3.1.3 
> m <- as.matrix(myTdm)
> #
> ### Step #7: Plot the most Frequent Words
> #
> # calculate the frequency of words and sort it descendingly by frequency
> v = sort(rowSums(m), decreasing=TRUE)
> d = data.frame(word=names(v), freq=v)
> #
> #word cloud
> wordcloud(d$word, d$freq, min.freq=30,random.color=TRUE, colors=rainbow(8))
 
