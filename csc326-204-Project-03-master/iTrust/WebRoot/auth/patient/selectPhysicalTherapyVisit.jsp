<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PatientBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PatientDAO"%>
<%@include file="/global.jsp"%>


<%
	pageTitle = "iTrust - Select Physical Therapy Office Visit";
%>
<%@include file="/header.jsp"%>

<%
	String visitID = request.getParameter("PT_VISIT_ID");
	session.setAttribute("PhysicalTherapyVisitID", visitID);
	String pidString = request.getParameter("PID");
	session.setAttribute("pid", pidString);
	if (null != visitID && !visitID.isEmpty() && null != pidString && !pidString.isEmpty()) {
		response.sendRedirect(request.getParameter("forward"));
	}

	PatientDAO patientDAO = prodDAO.getPatientDAO();
	PatientBean userBean = patientDAO.getPatient(loggedInMID);
	PhysicalTherapyVisitAction action = new PhysicalTherapyVisitAction(prodDAO, loggedInMID,
			loggedInMID.toString());
	long pid = action.getPid();
%>
<input type="hidden" name="physicalTherapyID" />
<h2>
	Physical Therapy visits for
	<%=userBean.getFullName()%>:
</h2>

<br />
<%
	List<PhysicalTherapyVisitBean> visits = action.getAllPhysicalTherapyVisits(pid);
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
		PhysicalTherapyVisitBean bean;
			for (int i = 0; i < visits.size(); i++) {
				bean = visits.get(i);
	%>
	<tr>
		<td name="PT_VISIT_ID"><%=StringEscapeUtils.escapeHtml("" + bean.getPhysicalTherapyVisitID())%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + bean.getPhysicalTherapyVisitDateString())%></td>

		<%
			if (bean.getAddedVisit()) {
		%>
		<td>
			<form
				action="selectPhysicalTherapyVisit.jsp?PT_VISIT_ID=<%=bean.getPhysicalTherapyVisitID()%>&PID=<%=loggedInMID%>&forward=viewPhysicalTherapyVisit.jsp"
				method="post">
				<input type="submit" value="View" />
			</form>
		</td>
		<%
			} else {
		%>
		<td>
		Visit Not Yet Documented.
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
No prior physical therapy visits on record.
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
	Physical Therapy visits for
	<%=d.getFullName()%>:
</h3>
<%
	visits = action.getAllPhysicalTherapyVisits(d.getMID());
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
		PhysicalTherapyVisitBean bean;
					for (int i = 0; i < visits.size(); i++) {
						bean = visits.get(i);
	%>
	<tr>
		<td name="PT_VISIT_ID"><%=StringEscapeUtils.escapeHtml("" + bean.getPhysicalTherapyVisitID())%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + bean.getPhysicalTherapyVisitDateString())%></td>

		<%
			if (bean.getAddedVisit()) {
		%>
		<td>
			<form
				action="selectPhysicalTherapyVisit.jsp?PT_VISIT_ID=<%=bean.getPhysicalTherapyVisitID()%>&PID=<%=d.getMID()%>&forward=viewPhysicalTherapyVisit.jsp"
				method="post">
				<input type="submit" value="View" />
			</form>
		</td>
		<%
			} else {
		%>
		<td>
		Visit Not Yet Documented.
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
No prior physical therapy visits on record.
<%
	}
		}
	}
%>

<%@include file="/footer.jsp"%>