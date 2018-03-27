package org.afscme.enterprise.test;

import java.io.IOException;

/**
 * Contains re-usable login scripts that can be called by derived classes
 */
public abstract class AFSCMEWebTest extends WebTestBase {

    public AFSCMEWebTest(String name) throws IOException {
        super(name);
    }

	
	/**
	 * Performs the HTTP steps necessary to login to the application as the ft_afscme user
	 */
	protected void loginAFSCME() throws Exception {
		
        //get the Welcome page
		reset();
        get("");

        //navigate to the user login page
        selectLink("Login");

        //fill out the login form ant submit
        setParameter("userId", "ft_afscme");
        setParameter("password", "password");
        submit();

		//see if this is the first login
		if (getResponse().getTitle().equals("Account Info")) {
			//fist login, must change password
			setParameter("newPassword", "password");
			setParameter("newPassword2", "password");
			setParameter("challengeQuestion", "1001");
			setParameter("challengeResponse", "fido");
			submit();
		}

		//should be at the main menu
		assertEquals("Enterprise Application Main Menu", getResponse().getTitle());
	}
}
