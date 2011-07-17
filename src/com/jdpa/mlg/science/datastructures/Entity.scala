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
 * The Class Entity represents a semantic entity within the document.  This is not a specific sequence
 * of text indicating the entity, that is handled by the Mention class.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Mention
 * @todo implement methods
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer
import com.jdpa.mlg.science.utils.Log

@scala.serializable
class Entity extends MetaData with Log{

  val mMentions = new ListBuffer[ProbLink[Mention,Null]]
  val mSemanticType = new ListBuffer[ProbLink[List[String],Null]]
  val mPartOf = new ListBuffer[ProbLink[Entity,EntityRelationType.Value]]
  val mParentOf = new ListBuffer[ProbLink[Entity,EntityRelationType.Value]]
  val mEntityPolarity = new ListBuffer[ProbLink[Null,SentimentPolarity.Value]]

  
  def relation(ent: Entity): String = {
    var rel = "NONE"
    
    mPartOf.foreach{
      pl=>
      if (pl.ref == ent){
        if (!rel.equals("NONE")){
          warn("Entity has multiple relations! [" + this.toString + "]")
          warn("Was " + rel + " now  " + pl.typ.toString)
        }
        rel = pl.typ.toString
      }
    }
    
    mParentOf.foreach{
      pl=>
      if (pl.ref == ent){
        if (!rel.equals("NONE")){
          val newrel = pl.typ.toString
          if (rel.equals(newrel)){
            //assume symmetric relation, mark as S_*
            warn("Entity has symmetric relation " + newrel + ": [" + this.toString + "]")
            rel = "S_" + newrel
          }
          else{
            warn("Entity has multiple relations! [" + this.toString + "]")
            warn("Was " + rel + " now  INV_" + newrel)
            rel = "INV_" + newrel
          }
        }
        else{
          rel = "INV_" + pl.typ.toString
        }
      }
    }
    
    rel
  }
  
  /**
   * Checks if this entity has a particular sub-relation to another entity.
   *
   * @param ment The other entity that this entity may be a PartOf/ProducedBy/etc
   * @param typ The relation type between the entities
   * @return Boolean
   */
  def isPartOf(ment: Entity, typ: EntityRelationType.type): Boolean = {
    false
  }

  /**
   * Checks if this entity has a particular parent-relation to another entity.
   *
   * @param ment The other mention that may be a PartOf/ProducedBy/etc of this entity.
   * @param typ The relation type between the entities
   * @return Boolean
   */
  def isParentOf(ment: Entity, typ: EntityRelationType.type): Boolean = {
    false
  }

  /**
   * Gets the overall document sentiment about this particular entity
   *
   * @param useMeronomy Calculate including the sentiment of the parts of this entity.
   * @param useHolonomy Calculate including the sentiment of the parents of this entity.
   * @return Double
   */
  def getEntitySentiment(useMeronomy: Boolean, useHolonomy: Boolean): Double = {
    0
  }

  /**
   * Follow mention lists to all other mentions that we are a part of and create a link to the entity that this
   * entity is related to.  Do no call until all Entities have been created from the Mentions in the document.
   */
  def createPartOfLists(){
    //go through all mentions and get the entity they are a part of
    mMentions.foreach{
      ment=>
      ment.ref.mPartOf.foreach{
        ref=>
        //only add if we don't already have a reference to this entity
        val entref = ref.ref.mSources(0).ref
        val entOpt = mPartOf.find((x: ProbLink[Entity,EntityRelationType.Value])=> {x.ref == entref})
        if (entOpt.isEmpty){
          mPartOf += new ProbLink[Entity,EntityRelationType.Value](ref.prob,entref,ref.typ) 
        }
      }
      ment.ref.mParentPartOf.foreach{
        ref=>
        //only add if we don't already have a reference to this entity
        val entref = ref.ref.mSources(0).ref
        val entOpt = mParentOf.find((x: ProbLink[Entity,EntityRelationType.Value])=> {x.ref == entref})
        if (entOpt.isEmpty){
          mParentOf += new ProbLink[Entity,EntityRelationType.Value](ref.prob,entref,ref.typ) 
        }
      }
    }
    
  }

  /**
   * Convert entity to a list of its mentions
   */
  override def toString(): String = {
    mMentions.map(_.ref.toString).mkString(",")
  }
  
  def delete(){
    while(mMentions.length > 0){
      mMentions.remove(0)
    }
    while(mSemanticType.length > 0){
      mSemanticType.remove(0)
    }
    while(mPartOf.length > 0){
      mPartOf.remove(0)
    }    
    while(mParentOf.length > 0){
      mParentOf.remove(0)
    }    
    while(mEntityPolarity.length > 0){
      mEntityPolarity.remove(0)
    }    

  }
  
}
