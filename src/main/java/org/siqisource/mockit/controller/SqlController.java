package org.siqisource.mockit.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siqisource.mockit.service.SqlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class SqlController {

	private static Logger logger = LoggerFactory.getLogger(SqlController.class);

	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	SqlService sqlService;

	public void service(HttpServletRequest request, HttpServletResponse response) {

		try {
			String originUri = (String) request.getAttribute("originURI");
			String distUri = (String) request.getAttribute("distURI");

			Object result = sqlService.service(originUri, distUri);
			String jsonString = mapper.writeValueAsString(result);

			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			PrintWriter printWriter = response.getWriter();
			printWriter.print(jsonString);

		} catch (Exception e) {
			logger.error("error:", e);
		}
	}
}
