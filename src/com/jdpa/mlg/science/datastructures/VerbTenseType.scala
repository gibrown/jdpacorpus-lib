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
 * The Enum VerbTenseType enumerates the possible tenses a verb could have.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object VerbTenseType extends SmartEnum{

  val VTT_NONE = Value  
  val VTT_FUTURE = Value
  val VTT_PRESENT = Value
  val VTT_PAST = Value

}
