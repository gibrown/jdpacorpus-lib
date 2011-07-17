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
 * Parameter object for user control of the tokenizer.
 * 
 * @author Gregory Ichneumon Brown
 * @see Tokenize
 */
package com.jdpa.mlg.science.tokenize

import scala.collection.mutable.ListBuffer

import com.jdpa.mlg.science.datastructures.POS

import com.jdpa.mlg.science.utils.ParamUtils

object Params extends ParamUtils(List(
    /**
     * The file to be tokenized
     */
    ("INFILE","test/token/tokenization-test.txt")
  )){
	
}
