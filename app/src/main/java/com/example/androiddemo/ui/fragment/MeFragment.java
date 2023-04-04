package com.example.androiddemo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.example.androiddemo.R;
import com.example.androiddemo.app.BaseApplication;
import com.example.androiddemo.app.BaseFragment;
import com.example.androiddemo.app.MainActivity;
import com.example.androiddemo.bean.UserBean;
import com.example.androiddemo.bean.VoteBean;
import com.example.androiddemo.db.DBHelper;
import com.example.androiddemo.ui.activity.EditInfoActivity;
import com.example.androiddemo.ui.activity.ModifyPwdActivity;
import com.example.androiddemo.ui.activity.MsgActivity;
import com.example.androiddemo.ui.activity.SettingActivity;
import com.example.androiddemo.ui.activity.VoteTakeNotesActivity;
import com.tencent.mmkv.MMKV;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvName, tvTakeNotes, mTvId;
    private ImageView ivAvatar, redView;
    private UserBean mUserBean;
    private Handler mHandler = new Handler(Looper.getMainLooper()); // 全局变量
    private ArrayList<VoteBean> newMsgBean = new ArrayList<>();

    /**
     * 轮询器 开启定时任务 查询是否有新的提醒信息
     */
    private Runnable mTimeCounterRunnable = new Runnable() {
        @Override
        public void run() {
            /**
             * 每隔10秒查询一次数据
             * 1，区分用户跟管理员
             * 2，用户的话就查询出自己可参与的投票，还有已经参与的投票
             * 3，管理员的话就查询出自己创建过的投票
             */
            newMsgBean = new ArrayList<>();
            List<VoteBean> voteBeans;
            if (BaseApplication.userBean.getIs_user() != 1) {
                //如果是管理员就获取该管理员所创建的投票数据
                voteBeans = DBHelper.getInstance(getContext()).queryMeCreateVote();
                for (int i = 0; i < voteBeans.size(); i++) {
                    VoteBean data = voteBeans.get(i);
                    if (data.getMsg_dying_period().isEmpty() && isEffectiveDate(data.getEnd_time()))
                        updateData(data, "您参与的投票还有30分钟就结束", 2);
                    if (data.getEnd_time() < Calendar.getInstance().getTimeInMillis())
                        updateData(data, "您参与的投票已结束", 3);
                }
            } else {
                //如果是用户的话就查询出自己可参与的投票，还有已经参与的投票
                voteBeans = DBHelper.getInstance(getContext()).queryAllVote();
                Iterator<VoteBean> iterator = voteBeans.iterator();
                while (iterator.hasNext()) {
                    boolean isContain = false;
                    VoteBean data = iterator.next();
                    String[] split = data.getUsers().split(",");
                    for (int i = 0; i < split.length; i++) {
                        if ((BaseApplication.userBean.get_id() + "").equals(split[i]))
                            isContain = true;
                    }
                    if (!isContain) {
                        //不允许的投票
                        iterator.remove();
                    } else {
                        if (data.getMsg_contain_me().isEmpty()) {
                            updateData(data, "您有新的可参与投票主题", 1);
                        } else {
                            data.setMsg_time(data.getStart_time());
                            newMsgBean.add(data);
                        }
                        boolean effectiveDate = isEffectiveDate(data.getEnd_time());
                        if (data.getMsg_dying_period().isEmpty() && effectiveDate)
                            updateData(data, "您参与的投票还有30分钟就结束", 2);
                        if (data.getMsg_expire().isEmpty() && data.getEnd_time() < Calendar.getInstance().getTimeInMillis())
                            updateData(data, "您参与的投票已结束", 3);
                    }
                }
            }

            //延迟10秒后再次执行run方法
            mHandler.postDelayed(mTimeCounterRunnable, 10 * 1000);
        }
    };

    /**
     * @param data    投票主题
     * @param content 提醒的内容
     * @param type    提醒的类型 =1 有可参与的投票 =2 即将到期 =3 到期
     */
    private void updateData(VoteBean data, String content, int type) {
        mmkv.putBoolean("isNewMsg", true);
        //提醒消息的时间
        switch (type) {
            case 1:
                data.setMsg_contain_me(content);
                data.setMsg_time(data.getStart_time());
                break;
            case 2:
                data.setMsg_dying_period(content);
                data.setMsg_time(data.getEnd_time() - 1000 * 60 * 30);
                break;
            case 3:
                data.setMsg_expire(content);
                data.setMsg_time(data.getEnd_time());
                break;
        }
        //更新数据库
        DBHelper.getInstance(requireContext()).updateVote(data);
        newMsgBean.add(data);
        redView.setVisibility(mmkv.getBoolean("isNewMsg", false) ? View.VISIBLE : View.GONE);
    }

    private MMKV mmkv;

    @Override
    public void initEvent() {
        contentView.findViewById(R.id.ivSetting).setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        contentView.findViewById(R.id.rlTakeNotes).setOnClickListener(this);
        contentView.findViewById(R.id.rlInfo).setOnClickListener(this);
        contentView.findViewById(R.id.rlPwd).setOnClickListener(this);
        contentView.findViewById(R.id.rlMsg).setOnClickListener(this);
    }

    @Override
    protected void initView() {
        mTvName = contentView.findViewById(R.id.tvName);
        tvTakeNotes = contentView.findViewById(R.id.tvTakeNotes);
        mTvId = contentView.findViewById(R.id.tvId);
        ivAvatar = contentView.findViewById(R.id.ivAvatar);
        redView = contentView.findViewById(R.id.redView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserBean = DBHelper.getInstance(requireContext()).queryUser(1);
        mTvName.setText(mUserBean.getStudent_name());
        mTvId.setText("ID: " + mUserBean.get_id());
        if (mUserBean.getAvatarUrl() != null && !"".equals(mUserBean.getAvatarUrl()))
            Glide.with(this).load(mUserBean.getAvatarUrl()).into(ivAvatar);
        tvTakeNotes.setText(BaseApplication.userBean.getIs_user() == 1 ? "我参与的投票" : "我创建的投票");
    }

    @Override
    protected void initContent(Bundle savedInstanceState) {
        mmkv = MMKV.defaultMMKV();
        //开启轮询
        mHandler.postDelayed(mTimeCounterRunnable, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSetting:
                startActivity(new Intent(requireContext(), SettingActivity.class));
                break;
            case R.id.ivAvatar:
                //单选
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .start(requireActivity(), ((MainActivity) requireActivity()).REQUEST_CODE_CHOOSE); // 打开相册
                break;
            case R.id.rlTakeNotes:
                startActivity(new Intent(getContext(), VoteTakeNotesActivity.class));
                break;
            case R.id.rlInfo:
                startActivity(new Intent(getContext(), EditInfoActivity.class));
                break;
            case R.id.rlPwd:
                startActivity(new Intent(getContext(), ModifyPwdActivity.class));
                break;
            case R.id.rlMsg:
                //投票提醒
                Intent intent = new Intent(getContext(), MsgActivity.class);
                intent.putExtra("data", newMsgBean);
                startActivity(intent);
                break;
        }
    }

    /**
     * 设置用户头像
     *
     * @param url 图片地址
     */
    public void setAvatar(String url) {
        Glide.with(this).load(url).into(ivAvatar);
        //入库
        DBHelper.getInstance(requireContext()).updateUser(mUserBean.get_id(), url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭定时任务
        mHandler.removeCallbacks(mTimeCounterRunnable);
    }

    public static boolean isEffectiveDate(Long time) {
        Long setTime = Long.valueOf(1000 * 60 * 30);//1秒*60*30
        Date date = new Date();
        return date.getTime() - time <= setTime && date.getTime() > time;
    }
}
