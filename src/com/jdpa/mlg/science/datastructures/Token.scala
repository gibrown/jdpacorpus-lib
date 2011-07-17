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
 * The Class Token contains information about a span of text that has been determined to
 * be a particular token.
 * 
 * @author      Gregory Ichneumon Brown
 *  
 * @todo remove DepLink and DepRel fields when we switch Dep Parser to use real Dep Trees
 */
package com.jdpa.mlg.science.datastructures

//removed, proprietary JDPA enumeration for token class type (eg UPPER, CAP, etc)
//import com.jdpa.mlg.science.tokenize.TokenClass

import scala.collection.mutable.ListBuffer

@scala.serializable
class Token(off1: Long, off2: Long, txt: String, tc: Int) extends Span(off1,off2,txt) with MetaData{

  var mStem: String = ""  //The lemma of the surface text: mText
  var mPOS: ProbLink[POS.Value,Null] = null

  var mOrthography: List[TokenProperty.Value] = Nil
  var mTokenClass = tc

  var mNext: Token = null
  var mPrev: Token = null
  var mSentence: Sentence = null
  val mDepNode = new ListBuffer[ProbLink[DependencyNodeToken,DependencyRelationType.Value]]

  var mPhraseIOB = ""
  var mHeadWord = ""
  var mHeadToken: Token = null
  var mDepth = 0
  var mHeadFunc = ""
  var mPathToRoot = ""
  var mHeadOfPhrase = false
  
  var mLabel = ""  //general label that can be used as scratch space to label this token
  
  var mDepIdx = -1
  var mDepRel = ""
  var mDepHead = -1
  
  
  /**
   * Return the next token in the document.
   *
   * @return Token
   */
  def next(): Token = {
    mNext
  }

  /**
   * Return the previous token in the document.
   *
   * @return Token
   */
  def prev(): Token = {
    mPrev
  }

  /**
   * Get a particular dependency node associated with this token
   *
   * @return a dependency node
   */
  def getDepNode(idx: Int):  DependencyNodeToken = {
    mDepNode(idx).ref
  }
  
  override def delete(){
    mStem = null
    mPOS = null
    mOrthography = null
    mNext = null
    mPrev = null
    mSentence = null

    mPhraseIOB = null
    mHeadWord = null
    mHeadToken = null
    mHeadFunc = null
    mPathToRoot = null
    mLabel = null
    mDepRel = null

    super.delete()
    
  }
  
}
