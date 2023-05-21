package com.roy.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.roy.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {

		String fileName = null;

		// file name
		String name = file.getOriginalFilename();

		// random name generate file
		String randomId = UUID.randomUUID().toString();
		fileName = randomId.concat(name.substring(name.lastIndexOf(".")));

		// final path
		String filePath = path + File.separator + fileName;

		// create folder if not created
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}

		// file copy
		Files.copy(file.getInputStream(), Paths.get(filePath));

		return fileName;

	}

	@Override
	public InputStream getResource(String path, String file) throws FileNotFoundException {
		String filePath = path + File.separator + file;
		InputStream is = new FileInputStream(filePath);
		return is;

	}

}
