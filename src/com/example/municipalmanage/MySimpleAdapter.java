package com.example.municipalmanage;


/** 
 *  
 */  
 
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;  
/** 
 * @author Himi 
 *  
 */  
public class MySimpleAdapter extends BaseAdapter {  
    private LayoutInflater mInflater;  
    private List<Map<String, Object>> list;  
    private int layoutID;  
    private String flag[];  
    private int ItemIDs[];  
    public MySimpleAdapter(Context context, List<Map<String, Object>> list,  
            int layoutID, String flag[], int ItemIDs[]) {  
        this.mInflater = LayoutInflater.from(context);  
        this.list = list;  
        this.layoutID = layoutID;  
        this.flag = flag;  
        this.ItemIDs = ItemIDs;  
    }  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return list.size();  
    }  
    @Override  
    public Object getItem(int arg0) {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
    @Override  
    public long getItemId(int arg0) {  
        // TODO Auto-generated method stub  
        return 0;  
    }  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        convertView = mInflater.inflate(layoutID, null);  
        for (int i = 0; i < flag.length; i++) {//��ע1  
            if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {  
                ImageView iv = (ImageView) convertView.findViewById(ItemIDs[i]);  
                iv.setBackgroundResource((Integer) list.get(position).get(  
                        flag[i]));  
            } else if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {  
                TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);  
                tv.setText((String) list.get(position).get(flag[i]));  
            }else{  
                //...��ע2  
            }  
        }  
        addListener(convertView);  
        return convertView;  
    }  
/** 
 * ͯЬ��ֻ��Ҫ����Ҫ���ü����¼������д�������ⷽ����Ϳ������� 
 * ��Ĳ���Ҫ�޸ģ� 
 * ��ע3 
 */  
    public void addListener(View convertView) {  
        ((ImageView)convertView.findViewById(R.id.question_image)).setOnClickListener(  
                new View.OnClickListener() {  
                    @Override  
                    public void onClick(View v) {  
                    
                    }  
                });  
        
    }  
} 