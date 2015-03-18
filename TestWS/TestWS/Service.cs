using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace TestWS
{
    [TestFixture]
    public class Service
    {
        [Test]
        public void testLoginStudent()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/student/1234/111");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("{\"status\":true}", reader.ReadToEnd().ToString());
                }
            }
        }
        [Test]
        public void testLoginVolunteer()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/volunteer/12345/qwerty");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("{\"status\":true}", reader.ReadToEnd().ToString());
                }
            }
        }
        [Test]
        public void testRegistrationStudent()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/register/student?studentid=56789&passwd=999&firstname=suresh&lastname=tummala&email=anvesh525@gmail.com&gender=Male&arrivaltime=05/08/2015%2007:10:31&airlines=Ethihad&flight=Fh007&address=4914%20Grand%20avenue");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("1", reader.ReadToEnd().ToString());
                }
            }

        }
        [Test]
        public void testRegistrationVolunteer()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/register/volunteer?studentid=66666&passwd=kalla&firstname=dileep&lastname=ponnada&email=rpqtc7@mail.umkc.edu&gender=Male&phone=9988998899&address=UMKC%20library&Mon=69&Tue=36&Wed=211&Thu=234&Fri=99&Sat=111&Sun=99");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("1", reader.ReadToEnd().ToString());
                }
            }
        }

        [Test]
        public void testgetStudentDetails()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/student/1234");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("[\"suresh\",\"tummala\",\"anvesh525@gmail.com\",\"Ethihad\",\"Fh007\",\"12345     \"]", reader.ReadToEnd().ToString());
                }
            }
        }

        [Test]
        public void testgetVolunteerDetails()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/volunteer/12345");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("{\"studentList\":[{\"address\":\"4914 Grand avenue\",\"fName\":\"suresh\",\"lName\":\"tummala\"},{\"address\":\"4914 Grand avenue\",\"fName\":\"suresh\",\"lName\":\"tummala\"},{\"address\":\"4914 Grand avenue\",\"fName\":\"satish\",\"lName\":\"tummala\"}]}", reader.ReadToEnd().ToString());
                }
            }
        }

    }
}