# UC88
  For this use case, we implemented a section in iTrust for managing orthopedic office visits (OOV). For
this modification, a Health Care Provider (HCP) must be able to attempt to view, edit, or document 
an OOV for a selected patient. Any HCP must be able to view an OOV. However, only HCP with 
specialization in either physical therapy or orthopedics may document and edit an OOV. These HCP 
will be referred to as SHCP for specialized health care providers. Non-physical therapy/non-orthopedic 
HCPs will be prompted to schedule a regular office visit if they attempt to document or edit an OOV. 
  When documenting or editting an OOV, a SHCP may submit an image of an X-Ray and/or an image of an MRI.
All other entered fields are either text or selected fields. The SHCP may enter information regarding a 
patient's injury and then identify one or more diagnoses for the selected patient. After the SHCP has submitted
either an edit or a documenting of an OOV, they are directed to an overview of all OOV for the selected 
patient.

# Front End
  For the front end, we decided to create four .jsp files. When a HCP proceeds to the OOV selection page,
all HCPs go to the selectOrthopedicVisit.jsp. This way, all HCPs can have their specializations verified before
attempting to modify OOVs. From there, documenting, editting, and viewing OOVs each have their own .jsp files.
This allows for unqiue handling and display options that are unique to each situation, such as displaying the 
contents of an image. When an OOV is documented, a page is loaded with empty fields. Viewing and editting 
OOVs pre-populate the fields with information from the SQL database.

# Back End
  We decided to control all back end interactions with the OOVs through one action class, 
OrthopedicVisitAction.java. This action class allows for adding, editting, and viewing OOVs. This class interacts
directly with our data access object (DAO) OrthopedicVisitDAO.java. This DAO provides the bridge between our 
back end and the SQL tables that we create in createTables.sql. The DAO also allows for logging of pertenant 
information. 

# SQL
  We create two SQL tables: orthopedicvisits (for the main functionality) and orthopedicvisitslog (for logging).
The main table maintains columns for each fields that is able to be entered by a SHCP, as well as the ability
to store images using SQLs Binary Large Object (BLOB).

# Testing
  We tested five different cases:
  * Adding a visit succesfully
  * Adding an invalid visit (should result in error)
  * Editing a visit succesfully
  * Editing a visit to make it invalid (should result in error)
  * Viewing a visit
  * Attempting to Add or Edit with a non-specialized HCP

To thoroughly black box test, one must go through each of these. The first four must be tested by
logging in as a specialised HCP and going through the add and edit paths. The fifth can be tested with any hcp,
but the data being viewed must be added by a specialised HCP. The last must be tested with a non-specialised
HCP, who will not be able to access either the add or edit pages because the buttons are removed.
For automated testing, no test input is necessary for white box testing.
Test input is provided as part of the testing suite. For back box testing, however, atleast one registered patient
must exist, and atleast 1 SHCP and one non-specialized HCP must exist in the system.
