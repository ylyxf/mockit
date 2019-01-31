package org.siqisource.mockit.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

@Controller
public class YuiCompressorController {

	@RequestMapping("/index.js")
	public void javascript(String modules, HttpServletResponse response) {
		ApplicationHome home = new ApplicationHome(getClass());
		File jarFile = home.getSource();
		File parentFolder = jarFile.getParentFile();
		String[] pageModules = modules.split(";");
		StringBuffer allScripts = new StringBuffer();
		for (String pageModule : pageModules) {
			File jsFile = new File(parentFolder.getAbsolutePath() + "/src/" + pageModule + ".js");
			if (jsFile.exists()) {
				try {
					allScripts.append(FileUtils.readFileToString(jsFile, StandardCharsets.UTF_8));
				} catch (IOException e) {
					allScripts.append("alert(' error load module : " + pageModule + "');");
				}
			} else {
				allScripts.append("alert(' can not find module : " + pageModule + "');");
			}
		}
		InputStreamReader in = new InputStreamReader(IOUtils.toInputStream(allScripts, StandardCharsets.UTF_8));

		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/javascript");
			PrintWriter printWriter = response.getWriter();
			JavaScriptCompressor compressor = new JavaScriptCompressor(in, new ErrorReporter() {

				public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
					System.err.println("\n[WARNING] ");
					if (line < 0) {
						System.err.println("  " + message);
					} else {
						System.err.println("  " + line + ':' + lineOffset + ':' + message);
					}
				}

				public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
					System.err.println("[ERROR] in ");
					if (line < 0) {
						System.err.println("  " + message);
					} else {
						System.err.println("  " + line + ':' + lineOffset + ':' + message);
					}
				}

				public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
						int lineOffset) {
					error(message, sourceName, line, lineSource, lineOffset);
					return new EvaluatorException(message);
				}
			});

			// Close the input stream first, and then open the output stream,
			// in case the output file should override the input file.
			in.close();
			in = null;

			compressor.compress(printWriter, -1, true, false, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
