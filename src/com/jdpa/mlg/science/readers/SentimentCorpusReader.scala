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
 * This object loads a series of documents from a JDPA XML formatted sentiment corpus file.
 * 
 * 
 * @author      Gregory Ichneumon Brown
 * 
 * @see    Document
 */
package com.jdpa.mlg.science.readers

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap

import scala.xml.{XML,NodeSeq,Elem}

import java.io.File
import java.util.regex.Pattern
import com.jdpa.mlg.science.datastructures._
import com.jdpa.mlg.science.utils._
import com.jdpa.mlg.science.tokenize.OpenNLPTokenize

object SentimentCorpusReader extends Log{

  val mSlotHashMap = new HashMap[String,NodeSeq]
  val mClassHashMap = new HashMap[String,NodeSeq]
  
  val mTextExceptions = new ListBuffer[(Pattern,(String,Element)=>Boolean)]
  
  def reducePronoun(root: String)(surftxt: String,elem: Element): Boolean = {
    //change span seq to use only the first token
    List.range(1,elem.length).foreach{
      i=>
      elem.remove(i)
    }
    warn("Annotation on pronoun contraction [" + surftxt + "]. Forced to token [" + elem.toString + "]")
    true
  }
  
  mTextExceptions ++= List(
    (Pattern.compile("i'.*",Pattern.CASE_INSENSITIVE), reducePronoun("i")_),
    (Pattern.compile("they'.*",Pattern.CASE_INSENSITIVE), reducePronoun("they")_),
    (Pattern.compile("he'.*",Pattern.CASE_INSENSITIVE), reducePronoun("he")_),
    (Pattern.compile("she'.*",Pattern.CASE_INSENSITIVE), reducePronoun("she")_),
    (Pattern.compile("we'.*",Pattern.CASE_INSENSITIVE), reducePronoun("we")_),
    (Pattern.compile("you'.*",Pattern.CASE_INSENSITIVE), reducePronoun("you")_),
    (Pattern.compile("[a-z]+'[a-z]+",Pattern.CASE_INSENSITIVE), (txt:String,e:Element)=>true), //assume all other contractions are correct
    (Pattern.compile(".*&amp.*",Pattern.CASE_INSENSITIVE), (txt:String,e:Element)=>true) //assume tokens with &amp are just mismatching against &
  )
  
  /**
   * Creates a list of documents from a sentiment corpus file.
   * 
   * @return A list of documents with sentiment annotations.
   */
  def createDocs(fname: String,textpath: String): List[Document] = {
    val docList = new ListBuffer[Document]

    info("Reading xml file: " + fname)
    val scXML = XML.loadFile(fname)

    //get current file path
    val dirpath = (new File(fname)).getParent

    val txt_fname = fname.split("\\/").last.split("\\.").first + ".txt"
    
    var rcnt = 0
    for(docxml <- (scXML \\ "annotations")){
      docList += createFromXML(docxml,dirpath + "/" + textpath + "/" + txt_fname)
    }

    info(docList.length + " Documents loaded from " + fname)
    docList.toList
  }

  /**
   * Creates a documents from a sequence of XML nodes.  Tokenizes the text in the document.
   * 
   * @return A document with sentiment annotations.
   */
  def createFromXML(xmlSeq: NodeSeq,textfname: String): Document = {
      info("Opening text from: " + textfname + " in " + textfname)
      val doc = (new TextDocReader(textfname,new File(textfname))).create
      
      
      //tokenize doc
      //ComboTokenize.process(doc)
      OpenNLPTokenize.process(doc)
      
      //map id to an element for better lookup as we load classes
      val id2elemMap = new HashMap[String,Element]      
      val entSentList = new ListBuffer[(Mention,SentimentPolarity.Value)]
      
      buildSlotHash(xmlSeq)
      buildClassHash(xmlSeq)
      
      //first build all element objects based on the mentionClass
      (xmlSeq\"annotation").foreach{
        elemxml=>
        trace("annotation:" + elemxml)
                
        val id = (elemxml \ "mention" \ "@id").text
        val mentClassXML = getClassMention(id)
        trace("mentionClass: " + (mentClassXML\"mentionClass").text)

        //create the appropriate Element from the mentionClass
        var elem: Element = null
        val classParts = (mentClassXML\\"mentionClass").text.split('.')
        classParts(0) match{
          case "SentimentBearingExpression" => elem = new SentimentExpression
          case "Negator" => elem = new Negator
          case "Intensifier" => elem = new Intensifier
          case "Committer" => elem = new Committer
          case "Neutralizer" => elem = new Neutralizer
          case "OPO" => elem = new OPO
          case "Descriptor" => elem = new Descriptor
//          case "EntityMention" => { //old mentionClass type
//            if ((classParts.length > 1) && (classParts(1).equals("Descriptor"))){
//              elem = new Descriptor
//            }
//            else {
//              elem = new Mention
//              Document.addToProbList(elem.asInstanceOf[Mention].mSemanticType,classParts.drop(1).toList.asInstanceOf[List[String]],null,1.0)
//            }
//          }
          case "Mention" => {
            if ((classParts.length > 1) && (classParts(1).equals("Descriptor"))){
              elem = new Descriptor
            }
            else {
              elem = new Mention
              Document.addToProbList(elem.asInstanceOf[Mention].mSemanticType,classParts.drop(1).toList.asInstanceOf[List[String]],null,1.0)
            }
          }
          case "Comparison" => elem = new Comparison
          case ":THING" => warn("Unknown type of annotation: :THING")
          case "TODO" => warn("Unknown type of annotation: TODO")
          case other => {
            fatal("Unknown annotation type: " + other)
          }
        }

        if (elem != null){
          
          val spstarttxt = (elemxml\"span"\"@start").text
          //create span
          if (spstarttxt.equals("")){
            //malformed span sequence, ignore
            warn("Annotation[" + classParts(0) + "] with id " + id + " has no span, so ignoring")
          }
          else if (spstarttxt.toLong > doc.mTokens.getEndOffset){
            warn("Annotation[" + classParts(0) + "] with id " + id + " has a span offset larger than the document: " + spstarttxt)
          }
          else{
            val stoff = spstarttxt.toLong
            val endoff = (elemxml \ "span" \ "@end").text.toLong
            elem.buildSpanSequence(stoff,endoff,doc)
                  
            //assign id/Annotator
            elem.mMetaMap.put("ID",id)
            val ann = (elemxml \ "annotator")
            elem.mMetaMap.put("Annotator",ann.text)
            elem.mMetaMap.put("AnnotatorID",(ann \ "@id").text)
            elem.mProbabilityOfType = 1.0
            elem.mHasProb = true
            
            //check span
            //released corpus format does not contain the spanned text
//            val spantxt = (elemxml \ "spannedText").text.split("\\s").reduceLeft(_ + _)
//            val toktxt = elem.toStringNoWhiteSpace
//            if (!spantxt.equals(toktxt)){
//              var found = false
//              //check for exceptions
//              mTextExceptions.foreach{
//                exc=>
//                if  (exc._1.matcher(spantxt).matches){
//                  found = exc._2(spantxt,elem) 
//                }
//              }
//              
//              if (!found){
//                error("Annotation text [" + spantxt + "] does not match file text [" + toktxt + 
//                    "] for mention id " + id + "  Annotation spans [" + stoff + "," + endoff + "] token spans [" + 
//                    elem.getStartOffset + "," + elem.getEndOffset + "] len=" + elem.length + " : " + elem.toString)
//                var str = ""
//                elem.foreach{
//                  t=>
//                  str += t.asInstanceOf[Token].mTokenClass.toString + " " 
//                }
//                debug("seq token classes: " + str)
//              }
//            }
            id2elemMap.put(id,elem)
            doc.mElements += elem
          }
        }
      }

      
      //now go through all Elements and fill in the slot information for connecting edges between them
      id2elemMap.keys.foreach{
        id=>
        val elem = id2elemMap(id)
        val mentClassXML = getClassMention(id)
        val slotRefXMLs = (mentClassXML \\ "hasSlotMention")
        trace("Adding refs to " + id)
        
        slotRefXMLs.foreach{
          slotrefxml=>
          val slotid = (slotrefxml \ "@id").text
          val slot = getSlot(slotid)
              
          if (slot != null){
            val slotcomplex = (slot \\ "complexSlotMentionValue")
            val slotstringxml = (slot \\ "stringSlotMentionValue")
            if (slotstringxml.length > 1){
              fatal("More than one slot string value: " + slot)
            }
            val slotstring = (slotstringxml \ "@value").text
            val slottype = (slot\\"mentionSlot"\"@id").text
            trace("slot[" + slotid + "] [" + slottype + "]: " + slot)
            
            elem match {
            case e: SentimentExpression =>{
              slottype match {
                case "Target" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mTargets,target,null,1.0)
                        }),
                      "SentimentExpression" -> ((target: SentimentExpression) => {
                          warn("SE referencing another SE, not linking")
                        })
                      ))
                }
                case "PriorPolarity" => {
                  Document.addToProbList(elem.asInstanceOf[SentimentExpression].mPriorPolarity,
                      SentimentPolarity.getVal(slotstring),null,1.0)
                }
                case "LessImportantThan" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("SentimentExpression" -> ((target: SentimentExpression) => {
                        Document.addToProbList(e.mLessImportantThan,target,null,1.0)
                        Document.addToProbList(target.mLessImportantThan,e,null,1.0)                        
                        })
                      ))
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
            }
            case e: Negator => {
              slottype match {
                case "NegatorTarget" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("SentimentExpression" -> ((target: SentimentExpression) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "Modifier" -> ((target: Modifier) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Mention" -> ((target: Mention) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Comparison" -> ((target: Comparison) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "OPO" -> ((target: OPO) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        })                      
                      ))
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
            }
            case e: Intensifier => {
              slottype match {
                case "IntensifierTarget" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("SentimentExpression" -> ((target: SentimentExpression) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "Modifier" -> ((target: Modifier) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Mention" -> ((target: Mention) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Comparison" -> ((target: Comparison) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "OPO" -> ((target: OPO) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        })                      
                      ))
                }
                case "IntensifierDirection" => {
                  var v: IntensifierDirection.Value = 
                    if (slotstring.equals("Strengthen")) IntensifierDirection.ID_UPWARD  
                    else if (slotstring.equals("Weaken")) IntensifierDirection.ID_DOWNWARD
                    else {
                      error("No intensifier direction found for " + slotstring)
                      IntensifierDirection.ID_NONE
                    }
                  Document.addToProbList(elem.asInstanceOf[Intensifier].mDirection,v,null,1.0)
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
            }   
            case e: Committer => {
              slottype match {
                case "CommitterTarget" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Modifier" -> ((target: Modifier) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "SentimentExpression" -> ((target: SentimentExpression) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Mention" -> ((target: Mention) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Comparison" -> ((target: Comparison) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "OPO" -> ((target: OPO) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        })
                      ))
                }
                case "CommitterDirection" => {
                  val v = if (slotstring.equals("Upward")) IntensifierDirection.ID_UPWARD  
                    else if (slotstring.equals("Downward")) IntensifierDirection.ID_DOWNWARD
                    else {
                      error("No intensifier direction found for " + slotstring)
                      IntensifierDirection.ID_NONE
                    }
                  elem.asInstanceOf[Committer].mDirection = new ProbLink(1.0,v,null)
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
            }
            case e: Neutralizer => {
              slottype match {
                case "NeutralizerTarget" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("SentimentExpression" -> ((target: SentimentExpression) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "Modifier" -> ((target: Modifier) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Mention" -> ((target: Mention) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          Document.addToProbList(target.mParents,e,null,1.0)
                        }),                      
                        "Comparison" -> ((target: Comparison) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        }),
                        "OPO" -> ((target: OPO) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                          //Document.addToProbList(target.mParents,e,null,1.0)
                        })                      
                      ))
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
            }                            
            case e: OPO => {
              slottype match {
                case "OPOTarget" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("SentimentExpression" -> ((target: SentimentExpression) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                        }),
                        "OPO" -> ((target: OPO) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                        }),
                        "Mention" -> ((target: Mention) => {
                          warn("Ignoring link from OPO targeting a Mention: " + id)
                        }),                    
                        "Modifier" -> ((target: Modifier) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                        })                    
                      ))
                }
                case "OPOSource" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mSources,target,null,1.0)
                        })
                      ))
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
              
            }              
            case e: Mention => {
              slottype match {
                case "Target" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                          Document.addToProbList(e.mTargets,target,null,1.0)
                        })
                      ))
                }
                case "RefersTo" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                          Document.addToProbList(e.mReferTo,target,null,1.0)
                        })
                      ))
                }
                case "inverse_of_RefersTo_" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mParentReferTo,target,null,1.0)
                        })
                      ))
                }
                case "PartOf" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mPartOf,target,EntityRelationType.ERT_PART_OF,1.0)
                        Document.addToProbList(target.mParentPartOf,e,EntityRelationType.ERT_PART_OF,1.0)
                        })
                      ))
                  debug("Creating PartOf")
                }
                case "InstanceOf" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mPartOf,target,EntityRelationType.ERT_INSTANCE_OF,1.0)
                        Document.addToProbList(target.mParentPartOf,e,EntityRelationType.ERT_INSTANCE_OF,1.0)
                        })
                      ))
                  debug("Creating InstanceOf")
                }
                case "MemberOf" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mPartOf,target,EntityRelationType.ERT_MEMBER_OF,1.0)
                        Document.addToProbList(target.mParentPartOf,e,EntityRelationType.ERT_MEMBER_OF,1.0)
                        })
                      ))
                  debug("Creating MemeberOf")
                }
                case "FeatureOf" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((t: Mention) => {
                        Document.addToProbList(e.mPartOf,t,EntityRelationType.ERT_FEATURE_OF,1.0)
                        Document.addToProbList(t.mParentPartOf,e,EntityRelationType.ERT_FEATURE_OF,1.0)
                        }),
                        "SentimentExpression" -> ((t: SentimentExpression) => {
                        })
                      ))
                  debug("Creating FeatureOf")
                }
                case "Produces" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mPartOf,target,EntityRelationType.ERT_PRODUCES,1.0)
                        Document.addToProbList(target.mParentPartOf,e,EntityRelationType.ERT_PRODUCES,1.0)
                        })
                      ))
                  debug("Creating Produces")
                }
                case "AccessoryOf" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mPartOf,target,EntityRelationType.ERT_ACCESSORY_OF,1.0)
                        Document.addToProbList(target.mParentPartOf,e,EntityRelationType.ERT_ACCESSORY_OF,1.0)
                        })
                      ))
                  debug("Creating AccessoryOf")
                }
                case "Describes" => {
                  warn("Ignoring Describes relation on a Mention")
                }
                case "EntitySentiment" => {
                  entSentList += Tuple2(e,SentimentPolarity.getVal(slotstring))
                }
                case "MentionPriorPolarity" => {
                  Document.addToProbList(elem.asInstanceOf[Mention].mPriorPolarity,null,
                      SentimentPolarity.getVal(slotstring),1.0)
                }
                case "ContextualSentiment" => {
                  Document.addToProbList(elem.asInstanceOf[Mention].mContextualPolarity,null,
                      SentimentPolarity.getVal(slotstring),1.0)
                }
                case "EMLevel" => {
                  //do nothing, throw this out
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }
              
            }
            case e: Comparison => {
              slottype match {
                case "Dimension" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("SentimentExpression" -> ((target: SentimentExpression) => {
                        e.mDimension = target
                        }),
                      "Mention" -> ((target: Mention) => {
                        e.mDimension = target
                        })
                      ))
                }
                case "More" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mMore,target,null,1.0)
                        })
                      ))
                }
                case "Less" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map("Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mLess,target,null,1.0)
                        })
                      ))
                }
                case "Same" => {
                  if ((slot\"booleanSlotMentionValue"\"@value").text.equals("true")) e.mSame = true
                  else e.mSame = false
                }
                case other => {
                  fatal("don't know how to apply " + other + " to annotation " + id)
                }
              }              
            }              
            case e: Descriptor => {
              slottype match {
                case "Describes" => {
                  createLink(slotcomplex,slot,id2elemMap,
                      Map(
                      "Mention" -> ((target: Mention) => {
                        Document.addToProbList(e.mDescribes,target,null,1.0)
                        })
                      ))
                }
                case "EMLevel" => {
                  //do nothing, throw this out
                }
                case other => {
                  warn("Relation " + other + " can't be applied to to Describes " + id)
                }
              }              
            }                          
            case other => {
              fatal("Unknown annotation type: " + other)
            }
          }
          }
        }
      }
      
      var ent_id = 0
      //now go through all mentions and create the Entities for the Document
      doc.mElements.foreach{
        elem=>
        if (elem.isInstanceOf[Mention]){
          val ent = elem.asInstanceOf[Mention].buildEntity()
          if (ent != null){
            ent.mMetaMap.put("ENT_ID",ent_id.toString)
            ent_id += 1
            doc.mEntities += ent
          }
        }
      }
      
      //now fill in PartOf info for the entities
      doc.mEntities.foreach{
        ent=>
        ent.createPartOfLists()
      }      
      
      //fill in entitySentiment
      entSentList.foreach{
        s=>
        Document.addToProbList(s._1.mSources(0).ref.mEntityPolarity,null,s._2,1.0)
      }
      
      doc
  }
 

  //Map of Real class name to provided class name
  //this is a bit of a hack
  val mLinkClassMap = Map(
      "Committer" -> "Modifier",
      "Comparison" -> "Comparison",
      "Intensifier" -> "Modifier",
      "Mention" -> "Mention",
      "Negator" -> "Modifier",
      "Neutralizer" -> "Modifier",
      "OPO" -> "OPO",
      "SentimentExpression" -> "SentimentExpression"
      )
      
  
  /**
   * Looks up an element instance from the provided map based on the id, checks that the id is defined,
   * and then runs the caller's code for this Element type to perform the link.
   * 
   * @param slotcomplex The complex values for the linked element
   * @param slot the XML representing the slot (only used for debugging)
   * @param id2elemMap The map between ids and element instances
   * @param fMap A map to map Element subclass names to a function which will add the link.
   */
  private def createLink[T <: Element](slotcomplex: NodeSeq,slot: NodeSeq,
      id2elemMap: HashMap[String,Element],fMap: Map[String,(T)=>Unit]) {
    slotcomplex.foreach{
      valxml=>
      val refid = (valxml\"@value").text
      val tOpt = id2elemMap.get(refid)
      if (tOpt.isDefined){
        val t = tOpt.get
        val lcOpt = mLinkClassMap.get(t.getClass.getName.drop(36))  //remove "com.jdpa.mlg.science.datastructures." 
        if (lcOpt.isDefined){
          val fOpt = fMap.get(lcOpt.get)
          debug("Adding link")
          if (fOpt.isDefined) fOpt.get.apply(t.asInstanceOf[T])
          else{
            error("Slot " + slotcomplex + " has unsupported target type: " + slot + " class: " + t.getClass.getName)
          }
        }
        else{
          error("Slot " + slotcomplex + " has unsupported target type: " + slot + " class: " + t.getClass.getName)
        }
      }
      else{
        error("Unable to find refid: " + refid)
      }
    }
  }
      
  def buildSlotHash(annxml: NodeSeq){
    (annxml\\"complexSlotMention").foreach{
      s=>
      mSlotHashMap.put((s\"@id").text,s)
    }
    (annxml\\"stringSlotMention").foreach{
      s=>
      mSlotHashMap.put((s\"@id").text,s)
    }
    (annxml\\"booleanSlotMention").foreach{
      s=>
      mSlotHashMap.put((s\"@id").text,s)
    }
  }
  
  def buildClassHash(annxml: NodeSeq){
    (annxml\\"classMention").foreach{
      s=>
      mClassHashMap.put((s\"@id").text,s)
    }    
  }
  
  def getSlot(id: String): NodeSeq = {
      val resultOpt = mSlotHashMap.get(id)
      if (resultOpt.isDefined) resultOpt.get
      else null
  }

  def getClassMention(id: String): NodeSeq = {
      val resultOpt = mClassHashMap.get(id)
      if (resultOpt.isDefined) resultOpt.get
      else {
        fatal("Unable to find " + id + " in classMention hash")
        null
      }
  }

}
