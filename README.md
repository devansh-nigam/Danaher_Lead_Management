# Danaher Lead Management Solution
## _Transfer Your Business Leads Hassle-Free_ 📈
Bulit Under  "CorpComp Challenge" for TechFest 20-21 

![Preview](/preview.png)

The app is deployed on Google Firebase. A separate business email id : danaherlead@gmail.com has been made to manage the server operations at Firebase. It has been made, keeping in mind that this project will be transferred to Danaher Corporation.

### ✔ Description  of the problem
All big business corporations must have faced this problem where they don't have a centralized system of forwarding leads withing their business associates. This app is specifically made for the well-known Danaher Global Corporation keeping in mind the gravity of this problem.

### ✔  And Here's the solution for the above stated problem
**Danaher Lead** enables transfers of lead within the system very quickly, with just few touch on your moblie screen and you have submitted a valuable asset to your organisation. Though, some features are yet to be implemented, I think that this is a very amazing start for the app.

### ✔  So here's how it actually works 📱:
- The Employee creates his/her account on the **Moblie App** with all the required details (email and company included)
- Then he adds the leads manually ( with all the necessary info )
- If he has any assigned leads, it appears in real-time on the dashboard
- All the leads, when submitted have default status of Open
- A lead with Open status can be marked either Validated or Rejected the first time
- Only Validated Leads can be marked as closed afterwards
- Timestamp of each status change is captured and updated henceforth
- A user can filter through the assigned/submitted leads by the use of check boxes

### ✔  Tech Stack I have used ⚙

#### Front End ( Danaher Lead App ):
- XML 
- Firebase Firestore Recycler

#### Back End  ( Hosted on Google Firebase ):
- Firebase Firestore
- Firebase Authentication

### Native Mobile App 
- Kotlin

### ✔ Guided Tour Of App 

#### Register, Login & Forgot Password Activities

Login             |  Register | Forgot Password
:-------------------------:|:-------------------------:|:-------------------------:
![Login](/app-screenshots/Login.png)  |  ![Register](/app-screenshots/Register.png) | ![ForgotPassword](/app-screenshots/ForgotPassword.png)

- Care has been taken so that no one can bypass the authenication easily. 
- Also, Users's Segment and Exact company is taken as a input by the help of a spinner instead of a text field to increase security.

Segment Selection             |  Spinner In Action | Company Selection
:-------------------------:|:-------------------------:|:-------------------------:
![Segment](/app-screenshots/segment.png)  |  ![Register](/app-screenshots/segmentss.gif) | ![ForgotPassword](/app-screenshots/Screenshot_1615398241.png)


#### Dashboard, Assigned & Submitted Lead Activities

Dashboard             |  Leads Display | Lead Review
:-------------------------:|:-------------------------:|:-------------------------:
![Dashboard](/app-screenshots/Dashboard.png)  |  ![Lead Display](/app-screenshots/Leads.png) | ![Lead Review](/app-screenshots/LeadReview.png)

Lead Details            |  Lead Validation | Lead Closure
:-------------------------:|:-------------------------:|:-------------------------:
![Lead Details](/app-screenshots/LeadDetails.png)  |  ![Lead Validation](/app-screenshots/Validation.png) | ![Lead Closure](/app-screenshots/Closure.png)

#### Filter Leads On The Basis of Status



The apk for the android application is provided in the /app/release/ section of the github repository. 
