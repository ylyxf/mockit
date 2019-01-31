package org.siqisource.mockit.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;

public class MockitTomcatServletWebServerFactory extends TomcatServletWebServerFactory {

	@Value("${mockit.src-context-path}")
	String srcContextPath = "/web";

	@Override
	protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {

		if ("/".equals(srcContextPath)) {
			throw new RuntimeException("the src Context Path can't be / in mockit");
		}

		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		File parentFolder = jarFile.getParentFile();
		String mockPath = parentFolder.getAbsolutePath() + "/src";
		File mockPathFile = new File(mockPath);
		if (!mockPathFile.exists()) {
			mockPathFile.mkdirs();
		}
		Context context = tomcat.addWebapp(srcContextPath, mockPath);
		// WebappLoader loader = new
		// WebappLoader(Thread.currentThread().getContextClassLoader());
		// context.setLoader(loader);

		return new TomcatWebServer(tomcat, getPort() >= 0);
	}

}
