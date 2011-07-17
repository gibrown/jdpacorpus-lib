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
 * Top level JUnit test suite for JDPA Corpus Library
 *
 */


package com.jdpa.mlg.science.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.jdpa.mlg.science.tokenize.*;
import com.jdpa.mlg.science.utils.*;
import com.jdpa.mlg.science.datastructures.*;

@RunWith(Suite.class)
@Suite.SuiteClasses( { 
		//TokenizeTestSuite.class,
		TimerTestSuite.class
		//InstanceWriterReaderTestSuite.class
  })
public class AllTests {


}
