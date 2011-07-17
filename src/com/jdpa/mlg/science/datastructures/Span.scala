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
 * The Class Span defines the start and end of a span of text for a particular document.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import _root_.edu.stanford.nlp.ling.HasWord

@scala.serializable
abstract class Span (off1: Long,off2: Long,txt: String) extends HasWord{

  val mStartOffset = off1
  val mEndOffset = off2
  val mText = txt
  var mDocument:Document = null
  
  override def word(): String = {
    //change paren's to -LRB- and -RRB- for stanford parser compatability
    if (mText.equals("(")) "-LRB-"
    else if (mText.equals(")")) "-RRB-"
    else {
      //replace any other parens so they don't get matched by the phrase parser
      //also replace any spaces in the text
      mText.replaceAll("\\(","-LRB-").replaceAll("\\)","-RRB-").replaceAll(" ","")
    }
  }
  
  override def setWord(w: String){
    //can't set diff text word
  }
  
  override def toString(): String = {
    return mText
  }
  
  def delete(){
    mDocument = null
  }
}
