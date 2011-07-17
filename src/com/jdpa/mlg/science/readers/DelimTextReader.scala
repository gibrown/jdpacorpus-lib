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
 * The singleton class DelimTextReader reads a file and splits all the lines in the file
 * using the provided delimiter.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @todo allow skipping lines
 */
package com.jdpa.mlg.science.readers

import java.io._
import scala.io.Source
import java.util.StringTokenizer

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex
import java.util.regex.Pattern

import com.jdpa.mlg.science.utils.Log
import java.util.StringTokenizer

object DelimTextReader extends Log{

  /**
   * Read in a file ignoring comments and split/tokenize each line by the given delimitter.
   * This function tosses away any empty lines.
   * 
   * @param fname The file name
   * @param delim The string to use when splitting lines
   * @param commentStr The string that preceeds a comment in the file
   * @return A list of arrays.  Each element of the list corresponds to a single non-empty line, and each entry in the array corresponds to a token.
   */
  def readFields(fname: String,delim: String,commentStr: String): ListBuffer[Array[String]] = {
    val fields = new ListBuffer[Array[String]]
    val comReg = (commentStr + ".*$").r
    val nwsRegex = new Regex("""\S""")
    try{
      val src = Source.fromFile(new File(fname),"utf-8")

      src.getLines.foreach {      
        line: String =>
        val stripLine = comReg.replaceFirstIn(line.stripLineEnd,"")
        if (nwsRegex.findFirstIn(stripLine).isDefined){  //has non-whitespace
          fields += stripLine.split(delim)
        }
      }
    } catch {
      case e: Exception => fatal("Could not open file: " + fname)
    }    
    fields
  }
  
  /**
   * Read in a string ignoring comments and split/tokenize each line by the given delimitter.
   * This function tosses away any empty lines.
   * 
   * @param str The string to parse
   * @param delim The string to use when splitting lines
   * @param commentStr The string that preceeds a comment in the file
   * @return A list of arrays.  Each element of the list corresponds to a single non-empty line, and each entry in the array corresponds to a token.
   */
  def readFieldsFromString(str: String,delim: String,commentStr: String): ListBuffer[Array[String]] = {
    val fields = new ListBuffer[Array[String]]
    val comReg = (commentStr + ".*$").r
    val nwsRegex = new Regex("""\S""")
    val lines = str.split("\n")
    val stripComment = !commentStr.equals("")
    lines.foreach {      
      line: String =>
      var stripLine = ""
      if (stripComment)
        stripLine = comReg.replaceFirstIn(line.stripLineEnd,"")
      else
        stripLine = line
      if (nwsRegex.findFirstIn(stripLine).isDefined){  //has non-whitespace
        fields += stripLine.split(delim)
      }
    }
    fields
  }
  
  /**
   * Read in a file and split/tokenize each line by the given delimitter.
   * 
   * @param fname The file name
   * @param delim The string to use when splitting lines
   * @return A list of arrays.  Each element of the list corresponds to a single line, and each entry in the array corresponds to a token.
   */
  def readFields(fname: String,delim: String): ListBuffer[Array[String]] = {
    var fields: ListBuffer[Array[String]] = null
    try{
      fields = readFields(new File(fname),delim)
    } catch {
      case e: Exception => fatal("Could not open file: " + fname)
    }    
    fields
  }
  
  /**
   * Read in a file and split/tokenize each line by the given delimitter.
   * 
   * @param fname The File descriptor for the file
   * @param delim The string to use when splitting lines
   * @return A list of arrays.  Each element of the list corresponds to a single line, and each entry in the array corresponds to a token.
   */
  def readFields(file: File,delim: String): ListBuffer[Array[String]] = {
    val fields = new ListBuffer[Array[String]]
    try {
      val src = Source.fromFile(file,"utf-8")

      src.getLines.foreach {      
        line: String =>
        val stripLine = line.stripLineEnd
        fields += stripLine.split(delim)
      }
    } catch {
      case e: Exception => fatal("File " + file.getPath + " could not be accessed.")
    }
    fields
  }
  
}
