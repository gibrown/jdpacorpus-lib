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
 * The trait DependencyParseNode defines a node in a DependencyParseTree.  
 * This is essentially an interface. 
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see DependencyParseTree
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer

import com.jdpa.mlg.science.utils.Log

@scala.serializable
trait DependencyParseNode extends Log{

  //type DRT = DependencyRelationType.type
  
  val mLeftDaughters = new ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]]
  val mRightDaughters = new ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]]
  var mIdx = -1

  /**
   * Gets the left daughters of this node.
   *
   * @return List[DependencyParseNode]
   */
  def getLeftDaughters(): ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]] = {
    mLeftDaughters
  }

  /**
   * Gets the right daughters of this node.
   *
   * @return List[DependencyParseNode]
   */
  def getRightDaughters(): ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]] = {
    mRightDaughters
  }

  /**
   * Gets the heads of this node.
   *
   * @return List[DependencyParseNode]
   */
  def getHeads(): ListBuffer[ProbLink[DependencyParseNode,DependencyRelationType.Value]];  

  /**
   * Gets a particular head instance
   *
   * @return List[DependencyParseNode]
   */
  def getHead(idx: Int): (DependencyParseNode,DependencyRelationType.Value);  

  /**
   * Gets the token associated with this node
   *
   * @return The token or null if this is the root
   */
  def getToken(): Token;  
  
  /**
   * Recursively build the nodes beneath this node based on a list of indices pointing to heads.
   * As each node is added its entry in the list is removed.
   * 
   * @param lst A list of tuples (Token,token index,label,head index,probability) for all the nodes yet to be added to the tree
   * @param sent The Sentence represented by this tree
   * @param myidx The index value for this node
   *
   * @return List[DependencyParseNode]
   */  
  def buildTree(lst: ListBuffer[(Token,Int,String,Int,Double)],sent: Sentence,myidx: Int) {
    var i=0

    trace("building tree node: " + myidx + " remaining: " + lst.length)
    
    while(i<lst.length){
      if (lst(i)._4 == myidx){
        val newIdx = lst(i)._2
        val newRel = DependencyRelationType.getVal(lst(i)._3)
        val tok = lst(i)._1
        val prob = lst(i)._5
        val newDPN = new DependencyNodeToken(tok,sent,prob,this,newRel)
        tok.mDepNode += new ProbLink[DependencyNodeToken,DependencyRelationType.Value](prob,newDPN,newRel)
        newDPN.mIdx = lst(i)._2
        if (lst(i)._2 < myidx){
          mLeftDaughters += new ProbLink[DependencyParseNode,DependencyRelationType.Value](prob,newDPN,newRel)
        }
        else {
          mRightDaughters += new ProbLink[DependencyParseNode,DependencyRelationType.Value](prob,newDPN,newRel)
        }

        lst.remove(i)
        //recurse down tree
        newDPN.buildTree(lst,sent,newIdx)
        i=0 // reset i since the list could be arbitrarily shorter now
      }
      else{
        i += 1
      }
    }
  }

  /**
   * Find the head id number (index in sentence) and the relationship to the head
   * for the given token.
   * 
   * @param tok The token whose head to look up.
   * @return A tuple of the head id and the relationship to the head, or null
   */
  def getHead(tok: Token): (Token,DependencyRelationType.Value) = {
    //annon function to recursively find token in the tree
    val find = (x: ProbLink[DependencyParseNode,DependencyRelationType.Value]) => {
      val node = x.ref.asInstanceOf[DependencyNodeToken]
      if (node.mToken == tok){
        return (this.getToken(),x.typ)
      }
      else{
        val result = node.getHead(tok)
        if (result != null) return result
      }      
    }
    mLeftDaughters.foreach{
      d=>find(d)
    }
    mRightDaughters.foreach{
      d=>find(d)
    }
    return null //not found in this part of the tree
  }

  /**
   * Places all non-Root nodes and all of this nodes children into a ListBuffer.
   * 
   * @return A ListBuffer with this node and all of its children in it.
   * @todo support probabalistic list of heads
   */  
  def toList: List[DependencyParseNode] = {
    var lst: List[DependencyParseNode] = Nil
    if (this.isInstanceOf[Root] == true) {
      //ignore the root
      lst = this :: lst
      mLeftDaughters.foreach {
        d =>
        lst = lst ::: d.ref.toList
      }
      mRightDaughters.foreach {
        d =>
        lst = lst ::: d.ref.toList
      }
    }
    lst
  }

  /**
   * Converts this node to a string represented in fake CoNLL format.  Assumes that there
   * is only one head. 
   * 
   * @return A string representing the node.
   * @todo support probabalistic list of heads
   * @todo support real CoNLL format
   */  
  override def toString: String = {
    var str = ""
    if (this.isInstanceOf[Root] == false) {
      val dnt = this.asInstanceOf[DependencyNodeToken]
      str += mIdx + "\t" + dnt.mToken.mText + "\t" + dnt.mToken.mStem + "\t" + dnt.mToken.mPOS.toString + "\t" +
        "_" + "\t" + dnt.mHeads(0).ref.mIdx + "\t" + dnt.mHeads(0).typ.toString + "\n"
    }
    str
  }
  
}
