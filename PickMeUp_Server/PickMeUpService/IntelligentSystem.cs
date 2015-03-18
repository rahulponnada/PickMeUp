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
        public IntelligentSystem()
            {

            }

        public int assignVolunteer(string sid)
        {
            DateTime DateofComing = new DateTime(2000, 2, 2, 2, 2, 2);
            string dayofweek = "";
            bool status = false;
            byte arrivingTimeSlot = 0;   // Ariving on monday at 10:130:55 : 00001000 so its 2^3 = 8 
            byte daystatus = 0;
            string queryday = "";
            int x = 99;
            int result = 35;
            string studentMail = "";
            string studentFname= "";
            string studentLname = ""; 
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
                daystatus = reader.GetByte(reader.GetOrdinal(queryday));
                assignedVolunteerid = reader.GetString(0);

               studentMail = reader.GetString(4);
               studentFname = reader.GetString(2);
               studentLname = reader.GetString(3);

                if (daystatus != 0) { status = true; }
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


                    sendNotification email = new sendNotification();
                    string[] address = { studentMail };
                    string[] addresscc = { };
                    statusEmail = email.SendEmail("anvesh525@gmail.com", "tummalaanvesh", address, addresscc, "PickupNotification", "Hi, You have been assigned for picking " + studentFname + " " + studentLname, false);
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