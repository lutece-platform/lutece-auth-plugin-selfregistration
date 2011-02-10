<jsp:include page="../../PortalHeader.jsp" />
<jsp:useBean id="mySelfRegistrationApp" scope="session" class="fr.paris.lutece.plugins.selfregistration.web.SelfRegistrationApp" />

<%
	response.sendRedirect( mySelfRegistrationApp.doCreateRegistration( request ) );
%>
