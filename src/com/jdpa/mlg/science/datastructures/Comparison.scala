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
 * The Class Comparison represents a comparison between two things in the text.  
 * 
 * There are four types of comparisons:
 * <ul>
 * <li> "X is better than Y":
 *   mMore = X,
 *   mLess = Y,
 *   mDim = SentimentExpression(better),
 *   mSame = False
 * <li> "X is more expensive than Y":
 *   mMore = X,
 *   mLess = Y,
 *   mDim = SentimentExpression(expensive),
 *   mSame = False
 * <li> "X has a better Z than Y":
 *   mMore = X,
 *   mLess = Y,
 *   mDim = SentimentExpression(Z),
 *   mSame = False
 * <li> "X competes against Y":
 *   mMore = Y,
 *   mLess = X,
 *   mDim = Null,
 *   mSame = True
 * </ul>
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo implement transferSentiment
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class Comparison extends Element with ModifierTarget {
  
  val mLess = new ListBuffer[ProbLink[Mention,Null]]
  val mMore = new ListBuffer[ProbLink[Mention,Null]]
  var mDimension: ComparisonDimension = null
  var mSame = false

  /**
   * Recursively calculates the sentiment score for this element on a particular Entity. 
   *
   * @param target The entity.
   * @return Double
   */
  def transferSentiment(target: Entity): Double = {
    0
  }

}
