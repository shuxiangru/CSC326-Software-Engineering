# UC90
  For this use case, we implemented a section in iTrust for surgical orthopedic office visits (SOOV). For
this modification, a Health Care Provider (HCP) must be able to attempt to view or edit 
a SOOV for a selected patient. Any HCP must be able to view an SOOV. However, only HCP with 
specialization in orthopedics may edit a SOOV. These HCP will be referred to as OHCP 
for orthopedics health care providers. Orthopedic health care providers will be referred to as
OHCPs. OHCPs can order a SOOV while creating an orthopedic office visit, in which they must specify a
OHCP to recieve this SOOV. OHCPs cannot add SOOVs without one being ordered. 
Instead, they can only add or edit SOOVs orderd by OHCPs. HCPs will be prompted 
to schedule a regular office visit if they attempt to add or edit an SOOV. 
  When ordering a SOOV, the OHCP must provide the name of the OHCP to handle the SOOV. As well, the date of the
orthopedic office visit will be recorded from the form, as the date is required when filling out the orthopedic offic
visit. The name of the patient is also added this way. When adding or editting an SOOV, a OHCP must record what
surgeries are required for the patient, as well as any surgery notes.

# Front End
  For the front end, we decided to create three .jsp files. When a HCP proceeds to the SOOV selection page,
all HCPs go to the selectSurgicalOrthopedicVisit.jsp. This way, all HCPs can have their specializations verified before
attempting to modify SOOVs. From there, editting and viewing SOOVs each have their own .jsp files. Adding a SOOV is
actually just adding non-null values to required fields. Therefore, adding a SOOV is identical to editting SOOVs. 
Conversely, we differentiate the two by switching a boolean once the OHCP formally adds the visit such that it is
then editable without being addable. When an SOOV is added, a page is loaded with the date of the orthopedic
office visit, the OHCP to attend to it, and the patient's information. Viewing and editting SOOVs 
pre-populate the fields with information from the SQL database.

# Back End
  We decided to control all back end interactions with the SOOVs through one action class, 
SurgicalOrthopedicVisitAction.java. This action class allows for "adding", editting, and viewing SOOVs. This class interacts
directly with our data access object (DAO) SurgicalOrthopedicVisitDAO.java. This DAO provides the bridge between our 
back end and the SQL tables that we create in createTables.sql. The DAO also allows for logging of pertenant 
information. As well, we have a loader for loading date from ResultSets from SQL queries. Lastly, we have the
SurgicalOrthopedicVisitBean which holds all information regarding a SOOV.

# SQL
  We create two SQL tables: surgicalorthopedicvisits (for the main functionality) and surgicalorthopedicvisitslog (for logging).
The main table maintains columns for each fields that is able to be entered by a OHCP.

# Testing
  We tested nine different cases:
  * Ordering a visit succesfully
  * Ordering an invalid visit (should result in error)
    * ID is not an HCP
    * ID is an HCP but not a OHCP
    * ID is provided but no date
    * Date is provided but no ID
  * Attempting to Order with a PT
  * Documenting a visit
  * Editing a visit
  * Viewing a visit

To thoroughly black box test, one must go through each of these. Aside from the sixth test, the others must all be tested by first logging in as an orthopedic specialist to order the visit, or at least attempt to order it in the case of the invalid visits. In the sixth case, the user instead logs in as a PT and verifies they cannot see the options to order a visit. For the last three tests, the user must then navigate to the surgical orthopedic page to perform the actions for the test. Because there is no way to clear the date via the webpage, there cannot be selenium test for invalid documenting or editing. 
For automated testing, no test input is necessary for white box testing.
Test input is provided as part of the testing suite. For back box testing, however, atleast one registered patient
must exist, and atleast one OHCP, and one non-specialized HCP must exist in the system.
