package org.siqisource.mockit;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class ContextCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {

		MockitTomcatServletWebServerFactory tomcatFactory = (MockitTomcatServletWebServerFactory) factory;

		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		File parentFolder = jarFile.getParentFile();

		String srcPath = parentFolder.getAbsolutePath() + "/src";

		TomcatContextCustomizer srcCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				context.setPath("/");
				context.setDocBase(srcPath);
			}
		};

		tomcatFactory.addContextCustomizers(srcCustomizer);

	}
}
