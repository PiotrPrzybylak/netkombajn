package pl.netolution.sklep3.web.errors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class ExceptionReportMailer {

	private static final Logger log = Logger.getLogger(ExceptionReportMailer.class);

	private MailSender mailSender;

	public void sendExceptionReport(HttpServletRequest request, Exception exc, String applicationType) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("exception_alert@netolution.pl");
		message.setTo(new String[] { "piotr.przybylak@gmail.com" });
		message.setSubject("Exception in " + applicationType + ": " + request.getServerName());
		String content = "Exception: " + exc + "\nMessage: " + exc.getMessage();
		content += "\nSystem time: " + new Date();
		content += "\nURI: " + request.getRequestURI();
		String link = "\nURL: " + request.getRequestURL();
		if (request.getQueryString() != null) {
			link += "?" + request.getQueryString();
		}
		content += link;
		//	content += "\nQuery params: " + ;

		content += "\n\nMethod: " + request.getMethod();

		content += "\nParameters:\n";
		for (String paramName : (Set<String>) request.getParameterMap().keySet()) {
			content += "\n" + paramName + "=" + Arrays.toString((String[]) request.getParameterMap().get(paramName));
		}

		content += "\n\nUser IP: " + request.getRemoteAddr();
		content += "\nUser Host: " + request.getRemoteHost();
		content += "\nUser name:" + request.getRemoteUser();

		content += "\n\n HTTP headers:";
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			content += "\n" + headerName + " : " + request.getHeader(headerName);
		}

		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		exc.printStackTrace(writer);
		writer.flush();
		content += "\n\nStackTrace: \n" + stringWriter.toString();
		message.setText(content);
		log.debug("Sending error message: \n" + content);
		mailSender.send(message);
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
}
