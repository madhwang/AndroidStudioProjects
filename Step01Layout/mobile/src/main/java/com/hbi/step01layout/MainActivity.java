package com.hbi.step01layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


/*
화면을 제어하는 Main Activity  클래스
activity_main.xml 을 전개해서  view 를 만든 후 해당  View  를 컨트롤하는  Activity 클래스 이다.
 */
public class MainActivity extends Activity {

    //액티비티가 호출되는 클래스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //res > Layout > activity_main.xml 을 전개해서  View 구성하기
        setContentView(R.layout.activity_main);
    }

    public void push(View v)
    {
        Toast.makeText(this,"버튼을 눌렀네요",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
