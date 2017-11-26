<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.OrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@include file="/global.jsp"%>


<%
	pageTitle = "iTrust - Select Orthopedic Office Visit";
%>
<%@include file="/header.jsp"%>

<%
	String visitID = request.getParameter("VISIT_ID");
	session.setAttribute("orthopedicVisitID", visitID);
	String pidString = request.getParameter("PID");
	session.setAttribute("pid", pidString);
	if (null != visitID && !visitID.isEmpty() && null != pidString && !pidString.isEmpty()) {
		response.sendRedirect(request.getParameter("forward"));
	}

	PatientDAO patientDAO = prodDAO.getPatientDAO();
	PatientBean userBean = patientDAO.getPatient(loggedInMID);
	OrthopedicVisitAction action = new OrthopedicVisitAction(prodDAO, loggedInMID, loggedInMID.toString());
	long pid = action.getPid();
%>
<input type="hidden" name="visitID" />
<h2>
	Orthopedic visits for
	<%=userBean.getFullName()%>:
</h2>

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
		<td>
			<form
				action="selectOrthopedicVisit.jsp?VISIT_ID=<%=bean.getOrthopedicVisitID()%>&PID=<%=loggedInMID%>&forward=viewOrthopedicVisit.jsp"
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
	
	List<PatientBean> dependants = patientDAO.getDependantPatients(loggedInMID);
	if (dependants.size() > 0) {
%>
<h2>Dependants</h2>
<br />
<%
	for (PatientBean d : dependants) {
%>

<h3>
	Orthopedic visits for
	<%=d.getFullName()%>:
</h3>

<br />
<%
	visits = action.getAllOrthopedicVisits(d.getMID());
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
		<td>
			<form
				action="selectOrthopedicVisit.jsp?VISIT_ID=<%=bean.getOrthopedicVisitID()%>&PID=<%=d.getMID()%>&forward=viewOrthopedicVisit.jsp"
				method="post">
				<input type="submit" value="View" />
			</form>
		</td>
	</tr>
	<%
		}
				} else {
					%>
					No prior orthopedic visits on record.
					<%
						}
			}
		}
	%>

	<%@include file="/footer.jsp"%>