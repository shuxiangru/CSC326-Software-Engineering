<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.OrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean"%>
<%@page
	import="edu.ncsu.csc.itrust.action.SurgicalOrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean"%>
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
	pageTitle = "iTrust - View Orthopedic Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	OrthopedicVisitAction action = new OrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	PhysicalTherapyVisitAction ptAction = new PhysicalTherapyVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	SurgicalOrthopedicVisitAction soAction = new SurgicalOrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	OrthopedicVisitBean bean = new OrthopedicVisitBean();
	bean = action.viewBean(Long.parseLong((String) session.getAttribute("orthopedicVisitID")));
	loggingAction.logEvent(TransactionType.VIEW_ORTHOPEDIC_OV, loggedInMID.longValue(),
			Long.parseLong((String) session.getAttribute("pid")), Long.toString(bean.getOrthopedicVisitID()));
%>

<body>
	<h4>Appointment Date</h4>
	<%=bean.getOrthopedicVisitDateString()%>
	<h4>Limb or Joint</h4>
	<%=bean.getInjuredLimbJoint()%>
	<%
		if (null != bean.getXRay() && 0 != bean.getXRay().length) {
	%>
	<h4>X-Ray Image</h4>
	<img
		src="data:image/png;base64,<%=new String(Base64.encodeBase64(bean.getXRay()))%>">
	<%
		}
		if (null != bean.getMRI() && 0 != bean.getMRI().length) {
	%>
	<h4>MRI Image</h4>
	<img
		src="data:image/png;base64,<%=new String(Base64.encodeBase64(bean.getMRI()))%>">
	<%
		}
		if (null != bean.getMRIreport() && !bean.getMRIreport().isEmpty()) {
	%>
	<h4>MRI Report</h4>
	<%=bean.getMRIreport()%>
	<%
		}
		if (-1 != bean.getACLinjury()) {
	%>
	<h4>Anterior cruciate ligament injury</h4>
	<%=bean.getACLinjury() == 0 ? "False" : "True"%>
	<%
		}
		if (-1 != bean.getMeniscusTear()) {
	%>
	<h4>Meniscus tear</h4>
	<%=bean.getMeniscusTear() == 0 ? "False" : "True"%>
	<%
		}
		if (-1 != bean.getRAhand()) {
	%>
	<h4>Rheumatoid arthritis of hand</h4>
	<%=bean.getRAhand() == 0 ? "False" : "True"%>
	<%
		}
		if (-1 != bean.getChondromalacia()) {
	%>
	<h4>Chondromalacia</h4>
	<%=bean.getChondromalacia() == 0 ? "False" : "True"%>
	<%
		}
		if (-1 != bean.getCPC()) {
	%>
	<h4>Congenital pes cavus</h4>
	<%=bean.getCPC()%>
	<%
		}
		if (-1 != bean.getWhiplashinjury()) {
	%>
	<h4>Whiplash injury</h4>
	<%=bean.getWhiplashinjury() == 0 ? "False" : "True"%>
	<%
		}

		List<PhysicalTherapyVisitBean> ptVisits = ptAction.getPhysicalTherapyVisitsByOrthopedicVisitID(
				Long.parseLong((String) session.getAttribute("pid")), bean.getOrthopedicVisitID());

		if (ptVisits.size() > 0) {
	%>
	<h4>Physical Therapy Visits</h4>
	<table>
		<tr>
			<th>Visit ID</th>
			<th>Visit Date</th>
			<th>Physical Therapist MID</th>
		</tr>
		<%
			for (PhysicalTherapyVisitBean ptb : ptVisits) {
		%>
		<tr>
			<td name="PT_VISIT_ID"><%=StringEscapeUtils.escapeHtml("" + ptb.getPhysicalTherapyVisitID())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + ptb.getPhysicalTherapyVisitDateString())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + ptb.getPhysicalTherapistID())%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}

		List<SurgicalOrthopedicVisitBean> soVisits = soAction.getSurgicalOrthopedicVisitsByOrthopedicVisitID(
				Long.parseLong((String) session.getAttribute("pid")), bean.getOrthopedicVisitID());

		if (soVisits.size() > 0) {
	%>
	<h4>Surgical Orthopedic Visits</h4>
	<table>
		<tr>
			<th>Visit ID</th>
			<th>Visit Date</th>
			<th>Orthopedic MID</th>
		</tr>
		<%
			for (SurgicalOrthopedicVisitBean sob : soVisits) {
		%>
		<tr>
			<td name="PT_VISIT_ID"><%=StringEscapeUtils.escapeHtml("" + sob.getSurgicalOrthopedicVisitID())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + sob.getSurgicalOrthopedicVisitDateString())%></td>
			<td><%=StringEscapeUtils.escapeHtml("" + sob.getOrthopedicID())%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
	%>
</body>

<%@include file="/footer.jsp"%>