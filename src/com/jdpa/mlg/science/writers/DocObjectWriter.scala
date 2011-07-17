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
 * The class DocObjectWriter writes a list of documents and all its contents to an object file.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see    Document
 */
package com.jdpa.mlg.science.writers

import java.io._
import scala.io._

import com.jdpa.mlg.science.datastructures._
import com.jdpa.mlg.science.utils._

object DocObjectWriter extends Log {


  def create(docs: List[Document],fname: String) {
    val os = new FileOutputStream(fname)
    val objectOutputStream = new ObjectOutputStream(os)
    objectOutputStream.writeObject(docs)
    objectOutputStream.flush()
    objectOutputStream.close()
  }

  def create(doc: Document,fname: String) {
    val os = new FileOutputStream(fname)
    val objectOutputStream = new ObjectOutputStream(os)
    objectOutputStream.writeObject(doc)
    objectOutputStream.flush()
    objectOutputStream.close()
  }
  
}
