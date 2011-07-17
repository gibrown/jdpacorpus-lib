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
 * The Object Document defines static methods and values used throughout the datastructures
 * for performing operations on all the objects.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.MultiMap
import scala.collection.mutable.ListBuffer
import java.io.FileOutputStream
import java.io.File
import java.io.Serializable

import com.jdpa.mlg.science.utils.Log


object Document {
  
  /**
   * Add a new Probabalistic link to the given list.
   * 
   * @param list The list to update
   * @param ref The reference the link goes to
   * @param typ The type of link
   * @param prob The probability (score) this link is true
   */
  def addToProbList[A <% AnyRef,B <% Any](list: ListBuffer[ProbLink[A,B]],ref: A,typ: B,prob: Double) {
    list += new ProbLink[A,B](prob,ref,typ)
  }

}


/**
 * The Class Document contains all the information about a particular document including
 * all of the results from tokenization, parsing, POS tagging, sentiment, entities, etc
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo implement additional search methods, findWord, etc
 * @todo implement elements, wordmap, entities
 * @todo decide whether to store mTokens as SpanSequence or current ListBuffer (performance impact)
 * @todo implement clearDepTrees method
 * @todo move the print* methods into some writer classes
 */
@scala.serializable
class Document(name: String,txt: String) extends MetaData with Log{

  val mName = name
  var mText = txt
  
  val mTokens = new SpanSequence
  val mSentences = new ListBuffer[Sentence]
  val mElements = new ListBuffer[Element]
  val mEntities = new  ListBuffer[Entity]
  val mCoRefGroups = new ListBuffer[CoReferenceGroup]
  
//  val mWordMap = new MultiMap[String,Token]


  /**
   * Clear the dependency parse trees throughout this document.
   *
   */
  def clearDepTrees(): Unit = {
    
  }
  
  
  /**
   * Find the list of Tokens corresponding to a single word (String)
   *
   * @param str The word as a string
   * @return List[Token]
   */
  def findWord(str: String): List[Token] =  {
    //mWordMap(str)
    null
  }
  
  /**
   * Returns the sentene number for this sentence in the document.
   * 
   * @param sent
   * @return
   */
  def getSentenceNum(sent: Sentence): Int = {
    var off = 0
    var s = mSentences.first
    while((s != sent) && (s != null)){
      s = s.mNext
      off += 1
    }
    if (s == null) -1
    else off
  }
  
  /**
   * Returns the list of sentences sorted by length
   *
   * @return List[Sentence]
   */  
  def sortByLength(): List[Sentence] = {
    mSentences.toList.sort( (x,y) => x.length < y.length )
  }
  
  /**
   * Print the tokens for the whole document to Stdout
   *
   * @return Unit
   */
  def printTokens(): Unit = {
    import java.io.FileDescriptor
    val ostream = new FileOutputStream(FileDescriptor.out)
    printTokens(ostream)
  }

  
  /**
   * Print the tokens for the whole document to a file
   *
   * @return Unit
   */
  def printTokens(fname: String): Unit = {
    val file = new File(fname)
    printTokens(file)
  }

  /**
   * Print the tokens for the whole document to a file
   *
   * @return Unit
   */
  def printTokens(file: File): Unit = {
    import java.io.FileDescriptor
    val ostream = new FileOutputStream(file)
    printTokens(ostream)
  }

  /**
   * Prints list of tokens broken up by sentences to a particular FileOutputStream
   *
   * @return Unit
   */
  def printTokens(fstream: FileOutputStream): Unit = {
    import java.io.PrintWriter
    val writer = new PrintWriter(fstream)
    mSentences.foreach {
      s=>s.foreach {
        t=>writer.println(t.mText +"\t"+ t.mStartOffset +"\t"+ 
            t.mEndOffset + "\t" + t.asInstanceOf[Token].mTokenClass)
      }
      writer.println()
    }
    writer.close;
  }

  def debugTokens(): Unit = {
    mTokens.foreach {
      t=>
      debug(t.mText + " " + t.mStartOffset + " " + t.mEndOffset + " ")
    }
  }

  /**
   * Print the POS tags for the whole document to a file
   *
   * @return Unit
   */
  def printPOSTags(fname: String): Unit = {
    val file = new File(fname)
    printPOSTags(file)
  }

  /**
   * Print the POS tags for the whole document to a file
   *
   * @return Unit
   */
  def printPOSTags(file: File): Unit = {
    import java.io.FileDescriptor
    val ostream = new FileOutputStream(file)
    printPOSTags(ostream)
  }

  /**
   * Prints list of POS tags broken up by sentences to a particular FileOutputStream
   *
   * @return Unit
   */
  def printPOSTags(fstream: FileOutputStream): Unit = {
    import java.io.PrintWriter
    val writer = new PrintWriter(fstream)
    mSentences.foreach {
      s=>s.foreach {
        t=>writer.println(t.mText +"\t"+ t.asInstanceOf[Token].mPOS.ref.toString)
      }
      writer.println()
    }
    writer.close;
  }
  
  def printDepParseTrees(): Unit = {
    mSentences.foreach {
      s=>
      debug("\n" + s.mDepTrees(0).ref.toString)
    }
  }
  
  def delete() {
    mTokens.delete()
    while(mSentences.length > 0){
      mSentences(0).delete()
      mSentences.remove(0)
    }
    
    while(mElements.length > 0){
      mElements(0).delete()
      mElements.remove(0)
    }

    while(mEntities.length > 0){
      mEntities(0).delete()
      mEntities.remove(0)
    }
    
    while(mCoRefGroups.length > 0){
      mCoRefGroups(0).delete()
      mCoRefGroups.remove(0)
    }
    
  }
  
  
}
