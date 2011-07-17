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
 * The Class Descriptor represents ???
 * (NOTE: this is an old annotation that I (Greg) don't understand, if you want to understand this
 * talk to Jason Kessler.
 * 
 * 
 * @author      Gregory Ichneumon Brown
 * 
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class Descriptor extends Element {
  
  val mDescribes = new ListBuffer[ProbLink[Mention,Null]]


}
