<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.OrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.OrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean"%>
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
	pageTitle = "iTrust - Edit Orthopedic Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
	PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
	String error = "";
	boolean hasErrors = false;
	if (null != userBean.getSpecialty() && userBean.getSpecialty().equals("Physical Therapist")
			|| userBean.getSpecialty().equals("Orthopedic")) {

		OrthopedicVisitAction action = new OrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
				(String) session.getAttribute("pid"));
		PhysicalTherapyVisitAction ptAction = new PhysicalTherapyVisitAction(prodDAO, loggedInMID.longValue(),
				(String) session.getAttribute("pid"));
		SurgicalOrthopedicVisitAction soAction = new SurgicalOrthopedicVisitAction(prodDAO,
				loggedInMID.longValue(), (String) session.getAttribute("pid"));

		OrthopedicVisitBean bean = new OrthopedicVisitBean();
		bean = action.viewBean(Long.parseLong((String) session.getAttribute("orthopedicVisitID")));

		String appointmentDate = request.getParameter("appointmentDate");
		if (isMultipart || formIsFilled) {
			FileItemFactory factory = new DefaultFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			String ptAppointmentDate = "";
			String soAppointmentDate = "";
			long physicalTherapistID = -1;
			long orthopedicID = -1;
			bean.setPatientID(Long.parseLong((String) session.getAttribute("pid")));
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if ("acli".equals(item.getFieldName())) {
					bean.setACLinjury(Short.parseShort(item.getString()));
				} else if ("appointmentDate".equals(item.getFieldName())) {

					if (!"".equals(item.getString())) {
						appointmentDate = item.getString();
						bean.setOrthopedicVisitDate(item.getString());
					} else {
						error += "<p class=\"iTrustError\">Date field is required.</p>";
						hasErrors = true;
					}
				} else if ("ptAppointmentDate".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						ptAppointmentDate = item.getString();
					}
				} else if ("soAppointmentDate".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						soAppointmentDate = item.getString();
					}
				} else if ("limb&joint".equals(item.getFieldName())) {
					if (item.getString() == null || "".equals(item.getString())) {
						error += "<p class=\"iTrustError\">Injured Limb or Joint is required.</p>";
						hasErrors = true;
					} else if (item.getString().length() > 50) {
						error += "<p class=\"iTrustError\">Injured Limb or Joint must be less than 50 characters.</p>";
						hasErrors = true;
					} else {
						bean.setInjuredLimbJoint(item.getString());
					}
				} else if ("MRIReport".equals(item.getFieldName())) {
					if (item.getString().length() <= 200) {
						bean.setMRIreport(item.getString());
					} else {
						error += "<p class=\"iTrustError\">MRI report must be less than 200 characters.</p>";
						hasErrors = true;
					}
				} else if ("mt".equals(item.getFieldName())) {
					bean.setMeniscusTear(Short.parseShort(item.getString()));
				} else if ("raoh".equals(item.getFieldName())) {
					bean.setRAhand(Short.parseShort(item.getString()));
				} else if ("c".equals(item.getFieldName())) {
					bean.setChondromalacia(Short.parseShort(item.getString()));
				} else if ("cpc".equals(item.getFieldName())) {
					bean.setCPC(Short.parseShort(item.getString()));
				} else if ("wi".equals(item.getFieldName())) {
					bean.setWhiplashinjury(Short.parseShort(item.getString()));
				} else if ("XRAYImage".equals(item.getFieldName())) {
					if (item.getName().matches("(.*).jpg") || item.getName().matches("(.*).jpeg")
							|| item.getName().matches("(.*).png")) {
						if (item.get().length == 0) {
							bean.setXRay(bean.getXRay());
						} else {
							bean.setXRay(item.get());
						}
					} else if (item.get().length != 0) {
						error += "<p class=\"iTrustError\">XRAYImage only accept png, jpg, jpeg type file</p>";
						hasErrors = true;
					}
				} else if ("MRIImage".equals(item.getFieldName())) {
					if (item.getName().matches("(.*).jpg") || item.getName().matches("(.*).jpeg")
							|| item.getName().matches("(.*).png")) {
						if (item.get().length == 0) {
							bean.setMRI(bean.getMRI());
						} else {
							bean.setMRI(item.get());
						}
					} else if (item.get().length != 0) {
						error += "<p class=\"iTrustError\">MRIImage only accept png, jpg, jpeg type file</p>";
						hasErrors = true;
					}
				} else if ("Orthopedic".equals(userBean.getSpecialty())
						&& "physicalTherapistID".equals(item.getFieldName())) {
					try {
						physicalTherapistID = Long.parseLong(item.getString());
						PersonnelBean hcpBean = personnelDAO.getPersonnel(physicalTherapistID);
						if (null == hcpBean) {
							error += "<p class=\"iTrustError\">MID provided for Physical Therapist is not an hcp.</p>";
							hasErrors = true;
						} else {
							if ("Physical Therapist".equals(hcpBean.getSpecialty())) {
							} else {
								error += "<p class=\"iTrustError\">MID provided for Physical Therapist is not a physical therapist MID.</p>";
								hasErrors = true;
							}
						}
					} catch (Exception e) {
						if (!item.getString().isEmpty()) {
							error += "<p class=\"iTrustError\">Please enter a number for the Physical Therapist MID</p>";
							hasErrors = true;
						}
					}
				} else if ("orthopedicID".equals(item.getFieldName())) {
					try {
						orthopedicID = Long.parseLong(item.getString());
						PersonnelBean hcpBean = personnelDAO.getPersonnel(orthopedicID);
						if (null == hcpBean) {
							error += "<p class=\"iTrustError\">MID provided for Surgical Orthopedic is not an hcp.</p>";
							hasErrors = true;
						} else {
							if ("Orthopedic".equals(hcpBean.getSpecialty())) {
							} else {
								error += "<p class=\"iTrustError\">MID provided for Surgical Orthopedic is not an orthopedic MID.</p>";
								hasErrors = true;
							}
						}
					} catch (Exception e) {
						if (!item.getString().isEmpty()) {
							error += "<p class=\"iTrustError\">Please enter a number for the Surgical Orthopedic MID</p>";
							hasErrors = true;
						}
					}
				}
			}

			if ("Orthopedic".equals(userBean.getSpecialty()) && physicalTherapistID == -1
					&& !ptAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">MID for Physical Therapist is required if Physical Therapy Date is selected.</p>";
				hasErrors = true;
			} else if ("Orthopedic".equals(userBean.getSpecialty()) && physicalTherapistID != -1
					&& ptAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">Physical Therapy Appointment Date field is required if a Physical Therapist is selected.</p>";
				hasErrors = true;
			}

			if ("Orthopedic".equals(userBean.getSpecialty()) && orthopedicID == -1
					&& !soAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">MID for Orthopedic is required if Surgical Orthopedic Visit Date is selected.</p>";
				hasErrors = true;
			} else if ("Orthopedic".equals(userBean.getSpecialty()) && orthopedicID != -1
					&& soAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">Orthopedic Appointment Date field is required if Surgical Orthopedic Visit date is selected.</p>";
				hasErrors = true;
			}
			if (!hasErrors) {
				try {
					action.editBean(bean);
					loggingAction.logEvent(TransactionType.EDIT_ORTHOPEDIC_OV, loggedInMID.longValue(),
							Long.parseLong((String) session.getAttribute("pid")),
							Long.toString(bean.getOrthopedicVisitID()));
					if ("Orthopedic".equals(userBean.getSpecialty()) && physicalTherapistID != -1
							&& !ptAppointmentDate.isEmpty()) {
						PhysicalTherapyVisitBean ptBean = ptAction.orderVisit(physicalTherapistID,
								ptAppointmentDate, bean.getOrthopedicVisitID());
						loggingAction.logEvent(TransactionType.CREATE_PHYSICAL_THERAPY_OV,
								loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")),
								Long.toString(ptBean.getPhysicalTherapyVisitID()));
					}
					if ("Orthopedic".equals(userBean.getSpecialty()) && orthopedicID != -1
							&& !soAppointmentDate.isEmpty()) {
						SurgicalOrthopedicVisitBean soBean = soAction.orderVisit(orthopedicID,
								soAppointmentDate, bean.getOrthopedicVisitID());
						loggingAction.logEvent(TransactionType.CREATE_SURGICAL_OPHTHALMOLOGY_OV,
								loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")),
								Long.toString(soBean.getSurgicalOrthopedicVisitID()));
					}
					response.sendRedirect("selectOrthopedicVisit.jsp");
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
		action="editOrthopedicVisit.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<table>
			<tr class="subHeader">
				<td>Orthopedic Office Visit Date:</td>
				<td><input onchange="singleDate();" name="appointmentDate"
					id="appointmentDate"
					value="<%=StringEscapeUtils.escapeHtml(bean.getOrthopedicVisitDateString())%>"
					size="10" readonly> <input type="button"
					value="Select Date" onclick="displayDatePicker('appointmentDate');"></td>
			</tr>
		</table>
		<br> <br>
		<h4>Limb or Joint</h4>
		<input type="text" name="limb&joint"
			value="<%=bean.getInjuredLimbJoint()%>"> <br> <br>
		<%
			if (null != bean.getXRay() && 0 != bean.getXRay().length) {
		%>
		<h4>X-Ray Image</h4>
		<img
			src="data:image/png;base64,<%=new String(Base64.encodeBase64(bean.getXRay()))%>">
		<%
			}
		%>
		<table class="fTable mainTable">
			<tr>
				<th colspan="3">X-Ray Image</th>
			</tr>
			<tr>
				<td><input name="XRAYImage" type="file" /></td>
			</tr>
		</table>
		<br> <br>
		<%
			if (null != bean.getMRI() && 0 != bean.getMRI().length) {
		%>
		<h4>X-Ray Image</h4>
		<img
			src="data:image/png;base64,<%=new String(Base64.encodeBase64(bean.getMRI()))%>">
		<%
			}
		%>
		<table class="fTable mainTable">
			<tr>
				<th colspan="3">MRI Image</th>
			</tr>
			<tr>
				<td><input name="MRIImage" type="file" /></td>
			</tr>
		</table>
		<br> <br>
		<h4>MRI Report</h4>
		<textarea type="text" name="MRIReport" rows="5" cols="60"><%=bean.getMRIreport()%></textarea>
		<br> <br>
		<h4>Anterior cruciate ligament injury</h4>
		<select name="acli">
			<option value="-1" <%if (-1 == bean.getACLinjury()) {%>
				selected="selected" <%}%>>Select</option>
			<option value="1" <%if (1 == bean.getACLinjury()) {%>
				selected="selected" <%}%>>True</option>
			<option value="0" <%if (0 == bean.getACLinjury()) {%>
				selected="selected" <%}%>>False</option>
		</select> <br> <br>
		<h4>Meniscus tear</h4>
		<select name="mt">
			<option value="-1" <%if (-1 == bean.getMeniscusTear()) {%>
				selected="selected" <%}%>>Select</option>
			<option value="1" <%if (1 == bean.getMeniscusTear()) {%>
				selected="selected" <%}%>>True</option>
			<option value="0" <%if (0 == bean.getMeniscusTear()) {%>
				selected="selected" <%}%>>False</option>
		</select> <br> <br>
		<h4>Rheumatoid arthritis of hand</h4>
		<select name="raoh">
			<option value="-1" <%if (-1 == bean.getRAhand()) {%>
				selected="selected" <%}%>>Select</option>
			<option value="1" <%if (1 == bean.getRAhand()) {%>
				selected="selected" <%}%>>True</option>
			<option value="0" <%if (0 == bean.getRAhand()) {%>
				selected="selected" <%}%>>False</option>
		</select> <br> <br>
		<h4>Chondromalacia</h4>
		<select name="c">
			<option value="-1" <%if (-1 == bean.getChondromalacia()) {%>
				selected="selected" <%}%>>Select</option>
			<option value="1" <%if (1 == bean.getChondromalacia()) {%>
				selected="selected" <%}%>>True</option>
			<option value="0" <%if (0 == bean.getChondromalacia()) {%>
				selected="selected" <%}%>>False</option>
		</select> <br> <br>
		<h4>Congenital pes cavus</h4>
		<select name="cpc">
			<option value="-1" <%if (-1 == bean.getCPC()) {%> selected="selected"
				<%}%>>Select</option>
			<option value="1" <%if (1 == bean.getCPC()) {%> selected="selected"
				<%}%>>True</option>
			<option value="0" <%if (0 == bean.getCPC()) {%> selected="selected"
				<%}%>>False</option>
		</select> <br> <br>
		<h4>Whiplash injury</h4>
		<select name="wi">
			<option value="-1" <%if (-1 == bean.getWhiplashinjury()) {%>
				selected="selected" <%}%>>Select</option>
			<option value="1" <%if (1 == bean.getWhiplashinjury()) {%>
				selected="selected" <%}%>>True</option>
			<option value="0" <%if (0 == bean.getWhiplashinjury()) {%>
				selected="selected" <%}%>>False</option>
		</select> <br>
		<%
			if ("Orthopedic".equals(userBean.getSpecialty())) {

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
		%>
		<h4>Order Physical Therapy Visits</h4>
		<table>
			<tr class="subHeader">
				<td>Appointment Date:</td>
				<td><input onchange="singleDate();" name="ptAppointmentDate"
					id="ptAppointmentDate" size="10" readonly /> <input type="button"
					value="Select Date"
					onclick="displayDatePicker('ptAppointmentDate');" /></td>
			</tr>
		</table>
		<p>
			Enter the MID for a physical therapist below, and a Physical therapy
			visit will be created <br> to be handled by that physical
			therapist for the currently selected patient.
		</p>
		<input type="text" name="physicalTherapistID" /> <br> <br>
		<%
			List<SurgicalOrthopedicVisitBean> soVisits = soAction
							.getSurgicalOrthopedicVisitsByOrthopedicVisitID(
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
		<h4>Order Surgical Orthopedic Office Visit</h4>
		<table>
			<tr class="subHeader">
				<td>Appointment Date:</td>
				<td><input onchange="singleDate();" name="soAppointmentDate"
					id="soAppointmentDate" size="10" readonly /> <input type="button"
					value="Select Date"
					onclick="displayDatePicker('soAppointmentDate');" /></td>
			</tr>
		</table>
		<p>
			Enter the MID for an orthopedic below, and a Surgical Orthopedic
			visit will be created <br> to be handled by that
			orthopedic for the currently selected patient.
		</p>
		<input type="text" name="orthopedicID" /> <br>
		<%
			}
		%>
		<input type="submit" style="font-size: 16pt; font-weight: bold;"
			value="Update Orthopedic Visit">
	</form>
</body>
<%
	} else {
		//Person is not of the correct specialization
%>
<h4>Documenting Orthopedic visits can only be done by Orthopedic
	and Physical Therapy specialists. Click below to create a regular
	office visit instead.</h4>

<form id="officeVisitForward"
	action="../hcp-uap/documentOfficeVisit.jsp" method="POST">
	<input type="submit" value="Document Regular Office Visit" />
</form>
<%
	}
%>

<%@include file="/footer.jsp"%>