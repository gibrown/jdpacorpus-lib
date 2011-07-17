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
 * The class TextDocReader builds a Document from a text file, standard input, or flat string
 * that has no annotations in the text.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see    Document
 * @todo implement exception catching
 */
package com.jdpa.mlg.science.readers

import java.io._
import scala.io._

import com.jdpa.mlg.science.datastructures._
import com.jdpa.mlg.science.utils._

class TextDocReader(dname: String,str: String) extends Log {

  var mText = str  
  var mName = dname  

  
  def this(dname: String,file: File) = {
    this(dname,Source.fromFile(file,"utf-8").getLines.mkString("\n"))
  }
  def this(file: File) = {
    this("Unknown",file)
  }

  def this(dname: String,file: InputStream) = {
    this(dname,Source.fromInputStream(file,"utf-8").getLines.mkString)
  }
  def this(file: InputStream) = {
    this("Unknown",file)
  }

  def this(dname: String) = {
    this(dname,"")
  }

  def this() = {
    this("Unnamed","")
  }

  /**
   * Set the name of the file to read.
   */
  def readFileName(fname: String): Unit = {
    mText = Source.fromFile(fname,"utf-8").getLines.mkString("\n")
  } 

  /**
   * Creates a Document from the given flat text file.
   * 
   * @return A document with the text populated.
   */
  def create(): Document = {
    trace("Full Text:\n" + mText + "\n")
    val doc = new Document(mName,mText)
    doc
  }

  
  
}
