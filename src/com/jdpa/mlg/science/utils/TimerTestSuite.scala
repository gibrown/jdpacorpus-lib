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
 * Test for Timer trait.
 */

package com.jdpa.mlg.science.utils

import org.junit._
import Assert._

class TimerTestSuite extends Timer {
  
  /**
   * Test method time run with a long loop.
   */
  @Test def testTimeRun(){ 
    val loop = { 
      var x=0L
      var i=0L
      while (i<1000000000L){
        x+=1
        i+=1
      }
      x
    }
    
    val stTime = System.currentTimeMillis
    val results = timeRun(loop)
    val tEnd = System.currentTimeMillis
    
    val tDiff = (tEnd-stTime)
    mLog.info("Total Time: " + results._2 + " " + tEnd + " - " + stTime + " = " + tDiff)
    
    assertEquals("Function output value",results._1,1000000000L)
    assertTrue("time " + results._2 + " less than " + tDiff,results._2<=tDiff)
    //assertTrue("time " + results._2 + " greater than 0.8*" + tDiff,results._2>tDiff*0.8)
  }

}
