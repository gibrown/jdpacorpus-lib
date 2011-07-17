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
 * The Class Committer expresses certainty, hedging
 * 
 * Example: "<u>may</u> have been a bad car","is <u>definitely</u> a bad car"
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo implement transferSentiment
 */
package com.jdpa.mlg.science.datastructures

import scala.None

@scala.serializable
class Committer extends Modifier{
   
  var mDirection: ProbLink[IntensifierDirection.Value,Null] = null

  /**
   * Recursively calculate the sentiment on the given entity with a modification for thissvn://dev-vm-subversion.apps.umbriacom.com/svn/repository committer.
   *
   * @param target The Entity being targetted
   * @return Double
   */
  def transferSentiment(target: Entity): Double = {
    0
  }
  
 /**
   * Return the first direction value for this expression or none
   * 
   * @return The intensifier direction for this expression
   */
  def getDirVal(): IntensifierDirection.Value = {
    if (mDirection != null){
      mDirection.ref
    }
    else IntensifierDirection.ID_NONE
  }

}
