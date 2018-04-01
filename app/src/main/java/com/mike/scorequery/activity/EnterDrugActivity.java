package com.mike.scorequery.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.mike.scorequery.R;
import com.mike.scorequery.bean.Drug;
import com.mike.scorequery.utils.StringUtils;
import com.mike.scorequery.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class EnterDrugActivity extends BaseTitleBarActivity {
    private TextView et_enter_name_drug, tv_time;
    private EditText et_drug_num, et_drug_des, et_drug_point, et_enter_id_drug;
    private TimePickerView pickerView;


    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_drugenter, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTv_Title.setText("药品录入");
        mTv_Right.setText("确定");
        viewBy();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("scoreQrcode"))) {//二维码信息
            final String[] strings = StringUtils.split(getIntent().getStringExtra("scoreQrcode"), "|");
            if (strings.length == 3) {
                et_enter_id_drug.setText(strings[1]);
                et_enter_name_drug.setText(strings[2]);
            }
        }
    }

private void viewBy(){
        et_enter_name_drug=(TextView)findViewById(R.id.et_enter_name_drug);
        tv_time=(TextView)findViewById(R.id.tv_time);
        et_drug_num=(EditText)findViewById(R.id.et_drug_num);
        et_drug_des=(EditText)findViewById(R.id.et_drug_des);
        et_drug_point=(EditText)findViewById(R.id.et_drug_point);
        et_enter_id_drug=(EditText)findViewById(R.id.et_enter_id_drug);
        tv_time.setOnClickListener(this);
        }

@Override
public void onClick(View v){
        super.onClick(v);
        if(v==tv_time){
        initTime(v);
        }else if(v==mTv_Right){
        Drug drug=new Drug();
        drug.drug_name=et_enter_name_drug.getText().toString();
        drug.drug_time=tv_time.getText().toString();
        drug.drug_num=et_drug_num.getText().toString();
        drug.drug_des=et_drug_des.getText().toString();
        drug.drug_point=et_drug_point.getText().toString();
        drug.drug_id=et_enter_id_drug.getText().toString();
        showLoadingDialog();
        drug.save(new SaveListener<String>(){
@Override
public void done(String s,BmobException e){
        if(e==null){
        closeLoadingDialog();
        ToastUtil.showToast("录入成功");
        finish();
        }else{
        ToastUtil.showToast(e.getMessage());
        }
        }
        });
        }
        }


public void initTime(View v){
        Calendar startDate=Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate=Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        pickerView=new TimePickerView(this,TimePickerView.Type.YEAR_MONTH_DAY);
        pickerView.setTime(new Date());
//        pickerView.setRange(2018,2050);
        pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener(){
@Override
public void onTimeSelect(Date date){
        tv_time.setText(getTime(date));
        }
        });
        pickerView.show();
        }

@SuppressLint("SimpleDateFormat")
private String getTime(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        }
