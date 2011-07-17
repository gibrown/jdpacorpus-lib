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
 * Utility trait for components to use when loading .properties files.
 * 
 * @author Gregory Ichneumon Brown
 *
 * @see pos.Params
 */
package com.jdpa.mlg.science.utils

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import scala.collection.mutable.HashMap


abstract class ParamUtils(defaults: List[(String,String)]) extends Log{

  val mParams = new HashMap[String,String]
  setValues(defaults)

  /**
   * Allows the Parameters to be accessed with Params(key) to get the value
   * Exits if the parameter does not exist.
   *
   * @param param The parameter to get the value of.
	*/
  def apply(param: String): String = {
    val opt = mParams.get(param)
    if (opt.isEmpty) fatal("Unable to access parameter " + param)
    opt.get
  }

  /**
   * Set the values of the parameters to those in the given list.
   * Used to set the default values.
   *
   * @param lst List of parameter,value pairs
	*/
  protected def setValues(lst: List[(String,String)]){
    lst.foreach{
      item=>mParams(item._1) = item._2
    }
  }

  /**
   * Print out all defined parameters into the log file.
   * 
	*/
  def printParams(){
    mParams.keys.foreach{
      k=>info(k + "=" + mParams(k))
    }
  }
  
  /**
   * Load the parameters from the given .properties file.  These values
   * will override the default ones.
   *
   * @param fname The .properties file name
	*/
  def loadParams(fname: String){
	 val properties = new Properties();
		
	  // Load the properties from the file
	  try{
		  properties.load( new FileInputStream( "config/" + fname ));
	  } catch {
      case ioe: IOException => fatal("Could not open properties file " + fname,ioe)
      case e: Exception => fatal("unhandled exception",e)
    }
		
    //overwrite param map with those from the properties file
    val iter = properties.propertyNames()
    while(iter.hasMoreElements()){
      val param = iter.nextElement().asInstanceOf[String]
      mParams(param) = properties.getProperty(param)
    }

  }


}
