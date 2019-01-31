package org.siqisource.mockit.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siqisource.mockit.service.SqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SqlController {

	@Autowired
	SqlService sqlService;

	public void service(HttpServletRequest request, HttpServletResponse response) {
		String originUri = (String) request.getAttribute("originURI");
		String distUri = (String) request.getAttribute("distURI");
		sqlService.service(originUri, distUri);
	}
}
