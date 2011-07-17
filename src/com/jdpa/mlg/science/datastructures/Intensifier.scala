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
 * The Class Intensifier represents an intensifier on some other word.  It can either 
 * increase or decrease the score of sentiment.
 * <p>
 * Example: "much better"
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo implement methods
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class Intensifier extends Modifier {

  val mDirection = new ListBuffer[ProbLink[IntensifierDirection.Value,Null]]
  var mMultiplier = 0.0

  /**
   * Recursively calculate the sentiment for this Entity.
   *
   * @param target The Entity being targetted
   * @return Double
   */
  def transferSentiment(target: Entity): Double = {
    0
  }

  /**
   * Gets the multiplier, modified by the direction of this Intensifier.
   *
   * @return Double
   */
  def getMultiplier(): Double = {
    0
  }
  
 /**
   * Return the first direction value for this expression or none
   * 
   * @return The intensifier direction for this expression
   */
  def getDirVal(): IntensifierDirection.Value = {
    if (mDirection.length > 0){
      mDirection(0).ref
    }
    else IntensifierDirection.ID_NONE
  }


}
