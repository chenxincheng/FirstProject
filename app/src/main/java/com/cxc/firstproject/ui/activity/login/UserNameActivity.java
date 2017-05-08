package com.cxc.firstproject.ui.activity.login;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.cxc.firstproject.R;
import com.cxc.firstproject.base.BaseTitleActivity;
import com.cxc.firstproject.utils.AppManager;
import com.cxc.firstproject.utils.PerfectClickListener;
import com.cxc.firstproject.utils.SPUtils;
import com.cxc.firstproject.view.EditTextWithDel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：chenxincheng on 2017/4/27.
 * 邮件：chenxincheng@xwtec.cn
 */

public class UserNameActivity extends BaseTitleActivity {
    Button openbt;
    EditTextWithDel username;

    @Override
    public void initData() {
        openbt = (Button) findViewById(R.id.openbt);
        username = (EditTextWithDel) findViewById(R.id.user) ;
        setToolBar(toolbarBaseActivity, "设置用户名");
        openbt.setOnClickListener(listener);
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_username;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }
    PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            if(!TextUtils.isEmpty(username.getText().toString().trim())){
                showScuess("设置成功");
                SPUtils.put(UserNameActivity.this,"username",username.getText().toString().trim());
                startActivity(new Intent(UserNameActivity.this,LoginActivity.class));
                AppManager.getAppManager().finishActivity(LoginActivity.class);
                finish();
            }else{
                showWarn("请输入用户名");
            }
        }
    };
}
