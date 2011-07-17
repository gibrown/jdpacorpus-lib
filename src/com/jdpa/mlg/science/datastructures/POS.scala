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
 * The Enum POS enumerates the different Part of Speech tags (taken from the Penn Treebank).
 * 
 * @author      Gregory Ichneumon Brown
 * @todo check complete list against c++ code
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object POS extends SmartEnum{
  val POS_NONE           = Value(0,"NONE")
  val POS_CC             = Value(1,"CC")
  val POS_CD             = Value(2,"CD")
  val POS_DT             = Value(3,"DT")
  val POS_EX             = Value(4,"EX")
  val POS_FW             = Value(5,"FW")
  val POS_IN             = Value(6,"IN")
  val POS_JJ             = Value(7,"JJ")
  val POS_JJR            = Value(8,"JJR")
  val POS_JJS            = Value(9,"JJS")
  val POS_LS             = Value(10,"LS") 
  val POS_MD             = Value(11,"MD") 
  val POS_NN             = Value(12,"NN") 
  val POS_NNS            = Value(13,"NNS")
  val POS_NNP            = Value(14,"NNP")
  val POS_NNPS           = Value(15,"NNPS")
  val POS_PDT            = Value(16,"PDT")
  val POS_POS            = Value(17,"POS")
  val POS_PRP            = Value(18,"PRP")
  val POS_PRPS           = Value(19,"PRP$") 
  val POS_RB             = Value(20,"RB")
  val POS_RBR            = Value(21,"RBR")
  val POS_RBS            = Value(22,"RBS")
  val POS_RP             = Value(23,"RP")
  val POS_SYM            = Value(24,"SYM")
  val POS_TO             = Value(25,"TO")
  val POS_UH             = Value(26,"UH")
  val POS_VB             = Value(27,"VB")
  val POS_VBD            = Value(28,"VBD")
  val POS_VBG            = Value(29,"VBG")
  val POS_VBN            = Value(30,"VBN")
  val POS_VBP            = Value(31,"VBP")
  val POS_VBZ            = Value(32,"VBZ")
  val POS_WDT            = Value(33,"WDT")
  val POS_WP             = Value(34,"WP")
  val POS_WPS            = Value(35,"WP$")
  val POS_WRB            = Value(36,"WRB")
  val POS_SYM_HASH       = Value(37,"#")
  val POS_SYM_DOLLAR     = Value(38,"$")
  val POS_SYM_PERIOD     = Value(39,".")
  val POS_SYM_COMMA      = Value(40,",")
  val POS_SYM_COLON      = Value(41,":") 
  val POS_SYM_LPAREN     = Value(42,"(")
  val POS_SYM_RPARAN     = Value(43,")")
  val POS_SYM_TICK       = Value(44,"`")
  val POS_SYM_DTICK      = Value(45,"``")
  val POS_SYM_QUOTE      = Value(46,"'")
  val POS_SYM_TWOQUOTE   = Value(47,"''")

  //added by us
  val POS_HYPH           = Value(48,"HYPH")
  val POS_ROOT           = Value(49,"ROOT")
  val POS_BE             = Value(50,"$BE")
  val POS_NOUN_HEAD      = Value(51,"Nh")
  val POS_VERB_FINITE    = Value(52,"Vf") // generalizing "VBD" "VBP" "VBZ"
  val POS_PRF            = Value(53,"PRF")

  //WARNING: adding more states will break the training data for the POS trainer
  //The POS trainer uses this number of states, and does not handle them changing gracefully
  
}
