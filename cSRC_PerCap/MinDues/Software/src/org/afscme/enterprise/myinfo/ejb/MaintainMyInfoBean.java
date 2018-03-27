package org.afscme.enterprise.myinfo.ejb;

import java.lang.Integer;
import java.util.*;
import javax.ejb.*;
import java.util.Iterator;
import javax.naming.NamingException;
// AFSCME Enterprise Imports
import org.afscme.enterprise.myinfo.MyInfoData;
import org.afscme.enterprise.address.PersonAddress;
import org.afscme.enterprise.common.PhoneData;
import org.afscme.enterprise.users.ejb.MaintainUsers;
import org.afscme.enterprise.address.ejb.SystemAddress;
import org.afscme.enterprise.person.ejb.MaintainPersons;
import org.afscme.enterprise.member.ejb.MaintainMembers;
import org.afscme.enterprise.common.ejb.SessionBase;
import org.afscme.enterprise.util.JNDIUtil;


/**
 * Encapsulates all access to My Personal Information in the system
 * @ejb:bean name="MaintainMyInfo" display-name="MaintainMyInfo"
 * jndi-name="MaintainMyInfo"
 * type="Stateless" view-type="local"
 */
public class MaintainMyInfoBean extends SessionBase
{
    /**
     * variables to hold reference to ejb
     */
    protected SystemAddress m_addressBean;
    protected MaintainPersons m_maintainPersons;
    protected MaintainMembers m_maintainMembers;
    protected MaintainUsers m_maintainUsers;

    public void ejbCreate() throws CreateException {
         try {
            m_addressBean = JNDIUtil.getSystemAddressHome().create();
            m_maintainMembers = JNDIUtil.getMaintainMembersHome().create();
            m_maintainPersons = JNDIUtil.getMaintainPersonsHome().create();
            m_maintainUsers = JNDIUtil.getMaintainUsersHome().create();
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

    /**
     * @J2EE_METHOD  --  getMyInfoData
     * Retrieves the data for a specific person.
     *
     * @param personPK Person Primary Key
     * @return The user's personal information.
     * @ejb:interface-method view-type="local"
     * @ejb:transaction type="Supports"
     */
    public MyInfoData getMyInfoData(Integer personPk)
    {
		MyInfoData mid = new MyInfoData();
		mid.setPersonData(m_maintainPersons.getPersonDetail(personPk, m_maintainUsers.getUser(personPk).getDepartment()));
		mid.setPersonAddressRecord(m_addressBean.getSystemAddress(personPk));
		
		/*
		 * Code added to support change to getMemberAffiliatesSummary message signature
		 * which now requires a Collection (null or populated) to be passed representing
		 * the user's affiliate hierarchy, if the user is under the Affiliate
		 * view data utility(vdu) - in this case, the business method is not being called
		 * from under the vdu, so a null collection is always passed
		 *	 	 
		 */
		Collection vduAffiliates = null;
		mid.setMemberAffiliateResults(m_maintainMembers.getMemberAffiliatesSummary(personPk, vduAffiliates));
		return mid;
	}

	/**
	 * @J2EE_METHOD  --  updateMyInfoData
	 * Updates the data for a specific person.
	 * @param myInfoData The user's personal information.
	 * @return the Set object.
	 * @ejb:interface-method view-type="local"
	 * @ejb:transaction type="Required"
     */
	public Set updateMyInfoData(MyInfoData myInfoData)
	{
		myInfoData.getPersonAddressRecord();
		Set errors = m_addressBean.updateByOwner(myInfoData.getPersonData().getPersonPk(),
									  			 myInfoData.getPersonAddressRecord().getRecordData().getPk(), myInfoData.getPersonAddressRecord());
		if(errors != null) {
			return errors;
		}
		Collection phoneData = myInfoData.getPersonData().getThePhoneData();
		if(phoneData != null) {
			Iterator i = phoneData.iterator();
			while(i.hasNext()) {
				PhoneData pd = (PhoneData)i.next();
				m_maintainPersons.updatePersonPhone(myInfoData.getPersonData().getPersonPk(), myInfoData.getPersonData().getPersonPk(),
								  pd);
			}
		}
		m_maintainPersons.updatePersonEmail(myInfoData.getPersonData().getPersonPk(), myInfoData.getPersonData().getPersonPk(),
						  				    myInfoData.getPersonData().getTheEmailData());
		return errors;
	}
}
