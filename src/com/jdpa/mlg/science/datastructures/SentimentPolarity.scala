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
 * The Enum SentimentPolarity enumerates the possible polarities of a sentiment expression.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see SentimentExpression
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object SentimentPolarity extends SmartEnum {
  
  val SP_POSITIVE = Value(0,"Positive")
  val SP_NEGATIVE = Value(1,"Negative")
  val SP_MIXED = Value(2,"Mixed")
  val SP_NEUTRAL = Value(3,"Neutral")
  val SP_UNKNOWN = Value(4,"Unknown")
}
