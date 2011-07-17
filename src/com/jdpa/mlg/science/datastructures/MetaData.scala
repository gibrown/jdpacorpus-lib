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
 * The trait MetaData is a generic container that gets mixed into various other datastructures.
 * This is essentially just a map that can be used to store Strings things like AnnotatorID,
 * SourceFileLocation, etc.  It also explicitly has values for AnnotationSourceType and
 * AnnotatorName
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.HashMap

@scala.serializable
trait MetaData{

  val mMetaMap = new HashMap[String,String]
  
  var mAnnotationSource = AnnotationSourceType.AST_UNKNOWN
  var mAnnotatorName = new String

}
