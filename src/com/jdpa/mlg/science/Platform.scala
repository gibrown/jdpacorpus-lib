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
 *  @author Gregory Ichneumon Brown
 */

package com.jdpa.mlg.science

import com.jdpa.mlg.science.utils.ParamUtils
import com.jdpa.mlg.science.utils.Log
import com.jdpa.mlg.science.datastructures.Document

import java.io._
import org.slf4j.Logger
import java.text.{SimpleDateFormat,FieldPosition}
import java.util.Date
import java.lang.StringBuffer
import scala.collection.mutable.HashMap

import org.apache.commons.cli._

/**
 * Abstract class for interpretting command line, creating runtime directories,
 * configuring the logger, and it contains the main platform specific parameters.
 *
 * This class should be extended by any object with a main function to provide
 * access to the standard Platform object, system properties loading, and
 * command line parsing.
 *
 * The same parameters that are in the properties files can also be added
 * to the command line option interface.  If they have the same name they will
 * be automatically updated. (eg: expname is in both properties and cli)
 * 
 *
 */
object Platform extends ParamUtils(List(
  ("expname","test"),
  ("datelog","false")
  )){

  val OUTPUT_DIR = "output"
  var mProcessName = ""
    
  def resultsPath(): String = {
    OUTPUT_DIR + "/" + Platform("expname")
  }
  
}

abstract class Platform extends Log{
  

  //list of directories to make in the experime's results directory
  val mDirList = List("depar","pos","token")

  //create command line options list
  val mOptions = new Options
  mOptions.addOption("expname",true,"The experiment name")
  mOptions.addOption("datelog",true,"Uniquify the log names using a date/time stamp")
  mOptions.addOption("h","help",false,"Print usage info")

  var mArg: CommandLine = null

  private val mLogAppendMap = new HashMap[String,Boolean]
  
  /**
   * Configures the Platform parameters, creates the results directories,
   * configures the logging file path names, and parses the command line options
   * and uses them to update the parameter lists.
   * 
   * Logfile names become (expname).(processName).(orig log file name)
   * eg depar.log becomes: exp1.train.depar.log
   * 
   * Optionally the log file name can also include the datestamp:
   * (expname).(processName).(datestamp).(orig log file name)
   * 
   * @param args The command line arguments
   * @param params The ParamUtils object for the main process that can get updated from the command line
   * @param processName The string to output in the logfile name
   * 
   */ 
  def startup(args: Array[String],params: ParamUtils,processName: String): Unit = {
    Platform.mProcessName = processName
    //load/configure platform parameters
    this.loadParams("science.properties")

    //parse the command line options
    val parser = new PosixParser()
    try{
      mArg = parser.parse( mOptions, args )
    } catch {
      case e: Exception => fatal("Command Parsing failed",e)
    }
    
    //output usage info
    if (mArg.hasOption("help")){
      val formatter = new HelpFormatter()
      formatter.printHelp("Platform", mOptions )
      System.exit(0)
    }

    //and update all Platform parameters with the same name
    Platform.mParams.keys.foreach{
      p=>
      if (mArg.hasOption(p)){
        Platform.mParams(p) = mArg.getOptionValue(p)
      }
    }
    //and update all other parameters with the same name
    if (params != null){
      params.mParams.keys.foreach{
        p=>
        if (mArg.hasOption(p)){
          params.mParams(p) = mArg.getOptionValue(p)
        }
      }
    }

    //update the logging file path names
    var dateStr = ""
    if (this("datelog").toLowerCase.equals("true")){
      val projDate = new Date()
      val sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
      val dateStrBuf = new StringBuffer
      sdf.format(projDate, dateStrBuf, new FieldPosition(0))
      dateStr = "." + dateStrBuf.toString
    }
    val logFilePrefix = "log/" + this("expname") + "." + processName + dateStr
    info("\n**************\nLog file prefix for this run: " + logFilePrefix + "\n**************\n")

    //this is log4j specific, but appears to be no way to dynamically change slf4j
   
    //loop through all defined loggers and update all appenders
    val loggers = org.apache.log4j.LogManager.getCurrentLoggers
    while(loggers.hasMoreElements){
      val appenders = loggers.nextElement().asInstanceOf[org.apache.log4j.Logger].getAllAppenders()
      //all files should have the log prefix added to their filename
      while(appenders.hasMoreElements()){
        val currAppender = appenders.nextElement().asInstanceOf[org.apache.log4j.Appender]
        val name = currAppender.getName
        if (mLogAppendMap.get(currAppender.getName).isEmpty){
          trace("found appender: " + name + " " + currAppender.getClass.getName)
          if (currAppender.isInstanceOf[org.apache.log4j.FileAppender]){
            updateAppender(currAppender.asInstanceOf[org.apache.log4j.FileAppender],logFilePrefix)
          }
          mLogAppendMap(name) = true
        }
      }
    }

    //update appenders on the rootLogger
    val rootLogger = org.apache.log4j.LogManager.getRootLogger
    val appenders = rootLogger.getAllAppenders()
    //all files should have the log prefix added to their filename
    while(appenders.hasMoreElements()){
      val currAppender = appenders.nextElement().asInstanceOf[org.apache.log4j.Appender]
      val name = currAppender.getName
      if (mLogAppendMap.get(currAppender.getName).isEmpty){ 
        trace("found appender: " + name + " " + currAppender.getClass.getName)
        if (currAppender.isInstanceOf[org.apache.log4j.FileAppender]){
          updateAppender(currAppender.asInstanceOf[org.apache.log4j.FileAppender],logFilePrefix)
        }
        mLogAppendMap(name) = true
      }
    }
    
    Platform.printParams
    if (params != null) params.printParams
    
    //create the result directories for this experiment
    try{
      val expdir = Platform.resultsPath + "/"
      mDirList.foreach{
        str=>
        val success = (new File(expdir + str)).mkdirs
        null
        //if can't create, assume already exists
      }
    } catch  {
      case e:Exception => fatal("Unable to create directories.",e)
    }


  }

  /**
   * Updates the Logging appender's file path with the experiment name, etc
   * 
   * @param app The appender to update
   * @param namePrefix The prefix to add to the log file name
   */
  private def updateAppender(app: org.apache.log4j.FileAppender,namePrefix: String): Unit = {
    var str = app.getFile
    trace("updating log file name: " + str)
    if (str.startsWith("log/")) {
      str = str.slice(4,str.length)
    }
    app.setFile(namePrefix + "." + str)
    app.activateOptions()
  }
  
  /**
   * Make this look like a ParamUtils class, but really access the Platform object
   * 
   * @param str The parameter to lookup 
   * @return The value of the parameter
   */
  def apply(str:String): String = {
    Platform(str)
  }

  /**
   * Make this look like a ParamUtils class, but really access the Platform object
   * 
   * @param str The file name of the properties file.
   */
  def loadParams(str:String) {
    Platform.loadParams(str)
  }
  
  /**
   * Abstract method for processing a series of documents.  The documents are
   * updated/modified by this method.
   * 
   * @param docs The list of documents to be processed by this element
   */
  def run(docs: List[Document]): Unit
	
}
