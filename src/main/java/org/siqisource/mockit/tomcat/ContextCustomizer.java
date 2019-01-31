package org.siqisource.mockit.tomcat;

import java.io.File;

import org.apache.catalina.Context;
import org.siqisource.mockit.MockitFilter;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

@Component
public class ContextCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		TomcatServletWebServerFactory tomcatFactory = (TomcatServletWebServerFactory) factory;
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		File parentFolder = jarFile.getParentFile();

		String srcPath = parentFolder.getAbsolutePath() + "/mock";
		if (!new File(srcPath).exists()) {
			return;
		}
		TomcatContextCustomizer srcCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				context.setPath("/");
				context.setDocBase(srcPath);
				// mockit过滤器
				MockitFilter.context = context;
			}
		};

		tomcatFactory.addContextCustomizers(srcCustomizer);

	}
}
