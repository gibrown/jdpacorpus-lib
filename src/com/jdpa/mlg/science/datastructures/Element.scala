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
 * The abstract class Element is a sequence of spans in the document that have been annotated
 * in a particular way.  The Element can have a probability indicating how likely this
 * annotation is to be true.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.Log

@scala.serializable
abstract class Element extends SpanSequence with MetaData with Log {

  var mProbabilityOfType = 0.0 

  var mSentence: Sentence = null 
  var mSentNum = -1
  var mMultiSent = false
  var mHasProb = false

  
  /**
   * Populate a contiguous span sequence with the appropriate tokens based upon its offsets.
   * 
   * @param stoff Starting offset for the span sequence
   * @param endoff Ending offset for the span sequence
   * @param doc The document this span sequence is in.
   */
  def buildSpanSequence(stoff: Long,
                        endoff: Long,
                        doc: Document){
    var tok = doc.mTokens.findOffset(stoff).asInstanceOf[Token]
    if (tok == null){
      fatal("no token corresponds to the starting offset of " + stoff)
    }
    this.mSentence = tok.mSentence
    this.mSentNum = doc.getSentenceNum(this.mSentence)
    
    while((tok != null) && (tok.mStartOffset < endoff)){
      this += tok
      if (mSentence != tok.mSentence) this.mMultiSent = true
      tok = tok.next()
    }
  }
  
  
  /**
   * Populate a contiguous span sequence with the appropriate tokens based upon a starting token,
   * and the total number of tokens in the sequence
   * 
   * @param tok The starting token
   * @param num The number of tokens in the sequence
   */
  def buildSeq(tok: Token,num: Int){
    var currtok = tok
    var i = num
    while((currtok != null) && (i>0)){
      this += currtok
      currtok = currtok.next()
      i -= 1
    }    
  }
  
  override def delete(){
    mSentence = null 
    super.delete()
  }

}
