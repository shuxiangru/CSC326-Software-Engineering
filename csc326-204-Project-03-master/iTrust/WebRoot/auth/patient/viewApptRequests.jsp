<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="edu.ncsu.csc.itrust.action.EditApptTypeAction"%>
<%@page import="edu.ncsu.csc.itrust.action.ViewMyApptsAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.ApptRequestBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptTypeDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.ApptRequestDAO"%>
<%@page import="edu.ncsu.csc.itrust.dao.DAOFactory"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View My Appointment Requests";
%>

<%@include file="/header.jsp"%>

<div align=center>
	<h2>My Appointments</h2>
	<%
		loggingAction.logEvent(TransactionType.PATIENT_VIEW_APPPOINTMENT_REQUESTS, loggedInMID.longValue(), 0, "");

		ViewMyApptsAction action = new ViewMyApptsAction(prodDAO, loggedInMID.longValue());
		ApptRequestDAO apptRequestDAO = prodDAO.getApptRequestDAO();
		EditApptTypeAction types = new EditApptTypeAction(prodDAO, loggedInMID.longValue());
		ApptTypeDAO apptTypeDAO = prodDAO.getApptTypeDAO();
		List<ApptRequestBean> appts = apptRequestDAO.getApptRequestsForPatient(loggedInMID.longValue());
		session.setAttribute("appts", appts);
		if (appts.size() > 0) {
	%>
	<table class="fancyTable">
		<tr>
			<th>HCP</th>
			<th>Appointment Type</th>
			<th>Appointment Date/Time</th>
			<th>Status</th>
			<th>Comment</th>
		</tr>
		<%
			int index = 0;
				for (ApptRequestBean ar : appts) {
					ApptBean a = ar.getRequestedAppt();
					String comment = "";
					if (a.getComment() == null || a.getComment().isEmpty())
						comment = "No Comment";
					else
						comment = a.getComment();

					Date d = new Date(a.getDate().getTime());
					DateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

					String status = "";
					if (ar.isPending()) {
						if ((a.getDate()
								.compareTo(new Timestamp(System.currentTimeMillis()))) < 0) {
							status = "Rejected";
							ar.setPending(false);
							ar.setAccepted(false);
							apptRequestDAO.updateApptRequest(ar);
						} else
							status = "Pending";
					} else if (ar.isAccepted())
						status = "Accepted";
					else
						status = "Rejected";
		%>
		<%="<tr " + ((index % 2 == 1) ? "class=\"alt\"" : "") + ">"%>
		<td><%=StringEscapeUtils.escapeHtml("" + (action.getName(a.getHcp())))%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + (a.getApptType()))%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + (format.format(d)))%></td>
		<td><%=StringEscapeUtils.escapeHtml("" + status)%></td>
		<td><%=comment%></td>
		</tr>
		<%
			index++;
		%>
		<%
			}
		%>
	</table>
	<%
		} else {
	%>
	<div>
		<i>You have no Appointments</i>
	</div>
	<%
		}
	%>
	<br />
</div>

<%@include file="/footer.jsp"%>
