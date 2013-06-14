package com.singly.spring;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.HttpException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.singly.client.SinglyAccountStorage;
import com.singly.client.SinglyApiException;
import com.singly.client.SinglyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/conf/webapp-context.xml" })
public class SinglyTest {

	@Autowired
	private SinglyService singlyService;

	@Autowired
	private SinglyAccountStorage accountStorage;

	@Test
	public void test() {

		{
			/*
			 * enumerate services
			 */
			String apiEndpoint = "/services";
			System.out.println(apiEndpoint);

			Map<String, String> params = null;
			String servicesJson = singlyService.doGetApiRequest(apiEndpoint,
					params);
			System.out.println(servicesJson);
			System.out.println("----");
		}

		{
			/*
			 * get authentication url for a service
			 */
			String account = null;
			String service = "bodymedia";
			String redirectUrl = "http://this.can/be/anthing";
			Map<String, String> authExtra = null;
			String authenticationUrl = singlyService.getAuthenticationUrl(
					account, service, redirectUrl, authExtra);
			System.out.println("authenticationUrl");
			System.out.println(authenticationUrl);
			System.out.println("----");
		}

		{
			/*
			 * exchange an account token with an authentication code
			 */
			String authCode = "fc4b26c1c5925451928ad6ea2724e710";
			String account = singlyService.completeAuthentication(authCode);
			System.out.println("account");
			System.out.println(account);
			System.out.println("----");
		}

		{
			/*
			 * verify authentication code
			 */
			String account = "6166ea2221a33ed468417155483a5853";
			boolean authenticated = singlyService.isAuthenticated(account);
			System.out.println("authenticated");
			System.out.println(authenticated);
			System.out.println("----");
		}

		/*
		 * now, the authentication is complete
		 */

		{
			/*-
			 * https://singly.com/docs/appfabric/profiles#Managing-Profiles
			 */
			String apiEndpoint = "/profiles";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		{
			/*-
			 * https://singly.com/docs/appfabric/profiles#Simple-Unified-Profile
			 */
			String apiEndpoint = "/profile";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		{
			/*-
			 * https://singly.com/docs/appfabric/profiles#Service-Profiles
			 */
			String apiEndpoint = "/profile/bodymedia";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		{
			/*-
			 * https://singly.com/docs/appfabric/friends#Discovery
			 * 
			 * XXX not works
			 */
			String apiEndpoint = "/friends";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		{
			/*-
			 * https://singly.com/docs/appfabric/friends#Table-of-Contents
			 * 
			 * XXX not works
			 */
			String apiEndpoint = "/friends/all";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		{
			/*-
			 * https://singly.com/docs/data/types_overview#:type-vs.-:type_feed
			 * 
			 * XXX not works
			 */
			String apiEndpoint = "/types/photos";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		{
			/*-
			 * https://singly.com/docs/data/types_overview#Discovery
			 * 
			 * XXX not works
			 */
			String apiEndpoint = "/types";
			String account = "6166ea2221a33ed468417155483a5853";
			Map<String, String> qparams = new LinkedHashMap<String, String>();
			qparams.put("access_token", accountStorage.getAccessToken(account));
			get(apiEndpoint, qparams);
		}

		return;
	}

	private void get(String apiEndpoint, Map<String, String> qparams) {
		try {
			System.out.println(apiEndpoint);

			String profilesJson = singlyService.doGetApiRequest(apiEndpoint,
					qparams);
			System.out.println(profilesJson);
		} catch (SinglyApiException ex) {
			if (ex.getCause() instanceof HttpException) {
				System.out.println(ex.getLocalizedMessage());
			} else {
				ex.printStackTrace();
			}
		} finally {
			System.out.println("----");
		}
	}
}
