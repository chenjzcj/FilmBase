package succ7.com.filmbase.bmob;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/18 0018 17:37
 * 错误码
 */
public class BmobExceptionErrorCode {
    /**
     * 内容：AppKey is Null, Please initialize BmobSDK.
     * 含义：Application Id为空，请初始化。
     */
    private static final int ERROR_CODE1 = 9001;
    /**
     * 内容：Parse data error
     * <p/>
     * 含义：解析返回数据出错
     */
    private static final int ERROR_CODE2 = 9002;
    /**
     * 内容：upload file error
     * <p/>
     * 含义：上传文件出错
     */
    private static final int ERROR_CODE3 = 9003;
    /**
     * 内容：upload file failure
     * <p/>
     * 含义：文件上传失败
     */
    private static final int ERROR_CODE4 = 9004;
    /**
     * 内容：A batch operation can not be more than 50
     * <p/>
     * 含义：批量操作只支持最多50条
     */
    private static final int ERROR_CODE5 = 9005;
    /**
     * 内容：objectId is null
     * <p/>
     * 含义：objectId为空
     */
    private static final int ERROR_CODE6 = 9006;
    /**
     * 内容：BmobFile File size must be less than 10M.
     * <p/>
     * 含义：文件大小超过10M
     */
    private static final int ERROR_CODE7 = 9007;
    /**
     * 内容：BmobFile File does not exist.
     * <p/>
     * 含义：上传文件不存在
     */
    private static final int ERROR_CODE8 = 9008;
    /**
     * 内容：No cache data.
     * <p/>
     * 含义：没有缓存数据
     */
    private static final int ERROR_CODE9 = 9009;
    /**
     * 内容：The network is not normal.(Time out)
     * <p/>
     * 含义：网络超时
     */
    private static final int ERROR_CODE10 = 90010;
    /**
     * 内容：BmobUser does not support batch operations.
     * <p/>
     * 含义：BmobUser类不支持批量操作
     */
    private static final int ERROR_CODE11 = 90011;
    /**
     * 内容：context is null.
     * <p/>
     * 含义：上下文为空
     */
    private static final int ERROR_CODE12 = 90012;
    /**
     * 内容： BmobObject Object names(database table name) format is not correct.
     * <p/>
     * 含义：BmobObject（数据表名称）格式不正确
     */
    private static final int ERROR_CODE13 = 90013;
    /**
     * 含义：第三方账号授权失败
     */
    private static final int ERROR_CODE14 = 90014;
    /**
     * 含义：其他错误均返回此code
     */
    private static final int ERROR_CODE15 = 90015;
    /**
     * 内容：The network is not available,please check your network!
     * <p/>
     * 含义：无网络连接，请检查您的手机网络。
     */
    private static final int ERROR_CODE16 = 90016;
    /**
     * 含义：与第三方登录有关的错误，具体请看对应的错误描述
     */
    private static final int ERROR_CODE17 = 90017;
    /**
     * 含义：参数不能为空
     */
    private static final int ERROR_CODE18 = 90018;
    /**
     * 含义：格式不正确：手机号码、邮箱地址、验证码
     */
    private static final int ERROR_CODE19 = 90019;
}
