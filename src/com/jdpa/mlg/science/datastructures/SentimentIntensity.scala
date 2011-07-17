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
 * The Enum SentimentIntensity enumerates the possible intensity levels of a sentiment expression.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see SentimentExpression
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object SentimentIntensity extends SmartEnum{
  
  val SI_NONE = Value
  val SI_LOW = Value
  val SI_MEDLOW = Value
  val SI_MED = Value
  val SI_MEDHIGH = Value
  val SI_HIGH = Value
}
