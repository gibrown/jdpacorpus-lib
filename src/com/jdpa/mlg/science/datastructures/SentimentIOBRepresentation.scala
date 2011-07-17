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
 * IOB style representation of the document.  Built by looking up a series of data attached to the tokens
 * in a document.
 * 
 * @author gregory_brown
 *
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer
import scala.collection.immutable.HashSet
import com.jdpa.mlg.science.utils.Log

@scala.serializable
class SentimentIOBRepresentation extends Log{

  var mDoc: Document = null
  var mLabel = ""
  
  def this(doc: Document){
    this()
    mDoc = doc
  }


  /**
   * Add IOB labels to the mMetaMap(label) value for annotations attached to this document.
   * Which labels are created depends on the the includedAnnotations set that is passed in as an argument.
   * This set is a list of the class names of the particular annotations.
   * 
   * @param includedAnnotations  The set of annotations that will be output into IOB format, all other ignored ("O") 
   * @param label The name of the label in the mMetaMap of each Token
   */
  def createLabels(includedAnnotations: HashSet[String], label: String){
    mLabel = label
    //first set all tokens to be O
    mDoc.mTokens.foreach{x=> x.asInstanceOf[Token].mMetaMap.put(mLabel,"O")}

    //now add sentiment annotations
    if (includedAnnotations.contains("SentimentExpression")){
      mDoc.mElements.foreach{_ match {
        case e: SentimentExpression => {
          e.getPolarityVal.toString match {
            case "Positive" => addTags("SE-Positive",e)
            case "Negative" => addTags("SE-Negative",e)
            case other => null //ignore other SE, too rare
          }
        }
        case other => null
      }}
    }
    
    if (includedAnnotations.contains("Anchor")){
      mDoc.mCoRefGroups.foreach{
        c=>
        c.mElems.foreach{_ match {
          case e: Anchor => {
            e.mType match{
              case AnchorType.AT_SUPERTOPIC => addTags("SuperTopic",e) 
              case AnchorType.AT_SUBTOPIC => addTags("SubTopic",e) 
              case AnchorType.AT_SUBSUBTOPIC => addTags("SubSubTopic",e) 
              case AnchorType.AT_UNKNOWN => addTags("UnknownTopic",e) 
            }
          }
          case other => null
        }}
      }
    }
      
    if (includedAnnotations.contains("Intensifier")){
      mDoc.mElements.foreach{_ match {
        case e: Intensifier => addTags("Intensifier",e) 
        case other => null
      }}
    }
    if (includedAnnotations.contains("Negator")){
      mDoc.mElements.foreach{_ match {
        case e: Negator => addTags("Negator",e) 
        case other => null
      }}
    }
    if (includedAnnotations.contains("Neutralizer")){
      mDoc.mElements.foreach{_ match {
        case e: Neutralizer => addTags("Neutralizer",e) 
        case other => null
      }}
    }

    if (includedAnnotations.contains("Mention")){
      mDoc.mElements.foreach{_ match {
        case e: Mention => {
          val strs = e.getSemanticType
          if (strs.length > 0)
            if (strs(0).startsWith("Food")){
              //skip, there aren't many of these
            }
            else{
              addTags("Mention-" + strs(0),e)
            }
          else
            warn("Mention without a type!")
        }
        case other => null
      }}
    }
    
    if (includedAnnotations.contains("Committer")){
      mDoc.mElements.foreach{_ match {
        case e: Committer => {
          if (!e.getDirVal.toString.equals("NONE")){
            addTags("Committer-" + e.getDirVal.toString,e) 
          }
        }
        case other => null
      }}
    }
    
    if (includedAnnotations.contains("Comparison")){
      mDoc.mElements.foreach{_ match {
        case e: Comparison => 
          if (e.mSame) addTags("ComparisonSame",e) 
          else addTags("Comparison",e) 
        case other => null
      }}
    }
    
    if (includedAnnotations.contains("Descriptor")){
      mDoc.mElements.foreach{_ match {
        case e: Descriptor => addTags("Descriptor",e) 
        case other => null
      }}
    }

  }
  
  /**
   * Add a tag to the tokens in this sequence.  The tag is added to the mMetaMap
   * properties attached to each Token instance.
   * 
   * @param tag The tag to add
   * @param tokens The sequence of tokens to add the tag to.
   */
  def addTags(tag: String,tokens: SpanSequence){
    if (tokens.length > 0){
      var alreadylabeled = false
      tokens.foreach{
        tok=>
        if (!tok.asInstanceOf[Token].mMetaMap(mLabel).equals("O")) alreadylabeled = true 
      }
      if (!alreadylabeled){
        tokens(0).asInstanceOf[Token].mMetaMap.put(mLabel,"B-" + tag)
        tokens.drop(1).foreach{
          t=>
          t.asInstanceOf[Token].mMetaMap.put(mLabel,"I-" + tag)
        }
      }
    }
  }
  
}
