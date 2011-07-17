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
 * The Enum EntityRelationType enumerates the possible relationships between Entities.
 * 
 * @author      Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.datastructures

import com.jdpa.mlg.science.utils.SmartEnum

object EntityRelationType extends SmartEnum{
  
  val ERT_NONE              = Value(0,"NONE")
  val ERT_ANY               = Value(1,"ANY")
  val ERT_FEATURE_OF        = Value(2,"FEATURE_OF")
  val ERT_INSTANCE_OF       = Value(3,"INSTANCE_OF")
  val ERT_MEMBER_OF         = Value(4,"MEMBER_OF")
  val ERT_PART_OF           = Value(5,"PART_OF")
  val ERT_PRODUCES          = Value(6,"PRODUCES")
  val ERT_ACCESSORY_OF      = Value(7,"ACCESSORY_OF")

  //ACE specific relations
  //TYPE="ART" SUBTYPE="Inventor-or-Manufacturer"
  //TYPE="ART" SUBTYPE="Other"
  //TYPE="ART" SUBTYPE="User-or-Owner"
  //TYPE="DISC"
  //TYPE="EMP-ORG" SUBTYPE="Employ-Executive"
  //TYPE="EMP-ORG" SUBTYPE="Employ-Staff"
  //TYPE="EMP-ORG" SUBTYPE="Employ-Undetermined"
  //TYPE="EMP-ORG" SUBTYPE="Member-of-Group"
  //TYPE="EMP-ORG" SUBTYPE="Other"
  //TYPE="EMP-ORG" SUBTYPE="Partner"
  //TYPE="EMP-ORG" SUBTYPE="Subsidiary"
  //TYPE="GPE-AFF" SUBTYPE="Based-In"
  //TYPE="GPE-AFF" SUBTYPE="Citizen-or-Resident"
  //TYPE="GPE-AFF" SUBTYPE="Other"
  //TYPE="METONYMY"
  //TYPE="OTHER-AFF" SUBTYPE="Ethnic"
  //TYPE="OTHER-AFF" SUBTYPE="Ideology"
  //TYPE="OTHER-AFF" SUBTYPE="Other"
  //TYPE="PER-SOC" SUBTYPE="Business"
  //TYPE="PER-SOC" SUBTYPE="Family"
  //TYPE="PER-SOC" SUBTYPE="Other"
  //TYPE="PHYS" SUBTYPE="Located"
  //TYPE="PHYS" SUBTYPE="Near"
  //TYPE="PHYS" SUBTYPE="Part-Whole"  

  //general ACE types
  val ERT_ART                  = Value(8,"ART")
  val ERT_DISC                 = Value(9,"DISC")
  val ERT_EMP_ORG              = Value(10,"EMP_ORG")
  val ERT_GPE_AFF              = Value(11,"GPE_AFF")
  val ERT_OTHER_AFF            = Value(12,"OTHER_AFF")
  val ERT_PER_SOC              = Value(13,"PER_SOC")
  val ERT_PHYS                 = Value(14,"PHYS")

  //ACE subtypes
  val ERT_ART_INV              = Value(20,"ART_INV")
  val ERT_ART_OTHER            = Value(21,"ART_OTHER")
  val ERT_ART_USER             = Value(22,"ART_USER")
  val ERT_EMP_ORG_EMP_EXEC     = Value(23,"EMP_ORG_EMP_EXEC")
  val ERT_EMP_ORG_EMP_STAFF    = Value(24,"EMP_ORG_EMP_STAFF")
  val ERT_EMP_ORG_EMP_UN       = Value(25,"EMP_ORG_EMP_UN")
  val ERT_EMP_ORG_MEMBER       = Value(26,"EMP_ORG_MEMBER")
  val ERT_EMP_ORG_OTHER        = Value(27,"EMP_ORG_OTHER")
  val ERT_EMP_ORG_PARTNER      = Value(28,"EMP_ORG_PARTNER")
  val ERT_EMP_ORG_SUBSIDIARY   = Value(29,"EMP_ORG_SUBSIDIARY")
  val ERT_GPE_AFF_BASED_IN     = Value(30,"GPE_AFF_BASED_IN")
  val ERT_GPE_AFF_RESIDENT     = Value(31,"GPE_AFF_RESIDENT")
  val ERT_GPE_AFF_OTHER        = Value(32,"GPE_AFF_OTHER")
  val ERT_OTHER_AFF_ETHNIC     = Value(33,"OTHER_AFF_ETHNIC")
  val ERT_OTHER_AFF_IDEOLOGY   = Value(34,"OTHER_AFF_IDEOLOGY")
  val ERT_OTHER_AFF_OTHER      = Value(35,"OTHER_AFF_OTHER")
  val ERT_PER_SOC_BUSINESS     = Value(36,"PER_SOC_BUSINESS")
  val ERT_PER_SOC_FAMILY       = Value(37,"PER_SOC_FAMILY")
  val ERT_PER_SOC_OTHER        = Value(38,"PER_SOC_OTHER")
  val ERT_PHYS_LOCATED         = Value(39,"PHYS_LOCATED")
  val ERT_PHYS_NEAR            = Value(40,"PHYS_NEAR")
  val ERT_PHYS_PART_WHOLE      = Value(41,"PHYS_PART_WHOLE")
  
  
}
