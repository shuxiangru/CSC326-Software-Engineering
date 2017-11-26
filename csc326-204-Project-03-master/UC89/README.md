# UC89
  For this use case, we implemented a section in iTrust for physical therapy office visits (PTOV). For
this modification, a Health Care Provider (HCP) must be able to attempt to view or edit 
a PTOV for a selected patient. Any HCP must be able to view an PTOV. However, only HCP with 
specialization in physical therapy may edit a PTOV. These HCP will be referred to as PTHCP 
for physical therapy health care providers. Orthopedic health care providers will be referred to as
OHCPs. OHCPs can order a PTOV while creating a an orthopedic office visit, in which they must specify a
PTHCP to recieve this PTOV. PTHCPs cannot order PTOVs. Instead, they can only add or edit PTOVs orderd by OHCPs.
HCPs will be prompted to schedule a regular office visit if they attempt to add or edit an PTOV. 
  When ordering a PTOV, the OHCP must provide the name of the PTHCP to handle the PTOV. As well, the date of the
orthopedic office visit will be recorded from the form, as the date is required when filling out the orthopedic offic
visit. The name of the patient is also added this way. When adding or editting an PTOV, a PTHCP must record 
the results of a wellness survey administered to the specified patient. A wellness score is then calculated
from these results, where each field is out of 10, and the total score is out of 100. 
Lastly, the PTHCP may add exercises for the patient to perform.

# Front End
  For the front end, we decided to create three .jsp files. When a HCP proceeds to the PTOV selection page,
all HCPs go to the selectPhysicalTherapyVisit.jsp. This way, all HCPs can have their specializations verified before
attempting to modify PTOVs. From there, editting and viewing PTOVs each have their own .jsp files. Adding a PTOV is
actually just adding non-null values to required fields. Therefore, adding a PTOV is identical to editting PTOVs. 
Conversely, we differentiate the two by switching a boolean once the PTHCP formally adds the visit such that it is
then editable without being addable. When an PTOV is added, a page is loaded with the date of the orthopedic
office visit, the PTHCP to attend to it, and the patient's information. Viewing and editting PTOVs 
pre-populate the fields with information from the SQL database.

# Back End
  We decided to control all back end interactions with the PTOVs through one action class, 
PhysicalTherapyVisitAction.java. This action class allows for "adding", editting, and viewing PTOVs. This class interacts
directly with our data access object (DAO) PhysicalTherapyVisitDAO.java. This DAO provides the bridge between our 
back end and the SQL tables that we create in createTables.sql. The DAO also allows for logging of pertenant 
information. As well, we have a loader for loading date from ResultSets from SQL queries. Lastly, we have the
PhysicalTherapyVisitBean which holds all information, including results from the wellness survey. We considered 
making a seperate object for the survey, but decided against it as we would basically be using that object
exclusively to hold fields about the survey for this one use case.

# SQL
  We create two SQL tables: physicaltherapyvisits (for the main functionality) and physicaltherapyvisitslog (for logging).
The main table maintains columns for each fields that is able to be entered by a PTHCP.

# Testing
  We tested nine different cases:
  * Ordering a visit succesfully
  * Ordering an invalid visit (should result in error)
    * ID is not an HCP
    * ID is an HCP but not a PT
    * ID is provided but no date
    * Date is provided but no ID
  * Attempting to Add with a PT
  * Documenting a visit
  * Editing a visit
  * Viewing a visit

To thoroughly black box test, one must go through each of these. Aside from the sixth test, the others must all be tested by first logging in as an orthopedic specialist to order the visit, or at least attempt to order it in the case of the invalid visits. In the sixth case, the user instead logs in as a PT and verifies they cannot see the options to order a visit. For the last three tests, the user must then log out and log in as a PT specialist in order to perform the actions for the test. Because there is no way to clear the date via the webpage, there cannot be selenium test for invalid documenting or editing. 
For automated testing, no test input is necessary for white box testing.
Test input is provided as part of the testing suite. For back box testing, however, atleast one registered patient
must exist, and atleast one PTHCP, one OHCP, and one non-specialized HCP must exist in the system.
