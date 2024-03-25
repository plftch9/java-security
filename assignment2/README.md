"sdevadmin" has admin privileges assigned (privileges don't actually do anything in this application) and therefore requires 2 step verification
when performing 2 step verification you will be prompted at the command terminal, not the GUI, for an email address and then for the code sent
the code will be sent from "sdevtesting@gmail.com" and the subject line of the email will be "Testing Subject"
upon successfully completing 2 step verification the GUI will notify the user of successful login

"user" has base privileges and does not require 2 step verification

in lieu of a database engine or some other credential storage, the login credentials used for testing purposes are stored in the
"Authenticate.java" class as a two dimensional array. The passwords are not in clear text in the application, but they are noted below
sdevadmin: 425!pass
user: password
