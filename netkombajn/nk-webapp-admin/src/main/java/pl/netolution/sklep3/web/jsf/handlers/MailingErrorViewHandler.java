package pl.netolution.sklep3.web.jsf.handlers;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import pl.netolution.sklep3.web.errors.ExceptionReportMailer;

import com.sun.facelets.FaceletViewHandler;

public class MailingErrorViewHandler extends FaceletViewHandler {

	private static final Logger log = Logger.getLogger(MailingErrorViewHandler.class);

	public MailingErrorViewHandler(ViewHandler parent) {
		super(parent);
	}

	@Override
	protected void handleRenderException(FacesContext facesContext, Exception exception) throws IOException, ELException, FacesException {

		sendErrorEmail(exception, facesContext);

		super.handleRenderException(facesContext, exception);
	}

	private void sendErrorEmail(Exception exception, FacesContext facesContext) {

		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		ExceptionReportMailer exceptionReportMailer = (ExceptionReportMailer) applicationContext.getBean("exceptionReportMailer");

		exceptionReportMailer.sendExceptionReport(request, exception, "admin");

	}
}
