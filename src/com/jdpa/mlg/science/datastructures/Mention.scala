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
 * The Class Mention represents the mention of an entity within the document.
 * This class also contains a probabalistic list of the possible Semantic types
 * for the mention.  The Semantic type is listed as a series of strings representing
 * the type from least specific to most specific (eg. VEHICLE,CAR,SUV,...)
 * <p>
 * Examples of a mention are: "car", "he", "dog"
 * Mentions with some intensity/polarity are: "hero","This lemon [of a car]"
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see Entity
 * @todo implement methods
 * @todo implement transferSentiment
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

@scala.serializable
class Mention extends Element with ModifierTarget with ComparisonDimension{

  val mPriorIntensity = new ListBuffer[ProbLink[Null,SentimentIntensity.Value]]
  val mPriorPolarity = new ListBuffer[ProbLink[Null,SentimentPolarity.Value]]
  val mContextualPolarity = new ListBuffer[ProbLink[Null,SentimentPolarity.Value]]
  var mPriorScore = 0.0

  val mSemanticType = new ListBuffer[ProbLink[List[String],Null]]

  val mTargets = new ListBuffer[ProbLink[Mention,Null]]
  val mParents = new ListBuffer[ProbLink[Modifier,Null]]
  val mSources = new ListBuffer[ProbLink[Entity,Null]]
  val mReferTo = new ListBuffer[ProbLink[Mention,Null]]
  val mParentReferTo = new ListBuffer[ProbLink[Mention,Null]]
  val mPartOf = new ListBuffer[ProbLink[Mention,EntityRelationType.Value]]
  val mParentPartOf = new ListBuffer[ProbLink[Mention,EntityRelationType.Value]]

  /**
   * Return the first polarity value for this expression or neutral
   * 
   * @return The sentiment polarity for this expression
   */
  def getPolarityVal(): SentimentPolarity.Value = {
    if (mPriorPolarity.length > 0){
      mPriorPolarity(0).ref
    }
    else SentimentPolarity.SP_NEUTRAL
  }

  
  /**
   * Return the first semantic type for this expression
   * 
   * @return The sentiment polarity for this expression
   */
  def getSemanticType(): List[String] = {
    if (mSemanticType.length > 0){
      mSemanticType(0).ref
    }
    else List("")
  }
  
  /**
   * Gets the prior polarity for this mention
   *
   * @return Double
   */  
  def getPriorPolarity(): Double =  {
    0
  }

  /**
   * Gets the contextual polarity for this mention by recursively following the modifiers
   *
   * @return Double
   */
  def getContextualPolarity(): Double = {
    0
  }

  /**
   * Gets the target entities.
   *
   * @return List[Entities]
   */
  def getTargetEntities(): List[ProbLink[Entity,Null]] = {
    mSources.toList
  }

  /**
   * Checks if this mention has a particular sub-relation to another mention.
   *
   * @param ment The other mention that this mention may be a PartOf/ProducedBy/etc
   * @param typ The relation type between the mentions
   * @return Boolean
   */
  def isPartOf(ment: Mention, typ: EntityRelationType.Value): Boolean = {
    false
  }

  /**
   * Checks if this mention has a particular parent-relation to another mention.
   *
   * @param ment The other mention that may be a PartOf/ProducedBy/etc of this mention.
   * @param typ The relation type between the mentions
   * @return Boolean
   */
  def isParentOf(ment: Mention, typ: EntityRelationType.Value): Boolean = {
    false
  }
  
  /**
   * Recurse down the tree of modifier targets and accumulate the total sentiment score.
   *
   * @param target The entity being targetted.
   * @return Double the sentiment score
   */
  def transferSentiment(target: Entity): Double = {
    0.0
  }

  def relation(ment: Mention): String = {
    var rel = "NONE"
    
    mPartOf.foreach{
      pl=>
      if (pl.ref == ment){
        if (!rel.equals("NONE")){
          warn("Mentions have multiple relations! [" + this.toString + "]")
          warn("Was " + rel + " now  " + pl.typ.toString)
        }
        rel = pl.typ.toString
      }
    }
    
    mParentPartOf.foreach{
      pl=>
      if (pl.ref == ment){
        if (!rel.equals("NONE")){
          val newrel = pl.typ.toString
          if (rel.equals(newrel)){
            //assume symmetric relation, mark as S_*
            warn("Mentions have symmetric relation " + newrel + ": [" + this.toString + "]")
            rel = "S_" + newrel
          }
          else{
            warn("Mentions have multiple relations! [" + this.toString + "]")
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
   * Build a single entity to represent this Mention and all the mentions it is linked to through
   * a refers/inverse refers.  This method assumes that a Mention can have only a single entity in its mSources
   * list, and recursively walks through all related mentions using buildEntity(Entity) to build the entity.
   * 
   * @return The newly built entity or null if an entity already existed.
   */
  def buildEntity(): Entity = {
    var ent: Entity = null
    
    if (mSources.length == 0){
      ent = new Entity
      
      this.buildEntity(ent)
    }
    ent
  }
 
  /**
   * Recursively builds an entity by following all refers to and inverse refers to links.
   * 
   * @param ent The entity to connect to this Mention
   */
  def buildEntity(ent: Entity) {
    if (mSources.length == 0){
      Document.addToProbList(this.mSources,ent,null,1.0)
      
      //update entity with connections to this Mention
      Document.addToProbList(ent.mMentions,this,null,1.0)
      ent.mSemanticType ++= this.mSemanticType
      
      //follow all links and connect them to this entity
      mReferTo.foreach{
        ref=> ref.ref.buildEntity(ent)
      }
      mParentReferTo.foreach{
        ref=> ref.ref.buildEntity(ent)
      }
    }
    else{
      //do nothing, got here through a circular loop in the refersTo graph
    }
  }
  
  override def delete(){
    while(mPriorIntensity.length > 0){
      mPriorIntensity.remove(0)
    }
    while(mPriorPolarity.length > 0){
      mPriorPolarity.remove(0)
    }
    while(mContextualPolarity.length > 0){
      mContextualPolarity.remove(0)
    }
    while(mSemanticType.length > 0){
      mSemanticType.remove(0)
    }

    while(mTargets.length > 0){
      mTargets.remove(0)
    }
    while(mParents.length > 0){
      mParents.remove(0)
    }
    while(mSources.length > 0){
      mSources.remove(0)
    }
    while(mReferTo.length > 0){
      mReferTo.remove(0)
    }
    while(mParentReferTo.length > 0){
      mParentReferTo.remove(0)
    }
    while(mPartOf.length > 0){
      mPartOf.remove(0)
    }
    while(mParentPartOf.length > 0){
      mParentPartOf.remove(0)
    }

    super.delete()
    
  }
}
