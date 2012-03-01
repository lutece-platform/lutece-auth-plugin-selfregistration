/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.selfregistration.business;

import fr.paris.lutece.plugins.selfregistration.utils.SelfRegistrationUtils;

import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * LdapUserDAO Class
 *
 */
public class LdapUserDAO implements ILdapUserDAO
{
    // Properties
    private static final String PROPERTY_CIVILITY_ITEMS_NUMBER = "selfregistration.civility.items.numbers";
    private static final String PROPERTY_LABEL_CIVYLITY = "selfregistration.xpage.registration.civility";
    private static final String PROPERTY_STREET_SUFFIX_ITEMS_NUMBER = "selfregistration.streetNumberSuffix.items.numbers";
    private static final String PROPERTY_LABEL_STREET_SUFFIX = "selfregistration.xpage.registration.streetNumberSuffix";
    private static final String PROPERTY_STREET_TYPE_ITEMS_NUMBER = "selfregistration.streetType.items.numbers";
    private static final String PROPERTY_LABEL_STREET_TYPE = "selfregistration.xpage.registration.streetType";

    // Ldap properties
    private static final String LDAP_CNX_POOL = "com.sun.jndi.ldap.connect.pool";
    private static final String LDAP_CNX_POOL_MAXSIZE = "com.sun.jndi.ldap.connect.pool.maxsize";
    private static final String LDAP_CNX_POOL_PREFSIZE = "com.sun.jndi.ldap.connect.pool.prefsize";
    private static final String LDAP_CNX_POOL_TIMEOUT = "com.sun.jndi.ldap.connect.pool.timeout";
    private static final String LDAP_ATTRIBUTE_UID = "ldap.attribute.uid";
    private static final String LDAP_ATTRIBUTE_CN = "ldap.attribute.cn";
    private static final String LDAP_ATTRIBUTE_OBJECT_CLASS = "ldap.attribute.objectClass";
    private static final String LDAP_ATTRIBUTE_OBJECT_CLASS_PERSON = "ldap.attribute.objectClass.person";
    private static final String LDAP_ATTRIBUTE_OBJECT_CLASS_TOP = "ldap.attribute.objectClass.top";
    private static final String LDAP_ATTRIBUTE_OBJECT_CLASS_ORG_PERSON = "ldap.attribute.objectClass.orgPerson";
    private static final String LDAP_ATTRIBUTE_OBJECT_CLASS_INET_ORG_PERSON = "ldap.attribute.objectClass.inetOrgPerson";
    private static final String LDAP_ATTRIBUTE_OBJECT_CLASS_PARIS_PERSON = "ldap.attribute.objectClass.parisPerson";
    private static final String LDAP_ATTRIBUTE_CIVILITY = "ldap.attribute.civility";
    private static final String LDAP_ATTRIBUTE_LAST_NAME = "ldap.attribute.lastName";
    private static final String LDAP_ATTRIBUTE_GIVEN_NAME = "ldap.attribute.givenName";
    private static final String LDAP_ATTRIBUTE_EMAIL = "ldap.attribute.mail";
    private static final String LDAP_ATTRIBUTE_PASSWD = "ldap.attribute.userPassword";
    private static final String LDAP_ATTRIBUTE_PHONE_NUMBER = "ldap.attribute.phoneNumber";
    private static final String LDAP_ATTRIBUTE_STREET_NUMBER = "ldap.attribute.streetNumber";
    private static final String LDAP_ATTRIBUTE_STREET_NUMBER_SUFFIX = "ldap.attribute.streetNumberSuffix";
    private static final String LDAP_ATTRIBUTE_STREET_TYPE = "ldap.attribute.streetType";
    private static final String LDAP_ATTRIBUTE_STREET = "ldap.attribute.streetName";
    private static final String LDAP_ATTRIBUTE_DISTRICT_NUMBER = "ldap.attribute.districtNumber";
    private static final String LDAP_ATTRIBUTE_POSTAL_CODE = "ldap.attribute.postalCode";
    private static final String LDAP_ATTRIBUTE_STATE_PROV = "ldap.attribute.stateProv";
    private static final String LDAP_ATTRIBUTE_CITY = "ldap.attribute.city";
    private static final String LDAP_ATTRIBUTE_COUNTRY = "ldap.attribute.country";
    private String _strContextFactory;
    private String _strProviderUrl;
    private String _strOu;
    private String _strSecurityAuthentication;
    private String _strLogin;
    private String _strPassword;
    private String _strPoolMaxSize;
    private String _strPoolPrefSize;
    private String _strPoolTimeout;
    private Properties _ldapAttributes;

    /**
     * Open connexion to the ldap
     * @return the ldap context
     * @throws NamingException the namming exception
     */
    public DirContext openConnexion(  ) throws NamingException
    {
        Hashtable ldapEnv = new Hashtable(  );
        ldapEnv.put( Context.INITIAL_CONTEXT_FACTORY, getContextFactory(  ) );
        ldapEnv.put( Context.PROVIDER_URL, getProviderUrl(  ) );
        ldapEnv.put( Context.SECURITY_AUTHENTICATION, getSecurityAuthentication(  ) );
        ldapEnv.put( Context.SECURITY_PRINCIPAL, getLogin(  ) );
        ldapEnv.put( Context.SECURITY_CREDENTIALS, getPassword(  ) );
        ldapEnv.put( LDAP_CNX_POOL, Boolean.TRUE.toString(  ) );
        ldapEnv.put( LDAP_CNX_POOL_MAXSIZE, getPoolMaxSize(  ) );
        ldapEnv.put( LDAP_CNX_POOL_PREFSIZE, getPoolPrefSize(  ) );
        ldapEnv.put( LDAP_CNX_POOL_TIMEOUT, getPoolTimeout(  ) );

        DirContext ldapContext = new InitialDirContext( ldapEnv );

        return ldapContext;
    }

    /**
     * Close connexion to the ldap
     * @param ldapContext the ldap context
     * @throws NamingException the namming exception
     */
    public void closeConnexion( DirContext ldapContext )
        throws NamingException
    {
        ldapContext.close(  );
    }

    /**
     * Add an user into the ldap
     * @param ldapUser the user to add in ldap
     * @throws NamingException the namming exception
     */
    public void registration( LdapUser ldapUser ) throws NamingException
    {
        Attributes attributes = getAttributes( ldapUser, false );

        DirContext ldapContext = openConnexion(  );
        ldapContext.createSubcontext( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_UID ) + "=" +
            ldapUser.getMail(  ) + "," + _strOu, attributes );
        closeConnexion( ldapContext );
    }

    /**
     * Test if user exist
     * @param strUid the uid of user
     * @return true if user exist
     * @throws NamingException the namming exception
     */
    public boolean uidExit( String strUid ) throws NamingException
    {
        NamingEnumeration objs = null;

        BasicAttributes searchAttrs = new BasicAttributes(  );
        searchAttrs.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_UID ), strUid );

        DirContext ldapContext = openConnexion(  );
        objs = ldapContext.search( _strOu, searchAttrs );
        closeConnexion( ldapContext );

        return objs.hasMoreElements(  );
    }

    /**
     * check if the Old Password equals the password associete to the uid
     * @param strUid the uid of user
     * @param strOldPassword the password to check
     * @return true if the Old Password equals the password associete to the uid
     * @throws NamingException the namming exception
     */
    public boolean checkOldPassword( String strUid, String strOldPassword )
        throws NamingException
    {
        NamingEnumeration objs = null;

        BasicAttributes searchAttrs = new BasicAttributes(  );
        searchAttrs.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_UID ), strUid );
        searchAttrs.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_PASSWD ), strOldPassword );

        DirContext ldapContext = openConnexion(  );
        objs = ldapContext.search( _strOu, searchAttrs );
        closeConnexion( ldapContext );

        return objs.hasMoreElements(  );
    }

    /**
     * Return the ldapuser if exist
     * @param strUid the uid of user
     * @param request the http request
     * @return the ldap user if exist null other
     * @throws NamingException the namming exception
     */
    public LdapUser getLdapUserByUid( HttpServletRequest request, String strUid )
        throws NamingException
    {
        NamingEnumeration objs = null;

        BasicAttributes searchAttrs = new BasicAttributes(  );
        searchAttrs.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_UID ), strUid );

        DirContext ldapContext = openConnexion(  );
        objs = ldapContext.search( _strOu, searchAttrs );
        closeConnexion( ldapContext );

        if ( objs.hasMoreElements(  ) )
        {
            SearchResult match = (SearchResult) objs.nextElement(  );

            return createLdapUser( request, match.getAttributes(  ) );
        }
        else
        {
            return null;
        }
    }

    /**
     * Modify the attribute of the ldapUser in the ldap
     * @param ldapUser the ldapUser
     * @throws NamingException the namming exception
     */
    public void modification( LdapUser ldapUser ) throws NamingException
    {
        Attributes attributes = getAttributes( ldapUser, true );

        DirContext ldapContext = openConnexion(  );
        ldapContext.modifyAttributes( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_UID ) + "=" +
            ldapUser.getMail(  ) + "," + _strOu, DirContext.REPLACE_ATTRIBUTE, attributes );
    }

    /**
     * Create and build a LdapUIser with attributes
     * @param request the http request
     * @param attrs the ldap Attributes
     * @return return an instance of LdapUser
     * @throws NamingException the namming exception
     */
    private LdapUser createLdapUser( HttpServletRequest request, Attributes attrs )
        throws NamingException
    {
        LdapUser ldapUser = new LdapUser(  );
        ldapUser.setTitleLabel( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_CIVILITY ) ).get(  ) );

        if ( ldapUser.getTitleLabel(  ) != null )
        {
            String strTitleCode = SelfRegistrationUtils.getCodeOfLabel( request, PROPERTY_CIVILITY_ITEMS_NUMBER,
                    PROPERTY_LABEL_CIVYLITY, ldapUser.getTitleLabel(  ) );
            ldapUser.setTitle( strTitleCode );
        }

        ldapUser.setSn( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_LAST_NAME ) ).get(  ) );
        ldapUser.setGivenName( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_GIVEN_NAME ) ).get(  ) );
        ldapUser.setMail( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_EMAIL ) ).get(  ) );

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_PHONE_NUMBER ) ) != null )
        {
            ldapUser.setTelephoneNumber( (String) attrs.get( getLdapAttributes(  )
                                                                 .getProperty( LDAP_ATTRIBUTE_PHONE_NUMBER ) ).get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STREET_NUMBER ) ) != null )
        {
            ldapUser.setStreetNumber( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STREET_NUMBER ) )
                                                    .get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STREET_NUMBER_SUFFIX ) ) != null )
        {
            ldapUser.setStreetNumberSuffixLabel( (String) attrs.get( getLdapAttributes(  )
                                                                         .getProperty( LDAP_ATTRIBUTE_STREET_NUMBER_SUFFIX ) )
                                                               .get(  ) );
        }

        if ( ldapUser.getStreetNumberSuffixLabel(  ) != null )
        {
            String strStreetNumberSuffixCode = SelfRegistrationUtils.getCodeOfLabel( request,
                    PROPERTY_STREET_SUFFIX_ITEMS_NUMBER, PROPERTY_LABEL_STREET_SUFFIX,
                    ldapUser.getStreetNumberSuffixLabel(  ) );
            ldapUser.setStreetNumberSuffix( strStreetNumberSuffixCode );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STREET_TYPE ) ) != null )
        {
            ldapUser.setStreetTypeLabel( (String) attrs.get( getLdapAttributes(  )
                                                                 .getProperty( LDAP_ATTRIBUTE_STREET_TYPE ) ).get(  ) );
        }

        if ( ldapUser.getStreetTypeLabel(  ) != null )
        {
            String strStreetTypeCode = SelfRegistrationUtils.getCodeOfLabel( request,
                    PROPERTY_STREET_TYPE_ITEMS_NUMBER, PROPERTY_LABEL_STREET_TYPE, ldapUser.getStreetTypeLabel(  ) );
            ldapUser.setStreetType( strStreetTypeCode );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STREET ) ) != null )
        {
            ldapUser.setStreet( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STREET ) ).get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_DISTRICT_NUMBER ) ) != null )
        {
            ldapUser.setDistrictNumber( (String) attrs.get( getLdapAttributes(  )
                                                                .getProperty( LDAP_ATTRIBUTE_DISTRICT_NUMBER ) ).get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_POSTAL_CODE ) ) != null )
        {
            ldapUser.setPostalCode( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_POSTAL_CODE ) )
                                                  .get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_CITY ) ) != null )
        {
            ldapUser.setLn( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_CITY ) ).get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STATE_PROV ) ) != null )
        {
            ldapUser.setSt( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_STATE_PROV ) ).get(  ) );
        }

        if ( attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_COUNTRY ) ) != null )
        {
            ldapUser.setCo( (String) attrs.get( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_COUNTRY ) ).get(  ) );
        }

        return ldapUser;
    }

    /**
     * return the ldap user on the Attributes
     * @param ldapUser the ldap user
     * @param isModification creation or modification
     * @return the attributes of the ldap user
     */
    private Attributes getAttributes( LdapUser ldapUser, boolean isModification )
    {
        Attributes attributes = new BasicAttributes(  );

        attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_CN ),
            ldapUser.getSn(  ) + " " + ldapUser.getGivenName(  ) );

        Attribute classes = new BasicAttribute( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_OBJECT_CLASS ) );
        classes.add( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_OBJECT_CLASS_PERSON ) );
        classes.add( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_OBJECT_CLASS_TOP ) );
        classes.add( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_OBJECT_CLASS_ORG_PERSON ) );
        classes.add( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_OBJECT_CLASS_INET_ORG_PERSON ) );
        classes.add( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_OBJECT_CLASS_PARIS_PERSON ) );

        attributes.put( classes );
        attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_CIVILITY ), ldapUser.getTitleLabel(  ) );
        attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_LAST_NAME ), ldapUser.getSn(  ) );
        attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_GIVEN_NAME ), ldapUser.getGivenName(  ) );
        attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_EMAIL ), ldapUser.getMail(  ) );

        if ( ( ldapUser.getPasswd(  ) != null ) && !ldapUser.getPasswd(  ).equals( "" ) )
        {
            attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_PASSWD ), ldapUser.getPasswd(  ) );
        }

        putAttribute( attributes, LDAP_ATTRIBUTE_PHONE_NUMBER, ldapUser.getTelephoneNumber(  ), isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_STREET_NUMBER, ldapUser.getStreetNumber(  ), isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_STREET_NUMBER_SUFFIX, ldapUser.getStreetNumberSuffixLabel(  ),
            isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_STREET_TYPE, ldapUser.getStreetTypeLabel(  ), isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_STREET, ldapUser.getStreet(  ), isModification );

        if ( ( ldapUser.getDistrictNumber(  ) != null ) && !ldapUser.getDistrictNumber(  ).equals( "0" ) )
        {
            attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_DISTRICT_NUMBER ),
                ldapUser.getDistrictNumber(  ) );
        }
        else if ( isModification )
        {
            attributes.put( getLdapAttributes(  ).getProperty( LDAP_ATTRIBUTE_DISTRICT_NUMBER ), null );
        }

        putAttribute( attributes, LDAP_ATTRIBUTE_POSTAL_CODE, ldapUser.getPostalCode(  ), isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_CITY, ldapUser.getLn(  ), isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_STATE_PROV, ldapUser.getSt(  ), isModification );
        putAttribute( attributes, LDAP_ATTRIBUTE_COUNTRY, ldapUser.getCo(  ), isModification );

        return attributes;
    }

    /**
     *
     * @param attributes the list attribute for put
     * @param attribute the attribute to put
     * @param strValue the value to put
     * @param isModification creation or modification
     */
    private void putAttribute( Attributes attributes, String attribute, String strValue, boolean isModification )
    {
        if ( ( strValue != null ) && !strValue.equals( "" ) )
        {
            attributes.put( getLdapAttributes(  ).getProperty( attribute ), strValue );
        }
        else if ( isModification )
        {
            attributes.put( getLdapAttributes(  ).getProperty( attribute ), null );
        }
    }

    /**
     * @return the _strLogin
     */
    public String getLogin(  )
    {
        return _strLogin;
    }

    /**
     * @param strLogin the _strLogin to set
     */
    public void setLogin( String strLogin )
    {
        _strLogin = strLogin;
    }

    /**
     * @return the _strPassword
     */
    public String getPassword(  )
    {
        return _strPassword;
    }

    /**
     * @param strPassword the _strPassword to set
     */
    public void setPassword( String strPassword )
    {
        _strPassword = strPassword;
    }

    /**
     * @return the _strContextFactory
     */
    public String getContextFactory(  )
    {
        return _strContextFactory;
    }

    /**
     * @param strContextFactory the _strContextFactory to set
     */
    public void setContextFactory( String strContextFactory )
    {
        _strContextFactory = strContextFactory;
    }

    /**
     * @return the _strPoolMaxSize
     */
    public String getPoolMaxSize(  )
    {
        return _strPoolMaxSize;
    }

    /**
     * @param strPoolMaxSize the _strPoolMaxSize to set
     */
    public void setPoolMaxSize( String strPoolMaxSize )
    {
        _strPoolMaxSize = strPoolMaxSize;
    }

    /**
     * @return the _strPoolPrefSize
     */
    public String getPoolPrefSize(  )
    {
        return _strPoolPrefSize;
    }

    /**
     * @param strPoolPrefSize the _strPoolPrefSize to set
     */
    public void setPoolPrefSize( String strPoolPrefSize )
    {
        _strPoolPrefSize = strPoolPrefSize;
    }

    /**
     * @return the _strPoolTimeout
     */
    public String getPoolTimeout(  )
    {
        return _strPoolTimeout;
    }

    /**
     * @param strPoolTimeout the _strPoolTimeout to set
     */
    public void setPoolTimeout( String strPoolTimeout )
    {
        _strPoolTimeout = strPoolTimeout;
    }

    /**
     * @return the _strProviderUrl
     */
    public String getProviderUrl(  )
    {
        return _strProviderUrl;
    }

    /**
     * @param strProviderUrl the _strProviderUrl to set
     */
    public void setProviderUrl( String strProviderUrl )
    {
        _strProviderUrl = strProviderUrl;
    }

    /**
     * @return the _strSecurityAuthentication
     */
    public String getSecurityAuthentication(  )
    {
        return _strSecurityAuthentication;
    }

    /**
     * @param strSecurityAuthentication the _strSecurityAuthentication to set
     */
    public void setSecurityAuthentication( String strSecurityAuthentication )
    {
        _strSecurityAuthentication = strSecurityAuthentication;
    }

    /**
     * @return the _strOu
     */
    public String getOu(  )
    {
        return _strOu;
    }

    /**
     * @param strOu the _strOu to set
     */
    public void setOu( String strOu )
    {
        _strOu = strOu;
    }

    /**
     * @return the ldapAttributes
     */
    public Properties getLdapAttributes(  )
    {
        return _ldapAttributes;
    }

    /**
     * @param ldapAttributes the ldapAttributes to set
     */
    public void setLdapAttributes( Properties ldapAttributes )
    {
        _ldapAttributes = ldapAttributes;
    }
}
