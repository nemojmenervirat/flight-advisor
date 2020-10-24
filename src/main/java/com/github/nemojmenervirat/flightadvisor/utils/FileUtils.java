package com.github.nemojmenervirat.flightadvisor.utils;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

	public static boolean isTxtOrCsv(MultipartFile file) {
		return file.getContentType() != "text/csv" && file.getContentType() != "text/plain";
	}

}
