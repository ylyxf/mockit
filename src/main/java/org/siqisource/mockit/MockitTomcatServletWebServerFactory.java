package org.siqisource.mockit;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;

public class MockitTomcatServletWebServerFactory extends TomcatServletWebServerFactory {

	@Override
	protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {

		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		File parentFolder = jarFile.getParentFile();
		String mockPath = parentFolder.getAbsolutePath() + "/mock";

		Context context = tomcat.addWebapp("/mock", mockPath);
		WebappLoader loader = new WebappLoader(Thread.currentThread().getContextClassLoader());
		context.setLoader(loader);

		return new TomcatWebServer(tomcat, getPort() >= 0);
	}

}
