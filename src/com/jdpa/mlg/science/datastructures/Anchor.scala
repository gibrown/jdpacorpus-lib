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
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see SuperTopic
 * @see CoReferenceGroup
 */
package com.jdpa.mlg.science.datastructures

@scala.serializable
class Anchor(at: AnchorType.Value) extends Element {

  val mType = at
  var mRefersToLeft: Anchor = null
  var mRefersToRight: Anchor = null
  var mCoRefGroup: CoReferenceGroup = null

  def toDebugString(): String = {
    mType.toString + " " + this.getStartOffset + " " + this.getEndOffset + " " + 
      this.length + " " + this(0).mText
  }
}
