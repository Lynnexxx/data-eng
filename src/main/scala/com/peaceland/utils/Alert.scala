package com.peaceland.utils

//import com.github.jurajburian.mailer._ //a wrapper based on java mail api
import java.util.Properties
import javax.mail._
import javax.mail.internet._
import scala.util._



case class Alert
(
  to: String,
  //cc: String,
  from: String,
  subject: String,
  content: String
)

object Alert
{
  def generateAlert(alert : Alert)=
  {
    //Build java mail session
    //val session = (SessionFactory() + (SmtpAddress("smtp.gmail.com", 587))).session()
    //val session = (SmtpAddress("smtp.gmail.com", 587) :: SessionFactory()).session(Some(alert.from-> "Loyanta!2700"))
    val props = new Properties()
    props.put("mail.smtp.ssl.enable", "false")
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.ssl.protocols", "TLSv1.2")
    props.put("mail.debug", "true")
    props.put("mail.smtp.host", "smtp.office365.com")
    //props.put("mail.smtp.host", "smtp.gmail.com")
    props.put("mail.smtp.port", "587")

    //val TO_ADDRESSES = Array("marie-lynne-murielle-essenahoun.agbaholou@efrei.net", "axel-loyanta.daoudongar@efrei.net")
    val TO_ADDRESSES = Array(alert.to)

    val session = Session.getInstance(props, new javax.mail.Authenticator() {
      override def getPasswordAuthentication(): PasswordAuthentication = {
        new PasswordAuthentication(alert.from, "#Ax20-Da52")
      }
    })
    //Build message content
    //val content : Content = new  Content().text(alert.content)
    val message = new MimeMessage(session)
    message.setFrom(new InternetAddress(alert.from))
    val toAddresses = TO_ADDRESSES.map(new InternetAddress(_))
    message.setRecipients(Message.RecipientType.TO, toAddresses.asInstanceOf[Array[Address]])
    message.setSubject(alert.subject)
    message.setText(alert.content)


    //Send mail
    Try{Transport.send(message)}
  }
}
