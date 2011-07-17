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
 * The Enum IntensifierDirection enumerates the directions in which an Intensifier 
 * performs its modification.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Intensifier
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object IntensifierDirection extends SmartEnum {
  
  val ID_NONE = Value(0,"NONE")
  val ID_DOWNWARD = Value(1,"DOWNWARD")
  val ID_UPWARD = Value(2,"UPWARD")
}
