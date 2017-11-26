<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page
	import="edu.ncsu.csc.itrust.action.SurgicalOrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.DefaultFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="java.util.Iterator"%>

<%@include file="/global.jsp"%>

<%
	pageTitle = "iTrust - View Surgical Orthopedic Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	SurgicalOrthopedicVisitAction action = new SurgicalOrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	SurgicalOrthopedicVisitBean bean = new SurgicalOrthopedicVisitBean();
	bean = action.getVisit(Long.parseLong((String) session.getAttribute("SurgicalOrthopedicVisitID")));
	loggingAction.logEvent(TransactionType.VIEW_SURGICAL_OPHTHALMOLOGY_OV, loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")), Long.toString(bean.getSurgicalOrthopedicVisitID()));
%>

<body>
	<h4>Appointment Date</h4>
	<%=bean.getSurgicalOrthopedicVisitDateString()%>
	<h4>Surgeries</h4>
	<%
		if (bean.hasSurgeries()) {
	%><table>
		<%
			if (bean.getSurgery(0)) {
		%><tr>
			<td>Total Knee Replacement</td>
		</tr>

		<%
			}
				if (bean.getSurgery(1)) {
		%><tr>
			<td>Total Joint Replacement</td>
		</tr>

		<%
			}
				if (bean.getSurgery(2)) {
		%><tr>
			<td>ACL Reconstruction</td>
		</tr>

		<%
			}
				if (bean.getSurgery(3)) {
		%><tr>
			<td>Ankle Replacement</td>
		</tr>

		<%
			}
				if (bean.getSurgery(4)) {
		%><tr>
			<td>Spine Surgery</td>
		</tr>

		<%
			}
				if (bean.getSurgery(5)) {
		%><tr>
			<td>Arthroscopic Surgery</td>
		</tr>

		<%
			}
				if (bean.getSurgery(6)) {
		%><tr>
			<td>Rotator Cuff Repair</td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		} else {
	%>
	None.
	<%
		}
	%>
	<h4>Surgery Notes</h4>
	<%
		String surgeryNotes = bean.getSurgicalNotes();
		if (null != surgeryNotes && !surgeryNotes.isEmpty()) {
	%>
	<%=surgeryNotes%>
	<%
		} else {
	%>
	None.
	<%
		}
	%>
</body>

<%@include file="/footer.jsp"%>