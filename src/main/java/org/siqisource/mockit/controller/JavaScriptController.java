package org.siqisource.mockit.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.siqisource.mockit.service.SqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author yulei
 *
 */
@Controller
public class JavaScriptController {

	private static Logger logger = LoggerFactory.getLogger(JavaScriptController.class);

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	SqlService sqlService;

	public void service(HttpServletRequest request, HttpServletResponse response) {

		try {
			String baseDir = (String) request.getAttribute("baseDir");
			String distUri = (String) request.getAttribute("distURI");

			String result = "";
			File jsFile = new File(baseDir + distUri);
			if (!jsFile.exists()) {
				result = jsFile.getAbsoluteFile() + " not exist!";
			} else {
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
				String jsCode = FileUtils.readFileToString(jsFile, Charsets.UTF_8);
				try {
					String paramString = toJsonParams(request.getParameterMap());
					jsCode = "(" + jsCode + "(JSON.parse('" + paramString + "')))";
					Object jsResult = engine.eval(jsCode);
					result = mapper.writeValueAsString(jsResult);
				} catch (Exception e) {
					result = e.getMessage();
				}
			}

			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.print(result);

		} catch (Exception e) {
			logger.error("error:", e);
		}
	}

	private String toJsonParams(Map<String, String[]> params) throws JsonProcessingException {
		Map<String, Object> result = new HashMap<String, Object>();
		for (Map.Entry<String, String[]> param : params.entrySet()) {
			if (param.getValue().length == 1) {
				result.put(param.getKey(), param.getValue()[0]);
			} else {
				result.put(param.getKey(), param.getValue());
			}
		}
		return mapper.writeValueAsString(params);
	}
}
