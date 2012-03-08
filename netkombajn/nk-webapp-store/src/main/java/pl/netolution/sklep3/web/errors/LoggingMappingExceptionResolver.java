package pl.netolution.sklep3.web.errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class LoggingMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private static final Logger log = Logger.getLogger(LoggingMappingExceptionResolver.class);

	private ExceptionReportMailer exceptionReportMailer;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse arg1, Object handler, Exception exc) {
		log.error(handler, exc);

		try {
			exceptionReportMailer.sendExceptionReport(request, exc, "sklep");

		} catch (RuntimeException ex) {
			log.error("Exception in exception handler!", ex);
		}

		return super.doResolveException(request, arg1, handler, exc);
	}

	public void setExceptionReportMailer(ExceptionReportMailer exceptionReportMailer) {
		this.exceptionReportMailer = exceptionReportMailer;
	}

}
