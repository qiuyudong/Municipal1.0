/*package com.example.municipalmanage;


public class WebApi {
	// API
	public static String URL_SERVICE = "http://49.140.76.93:8080/municipal/";
	// ��½
	public static String URL_LOGIN = URL_SERVICE + "clientLogin";
	// ���ط���
	public static String URL_LoadFinds = URL_SERVICE + "loadFinds"; 
           // �ϴ�����
	public static String URL_uploadFind  = URL_SERVICE + "uploadFind";
	// �ϴ�ά��
	public static String URL_uploadFinish = URL_SERVICE + "uploadFinish";
	// �ӵ�
	public static String URL_receive = URL_SERVICE + "receive";
	//�ҵĽӵ�
	public static String URL_MyOrder = URL_SERVICE + "loadFindsByphone";

	
}*/


package com.example.municipalmanage;


public class WebApi {
	// API
	public static String URL_SERVICE = "http://123.206.54.133:8080/municipal/";
	// ��½
	public static String URL_LOGIN = URL_SERVICE + "userAction!clientLogin";
	// ���ط���
	public static String URL_LoadFinds = URL_SERVICE + "projectAction!loadFinds"; 
            // �ϴ�����
	public static String URL_uploadFind  = URL_SERVICE + "projectAction!uploadFind";
	// �ϴ�ά��
	public static String URL_uploadFinish = URL_SERVICE + "projectAction!uploadFinish";
	// �ӵ�
	public static String URL_receive = URL_SERVICE + "projectAction!receive";
	//�ҵĽӵ�
	public static String URL_MyOrder = URL_SERVICE + "projectAction!loadFindsByphone";

}


