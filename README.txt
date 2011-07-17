J.D. Powers and Associates Sentiment Corpus Library
====================================================
code version: 1.0
code release date: 
code repository: 
JDPA Corpus: http://verbs.colorado.edu/jdpacorpus
contact: Gregory Ichneumon Brown (browngp (-at-) colorado edu)
===================================================
Licensing

This code is distributed under the licensing terms of the
JDPA Sentiment Corpus available at https://verbs.colorado.edu/jdpacorpus/JDPA-Sentiment-Corpus-Licence-ver-2009-12-17.pdf

To use this code be sure to sign and send in a license as described at
http://verbs.colorado.edu/jdpacorpus

The code is copyright J.D. Power and Associates and Gregory Ichneumon Brown, and
is provided as is.  If you have useful changes/bugfixes then we would love contributions
to the library.  Unfortunately I (Greg) am unsure whether we will ever be able to get JDPA
to change the license to a better open source license.  But you're probably only using this
library if you agree to the corpus license anyways.

===================================================
This library was started while working for JDPA in 2010, and then expanded on for my
thesis.  Because of the code history, some pieces may not be as clean as they could be,
and may appear overly complex.  Sorry.

Pretty much all code is written in Scala, so you should be able to use this libary from any
JVM language, but I've only ever tested it by calling from Scala.

-----------------------------------
Quick and Dirty How To
-----------------------------------

To load a file from the corpus:
XXX

To load a list of files from the corpus:
XXX

The corpus documents by default get tokenized by the default OpenNLP tokenizer and sentence splitter 
with a number of regexp that I used to post process for my thesis.  (You should seriously consider
changing this, and someday maybe the code should be modified to support an arbitrary tokenizer.)

There is included code for running the Stanford Parser (though you'll have to download it yourself - version XXX).
Based on my results, I would suggest using a different parser though, blog data is hard. :)  But to run the stanford parser
call:
XXX

The corpus documents get extracted into the the myriad datastructures, see doc/apidocs or the code for a description.  There
is also a somewhat out of date UML diagram at doc/datastructures.zargo


If you need code changes, better documentation etc, feel free to contact me, I'm
currently in a "just get something online" mode, but could certainly put some more time into
cleaning up the code base if there is some demand.  

-----------------------------------
Summary of Directory Structure
-----------------------------------


   ./
   |-bin/                                All scripts for running common pieces.
   |---run.sh                            --run a class from command line with arguments (via ant)
   |---run_scala.sh                      --run a class from command line with arguments (via scala command line)
   |---classpath.sh                      --classpath used for run_scala.sh
   |-build.xml                           main ant build script
   |-classes/                            .class files get compiled to here (DO NOT CHECKIN)
   |-config/                             files to control the system
   |---science.properties                --common control file
   |---log4j.properties                  --logging control file
   |-doc/
   |---apidoc/                           --scaladoc generated documentation
   |jdpacorpus-lib.jar                   compiled library jar file for inclusion in other projects
   |-log/                                system log files get generated here (DO NOT CHECK IN)
   |---<EXPNAME>.<process>.science.log   --root level log (set <EXPNAME> name in science.properties)
   |---<EXPNAME>.<process>.token.log     --tokenizer specific log 
   |                                        (<process> set automatically in top leve main function 
   |                                         to indicate what is being run: eg decode,train)
   |-output/                           Output files from experiment runs get output here (DO NOT CHECK IN)
   |---<EXPNAME>/                       --<EXPNAME> run results
   |-----token/
   |-src/                               Main source tree
   |---com/
   |-----jdpa/
   |-------mlg/
   |---------science/
   |-----------datastructures/          ----Document representation classes
   |-----------readers/                 ----Main Sentiment Corpus Reader, and a few misc file readers
   |-----------tests/                   ----Unit Test Suites (ind tests in same pkg as class being tested)
   |-----------tokenize/                ----OpenNLP Tokenizer Wrapper
   |-----------utils/                   ----Logging, Timer, System parameters, other generic utilities
   |-----------writers/                 ----Writing data out to various formats
   |-thirdparty/                        Third Party Libraries (broken down by license type)
   |---commercial/                      --commercial/Apache/not restricted distribution licenses
   |-----log4j/
   |-----scalatest-1.0/
   |-----slf4j-1.6.0/
   |---scala/                           --scala binaries for compiling
   |-----scala-2.7.7.final/             ----scala 2.7.7 (hopefully no longer needed)
   |-----scala-2.8.0.RC6/               ----scala 2.8 RC6 (should get upgraded soon to real 2.8 release)


-----------------------------------
Compilation/Building/Setup
-----------------------------------
-Modify config/science.properties to give yourself an experiment name (EXPNAME=test-20100617)
-Compile with:> ant -f build/build.xml compile 
(NOTE: if you run out of stack space you may need to define: ANT_OPTS=-Xss2M to increase the stack size
-Make doc/apidoc:> ant -f build/build.xml apidoc
-Compile, generate apidoc, and create lib/science.jar:> ant -f build/build.xml

-----------------------------------
Run All Unit Tests
-----------------------------------
>ant -f build/build.xml test

------------------
Run Tokenization
------------------
customize file locations with config/token.properties
> bin/run_scala.sh tokenize.Tokenize  [args]

