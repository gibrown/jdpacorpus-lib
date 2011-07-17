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
 * The abstract class Invisible represents a piece of semantic information in
 * a document that is not associated with a particular span in the document.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

@scala.serializable
abstract class Invisible {

  var mMeaning: String = ""
  var mDocument: Document = null

}
