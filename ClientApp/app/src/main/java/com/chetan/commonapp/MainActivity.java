package com.chetan.commonapp;
import com.chetan.clientapp.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.chetan.commonapp.balanceServiceCommon.DailyCash;
import com.chetan.commonapp.balanceServiceCommon.BalanceService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    private BalanceService common;
    private ServiceConnection serviceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            common = BalanceService.Stub.asInterface(service);
            Toast.makeText(getApplicationContext(),	"Service Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            common = null;
            Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (common == null) {
            Intent it = new Intent("com.chetan.service.AIDL");
            bindService(convertImplicitIntentToExplicitIntent(it), serviceCon, Context.BIND_AUTO_CREATE);
        }


    }

    public Intent convertImplicitIntentToExplicitIntent(Intent implicitIntent){
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent,0);
        if (resolveInfoList == null || resolveInfoList.size() != 1){
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        ComponentName component = new ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public void createDB(View view){
        try {
            boolean result = common.createDatabase();
            if (result == true){
                Toast.makeText(getApplicationContext(),	"True", Toast.LENGTH_SHORT).show();
                Log.i("TAG","DB is true ");
                findViewById(R.id.edityyyy).setEnabled(true);
                findViewById(R.id.editmm).setEnabled(true);
                findViewById(R.id.editdd).setEnabled(true);
                findViewById(R.id.editworkingdays).setEnabled(true);
                findViewById(R.id.qryDB).setEnabled(true);
            }
            else{
                Toast.makeText(getApplicationContext(),	"False", Toast.LENGTH_SHORT).show();
                Log.i("TAG","DB is false ");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void queryDB(View view){
        ArrayList<String> array_list = new ArrayList<String>();
        EditText edityyyy = findViewById(R.id.edityyyy);
        EditText editmm = findViewById(R.id.editmm);
        EditText editdd = findViewById(R.id.editdd);
        EditText editworking = findViewById(R.id.editworkingdays);
        try {
            int yyyy = Integer.parseInt(edityyyy.getText().toString());
            int dd = Integer.parseInt(editdd.getText().toString());
            int mm = Integer.parseInt(editmm.getText().toString());
            int workingDays = Integer.parseInt(editworking.getText().toString());

            if ((edityyyy.getText().toString().matches("")) || (editmm.getText().toString().matches("")) || (editdd.getText().toString().matches("")) || (editworking.getText().toString().matches(""))){
                Toast.makeText(getApplicationContext(),	"Empty field", Toast.LENGTH_SHORT).show();
            }
            else if ((yyyy == 2017 || yyyy == 2018) && (mm <= 12 || mm >= 1 ) && (dd >= 1 || dd <= 31) && (workingDays >= 1 || workingDays <= 30)) {
                if (mm == 2 && dd > 28){
                    Toast.makeText(getApplicationContext(),	"Date should be less than or equal to 28", Toast.LENGTH_SHORT).show();
                }
                else{

                    int[] workingDate = dateChecker(yyyy, mm, dd);
                    //DailyCash[] result1 = common.dailyCash(yyyy, mm, dd, workingDays);
                    DailyCash[] result1 = common.dailyCash(workingDate[0], workingDate[1], workingDate[2], workingDays);


                    for (int i = 0; i < result1.length; i++){

                        String v = String.valueOf(result1[i].getmYear())+"-"+String.valueOf(result1[i].getmMonth())+"-"+String.valueOf(result1[i].getmDay())+"   " +result1[i].getmDayOfWeek()+"%$"+ String.valueOf(result1[i].getmCash());
                        array_list.add(v);
                        Log.i("TAG", "Value  is " + v);
                    }

                    if (array_list.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No Data present", Toast.LENGTH_SHORT).show();
                    }else {

                        Intent intent = new Intent(this, SecondActivity.class);

                        intent.putExtra("array_list", array_list);

                        startActivity(intent);
                    }
                }

            }
            else{
                Toast.makeText(getApplicationContext(),	"Not valid input", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.i("TAG", "ERROR is " + e);
            Toast.makeText(getApplicationContext(),	"No data in DB for the date", Toast.LENGTH_SHORT).show();
        }

    }


    public int[] dateChecker(int yyyy, int mm, int dd){
        Calendar startDate = Calendar.getInstance();
        int[] return_date_array;
        startDate.set(yyyy, mm-1, dd);
        if (startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )  {
            startDate.add(Calendar.DATE, 1);

            if (startDate.get(Calendar.YEAR) == 2018 && startDate.get(Calendar.MONTH) == 0 && startDate.get(Calendar.DAY_OF_MONTH) == 1){
                startDate.add(Calendar.DATE, 1);
            }
            return_date_array = new int[]{startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH)};

        }
        else if (startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            startDate.add(Calendar.DATE, 2);

            if (startDate.get(Calendar.YEAR) == 2018 && startDate.get(Calendar.MONTH) == 0 && startDate.get(Calendar.DAY_OF_MONTH) == 1){
                startDate.add(Calendar.DATE, 1);
            }
            return_date_array = new int[]{startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH)};

        }
        else{

            return_date_array = new int[]{startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH)};
        }

        return return_date_array;
    }
}
