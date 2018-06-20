package com.kayak.upload.action;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kayak.base.action.BaseController;
import com.kayak.base.util.Tools;
import com.kayak.util.SysParams;

@RestController
public class UploadAction extends BaseController {

	/**
	 * 通用的上传文件接口
	 * 
	 * @param request
	 * @param response
	 * @param files
	 * @return
	 */
	@RequestMapping(value = "/base/comn-upload.json")
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file) {
		if (file != null) {
			try {
				String uploadDir = SysParams.getParams("deploy_home");

				if (Tools.strIsEmpty(uploadDir)) {
					return updateFailure("获取系统参数错误");
				}

				String upload_path = UploadUtil.upload(file, uploadDir);

				JSONObject json = new JSONObject();
				json.put("success", true);
				json.put("upload_path", upload_path);
				json.put("upload_name", file.getOriginalFilename());

				return json.toString();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return updateFailure("文件上传失败");
			}
		}

		return updateFailure("文件上传失败");

	}

	/**
	 * 判断浏览器是否是IE
	 * 
	 * @param request
	 * @return
	 */
	public boolean isIE(HttpServletRequest request) {
		String agent = request.getHeader("USER-AGENT").toLowerCase();

		if (agent.contains("rv:11.0") || agent.contains("msie")) {
			return true;
		}
		return false;
	}

}
