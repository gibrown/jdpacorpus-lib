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
 * The trait ModifierTarget is an interface for the classes a modifier can target.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Modifier
 */
package com.jdpa.mlg.science.datastructures

@scala.serializable
trait ModifierTarget {
    
  /**
   * Recurse down the tree of modifier targets and accumulate the total sentiment score.
   *
   * @param target The entity being targetted.
   * @return Double the sentiment score
   */
  def transferSentiment(target: Entity): Double;

}
