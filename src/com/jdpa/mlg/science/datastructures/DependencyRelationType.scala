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
 * The Enum DependencyRelationType enumerates the relations within a dependency parse tree.
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see DependencyParseTree
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object DependencyRelationType extends SmartEnum{
  val DRT_ADV       = Value(0,"ADV")
  val DRT_AMOD      = Value(1,"AMOD")
  val DRT_APPO      = Value(2,"APPO")
  val DRT_BNF       = Value(3,"BNF")
  val DRT_CONJ      = Value(4,"CONJ")
  val DRT_COORD     = Value(5,"COORD")
  val DRT_DEP       = Value(6,"DEP")
  val DRT_DIR       = Value(7,"DIR")
  val DRT_DTV       = Value(8,"DTV")
  val DRT_DVT       = Value(9,"DVT")
  val DRT_EXT       = Value(10,"EXT")
  val DRT_EXTR      = Value(11,"EXTR")
  val DRT_HMOD      = Value(12,"HMOD")
  val DRT_HYPH      = Value(13,"HYPH")
  val DRT_IM        = Value(14,"IM")
  val DRT_LGS       = Value(15,"LGS")
  val DRT_LOC       = Value(16,"LOC")
  val DRT_MNR       = Value(17,"MNR")
  val DRT_NAME      = Value(18,"NAME")
  val DRT_NMOD      = Value(19,"NMOD")
  val DRT_OBJ       = Value(20,"OBJ")
  val DRT_OPRD      = Value(21,"OPRD")
  val DRT_P         = Value(22,"P")
  val DRT_PMOD      = Value(23,"PMOD")
  val DRT_POSTHON   = Value(24,"POSTHON")
  val DRT_PRD       = Value(25,"PRD")
  val DRT_PRN       = Value(26,"PRN")
  val DRT_PRP       = Value(27,"PRP")
  val DRT_PRT       = Value(28,"PRT")
  val DRT_PUT       = Value(29,"PUT")
  val DRT_ROOT      = Value(30,"ROOT")
  val DRT_SBJ       = Value(31,"SBJ")
  val DRT_SUB       = Value(32,"SUB")
  val DRT_SUFFIX    = Value(33,"SUFFIX")
  val DRT_TITLE     = Value(34,"TITLE")
  val DRT_TMP       = Value(35,"TMP")
  val DRT_VC        = Value(36,"VC")
  val DRT_VOC       = Value(37,"VOC")

}
