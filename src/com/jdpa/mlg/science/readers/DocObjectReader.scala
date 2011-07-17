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
 * The class DocObjectReader reads a list of documents from an object file.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see    Document
 */
package com.jdpa.mlg.science.readers

import java.io._
import scala.io._

import com.jdpa.mlg.science.datastructures._
import com.jdpa.mlg.science.utils._

object DocObjectReader extends Log {

  def createDocs(fname: String): List[Document] = {
    val fis = new FileInputStream(fname)
    val ois = new ObjectInputStream(fis)
    
    val lst = ois.readObject.asInstanceOf[List[Document]]
    ois.close
    lst
  }

  def createDoc(fname: String): Document = {
    val fis = new FileInputStream(fname)
    val ois = new ObjectInputStream(fis)
    
    val lst = ois.readObject.asInstanceOf[Document]
    ois.close
    lst
  }
  
}
