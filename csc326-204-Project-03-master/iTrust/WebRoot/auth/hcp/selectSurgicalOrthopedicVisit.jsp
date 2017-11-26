<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.SurgicalOrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@include file="/global.jsp"%>


<%
	pageTitle = "iTrust - Select Surgical Orthopedic Office Visit";
%>
<%@include file="/header.jsp"%>

<%
	String visitID = request.getParameter("SO_VISIT_ID");
	session.setAttribute("SurgicalOrthopedicVisitID", visitID);
	if (null != visitID && !"".equals(visitID)) {
		response.sendRedirect(request.getParameter("forward"));
	}

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect(
				"/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/selectSurgicalOrthopedicVisit.jsp");
		return;
	}

	SurgicalOrthopedicVisitAction action = new SurgicalOrthopedicVisitAction(prodDAO, loggedInMID, pidString);
	long pid = action.getPid();
%>
<input type="hidden" name="SurgicalOrthopedicID" />
<h2>
	Surgical Orthopedic visits for Patient:
	<%=StringEscapeUtils.escapeHtml("" + pidString)%></h2>

<br />
<%
	List<SurgicalOrthopedicVisitBean> visits = action.getAllSurgicalOrthopedicVisits(pid);
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
		SurgicalOrthopedicVisitBean bean;
			for (int i = 0; i < visits.size(); i++) {
				bean = visits.get(i);
	%>
	<tr>
		<td name="PT_VISIT_ID"><%=StringEscapeUtils.escapeHtml("" + bean.getSurgicalOrthopedicVisitID())%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + bean.getSurgicalOrthopedicVisitDateString())%></td>

		<%
			PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
					PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
					if (null != userBean.getSpecialty() && (userBean.getSpecialty().equals("Orthopedic"))) {
		%>
		<td>
			<form
				action="selectSurgicalOrthopedicVisit.jsp?SO_VISIT_ID=<%=bean.getSurgicalOrthopedicVisitID()%>&forward=editSurgicalOrthopedicVisit.jsp"
				method="post">
				<input
					type="submit"
					value="<%=bean.getAddedVisit() ? "Edit" : "Document"%>" />

			</form>
		</td>
		<%
			} else {
		%>
		<td></td>
		<%
			}
					if (bean.getAddedVisit()) {
		%>
		<td>
			<form
				action="selectSurgicalOrthopedicVisit.jsp?SO_VISIT_ID=<%=bean.getSurgicalOrthopedicVisitID()%>&forward=viewSurgicalOrthopedicVisit.jsp"
				method="post">
				<input type="submit" value="View" />
			</form>
		</td>
		<%
			}
		%>
	</tr>
	<%
		}
	%>
</table>
<%
	} else {
%>
No prior orthopedic office visits on record.
<%
	}
%>

<%@include file="/footer.jsp"%>