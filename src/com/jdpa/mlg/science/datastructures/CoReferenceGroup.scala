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
 * The factory methods for building a CoReferenceGroup object.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Anchor
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer
import com.jdpa.mlg.science.utils.Log


object CoReferenceGroup extends Log{
  
  /**
   * Takes an in order list of spans in the text and builds the coreference group of Elements for them.  This builds 
   * the Anchors from a particular tokenized document, and adds the coreference groups to the document
   * instance.
   * 
   * @param anchorList List of tuples with: (start offset, end offset)
   * @param doc The document these spans are from
   * @param at The type of anchor elements to build
   * 
   * @return A reference to the CoReferenceGroup instance created.
   */
  def buildCoRefGroup(anchorList: List[(Long,Long)], doc: Document,at: AnchorType.Value,query: String): CoReferenceGroup = {
    val coref = new CoReferenceGroup(doc)
    var lastAnchor: Anchor = null
    anchorList.foreach{
      a=>
      trace("Adding " + at.toString + " annotation: " + a.toString)
      val anchor = new Anchor(at)
      coref.mElems += anchor
      anchor.buildSpanSequence(a._1,a._2,doc)
          
      //fill in doubly linked list between anchors
      if (lastAnchor == null){
        lastAnchor = anchor
      }
      else{
        lastAnchor.mRefersToRight = anchor
        anchor.mRefersToLeft = lastAnchor
      }          
    }
    coref.mQuery = query
    doc.mCoRefGroups += coref
    
    coref        
  }
}

/**
 * The container class for a group of anchors in the text linking a series of topics or subtopics together.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Anchor
 */

@scala.serializable
class CoReferenceGroup(doc: Document){

  //the anchor elements in the text
  val mElems = new ListBuffer[Anchor]

  var mDoc = doc
  var mQuery = ""
  
  //other co ref groups that are related to this one
  var mRelatedTo = new ListBuffer[CoReferenceGroup]
  
  def delete(){
    mDoc = null
    mQuery = null    
  }
}
