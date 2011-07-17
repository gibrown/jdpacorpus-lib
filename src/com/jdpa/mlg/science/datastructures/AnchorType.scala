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
 *  The Enum AnchorType defines the type of a topic's annotation.
 * 
 *  @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object AnchorType extends SmartEnum{
  val AT_UNKNOWN = Value("UNKNOWN")
  val AT_SUPERTOPIC = Value("SUPERTOPIC")
  val AT_SUBTOPIC = Value("SUBTOPIC")
  val AT_SUBSUBTOPIC = Value("SUBSUBTOPIC")
}
