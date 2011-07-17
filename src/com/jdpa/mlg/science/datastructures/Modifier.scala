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
 * The Abstract Class Modifier represents a modifier on some expression.  The subclasses
 * implement specific cases for different modifiers.
 * <p>
 * Examples: "good dog","less expensive","not true",etc
 * 
 * @author      Gregory Ichneumon Brown
 * 
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
abstract class Modifier extends Element with ModifierTarget with ModifierParent with OPOTarget{

  val mTargets = new ListBuffer[ProbLink[ModifierTarget,Null]]
  val mParents = new ListBuffer[ProbLink[ModifierParent,Null]]
  val mSources = new ListBuffer[ProbLink[Entity,Null]]
  
  /**
   * Recursively calculates the sentiment score for this element on a particular Entity. 
   *
   * @param target The entity.
   * @return Double
   */
  def transferSentiment(target: Entity): Double;

  override def delete(){
    while(mTargets.length > 0){
      mTargets.remove(0)
    }
    while(mParents.length > 0){
      mParents.remove(0)
    }
    while(mSources.length > 0){
      mSources.remove(0)
    }

    super.delete()
  }
}
