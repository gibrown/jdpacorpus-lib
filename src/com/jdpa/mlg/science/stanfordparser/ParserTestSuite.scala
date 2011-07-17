/**
 *  This code is distributed under the licensing terms of the JDPA Sentiment Corpus
 *  available at https://verbs.colorado.edu/jdpacorpus/JDPA-Sentiment-Corpus-Licence-ver-2009-12-17.pdf
 *
 *  To use this code be sure to sign and send in a license as described at
 *  http://verbs.colorado.edu/jdpacorpus
 * 
 *  The code is copyright J.D. Power and Associates and Gregory Ichneumon Brown, and
 *  is provided as is.
 *
 * Test Class for Stanford Parser interface.
 *
 */
package com.jdpa.mlg.science.stanfordparser

import org.junit._
import Assert._
import com.jdpa.mlg.science.datastructures._
import com.jdpa.mlg.science.tokenize.OpenNLPTokenize
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.ListMap
import com.jdpa.mlg.science.utils.Log

class ParserTestSuite extends Log{

  /**
   * Test simple set of sentences tagging.
   */
  @Test def testSimple(){ 
    val test_str = "The big fat cat sat upon my hat.  In a classroom evaluation, students learned about expert systems programming by reviewing a Mycin-like system represented in EXPLAINER. My dog also likes eating bananas."
    val pos_lst = List("DT","JJ","JJ","NN","VBD","IN","PRP$","NN",".",
                       "IN","DT","NN","NN",",","NNS","VBD","IN","JJ","NNS","NN","IN","VBG","DT","JJ","NN","VBN","IN","NNP",".",
                       "PRP$","NN","RB","VBZ","VBG","NNS",".")
    val hw_lst = List("cat","cat","cat","sat","sat","sat","hat","upon","sat","learned","evaluation",
                      "evaluation","In","learned","learned","learned","learned","programming",
                      "programming","about","learned","by","system","system","reviewing","system",
                      "represented","in","learned","dog","likes","likes","likes","likes","???",
                      "likes")
    val iob_lst = List("B-NP","I-NP","I-NP","I-NP","B-VP","B-PP","B-NP","I-NP","O","B-PP","B-NP",
                       "I-NP","I-NP","O","B-NP","B-VP","B-PP","B-NP","I-NP","I-NP","B-PP","B-VP",
                       "B-NP","I-NP","I-NP","B-VP","B-PP","B-NP","O","B-NP","I-NP","B-ADVP","B-VP",
                       "B-VP","B-ADJP","O")
    val hop_lst = List(false,false,false,true,true,true,false,true,false,true,false,false,true,
                       false,true,true,true,false,false,true,true,true,false,false,true,true,
                       true,true,false,false,true,true,true,true,true,false)
    val doc = OpenNLPTokenize.tokenizeString(test_str)
    
    Parser.run(List(doc))

    var i = 0
    for(i<-0 until pos_lst.length){
      debug("Checking word: " + doc.mTokens(i).mText)
      assertEquals(pos_lst(i),doc.mTokens(i).asInstanceOf[Token].mPOS.ref.toString)
      assertEquals(hw_lst(i),doc.mTokens(i).asInstanceOf[Token].mHeadWord)
      assertEquals(iob_lst(i),doc.mTokens(i).asInstanceOf[Token].mPhraseIOB)
      assertEquals(hop_lst(i),doc.mTokens(i).asInstanceOf[Token].mHeadOfPhrase)
    }
    
  }
  
}

