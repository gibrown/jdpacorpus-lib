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
 * The Class ProbLink indicates a probabalistic link to another object. 
 * It contains a reference to another object, the probability that link is true,
 * and a type associated with that link.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

@scala.serializable
class ProbLink[R,T](p: Double,r:R,t:T){
  val prob = p
  val ref = r
  val typ = t
}
