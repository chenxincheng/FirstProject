package com.cxc.firstproject.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxc.firstproject.MainActivity;
import com.cxc.firstproject.R;
import com.cxc.firstproject.app.AppConstants;
import com.cxc.firstproject.base.BaseFragment;
import com.cxc.firstproject.base.BaseNoTitleFragment;
import com.cxc.firstproject.utils.AppManager;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.utils.Tools;
import com.cxc.firstproject.view.EditTextWithDel;
import com.cxc.firstproject.view.mysnackbar.Prompt;
import com.cxc.firstproject.view.mysnackbar.TSnackbar;

import butterknife.Bind;

/**
 * 作者：chenxincheng on 2017/4/25. 邮件：chenxincheng@xwtec.cn
 */

public class FragmentLogin extends BaseNoTitleFragment {
    @Bind(R.id.userph)
    EditTextWithDel userphone;
    @Bind(R.id.userpass)
    EditTextWithDel userpass;
    @Bind(R.id.bt_login)
    Button bt_login;
    @Bind(R.id.tv_forgetcode)
    TextView tv_forgetcode;
    @Bind(R.id.loginusericon)
    ImageView loginusericon;
    @Bind(R.id.codeicon)
    ImageView codeicon;
    @Bind(R.id.rela_name)
    RelativeLayout rela_name;
    @Bind(R.id.rela_pass)
    RelativeLayout rela_pass;



    private Handler handler = new Handler() {
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initLogin();
        textListener();
    }

    private void textListener() {
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
                    rela_name.setBackground(getResources()
                            .getDrawable(R.drawable.bg_border_color_black));

                }

            }
        });
        userpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                rela_pass.setBackground(getResources()
                        .getDrawable(R.drawable.bg_border_color_black));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // rela_pass.setBackground(getResources().getDrawable(R.drawable.bg_border_color_black));

            }
        });
    }

    private void initLogin() {
        ;
        userphone.setText(SPUtils.getString(getActivity(), "phone")+"");
        userpass.setText(SPUtils.getString(getActivity(), "passWord")+"");
        bt_login.setOnClickListener(listener);

    }

    PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            final String phone = userphone.getText().toString();
            final String passwords = userpass.getText().toString();
            final View view = v;

            if (TextUtils.isEmpty(phone)) {
                rela_name.setBackground(getResources().getDrawable(
                        R.drawable.bg_border_color_cutmaincolor));
                loginusericon.setAnimation(Tools.shakeAnimation(2));
                showWarn("IYO提示：请输入手机号码");
                return;
            }
            if (!isMobile(phone)) {
                // 抖动
                rela_name.setBackground(getResources().getDrawable(
                        R.drawable.bg_border_color_cutmaincolor));
                loginusericon.setAnimation(Tools.shakeAnimation(2));
                showWarn("IYO提示：用户名不正确");

                return;
            }
            if (TextUtils.isEmpty(passwords)) {
                rela_pass.setBackground(getResources().getDrawable(
                        R.drawable.bg_border_color_cutmaincolor));
                codeicon.setAnimation(Tools.shakeAnimation(2));
                showWarn("IYO提示：请输入密码");
                return;
            }
            if(TextUtils.isEmpty(SPUtils.getString(getActivity(), "phone"))){
                rela_name.setBackground(getResources().getDrawable(
                        R.drawable.bg_border_color_cutmaincolor));
                loginusericon.setAnimation(Tools.shakeAnimation(2));
                showWarn("IYO提示：用户名不正确");
                return;
            }
            rela_name.setBackground(getResources()
                    .getDrawable(R.drawable.bg_border_color_black));
            rela_name.setBackground(getResources()
                    .getDrawable(R.drawable.bg_border_color_black));
            final TSnackbar snackbar = showLoading("正在登录");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    snackbar.setPromptThemBackground(
                            Prompt.SUCCESS).setText("登录成功")
                            .setDuration(TSnackbar.LENGTH_LONG)
                            .show();
                    AppConstants.STATELONG = true;
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    AppManager.getAppManager().finishActivity(MainActivity.class);
                    getActivity().finish();
                }
            }, 1500);
        }
    };
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
