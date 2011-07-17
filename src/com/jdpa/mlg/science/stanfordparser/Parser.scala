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
 *  Wrapper around the Stanford Parser for running in JDPA Corpus Document framework.
 *
 *  @author Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.stanfordparser

import java.util.{ArrayList,StringTokenizer}   
import java.io._
import scala.collection.mutable.ListBuffer

import com.jdpa.mlg.science.Platform

import com.jdpa.mlg.science.datastructures._
import com.jdpa.mlg.science.utils.{Log,Timer,ProcessDirectory}
import com.jdpa.mlg.science.readers.DelimTextReader

import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.parser.lexparser.LexicalizedParser



object Parser extends Platform with Timer with Log{
  var mParser: LexicalizedParser = null
  var mTokCnt = 0
  
  def main(args: Array[String]) {
    //Params.loadParams("stanfordparser.properties")
    //startup(args,Params,"decode")

//    val t = time(ProcessDirectory.processAllDirs( Params("TST_DIR"),
//                                                  Params("TST_EXT"),
//                                                  this.tagFile))
//    val seconds = t/1000.0;
//    info( "*** Decoding time = " + seconds            + " sec" );
//    info( "*** Speed         = " + (mTokCnt/seconds) + " tok/sec" );
   }
    
  /**
   * Runs POS tagging on a series of documents.
   * 
   * @param docs The list of documents to update
   */
  def run(docs: List[Document]): Unit = {
    //Params.loadParams("pos.properties")
    
    loadParser()
    docs.foreach{
      doc=>parseDocument(doc)
    }
  }
  
  /**
   * Load the parser to use
   * 
   */
  def loadParser(): Unit = {
    mParser = new LexicalizedParser("thirdparty/gpl/stanford-parser-2010-08-20/englishPCFG.ser.gz")
  }
  
    
 /**
  *  Parse a document and apply the results.
  *
  * @param doc The Document to parse.
  * @return The document.
  */
  def parseDocument(doc: Document): Document = {
    doc.mSentences.foreach{
      s=>parseSentence(s)
    }
    mTokCnt += doc.mTokens.length
    doc
  }
  
 
 /**
  * Parse a single sentence using the Stanford Parser.  Apply POS tags, and save the parse tree.
  * 
  * @param sent Sentence to parse.
  */   
  def parseSentence(sent: Sentence): Unit = {
    val words = new java.util.Vector[Span]

    //ugly and slow, but tired!
    sent.foreach{
      t=>
      words.add(t)
    }

    debug(sent.toString)
    debug(words.toString)
    
    val tree = mParser.apply(words)
    
    debug(tree.toString)

    //run perl phrase chunker on tree
    val wlist = runPhraseChunker(tree)

    if (wlist.length != sent.length) fatal ("Chunker output not correct length: \n" + 
                                            wlist.map(_.mkString(" ")).mkString("\n"))
    
    for(i<-0 until sent.length){
      val w = wlist(i)
      if (w.length < 8) error ("result line too short: " + wlist(i).toList.mkString(" "))
      val tok = sent(i).asInstanceOf[Token]
      var pos = w(4)
      
      if (pos.equals("COMMA")) pos = ","
      if (pos.equals("-LRB-")) pos = "("
      if (pos.equals("-RRB-")) pos = ")"
      
      tok.mPOS = new ProbLink(1.0,POS.getVal(pos),null)
      tok.mPhraseIOB = w(3)
      tok.mHeadWord = w(7)
      var idx = w(8)
      if (idx.contains('/')) idx = idx.takeWhile((c: Char)=>c != '/')
      if (!idx.equals("???"))
        tok.mHeadToken = sent(idx.toInt).asInstanceOf[Token]
      tok.mHeadFunc = w(6)
      tok.mPathToRoot = w(9)
      tok.mDepth = w(9).count((c: Char)=>c == '/')
      if (w(6).equals("NOFUNC")) tok.mHeadOfPhrase = false
      else tok.mHeadOfPhrase = true
    }
    
    sent.mStanfordTree = tree
    
    //old POS parsing code, now replaced by the phrase chunker
//    val leaves = tree.getLeaves().toArray(new Array[Tree](1))
//    if (leaves.length != sent.length){
//      fatal("Number of children (" + leaves.length + ") does not match words in sentence(" + sent.length + ")")
//    }
//    else{
//      for(i<-0 until leaves.length){
//        val pos = leaves(i).parent(tree).value
//        debug(pos)
//        sent(i).asInstanceOf[Token].mPOS = new ProbLink(1.0,POS.getVal(pos),null)
//      }
//      sent.mStanfordTree = tree
//    }
    
  }

  val PHRASE_CHUNKER_CMD = "thirdparty/commercial/phrase_chunker/chunklink_2-2-2000_for_conll.pl"
  val SENT_TMP_FILE = "tree_tmp.mrg"
    
  def runPhraseChunker(tree: Tree): ListBuffer[Array[String]] = {
    
    val cmd = PHRASE_CHUNKER_CMD + " " + SENT_TMP_FILE 
    
    //write input file
    val mBOS = new BufferedWriter(new FileWriter(SENT_TMP_FILE))
    val str = tree.pennString.replaceAll("\\[[-0-9\\.]+\\]","").replace("ROOT","").replace("  "," ")
    mBOS.write(str)
    mBOS.flush()
    mBOS.close()
    
    debug("phrase chunker> " + cmd)
    
    var result_str = ""

//    if (str.contains("(NP (CD 2009) (NNP New) (NNP Honda) (CD 2WD) (NN LX) (JJ 5-speed) (JJ automatic) (NN transmission) (NNS Grade))")){
//      val br_out = new BufferedReader(new FileReader(new File("tree_result.txt")))
//      var line_out: String = ""
//      while(line_out != null){
//          line_out = br_out.readLine()
//          if (line_out != null) result_str += line_out + "\n"      
//      }
//    }
//    else{
    
    try{
      val rt = Runtime.getRuntime()
      val proc = rt.exec(cmd)
      //val stderr = proc.getErrorStream()
      val stdout = proc.getInputStream()
      val br_out = new BufferedReader(new InputStreamReader(stdout))
      //val br_err = new BufferedReader(new InputStreamReader(stderr))
      var line_err: String = ""
      var line_out: String = ""
      var result_err = ""
      while ((line_out != null)){
        //line_err = br_err.readLine()
        line_out = br_out.readLine()
        //if (line_err != null) result_err += line_err + "\n"
        if (line_out != null) result_str += line_out + "\n"
      }
      val exitVal = proc.waitFor()
      if (exitVal != 0){
        error(result_err)
        fatal("Process exitValue is " + exitVal)
      }
      else{
        trace("Process exitValue is " + exitVal)
      }
    } catch {
      case e: Exception => fatal("running " + cmd + " failed",e)
    }
    //}

    debug(result_str)
    
    DelimTextReader.readFieldsFromString(result_str,"\\s+","").drop(2)
  }
  
}
