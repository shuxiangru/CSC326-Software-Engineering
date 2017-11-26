INSERT INTO patients
(MID, 
lastName, 
firstName,
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
dateofbirth,
mothermid,
fathermid,
bloodtype,
ethnicity,
gender, 
topicalnotes)
VALUES
(1337,
'Cake', 
'Chocolate', 
'naruto69@gmail.com', 
'1234 Fake Street', 
'Suite 765', 
'Raleigh', 
'NC', 
'27606-1234', 
'919-515-3644', 
'', 
'', 
'', 
'', 
'', 
'',
'NC', 
'', 
'', 
'', 
'2278-06-10',
1,
0,
'O+',
'Cake',
'Male',
'')
 ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer, isDependent) 
			VALUES (1337, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'what is your favorite color?', 'blue', 1)
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

DELETE FROM personalhealthinformation WHERE PatientID = 1337;
INSERT INTO personalhealthinformation
(PatientID,OfficeVisitID,Height,Weight,Smoker,SmokingStatus,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID,AsOfDate,OfficeVisitDate,BMI)
VALUES ( 1337,  11, 72,   180,   0, 9,      100,          100,           40,             100,         100,          9000000000, '2007-06-07 20:33:58','2007-06-07',24.41)
on duplicate key update OfficeVisitID = OfficeVisitID;

INSERT INTO personalhealthinformation
(PatientID,OfficeVisitID,Height,Weight,Smoker,SmokingStatus,BloodPressureN,BloodPressureD,CholesterolHDL,CholesterolLDL,CholesterolTri,HCPID,AsOfDate,OfficeVisitDate,BMI)
VALUES ( 1337,  23, 72,   185,   0, 9,      107,          104,           41,             104,         101,          9000000000, '2007-06-11 14:38:12','2007-06-11',25.1)
on duplicate key update OfficeVisitID = OfficeVisitID;

INSERT INTO ovdiagnosis(ICDCode, VisitID) VALUES 
(350.0, 11),
(715.09, 11),
(250.0, 11)
 ON DUPLICATE KEY UPDATE ICDCode = VALUES(ICDCode), VisitID = VALUES(VisitID);

INSERT INTO declaredhcp(PatientID,HCPID) VALUE(1337, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;
