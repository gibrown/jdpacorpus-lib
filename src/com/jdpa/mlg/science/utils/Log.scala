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
 * Logging trait for mixing into other classes to provide logging functionality.
 * Logs can be controlled with log4j.properties file to turn on different message levels 
 * (TRACE=0,DEBUG=1,INFO=2,WARN=3,ERROR=4). All messages above the set threshold level
 * will be logged, any others will be ignored. Different classes can be set to different 
 * levels.  See log4j documentation or existing log4j.properties file for additional details.
 * 
 * @author      Gregory Ichneumon Brown
 * @see    slf4j
 * @see    log4j
 */
package com.jdpa.mlg.science.utils

import org.slf4j.{Logger, LoggerFactory}
import org.apache.log4j.PropertyConfigurator;

import java.lang.RuntimeException


trait Log {
  val mLog = LoggerFactory.getLogger(getClass.getName)	
    
  /**
   * Output TRACE level message to log/stdout.
   *
   * @param  message  output message
   */
  def trace(message:String): Unit =	mLog.trace(message)   

  /**
   * Output TRACE level message to log/stdout and print stacktrace from exception.
   *
   * @param  message  output message
   * @param  error  exception to print
   */
  def trace(message:String, error:Throwable): Unit = mLog.trace(message, error)
  	 

  /**
   * Output DEBUG level message to log/stdout.
   *
   * @param  message  output message
   */
  def debug(message:String): Unit = mLog.debug(message)

  /**
   * Output DEBUG level message to log/stdout and print stacktrace from exception.
   *
   * @param  message  output message
   * @param  error  exception to print
   */
  def debug(message:String, error:Throwable): Unit = mLog.debug(message, error)
	 
	 
  /**
   * Output INFO level message to log/stdout.
   *
   * @param  message  output message
   */
  def info(message:String): Unit = mLog.info(message)

  /**
   * Output INFO level message to log/stdout and print stacktrace from exception.
   *
   * @param  message  output message
   * @param  error  exception to print
   */
  def info(message:String, error:Throwable): Unit = mLog.info(message, error)
	 

  /**
   * Output WARN level message to log/stdout.
   *
   * @param  message  output message
   */
  def warn(message:String): Unit = mLog.warn(message)

  /**
   * Output WARN level message to log/stdout and print stacktrace from exception.
   *
   * @param  message  output message
   * @param  error  exception to print
   */
  def warn(message:String, error:Throwable): Unit = mLog.warn(message, error)

  
  /**
   * Output ERROR level message to log/stdout.
   *
   * @param  message  output message
   */
  def error(message:String): Unit = mLog.error(message)

  /**
   * Output ERROR level message to log/stdout and print stacktrace from exception.
   *
   * @param  message  output message
   * @param  error  exception to print
   */
  def error(message:String, error:Throwable): Unit = mLog.error(message, error)	 

  
  /**
   * Output ERROR level message to log/stdout, then force the program to exit
   * immediately
   *
   * @param  message  output message
   */
  def fatal(message:String): Unit = {
    mLog.error(message)
    throw new RuntimeException(message)
  }

  /**
   * Output ERROR level message to log/stdout and print stacktrace from exception,
   * then force the program to exit immediately.
   *
   * @param  message  output message
   * @param  error  exception to print
   */
  def fatal(message:String, error:Throwable): Unit = {
    mLog.error(message, error)
    throw error
  } 

}
