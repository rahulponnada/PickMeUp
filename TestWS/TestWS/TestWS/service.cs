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
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/student/9999/111");
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
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/volunteer/77777/aswd");
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
        public void testLoginAdmin()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/admin/16173144/abcd");
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
        public void testassignVolunteer()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/assignVolunteer/98765");
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
        public void testRegistrationStudent()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/register/student?studentid=1234&passwd=111&firstname=suresh&lastname=tummala&email=anvesh525@gmail.com&gender=Male&arrivaltime=01/02/2015%2022:30:31&airlines=Ethihad&flight=Fh007&address=4914%20Grand%20avenue");
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
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/register/volunteer?studentid=12345&passwd=aswd&firstname=rahul&lastname=ponnada&email=rpqt7@mail.umkc.edu&gender=Male&phone=9988998899&address=UMKC%20library&Mon=255&Tue=21&Wed=76&Thu=98&Fri=129&Sat=200&Sun=67");
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
        public void testRegistrationAdminFailCase()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/register/admin?studentid=16173144&passwd=abcd&firstname=goutham&lastname=donthu&&email=goutham.donthu@gmail.com&gender=male&phone=6712345667&address=4819%20halim%20nagar%20hyd-610020&university=nagarjuna%20unversity");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("0", reader.ReadToEnd().ToString());
                }
            }
        }

        [Test]
        public void testgetStudentDetails()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/student/98765");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreEqual("[\"Rajesh\",\"tummala\",\"rajeshtummala@gmail.com\",\"United\",\"F0999\",\"88888     \"]", reader.ReadToEnd().ToString());
                }
            }
        }

        [Test]
        public void testgetVolunteerDetailsFailCase()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/volunteer/12345");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreNotEqual("{\"studentList\":[{\"address\":\"4914 Grand avenue\",\"fName\":\"sampath\",\"lName\":\"tummala\"},{\"address\":\"4914 Grand avenue\",\"fName\":\"Kalid\",\"lName\":\"tummala\"}]}", reader.ReadToEnd().ToString());
                }
            }
        }

        [Test]
        public void testgetStudentVolunteerDetails()
        {
            WebRequest req = WebRequest.Create(@"http://localhost:52715/AuthService.svc/login/volunteer/12345");
            req.Method = "GET";
            HttpWebResponse resp = req.GetResponse() as HttpWebResponse;
            if (resp.StatusCode == HttpStatusCode.OK)
            {
                using (Stream respStream = resp.GetResponseStream())
                {
                    StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
                    Assert.AreNotEqual("{\"VolunteerList\":[{\"studentList\":[{\"arrivalTime\":null,\"fName\":\"anvesh\",\"lName\":\"tummala\",\"userName\":\"77777     \"},{\"arrivalTime\":\"12\\/10\\/2015 5:30:11 PM\",\"fName\":\"sampath\",\"lName\":\"tummala\",\"userName\":\"33333     \"},{\"arrivalTime\":\"1\\/2\\/2015 6:30:31 PM\",\"fName\":\"Kalid\",\"lName\":\"tummala\",\"userName\":\"9999      \"}]}]}", reader.ReadToEnd().ToString());
                }
            }
        }

    }
}