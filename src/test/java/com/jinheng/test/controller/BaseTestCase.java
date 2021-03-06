package com.jinheng.test.controller;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Convenience generic class to make tests simpler.<br/>
 * Extending Spring's JUnits API.<br/>
 * Runwith already set.<br/>
 * ContextConfiguration already set here.
 * 
 * @author azliabdullah
 */
@ContextConfiguration({ "file:src/test/resources/test-servlet-context.xml", "file:src/test/resources/test-root-context.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public abstract class BaseTestCase extends AbstractTransactionalJUnit4SpringContextTests {

	protected transient final Log logger = LogFactory.getLog(getClass());
	private static final String defaultMobileRequestURL = "/s/";
	private static final String defaultMobileRequestURLInit = "/init/";

	@Before
	public void setup() {
		logger.debug("Setting up test");
	}

	@After
	public void afterTest() {
		logger.debug("Test Finished");
	}

	/**
	 * A mock of mobile request sent to server.<br/>
	 * By default its like sending request from mobile app to (server_url)/s/
	 * 
	 * @return a MockHttpServletRequest with a POST to (server_url)/s/
	 */
	public MockHttpServletRequest newMobileRequest() {
		MockHttpServletRequest mhsr = new MockHttpServletRequest("POST", defaultMobileRequestURL);
		mhsr.setContentType("text/plain");
		return mhsr;
	}

	/**
	 * A mock of mobile request sent to server for init service only.<br/>
	 * By default its like sending request from mobile app to (server_url)/init/
	 * 
	 * @return a MockHttpServletRequest with a POST to (server_url)/init/
	 */
	public MockHttpServletRequest newMobileRequestInit() {
		MockHttpServletRequest mhsr = new MockHttpServletRequest("POST", defaultMobileRequestURLInit);
		return mhsr;
	}

	/**
	 * To create a new Locale object
	 * 
	 * @param language (String) the language, eg: en (English)
	 * @return a Locale
	 */
	public Locale newLocale(String language) {
		Locale locale = new Locale(language);
		return locale;
	}

	/**
	 * A mock of response sent from server to mobile app.
	 * 
	 * @return a MockHttpServletResponse
	 */
	public MockHttpServletResponse newResponse() {
		return new MockHttpServletResponse();
	}

	/**
	 * A mock of any request sent to server with POST method.<br/>
	 * Suitable to be used in portal
	 * 
	 * @param url the URL to post to
	 * @return a MockHttpServletRequest with a POST to the specified URL
	 */
	public MockHttpServletRequest newPostRequest(String url) {
		return new MockHttpServletRequest("POST", url);
	}

	/**
	 * A mock of any request sent to server with GET method.<br/>
	 * Suitable to be used in portal
	 * 
	 * @param url the URL to get from
	 * @return a MockHttpServletRequest with a GET to the specified URL
	 */
	public MockHttpServletRequest newGetRequest(String url) {
		return new MockHttpServletRequest("GET", url);
	}
}
