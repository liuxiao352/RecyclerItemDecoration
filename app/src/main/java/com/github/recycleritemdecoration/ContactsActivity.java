package com.github.recycleritemdecoration;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @data 2018-09-26
 * @desc
 */

public class ContactsActivity extends AppCompatActivity {

    private WaveSideBar mSideBar;
    private LinearLayout mLltProgress;
    private RecyclerView mRecyclerView;
    private ContactsAdapter mAdapter;
    private static final int REQUEST_CODE_READ_CONTACTS = 101;
    private MyHandler mHandler;
    private LinearLayoutManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mHandler = new MyHandler(this);
        mSideBar = findViewById(R.id.side_bar);
        mLltProgress = findViewById(R.id.llt_progress);
        mRecyclerView = findViewById(R.id.recyclerView);
        initAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) { //没有打开权限 提示用户去打开权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
            }else{
                getContacts();
            }
        }
        mSideBar.setIndexItems("A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#");
        mSideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                int position = getScrollPosition(index);
                mManager.scrollToPositionWithOffset(position, 0);
            }
        });
    }

    private void initAdapter() {
        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.addItemDecoration(new SectionDecoration(this, new DecorationCallback() {
            @Override
            public long getGroupId(int position) {
                ArrayList<ContactInfo> data = mAdapter.getData();
                ContactInfo contactInfo = data.get(position);
                String firstLetter = PinYinUtils.getFirstLetter(contactInfo.getName());
                return firstLetter.hashCode();
            }

            @Override
            public String getGroupFirstLine(int position) {
                ArrayList<ContactInfo> data = mAdapter.getData();
                ContactInfo contactInfo = data.get(position);
                String firstLetter = PinYinUtils.getFirstLetter(contactInfo.getName());
                return firstLetter;
            }
        }));
        mAdapter = new ContactsAdapter(new ArrayList<ContactInfo>());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void hintProgress(){
        mLltProgress.setVisibility(View.GONE);
    }

    public void setNewData(ArrayList<ContactInfo> infos){
        mAdapter.setNewData(infos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //打开了权限
                getContacts();
            }else{ //权限被拒绝
                Toast.makeText(this, "读取联系人的权限被拒绝, 当前功能无法使用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContacts(){
        mLltProgress.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ContactInfo> infos = ContactsUtils.getAllContacts(getApplicationContext());
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", infos);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }).start();
    }


    public static class MyHandler extends Handler{
        private WeakReference<ContactsActivity> mActivity;
        public MyHandler(ContactsActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            ArrayList<ContactInfo> infos = data.getParcelableArrayList("list");
            Log.e("test", "infos : " + infos.size());
            Collections.sort(infos, new Comparator<ContactInfo>() {
                @Override
                public int compare(ContactInfo contactInfo, ContactInfo t1) {
                    String name1 = contactInfo.getName();
                    String name2 = t1.getName();
                    String firstLetter1 = PinYinUtils.getFirstLetter(name1);
                    String firstLetter2 = PinYinUtils.getFirstLetter(name2);
                    boolean c1Flag = firstLetter1.equals("#"); // 不是字母
                    boolean c2Flag = firstLetter2.equals("#"); // 不是字母
                    if (c1Flag && !c2Flag) {
                        return 1;
                    } else if (!c1Flag && c2Flag) {
                        return -1;
                    }
                    return firstLetter1.hashCode() - firstLetter2.hashCode();
                }
            });
            ContactsActivity activity = mActivity.get();
            if (activity != null) {
                activity.setNewData(infos);
                activity.hintProgress();
            }
        }
    }

    /**
     * 获得对应的位置
     * @param character
     * @return
     */
    public int getScrollPosition(String character) {
        ArrayList<ContactInfo> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getName();
            if (PinYinUtils.getFirstLetter(name).equals(character)) {
                return i;
            }
        }
        return -1; // -1不会滑动
    }
}
