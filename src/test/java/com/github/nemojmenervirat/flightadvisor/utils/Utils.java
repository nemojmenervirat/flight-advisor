package com.github.nemojmenervirat.flightadvisor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

public class Utils {

	public static String readResourceFile(String resourcePath) throws IOException {
		InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();
		String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		return content;
	}
}
