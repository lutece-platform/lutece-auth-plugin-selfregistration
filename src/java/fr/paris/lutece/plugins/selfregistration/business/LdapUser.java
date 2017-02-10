/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import java.io.Serializable;


/**
 *
 * The class LdapUser Object
 *
 */
public class LdapUser implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -4084027325816863976L;
    private String _strTitle;
    private String _strTitleLabel;
    private String _strSn;
    private String _strGivenName;
    private String _strMail;
    private String _strPasswd;
    private String _strConfirmPasswd;
    private String _strTelephoneNumber;
    private String _strStreetNumber;
    private String _strStreetNumberSuffix;
    private String _strStreetNumberSuffixLabel;
    private String _strStreetType;
    private String _strStreetTypeLabel;
    private String _strStreet;
    private String _strDistrictNumber;
    private String _strPostalCode;
    private String _strLn;
    private String _strSt;
    private String _strCo;

    /**
     *
     * Constructor
     *
     */
    public LdapUser(  )
    {
    }

    /**
     * @return the givenName
     */
    public String getGivenName(  )
    {
        return _strGivenName;
    }

    /**
     * @param strGivenName the givenName to set
     */
    public void setGivenName( String strGivenName )
    {
        _strGivenName = strGivenName;
    }

    /**
     * @return the mail
     */
    public String getMail(  )
    {
        return _strMail;
    }

    /**
     * @param strMail the mail to set
     */
    public void setMail( String strMail )
    {
        _strMail = strMail;
    }

    /**
     * @return the sn
     */
    public String getSn(  )
    {
        return _strSn;
    }

    /**
     * @param strSn the sn to set
     */
    public void setSn( String strSn )
    {
        _strSn = strSn;
    }

    /**
     * @return the title
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * @param strTitle the title to set
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * @return the _strCo
     */
    public String getCo(  )
    {
        return _strCo;
    }

    /**
     * @param strCo the _strCo to set
     */
    public void setCo( String strCo )
    {
        _strCo = strCo;
    }

    /**
     * @return the _strDistrictNumber
     */
    public String getDistrictNumber(  )
    {
        return _strDistrictNumber;
    }

    /**
     * @param strDistrictNumber the _strDistrictNumber to set
     */
    public void setDistrictNumber( String strDistrictNumber )
    {
        _strDistrictNumber = strDistrictNumber;
    }

    /**
     * @return the _strL
     */
    public String getLn(  )
    {
        return _strLn;
    }

    /**
     * @param strLn the _strLn to set
     */
    public void setLn( String strLn )
    {
        _strLn = strLn;
    }

    /**
     * @return the _strPostalCode
     */
    public String getPostalCode(  )
    {
        return _strPostalCode;
    }

    /**
     * @param strPostalCode the _strPostalCode to set
     */
    public void setPostalCode( String strPostalCode )
    {
        _strPostalCode = strPostalCode;
    }

    /**
     * @return the _strSt
     */
    public String getSt(  )
    {
        return _strSt;
    }

    /**
     * @param strSt the _strSt to set
     */
    public void setSt( String strSt )
    {
        _strSt = strSt;
    }

    /**
     * @return the _strStreet
     */
    public String getStreet(  )
    {
        return _strStreet;
    }

    /**
     * @param strStreet the _strStreet to set
     */
    public void setStreet( String strStreet )
    {
        _strStreet = strStreet;
    }

    /**
     * @return the _strStreetNumber
     */
    public String getStreetNumber(  )
    {
        return _strStreetNumber;
    }

    /**
     * @param strStreetNumber the _strStreetNumber to set
     */
    public void setStreetNumber( String strStreetNumber )
    {
        _strStreetNumber = strStreetNumber;
    }

    /**
     * @return the _strStreetNumberSuffix
     */
    public String getStreetNumberSuffix(  )
    {
        return _strStreetNumberSuffix;
    }

    /**
     * @param streetNumberSuffix the _strStreetNumberSuffix to set
     */
    public void setStreetNumberSuffix( String streetNumberSuffix )
    {
        _strStreetNumberSuffix = streetNumberSuffix;
    }

    /**
     * @return the _strStreetType
     */
    public String getStreetType(  )
    {
        return _strStreetType;
    }

    /**
     * @param streetType the _strStreetType to set
     */
    public void setStreetType( String streetType )
    {
        _strStreetType = streetType;
    }

    /**
     * @return the _strTelephoneNumber
     */
    public String getTelephoneNumber(  )
    {
        return _strTelephoneNumber;
    }

    /**
     * @param telephoneNumber the _strTelephoneNumber to set
     */
    public void setTelephoneNumber( String telephoneNumber )
    {
        _strTelephoneNumber = telephoneNumber;
    }

    /**
     * @return the _strpasswd
     */
    public String getPasswd(  )
    {
        return _strPasswd;
    }

    /**
     * @param strPasswd the _strpasswd to set
     */
    public void setPasswd( String strPasswd )
    {
        _strPasswd = strPasswd;
    }

    /**
     * @return the _strConfirmPasswd
     */
    public String getConfirmPasswd(  )
    {
        return _strConfirmPasswd;
    }

    /**
     * @param confirmPasswd the _strConfirmPasswd to set
     */
    public void setConfirmPasswd( String confirmPasswd )
    {
        _strConfirmPasswd = confirmPasswd;
    }

    /**
     * @return the _strTitleLabel
     */
    public String getTitleLabel(  )
    {
        return _strTitleLabel;
    }

    /**
     * @param titleLabel the _strTitleLabel to set
     */
    public void setTitleLabel( String titleLabel )
    {
        _strTitleLabel = titleLabel;
    }

    /**
     * @return the _strStreetNumberSuffixLabel
     */
    public String getStreetNumberSuffixLabel(  )
    {
        return _strStreetNumberSuffixLabel;
    }

    /**
     * @param streetNumberSuffixLabel the _strStreetNumberSuffixLabel to set
     */
    public void setStreetNumberSuffixLabel( String streetNumberSuffixLabel )
    {
        _strStreetNumberSuffixLabel = streetNumberSuffixLabel;
    }

    /**
     * @return the _strStreetTypeLabel
     */
    public String getStreetTypeLabel(  )
    {
        return _strStreetTypeLabel;
    }

    /**
     * @param streetTypeLabel the _strStreetTypeLabel to set
     */
    public void setStreetTypeLabel( String streetTypeLabel )
    {
        _strStreetTypeLabel = streetTypeLabel;
    }
}
