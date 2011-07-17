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
 * The Class Root represents the root node of a dependency parse tree for a sentence.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see DependencyParseTree
 * @see DependencyParseNode
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class Root(sent:Sentence) extends Invisible with DependencyParseNode {

  var mSentence: Sentence = sent

  /**
   * Gets a list of ProbLinks to the heads of this node.  Since this is the root,
   * this will always return null
   *
   * @return null
   */
  def getHeads(): ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]] = {
    null
  }
  
  /**
   * Gets the heads of this node.  Since this is the root,
   * this will always return null
   *
   * @return null
   */
  def getHead(idx: Int): (DependencyParseNode,DependencyRelationType.Value) = {
    null
  }

  /**
   * Gets the token associated with this node
   *
   * @return null since this is the root
   */
  def getToken(): Token = {
    null
  }

}
