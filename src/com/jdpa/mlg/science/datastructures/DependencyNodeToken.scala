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
 * The Class DependencyNodeToken is any non-root node in a DependencyParseTree.
 * It is associated with a particular Token in the text.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see DependencyParseTree
 * @see Root
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class DependencyNodeToken(tok: Token,
                          sent: Sentence,
                          prob: Double,
                          head: DependencyParseNode,
                          typ: DependencyRelationType.Value) extends DependencyParseNode {
   
  val mHeads = new ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]]
  var mToken: Token = tok
  var mSentence: Sentence = sent

  mHeads += new ProbLink[DependencyParseNode,DependencyRelationType.Value](prob,head,typ)
    
  /**
   * Gets the list of heads for this node. 
   *
   * @return ListBuffer with ProbLinks to other nodes and associated probabilities.
   */
  def getHeads(): ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]] = {
    mHeads
  }
  
  /**
   * Gets the head at a particular index.
   *
   * @param idx Index of the head.
   * @return Tuple of (headNode,relationType)
   */
  def getHead(idx: Int): (DependencyParseNode,DependencyRelationType.Value) = {  
    (mHeads(idx).ref,mHeads(idx).typ)
  }

  /**
   * Gets the token associated with this node
   *
   * @return The token
   */
  def getToken(): Token = {
    mToken
  }

}
