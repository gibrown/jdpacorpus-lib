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
 * Trait to provide easy timing of function calls in milliseconds.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.utils

trait Timer extends Log{

  /**
   * Runs a function and times how long it takes to run.  Returns the runtime,
   * throws out the result.
   *
   * @param  f  Function to run and time
   * @return time (in milliseconds) to run the function
   */
  def time[T](f: => T) = {
    val res = timeRun(f)
    res._2
  } 

  /**
   * Runs a function and times how long it takes to run.  Runtime is printed
   * (in milliseconds) to a log.
   *
   * @param  f  Function to run and time
   * @param  log slf4j Logger instance
   * @todo enable logger
   * @return the resulting value from running f
   */
  def timeLog[T](f: => T/*,log: Logger*/) = {
      val res = timeRun(f)
      info("Runtime: " + res._2)
      res._1
  } 

  
  /**
   * Runs a function and times how long it takes to run. Returns tuple.
   *
   * @param  f  Function to run and time
   * @return tuple (function results,runtime in milliseconds)
   */
  def timeRun[A](f: => A) = {
      val startTime = System.currentTimeMillis
      val result = f
      val tDiff = (System.currentTimeMillis - startTime)
      (result,tDiff)
  }
    
}
