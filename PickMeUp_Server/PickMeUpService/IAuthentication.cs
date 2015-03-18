using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace PickMeUpService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IAuthentication
    {
        [OperationContract]
        AuthService.Status loginStudent(string usrn, string pwd);

        [OperationContract]
        AuthService.Status loginVolunteer(string usrn, string pwd);
       

        [OperationContract]
        int registerStudent(string sid, string pwd, string fname, string lname, string eid,string sex, string atime, string airline, string flight, string address1);

        [OperationContract]
        int registerVolunteer(string sid, string pwd, string fname, string lname, string eid, string sex, string ph, string address1, byte mon, byte tue, byte wed, byte thu, byte fri, byte sat, byte sun);

        [OperationContract]
        String[] getStudentDetails(string usrn);

        [OperationContract]
        StudentList getVolunteerDetails(string usrn);
    }


    // Use a data contract as illustrated in the sample below to add composite types to service operations.
    [DataContract]
    public class CompositeType
    {
        bool boolValue = true;
        string stringValue = "Hello ";

        [DataMember]
        public bool BoolValue
        {
            get { return boolValue; }
            set { boolValue = value; }
        }

        [DataMember]
        public string StringValue
        {
            get { return stringValue; }
            set { stringValue = value; }
        }
    }

    [DataContract]
    public class Student
    {
        [DataMember]
        public String fName { get; set; }
        [DataMember]
        public String lName { get; set; }
        [DataMember]
        public String address { get; set; }
    }

    [DataContract]
    public class StudentList
    {
        [DataMember]
        public List<Student> studentList { get; set; }
    }
}
