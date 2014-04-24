/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.selfregistration.utils;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * The SelfRegistrationUtils classe
 *
 */
public final class SelfRegistrationUtils
{
    //Utils
    private static final String EMPTY_STRING = "";

    /**
    *
    * Constructor
    *
    */
    private SelfRegistrationUtils(  )
    {
    }

    /**
    * Check the password with the password confirmation string
    * Check if password is empty
    *
    * @param strPassword The password
    * @param strConfirmation The password confirmation
    * @return true if password is equal to confirmation password and not empty
    */
    public static boolean checkPassword( String strPassword, String strConfirmation )
    {
        Boolean bReturn = true;

        if ( ( strPassword == null ) || ( strConfirmation == null ) || strPassword.equals( "" ) ||
                !strPassword.equals( strConfirmation ) )
        {
            bReturn = false;
        }

        return bReturn;
    }

    /**
     * Check a given string against a given pattern
     * @param strPattern the pattern to match
     * @param strValue the value to check
     * @return true if pattern matches, false otherwise.
     */
    public static boolean checkValueOnPattern( String strPattern, String strValue )
    {
        Pattern pattern = Pattern.compile( strPattern );
        Matcher matcher = pattern.matcher( strValue );

        return matcher.matches(  );
    }

    /**
     * Build a reference list
     * @param request the request
     * @param strPropertiesItemsNumber the properties item number
     * @param strPropertiesLabel the properties label
     * @return a reference list
     */
    public static ReferenceList buildRefList( HttpServletRequest request, String strPropertiesItemsNumber,
        String strPropertiesLabel )
    {
        ReferenceList refList = new ReferenceList(  );
        int nNubersItems = Integer.parseInt( AppPropertiesService.getProperty( strPropertiesItemsNumber ) );

        for ( int i = 0; i < nNubersItems; i++ )
        {
            ReferenceItem item = new ReferenceItem(  );
            String strCode = String.valueOf( i );
            item.setCode( strCode );
            item.setName( I18nService.getLocalizedString( strPropertiesLabel + strCode, request.getLocale(  ) ) );
            refList.add( item );
        }

        return refList;
    }

    /**
     * Get the code of strLabel
     * @param request the request
     * @param strPropertiesItemsNumber the properties item number
     * @param strPropertiesLabel the properties label
     * @param strLabel the label to compare
     * @return the code corresponding to the strLabel
     */
    public static String getCodeOfLabel( HttpServletRequest request, String strPropertiesItemsNumber,
        String strPropertiesLabel, String strLabel )
    {
        int nNubersItems = Integer.parseInt( AppPropertiesService.getProperty( strPropertiesItemsNumber ) );

        for ( int i = 0; i < nNubersItems; i++ )
        {
            String strCode = String.valueOf( i );
            String strCurrentLabel = I18nService.getLocalizedString( strPropertiesLabel + strCode, request.getLocale(  ) );

            if ( strLabel.equals( strCurrentLabel ) )
            {
                return strCode;
            }
        }

        return EMPTY_STRING;
    }
}
