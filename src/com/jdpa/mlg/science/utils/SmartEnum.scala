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
 * SmartEnum adds methods to the scala Enumeration class to
 * aid in converting between strings and enumeration Values.
 * Each value has three representations: An integer, a String,
 * and a Value.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.utils


trait SmartEnum extends Enumeration with Log{

 /**
   * Converts a string into the matching enumeration value.
   * (Using a search, ie O(n))
   * 
   * @param str The string to convert
   *
   * @return The enumeration Value
   */  
  def getVal(str: String): Value = {
    val enumOpt = this.iterator.find(_.toString == str)
    if (enumOpt.isEmpty) fatal("Unable to find " + this.getClass.getName + " tag: " + str)
    enumOpt.get
  }


 /**
   * Converts an integer into the string for the corresponding Value.
   * (Using a search, ie O(n))
   * 
   * @param str The integer representation of the Value
   *
   * @return The string representation.
   */  
  def getNameByInt(i: Int): String = {
    val enumOpt = this.iterator.find(_.id == i)
    if (enumOpt.isEmpty) fatal("Unable to find " + this.getClass.getName + " id: " + i)
    enumOpt.get.toString
  }

 /**
   * Converts an integer into the Value representation
   * (Using a search, ie O(n))
   * 
   * @param str The integer representation of the Value
   *
   * @return The Value representation
   */  
  def getValByInt(i: Int): Value = {
    val enumOpt = this.iterator.find(_.id == i)
    if (enumOpt.isEmpty) fatal("Unable to find " + this.getClass.getName + " id: " + i)
    enumOpt.get
  }


 /**
   * Gets the number of elements in this enumeration.
   * 
   */  
  def length(): Int = {
    maxId + 1
  }
}
