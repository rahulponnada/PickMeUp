using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Globalization;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using System.Text;

namespace PickMeUpService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class AuthService : IAuthentication
    {
        // Student login() -- fetches password and compares it with the input password then returns true or false.
        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "login/student/{usrn}/{pwd}")]
        public string loginStudent(string usrn,string pwd)
        {
            string password = null;
            bool stats = false;
            string statusEmail = "Email Send failed";
            try
            {
                SqlConnection sqlConnection1 = new SqlConnection(ConfigurationManager.ConnectionStrings["pickdb"].ConnectionString);
                SqlCommand cmd = new SqlCommand();
                SqlDataReader reader;

                
                cmd.CommandText = "SELECT passwd FROM student where studentid ='" + usrn + "'";
                cmd.CommandType = CommandType.Text;
                cmd.Connection = sqlConnection1;

                sqlConnection1.Open();

                reader = cmd.ExecuteReader();
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        password = reader.GetString(0);
                        if (password == pwd)
                        {
                            stats = true;
                            sendNotification email = new sendNotification();
                            string[] address = {"rahul.auce@hotmail.com"};
                            string[] addresscc = { };
                            statusEmail = email.SendEmail("rahulponnada@ymail.com","6thsensE",address,addresscc,"PickupNotification","Helo",false);
                        }
                        else
                        {
                            stats = false;
                        }
                    }   
                }
                else
                {
                       Console.WriteLine("No rows found.");
                }
                reader.Close();
                sqlConnection1.Close();
                
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
         /*   return new Status()
            {
                status = stats
            };*/
            return statusEmail;
        }

        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "login/volunteer/{usrn}/{pwd}")]
        public Status loginVolunteer(string usrn, string pwd)
        {
            string password = null;
            bool stats = false;
            try
            {
                SqlConnection sqlConnection1 = new SqlConnection(ConfigurationManager.ConnectionStrings["pickdb"].ConnectionString);
                SqlCommand cmd = new SqlCommand();
                SqlDataReader reader;

                cmd.CommandText = "SELECT passwd FROM Volunteer where studentid ='" + usrn + "'";
                cmd.CommandType = CommandType.Text;
                cmd.Connection = sqlConnection1;

                sqlConnection1.Open();

                reader = cmd.ExecuteReader();
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        password = reader.GetString(0);
                        if (password == pwd)
                        {
                            stats = true;
                        }
                        else
                        {
                            stats = false;
                        }
                    }
                }
                else
                {
                    Console.WriteLine("No rows found.");
                }
                reader.Close();
                sqlConnection1.Close();

            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return new Status()
            {
                status = stats
            };
        }

        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }

        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "register/student?studentid={sid}&passwd={pwd}&firstname={fname}&lastname={lname}&email={eid}&gender={sex}&arrivaldate={adate}&arrivaltime={atime}&airlines={airline}&flight={flight}&address={address1}")]
        public int registerStudent(string sid, string pwd,string fname, string lname,string eid,string sex, string adate, string atime,string airline,string flight,string address1)
        {
            int result = 39;
            try
            { 
                SqlConnection sqlConnection1 = new SqlConnection(ConfigurationManager.ConnectionStrings["pickdb"].ConnectionString);
                SqlCommand cmd = new SqlCommand();

                //DateTime arrivalDate = Convert.ToDateTime(adate);
                result = 45;
                //string temp = adate + atime;
                //DateTime dt = DateTime.Parse(atime)
                //DateTime arrivalTime = Convert.ToDateTime(atime).ToString("HH:mm:ss");
                //arrivalTime = arrivalTime.ToString("HH:mm:ss");
                //DateTime arrivalTime = DateTime.ParseExact(atime, "HH:mm:ss",
                                        //CultureInfo.InvariantCulture);
                //DateTime arrivalT = DateTime.Parse(atime);

                //result = 46;
                //DateTime arrivaltime = Convert.ToDateTime(temp);

                cmd.CommandText = "INSERT into student (studentid,passwd,firstname,lastname,email,gender,arrivaldate,arrivaltime,airlines,flight,address1) VALUES ('" + sid + "','" + pwd + "','" + fname + "','" + lname + "','" + eid + "','" + sex + "',NULL,NULL,'" + airline + "','" + flight + "','" + address1 + "')";
                
                cmd.CommandType = CommandType.Text;
                cmd.Connection = sqlConnection1;
                sqlConnection1.Open();
                result = cmd.ExecuteNonQuery();
                sqlConnection1.Close();
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return result;
        }

        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "register/volunteer?studentid={sid}&passwd={pwd}&firstname={fname}&lastname={lname}&email={eid}&gender={sex}&phone={ph}&address={address1}&available={days}")]
        public int registerVolunteer(string sid, string pwd, string fname, string lname, string eid, string sex, string ph, string address1, string days)
        {
            int result = 35;
            try
            {
                SqlConnection sqlConnection1 = new SqlConnection(ConfigurationManager.ConnectionStrings["pickdb"].ConnectionString);
                SqlCommand cmd = new SqlCommand();
                result = 34;
                cmd.CommandText = "INSERT into Volunteer(studentid,passwd,firstname,lastname,email,gender,phone,address,available) VALUES ('" + sid + "','" + pwd + "','" + fname + "','" + lname + "','" + eid + "','" + sex + "','" + ph + "','" + address1 + "','" + days + "')";
                
                cmd.CommandType = CommandType.Text;
                cmd.Connection = sqlConnection1;

                sqlConnection1.Open();
                result = cmd.ExecuteNonQuery();
                sqlConnection1.Close();
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return result;
        }

        public class Status
        {
            public Status()
            {

            }
            public bool status { get; set; }
        }
               
    }
}
