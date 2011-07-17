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
 * The Class SentimentExpression represents a single expression of some sentiment in the text.
 * <p>
 * Examples: <ul>
 * <li> "The car has <u>good</u> handling": mTarget = handling, mSource=car, mParent = null
 * <li> "It has very <u>good</u> handling": mTarget = handling, mSource = it, mParent = very
 * </ul>
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo implement methods
 */ 
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class SentimentExpression extends Element with ModifierTarget with OPOTarget with ComparisonDimension {

  val mPriorIntensity = new ListBuffer[ProbLink[SentimentIntensity.Value,Null]]
  val mPriorPolarity = new ListBuffer[ProbLink[SentimentPolarity.Value,Null]]
  var mPriorScore = 0.0

  val mTargets = new ListBuffer[ProbLink[Mention,Null]]
  val mParents = new ListBuffer[ProbLink[Modifier,Null]]
  val mSources = new ListBuffer[ProbLink[Entity,Null]]
  val mLessImportantThan = new ListBuffer[ProbLink[SentimentExpression,Null]]
  val mMoreImportantThan = new ListBuffer[ProbLink[SentimentExpression,Null]]

  /**
   * Return the first polarity value for this expression or unknown
   * 
   * @return The sentiment polarity for this expression
   */
  def getPolarityVal(): SentimentPolarity.Value = {
    if (mPriorPolarity.length > 0){
      mPriorPolarity(0).ref
    }
    else SentimentPolarity.SP_UNKNOWN
  }
  
  /**
   * Gets the prior polarity as a real value.
   *
   * @return Double
   */
  def getPriorPolarity(): Double = {
    0
  }

  /**
   * Gets the contextual polarity as a real value;
   *
   * @return Double
   */
  def getContextualPolarity(): Double = {
   0.0
  }


  /* 
   * @see com.jdpa.datastructures.ModifierTarget#transferSentiment(com.jdpa.mlg.science.datastructures.Entity)
   */
  def transferSentiment(target: Entity): Double = {
    0.0;
  }

  override def delete(){
    while(mPriorIntensity.length > 0){
      mPriorIntensity.remove(0)
    }
    while(mPriorPolarity.length > 0){
      mPriorPolarity.remove(0)
    }

    while(mTargets.length > 0){
      mTargets.remove(0)
    }
    while(mParents.length > 0){
      mParents.remove(0)
    }
    while(mSources.length > 0){
      mSources.remove(0)
    }
    while(mLessImportantThan.length > 0){
      mLessImportantThan.remove(0)
    }
    while(mMoreImportantThan.length > 0){
      mMoreImportantThan.remove(0)
    }
    
    super.delete()
  }
  
}
