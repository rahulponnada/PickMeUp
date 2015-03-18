using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Net.Mail;
using System.Net;

namespace PickMeUpService
{

    public class sendNotification
    {
    private static string SMTPSERVER = "smtp.gmail.com";
    private static int PORTNO = 587;

    public string SendEmail(string gmailUserName, string gmailUserPassword,  
       string[] emailToAddress, string[] ccemailTo, string subject, string body, bool isBodyHtml)
    {            
        if (gmailUserName == null || gmailUserName.Trim().Length == 0)
        {
            return "User Name Empty";
        }
        if (gmailUserPassword == null || gmailUserPassword.Trim().Length == 0)
        {
            return "Email Password Empty";
        }
        if (emailToAddress == null || emailToAddress.Length == 0)
        {
            return "Email To Address Empty";
        }

        List<string> tempFiles = new List<string>();

        SmtpClient smtpClient = new SmtpClient(SMTPSERVER, PORTNO);
        smtpClient.EnableSsl = true;
        smtpClient.DeliveryMethod = SmtpDeliveryMethod.Network;
        smtpClient.UseDefaultCredentials = false;
        smtpClient.Credentials = new NetworkCredential(gmailUserName, gmailUserPassword);
        using (MailMessage message = new MailMessage())
        {
            message.From = new MailAddress(gmailUserName);
            message.Subject = subject == null ? "" : subject;
            message.Body = body == null ? "" : body;
            message.IsBodyHtml = isBodyHtml;

            foreach (string email in emailToAddress)
            {                   
                message.To.Add(email);
            }
            if (ccemailTo != null && ccemailTo.Length > 0)
            {
                foreach (string emailCc in ccemailTo)
                {                       
                    message.CC.Add(emailCc);
                }
            }
            try
            {
                smtpClient.Send(message);                 
                return "Email Send SuccessFully";
            }
            catch
            {                  
                return "Email Send failed";
            }
        }
    }
    }
}