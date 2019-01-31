package org.siqisource.mockit.controller;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.siqisource.mockit.service.SqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class JsonController {

	private static Logger logger = LoggerFactory.getLogger(JsonController.class);

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	SqlService sqlService;

	public void service(HttpServletRequest request, HttpServletResponse response) {

		try {
			String distUri = (String) request.getAttribute("distURI");
			String jsonString = "";
			if (distUri.indexOf('{') != -1) {
				jsonString = distUri.substring(0, distUri.length() - 5);
			} else {
				String baseDir = (String) request.getAttribute("baseDir");
				File jsonFile = new File(baseDir + distUri);
				if (!jsonFile.exists()) {
					jsonString = jsonFile.getAbsoluteFile() + " not exist!";
				} else {
					jsonString = FileUtils.readFileToString(jsonFile, Charsets.UTF_8);
				}
			}
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.print(jsonString);

		} catch (Exception e) {
			logger.error("error:", e);
		}
	}
}
