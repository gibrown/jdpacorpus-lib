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
 * The Enum VerbAspectType enumerates the possible aspects a verb can have.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object VerbAspectType extends SmartEnum{

  val VAT_NONE = Value
  val VAT_PERFECT = Value
  val VAT_PERFECT_PROGRESSIVE = Value
  val VAT_PROGRESSIVE = Value
  val VAT_SIMPLE = Value

}
