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
<%@page import="java.util.Iterator"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>

<%@include file="/global.jsp"%>

<%
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");

	SurgicalOrthopedicVisitAction action = new SurgicalOrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	SurgicalOrthopedicVisitBean bean = new SurgicalOrthopedicVisitBean();
	bean = action.getVisit(Long.parseLong((String) session.getAttribute("SurgicalOrthopedicVisitID")));
	pageTitle = "iTrust - " + (bean.getAddedVisit() ? "Edit" : "Document")
			+ " Surgical Orthopedic Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
	PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
	String error = "";
	boolean hasErrors = false;
	if (userBean.getSpecialty().equals("Orthopedic")) {

		String startDate = request.getParameter("ptAppointmentDate");
		if (isMultipart || formIsFilled) {
			FileItemFactory factory = new DefaultFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			boolean[] surgeries = new boolean[7];
			List<FileItem> items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if ("ptAppointmentDate".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setSurgicalOrthopedicVisitDate(item.getString());
					} else {
						error += "<p class=\"iTrustError\">Date field is required.</p>";
						hasErrors = true;
					}
				} else if ("TotalKneeReplacement".equals(item.getFieldName())) {
					surgeries[0] = true;
				} else if ("TotalJointReplacement".equals(item.getFieldName())) {
					surgeries[1] = true;
				} else if ("ACLReconstruction".equals(item.getFieldName())) {
					surgeries[2] = true;
				} else if ("AnkleReplacement".equals(item.getFieldName())) {
					surgeries[3] = true;
				} else if ("SpineSurgery".equals(item.getFieldName())) {
					surgeries[4] = true;
				} else if ("ArthroscopicSurgery".equals(item.getFieldName())) {
					surgeries[5] = true;
				} else if ("RotatorCuffRepair".equals(item.getFieldName())) {
					surgeries[6] = true;
				} else if ("SurgeryNotes".equals(item.getFieldName())) {
					bean.setSurgicalNotes(item.getString());
				}
			}
			if (!hasErrors) {
				try {
					bean.setSurgeries(surgeries);
					if (!bean.getAddedVisit())
						bean.setAddedVisit(true);
					action.editVisit(bean);
					loggingAction.logEvent(TransactionType.EDIT_SURGICAL_OPHTHALMOLOGY_OV, loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")), Long.toString(bean.getSurgicalOrthopedicVisitID()));
					response.sendRedirect("selectSurgicalOrthopedicVisit.jsp");
				} catch (Exception e) {
					error += "<p class=\"iTrustError\">Error Submitting form to database.</p>";
					hasErrors = true;
				}
			}
		}
		if (hasErrors) {
			out.write(error);
		}
%>
<body>
	<form id="filterForm" enctype="multipart/form-data"
		action="editSurgicalOrthopedicVisit.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<%
			if (bean.getAddedVisit()) {
		%>

		<table>
			<tr class="subHeader">
				<td>Appointment Date:</td>
				<td><input onchange="singleDate();" name="soAppointmentDate"
					id="soAppointmentDate"
					value="<%=StringEscapeUtils.escapeHtml(bean.getSurgicalOrthopedicVisitDateString())%>"
					size="10" readonly /> <input type="button" value="Select Date"
					onclick="displayDatePicker('soAppointmentDate');" /></td>
			</tr>
		</table>
		<%
			} else {
		%>
		<h4>Appointment Date</h4>
		<%=bean.getSurgicalOrthopedicVisitDateString()%>
		<%
			}
		%>
		<h4>Exercises</h4>
		<table>
			<tr>
				<td><input type="checkbox" name="TotalKneeReplacement"
					value="1" <%if (bean.getSurgery(0)) {%> checked="checked" <%}%>>
					Total Knee Replacement<br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="TotalJointReplacement"
					value="1" <%if (bean.getSurgery(1)) {%> checked="checked" <%}%>>
					Total Joint Replacement<br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="ACLReconstruction" value="1"
					<%if (bean.getSurgery(2)) {%> checked="checked" <%}%>> ACL
					Reconstruction<br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="AnkleReplacement" value="1"
					<%if (bean.getSurgery(3)) {%> checked="checked" <%}%>>
					Ankle Replacement<br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="SpineSurgery" value="1"
					<%if (bean.getSurgery(4)) {%> checked="checked" <%}%>>
					Spine Surgery<br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="ArthroscopicSurgery" value="1"
					<%if (bean.getSurgery(5)) {%> checked="checked" <%}%>>
					Arthroscopic Surgery<br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="RotatorCuffRepair"
					value="1" <%if (bean.getSurgery(6)) {%> checked="checked" <%}%>>
					Rotator Cuff Repair<br></td>
			</tr>
		</table>
		<br> <br>
		<h4>Surgery Notes</h4>
		<textarea type="text" name="SurgeryNotes" rows="5" cols="60"><%=bean.getSurgicalNotes()%></textarea>
		<br> <br> <input type="submit"
			style="font-size: 16pt; font-weight: bold;"
			value="Update Surgical Orthopedic Visit">
	</form>
</body>
<%
	} else {
		//Person is not of the correct specialization
%>
<h4>Documenting Surgical Orthopedic Therapy visits can only be done
	by Orthopedic and Surgical Orthopedic specialists. Click below to
	create a regular office visit instead.</h4>

<form id="officeVisitForward"
	action="../hcp-uap/documentOfficeVisit.jsp" method="POST">
	<input type="submit" value="Document Regular Office Visit" />
</form>
<%
	}
%>

<%@include file="/footer.jsp"%>