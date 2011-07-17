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
 * Default pipeline Parameter list.  Can be overridden from the
 * pipeline.properties file
 * 
 * @author Gregory Ichneumon Brown
 */
package com.jdpa.mlg.science.pipeline

import scala.collection.mutable.ListBuffer

import com.jdpa.mlg.science.datastructures.POS

import com.jdpa.mlg.science.utils.ParamUtils

object Params extends ParamUtils(List(
		
    //////////Files to input into the pipeline//////////////
    //if IFILE_DIR is not blank, then the pipeline will recursively descend from
    //the specified directory and load all files that match the IFILE_NAME suffix
    ("IFILE_DIR",""), 

    //IFILE_LIST specifies a file that contains a list of the files to load.  This var
    //supercedes all others unless it is blank.
    ("IFILE_LIST",""), 

    //if IFILE_DIR is blank, then IFILE_NAME is the path to a single file to load
    //(which could contain multiple documents), otherwise it is the suffix for files
    ("IFILE_NAME",""),

    //if IFILE_DIR is blank, then IFILE_NAME is the path to a single file to load
    //(which could contain multiple documents), otherwise it is the suffix for files
    ("IFILE_NAME",""),

    //the type of reader to use to read the files
    ("IFILE_FORMAT","text"), 

    //generic list of arguments to pass into the particular pipeline (pipeline specific arguments)
    ("PIPELINE_ARGS",""),

    /////////Files output from the pipeline////////////////
    //all output files are placed in output/<expname>/<pipelinename>/.

    //if true then all docs are output into a single file, otherwise they are
    //separated into multiple files prefixed with their mName
    //if there is a single file, then it will be named <pipelinename><OFILE_SUFFIX>
    ("OFILE_SINGLE","true"),

    //The suffix to place at the end of the file name(s)
    ("OFILE_SUFFIX",".out"),

    //the type of writer to use to write the files
    ("OFILE_FORMAT","none") 

)){
	
}
