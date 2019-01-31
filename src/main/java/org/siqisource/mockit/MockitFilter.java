package org.siqisource.mockit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.siqisource.mockit.controller.SqlController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@WebFilter(urlPatterns = "/**", filterName = "mockitFilter")
@Order(Integer.MIN_VALUE)
public class MockitFilter extends FileAlterationListenerAdaptor implements Filter {

	private static Logger logger = LoggerFactory.getLogger(MockitFilter.class);

	public static Context context = null;

	private Map<String, String> innerServiceMap = new HashMap<String, String>();

	private Map<String, Servlet> proxyServiceMap = new HashMap<String, Servlet>();

	@Value("${mockit.mockdir}")
	private final String mockdir = "mock";

	private final String configFileName = "mockit.properties";

	@Autowired
	private SqlController sqlController;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String url = request.getRequestURI();
		String dist = innerServiceMap.get(url);
		if (dist != null) {
			System.out.println("url map:" + url + "-->" + dist);
			request.setAttribute("originURI", url);
			request.setAttribute("distURI", dist);

			if (dist.endsWith(".jsp")) {
				request.getRequestDispatcher(dist).forward(request, response);
			} else if (dist.endsWith(".sql")) {
				sqlController.service(request, response);
			} else if (dist.endsWith(".js")) {

			} else if (dist.endsWith(".xls") || dist.endsWith(".xlsx")) {

			} else {
				chain.doFilter(servletRequest, servletResponse);
			}
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		File parentFolder = jarFile.getParentFile();
		String mockConfig = parentFolder + "/" + mockdir + "/" + configFileName;
		File mockConfigFile = new File(mockConfig);
		if (!mockConfigFile.exists()) {
			logger.error("Can't find file:" + mockConfig + ",I will create it");
			try {
				mockConfigFile.createNewFile();
			} catch (IOException e) {
				logger.error("Can't create file:" + mockConfig, e);
			}
		}
		FileAlterationObserver observer = new FileAlterationObserver(parentFolder);
		observer.addListener(this);
		long interval = TimeUnit.SECONDS.toMillis(1);
		FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
		try {
			monitor.start();
		} catch (Exception e) {
			logger.error("Error watch file :" + mockConfigFile);
		}
		onFileChange(mockConfigFile);
	}

	@Override
	public void destroy() {

	}

	public void onFileChange(File file) {
		try {
			if (!file.getName().equals(configFileName)) {
				return;
			}
			System.out.println("url mappings reloading ...");
			InputStream in = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(in);
			Map<String, String> tempServiceMap = new HashMap<String, String>();

			for (Map.Entry<String, Servlet> entry : proxyServiceMap.entrySet()) {
				context.removeServletMapping(entry.getKey());
				entry.getValue().destroy();
			}

			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				if (value.startsWith("^http://") || value.startsWith("^https://")) {
					// apply proxy servlet
					String targetUrl = value.substring(1);
					String pattern = key;
					String proxyServletName = UUID.randomUUID().toString();
					ProxyServlet proxyServlet = new ProxyServlet();
					Wrapper wrapper = Tomcat.addServlet(context, proxyServletName, proxyServlet);
					wrapper.addInitParameter("targetUri", targetUrl);
					context.addServletMappingDecoded(pattern, proxyServletName);
					proxyServiceMap.put(pattern, proxyServlet);
				} else {
					// inner rules
					tempServiceMap.put(key, value);
				}
			}
			innerServiceMap = tempServiceMap;
			System.out.println("url mappings reload success!");
		} catch (Exception e) {
			logger.error("error load file:" + file.getAbsolutePath() + " as properties");
		}
	}

}
