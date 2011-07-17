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
 * The Class OPO (Other Persons Opinion) represents the opinion of someone other than the
 * author of the document.  
 * <p>
 * <ul>
 * <li>Simple Example: "Alice thinks its good": <br>
 *   mSource = Alice,
 *   mTarget = Null,
 * <li>Recursive Example: "Alice thinks that Bob thinks that its good":<br>
 *   Needs a series of OPO instances:
 *     mSource_1 = Alice,
 *     mTarget_1 = OPO(Bob, thinks),
 *     mSource_2 = Bob,
 *     mTarget_2 = Null
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo implement transferSentiment
 */ 
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class OPO extends Element with ModifierParent with OPOTarget with ModifierTarget {

  val mSources = new ListBuffer[ProbLink[Mention,Null]]
  val mTargets = new ListBuffer[ProbLink[OPOTarget,Null]]

  /**
   * Recursively calculates the sentiment score for this element on a particular Entity. 
   *
   * @param target The entity.
   * @return Double
   */
  def transferSentiment(target: Entity): Double = {
    0
  }

}
