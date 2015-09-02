package net.gocourse.util;
/**
 * User: xucan
 * Date: 2015-05-20
 * Time: 10:16
 * description: 局部常量
 */
public class Constant {

    public final static String SP_APP="sp_app";
    public final static String SP_USER="sp_user";
    public final static String SP_Course="sp_course";
    public final static String  ISFIRST = "isfirst";
    /**
     * 课程表的名字
     */
    public  final static String COURSE_NAME = "kb";
    /**
     *
     */
    public final static int MainOk = 2;
    //--------------------------http------------------------------------
    //get请求标识符
    public static final int HTTP_METHOD_GET = 0;

    //post请求标识符
    public static final int HTTP_METHOD_POST = 1;

    //连接超时
    public static final int CONNECT_TIMEOUT = 5000;
    //读取超时
    public static final int READ_TIMEOUT=5000;

    public static String getHttpTypeStr(int httpMethod){
        switch (httpMethod){
            case HTTP_METHOD_GET:
                return "GET";
            case HTTP_METHOD_POST:
                return "POST";
        }
        return "";
    }
    //----------------表示Activity----------------
    public final static int ACTIVITY_MAIN=0;
    public final static int ACTIVITY_SETTING=1;
    public final static int ACTIVITY_ABOUT=2;



    //-----------------------------SERVER----------------------------
    public final static String HOST="http://loveyu.website:8080/gocourse";

    //------------------------------操作类型--------------------------
    public final static int OPERATE_COURSE=0;   //课表查询
    public final static int OPERATE_SIGN_IN=1;  //查看签到
    public final static int OPERATE_EXAM=2;     //查看测验
    public final static int OPERATE_COMMENT=3;  //查看评论

    //------------------------------APP------------------------------
    //密码加密类型：MD5
    public final static String PWD_TYPE="md5";
    //本地图片存储路径
    public final static String LOCAL_PATH_IMAGES="/gocourse/images/";
    //头像的本地存储名称
    public final static String LOCAL_NAME_USER_HEAD="user_head.jpeg";
    //------------------------------URL------------------------------
    //更新app
    public static final int URL_APP_UPDATE = 001;
    //更新app
    public static final int URL_APP_DOWNLOAD = 002;
    //获取服务器时间信息
    public static final int URL_SERVER_TIME=003;

    //登录
    public static final int URL_USER_LOGIN = 101;
    //注销
    public static final int URL_USER_LOGOUT = 102;
    //注册
    public static final int URL_USER_REGISTER = 103;
    //获取用户信息
    public static final int URL_USER_INFO=104;
    //更新Token
    public static final int URL_REFRESH_TOKEN=105;
    //修改密码
    public static final int URL_USER_ALTER_PWD=106;
    //查看邮箱状态
    public static final int URL_USER_EMAIL_STATUS=107;
    //绑定邮箱
    public static final int URL_USER_EMAIL_BIND=108;
    //修改邮箱,解绑，发送验证码到邮箱
    public static final int URL_USER_EMAIL_ALTER_UNBIND=109;
    //修改邮箱，确认，对验证码进行验证
    public static final int URL_USER_EMAIL_ALTER_CONFIRM=110;
    //修改个人简介
    public static final int URL_USER_DES_ALTER=111;
    //忘记密码
    public static final int URL_USER_FORGET_PWD=112;
    //查询教师的全部课程信息
    public static final int URL_COURSE_ALL=201;
    //查询教师的课程名称列表
    public static final int URL_COURSE_LIST=202;

    //创建一个签到
    public static final int URL_SIGN_CREATE=301;
    //查询签到列表
    public static final int URL_SIGN_ALL=302;

    //获取课程评价
    public static final int URL_COMMENT_LIST=401;
    //对学生的课程评论进行回复
    public static final int URL_COMMENT_REPLY=402;
    //对课程评论进行踩，赞
    public static final int URL_COMMENT_LIKE=403;

    //添加一个测验
    public static final int URL_QUIZ_ADD = 501;
    //查询测验列表（course_id）
    public static final int URL_QUIZ_ALL_COURSE=502;
    //查询教师的共享测验列表（包括自己的）
    public static final int URL_QUIZ_ALL_COURSE_SHARED=503;
    //分享某一测验
    public static final int URL_QUIZ_SHARE=504;
    //取消分享某一测验
    public static final int URL_QUIZ_SHARE_CANCEL=505;
    //获取某一课程表绑定的测验（包括教师自己的和共享所得的）
    public static final int URL_QUIZ_ALL_TABLE_BIND=506;
    //获取某一课程表未绑定的测验（只包含教师自己的）
    public static final int URL_QUIZ_ALL_TABLE_UNBIND=507;
    //获取某一课程表未绑定的共享测验列表
    public static final int URL_QUIZ_ALL_TABLE_UNBIND_SHARED=508;
    //绑定某一测验和某一课程表
    public static final int URL_QUIZ_BIND=509;
    //取消绑定某一测验和某一课程表
    public static final int URL_QUIZ_UNBIND=510;


    public static  String getUrlStr(int urlId){
        switch (urlId){
            case URL_USER_LOGIN:
                return HOST +"/user_action/login";
            case URL_USER_LOGOUT:
                return HOST +"/user_action/logout";
            case URL_USER_INFO:
                return HOST +"/teacher/info";
            case URL_REFRESH_TOKEN:
                return HOST +"/user/refresh_token";
            case URL_SERVER_TIME:
                return HOST +"/server_status/time";
            case URL_COURSE_ALL:
                return HOST +"/course_table/search";
            case URL_COURSE_LIST:
                return HOST +"/quiz_teacher/course_list";
            case URL_SIGN_CREATE:
                return HOST +"/sign/create";
            case URL_SIGN_ALL:
                return HOST +"/sign/teacher_list";
            case URL_COMMENT_LIST:
                return HOST +"/review/list";
            case URL_COMMENT_REPLY:
                return HOST +"/review/reply";
            case URL_QUIZ_ADD:
                return HOST +"/quiz_teacher/quiz_add";
            case URL_QUIZ_ALL_COURSE:
                return HOST +"/quiz_teacher/quiz_list";
            case URL_QUIZ_ALL_COURSE_SHARED:
                return HOST +"/quiz_teacher/quiz_share_list";
            case URL_QUIZ_SHARE:
                return HOST +"/quiz_teacher/quiz_share";
            case URL_QUIZ_SHARE_CANCEL:
                return HOST +"/quiz_teacher/quiz_share_cancel";
            case URL_QUIZ_ALL_TABLE_BIND:
                return HOST +"/quiz_teacher/bind_list";
            case URL_QUIZ_ALL_TABLE_UNBIND:
                return HOST +"/quiz_teacher/unbind_list";
            case URL_QUIZ_ALL_TABLE_UNBIND_SHARED:
                return HOST +"/quiz_teacher/unbind_share_list";
            case URL_QUIZ_BIND:
                return HOST +"/quiz_teacher/bind_quiz";
            case URL_QUIZ_UNBIND:
                return HOST +"/quiz_teacher/bind_quiz_cancel";
            case URL_COMMENT_LIKE:
                return HOST +"/review/like";
            case URL_USER_EMAIL_STATUS:
                return HOST +"/user/email/status";
            case URL_USER_EMAIL_BIND:
                return HOST +"/user/email/new";
            case URL_USER_EMAIL_ALTER_UNBIND:
                return HOST +"/user/email/unbind";
            case URL_USER_EMAIL_ALTER_CONFIRM:
                return HOST +"/user/email/unbind_confirm";
            case URL_USER_ALTER_PWD:
                return HOST+"/user/change_password";
            case URL_USER_DES_ALTER:
                return HOST+"/teacher/update_info";


        }
        return "";
    }

    //找回密码页面地址
    public static final String URL_WEB_ALTER_EMAIL="http://loveyu.website/home.html#/email_bind";
    public static final String URL_WEB_FORGET_PWD="http://loveyu.website/forget.html";
    public static final String URL_WEB_ADD_COURSE="http://loveyu.website/course_teacher.html#/schedule_add";

    public final static String[] ARR_SECTIONS=new String[]{"第一节","第二节","第三节","第四节","第五节"};
    public final static String[] ARR_SECTION_TIMES=new String[]{"08:00-09:35","10:05-11:40","14:00-15:35","16:05-17:40","19:00-20:35"};
    public final static String[] ARR_QUIZ_TYPE=new String[]{"单选","多选","判断"};
    public final static String[] ARR_WEEK_NAME=new String[]{"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
}
