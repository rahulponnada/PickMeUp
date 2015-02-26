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
            WebRequest req = WebRequest.Create(@"http://localhost:51981/AuthService.svc/login/student/123/123");
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
            WebRequest req = WebRequest.Create(@"http://localhost:51981/AuthService.svc/login/volunteer/1234/qwerty");
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
            WebRequest req = WebRequest.Create(@"http://localhost:51981/AuthService.svc/register/student?studentid=128&passwd=211904&firstname=surya&lastname=prabha&email=ab@gmail.com&gender=f&arrivaldate=10/10/1234&arrivaltime=12&airlines=ethihad&flight=12&address=plaza");
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
            WebRequest req = WebRequest.Create(@"http://localhost:51981/AuthService.svc/register/volunteer?studentid=389326354&passwd=don44&firstname=sharukh&lastname=khan&email=686543@fb&gender=m&phone=1234&address=dsnfkjsd&available=skh");
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
    }
}