package com.nis.web.controller.restful;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nis.web.service.fdfs.FastDFSFile;
import com.nis.web.service.fdfs.FileManager;

/**
 * <p>Title: FastDFSController.java</p>
 * <p>Description: fastdfs测试controller</p>
 * <p>Company: IIE</p>
 * @author rkg
 * @date 2018年5月16日
 *
 */
@Controller
public class FastDFSController {
	public static void main(String[] args) {
		String path = new FastDFSController().testAdd();
		//String path = "M00/00/01/CgAGwFr786aAegOQAADz3NN2rlY283.jpg";
		new FastDFSController().testDelete(path);

	}

	public void testDelete(String filePath) {
		filePath = filePath.substring(filePath.indexOf("group"));
		String groupName=filePath.substring(0,filePath.indexOf("/"));
		filePath=filePath.substring(filePath.indexOf("/")+1);
		Integer a = FileManager.delete_file(groupName, filePath);
		System.out.println(a);
	}

	public String testAdd() {
		String filePath = "d:/1.jpg";
		File file = new File(filePath);
		String fileName = file.getName();
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		NameValuePair[] meta_list = new NameValuePair[4];
		meta_list[0] = new NameValuePair("fileName", fileName);
		meta_list[1] = new NameValuePair("fileLength", String.valueOf(file.length()));
		meta_list[2] = new NameValuePair("fileExt", ext);
		meta_list[3] = new NameValuePair("fileAuthor", "rkg");
		String successFilePath = FileManager.upload(new FastDFSFile(getByte(file), ext), null);
		System.out.println("上传成功返回的路径是" + successFilePath);
		return successFilePath;
	}

	private byte[] getByte(File file) {
		byte[] bf = null;
		try {
			InputStream fis = new FileInputStream(file);
			bf = new byte[fis.available()];
			fis.read(bf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bf;
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	// public String add(@Validated User user, MultipartFile attach,
	// HttpServletRequest request)
	public String add(MultipartFile attach, HttpServletRequest request) throws IOException, MyException {
		// 获取文件后缀名
		String ext = attach.getOriginalFilename().substring(attach.getOriginalFilename().lastIndexOf(".") + 1);
		FastDFSFile file = new FastDFSFile(attach.getBytes(), ext);
		NameValuePair[] meta_list = new NameValuePair[4];
		meta_list[0] = new NameValuePair("fileName", attach.getOriginalFilename());
		meta_list[1] = new NameValuePair("fileLength", String.valueOf(attach.getSize()));
		meta_list[2] = new NameValuePair("fileExt", ext);
		meta_list[3] = new NameValuePair("fileAuthor", "rkg");
		String filePath = FileManager.upload(file, meta_list);
		// user.setFilePath(filePath);
		// users.put(user.getUsername(), user);
		return filePath;
	}

	// public ResponseEntity<byte[]> download(@PathVariable String filePath,
	// HttpServletResponse response)
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@RequestParam("filePath") String filePath, HttpServletResponse response)
			throws IOException, MyException {
		// User u = users.get(username);
		// String filePath = u.getFilePath();
		// String filePath = "";
		String substr = filePath.substring(filePath.indexOf("group"));
		String group = substr.split("/")[0];
		String remoteFileName = substr.substring(substr.indexOf("/") + 1);
		// 重命名文件
		String specFileName = "rkg" + substr.substring(substr.indexOf("."));
		return FileManager.download(group, remoteFileName, specFileName);
	}
}
