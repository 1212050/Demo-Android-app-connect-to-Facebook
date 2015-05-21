package com.example.duy.drawer2;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.actions.Cursor;
import com.sromku.simple.fb.entities.Like;
import com.sromku.simple.fb.listeners.OnLikesListener;
import com.sromku.simple.fb.utils.Utils;

import java.util.List;


public class GetLikes extends ActionBarActivity {

    private TextView mResult;
    private TextView mMore;
    private String mAllPages = "";
    SimpleFacebook mSimpleFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_likes);

        mResult = (TextView)findViewById(R.id.result);
        mMore = (TextView)findViewById(R.id.load_more);
        mMore.setPaintFlags(mMore.getPaint().getFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mAllPages = "";
        mResult.setText(mAllPages);

        String entityId = "14104316802_522484207864952";
        SimpleFacebook.getInstance().getLikes(entityId, new OnLikesListener() {

            @Override
            public void onThinking() {
               // showDialog();
            }

            @Override
            public void onException(Throwable throwable) {
                //hideDialog();
                mResult.setText(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                //hideDialog();
                mResult.setText(reason);
            }

            @Override
            public void onComplete(List<Like> response) {
                //hideDialog();
                // make the result more readable.
                mAllPages += "<u>\u25B7\u25B7\u25B7 (paging) #" + getPageNum() + " \u25C1\u25C1\u25C1</u><br>";
                mAllPages += Utils.join(response.iterator(), "<br>", new Utils.Process<Like>() {
                    @Override
                    public String process(Like like) {
                        return "\u25CF " + like.getUser().getName() + " \u25CF";
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_likes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableLoadMore(final Cursor<List<Like>> cursor) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
