package com.tgt.cwl.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class ConversionUtil {
	public static String convertFileContentToString(String fileName) throws IOException {
		ClassLoader classLoader = ConversionUtil.class.getClassLoader();
		return IOUtils.toString(classLoader.getResourceAsStream(fileName));
	}
}
