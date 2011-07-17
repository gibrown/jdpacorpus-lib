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
 * Processes a Document instance and adds the Token and Sentence information to the Document.  
 * This tokenizer uses the default OpenNLP Sentence Splitter and Tokenizer
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see    Document
 */
package com.jdpa.mlg.science.tokenize

import java.io._
import com.jdpa.mlg.science.datastructures.{Document,Token,Sentence}
import com.jdpa.mlg.science.readers.TextDocReader
import scala.collection.immutable.HashSet

import com.jdpa.mlg.science.Platform 
import com.jdpa.mlg.science.utils.Timer
import com.jdpa.mlg.science.utils.Log

import opennlp.tools.sentdetect._
import opennlp.tools.tokenize._
import opennlp.tools.util.Span


object OpenNLPTokenize extends Platform with Timer with Log{
  
  var mTokCnt = 0

  var mSentModel: SentenceModel = null
  var mSentDetector: SentenceDetectorME = null

  var mTokenModel: TokenizerModel = null
  var mTokenDetector: TokenizerME = null
  
  var mSentSplitOnCarriageReturns = true
  
  def loadSentModel(){
    val modelIn = new FileInputStream("thirdparty/gpl/opennlp-tools-1.5.0/en-sent.bin");
    
    try {
      mSentModel = new SentenceModel(modelIn);
    }
    catch {
      case e:IOException => e.printStackTrace(System.out); System.exit(-1)
    }
    finally {
      if (modelIn != null) {
        try {
          modelIn.close();
        }
        catch{
          case e:IOException => None
        }
      }
    }  
    
    mSentDetector = new SentenceDetectorME(mSentModel);
  }


  def loadTokenModel(){
    val modelIn = new FileInputStream("thirdparty/gpl/opennlp-tools-1.5.0/en-token.bin");
    
    try {
      mTokenModel = new TokenizerModel(modelIn);
    }
    catch {
      case e:IOException => e.printStackTrace(System.out); System.exit(-1)
    }
    finally {
      if (modelIn != null) {
        try {
          modelIn.close();
        }
        catch{
          case e:IOException => None
        }
      }
    }  
    
    mTokenDetector = new TokenizerME(mTokenModel);
  }
  
  /**
   * Takes a Document instance, and use the OpenNLP Sentence Splitter and Tokenizer
   * to tokenize the text.
   * 
   * Results are put into the Document.mTokens datastructure.
   *
   * @param doc The Document instance to process and update.
   * @return The Document instance
   */  
  def process(doc: Document): Document = {
    if (mSentDetector == null) loadSentModel
    if (mTokenDetector == null) loadTokenModel
    
    var sent = new Sentence;
    var sent_num = 0
    var last_sent: Sentence = null;
    var t: Token = null;
    var last_t: Token = null;
    
    val sents = mSentDetector.sentDetect(doc.mText);
    val sent_spans = mSentDetector.sentPosDetect(doc.mText);

    for(i <- 0 until sents.length){
      var offStart = sent_spans(i).getStart
      var offEnd = sent_spans(i).getEnd

      var sub_sent = sents(i).split("\n")
      if (!mSentSplitOnCarriageReturns) sub_sent = Array(sents(i))
      
      for(k <- 0 until sub_sent.length){
        val sent_txt = sub_sent(k)
        if (k < sub_sent.length - 1){
          offEnd = offStart + sub_sent.length + 1          
        }
        else{
          offEnd = sent_spans(i).getEnd
        }
                 
        
        val tokens = mTokenDetector.tokenize(sent_txt);
        val token_spans = mTokenDetector.tokenizePos(sent_txt);
        if (tokens.length != 0){
          for(j <- 0 until tokens.length){
            var txt = tokens(j)
            var txt_start = token_spans(j).getStart + offStart
            var txt_end = token_spans(j).getEnd + offStart
            trace("Add token: " + txt + " " + txt_start)
    
            //split off starting symbol into separate token
            if (txt.matches("^(\\[|\\]|\"|\\.|/|-|\\(|:|;|\\{)\\p{Alnum}.*") ||
                txt.matches("^'((\\p{Digit})|(\\p{Alpha}\\{Alpha}\\{Alpha}+)).*") ||
                txt.matches("^,\\p{Lower}.*")
                ){
              if (!txt.equals("'s")){
                trace("Split off first char.")
                last_t = addToken(last_t,sent,doc,
                                  txt.take(1),txt_start,txt_start+1)
                txt = txt.drop(1)
                txt_start += 1
              }
            }
            
            //split off starting elipsis ...
            if (txt.matches("^\\.\\.\\.\\p{Alpha}.*")){
              trace("Split off \"...\"")
              last_t = addToken(last_t,sent,doc,
                                txt.take(3),txt_start,txt_start+3)
              txt = txt.drop(3)
              txt_start += 3
            }
            //split off starting elipsis ..
            if (txt.matches("^\\.\\.\\p{Alpha}.*")){
              trace("Split off \"..\"")
              last_t = addToken(last_t,sent,doc,
                                txt.take(2),txt_start,txt_start+2)
              txt = txt.drop(2)
              txt_start += 2
            }
            
            // num.caplower
            // num-caplower
            // lower-cap
            // lower.Cap
            // alpha+alpha
            if (txt.matches("\\p{Digit}+(\\.|-)\\p{Upper}\\p{Lower}+.*") ||
                txt.matches("\\p{Upper}?\\p{Lower}+(\\.|-)\\p{Upper}\\p{Lower}+.*") ||
                txt.matches("\\p{Alpha}+(\\+|;)\\p{Alnum}+.*")){
              trace("Splitting middle.")
              val strs = txt.split("(\\.|-|\\+|;)",2)
             
              //add first token
              last_t = addToken(last_t,sent,doc,
                                strs(0),txt_start,txt_start+strs(0).length)
              txt_start += strs(0).length

              //add hyphen, or period
              txt = txt.drop(strs(0).length)
              last_t = addToken(last_t,sent,doc,
                                txt.take(1),txt_start,txt_start+1)

              if (txt.matches(".+\\p{Lower}\\..+")){
                trace("*******end of sentence")
                doc.mSentences += sent
                sent.mSentNum = sent_num
                sent_num += 1
                last_sent = sent
                sent = new Sentence;
                sent.mPrev = last_sent
                last_sent.mNext = sent
              }
              
              txt_start += 1
              txt = txt.drop(1)
            }

            //split on hyphenation, etc (but not if capitals on either side of -)
            // [alpha|num]-$
            if (txt.matches(".*[\\p{Lower}\\p{Digit}]+-[\\p{Lower}\\p{Digit}]+.*") ||
                txt.matches(".*\\p{Alnum}+[:/\\]\\[\\)\\(]\\p{Alnum}+.*") ||
                txt.matches("\\p{Digit}+(\\.|-)\\p{Upper}\\p{Lower}+.*") ||
                txt.matches("\\p{Alnum}+-\\$\\p{Digit}+.*")){
              if (! txt.matches(".+/.+/.+")){
                trace("Splitting middle.")
                val strs = txt.split(":|/|-|\\[|\\]|\\)|\\(",2)
      
                //add first token
                last_t = addToken(last_t,sent,doc,
                                  strs(0),txt_start,txt_start+strs(0).length)
                txt_start += strs(0).length
                      
                //add hyphen, etc
                txt = txt.drop(strs(0).length)
                last_t = addToken(last_t,sent,doc,
                                  txt.take(1),txt_start,txt_start+1)
                txt_start += 1
                txt = txt.drop(1)
              }
            }

            // lower--lower
            if (txt.matches("\\p{Lower}+--\\p{Lower}+")){
                trace("Splitting middle.")
                val strs = txt.split("--",2)
      
                //add first token
                last_t = addToken(last_t,sent,doc,
                                  strs(0),txt_start,txt_start+strs(0).length)
                txt_start += strs(0).length
                      
                //add hyphen, etc
                txt = txt.drop(strs(0).length)
                last_t = addToken(last_t,sent,doc,
                                  txt.take(2),txt_start,txt_start+2)
                txt_start += 2
                txt = txt.drop(2)
            }
            
            // alpha...alpha
            if (txt.matches("\\p{Alpha}+\\.\\.\\.\\p{Alpha}+")){ 
                trace("Splitting middle.")
                val strs = txt.split("\\.\\.\\.",2)
      
                //add first token
                last_t = addToken(last_t,sent,doc,
                                  strs(0),txt_start,txt_start+strs(0).length)
                txt_start += strs(0).length
                      
                //add hyphen, etc
                txt = txt.drop(strs(0).length)
                last_t = addToken(last_t,sent,doc,
                                  txt.take(3),txt_start,txt_start+3)
                txt_start += 3
                txt = txt.drop(3)
            }
                
            //split multi
            // alpha/alpha/alpha
            // lower-lower-lower-lower
            if (txt.matches("^\\p{Alnum}+(-|\\/)(\\p{Alnum}+(-|\\/))+\\p{Alnum}+$")){ 
                trace("Splitting multi.")
                val strs = txt.split("[-/]")

                for(m <- 0 until strs.length-1){
                  last_t = addToken(last_t,sent,doc,
                                    strs(m),txt_start,txt_start+strs(m).length)
                  txt_start += strs(m).length
                  txt = txt.drop(strs(m).length)
                        
                  //add hyphen, etc
                  last_t = addToken(last_t,sent,doc,
                                    txt.take(1),txt_start,txt_start+1)
                  txt_start += 1
                  txt = txt.drop(1)
                }
            }



            // numlower  (eg 123hp, 4,400mph)
            // numCap (eg 2.0L)
            if (txt.matches("^\\p{Digit}+((\\.|,)\\p{Digit}+)?\\p{Lower}\\p{Lower}+$") ||
                txt.matches("^\\p{Digit}+\\.\\p{Digit}+?\\p{Upper}$")){ 
              if (!txt.endsWith("th") && !txt.endsWith("st")){
                trace("Splitting presumed num unit combo.")
                val strs = txt.split("\\p{Alpha}",2)
      
                //add first token
                last_t = addToken(last_t,sent,doc,
                                  strs(0),txt_start,txt_start+strs(0).length)
                txt_start += strs(0).length
                txt = txt.drop(strs(0).length)                      
              }
            }

            
            
            //split off "..."
            if (txt.matches("\\p{Alnum}+\\.\\.\\.$")){
              trace("Split off \"...\"")
              last_t = addToken(last_t,sent,doc,
                                txt.dropRight(3),txt_start,txt_end-3)
              txt = txt.takeRight(3)
              txt_start = txt_end - 3
            }
            
            
            //split off ending symbol into separate token
            if (txt.matches(".*\\p{Alnum}(\\.|-|\\)|/|,|\"|'|\\[|\\]|:|\\()$")){
              trace("Split off last char.")
              last_t = addToken(last_t,sent,doc,
                                txt.dropRight(1),txt_start,txt_end-1)
              txt = txt.takeRight(1)
              txt_start = txt_end - 1
            }
            
            
            last_t = addToken(last_t,sent,doc,
                              txt,txt_start,txt_end)
          }
          
          
          trace("*******end of sentence")
          doc.mSentences += sent
          sent.mSentNum = sent_num
          sent_num += 1
          last_sent = sent
          sent = new Sentence;
          sent.mPrev = last_sent
          last_sent.mNext = sent
        }

        offStart += sent_txt.length + 1
        
      }
    }
            
    doc
  }
  

  def addToken(lastTok: Token,
               sent: Sentence, doc: Document,
               txt: String, start: Int, end: Int): Token = {
    val t = new Token(start,
                      end,
                      txt,0)
    t.mSentence = sent
    t.mStem = t.mText 
    t.mPrev = lastTok
    val len = t.mText.length - 2
    if (len > 4){
      t.mStem = t.mText.toLowerCase.take(len)  //simple, dumb stemmer
    }
    else{
      t.mStem = t.mText.toLowerCase
    }
    if (lastTok != null) lastTok.mNext = t
    doc.mTokens += t
    sent += t
    mTokCnt += 1          
    t
  }
  
  
  /**
   * Takes a filename, creates a new Document and tokenizes the document.
   *
   * @param fname The file to grab the text from
   * @return A tokenized Document instance
   */  
  def tokenizeFile( fname: String ): Document = {
    val doc = (new TextDocReader(fname,new File(fname))).create
    process(doc)
  }

  
  /**
   * Takes a file from stdin, creates a new Document and tokenizes the document.
   *
   * @return A tokenized Document instance
   */  
  def tokenizeStdIn(): Document = {
    val doc = (new TextDocReader("User Std In",System.in)).create
    process(doc)
  }
  
  
  /**
   * Takes a string, creates a new Document from the String,
   * and tokenizes the document.
   *
   * @param str The string to process
   * @return A tokenized Document instance
   */  
  def tokenizeString( str: String ): Document = {
    val doc = new Document("User String",str)
    process(doc)
  }

  /**
   * Tokenizes a series of documents.
   * 
   * @param docs The list of documents to update
   */
  def run(docs: List[Document]): Unit = {
    docs.foreach{
      doc=>process(doc)
    }
  }
    
  def main(args: Array[String]) {
    Params.loadParams("token.properties")
    startup(args,Params,"tokenize")
    
    val t = time(tokenizeFile( Params("FILE") ).printTokens(Platform.resultsPath + "/token/tokens.out"))
    val seconds = t/1000.0;
    info( "*** Tokenization time = " + seconds            + " sec" );
    info( "*** Speed         = " + (mTokCnt/seconds) + " tok/sec" );
    info("Done.")
  }//~main

}
