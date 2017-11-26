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
	pageTitle = "iTrust - View Physical Therapy Office Visit";
%>

<%@include file="/header.jsp"%>

<%
	PhysicalTherapyVisitAction action = new PhysicalTherapyVisitAction(prodDAO, loggedInMID.longValue(),
			(String) session.getAttribute("pid"));
	PhysicalTherapyVisitBean bean = new PhysicalTherapyVisitBean();
	bean = action.getVisit(Long.parseLong((String) session.getAttribute("PhysicalTherapyVisitID")));
	
	if (loggedInMID.toString().equals((String) session.getAttribute("pid")))
		loggingAction.logEvent(TransactionType.PATIENT_VIEW_ORTHOPEDIC_PT_OV, loggedInMID.longValue(), 0,
				Long.toString(bean.getPhysicalTherapyVisitID()));
	else
		loggingAction.logEvent(TransactionType.PATIENT_VIEW_DEPENDANT_ORTHOPEDIC_PT_OV, loggedInMID.longValue(),
				Long.parseLong((String) session.getAttribute("pid")),
				Long.toString(bean.getPhysicalTherapyVisitID()));
%>

<body>
	<h4>Appointment Date</h4>
	<%=bean.getPhysicalTherapyVisitDateString()%>
	<h4>Wellness Survey Results</h4>
	<table>
		<tr>
			<td>Performing normal house work</td>
			<td><%=bean.getHouseWorkScoreString()%></td>
		</tr>
		<tr>
			<td>Getting into or out of bath</td>
			<td><%=bean.getBathScoreString()%></td>
		</tr>
		<tr>
			<td>Walking between rooms</td>
			<td><%=bean.getWalkingRoomScoreString()%></td>
		</tr>
		<tr>
			<td>Squatting?</td>
			<td><%=bean.getSquatScoreString()%></td>
		</tr>
		<tr>
			<td>Lift an object from floor</td>
			<td><%=bean.getLiftScoreString()%></td>
		</tr>
		<tr>
			<td>Walking two blocks?</td>
			<td><%=bean.getWalkingBlockScoreString()%></td>
		</tr>
		<tr>
			<td>Getting up or down stairs</td>
			<td><%=bean.getStairsScoreString()%></td>
		</tr>
		<tr>
			<td>Standing for 1 hour</td>
			<td><%=bean.getStandingScoreString()%></td>
		</tr>
		<tr>
			<td>Jumping</td>
			<td><%=bean.getJumpingScoreString()%></td>
		</tr>
		<tr>
			<td>Running on uneven ground</td>
			<td><%=bean.getRunningScoreString()%></td>
		</tr>
	</table>
	<h4>Wellness Score</h4>
	<%=bean.getWellnessScore()%>
	<h4>Exercises</h4>
	<%
		if (bean.hasExercises()) {
	%><table>
		<%
			if (bean.getQuadSetExercise()) {
		%><tr>
			<td>Quad Set: Slight Flexion</td>
		</tr>

		<%
			}
				if (bean.getHeelSlideExercise()) {
		%><tr>
			<td>Heel Slide (Supine)</td>
		</tr>

		<%
			}
				if (bean.getCalfTowelExercise()) {
		%><tr>
			<td>Calf Towel Stretch</td>
		</tr>

		<%
			}
				if (bean.getStraightLegExercise()) {
		%><tr>
			<td>Straight Leg Raise</td>
		</tr>

		<%
			}
				if (bean.getTerminalKneeExercise()) {
		%><tr>
			<td>Terminal Knee Extension</td>
		</tr>

		<%
			}
				if (bean.getGastrocStretchExcercise()) {
		%><tr>
			<td>Gastroc Stretch</td>
		</tr>

		<%
			}
				if (bean.getWallSlideExercise()) {
		%><tr>
			<td>Wall Slide</td>
		</tr>

		<%
			}
				if (bean.getProprioceptionExercise()) {
		%><tr>
			<td>Proprioception: Step Over</td>
		</tr>

		<%
			}
				if (bean.getHipAbductionExercise()) {
		%><tr>
			<td>Hip Abduction</td>
		</tr>

		<%
			}
				if (bean.getSingleLegExercise()) {
		%><tr>
			<td>Single-Leg Step Up</td>
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
</body>

<%@include file="/footer.jsp"%>