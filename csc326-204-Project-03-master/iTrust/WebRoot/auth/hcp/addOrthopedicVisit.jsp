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
<%@page import="edu.ncsu.csc.itrust.action.SurgicalOrthopedicVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.SurgicalOrthopedicVisitBean"%>
<%@page import="edu.ncsu.csc.itrust.beans.PersonnelBean"%>
<%@page import="edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.DefaultFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="edu.ncsu.csc.itrust.exception.DBException"%>

<%@include file="/global.jsp"%>

<%
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	boolean formIsFilled = request.getParameter("formIsFilled") != null
			&& request.getParameter("formIsFilled").equals("true");
	pageTitle = "iTrust - Add Orthopedic Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
	PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
	String error = "";
	boolean hasErrors = false;
	if (null != userBean.getSpecialty() && (userBean.getSpecialty().equals("Physical Therapist")
	|| userBean.getSpecialty().equals("Orthopedic"))) {

		OrthopedicVisitAction action = new OrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
		(String) session.getAttribute("pid"));
		PhysicalTherapyVisitAction ptAction = null;
		SurgicalOrthopedicVisitAction soAction = null;

		OrthopedicVisitBean bean = new OrthopedicVisitBean();

		if (isMultipart || formIsFilled) {
	FileItemFactory factory = new DefaultFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	List<FileItem> items = upload.parseRequest(request);
	Iterator iter = items.iterator();
	bean.setPatientID(Long.parseLong((String) session.getAttribute("pid")));
	String ptAppointmentDate = "";
	String soAppointmentDate = "";
	long physicalTherapistID = -1;
	long orthopedicID = -1;
	while (iter.hasNext()) {
		FileItem item = (FileItem) iter.next();
		if ("acli".equals(item.getFieldName())) {
			bean.setACLinjury(Short.parseShort(item.getString()));
		} else if ("orthopedicVisitDate".equals(item.getFieldName())) {
			if (!"".equals(item.getString())) {
				bean.setOrthopedicVisitDate(item.getString());
			} else {
				error += "<p class=\"iTrustError\">Date field is required.</p>";
				hasErrors = true;
			}
		} else if ("ptAppointmentDate".equals(item.getFieldName())) {
			if (!"".equals(item.getString())) {
				ptAppointmentDate = item.getString();
			}
		} else if("soAppointmentDate".equals(item.getFieldName() ) ) {
			if(!"".equals(item.getString())) {
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
				bean.setXRay(item.get());
			} else if (item.get().length != 0) {
				error += "<p class=\"iTrustError\">XRAYImage only accept png, jpg, jpeg type file</p>";
				hasErrors = true;
			}
		} else if ("MRIImage".equals(item.getFieldName())) {
			if (item.getName().matches("(.*).jpg") || item.getName().matches("(.*).jpeg")
					|| item.getName().matches("(.*).png")) {
				bean.setMRI(item.get());
			} else if (item.get().length != 0) {
				error += "<p class=\"iTrustError\">MRIImage only accept png, jpg, jpeg type file</p>";
				hasErrors = true;
			}
		} else if ("Orthopedic".equals(userBean.getSpecialty() ) && 
				   "physicalTherapistID".equals(item.getFieldName())) {
			try {
				physicalTherapistID = Long.parseLong(item.getString());
				PersonnelBean hcpBean = personnelDAO.getPersonnel(physicalTherapistID);
				if (null == hcpBean) {
					error += "<p class=\"iTrustError\">MID provided for Physical Therapist is not an hcp.</p>";
					hasErrors = true;
				} else {
					if ("Physical Therapist".equals(hcpBean.getSpecialty())) {
						ptAction = new PhysicalTherapyVisitAction(prodDAO, loggedInMID.longValue(),
								(String) session.getAttribute("pid"));
					} else {
						error += "<p class=\"iTrustError\">MID provided for Physical Therapist is not a physical therapist.</p>";
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
								soAction = new SurgicalOrthopedicVisitAction(prodDAO, loggedInMID.longValue(),
										(String) session.getAttribute("pid"));
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
				error += "<p class=\"iTrustError\">MID for Physical Therapist is required.</p>";
				hasErrors = true;
			} else if ("Orthopedic".equals(userBean.getSpecialty()) && null != ptAction
					&& ptAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">Physical Therapy Appointment Date field is required.</p>";
				hasErrors = true;
			}

			if ("Orthopedic".equals(userBean.getSpecialty()) && orthopedicID == -1
					&& !soAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">MID for Orthopedic is required if Surgical Orthopedic Visit Date is selected.</p>";
				hasErrors = true;
			} else if ("Orthopedic".equals(userBean.getSpecialty()) && null != soAction
					&& soAppointmentDate.isEmpty()) {
				error += "<p class=\"iTrustError\">Orthopedic Appointment Date field is required if Surgical Orthopedic Visit date is selected.</p>";
				hasErrors = true;
			}

			if (!hasErrors) {
				try {
					bean.setOrthopedicID(loggedInMID.longValue());
					action.addBean(bean);
					loggingAction.logEvent(TransactionType.CREATE_ORTHOPEDIC_OV, loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")), Long.toString(bean.getOrthopedicVisitID()));
					if ("Orthopedic".equals(userBean.getSpecialty()) && null != ptAction
							&& !ptAppointmentDate.isEmpty()) {
						PhysicalTherapyVisitBean ptBean = ptAction.orderVisit(physicalTherapistID, ptAppointmentDate, bean.getOrthopedicVisitID());
						loggingAction.logEvent(TransactionType.CREATE_PHYSICAL_THERAPY_OV, loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")), Long.toString(ptBean.getPhysicalTherapyVisitID()));
					}
					if ("Orthopedic".equals(userBean.getSpecialty()) && null != soAction
							&& !soAppointmentDate.isEmpty()) {
						SurgicalOrthopedicVisitBean soBean = soAction.orderVisit(orthopedicID, soAppointmentDate, bean.getOrthopedicVisitID());
						loggingAction.logEvent(TransactionType.CREATE_SURGICAL_OPHTHALMOLOGY_OV, loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")), Long.toString(soBean.getSurgicalOrthopedicVisitID()));
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
	<form id="mainForm" enctype="multipart/form-data"
		action="addOrthopedicVisit.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<table>
			<tr class="subHeader">
				<td>Orthopedic Office Visit Date::</td>
				<td><input onchange="singleDate();" name="orthopedicVisitDate"
					id="orthopedicVisitDate" size="10" readonly> <input type="button"
					value="Select Date" onclick="displayDatePicker('orthopedicVisitDate');"></td>
			</tr>
		</table>
		<br> <br>
		<h4>Limb or Joint</h4>
		<input type="text" name="limb&joint"> <br> <br>
		<table class="fTable mainTable">
			<tr>
				<th colspan="3">X-Ray Image</th>
			</tr>
			<tr>
				<td><input name="XRAYImage" type="file" /></td>
			</tr>
		</table>
		<br> <br>
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
		<textarea type="text" name="MRIReport" rows="5" cols="60"></textarea>
		<br> <br>
		<h4>Anterior cruciate ligament injury</h4>
		<select name="acli">
			<option selected="selected" value="-1">Select</option>
			<option value="1">True</option>
			<option value="0">False</option>
		</select> <br> <br>
		<h4>Meniscus tear</h4>
		<select name="mt">
			<option selected value="-1">Select</option>
			<option value="1">True</option>
			<option value="0">False</option>
		</select> <br> <br>
		<h4>Rheumatoid arthritis of hand</h4>
		<select name="raoh">
			<option selected value="-1">Select</option>
			<option value="1">True</option>
			<option value="0">False</option>
		</select> <br> <br>
		<h4>Chondromalacia</h4>
		<select name="c">
			<option selected value="-1">Select</option>
			<option value="1">True</option>
			<option value="0">False</option>
		</select> <br> <br>
		<h4>Congenital pes cavus</h4>
		<select name="cpc">
			<option selected value="-1">Select</option>
			<option value="1">True</option>
			<option value="0">False</option>
		</select> <br> <br>
		<h4>Whiplash injury</h4>
		<select name="wi">
			<option selected value="-1">Select</option>
			<option value="1">True</option>
			<option value="0">False</option>
		</select> <br>
		
		<% 
		
		if("Orthopedic".equals(userBean.getSpecialty() ) ) {
		
		%>
			<h4>Order Physical Therapy Appointment</h4>
			<table>
			<tr class="subHeader">
				<td>Start Date:</td>
				<td><input onchange="singleDate();" name="ptAppointmentDate"
					id="ptAppointmentDate" size="10" readonly> <input
					type="button" value="Select Date"
					onclick="displayDatePicker('ptAppointmentDate');"></td>
			</tr>
			</table>
			<p>
			Enter the MID for a physical therapist below, and a Physical therapy
			visit will be created <br> to be handled by that physical
			therapist for the currently selected patient.
			</p>
			<input type="text" name="physicalTherapistID" /> <br> <br>
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
			value="Add Orthopedic Visit">
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