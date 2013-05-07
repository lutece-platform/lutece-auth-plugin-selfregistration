/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.selfregistration.web;

import fr.paris.lutece.plugins.selfregistration.business.LdapUser;
import fr.paris.lutece.plugins.selfregistration.business.LdapUserHome;
import fr.paris.lutece.plugins.selfregistration.utils.SelfRegistrationUtils;
import fr.paris.lutece.portal.service.captcha.CaptchaSecurityService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.util.HashMap;
import java.util.Locale;

import javax.naming.NamingException;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * The SelfRegistrationApp classe
 *
 */
public class SelfRegistrationApp implements XPageApplication
{
    // Parameters
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_ACTION = "action";
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";
    private static final String PARAMETER_CIVILITY = "civility";
    private static final String PARAMETER_LAST_NAME = "last_name";
    private static final String PARAMETER_FIRST_NAME = "first_name";
    private static final String PARAMETER_EMAIL = "email";
    private static final String PARAMETER_OLD_PASSWORD = "old_password";
    private static final String PARAMETER_PASSWORD = "password";
    private static final String PARAMETER_CONFIRMATION_PASSWORD = "confirmation_password";
    private static final String PARAMETER_TELEPHONE_NUMBER = "telephone_number";
    private static final String PARAMETER_STREET_NUMBER = "street_number";
    private static final String PARAMETER_STREET_SUFFIX_NUMBER = "street_number_suffix";
    private static final String PARAMETER_STREET_TYPE = "street_type";
    private static final String PARAMETER_STREET = "street";
    private static final String PARAMETER_DISTRICT_NUMBER = "district_number";
    private static final String PARAMETER_POSTAL_CODE = "postal_code";
    private static final String PARAMETER_CITY = "city";
    private static final String PARAMETER_COUNTRY = "country";
    private static final String PARAMETER_ERROR_CODE = "error_code";
    private static final String PARAMETER_ACTION_SUCCESSFUL = "action_successful";
    private static final String PARAMETER_ACTION_BACK = "action_back";
    private static final String PARAMETER_ETAT_PROV = "etat_prov";

    // Actions
    private static final String ACTION_REGISTRATION = "registration";
    private static final String ACTION_MODIFICATION = "modification";

    // Errors
    private static final String ERROR_SYNTAX_EMAIL = "error_syntax_email";
    private static final String ERROR_MANDATORY_FIELDS = "error_mandatory_fields";
    private static final String ERROR_PASSWORD_MANDATORY_FIELDS = "error_password_mandatory_fields";
    private static final String ERROR_OLD_PASSWORD = "error_old_password";
    private static final String ERROR_SAME_PASSWORD = "error_same_password";
    private static final String ERROR_CONFIRMATION_PASSWORD = "error_confirmation_password";
    private static final String ERROR_EMAIL_ALREADY_EXISTS = "error_mail_already_exists";
    private static final String ERROR_SYNTAX_PHONE_NUMBER = "error_syntax_phone_number";
    private static final String ERROR_SYNTAX_POSTAL_CODE = "error_syntax_postal_code";
    private static final String ERROR_SYNTAX_STREET_NUMBER = "error_syntax_street_number";
    private static final String ERROR_CAPTCHA = "error_captcha";

    // Templates
    private static final String TEMPLATE_REGISTRATION_PAGE = "skin/plugins/selfregistration/create_registration.html";
    private static final String TEMPLATE_MODIFICATION_PAGE = "skin/plugins/selfregistration/modify_registration.html";

    // Properties
    private static final String PROPERTY_URL_DEFAULT_REDIRECT = "selfregistration.url.default.redirect";
    private static final String PROPERTY_REGISTRATION_LABEL = "selfregistration.xpage.createRegistration.label";
    private static final String PROPERTY_REGISTRATION_TITLE = "selfregistration.xpage.createRegistration.title";
    private static final String PROPERTY_MODIFICATION_LABEL = "selfregistration.xpage.createModification.label";
    private static final String PROPERTY_MODIFICATION_TITLE = "selfregistration.xpage.createModification.title";
    private static final String PROPERTY_CIVILITY_ITEMS_NUMBER = "selfregistration.civility.items.numbers";
    private static final String PROPERTY_LABEL_CIVYLITY = "selfregistration.xpage.registration.civility";
    private static final String PROPERTY_STREET_SUFFIX_ITEMS_NUMBER = "selfregistration.streetNumberSuffix.items.numbers";
    private static final String PROPERTY_LABEL_STREET_SUFFIX = "selfregistration.xpage.registration.streetNumberSuffix";
    private static final String PROPERTY_STREET_TYPE_ITEMS_NUMBER = "selfregistration.streetType.items.numbers";
    private static final String PROPERTY_LABEL_STREET_TYPE = "selfregistration.xpage.registration.streetType";
    private static final String PROPERTY_DISTRIC_NUMBER_ITEMS_NUMBER = "selfregistration.districtNumber.items.numbers";
    private static final String PROPERTY_LABEL_DISTRIC_NUMBER = "selfregistration.xpage.registration.districtNumber";
    private static final String PROPERTY_ERROR_MESSAGE_USER_REGISTERED_NOT_FOUND = "selfregistration.xpage.errorMessage.userRegisteredNotFound";
    private static final String PROPERTY_ERROR_MESSAGE_USER_NOT_REGISTERED = "selfregistration.xpage.errorMessage.userNotRegistered";
    private static final String PROPERTY_ACTION_BACK = "selfregistration.url.action.back";

    // Markers
    private static final String MARK_PLUGIN_NAME = "plugin_name";
    private static final String MARK_CIVILITY_REF_LIST = "civility_list";
    private static final Object MARK_STREET_SUFFIX_REF_LIST = "street_number_suffix_list";
    private static final Object MARK_STREET_TYPE_REF_LIST = "street_type_list";
    private static final Object MARK_DISTRICT_NUMBER_LIST = "district_number_list";
    private static final String MARK_ERROR_CODE = "error_code";
    private static final String MARK_ACTION_SUCCESSFUL = "action_successful";
    private static final String MARK_USER = "user";
    private static final String MARK_CAPTCHA = "captcha";
    private static final String MARK_IS_ACTIVE_CAPTCHA = "is_active_captcha";

    // Session Atributes
    private static final String SESSION_ATTRIBUTE_LDAP_USER = "ldap_user";

    // Utils
    private static final String PATERN_PHONE_NUMBER = "selfregistration.phoneNumber.pattern";
    private static final String PATERN_POSTAL_CODE = "selfregistration.portalCode.pattern";
    private static final String PATERN_STREET_NUMBER = "selfregistration.streetNumber.pattern";
    private static final String JCAPTCHA_PLUGIN = "jcaptcha";
    private static final String MESSAGE_LDAP_ERROR = "selfregistration.message.ldap.error";

    // private fields
    private Plugin _plugin;
    private Locale _locale;

    //Captcha
    private CaptchaSecurityService _captchaService = new CaptchaSecurityService(  );

    /**
     * Init locale and plugin
     * @param request The HTTP request
     * @param plugin The plugin
     */
    public void init( HttpServletRequest request, Plugin plugin )
    {
        _locale = request.getLocale(  );
        _plugin = plugin;
    }

    /**
     * Get the xpage
     * @param request The HTTP request
     * @param nmode the mode
     * @param plugin The plugin
     * @return the Xpage
     * @throws UserNotSignedException the user not signed exception
     * @throws SiteMessageException the site message exception
     */
    public XPage getPage( HttpServletRequest request, int nmode, Plugin plugin )
        throws UserNotSignedException, SiteMessageException
    {
        XPage page = new XPage(  );
        init( request, plugin );

        String strAction = request.getParameter( PARAMETER_ACTION );

        if ( ( strAction == null ) )
        {
            // clear the ldap user info in session
            request.getSession(  ).setAttribute( SESSION_ATTRIBUTE_LDAP_USER, null );
            // get the registration page
            page = getCreateRegistration( page, request );
        }
        else if ( strAction.equals( ACTION_REGISTRATION ) )
        {
            // get the registration page
            page = getCreateRegistration( page, request );
        }
        else if ( strAction.equals( ACTION_MODIFICATION ) )
        {
            SecurityService instance = SecurityService.getInstance(  );

            // get the lutece user registered
            LuteceUser registeredUser = instance.getRegisteredUser( request );

            //if a Lutece user is registered
            if ( instance.isAuthenticationEnable(  ) && ( registeredUser != null ) )
            {
                String strEmailUid = registeredUser.getName(  );
                LdapUser ldapUser = null;

                try
                {
                    ldapUser = LdapUserHome.getLdapUserByUid( request, strEmailUid );
                }
                catch ( NamingException e )
                {
                    AppLogService.error( e );
                    throw new AppException( AppPropertiesService.getProperty( MESSAGE_LDAP_ERROR ), e );
                }

                // If the Lutece user registered exist in the ldap
                if ( ldapUser != null )
                {
                    String strErrorCode = request.getParameter( PARAMETER_ERROR_CODE );

                    //No error code put the ldap user in session
                    if ( ( strErrorCode == null ) || strErrorCode.equals( "" ) )
                    {
                        // put the ldap user info in session
                        request.getSession(  ).setAttribute( SESSION_ATTRIBUTE_LDAP_USER, ldapUser );
                    }

                    // get the modification page
                    page = getModifyRegistration( page, request );
                }
                else
                {
                    SiteMessageService.setMessage( request, PROPERTY_ERROR_MESSAGE_USER_REGISTERED_NOT_FOUND,
                        SiteMessage.TYPE_ERROR );
                }
            }
            else
            {
                SiteMessageService.setMessage( request, PROPERTY_ERROR_MESSAGE_USER_NOT_REGISTERED,
                    SiteMessage.TYPE_STOP );
            }
        }

        return page;
    }

    /**
     * Get the xpage for create a registration
     * @param page the xpage
     * @param request the request
     * @return the xpage
     */
    public XPage getCreateRegistration( XPage page, HttpServletRequest request )
    {
        String strErrorCode = request.getParameter( PARAMETER_ERROR_CODE );
        String strSuccess = request.getParameter( PARAMETER_ACTION_SUCCESSFUL );

        LdapUser ldapUser = (LdapUser) request.getSession(  ).getAttribute( SESSION_ATTRIBUTE_LDAP_USER );

        if ( ldapUser == null )
        {
            ldapUser = new LdapUser(  );
        }

        HashMap model = new HashMap(  );

        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );
        model.put( MARK_CIVILITY_REF_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_CIVILITY_ITEMS_NUMBER, PROPERTY_LABEL_CIVYLITY ) );
        model.put( MARK_STREET_SUFFIX_REF_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_STREET_SUFFIX_ITEMS_NUMBER,
                PROPERTY_LABEL_STREET_SUFFIX ) );
        model.put( MARK_STREET_TYPE_REF_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_STREET_TYPE_ITEMS_NUMBER, PROPERTY_LABEL_STREET_TYPE ) );
        model.put( MARK_DISTRICT_NUMBER_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_DISTRIC_NUMBER_ITEMS_NUMBER,
                PROPERTY_LABEL_DISTRIC_NUMBER ) );
        model.put( MARK_ERROR_CODE, strErrorCode );
        model.put( MARK_ACTION_SUCCESSFUL, strSuccess );
        model.put( MARK_USER, ldapUser );

        String strBaseUrl = AppPathService.getBaseUrl( request );

        model.put( PARAMETER_ACTION_BACK, strBaseUrl + AppPropertiesService.getProperty( PROPERTY_ACTION_BACK ) );

        model.put( MARK_IS_ACTIVE_CAPTCHA, PluginService.isPluginEnable( JCAPTCHA_PLUGIN ) );
        model.put( MARK_CAPTCHA, _captchaService.getHtmlCode(  ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_REGISTRATION_PAGE, _locale, model );
        page.setContent( t.getHtml(  ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_REGISTRATION_LABEL, _locale ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_REGISTRATION_TITLE, _locale ) );

        return page;
    }

    /**
     * Get the xpage for modify registration
     * @param page the xpage
     * @param request the request
     * @return the xpage
     */
    public XPage getModifyRegistration( XPage page, HttpServletRequest request )
    {
        String strErrorCode = request.getParameter( PARAMETER_ERROR_CODE );
        String strSuccess = request.getParameter( PARAMETER_ACTION_SUCCESSFUL );

        LdapUser ldapUser = (LdapUser) request.getSession(  ).getAttribute( SESSION_ATTRIBUTE_LDAP_USER );

        if ( ldapUser == null )
        {
            ldapUser = new LdapUser(  );
        }

        HashMap model = new HashMap(  );

        model.put( MARK_PLUGIN_NAME, _plugin.getName(  ) );
        model.put( MARK_CIVILITY_REF_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_CIVILITY_ITEMS_NUMBER, PROPERTY_LABEL_CIVYLITY ) );
        model.put( MARK_STREET_SUFFIX_REF_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_STREET_SUFFIX_ITEMS_NUMBER,
                PROPERTY_LABEL_STREET_SUFFIX ) );
        model.put( MARK_STREET_TYPE_REF_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_STREET_TYPE_ITEMS_NUMBER, PROPERTY_LABEL_STREET_TYPE ) );
        model.put( MARK_DISTRICT_NUMBER_LIST,
            SelfRegistrationUtils.buildRefList( request, PROPERTY_DISTRIC_NUMBER_ITEMS_NUMBER,
                PROPERTY_LABEL_DISTRIC_NUMBER ) );
        model.put( MARK_ERROR_CODE, strErrorCode );
        model.put( MARK_ACTION_SUCCESSFUL, strSuccess );
        model.put( MARK_USER, ldapUser );

        String strBaseUrl = AppPathService.getBaseUrl( request );

        model.put( PARAMETER_ACTION_BACK, strBaseUrl + AppPropertiesService.getProperty( PROPERTY_ACTION_BACK ) );

        HtmlTemplate t = AppTemplateService.getTemplate( TEMPLATE_MODIFICATION_PAGE, _locale, model );
        page.setContent( t.getHtml(  ) );
        page.setPathLabel( I18nService.getLocalizedString( PROPERTY_MODIFICATION_LABEL, _locale ) );
        page.setTitle( I18nService.getLocalizedString( PROPERTY_MODIFICATION_TITLE, _locale ) );

        return page;
    }

    /**
     * Create an registration
     * @param request the http request
     * @return the URL for redirect
     */
    public String doCreateRegistration( HttpServletRequest request )
    {
        Plugin plugin = PluginService.getPlugin( request.getParameter( PARAMETER_PLUGIN_NAME ) );

        init( request, plugin );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) + "?" +
                PARAMETER_PAGE + "=" + _plugin.getName(  ) );
        url.addParameter( PARAMETER_ACTION, ACTION_REGISTRATION );
        url.addParameter( PARAMETER_PLUGIN_NAME, _plugin.getName(  ) );

        String strError = null;
        String strCivility = request.getParameter( PARAMETER_CIVILITY );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );
        String strPasswd = request.getParameter( PARAMETER_PASSWORD );
        String strConfirmPasswd = request.getParameter( PARAMETER_CONFIRMATION_PASSWORD );
        String strTelephoneNumber = request.getParameter( PARAMETER_TELEPHONE_NUMBER );
        String strStreetNumber = request.getParameter( PARAMETER_STREET_NUMBER );
        String strStreetNumberSuffix = request.getParameter( PARAMETER_STREET_SUFFIX_NUMBER );
        String strStreetType = request.getParameter( PARAMETER_STREET_TYPE );
        String strStreet = request.getParameter( PARAMETER_STREET );
        String strDistrictNumber = request.getParameter( PARAMETER_DISTRICT_NUMBER );
        String strPostalCode = request.getParameter( PARAMETER_POSTAL_CODE );
        String strCity = request.getParameter( PARAMETER_CITY );
        String strEtatProv = request.getParameter( PARAMETER_ETAT_PROV );
        String strCountry = request.getParameter( PARAMETER_COUNTRY );

        //Check mandatory fields
        if ( ( strCivility == null ) || ( strFirstName == null ) || ( strEmail == null ) || ( strLastName == null ) ||
                ( strPasswd == null ) || ( strConfirmPasswd == null ) || strCivility.equals( "0" ) ||
                strLastName.equals( "" ) || strFirstName.equals( "" ) || strEmail.equals( "" ) ||
                strPasswd.equals( "" ) || strConfirmPasswd.equals( "" ) )
        {
            strError = ERROR_MANDATORY_FIELDS;
        }

        //Check email format
        if ( ( strError == null ) && !StringUtil.checkEmail( strEmail ) )
        {
            strError = ERROR_SYNTAX_EMAIL;
        }

        // Check password confirmation
        if ( ( strError == null ) && !SelfRegistrationUtils.checkPassword( strPasswd, strConfirmPasswd ) )
        {
            strError = ERROR_CONFIRMATION_PASSWORD;
        }

        // Check telephone number format
        String strPatternPhoneNumber = AppPropertiesService.getProperty( PATERN_PHONE_NUMBER );

        if ( ( strError == null ) && ( strTelephoneNumber != null ) && !strTelephoneNumber.equals( "" ) &&
                !SelfRegistrationUtils.checkValueOnPattern( strPatternPhoneNumber, strTelephoneNumber ) )
        {
            strError = ERROR_SYNTAX_PHONE_NUMBER;
        }

        // Check street number format
        String strPatternStreetNumber = AppPropertiesService.getProperty( PATERN_STREET_NUMBER );

        if ( ( strError == null ) && ( strStreetNumber != null ) && !strStreetNumber.equals( "" ) &&
                !SelfRegistrationUtils.checkValueOnPattern( strPatternStreetNumber, strStreetNumber ) )
        {
            strError = ERROR_SYNTAX_STREET_NUMBER;
        }

        //Check postal code format
        String strPatternPostalCode = AppPropertiesService.getProperty( PATERN_POSTAL_CODE );

        if ( ( strError == null ) && ( strPostalCode != null ) && !strPostalCode.equals( "" ) &&
                !SelfRegistrationUtils.checkValueOnPattern( strPatternPostalCode, strPostalCode ) )
        {
            strError = ERROR_SYNTAX_POSTAL_CODE;
        }

        // Check if uid (email) exist
        try
        {
            if ( ( strError == null ) && LdapUserHome.uidExit( strEmail ) )
            {
                strError = ERROR_EMAIL_ALREADY_EXISTS;
            }
        }
        catch ( NamingException e )
        {
            AppLogService.error( e );
            throw new AppException( AppPropertiesService.getProperty( MESSAGE_LDAP_ERROR ), e );
        }

        // test the captcha
        if ( PluginService.isPluginEnable( JCAPTCHA_PLUGIN ) )
        {
            if ( !_captchaService.validate( request ) )
            {
                strError = ERROR_CAPTCHA;
            }
        }

        LdapUser ldapUser = new LdapUser(  );
        ldapUser.setTitle( strCivility );
        ldapUser.setTitleLabel( I18nService.getLocalizedString( PROPERTY_LABEL_CIVYLITY + strCivility,
                request.getLocale(  ) ) );
        ldapUser.setGivenName( strFirstName );
        ldapUser.setSn( strLastName );
        ldapUser.setMail( strEmail );
        ldapUser.setPasswd( strPasswd );
        ldapUser.setTelephoneNumber( strTelephoneNumber );
        ldapUser.setStreetNumber( strStreetNumber );
        ldapUser.setStreetNumberSuffix( strStreetNumberSuffix );
        ldapUser.setStreetNumberSuffixLabel( I18nService.getLocalizedString( PROPERTY_LABEL_STREET_SUFFIX +
                strStreetNumberSuffix, request.getLocale(  ) ) );
        ldapUser.setStreetType( strStreetType );
        ldapUser.setStreetTypeLabel( I18nService.getLocalizedString( PROPERTY_LABEL_STREET_TYPE + strStreetType,
                request.getLocale(  ) ) );
        ldapUser.setStreet( strStreet );
        ldapUser.setDistrictNumber( strDistrictNumber );
        ldapUser.setPostalCode( strPostalCode );
        ldapUser.setLn( strCity );
        ldapUser.setSt( strEtatProv );
        ldapUser.setCo( strCountry );

        if ( strError != null )
        {
            request.getSession(  ).setAttribute( SESSION_ATTRIBUTE_LDAP_USER, ldapUser );
            url.addParameter( PARAMETER_ERROR_CODE, strError );

            return url.getUrl(  );
        }
        else
        {
            //Create user in ldap
            try
            {
                LdapUserHome.registration( ldapUser );
            }
            catch ( NamingException e )
            {
                AppLogService.error( e );
                throw new AppException( AppPropertiesService.getProperty( MESSAGE_LDAP_ERROR ), e );
            }
        }

        url.addParameter( PARAMETER_ACTION_SUCCESSFUL, AppPropertiesService.getProperty( PROPERTY_URL_DEFAULT_REDIRECT ) );

        return url.getUrl(  );
    }

    /**
     * Modify an registration
     * @param request the http request
     * @return the URL for redirect
     */
    public String doModifyRegistration( HttpServletRequest request )
    {
        Plugin plugin = PluginService.getPlugin( request.getParameter( PARAMETER_PLUGIN_NAME ) );

        init( request, plugin );

        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + AppPathService.getPortalUrl(  ) + "?" +
                PARAMETER_PAGE + "=" + _plugin.getName(  ) );
        url.addParameter( PARAMETER_ACTION, ACTION_MODIFICATION );
        url.addParameter( PARAMETER_PLUGIN_NAME, _plugin.getName(  ) );

        String strError = null;
        String strCivility = request.getParameter( PARAMETER_CIVILITY );
        String strLastName = request.getParameter( PARAMETER_LAST_NAME );
        String strFirstName = request.getParameter( PARAMETER_FIRST_NAME );
        String strEmail = request.getParameter( PARAMETER_EMAIL );

        String strOldPasswd = request.getParameter( PARAMETER_OLD_PASSWORD );
        String strPasswd = request.getParameter( PARAMETER_PASSWORD );
        String strConfirmPasswd = request.getParameter( PARAMETER_CONFIRMATION_PASSWORD );

        String strTelephoneNumber = request.getParameter( PARAMETER_TELEPHONE_NUMBER );
        String strStreetNumber = request.getParameter( PARAMETER_STREET_NUMBER );
        String strStreetNumberSuffix = request.getParameter( PARAMETER_STREET_SUFFIX_NUMBER );
        String strStreetType = request.getParameter( PARAMETER_STREET_TYPE );
        String strStreet = request.getParameter( PARAMETER_STREET );
        String strDistrictNumber = request.getParameter( PARAMETER_DISTRICT_NUMBER );
        String strPostalCode = request.getParameter( PARAMETER_POSTAL_CODE );
        String strCity = request.getParameter( PARAMETER_CITY );
        String strEtatProv = request.getParameter( PARAMETER_ETAT_PROV );
        String strCountry = request.getParameter( PARAMETER_COUNTRY );

        // Check mandatory fields
        if ( ( strCivility == null ) || ( strFirstName == null ) || ( strEmail == null ) || ( strLastName == null ) ||
                strCivility.equals( "0" ) || strLastName.equals( "" ) || strFirstName.equals( "" ) ||
                strEmail.equals( "" ) )
        {
            strError = ERROR_MANDATORY_FIELDS;
        }

        //Check email format
        if ( ( strError == null ) && !StringUtil.checkEmail( strEmail ) )
        {
            strError = ERROR_SYNTAX_EMAIL;
        }

        // check for modification of password
        if ( ( ( strOldPasswd != null ) && !strOldPasswd.equals( "" ) ) ||
                ( ( strPasswd != null ) && !strPasswd.equals( "" ) ) ||
                ( ( strConfirmPasswd != null ) && !strConfirmPasswd.equals( "" ) ) )
        {
            // Check password mandatory fields
            if ( ( strOldPasswd == null ) || ( strPasswd == null ) || ( strConfirmPasswd == null ) ||
                    strOldPasswd.equals( "" ) || strPasswd.equals( "" ) || strConfirmPasswd.equals( "" ) )
            {
                strError = ERROR_PASSWORD_MANDATORY_FIELDS;
            }

            // Check  old password
            try
            {
                if ( ( strError == null ) && !( ( strOldPasswd == null ) || strOldPasswd.equals( "" ) ) &&
                        !LdapUserHome.checkOldPassword( strEmail, strOldPasswd ) )
                {
                    strError = ERROR_OLD_PASSWORD;
                }
            }
            catch ( NamingException e )
            {
                AppLogService.error( e );
                throw new AppException( AppPropertiesService.getProperty( MESSAGE_LDAP_ERROR ), e );
            }

            // Check password confirmation
            if ( ( strError == null ) && !( ( strPasswd == null ) || strPasswd.equals( "" ) ) &&
                    !SelfRegistrationUtils.checkPassword( strPasswd, strConfirmPasswd ) )
            {
                strError = ERROR_CONFIRMATION_PASSWORD;
            }

            // Check same password 
            if ( ( strError == null ) && strPasswd.equals( strOldPasswd ) )
            {
                strError = ERROR_SAME_PASSWORD;
            }
        }

        // Check telephone number format
        String strPatternPhoneNumber = AppPropertiesService.getProperty( PATERN_PHONE_NUMBER );

        if ( ( strError == null ) && ( strTelephoneNumber != null ) && !strTelephoneNumber.equals( "" ) &&
                !SelfRegistrationUtils.checkValueOnPattern( strPatternPhoneNumber, strTelephoneNumber ) )
        {
            strError = ERROR_SYNTAX_PHONE_NUMBER;
        }

        // Check street number format
        String strPatternStreetNumber = AppPropertiesService.getProperty( PATERN_STREET_NUMBER );

        if ( ( strError == null ) && ( strStreetNumber != null ) && !strStreetNumber.equals( "" ) &&
                !SelfRegistrationUtils.checkValueOnPattern( strPatternStreetNumber, strStreetNumber ) )
        {
            strError = ERROR_SYNTAX_STREET_NUMBER;
        }

        //Check postal code format
        String strPatternPostalCode = AppPropertiesService.getProperty( PATERN_POSTAL_CODE );

        if ( ( strError == null ) && ( strPostalCode != null ) && !strPostalCode.equals( "" ) &&
                !SelfRegistrationUtils.checkValueOnPattern( strPatternPostalCode, strPostalCode ) )
        {
            strError = ERROR_SYNTAX_POSTAL_CODE;
        }

        LdapUser ldapUser = new LdapUser(  );
        ldapUser.setTitle( strCivility );
        ldapUser.setTitleLabel( I18nService.getLocalizedString( PROPERTY_LABEL_CIVYLITY + strCivility,
                request.getLocale(  ) ) );
        ldapUser.setGivenName( strFirstName );
        ldapUser.setSn( strLastName );
        ldapUser.setMail( strEmail );
        ldapUser.setPasswd( strPasswd );
        ldapUser.setTelephoneNumber( strTelephoneNumber );
        ldapUser.setStreetNumber( strStreetNumber );
        ldapUser.setStreetNumberSuffix( strStreetNumberSuffix );
        ldapUser.setStreetNumberSuffixLabel( I18nService.getLocalizedString( PROPERTY_LABEL_STREET_SUFFIX +
                strStreetNumberSuffix, request.getLocale(  ) ) );
        ldapUser.setStreetType( strStreetType );
        ldapUser.setStreetTypeLabel( I18nService.getLocalizedString( PROPERTY_LABEL_STREET_TYPE + strStreetType,
                request.getLocale(  ) ) );
        ldapUser.setStreet( strStreet );
        ldapUser.setDistrictNumber( strDistrictNumber );
        ldapUser.setPostalCode( strPostalCode );
        ldapUser.setLn( strCity );
        ldapUser.setSt( strEtatProv );
        ldapUser.setCo( strCountry );

        if ( strError != null )
        {
            request.getSession(  ).setAttribute( SESSION_ATTRIBUTE_LDAP_USER, ldapUser );
            url.addParameter( PARAMETER_ERROR_CODE, strError );

            return url.getUrl(  );
        }
        else
        {
            //Modify user in ldap
            try
            {
                LdapUserHome.modification( ldapUser );
            }
            catch ( NamingException e )
            {
                AppLogService.error( e );
                throw new AppException( AppPropertiesService.getProperty( MESSAGE_LDAP_ERROR ), e );
            }
        }

        url.addParameter( PARAMETER_ACTION_SUCCESSFUL, AppPropertiesService.getProperty( PROPERTY_URL_DEFAULT_REDIRECT ) );

        return url.getUrl(  );
    }
}
