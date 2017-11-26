# UC92
For this use case, we implemented a section in iTrust that allows patients to request either an Orthopedic Appointment or a Physical Therapy Appointment. Health Care 
Providers (HCPs) are then able to accept or reject the request. A physical therapist can only accept a request if a doctor has ordered 
a request for that patient. The patient can see the status of the request. 

# Front End
For the front end for the patient, we used a modified version of the already existing requestAppointment.jsp. The functionality was what we needed except for the 
ability to filter based off of appointment type. For HCPs, we used preexisting appoint jsps for the same reason. The physical therapy jsp has code to verify that
a doctor has ordered the visit or not.

# Back End
We had to alter the existing surgical orthopedic and physical therapy DAOs to include the orthopedic office visit associated with them. We also had to add functions to
apptrequestDAO to get appointment requests for a patient, added a function to orthopedicDAO and physicaltherapyDAO to get all visits for a given patient id and hcp id,  
added two new appointment types to the table, added a function to ViewVisitedHCPsAction to include orthopedic/pt visits, and added a function to apptrequestDAO to get 
appointment requests for a patient.

# SQL
Other than adding two new appointment types to the existing appointment table, our code mostly dealt with managing current table entries.

# Testing
We tested seven different cases:

*A patient requesting an appointment with an orthopedic or a physical therapist (functionality is the same)

*A physical therapist or orthopedic accepting an appointment
*A physical therapist or orthopedic denying an appointment
*A physical therapist or orthopedic's ability to view all appointment requests for them
*A patient's abiity to see all requests for appointments they have created

*A non specialized HCP's ability to view appointment requests
*A physical therapist's ability to accepting an appointment without an order from a doctor.

To thoroughly black box test, one must go through each of these. There must be atleast one registered patient, 
one registered orthopedic, one registered physical therapist, and one registered non-specialized HCP.

To thoroughly white box test, no external data is needed. The data is provided by the testing suite.
