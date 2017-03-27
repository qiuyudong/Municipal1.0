/*package com.example.municipalmanage;


public class WebApi {
	// API
	public static String URL_SERVICE = "http://49.140.76.93:8080/municipal/";
	// 登陆
	public static String URL_LOGIN = URL_SERVICE + "clientLogin";
	// 加载发现
	public static String URL_LoadFinds = URL_SERVICE + "loadFinds"; 
           // 上传发现
	public static String URL_uploadFind  = URL_SERVICE + "uploadFind";
	// 上传维修
	public static String URL_uploadFinish = URL_SERVICE + "uploadFinish";
	// 接单
	public static String URL_receive = URL_SERVICE + "receive";
	//我的接单
	public static String URL_MyOrder = URL_SERVICE + "loadFindsByphone";

	
}*/


package com.example.municipalmanage;


public class WebApi {
	// API
	public static String URL_SERVICE = "http://123.206.54.133:8080/municipal/";
	// 登陆
	public static String URL_LOGIN = URL_SERVICE + "userAction!clientLogin";
	// 加载发现
	public static String URL_LoadFinds = URL_SERVICE + "projectAction!loadFinds"; 
            // 上传发现
	public static String URL_uploadFind  = URL_SERVICE + "projectAction!uploadFind";
	// 上传维修
	public static String URL_uploadFinish = URL_SERVICE + "projectAction!uploadFinish";
	// 接单
	public static String URL_receive = URL_SERVICE + "projectAction!receive";
	//我的接单
	public static String URL_MyOrder = URL_SERVICE + "projectAction!loadFindsByphone";

}


