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
 * The Class DependencyParseTree represents a dependency parse tree for a sentence.
 * The nodes of the tree are implemented in DependencyParseNode instances.  Each tree can
 * have multiple probabalistic Root nodes associated with it.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer
import com.jdpa.mlg.science.utils.Log

@scala.serializable
class DependencyParseTree extends MetaData with Log{

  var mSentence: Sentence = null
  val mRoot = new ListBuffer[ProbLink[DependencyParseNode,Null]]

 /**
   * Recursively build the the tree based upon an indexed list with the relationships between
   * the nodes.
   * 
   * @param lst A ListBuffer of tuples (Token,token index,label,head index,probability) for all the nodes in the tree.  THIS LIST GETS DESTROYED!
   * @param sent The Sentence represented by this tree
   * @param prob The probability of this tree
   *
   * @return List[DependencyParseNode]
   */  
  def this(lst: ListBuffer[(Token,Int,String,Int,Double)],sent: Sentence,prob: Double) = {
    this()    
    mSentence = sent

    ///////build tree
    
    //first fill in root and create root node
    val rootIdx = lst.findIndexOf(t=>t._4 == -1)
    if (rootIdx == -1) {
      fatal("No root for dependency parse tree: '" + sent.toString +"'")
    }
    val rootDPN: DependencyParseNode = new Root(sent)
    mRoot += new ProbLink[DependencyParseNode,Null](prob,rootDPN,null)
    rootDPN.mIdx = -1 
    
    rootDPN.buildTree(lst,sent,-1)
    if (lst.length>0){
      List.range(0,lst.length).foreach{
        i=>
        error("token lst: " + lst(i)._2 + " " + lst(i)._3 + " " + lst(i)._4)
      }
      fatal("Parse tree is not fully connected: " + sent.toString)  
    }
  } 

  /**
   * Find the head id number (index in sentence) and the relationship to the head
   * for the given token.
   * 
   * @param tok The token whose head to look up.
   * @return A tuple of the head id and the relationship to the head
   */
  def getHead(tok: Token): (Int,DependencyRelationType.Value) = {
    val result = mRoot(0).ref.getHead(tok)
    if (result._1 == null){//root is the head
      (0,result._2)
    }
    else{
      (mSentence.indexOf(result._1)+1,result._2)
    }
  }
  
  
 /**
   * Convert the entire tree to a string in fake CoNLL format.
   * 
   * @param lst A ListBuffer of tuples (Token,token index,label,head index,probability) for all the nodes in the tree.  THIS LIST GETS DESTROYED!
   * @param sent The Sentence represented by this tree
   *
   * @return List[DependencyParseNode]
   * @todo support real CoNLL format
   */  
  override def toString(): String = {
    var str = ""

    mRoot.foreach {
      tree=>
      val lst = tree.ref.toList.sortWith((x,y) => x.mIdx < y.mIdx )
      lst.foreach {
        dpn=> str += dpn.toString  
      }
      str += "\n"
    }
    str
  }
  
}
