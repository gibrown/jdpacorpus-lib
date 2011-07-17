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
 * The Class Sentence is a SpanSequence that composes a single sentence in the document.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.mutable.ListBuffer
import edu.stanford.nlp.trees.Tree
import edu.stanford.nlp.trees.CollinsHeadFinder

@scala.serializable
class Sentence extends SpanSequence {

  var mNext: Sentence=null;
  var mPrev: Sentence=null;

  val mDepTrees = new ListBuffer[ProbLink[DependencyParseTree,Null]]

  var mStanfordTree: Tree = null
  var mSentNum = -1
    
  /**
   * Return the next Sentence in the document.
   *
   * @return Sentence
   */
  def next(): Sentence = {
    mNext
  }

  /**
   * Return the previous Sentence in the document.
   *
   * @return Sentence
   */
  def prev(): Sentence = {
    mPrev
  }

  /**
   * Attaches a dependency parse tree to this sentence.
   *
   * @param prob The probability of this tree
   * @param tree A reference to the tree
   */
   def addDepTree(prob: Double,tree: DependencyParseTree){
     mDepTrees += new ProbLink[DependencyParseTree,Null](prob,tree,null)
   }
   
  /**
   * Convert sentence of tokens into a text string.
   *
   * @return Sentence text as a string.
   */
  override def toString(): String = {
    var str = ""
    this.foreach(t=>str+=t.mText + " " )
    str
  }
  
  /**
   * Determine the path between two tokens in the same sentence using
   * the Stanford Parse tree.
   * 
   * @param s1 The first token(span)
   * @param s2 The second token(span)
   * @return A tuple of a list of the labels for the path, and the head word of that path.
   */
  def getStanfordNode2NodePath(s1:Span,s2:Span): (List[String],String)={
    var lst = new ListBuffer[String]
    
    //get the Stanford Tree nodes for each span
    val leaves = mStanfordTree.getLeaves()
    val idx1 = this.indexOf(s1)
    val st1 = leaves.get(idx1)
    val idx2 = this.indexOf(s2)
    val st2 = leaves.get(idx2)

    getStanfordTreePath(st1,st2)
  }

  def getStanfordNode2NodePath(s1:Span): (List[String],String)={
    var lst = new ListBuffer[String]
    
    //get the Stanford Tree nodes for each span
    val leaves = mStanfordTree.getLeaves()
    val idx1 = this.indexOf(s1)
    val st1 = leaves.get(idx1)

    getStanfordTreePath(st1,mStanfordTree)
  }

  def getStanfordTreePath(n1:Tree,n2:Tree): (List[String],String)={
    var lst = new ListBuffer[String]
    
    val jlst = mStanfordTree.pathNodeToNode(n1,n2)
    var min_depth = 1000
    var head_word = "-NONE-"
    var dir = "U"
    for(i <- 1 until jlst.size()-1){ // skip head and tail which are the two words
      lst += (jlst.get(i).nodeString().replaceAll(" \\[\\S+\\]","") + "-" + dir) // remove probability
      if (jlst.get(i).depth() < min_depth){
        min_depth = jlst.get(i).depth()
        head_word = jlst.get(i).headTerminal(new CollinsHeadFinder()).nodeString()
      }
      else{
        dir = "D"
      }
    }
    
    Tuple2(lst.toList,head_word)      
  }
  
  override def delete(){
    mNext=null;
    mPrev=null;

    mStanfordTree = null
    super.delete()
  }
}
