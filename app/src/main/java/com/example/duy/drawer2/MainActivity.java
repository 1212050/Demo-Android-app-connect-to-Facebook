package com.example.duy.drawer2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Group;
import com.sromku.simple.fb.listeners.OnGroupsListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    ArrayList<String> dataArray_right=new ArrayList<String>();

    ArrayList<Object> objectArray_right=new ArrayList<Object>();

    ArrayList<String> dataArray_left=new ArrayList<String>();

    ArrayList<Object> objectArray_left=new ArrayList<Object>();


    DrawerLayout mDrawerlayout;
    ListView mDrawerList_Left,mDrawerList_Right;
    ActionBarDrawerToggle mDrawerToggle;
    ImageButton imgLeftMenu,imgRightMenu;


    ListItemsAdapter_Left Left_Adapter;
    ListItemsAdapter_Right Right_Adapter;
    private TextView mResult;
    private TextView mMore;
    private String mAllPages = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = (TextView)findViewById(R.id.result);
        mMore = (TextView)findViewById(R.id.load_more);
        mMore.setPaintFlags(mMore.getPaint().getFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Intent i=getIntent();


        //===============Initialization of Variables=========================//

        mDrawerlayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList_Left=(ListView)findViewById(R.id.drawer_list_left);
        mDrawerList_Right=(ListView)findViewById(R.id.drawer_list_right);
        imgLeftMenu=(ImageButton)findViewById(R.id.imgLeftMenu);
        imgRightMenu=(ImageButton)findViewById(R.id.imgRightMenu);



        mDrawerlayout.setDrawerListener(mDrawerToggle);


        //============== Define a Custom Header for Navigation drawer=================//


        LayoutInflater inflator=(LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v=inflator.inflate(R.layout.header, null);



        imgLeftMenu=(ImageButton)v.findViewById(R.id.imgLeftMenu);
        imgRightMenu=(ImageButton)v.findViewById(R.id.imgRightMenu);

        getSupportActionBar().setHomeButtonEnabled(true);

        //	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayUseLogoEnabled(false);

        getSupportActionBar().setDisplayShowCustomEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1281A9")));

        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        getSupportActionBar().setCustomView(v);

        imgLeftMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mDrawerlayout.isDrawerOpen(mDrawerList_Right)){
                    mDrawerlayout.closeDrawer(mDrawerList_Right);
                }
                mDrawerlayout.openDrawer(mDrawerList_Left);
            }
        });


        imgRightMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDrawerlayout.isDrawerOpen(mDrawerList_Left)){
                    mDrawerlayout.closeDrawer(mDrawerList_Left);
                }
                mDrawerlayout.openDrawer(mDrawerList_Right);
            }
        });



        Fill_LeftList();
        Fill_RightList();

        RefreshListView();
        final Intent intent=new Intent(this, GetMovie.class);
        final Intent intent3=new Intent(this, GetTelevisions.class);
        mDrawerList_Left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1,
                                    int arg2,
                                    long arg3) {
                switch (arg2) {
                    case 0:
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(intent3);
                    default:
                        break;
                }
            }
        });

        final Intent intent2=new Intent(this, GetLikes.class);
        mDrawerList_Right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1,
                                    int arg2,
                                    long arg3) {
                switch (arg2) {
                    case 0:
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });

        mAllPages = "";
        mResult.setText(mAllPages);

        SimpleFacebook.getInstance().getGroups(new OnGroupsListener() {

            @Override
            public void onThinking() {
                //showDialog();
            }

            @Override
            public void onException(Throwable throwable) {
                //hideDialog();
                mResult.setText(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                //hideDialog();
                mResult.setText(reason+". Vui lòng trở lại và nhấn log in lần nữa.");
            }

            @Override
            public void onComplete(List<Group> response) {
                //hideDialog();
                // make the result readable.
                mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
                mAllPages += Utils.join(response.iterator(), "<br>", new Utils.Process<Group>() {
                    @Override
                    public String process(Group group) {
                        return "\u25CF " + group.getName() + " \u25CF";
                    }
                });
                mAllPages += "<br>";
                mResult.setText(Html.fromHtml(mAllPages));

                // check if more pages exist
                if (hasNext()) {
                    enableLoadMore(getCursor());
                } else {
                    disableLoadMore();
                }
            }
        });


    }


    private void enableLoadMore(final Cursor<List<Group>> cursor) {
        mMore.setVisibility(View.VISIBLE);
        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mAllPages += "<br>";
                cursor.next();
            }
        });
    }

    private void disableLoadMore() {
        mMore.setOnClickListener(null);
        mMore.setVisibility(View.INVISIBLE);
    }


    // Filling the ArrayLists


    public void RefreshListView() {
        objectArray_left.clear();
        for (int i = 0; i < dataArray_left.size(); i++) {
            Object obj = new Object();
            objectArray_left.add(obj);
        }
        Log.d("object array", "" + objectArray_left.size());
        Left_Adapter = new ListItemsAdapter_Left(objectArray_left, 1);
        mDrawerList_Left.setAdapter(Left_Adapter);



        objectArray_right.clear();
        for (int i = 0; i < dataArray_right.size(); i++) {
            Object obj = new Object();
            objectArray_right.add(obj);
        }
        Log.d("object array", "" + objectArray_right.size());
        Right_Adapter = new ListItemsAdapter_Right(objectArray_right, 1);
        mDrawerList_Right.setAdapter(Right_Adapter);

    }



    public void Fill_LeftList()
    {

        dataArray_left.clear();


        dataArray_left.add("Get Movies");
        dataArray_left.add("Get Televisions");


    }


    public void Fill_RightList()
    {
        dataArray_right.clear();

        dataArray_right.add("Get Likes");
        dataArray_right.add("Get Friends");
    }



    //  ==============   Left Listview Adapter Implementation;=====================//


    private class ListItemsAdapter_Left extends ArrayAdapter<Object>
    {
        ViewHolder holder1;

        public ListItemsAdapter_Left(List<Object>items,int x) {
            // TODO Auto-generated constructor stub
            super(MainActivity.this, android.R.layout.simple_list_item_single_choice, items);
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return dataArray_left.get(position);
        }

        public int getItemInteger(int pos)
        {
            return pos;

        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataArray_left.size();
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflator=getLayoutInflater();

            convertView=inflator.inflate(R.layout.data, null);



            holder1=new ViewHolder();

            holder1.text=(TextView)convertView.findViewById(R.id.txtData);



            holder1.iv=(ImageView)convertView.findViewById(R.id.imgView);


            convertView.setTag(holder1);

            String text=dataArray_left.get(position);
            holder1.text.setText(dataArray_left.get(position));





            return convertView;
        }

    }

    //=============Right Listview Adapter Implementation;================//


    private class ListItemsAdapter_Right extends ArrayAdapter<Object>
    {
        ViewHolder holder1;

        public ListItemsAdapter_Right(List<Object>items,int x) {
            // TODO Auto-generated constructor stub
            super(MainActivity.this, android.R.layout.simple_list_item_single_choice, items);
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return dataArray_right.get(position);
        }

        public int getItemInteger(int pos)
        {
            return pos;

        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataArray_right.size();
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflator=getLayoutInflater();

            convertView=inflator.inflate(R.layout.data, null);



            holder1=new ViewHolder();

            holder1.text=(TextView)convertView.findViewById(R.id.txtData);



            holder1.iv=(ImageView)convertView.findViewById(R.id.imgView);


            convertView.setTag(holder1);

            String text=dataArray_right.get(position);
            holder1.text.setText(dataArray_right.get(position));





            return convertView;
        }

    }



    private class ViewHolder {
        TextView text,textcounter;
        ImageView iv;

    }





}

