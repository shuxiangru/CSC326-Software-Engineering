<%@taglib uri="/WEB-INF/tags.tld" prefix="itrust"%>
<%@page errorPage="/auth/exceptionHandler.jsp"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="edu.ncsu.csc.itrust.enums.FlagValue"%>
<%@page import="edu.ncsu.csc.itrust.exception.FormValidationException"%>
<%@page import="edu.ncsu.csc.itrust.action.PhysicalTherapyVisitAction"%>
<%@page import="edu.ncsu.csc.itrust.beans.PhysicalTherapyVisitBean"%>
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

	PhysicalTherapyVisitAction action = new PhysicalTherapyVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	PhysicalTherapyVisitBean bean = new PhysicalTherapyVisitBean();
	bean = action.getVisit(Long.parseLong((String) session.getAttribute("PhysicalTherapyVisitID")));
	pageTitle = "iTrust - " + (bean.getAddedVisit() ? "Edit" : "Document") + " Physical Therapy Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	PersonnelDAO personnelDAO = prodDAO.getPersonnelDAO();
	PersonnelBean userBean = personnelDAO.getPersonnel(loggedInMID);
	String error = "";
	boolean hasErrors = false;
	if (userBean.getSpecialty().equals("Physical Therapist")) {

		String startDate = request.getParameter("ptAppointmentDate");
		if (isMultipart || formIsFilled) {
			FileItemFactory factory = new DefaultFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			boolean[] exercises = new boolean[10];
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if ("ptAppointmentDate".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setPhysicalTherapyVisitDate(item.getString());
					} else {
						error += "<p class=\"iTrustError\">Date field is required.</p>";
						hasErrors = true;
					}
				} else if ("HouseWorkScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setHouseWorkScore(Short.parseShort(item.getString()));
					}
				} else if ("BathScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setBathScore(Short.parseShort(item.getString()));
					}
				} else if ("WalkingRoomScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setWalkingRoomScore(Short.parseShort(item.getString()));
					}
				} else if ("SquatScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setSquatScore(Short.parseShort(item.getString()));
					}
				} else if ("LiftScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setLiftScore(Short.parseShort(item.getString()));
					}
				} else if ("WalkingBlockScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setWalkingBlockScore(Short.parseShort(item.getString()));
					}
				} else if ("StairsScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setStairsScore(Short.parseShort(item.getString()));
					}
				} else if ("StandingScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setStandingScore(Short.parseShort(item.getString()));
					}
				} else if ("JumpingScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setJumpingScore(Short.parseShort(item.getString()));
					}
				} else if ("RunningScore".equals(item.getFieldName())) {
					if (!"".equals(item.getString())) {
						bean.setRunningScore(Short.parseShort(item.getString()));
					}
				} else if ("QuadSetExercise".equals(item.getFieldName())) {
					exercises[0]=true;
				} else if ("HeelSlideExercise".equals(item.getFieldName())) {
					exercises[1]=true;
				} else if ("CalfTowelExercise".equals(item.getFieldName())) {
					exercises[2]=true;
				} else if ("StraightLegExercise".equals(item.getFieldName())) {
					exercises[3]=true;
				} else if ("TerminalKneeExercise".equals(item.getFieldName())) {
					exercises[4]=true;
				} else if ("GastrocStretchExcercise".equals(item.getFieldName())) {
					exercises[5]=true;
				} else if ("WallSlideExercise".equals(item.getFieldName())) {
					exercises[6]=true;
				} else if ("ProprioceptionExercise".equals(item.getFieldName())) {
					exercises[7]=true;
				} else if ("HipAbductionExercise".equals(item.getFieldName())) {
					exercises[8]=true;
				} else if ("SingleLegExercise".equals(item.getFieldName())) {
					exercises[9]=true;
				}
			}
			if (!hasErrors) {
				try {
					bean.setExercises(exercises);
					if(!bean.getAddedVisit())
						bean.setAddedVisit(true);
					action.editVisit(bean);
					loggingAction.logEvent(TransactionType.EDIT_PHYSICAL_THERAPY_OV, loggedInMID.longValue(), Long.parseLong((String) session.getAttribute("pid")), Long.toString(bean.getPhysicalTherapyVisitID()));
					response.sendRedirect("selectPhysicalTherapyVisit.jsp");
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
		action="editPhysicalTherapyVisit.jsp" method="post">
		<input type="hidden" name="formIsFilled" value="true">
		<%
		if (bean.getAddedVisit()) {
	%>

		<table>
			<tr class="subHeader">
				<td>Appointment Date:</td>
				<td><input onchange="singleDate();" name="ptAppointmentDate"
					id="ptAppointmentDate"
					value="<%=StringEscapeUtils.escapeHtml(bean.getPhysicalTherapyVisitDateString())%>"
					size="10" readonly /> <input type="button" value="Select Date"
					onclick="displayDatePicker('ptAppointmentDate');" /></td>
			</tr>
		</table>
		<%
		} else {
	%>
		<h4>Appointment Date</h4>
		<%=bean.getPhysicalTherapyVisitDateString()%>
		<%
		}
	%>
		<h4>Wellness Survey Results</h4>
		<table>
			<tr>
				<td><b>Do you have a any difficulty</b></td>
				<td><b>Unable</b></td>
				<td><b>Very difficult</b></td>
				<td><b>Moderate</b></td>
				<td><b>Little bit</b></td>
				<td><b>No difficulty</b></td>
			</tr>
			<tr>
				<td>Performing normal house work</td>
				<td><input type="radio" name="HouseWorkScore" value="0"
					<%if (0 == bean.getHouseWorkScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="HouseWorkScore" value="1"
					<%if (1 == bean.getHouseWorkScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="HouseWorkScore" value="2"
					<%if (2 == bean.getHouseWorkScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="HouseWorkScore" value="3"
					<%if (3 == bean.getHouseWorkScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="HouseWorkScore" value="4"
					<%if (4 == bean.getHouseWorkScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Getting into or out of bath</td>
				<td><input type="radio" name="BathScore" value="0"
					<%if (0 == bean.getBathScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="BathScore" value="1"
					<%if (1 == bean.getBathScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="BathScore" value="2"
					<%if (2 == bean.getBathScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="BathScore" value="3"
					<%if (3 == bean.getBathScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="BathScore" value="4"
					<%if (4 == bean.getBathScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Walking between rooms</td>
				<td><input type="radio" name="WalkingRoomScore" value="0"
					<%if (0 == bean.getWalkingRoomScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingRoomScore" value="1"
					<%if (1 == bean.getWalkingRoomScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingRoomScore" value="2"
					<%if (2 == bean.getWalkingRoomScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingRoomScore" value="3"
					<%if (3 == bean.getWalkingRoomScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingRoomScore" value="4"
					<%if (4 == bean.getWalkingRoomScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Squatting?</td>
				<td><input type="radio" name="SquatScore" value="0"
					<%if (0 == bean.getSquatScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="SquatScore" value="1"
					<%if (1 == bean.getSquatScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="SquatScore" value="2"
					<%if (2 == bean.getSquatScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="SquatScore" value="3"
					<%if (3 == bean.getSquatScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="SquatScore" value="4"
					<%if (4 == bean.getSquatScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Lift an object from floor</td>
				<td><input type="radio" name="LiftScore" value="0"
					<%if (0 == bean.getLiftScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="LiftScore" value="1"
					<%if (1 == bean.getLiftScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="LiftScore" value="2"
					<%if (2 == bean.getLiftScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="LiftScore" value="3"
					<%if (3 == bean.getLiftScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="LiftScore" value="4"
					<%if (4 == bean.getLiftScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Walking two blocks?</td>
				<td><input type="radio" name="WalkingBlockScore" value="0"
					<%if (0 == bean.getWalkingBlockScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingBlockScore" value="1"
					<%if (1 == bean.getWalkingBlockScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingBlockScore" value="2"
					<%if (2 == bean.getWalkingBlockScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingBlockScore" value="3"
					<%if (3 == bean.getWalkingBlockScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="WalkingBlockScore" value="4"
					<%if (4 == bean.getWalkingBlockScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Getting up or down stairs</td>
				<td><input type="radio" name="StairsScore" value="0"
					<%if (0 == bean.getStairsScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StairsScore" value="1"
					<%if (1 == bean.getStairsScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StairsScore" value="2"
					<%if (2 == bean.getStairsScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StairsScore" value="3"
					<%if (3 == bean.getStairsScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StairsScore" value="4"
					<%if (4 == bean.getStairsScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Standing for 1 hour</td>
				<td><input type="radio" name="StandingScore" value="0"
					<%if (0 == bean.getStandingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StandingScore" value="1"
					<%if (1 == bean.getStandingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StandingScore" value="2"
					<%if (2 == bean.getStandingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StandingScore" value="3"
					<%if (3 == bean.getStandingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="StandingScore" value="4"
					<%if (4 == bean.getStandingScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Jumping</td>
				<td><input type="radio" name="JumpingScore" value="0"
					<%if (0 == bean.getJumpingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="JumpingScore" value="1"
					<%if (1 == bean.getJumpingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="JumpingScore" value="2"
					<%if (2 == bean.getJumpingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="JumpingScore" value="3"
					<%if (3 == bean.getJumpingScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="JumpingScore" value="4"
					<%if (4 == bean.getJumpingScore()) {%> checked="checked" <%}%>></td>
			</tr>
			<tr>
				<td>Running on uneven ground</td>
				<td><input type="radio" name="RunningScore" value="0"
					<%if (0 == bean.getRunningScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="RunningScore" value="1"
					<%if (1 == bean.getRunningScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="RunningScore" value="2"
					<%if (2 == bean.getRunningScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="RunningScore" value="3"
					<%if (3 == bean.getRunningScore()) {%> checked="checked" <%}%>></td>
				<td><input type="radio" name="RunningScore" value="4"
					<%if (4 == bean.getRunningScore()) {%> checked="checked" <%}%>></td>
			</tr>
		</table>
		<h4>Exercises</h4>
		<table>
			<tr>
				<td><input type="checkbox" name="QuadSetExercise" value="1"
					<%if (bean.getQuadSetExercise()) {%> checked="checked" <%}%>>
					Quad Set: Slight Flexion <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="HeelSlideExercise" value="1"
					<%if (bean.getHeelSlideExercise()) {%> checked="checked" <%}%>>
					Heel Slide (Supine) <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="CalfTowelExercise" value="1"
					<%if (bean.getCalfTowelExercise()) {%> checked="checked" <%}%>>
					Calf Towel Stretch <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="StraightLegExercise" value="1"
					<%if (bean.getStraightLegExercise()) {%> checked="checked" <%}%>>
					Straight Leg Raise <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="TerminalKneeExercise"
					value="1" <%if (bean.getTerminalKneeExercise()) {%>
					checked="checked" <%}%>> Terminal Knee Extension <br>
				</td>
			</tr>
			<tr>
				<td><input type="checkbox" name="GastrocStretchExcercise"
					value="1" <%if (bean.getGastrocStretchExcercise()) {%>
					checked="checked" <%}%>> Gastroc Stretch <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="WallSlideExercise" value="1"
					<%if (bean.getWallSlideExercise()) {%> checked="checked" <%}%>>
					Wall Slide <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="ProprioceptionExercise"
					value="1" <%if (bean.getProprioceptionExercise()) {%> checked <%}%>>
					Proprioception: Step Over <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="HipAbductionExercise"
					value="1" <%if (bean.getHipAbductionExercise()) {%> checked <%}%>>
					Hip Abduction <br></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="SingleLegExercise" value="1"
					<%if (bean.getSingleLegExercise()) {%> checked <%}%>>
					Single-Leg Step Up <br></td>
			</tr>
		</table>
		<br> <br> <input type="submit"
			style="font-size: 16pt; font-weight: bold;"
			value="Update Physical Therapy Visit">
	</form>
</body>
<%
	} else {
		//Person is not of the correct specialization
%>
<h4>Documenting Physical Therapy visits can only be done by
	Orthopedic and Physical Therapy specialists. Click below to create a
	regular office visit instead.</h4>

<form id="officeVisitForward"
	action="../hcp-uap/documentOfficeVisit.jsp" method="POST">
	<input type="submit" value="Document Regular Office Visit" />
</form>
<%
	}
%>

<%@include file="/footer.jsp"%>