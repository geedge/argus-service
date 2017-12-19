/**   
* @Title: ConfigFile.java 
* @Package com.nis.domain.restful 
* @Description: TODO(用一句话描述该文件做什么) 
* @author （DDN）  
* @date 2016年09月21日 下午5:36:28 
* @version V1.0   
*/
package com.nis.domain.restful;


import org.springframework.web.multipart.MultipartFile;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: ConfigFile
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author (DDM)
 * @date 2016年09月21日 下午5:36:28
 * @version V1.0
 */
public class ConfigFile extends ConfigCommonSource {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -863523890174554410L;
	@ApiModelProperty(value = "上传文件列表", required = true)
	private MultipartFile[] files;
	
	public MultipartFile[] getFiles() {
		return files;
	}
	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}
}
