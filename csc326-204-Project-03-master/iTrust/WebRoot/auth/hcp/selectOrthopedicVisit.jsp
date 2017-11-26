<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.OrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@include file="/global.jsp"%>


<%
	pageTitle = "iTrust - Select Orthopedic Office Visit";
%>
<%@include file="/header.jsp"%>

<%
	String visitID = request.getParameter("VISIT_ID");
	session.setAttribute("orthopedicVisitID", visitID);
	if (null != visitID && !"".equals(visitID)) {
		response.sendRedirect(request.getParameter("forward"));
	}

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect(
				"/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/selectOrthopedicVisit.jsp");
		return;
	}

	OrthopedicVisitAction action = new OrthopedicVisitAction(prodDAO, loggedInMID, pidString);
	long pid = action.getPid();
%>
<input type="hidden" name="visitID" />
<h2>
	Orthopedic visits for Patient:
	<%=StringEscapeUtils.escapeHtml("" + pidString)%></h2>

<%
PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
if (null != userBean.getSpecialty() &&
(userBean.getSpecialty().equals("Physical Therapist") ||
userBean.getSpecialty().equals("Orthopedic"))) {%>
<form action="addOrthopedicVisit.jsp" method="post">
	<input type="submit" value="Document Visit" />
</form>
<% }%>
<br />
<%
	List<OrthopedicVisitBean> visits = action.getAllOrthopedicVisits(pid);
	if (visits != null && visits.size() > 0) {
%>
<table class="visitsTable">
	<tr>
		<th>Visit ID</th>
		<th>Date</th>
		<th></th>
		<th></th>
	</tr>
	<%
		OrthopedicVisitBean bean;
			for (int i = 0; i < visits.size(); i++) {
				bean = visits.get(i);
	%>
	<tr>
		<td name="VISIT_ID"><%=StringEscapeUtils.escapeHtml("" + bean.getOrthopedicVisitID())%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + bean.getOrthopedicVisitDateString())%></td>

<%if (null != userBean.getSpecialty() &&
(userBean.getSpecialty().equals("Physical Therapist") ||
userBean.getSpecialty().equals("Orthopedic"))) {%>
		<td>
			<form
				action="selectOrthopedicVisit.jsp?VISIT_ID=<%=bean.getOrthopedicVisitID()%>&forward=editOrthopedicVisit.jsp"
				method="post">
				<input type="submit" value="Edit" />
			</form>
		</td>
<% }%>
		<td>
			<form
				action="selectOrthopedicVisit.jsp?VISIT_ID=<%=bean.getOrthopedicVisitID()%>&forward=viewOrthopedicVisit.jsp"
				method="post">
				<input type="submit" value="View" />
			</form>
		</td>
	</tr>
	<%
		}
	%>
</table>
<%
	} else {
%>
No prior orthopedic visits on record.
<%
	}
%>

<%@include file="/footer.jsp"%>