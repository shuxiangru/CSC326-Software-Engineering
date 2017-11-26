# UC91
  For this use case, we implemented a section in iTrust that allows patients to view thieor and/or their dependent(s)'s
  orthopedic/physical therapy visits. We will refer to these as SOV for specialized office visits. The patient can only
  view these SOV's, in the same way that they can be viewed by health care providers in previous use cases.

# Front End
  For the front end, we decided to create two .jsp files, viewOrthopedicVisit.jsp and viewPhysicalTherapyVisit.jsp. These jsps are directed
  to from the patient's menu. Once on either of these pages, the logged in patient can view their SOVs of the appropriate type (the viewOrthopedicVisit page
  allows the patient to only view orthopedic visits and vice versa) as well as their dependent(s)'s visits of the appropriate type.

# Back End
  We had to add a SQL query into PatientDAO.java in order to get
  dependents of patients. Otherwise, we utilized pre-existing patient handling code and preexisting sql data.

# SQL
  We also did not have to create an SQL tables, as we simply used existing orthopedic office visit and physical therapy office visit tables. 
  **In this use case we consolidated all logging tables into the transactionlog table**

# Testing
  We tested six different cases:
  * The patient views all of either type of SOV
  * The patient views one of their dependent's SOV
  * The patient selects their own SOV to view
  * The patient is able to view but not edit the correct fields on the SOV
  
  * The patient having no office visits
  * The patient's ability to access appropriate records or not

To thoroughly black box test, one must go through each of these. There must be atleast one registered patient A in the system, one registered patient B who is 
a dependent of A, and two registered patients C and D who are both unrelated to either A or B. A and be must have atleast one SOV each. C must have alteast one SOV.
D must not have any SOV's.

To thoroughly white box test, no external data is needed. The data is provided by the testing suite.
