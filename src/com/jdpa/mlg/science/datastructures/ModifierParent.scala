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
 * The Trait ModifierParent is an interface for the possible parents for a Modifier
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Modifier
 */
package com.jdpa.mlg.science.datastructures

@scala.serializable
trait ModifierParent {

  /**
   * Recursively calculates the sentiment score for this element on a particular Entity. 
   *
   * @param target The entity.
   * @return Double
   */
  def transferSentiment(target: Entity): Double;
}
