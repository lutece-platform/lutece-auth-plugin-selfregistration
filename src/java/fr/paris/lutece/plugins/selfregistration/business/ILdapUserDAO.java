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

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * ILdapUserDAO Interface
 *
 */
public interface ILdapUserDAO
{
    /**
     * Open connexion to the ldap
     * @return the ldap context
     * @throws NamingException the namming exception
     */
    DirContext openConnexion(  ) throws NamingException;

    /**
     * Close connexion to the ldap
     * @param ldapContext the ldap context
     * @throws NamingException the namming exception
     */
    void closeConnexion( DirContext ldapContext ) throws NamingException;

    /**
     * Add an user into the ldap
     * @param ldapUser the user to add in ldap
     * @throws NamingException the namming exception
     */
    void registration( LdapUser ldapUser ) throws NamingException;

    /**
     * Test if user exist
     * @param strUid the uid of user
     * @return true if user exist
     * @throws NamingException the namming exception
     */
    boolean uidExit( String strUid ) throws NamingException;

    /**
     * check if the Old Password equals the password associete to the uid
     * @param strUid the uid of user
     * @param strOldPassword the password to check
     * @return true if the Old Password equals the password associete to the uid
     * @throws NamingException the namming exception
     */
    boolean checkOldPassword( String strUid, String strOldPassword )
        throws NamingException;

    /**
     * Return the ldapuser if exist
     * @param request the http request
     * @param strUid the uid of user
     * @return the ldap user if exist null other
     * @throws NamingException the namming exception
     */
    LdapUser getLdapUserByUid( HttpServletRequest request, String strUid )
        throws NamingException;

    /**
     * Modify the attributes user into the ldap
     * @param ldapUser the user to add in ldap
     * @throws NamingException the namming exception
     */
    void modification( LdapUser ldapUser ) throws NamingException;
}
