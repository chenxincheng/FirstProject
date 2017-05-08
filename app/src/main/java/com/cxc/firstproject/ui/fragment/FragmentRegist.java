package com.cxc.firstproject.ui.fragment;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxc.firstproject.R;
import com.cxc.firstproject.base.BaseNoTitleFragment;
import com.cxc.firstproject.ui.activity.login.UserNameActivity;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.utils.Tools;
import com.cxc.firstproject.view.EditTextWithDel;
import com.cxc.firstproject.view.mysnackbar.Prompt;
import com.cxc.firstproject.view.mysnackbar.TSnackbar;

import butterknife.Bind;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * 作者：chenxincheng on 2017/4/25. 邮件：chenxincheng@xwtec.cn
 */

public class FragmentRegist extends BaseNoTitleFragment {
    @Bind(R.id.next)
    Button nextBt;
    @Bind(R.id.userpassword)
    EditTextWithDel userpassword;
    @Bind(R.id.send_smscode)
    TextView sendsmscode;
    @Bind(R.id.userphone)
    EditTextWithDel userphone;
    @Bind(R.id.smscode)
    EditTextWithDel smscode;
    @Bind(R.id.fg_regist)
    LinearLayout fg_regist;
    @Bind(R.id.rela_rephone)
    RelativeLayout rela_rephone;
    @Bind(R.id.rela_recode)
    RelativeLayout rela_recode;
    @Bind(R.id.rela_repass)
    RelativeLayout rela_repass;
    @Bind(R.id.usericon)
    ImageView phoneIv;
    @Bind(R.id.keyicon)
    ImageView keyIv;
    @Bind(R.id.codeicon)
    ImageView passIv;

    MyCountTimer timer;
    private Handler handler = new Handler() {
    };
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_regist;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        TextListener();
        sendsmscode.setOnClickListener(listener);
        nextBt.setOnClickListener(listener);
    }

    PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
            case R.id.next:
                final String password = userpassword.getText().toString();
                String code = smscode.getText().toString();
                final String phone = userphone.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    rela_rephone.setBackground(getResources().getDrawable(
                            R.drawable.bg_border_color_cutmaincolor));
                    phoneIv.setAnimation(Tools.shakeAnimation(2));
                    showWarn("请输入手机号码");

                    return;
                }
                if (!isMobile(phone)) {
                    rela_rephone.setBackground(getResources().getDrawable(
                            R.drawable.bg_border_color_cutmaincolor));
                    phoneIv.setAnimation(Tools.shakeAnimation(2));
                    showWarn("提示：手机号不正确");
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    rela_recode.setBackground(getResources().getDrawable(
                            R.drawable.bg_border_color_cutmaincolor));
                    keyIv.setAnimation(Tools.shakeAnimation(2));
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    showWarn("提示：请输入验证码");
                    return;

                }
                if (TextUtils.isEmpty(password)) {
                    rela_repass.setBackground(getResources().getDrawable(
                            R.drawable.bg_border_color_cutmaincolor));
                    passIv.setAnimation(Tools.shakeAnimation(2));
                    // fg_regist.setBackgroundResource(R.color.colorAccent);
                    showWarn("提示：请输入密码");
                    return;
                }
                final TSnackbar snackbar = showLoading("正在注册");
                BmobSMS.verifySmsCode(getActivity(), "11位手机号码", "验证码",
                        new VerifySMSCodeListener() {

                            @Override
                            public void done(BmobException ex) {
                                // TODO Auto-generated method stub
//                                if (ex == null) {// 短信验证码已验证成功
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                    snackbar.setPromptThemBackground(
                                            Prompt.SUCCESS).setText("注册成功")
                                            .setDuration(TSnackbar.LENGTH_LONG)
                                            .show();
                                    SPUtils.put(getActivity(), "phone", phone);
                                    SPUtils.put(getActivity(), "passWord",
                                            password);
                                    startActivity(new Intent(getActivity(),
                                            UserNameActivity.class));
                                    }
                                }, 1500);
//                                } else {
//                                    snackbar.setPromptThemBackground(
//                                            Prompt.ERROR).setText("验证码填写错误")
//                                            .setDuration(TSnackbar.LENGTH_LONG)
//                                            .show();
//                                    rela_recode.setBackground(
//                                            getResources().getDrawable(
//                                                    R.drawable.bg_border_color_cutmaincolor));
//                                    keyIv.setAnimation(Tools.shakeAnimation(2));
////                                    Log.e("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
//                                }
                            }
                        });
                break;
            case R.id.send_smscode:
                String phones = userphone.getText().toString();
                boolean mobile = isMobile(phones);
                if (!TextUtils.isEmpty(phones)) {
                    if (mobile) {
                        timer = new MyCountTimer(60000, 1000);
                        timer.start();
                        BmobSMS.requestSMSCode(getActivity(), phones, "IYO短信",
                                new RequestSMSCodeListener() {

                                    @Override
                                    public void done(Integer smsId,
                                            BmobException ex) {
                                        if (ex == null) {// 验证码发送成功
                                            Log.i("smile", "短信id：" + smsId);
                                            showWarn("提示：短信发送成功");
                                        } else {
                                            showWarn("提示：短信发送失败" + "错误码"
                                                    + smsId);
                                        }
                                    }

                                });
                    } else {
                        rela_rephone.setBackground(getResources().getDrawable(
                                R.drawable.bg_border_color_cutmaincolor));
                        phoneIv.setAnimation(Tools.shakeAnimation(2));
                        showWarn("输入手机号码");
                    }

                } else {
                    rela_rephone.setBackground(getResources().getDrawable(
                            R.drawable.bg_border_color_cutmaincolor));
                    phoneIv.setAnimation(Tools.shakeAnimation(2));
                    showWarn("手机号码不正确");
                }

                break;
            }
        }
    };

    // 事件定时器
    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendsmscode.setText((millisUntilFinished / 1000) + "秒后重发");
            sendsmscode.setBackgroundResource(R.drawable.bg_button_noselector);
            sendsmscode.setClickable(false);
            sendsmscode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            sendsmscode.setText("重新发送");
            sendsmscode.setBackgroundResource(R.drawable.bg_button_selector);
            sendsmscode.setClickable(true);
            sendsmscode.setEnabled(true);
        }
    }

    // 回收timer
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void TextListener() {
        // 用户名改变背景变
        userphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = userphone.getText().toString();
                if (isMobile(text)) {
                    // 抖动
                    rela_rephone.setBackground(getResources()
                            .getDrawable(R.drawable.bg_border_color_black));

                }

            }
        });
        // 验证码改变背景变
        smscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                rela_recode.setBackground(getResources()
                        .getDrawable(R.drawable.bg_border_color_black));

            }
        });
        // 密码改变背景变
        userpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                rela_repass.setBackground(getResources()
                        .getDrawable(R.drawable.bg_border_color_black));

            }
        });
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
}
