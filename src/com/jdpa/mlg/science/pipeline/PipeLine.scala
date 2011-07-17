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
 * Abstract Pipeline class for running a series of processing elements
 * in succession.  All pipelines share the same .properties file with the idea that
 * only the top main pipeline actually needing to use the properties file and the
 * run methods of all subsequent pipelines called can just be passed the documents needed.
 * 
 * @author Gregory Ichneumon Brown
 *
 */
package com.jdpa.mlg.science.pipeline

import com.jdpa.mlg.science.Platform
import com.jdpa.mlg.science.utils.Timer
import com.jdpa.mlg.science.utils.ProcessDirectory
import com.jdpa.mlg.science.readers._
import com.jdpa.mlg.science.writers._
import com.jdpa.mlg.science.datastructures.Document
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.HashMap
import java.io.File
import scala.io._

abstract class PipeLine extends Platform with Timer {

  /**
   * Map of formats to the functions for processing that format type.
   * To add new formats just add to the hashmap.  This map is also used
   * for checking that the IFILE_FORMAT and OFILE_FORMAT parameters are
   * valid
   */
  val mIFormatMap = new HashMap[String,()=>List[Document]]
  val mOFormatMap = new HashMap[String,(List[Document])=>Unit]

  ////////////////////////
  //input formats
  ////////////////////////
  mIFormatMap.put("TextDocReader",()=>{
    stdReader((fname: String) => {
      val file = new File(fname)
      (new TextDocReader(file.getName,file)).create
    })
  })
  mIFormatMap.put("SentimentCorpusReader",()=>{
    stdMultiReader((fname: String) => {
      SentimentCorpusReader.createDocs(fname,Params("IFILE_TEXT_DIR"))
    })
  })
  mIFormatMap.put("DocObjectReader",()=>{
    stdMultiReader((fname: String) => {
      DocObjectReader.createDocs(fname)
    })
  })
  

  /**
   * Standard Multi/Single file reader which works well if the Reader class implements a 
   * create(): Document method.
   * 
   * @param f The function that defines how to read a document from a file name
   * 
   * @return The list of documents read in
   */
  def stdReader(f: (String) => Document): List[Document] = {
    val multi = if (Params("IFILE_DIR") == "") false else true
    val lst = if (Params("IFILE_LIST") == "") false else true
    if (lst){
      var files = Source.fromFile(Params("IFILE_LIST"),"utf-8").getLines
      val docs = new ListBuffer[Document]
      files.foreach{
        file=>
        docs += f(Params("IFILE_DIR") + "/" + file)
      }
      docs.toList
    }
    else{
      if (multi){
        ProcessDirectory.processRecursive(Params("IFILE_DIR"),
                                          Params("IFILE_NAME"),
                                          f
                                          ).values.toList
      }
      else{
        List(f(Params("IFILE_NAME")))
      }
    }    
  }

  /**
   * Standard Multi/Single file reader which works well if the Reader class implements a 
   * create(): List[Document] method.  For use with file formats that can have multiple documents
   * in a single file.
   * 
   * @param f The function that defines how to read a list document from a file name
   * 
   * @return The list of documents read in
   */
  def stdMultiReader(f: (String) => List[Document]): List[Document] = {
    val multi = if (Params("IFILE_DIR") == "") false else true
    val lst = if (Params("IFILE_LIST") == "") false else true
    if (lst){
      var files = Source.fromFile(Params("IFILE_LIST"),"utf-8").getLines
      val docs = new ListBuffer[Document]
      files.foreach{
        file=>
        docs ++= f(Params("IFILE_DIR") + "/" + file)
      }
      docs.toList
    }
    else{
      if (multi){
        ProcessDirectory.processRecursive(Params("IFILE_DIR"),
                                          Params("IFILE_NAME"),
                                          f
                                          ).values.toList.flatten
      }
      else{
        f(Params("IFILE_NAME"))
      }
    }
  }
  
  
  ////////////////////
  //output formats
  ////////////////////
  mOFormatMap.put("none",(docs: List[Document])=>{
    //do nothing
  })
  mOFormatMap.put("DocObjectWriter",(docs: List[Document])=>{
    stdWriter(docs,(docs: List[Document],fname: String) =>{
      DocObjectWriter.create(docs,fname)
    })
  })
  
  

  /**
   * Standard Multi/Single file writer which works well if the Writer class implements a 
   * create(List[Document]) method.  Uniquifies names if the document names (doc.mName) happen
   * to be the same.
   * 
   * @param docs The list of documents to write
   * @param f The function that defines how to write a document in a particular format
   */
  def stdWriter(docs: List[Document],f: (List[Document],String) => Unit): Unit = {
    val multi = if (Params("OFILE_SINGLE").equals("true")) false else true
    val path = Platform.resultsPath
    val suffix = Params("OFILE_SUFFIX")
    if (multi){
      //create hash for checking that all file names are unique
      val fnameMap = new HashMap[String,Int]
      
      docs.foreach{
        doc=>
        var fname = doc.mName
        if (fnameMap.contains(fname)){
          warn("Duplicate output file names: " + fname + " uniquifying to " + fname + "_" + fnameMap(fname))
          fname += "_" + fnameMap(fname)
        }
        else{
          fnameMap.put(fname,1)          
        }
        f(List(doc),path + "/" + fname + suffix)
      }
    }
    else{
      f(docs,path + "/" + Platform.mProcessName + suffix)
    }    
  }  
  
  /**
   * Generic startup for a pipeline to process the command line options and
   * load the properties file.
   * 
   * @param args
   */
  def startup(args: Array[String],processName: String){
    Params.loadParams("pipeline.properties")
    super.startup(args,Params,processName)
    checkuseage
  }

  /**
   * Check whether all the needed parameters are available and the formats
   * specified are supported.  Exits with error message on any failures.
   * 
   * This method tries to check everything we can before the real processing starts
   * so there is less likelihood of crashing after running for many hours just because
   * the output file is specified incorrectly.
   *
   */  
  protected def checkuseage(){
    //check that input files are specified
    if (Params("IFILE_DIR").equals("")){
      //single input file
      val ifile = new File(Params("IFILE_NAME"))
      if (!ifile.isFile()){
        fatal(ifile.toString + " does not exist!")
      }
    }
    else{
      //input directory
      val ifile = new File(Params("IFILE_DIR"))
      if (!ifile.isDirectory()){
        fatal(ifile.toString + " is not a directory that can be accessed!")
      }      
      if (Params("IFILE_NAME").equals("")){
        warn("No suffix specified for input files, will read all files in the directory.")
      }
    }
    
    //check that output files are specified
    if (Params("OFILE_SUFFIX").equals("")){
      warn("No suffix specified for output files, will only have the document names.")
    }
    if (!(Params("OFILE_SINGLE").equals("true") || Params("OFILE_SINGLE").equals("false"))){
      fatal("OFILE_SINGLE must be set to either 'true' or 'false', currently:" + Params("OFILE_SINGLE"))
    }

    
    //check the file formats
    if (!mIFormatMap.contains(Params("IFILE_FORMAT"))){
      error("Supported input formats:")
      mIFormatMap.keys.foreach{
        k=> error("\t" + k)
      }
      fatal("Specified input file format not supported: " + Params("IFILE_FORMAT"))
    }
    if (!mOFormatMap.contains(Params("OFILE_FORMAT"))){
      error("Supported output formats:")
      mOFormatMap.keys.foreach{
        k=> error("\t" + k)
      }
      fatal("Specified output file format not supported: " + Params("OFILE_FORMAT"))
    }
    
  }
  
  /**
   * Loads a series of documents based on the format specified by IFILE_FORMAT
   * 
   * @return The loaded list of documents
   */
  def loadDocs(): List[Document] = {
    val r = timeRun((mIFormatMap(Params("IFILE_FORMAT")))())
    val docs = r._1
    val secs = r._2/1000.0

    info("Loaded " + docs.length + " documents in " + secs + " sec (" + (docs.length/secs) + " docs/sec)");    
    docs
  }
  
  /**
   * Saves a series of documents based on the format specified by OFILE_FORMAT
   * 
   * @param docs The list of documents to save.
   */
  def saveDocs(docs: List[Document]): Unit = {
    val multi = if (Params("OFILE_SINGLE") == "true") false else true
    val r = timeRun((mOFormatMap(Params("OFILE_FORMAT")))(docs))
    val secs = r._2/1000.0

    info("Wrote " + docs.length + " documents in " + secs + " sec (" + (docs.length/secs) + " docs/sec)");
 }
  
}
