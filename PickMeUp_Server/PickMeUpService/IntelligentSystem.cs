using System;
using System.Collections.Generic;
using System.Configuration;
using System.Globalization;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using System.Text;
namespace PickMeUpService
{
    public class IntelligentSystem
    {
        string assignedVolunteerid = "0";
        string statusEmail = "Email Send failed";
       public  string adminEmail = "anvesh525@gmail.com";
        public IntelligentSystem()
            {
                
            }

        public int assignVolunteer(string sid)
        {
            DateTime DateofComing = new DateTime(2000, 2, 2, 2, 2, 2);
            string dayofweek = "";
            byte arrivingTimeSlot = 0;   // Ariving on monday at 10:130:55 : 00001000 so its 2^3 = 8 
            byte daystatus = 0;
            string queryday = "";
            int x = 99;
            int result = 35;
            string VolunteerMail = "";
            string StudentMail = "";
            string studentFname= "";
            string studentLname = "";
            string volunteerFname = "";
            string volunteerLname = "";
            try
            {
                SqlConnection sqlConnection1 = new SqlConnection(ConfigurationManager.ConnectionStrings["pickdb"].ConnectionString);
                SqlCommand cmd = new SqlCommand();
                SqlDataReader reader;


                cmd.CommandText = "SELECT * FROM student where studentid ='" + sid + "'";
                cmd.CommandType = CommandType.Text;
                cmd.Connection = sqlConnection1;
                sqlConnection1.Open();
                reader = cmd.ExecuteReader();
                reader.Read();
                studentFname = reader.GetString(2);
                studentLname = reader.GetString(3);
                StudentMail = reader.GetString(4);
                DateofComing = reader.GetDateTime(reader.GetOrdinal("arrivaltime"));
                reader.Close();
                sqlConnection1.Close();
                dayofweek = dayofweek + DateofComing.DayOfWeek.ToString();

                x = DateofComing.Hour / 3;
                arrivingTimeSlot = (byte)(Math.Pow(2, x));
                switch (dayofweek)
                {
                    case "Monday": queryday = queryday + "MonAvailability";
                        break;
                    case "Tuesday": queryday = queryday + "TueAvailability";
                        break;
                    case "Wednesday": queryday = queryday + "WedAvailability";
                        break;
                    case "Thursday": queryday = queryday + "ThuAvailability";
                        break;
                    case "Friday": queryday = queryday + "FriAvailability";
                        break;
                    case "Saturday": queryday = queryday + "SatAvailability";
                        break;
                    case "Sunday": queryday = queryday + "SunAvailability";
                        break;
                    default: queryday = null; break;
                }

                cmd.CommandText = "SELECT * FROM Volunteer where (" + queryday + " & " + arrivingTimeSlot.ToString() + " != 0 ) ORDER BY noOfStudents ASC";
                cmd.CommandType = CommandType.Text;
                cmd.Connection = sqlConnection1;
                sqlConnection1.Open();
                reader = cmd.ExecuteReader();
                reader.Read();
                if (reader.HasRows == false)
                {
                    return 0;
                }

                else
                {
                    daystatus = reader.GetByte(reader.GetOrdinal(queryday));
                    assignedVolunteerid = reader.GetString(0);

                    VolunteerMail = reader.GetString(4);
                    volunteerFname = reader.GetString(2);
                    volunteerLname = reader.GetString(3);

                    reader.Close();
                    sqlConnection1.Close();
                    if (daystatus != 0)
                    {
                        result = 34;
                        cmd.CommandText = " UPDATE student SET Volunteerid = " + assignedVolunteerid + " WHERE studentid = " + sid;

                        cmd.CommandType = CommandType.Text;
                        cmd.Connection = sqlConnection1;
                        sqlConnection1.Open();
                        result = cmd.ExecuteNonQuery();
                        sqlConnection1.Close();

                        cmd.CommandText = " UPDATE Volunteer SET noOfStudents = noOfStudents + 1 WHERE studentid = " + assignedVolunteerid;
                        cmd.CommandType = CommandType.Text;
                        cmd.Connection = sqlConnection1;
                        sqlConnection1.Open();
                        result = cmd.ExecuteNonQuery();
                        sqlConnection1.Close();


                        sendNotification email1 = new sendNotification();
                        string[] address = { VolunteerMail };
                        string[] addresscc = { };
                        statusEmail = email1.SendEmail("pickmeupUMKC@gmail.com", "pickup123", address, addresscc, "PickupNotification", "Hi, You have been assigned for picking " + studentFname + " " + studentLname, false);

                        sendNotification email2 = new sendNotification();
                        string[] adminAddress = { adminEmail };
                        statusEmail = email2.SendEmail("pickmeupUMKC@gmail.com", "pickup123", adminAddress, addresscc, "PickupNotification", "Hi, Volunteer " + volunteerFname + " " + volunteerLname + " have been assigned for picking " + studentFname + " " + studentLname, false);

                        sendNotification email3 = new sendNotification();
                        string[] studentAddress = { StudentMail };
                        statusEmail = email2.SendEmail("pickmeupUMKC@gmail.com", "pickup123", studentAddress, addresscc, "PickupNotification", "Hi, Volunteer " + volunteerFname + " " + volunteerLname + " have been assigned for picking you. ", false);
                   
                    }

                }
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }

            return result;
        }
    }
}