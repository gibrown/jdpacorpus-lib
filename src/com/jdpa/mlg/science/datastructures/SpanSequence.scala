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
 * The Class SpanSequence consists of a sequence of (not necessarily contiguous) Spans 
 * from the document.  SpanSequence is an Iterable object (ie can use foreach, etc directly
 * on it.  Internally, it uses an immutable Scala TreeMap to aid in
 * running faster.  A TreeMap is an implementation of a Red-Black tree which provides
 * O(log n) access for most all operations.
 * 
 * @todo implement all methods
 * @todo add hash on top of tree to improve some access times to O(1)
 * @todo implement: search by index
 * @todo implement: search by string
 * @todo implement: search by span start/end
 * @todo implement remaining methods
 */
package com.jdpa.mlg.science.datastructures

import scala.collection.immutable.TreeMap

import com.jdpa.mlg.science.utils.Log

@scala.serializable
class SpanSequence extends Iterable[Span] with Log{

  var tree = new TreeMap[Long,Span]
  private var mCount = 0
  
  def this(s: Span){
    this()
    this += s
  }
  
  /**
   * Allows direct indexing of items in the sequence (eg: spanseq(i)).  The sequence
   * is numbered sequentially from 0 to N-1
   *
   * @param idx The index into the sequence
   * @return The Span at that index location
   */
  def apply(idx: Int): Span = {
    (tree.toSeq)(idx)._2
  }  

  
  /**
   * Add a Span instance into the tree.
   *
   * @param span Span instance to add into tree.
   * @return Unit
   */
  def +=(span: Span): Unit = {
    tree = tree.insert(span.mStartOffset,span)
    mCount += 1
  }  

  /**
   * Combine another SpanSequence into this tree.
   *
   * @param ss SpanSequence to add in.
   * @return Unit
   */
  def +=(ss: SpanSequence): Unit = {
    ss.foreach { s=> 
      tree = tree.insert(s.mStartOffset,s)
      mCount += 1
    }
  }  

  /**
   * Remove a Span instance from the tree.
   *
   * @param span Span instance to remove from the tree.
   * @return Unit
   */
  def remove(span: Span): Unit = {
    tree = tree - span.mStartOffset
    mCount -= 1
  }  

  /**
   * Remove a Span instance by its index
   *
   * @param idx The index to remove
   * @return Unit
   */
  def remove(idx: Int): Unit = {
    if (idx < mCount){
      tree = tree - this(idx).mStartOffset
      mCount -= 1
    }
  }  
  
  
  /**
   * Returns the length of the span sequence
   *
   * @return The length
   */
  def length : Int = {
    mCount 
  }
  
  /**
   * Returns the index for a particular token.
   *
   * @return The index or -1 if the span is not in this sequence.
   */
  def indexOf(s: Span): Int = {
    val opt = tree.get(s.mStartOffset)
    if (opt.isEmpty){
      -1
    }
    else{
      tree.keys.toSeq.indexOf(s.mStartOffset)
    }
  }

  /**
   * Returns an Iterator over the items in the tree.
   *
   * @return An iterator over the TreeMap
   */
  def iterator : Iterator[Span] = {
    tree.valuesIterator
  }
  
  /**
   * Find the list of spans from this sequence that correspond to a particular string.
   *
   * @param str The string to find.
   * @return List[Span]
   */
  def find(str: String): List[Span] = {
    null
  }

  /**
   * Checks whether a Span is contained within this sequence.
   *
   * @param s The span to check for.
   * @return Boolean
   */
  def has(s: Span): Boolean = {
    val opt = tree.get(s.mStartOffset)
    opt.isDefined
  }

  /**
   * Gets the starting offset for this sequence.
   *
   */
  def getStartOffset(): Long = {
    if (mCount == 0) -1
    else this(0).mStartOffset
  }

  /**
   * Gets the ending offset for this sequence.
   *
   */
  def getEndOffset(): Long = {
    if (mCount == 0) -1
    else this(mCount - 1).mEndOffset
  }
  
  /**
   * Find a Span whose starting offset exactly corresponds with the given offset. O(lg n)
   *
   * @param span_start The starting span offset
   * @return Span
   */
  def findSpanStart(span_start: Long): Option[Span] = {
    tree.get(span_start)
  }

  /**
   * Find a Span which overlaps a particular offset. 
   * 
   * O(lg n + X): this method does a local search of the token sequence
   * within 100 locations of the offset.
   *
   * @param off The offset
   * @return Span
   */
  def findOffset(off: Long): Span = {
    var proj = tree.range(off-100,off+100)
    if (proj.size > 0){
      var lastOff = proj(proj.firstKey).mStartOffset
      proj.valuesIterator.foreach{
        t=>
        if ((t.mStartOffset <= off) && (t.mEndOffset > off))
          return t
        else if ((lastOff <= off) && (t.mEndOffset > off)){
          warn("Fuzzy matched token offset.")
          return t
        }
        lastOff = t.mEndOffset
      }
    }
    //should rarely get here
    
    //if fail to find a token, increase offset range dramatically, could inside a url
    //fyi: MS explorer only supports urls up to 2048 characters long, though
    //the official spec has no limit, but 5000 chars should be a good limit
    proj = tree.range(off-5000,off+100)
    proj.valuesIterator.foreach{
      t=>
      if ((t.mStartOffset <= off) && (t.mEndOffset > off))
        return t
    }
    //just fail
    null
  }
  
  /**
   * Find a Span whose ending offset corresponds with the given offset. 
   *
   * @param span_end The ending span offset
   * @return Span
   */
  def findSpanEnd(span_end: Long): Span = {
    null
  }

  /**
   * Checks if this sequence is consecutive.
   *
   * @return Boolean
   */
  def isConsecutive(): Boolean = {
    false
  }

  /**
   * Checks whether two SpanSequences overlap one another
   *
   * @param span_seq The other SpanSequence to check against.
   * @return Boolean
   */
  def overlaps(span_seq: SpanSequence): Boolean = {
    false
  }

  /**
   * Returns the head noun for this sequence.
   *
   * @return DependencyNodeToken
   */
  def getHeadNoun(): DependencyNodeToken = {
    null
  }

  /**
   * Find the closest token to the given offset
   *
   * @param offset Offset from beginning of Document
   * @return Token
   */
  def findClosestToken(offset: Long): Token = {
    null
  }


  /**
   * Converts this sequence to its string representation, inserting spaces between tokens.
   *
   */
  override def toString(): String = {
    var str = ""
    this.foreach{
      t=> str += t.mText + " "
    }
    if (str.equals("")) str
    else str.take(str.length - 1) //remove extra space
  }
  
  /**
   * Converts this sequence to its string representation with no spaces between tokens.
   *
   */
  def toStringNoWhiteSpace(): String = {
    var str = ""
    this.foreach{
      t=> str += t.mText
    }
    str
  }
  
  def delete(){
    while(this.length>0){
      val span = this(0)
      if (span != null){
        span.asInstanceOf[Token].delete()
      }
      this.remove(0)
    }
  }
  
}
