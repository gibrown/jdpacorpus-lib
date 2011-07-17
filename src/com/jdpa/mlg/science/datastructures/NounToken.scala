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
 * The Class NounToken represents a token that has been determined to be a noun.
 * 
 * @author      Gregory Ichneumon Brown
 * @todo not currently used by tokenizer
 */
package com.jdpa.mlg.science.datastructures

@scala.serializable
class NounToken(off1: Long, off2: Long, txt: String, tc: Int) extends Token(off1, off2, txt, tc) {

  var mPerson = false
  var mNumber = false
  var mGender = GenderType.GT_NONE

}
