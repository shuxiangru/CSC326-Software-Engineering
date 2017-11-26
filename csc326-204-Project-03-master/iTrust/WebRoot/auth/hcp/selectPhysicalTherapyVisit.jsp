<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@include file="/global.jsp"%>


<%
	pageTitle = "iTrust - Select Physical Therapy Office Visit";
%>
<%@include file="/header.jsp"%>

<%
	String visitID = request.getParameter("PT_VISIT_ID");
	session.setAttribute("PhysicalTherapyVisitID", visitID);
	if (null != visitID && !"".equals(visitID)) {
		response.sendRedirect(request.getParameter("forward"));
	}

	/* Require a Patient ID first */
	String pidString = (String) session.getAttribute("pid");
	if (pidString == null || 1 > pidString.length()) {
		response.sendRedirect(
				"/iTrust/auth/getPatientID.jsp?forward=/iTrust/auth/hcp/selectPhysicalTherapyVisit.jsp");
		return;
	}

	PhysicalTherapyVisitAction action = new PhysicalTherapyVisitAction(prodDAO, loggedInMID, pidString);
	long pid = action.getPid();
%>
<input type="hidden" name="physicalTherapyID" />
<h2>
	Physical Therapy visits for Patient:
	<%=StringEscapeUtils.escapeHtml("" + pidString)%></h2>

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
			PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
					PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
					if (null != userBean.getSpecialty() && (userBean.getSpecialty().equals("Physical Therapist"))) {
		%>
		<td>
			<form
				action="selectPhysicalTherapyVisit.jsp?PT_VISIT_ID=<%=bean.getPhysicalTherapyVisitID()%>&forward=editPhysicalTherapyVisit.jsp"
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
				action="selectPhysicalTherapyVisit.jsp?PT_VISIT_ID=<%=bean.getPhysicalTherapyVisitID()%>&forward=viewPhysicalTherapyVisit.jsp"
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
No prior physical therapy visits on record.
<%
	}
%>

<%@include file="/footer.jsp"%>