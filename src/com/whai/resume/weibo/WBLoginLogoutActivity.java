package com.whai.resume.weibo;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;
import com.whai.resume.R;

/**
 * ������Ҫ��ʾ�����ʹ�õ�¼/ע���ؼ���
 * Ŀǰ�������ṩ�������¼�ؼ���
 * <li>�����ĵ�½�ؼ���ֻ�ṩ��¼���ܣ�����������ʽ
 * <li>��¼/ע����Ͽؼ����ṩ���õĵ�¼��ע�����ܣ�����������ʽ
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBLoginLogoutActivity extends Activity {
    
    /** UIԪ���б� */
    private TextView mTokenView;
    private LoginButton mLoginBtnDefault;
    private LoginButton mLoginBtnStyle2;
    private LoginButton mLoginBtnStyle3;
    private LoginoutButton mLoginoutBtnDefault;
    private LoginoutButton mLoginoutBtnSilver;
    
    /** ��½��֤��Ӧ��listener */
    private AuthListener mLoginListener = new AuthListener();
    /** �ǳ�������Ӧ��listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    /**
     * �ð�ť���ڼ�¼��ǰ���������һ�� Button�������� {@link #onActivityResult}
     * �����н������֡�ͨ������£����ǵ�Ӧ����ֻ��Ҫһ�����ʵ� {@link LoginButton} 
     * ���� {@link LoginoutButton} ���ɡ�
     */
    private Button mCurrentClickedButton;
    
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_logout);
        mTokenView = (TextView) findViewById(R.id.result);

        // ������Ȩ��֤��Ϣ
        AuthInfo authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        /**
         * ��½��ť
         */
        // ��½��ť��Ĭ����ʽ��
        mLoginBtnDefault = (LoginButton) findViewById(R.id.login_button_default);
        mLoginBtnDefault.setWeiboAuthInfo(authInfo, mLoginListener);
        //mLoginBtnStyle2.setStyle(LoginButton.LOGIN_INCON_STYLE_1);

        // ��½��ť����ʽ����
        mLoginBtnStyle2 = (LoginButton) findViewById(R.id.login_button_style1);
        mLoginBtnStyle2.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginBtnStyle2.setStyle(LoginButton.LOGIN_INCON_STYLE_2);
        
        // ��½��ť����ʽ����:
        // ��ע�⣺����ʽû�а��µ�Ч��
        mLoginBtnStyle3 = (LoginButton) findViewById(R.id.login_button_style2);
        mLoginBtnStyle3.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginBtnStyle3.setStyle(LoginButton.LOGIN_INCON_STYLE_3);
        
        /**
         * ��¼/ע����ť
         */
        // ��¼/ע����ť��Ĭ����ʽ����ɫ��
        mLoginoutBtnDefault = (LoginoutButton) findViewById(R.id.login_out_button_default);
        mLoginoutBtnDefault.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginoutBtnDefault.setLogoutListener(mLogoutListener);
        
        // ��½��ť����ʽ��������ɫ��
        mLoginoutBtnSilver = (LoginoutButton) findViewById(R.id.login_out_button_silver);
        mLoginoutBtnSilver.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginoutBtnSilver.setLogoutListener(mLogoutListener);
        // ���� LoginLogouButton �������� Token ��Ϣ����ˣ���������ڳ���
        // ����ý���ʱ�����øð�ť��ʾ"ע��"����ſ����´���
        //Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
        //mLoginoutBtnSilver.setLogoutInfo(token, mLogoutListener);
        
        /**
         * ע����ť���ð�ťδ���κη�װ��ֱ�ӵ��ö�Ӧ API �ӿ�
         */
        final Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutAPI(AccessTokenKeeper.readAccessToken(WBLoginLogoutActivity.this)).logout(mLogoutListener);
            }
        });
        
        /**
         * ��ע�⣺Ϊÿ�� Button ����һ������� Listener ֻ��Ϊ�˼�¼��ǰ�����
         * ����һ�� Button�������� {@link #onActivityResult} �����н������֡�
         * ͨ������£����ǵ�Ӧ�ò���Ҫ���øú�����
         */
        mLoginBtnDefault.setExternalOnClickListener(mButtonClickListener);
        mLoginBtnStyle2.setExternalOnClickListener(mButtonClickListener);
        mLoginBtnStyle3.setExternalOnClickListener(mButtonClickListener);
        mLoginoutBtnDefault.setExternalOnClickListener(mButtonClickListener);
        mLoginoutBtnSilver.setExternalOnClickListener(mButtonClickListener);
    }

    /**
     * �� SSO ��Ȩ Activity �˳�ʱ���ú��������á�
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (mCurrentClickedButton != null) {
            if (mCurrentClickedButton instanceof LoginButton) {
                ((LoginButton)mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            } else if (mCurrentClickedButton instanceof LoginoutButton) {
                ((LoginoutButton)mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            }
        }
        
        /*
        if (mLoginBtnDefault != null) {
			mLoginBtnDefault.onActivityResult(requestCode, resultCode, data);
		}
        */
        
        /*
        if (mLoginBtnStyle2 != null) {
            mLoginBtnStyle2.onActivityResult(requestCode, resultCode, data);
        }
        */
        
        /*
        if (mLoginBtnStyle3 != null) {
            mLoginBtnStyle3.onActivityResult(requestCode, resultCode, data);
        }
        */
        
        /*
        if (mLoginoutBtnDefault != null) {
        	mLoginoutBtnDefault.onActivityResult(requestCode, resultCode, data);
		}
		*/
        
        /*
        if (mLoginoutBtnSilver != null) {
            mLoginoutBtnSilver.onActivityResult(requestCode, resultCode, data);
        }
        */
    }
    
    /**
     * ��ע�⣺Ϊÿ�� Button ����һ������� Listener ֻ��Ϊ�˼�¼��ǰ�����
     * ����һ�� Button�������� {@link #onActivityResult} �����н������֡�
     * ͨ������£����ǵ�Ӧ�ò���Ҫ����� Listener��
     */
    private OnClickListener mButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                mCurrentClickedButton = (Button)v;
            }
        }
    };

    /**
     * ���밴ť�ļ�������������Ȩ�����
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                mTokenView.setText(String.format(format, accessToken.getToken(), date));

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBLoginLogoutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBLoginLogoutActivity.this, 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * �ǳ���ť�ļ����������յǳ�����������API �������ļ�������
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(WBLoginLogoutActivity.this);
                        mTokenView.setText(R.string.weibosdk_demo_logout_success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }     

		@Override
		public void onWeiboException(WeiboException e) {
			mTokenView.setText(R.string.weibosdk_demo_logout_failed);
		}
    }
}
