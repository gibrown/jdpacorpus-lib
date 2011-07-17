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
 * Class to process all of the files in a particular directory structure. 
 * 
 * @author Gregory Ichneumon Brown
 * 
 */
package com.jdpa.mlg.science.utils

import java.io._
import scala.collection.mutable.HashMap


object ProcessDirectory extends Log{

 /**
  * Process each file in the next level of directories that matches a particular filter.
  * For each file, run the function f on that file.  
  *  
  * @param dirPath Path to the directory to be processed
  * @param filterSuffix File suffix string to match against (eg .txt)
  * @param func Function to run on that file.  The function must take two strings as input 
  * (generally input and output file names)
  * 
  * @return A map of the results returned by calling f, mapping the filename to the result. 
  */
  def processAllDirs[A](dirPath: String, 
                        filterSuffix: String,
                        func: (String) => A): HashMap[String,A] = {  
    val mapResults = new HashMap[String,A]
    val topDir = new File( dirPath )
    val dirs = topDir.listFiles()
        
    debug( "Processing directory: " + topDir.getPath() )
    try{
      for (dir<-dirs){ 
        if (dir.isDirectory()) {
          debug( "Processing embedded directory: " + dir.getPath() )
          val files = dir.listFiles();
          for (file<-files) {
            if (file.isFile() && file.getPath().endsWith( filterSuffix )){
              val filePath = file.getPath()
              debug( "  Processing file: " + filePath )
              mapResults(filePath) = func(filePath)
            }
          }
        }
      }
    } catch {
      case ioe: IOException => fatal("Could not access file",ioe)
      case e: Exception => fatal("Unhandled exception",e)
    }
    mapResults
  }
    
 /**
  * Recursively process all files in the directory structure.
  * For each file, run the function f on that file.  
  *  
  * @param dirPath Path to the directory to be processed
  * @param filterSuffix File suffix string to match against (eg .txt)
  * @param func Function to run on that file.  The function must take a filename as input.
  * 
  * @return A map of the results returned by calling f, mapping the filename to the result. 
  */
  def processRecursive[A](dirPath: String, 
                       filterSuffix: String,
                       func: (String) => A): HashMap[String,A] ={  
    val mapResults = new HashMap[String,A]
    val topDir = new File( dirPath )
    var files = topDir.listFiles()    
        
    var currFName = ""
    debug( "Processing directory: " + topDir.getPath() )
    try{
      for (file <- files) {
        if (file.isDirectory()){
          mapResults ++= processRecursive(file.getPath(),filterSuffix,func)
        } 
        else if (file.isFile() && file.getPath().endsWith( filterSuffix )){
          val filePath = file.getPath()
          currFName = filePath
          debug( "*** Processing file: " + filePath )
          mapResults(filePath) = func(filePath)
        }
      }
    } catch {
      case ioe: IOException => fatal("Could not access file",ioe)
      case e: Exception => fatal("Unhandled exception in file: " + currFName,e)
    }
    mapResults
  }

  
 /**
  * Process each file in the given directory that matches a filter
  * For each file, run the function f on that file.  
  *  
  * @param dirPath Path to the directory to be processed
  * @param filterSuffix File suffix string to match against (eg .txt)
  * @param func Function to run on that file.  The function must take two strings as input 
  * (generally input and output file names)
  * 
  * @return A map of the results returned by calling f, mapping the filename to the result. 
  */
  def processNotRecursive[A](dirPath: String, 
                             filterSuffix: String,
                             func: (String) => A): HashMap[String,A] = {  
    val mapResults = new HashMap[String,A]
    val topDir = new File( dirPath )
    val files = topDir.listFiles()
        
    debug( "Processing directory: " + topDir.getPath() )
    try{
      for (file<-files){ 
        if (file.isFile() && file.getPath().endsWith( filterSuffix )){
          val filePath = file.getPath()
          debug( "  Processing file: " + filePath )
          mapResults(filePath) = func(filePath)
        }
      }
    } catch {
      case ioe: IOException => fatal("Could not access file",ioe)
      case e: Exception => fatal("Unhandled exception",e)
    }
    mapResults
  }
  
}
