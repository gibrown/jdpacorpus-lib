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
 * The Enum TokenProperty enumerates all the possible forms of text decoration for a token.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object TokenProperty extends SmartEnum{

  val TP_NONE=Value
  val TP_ALL_UPPER_CASE=Value
  val TP_FIRST_UPPER_CASE=Value
  val TP_HAS_SYMBOL=Value
  val TP_HYPHEN=Value
  val TP_IS_SYMBOL=Value
  val TP_MIXED_CASE=Value

}
